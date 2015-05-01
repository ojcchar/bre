package edu.utdallas.seers.bre.javabre.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.controller.writer.RulesWriter;
import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.BusinessRule.RuleType;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.entity.TypeDcl;
import edu.utdallas.seers.bre.javabre.entity.words.Token;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.util.Utils;
import edu.utdallas.seers.bre.javabre.visitor.ServletVisitor;
import edu.utdallas.seers.bre.javabre.visitor.ServletVisitor2;

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
		businessTerms = Utils.readTermsFile(bussFile);
		sysTerms = Utils.readTermsFile(sysFile);
		this.writer = new RulesWriter(outFile, sysTerms);
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
			processFolder(parser, folder, true);
		}

		System.out
				.println("-----------------------------------------------------");

		for (String srcFolder : processFolders) {
			File folder = new File(srcFolder);
			processFolder(parser, folder, false);
		}

	}

	private void processFolder(ASTParser parser, File folder, boolean procServ)
			throws IOException {

		if (!folder.isDirectory()) {
			LOGGER.warn("The file " + folder
					+ " is not a directory, skipping it...");
			return;
		}

		File[] listFiles = folder.listFiles();

		for (File file : listFiles) {
			if (file.isDirectory()) {
				processFolder(parser, file, procServ);
			} else {
				processFile(parser, file, procServ);
			}
		}

	}

	private void processFile(ASTParser parser, File file, boolean procServ)
			throws IOException {

		if (!file.getName().endsWith(".java")) {
			// LOGGER.warn("The file " + file
			// + " is not a java file, skipping it...");
			return;
		}

		// LOGGER.info("Processing: " + file.getName());

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

		JavaFileInfo fileInfo = null;
		if (procServ) {
			ServletVisitor astVisitor = new ServletVisitor();
			cu.accept(astVisitor);
			fileInfo = astVisitor.getFileInfo();
			if (fileInfo != null) {
				List<MethodInvocation> methodInvoc = fileInfo.getMethodInvoc();
				processMethodInv(methodInvoc, file, procServ, fileInfo, cu);

				// processIfs(file, fileInfo);
			}
		} else {
			ServletVisitor2 astVisitor = new ServletVisitor2(clsMethods);
			cu.accept(astVisitor);
			fileInfo = astVisitor.getFileInfo();
			if (fileInfo != null) {
				List<MethodInvocation> methodInvoc = fileInfo.getMethodInvoc();
				processMethodInv(methodInvoc, file, procServ, fileInfo, cu);
				// processIfs(file, fileInfo);
			}
		}
	}

	// private void processIfs(File file, JavaFileInfo fileInfo) {
	// List<IfStatement> ifStmts = fileInfo.getIfStmts();
	//
	// for (IfStatement ifSt : ifStmts) {
	//
	// IfCondVisitor vis = new IfCondVisitor(businessTerms, sysTerms);
	// ifSt.getExpression().accept(vis);
	//
	// if (!vis.isInv()) {
	// System.out.println("\""
	// + file.getName()
	// + "\";\""
	// + ifSt.getExpression().toString().replace("\n", "\\n")
	// + "\";\""
	// + ifSt.getThenStatement().toString()
	// .replace("\n", "\\n") + "\"");
	// }
	// }
	// }

	HashSet<String> texts = new HashSet<String>();
	HashMap<String, List<String>> clsMethods = new HashMap<String, List<String>>();

	private void processMethodInv(List<MethodInvocation> methodInvoc,
			File file, boolean procServ, JavaFileInfo fileInfo,
			CompilationUnit cu) {

		for (MethodInvocation mInv : methodInvoc) {
			IMethodBinding bind = mInv.resolveMethodBinding();

			String qualifiedName = bind.getDeclaringClass().getQualifiedName();
			if (qualifiedName.startsWith("java.")) {
				continue;
			}

			String clName = bind.getDeclaringClass().getName();

			if (file.getName().endsWith(clName + ".java")) {
				continue;
			}
			// if (Utils.isTermContained(clName, sysTerms, false)) {
			if (Utils.isInValidIdent(clName, businessTerms, sysTerms)) {
				continue;
			}

			String t1 = Utils.bracketizeStr(Utils.getNLText(clName));
			List<Token> tokensTerm = NLPProcessor.getInstance().processText(
					mInv.getName().toString(), true);
			if (tokensTerm.get(0).getPos().startsWith("V")
					&& !tokensTerm.get(0).getWord().equals("is")
					&& (!tokensTerm.get(0).getWord().startsWith("set") && !tokensTerm
							.get(0).getWord().startsWith("get"))) {

				String t2 = Utils.getNLTokens(tokensTerm);
				String text = t1 + " " + t2;

				if (texts.add(text)) {

					// -------------------------------------------------------

					List<IfStatement> ifStmts2 = getIfStmts(mInv);

					for (IfStatement ifSt : ifStmts2) {
						String bText = text + " if [" + ifSt.getExpression()
								+ "]";
						BusinessRule rule = new BusinessRule(bText,
								RuleType.ACTIVITY_PRECOND);
						rule.addLocation(file,
								cu.getLineNumber(ifSt.getStartPosition()));

						System.out
								.println(Arrays.toString(rule.toStringArray()));
					}

					// -------------------------------------------------------
					//
					// List<IfStatement> ifStmts = fileInfo.getIfStmts();
					//
					// String mInvStrNoPunct = mInv.toString().replaceAll(
					// "[^a-zA-Z ]", " ");
					// List<Token> tokens = NLPProcessor.getInstance()
					// .processText(mInvStrNoPunct, true);
					// int lineNumber =
					// cu.getLineNumber(mInv.getStartPosition());
					// for (IfStatement ifSt : ifStmts) {
					// MethodInvVisitor vis = new MethodInvVisitor(tokens, cu,
					// lineNumber, mInv);
					// ifSt.accept(vis);
					// boolean b = vis.doesDependOnIf();
					//
					// if (b) {
					// String bText = text + " if ["
					// + ifSt.getExpression() + "]";
					// BusinessRule rule = new BusinessRule(bText,
					// RuleType.ACTIVITY_PRECOND);
					// rule.addLocation(file,
					// cu.getLineNumber(ifSt.getStartPosition()));
					//
					// System.out.println(Arrays.toString(rule
					// .toStringArray()));
					// }
					// }

					// MethodInvVisitor vis = new MethodInvVisitor(mInv, );

					if (procServ) {
						List<String> list = clsMethods.get(qualifiedName);
						if (list == null) {
							list = new ArrayList<String>();
							clsMethods.put(qualifiedName, list);
						}
						list.add(mInv.getName().toString());
					}
					// System.out.println(file.getName() + ";" + text);
				}
			}
		}

	}

	private List<IfStatement> getIfStmts(MethodInvocation mInv) {
		ASTNode parent = mInv.getParent();

		ArrayList<IfStatement> arrayList = new ArrayList<IfStatement>();
		getIfStmts(parent, arrayList);

		return arrayList;
	}

	private void getIfStmts(ASTNode node, ArrayList<IfStatement> arrayList) {

		if (node == null) {
			return;
		}

		if (node instanceof IfStatement) {
			arrayList.add((IfStatement) node);
		}

		getIfStmts(node.getParent(), arrayList);
	}

	public void close() throws IOException {
		writer.close();
	}

}
