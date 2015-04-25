package edu.utdallas.seers.bre.javabre.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.controller.writer.BTWriter;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.entity.words.bt.VarBT;
import edu.utdallas.seers.bre.javabre.util.Utils;
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
	private Set<Term> sysTerms;

	public BTController(String[] sourceFolders, String[] classPaths,
			File inBtFile, File outFile, String[] processFolders, File sysFile)
			throws IOException {
		sysTerms = Utils.readTermsFile(sysFile);
		this.sourceFolders = sourceFolders;
		this.classPaths = classPaths;
		this.inBtFile = inBtFile;
		this.processFolders = processFolders;

		encodings = new String[sourceFolders.length];
		for (int i = 0; i < sourceFolders.length; i++) {
			encodings[i] = "UTF-8";
		}

		astVisitor = new VariablesVisitor(sysTerms);
		this.writer = new BTWriter(outFile);
	}

	public void processRules() throws Exception {

		Set<Term> bTerms = Utils.readTermsFile(inBtFile);
		astVisitor.setbTerms(bTerms);

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		Utils.setParserConf(parser, encodings, sourceFolders, classPaths);

		for (String srcFolder : processFolders) {
			File folder = new File(srcFolder);
			processFolder(parser, folder);
		}

		// -----------------------

		HashMap<Term, HashMap<String, Set<VarBT>>> matches = astVisitor
				.getBtMatches();
		writer.writeBtMatches(matches);

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

		astVisitor.setCu(cu);
		astVisitor.setFile(file);
		cu.accept(astVisitor);

	}

	public void close() throws IOException {
		writer.close();
	}

}
