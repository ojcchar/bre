package edu.utdallas.seers.bre.javabre.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.controller.writer.IFWriter;
import edu.utdallas.seers.bre.javabre.entity.IFstmt;
import edu.utdallas.seers.bre.javabre.util.Utils;
import edu.utdallas.seers.bre.javabre.visitor.IFVisitor;

public class IFController {

	private static Logger LOGGER = LoggerFactory.getLogger(IFController.class);
	private String[] sourceFolders;
	private String[] classPaths;
	private String[] encodings;
	private IFVisitor astVisitor;
	private IFWriter writer;
	private File inVarsFile;
	private String[] processFolders;
	private HashMap<String, Set<String>> bVars;

	public IFController(String[] sourceFolders, String[] classPaths,
			File inVarsFile, File outFile, String[] processFolders)
			throws IOException {
		this.sourceFolders = sourceFolders;
		this.classPaths = classPaths;
		this.inVarsFile = inVarsFile;
		this.processFolders = processFolders;

		encodings = new String[sourceFolders.length];
		for (int i = 0; i < sourceFolders.length; i++) {
			encodings[i] = "UTF-8";
		}

		astVisitor = new IFVisitor();
		this.writer = new IFWriter(outFile);

		bVars = readBVars();
	}

	public void processRules() throws Exception {

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		Utils.setParserConf(parser, encodings, sourceFolders, classPaths);

		for (String srcFolder : processFolders) {
			File folder = new File(srcFolder);
			processFolder(parser, folder);
		}

	}

	private HashMap<String, Set<String>> readBVars() throws IOException {

		HashMap<String, Set<String>> fileVars = new HashMap<String, Set<String>>();

		try (BufferedReader br = new BufferedReader(new FileReader(inVarsFile))) {
			String line = br.readLine();
			while ((line = br.readLine()) != null) {

				String[] split = line.split(";");
				String filePath = split[1].replaceAll("\"", "");
				String var = split[2].replaceAll("\"", "");
				
				Set<String> vars = fileVars.get(filePath);
				if (vars == null) {
					vars = new HashSet<String>();
					fileVars.put(filePath, vars);
				}
				vars.add(var);
			}
		}

		return fileVars;
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
		Set<String> bVars2 = bVars.get(file.getAbsolutePath());

		if (bVars2 == null) {
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
		astVisitor.setbVars(bVars2);
		cu.accept(astVisitor);

		// -----------------------

		HashMap<String, Set<IFstmt>> ifSt = astVisitor.getIfStmts();
		writer.writeIfStmts(file, ifSt);

	}

	public void close() throws IOException {
		writer.close();
	}

}
