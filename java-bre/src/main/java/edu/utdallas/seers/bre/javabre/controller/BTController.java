package edu.utdallas.seers.bre.javabre.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.controller.writer.BTWriter;
import edu.utdallas.seers.bre.javabre.entity.words.Token;
import edu.utdallas.seers.bre.javabre.entity.words.bt.BusTerm;
import edu.utdallas.seers.bre.javabre.entity.words.bt.VarBT;
import edu.utdallas.seers.bre.javabre.visitor.VariablesVisitor;

public class BTController {

	private static Logger LOGGER = LoggerFactory.getLogger(BTController.class);
	private String[] sourceFolders;
	private String[] classPaths;
	private String[] encodings;
	private VariablesVisitor astVisitor;
	private BTWriter writer;
	private File inBtFile;
	private String[] processFolders;

	public BTController(String[] sourceFolders, String[] classPaths,
			File inBtFile, File outFile, String[] processFolders)
			throws IOException {
		this.sourceFolders = sourceFolders;
		this.classPaths = classPaths;
		this.inBtFile = inBtFile;
		this.processFolders = processFolders;

		encodings = new String[sourceFolders.length];
		for (int i = 0; i < sourceFolders.length; i++) {
			encodings[i] = "UTF-8";
		}

		astVisitor = new VariablesVisitor();
		this.writer = new BTWriter(outFile);
	}

	public void processRules() throws Exception {

		Set<BusTerm> bTerms = readBTerms();
		astVisitor.setbTerms(bTerms);

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		setParserConf(parser);

		for (String srcFolder : processFolders) {
			File folder = new File(srcFolder);
			processFolder(parser, folder);
		}

		// -----------------------

		HashMap<BusTerm, HashMap<String, Set<VarBT>>> matches = astVisitor
				.getBtMatches();
		writer.writeBtMatches(matches);

	}

	private Set<BusTerm> readBTerms() throws IOException {

		Set<BusTerm> terms = new HashSet<BusTerm>();

		int id = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(inBtFile))) {
			for (String line; (line = br.readLine()) != null;) {

				List<Token> tokens = NLPProcessor.getInstance().processText(
						line, false);
				if (hasNouns(tokens)) {
					terms.add(new BusTerm(id++, tokens));
				}
			}
		}

		return terms;
	}

	private static final List<String> NOUNS = Arrays.asList(new String[] {
			"NN", "NNS", "NNP", "NNPS" });

	private boolean hasNouns(List<Token> tokens) {

		for (Token token : tokens) {
			if (NOUNS.contains(token.getPos())) {
				return true;
			}
		}
		return false;
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
				LOGGER.error(problem.toString() + " - "
						+ problem.getSourceLineNumber());
			}
		}
		// ---------------------

		astVisitor.setCu(cu);
		astVisitor.setFile(file);
		cu.accept(astVisitor);

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
