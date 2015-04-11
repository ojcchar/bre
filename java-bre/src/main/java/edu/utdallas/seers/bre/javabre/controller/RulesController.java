package edu.utdallas.seers.bre.javabre.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.entity.TypeDcl;
import edu.utdallas.seers.bre.javabre.extractor.CategorizationEnumExtractor;
import edu.utdallas.seers.bre.javabre.extractor.RuleExtractor;
import edu.utdallas.seers.bre.javabre.extractor.SymbolicLiteralExtractor;
import edu.utdallas.seers.bre.javabre.extractor.ValidValExtractor;
import edu.utdallas.seers.bre.javabre.visitor.GeneralVisitor;

public class RulesController {

	private static Logger LOGGER = LoggerFactory
			.getLogger(RulesController.class);
	private HashMap<TypeDcl, List<TypeDeclaration>> subclasses = new HashMap<TypeDcl, List<TypeDeclaration>>();
	private HashMap<String, JavaFileInfo> classesInfo = new HashMap<String, JavaFileInfo>();
	private String[] sourceFolders;
	private String[] classPaths;
	private RulesWriter writer;

	public RulesController(String[] sourceFolders, String[] classPaths,
			File outFile) throws IOException {
		this.sourceFolders = sourceFolders;
		this.classPaths = classPaths;
		this.writer = new RulesWriter(outFile);
	}

	public void processRules() throws Exception {

		ASTParser parser = createParser();
		parser.setEnvironment(classPaths, sourceFolders,
				new String[] { "UTF-8" }, true);

		for (String srcFolder : sourceFolders) {
			File folder = new File(srcFolder);
			processFolder(parser, folder);
		}

		CategorizationEnumExtractor ex = new CategorizationEnumExtractor();
		List<BusinessRule> rules = ex.extract(subclasses, classesInfo);

		writer.writeRules(rules);

	}

	private void processFolder(ASTParser parser, File folder)
			throws IOException {

		if (!folder.isDirectory()) {
			LOGGER.warn("The file " + folder
					+ " is not a directory, skipping it...");
			return;
		}

		File[] listFiles = folder.listFiles();

		for (File file : listFiles) {
			if (file.isDirectory()) {
				processFolder(parser, file);
			} else {
				processFile(parser, file);
			}
		}

	}

	private void processFile(ASTParser parser, File file) throws IOException {

		if (!file.getName().endsWith(".java")) {
			LOGGER.warn("The file " + file
					+ " is not a java file, skipping it...");
			return;
		}

		LOGGER.info("Processing: " + file.getName());

		char[] fileContent = readFile(file);
		parser.setUnitName(file.getName());
		parser.setSource(fileContent);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		parser.setResolveBindings(true);
		parser.setEnvironment(classPaths, sourceFolders,
				new String[] { "UTF-8" }, true);

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		// ---------------------

		GeneralVisitor astVisitor = new GeneralVisitor();
		cu.accept(astVisitor);
		JavaFileInfo fileInfo = astVisitor.getFileInfo();
		fileInfo.setFile(file);
		fileInfo.setCompilUnit(cu);

		updateHierarchy(fileInfo);

		RuleExtractor extractor = new SymbolicLiteralExtractor();
		List<BusinessRule> rules = extractor.extract(fileInfo);
		writer.writeRules(rules);

		extractor = new ValidValExtractor();
		List<BusinessRule> rules2 = extractor.extract(fileInfo);
		// System.out.println(rules2);
		writer.writeRules(rules2);
	}

	@SuppressWarnings({ "unchecked" })
	private void updateHierarchy(JavaFileInfo fileInfo) {

		List<TypeDeclaration> classes = fileInfo.getClasses();

		for (TypeDeclaration cl : classes) {

			Type superclType = cl.getSuperclassType();
			List<Type> superintTypes = cl.superInterfaceTypes();

			ITypeBinding subClassBind = cl.resolveBinding();

			classesInfo.put(subClassBind.getQualifiedName(), fileInfo);

			// super class
			if (superclType != null) {
				String qualName = superclType.resolveBinding()
						.getQualifiedName();

				TypeDcl typeDcl = new TypeDcl(qualName,
						TypeDcl.TypeDclType.CLASS, superclType.resolveBinding()
								.getName());
				List<TypeDeclaration> subCl = subclasses.get(typeDcl);
				if (subCl == null) {
					subCl = new ArrayList<TypeDeclaration>();
					subclasses.put(typeDcl, subCl);
				}
				subCl.add(cl);
			}

			// super interfaces
			if (!superintTypes.isEmpty()) {
				for (Type type : superintTypes) {
					String qualName2 = type.resolveBinding().getQualifiedName();

					TypeDcl typeDcl2 = new TypeDcl(qualName2,
							TypeDcl.TypeDclType.INTERFACE, type
									.resolveBinding().getName());
					List<TypeDeclaration> subCl2 = subclasses.get(typeDcl2);
					if (subCl2 == null) {
						subCl2 = new ArrayList<TypeDeclaration>();
						subclasses.put(typeDcl2, subCl2);
					}
					subCl2.add(cl);
				}
			}

		}

	}

	private ASTParser createParser() {
		ASTParser parser = ASTParser.newParser(AST.JLS8);

		@SuppressWarnings("unchecked")
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				JavaCore.VERSION_1_6);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);

		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.setCompilerOptions(options);

		return parser;
	}

	private char[] readFile(File path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path.getAbsolutePath()));
		return new String(encoded, Charset.defaultCharset()).toCharArray();
	}

	public void close() throws IOException {
		writer.close();
	}

}
