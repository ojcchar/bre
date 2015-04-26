package edu.utdallas.seers.bre.javabre.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.controller.writer.RulesWriter;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.entity.TypeDcl;
import edu.utdallas.seers.bre.javabre.entity.words.Token;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.util.Utils;
import edu.utdallas.seers.bre.javabre.visitor.IfCondVisitor;
import edu.utdallas.seers.bre.javabre.visitor.ServletVisitor;

public class ServletController {

	private static Logger LOGGER = LoggerFactory
			.getLogger(ServletController.class);
	private HashMap<TypeDcl, List<TypeDeclaration>> subclasses = new HashMap<TypeDcl, List<TypeDeclaration>>();
	private HashMap<String, JavaFileInfo> classesInfo = new HashMap<String, JavaFileInfo>();
	private String[] sourceFolders;
	private String[] classPaths;
	private RulesWriter writer;
	private String[] encodings;
	private Set<Term> businessTerms;
	private String[] processFolders;
	private Set<Term> sysTerms;

	public ServletController(String[] sourceFolders, String[] classPaths,
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

//		 LOGGER.info("Processing: " + file.getName());

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

		ServletVisitor astVisitor = new ServletVisitor();
		cu.accept(astVisitor);
		JavaFileInfo fileInfo = astVisitor.getFileInfo();

		if (fileInfo != null) {
			List<MethodInvocation> methodInvoc = fileInfo.getMethodInvoc();
			processMethodInv(methodInvoc);

			// processIfs(file, fileInfo);
		}
	}

	private void processIfs(File file, JavaFileInfo fileInfo) {
		List<IfStatement> ifStmts = fileInfo.getIfStmts();

		for (IfStatement ifSt : ifStmts) {

			IfCondVisitor vis = new IfCondVisitor(businessTerms, sysTerms);
			ifSt.getExpression().accept(vis);

			if (!vis.isInv()) {
				System.out.println("\""
						+ file.getName()
						+ "\";\""
						+ ifSt.getExpression().toString().replace("\n", "\\n")
						+ "\";\""
						+ ifSt.getThenStatement().toString()
								.replace("\n", "\\n") + "\"");
			}
		}
	}

	private void processMethodInv(List<MethodInvocation> methodInvoc) {

		for (MethodInvocation mInv : methodInvoc) {
			IMethodBinding bind = mInv.resolveMethodBinding();
			String clName = bind.getDeclaringClass().getName();
			
			String t1 = Utils.getNLText(clName);
			List<Token> tokensTerm = NLPProcessor.getInstance().processText(mInv.getName().toString(), true);
			if (tokensTerm.get(0).getPos().startsWith("V")) {
				
				String t2 = Utils.getNLTokens(tokensTerm);
				
				System.out.println(t1 + " " + t2);
			}
		}

	}

	public void close() throws IOException {
		writer.close();
	}

}
