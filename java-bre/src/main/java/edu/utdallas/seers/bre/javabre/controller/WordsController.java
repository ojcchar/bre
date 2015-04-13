package edu.utdallas.seers.bre.javabre.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.entity.WordData;
import edu.utdallas.seers.bre.javabre.visitor.IdentifiersVisitor;

public class WordsController {

	private static Logger LOGGER = LoggerFactory
			.getLogger(WordsController.class);
	private String[] sourceFolders;
	private String[] classPaths;
	private String[] encodings;
	private IdentifiersVisitor astVisitor;
	private WordsWriter writer;

	public WordsController(String[] sourceFolders, String[] classPaths,
			File outFilePatt, File outFileData) throws IOException {
		this.sourceFolders = sourceFolders;
		this.classPaths = classPaths;

		encodings = new String[sourceFolders.length];
		for (int i = 0; i < sourceFolders.length; i++) {
			encodings[i] = "UTF-8";
		}

		astVisitor = new IdentifiersVisitor();
		this.writer = new WordsWriter(outFilePatt, outFileData);
	}

	public void processRules() throws Exception {

		ASTParser parser = createParser();

		for (String srcFolder : sourceFolders) {
			File folder = new File(srcFolder);
			processFolder(parser, folder);
		}

		// -----------------------

		HashMap<String, Integer> identPatterns = astVisitor.getIdentPatterns();
		writer.writePatterns(identPatterns);

		// -----------------------

		HashMap<String, HashMap<String, WordData>> data = astVisitor.getData();
		writer.writeData(data);

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

		char[] fileContent = readFile(file);
		parser.setUnitName(file.getName());
		parser.setSource(fileContent);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		setParserConf(parser);

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		IProblem[] problems = cu.getProblems();

		for (IProblem problem : problems) {
			if (problem.isError()) {
				LOGGER.debug(problem.toString() + " - "
						+ problem.getSourceLineNumber());
			}
		}
		// ---------------------

		astVisitor.setCu(cu);
		astVisitor.setFile(file);
		cu.accept(astVisitor);

	}

	private ASTParser createParser() {
		ASTParser parser = ASTParser.newParser(AST.JLS8);

		setParserConf(parser);

		return parser;
	}

	private void setParserConf(ASTParser parser) {
		@SuppressWarnings("unchecked")
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);

		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.setCompilerOptions(options);
		parser.setResolveBindings(true);

		parser.setEnvironment(classPaths, sourceFolders, encodings, true);
	}

	private char[] readFile(File path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path.getAbsolutePath()));
		return new String(encoded, Charset.defaultCharset()).toCharArray();
	}

	public void close() throws IOException {
		writer.close();
	}

}
