package edu.utdallas.seers.bre.javabre.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.controller.writer.RulesWriter;
import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.entity.TypeDcl;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.extractor.CategEnumConstExtractor;
import edu.utdallas.seers.bre.javabre.extractor.CategorizationEnumExtractor;
import edu.utdallas.seers.bre.javabre.extractor.EnumExtractor;
import edu.utdallas.seers.bre.javabre.extractor.IfElseIfExtractor;
import edu.utdallas.seers.bre.javabre.extractor.RuleExtractor;
import edu.utdallas.seers.bre.javabre.extractor.SwitchStmtExtractor;
import edu.utdallas.seers.bre.javabre.extractor.SymbolicLiteralExtractor;
import edu.utdallas.seers.bre.javabre.extractor.ValidValExtractor;
import edu.utdallas.seers.bre.javabre.util.Utils;
import edu.utdallas.seers.bre.javabre.visitor.GeneralVisitor;

public class RulesController {

	private static Logger LOGGER = LoggerFactory
			.getLogger(RulesController.class);
	private HashMap<TypeDcl, List<TypeDeclaration>> subclasses = new HashMap<TypeDcl, List<TypeDeclaration>>();
	private HashMap<String, JavaFileInfo> classesInfo = new HashMap<String, JavaFileInfo>();
	private String[] sourceFolders;
	private String[] classPaths;
	private RulesWriter writer;
	private String[] encodings;
	private Set<Term> businessTerms;
	private String[] processFolders;
	private Set<Term> sysTerms;

	public RulesController(String[] sourceFolders, String[] classPaths,
			File outFile, File bussFile, String[] processFolders, File sysFile)
			throws IOException {
		this.writer = new RulesWriter(outFile);
		businessTerms = Utils.readTermsFile(bussFile);
		sysTerms = Utils.readTermsFile(sysFile);
		this.sourceFolders = sourceFolders;
		this.classPaths = classPaths;
		this.processFolders = processFolders;

		encodings = new String[sourceFolders.length];
		for (int i = 0; i < sourceFolders.length; i++) {
			encodings[i] = "UTF-8";
		}
	}

	public void processRules() throws Exception {

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		Utils.setParserConf(parser, encodings, sourceFolders, classPaths);

		for (String srcFolder : processFolders) {
			File folder = new File(srcFolder);
			processFolder(parser, folder);
		}

		CategorizationEnumExtractor ex = new CategorizationEnumExtractor(
				businessTerms, sysTerms);
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
			// LOGGER.warn("The file " + file
			// + " is not a java file, skipping it...");
			return;
		}

		LOGGER.info("Processing: " + file.getName());

		char[] fileContent = Utils.readFile(file);
		parser.setUnitName(file.getName());
		parser.setSource(fileContent);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		Utils.setParserConf(parser, encodings, sourceFolders, classPaths);

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		IProblem[] problems = cu.getProblems();

		for (IProblem problem : problems) {
			if (problem.isError()) {
				LOGGER.error(problem.toString() + " - "
						+ problem.getSourceLineNumber());
			}
		}
		// ---------------------

		GeneralVisitor astVisitor = new GeneralVisitor();
		cu.accept(astVisitor);
		JavaFileInfo fileInfo = astVisitor.getFileInfo();
		fileInfo.setFile(file);
		fileInfo.setCompilUnit(cu);

		updateHierarchy(fileInfo);
//
//		CategEnumConstExtractor extractor2 = new CategEnumConstExtractor(
//				businessTerms, sysTerms);
//		List<BusinessRule> rules3 = extractor2.extract(fileInfo);
//		writer.writeRules(rules3);
//
//		RuleExtractor extractor = new SymbolicLiteralExtractor(businessTerms,
//				sysTerms, extractor2.getConstsRules());
//		List<BusinessRule> rules = extractor.extract(fileInfo);
//		writer.writeRules(rules);
//
//		extractor = new ValidValExtractor(businessTerms, sysTerms);
//		List<BusinessRule> rules2 = extractor.extract(fileInfo);
//		// System.out.println(rules2);
//		writer.writeRules(rules2);
//
//	}
		RuleExtractor extractor;
		
//		RuleExtractor extractor = new SymbolicLiteralExtractor(businessTerms, sysTerms);
//		List<BusinessRule> rules = extractor.extract(fileInfo);
//		writer.writeRules(rules);
//
//		extractor = new ValidValExtractor(businessTerms, sysTerms);
//		List<BusinessRule> rules2 = extractor.extract(fileInfo);
//		// System.out.println(rules2);
//		writer.writeRules(rules2);
//		
//		extractor = new CategEnumConstExtractor();
//		List<BusinessRule> rules3 = extractor.extract(fileInfo);
//		writer.writeRules(rules3);
		
		extractor = new SwitchStmtExtractor(businessTerms, sysTerms);
		List<BusinessRule> rules4 = extractor.extract(fileInfo);
		writer.writeRules(rules4);

		extractor = new EnumExtractor(businessTerms, sysTerms);
		List<BusinessRule> rules5 = extractor.extract(fileInfo);
		writer.writeRules(rules5);
		
		extractor = new IfElseIfExtractor(businessTerms, sysTerms);
		List<BusinessRule> rules6 = extractor.extract(fileInfo);
		writer.writeRules(rules6);	
	}
	
	@SuppressWarnings({ "unchecked" })
	private void updateHierarchy(JavaFileInfo fileInfo) {

		List<TypeDeclaration> classes = fileInfo.getClasses();

		for (TypeDeclaration cl : classes) {

			ITypeBinding subClassBind = cl.resolveBinding();
			classesInfo.put(subClassBind.getQualifiedName(), fileInfo);

			Type superclType = cl.getSuperclassType();
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

			List<Type> superintTypes = cl.superInterfaceTypes();
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

	public void close() throws IOException {
		writer.close();
	}

}
