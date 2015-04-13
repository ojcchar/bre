package edu.utdallas.seers.bre.javabre.main.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import edu.utdallas.seers.bre.javabre.visitor.TestVisitor;

public class MainTest {

	public static void main(String[] args) throws IOException {
		ASTParser parser = createParser();
		String[] classPaths = { "test_data\\test_dummy" };
		String[] sourceFolders = { "test_data\\test_dummy" };
		parser.setEnvironment(classPaths, sourceFolders,
				new String[] { "UTF-8" }, true);

		File file = new File("test_data\\test_dummy\\org\\SubClass1.java");
		char[] fileContent = readFile(file);
		parser.setUnitName(file.getName());
		parser.setSource(fileContent);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		IProblem[] problems = cu.getProblems();

		for (IProblem problem : problems) {
			System.out.println(problem.toString() + " - "
					+ problem.getSourceLineNumber());

		}

		TestVisitor astVisitor = new TestVisitor(cu);
		cu.accept(astVisitor);

		// ----------------------
		// setConf(parser);
		parser.setResolveBindings(true);

		// parser.setEnvironment(classPaths, sourceFolders,
		// new String[] { "UTF-8" }, true);

		file = new File("test_data\\test_dummy\\org\\SubClass1.java");
		fileContent = readFile(file);
		parser.setUnitName(file.getName());
		parser.setSource(fileContent);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		cu = (CompilationUnit) parser.createAST(null);

		problems = cu.getProblems();

		for (IProblem problem : problems) {
			System.out.println(problem.toString() + " - "
					+ problem.getSourceLineNumber());

		}

		astVisitor = new TestVisitor(cu);
		cu.accept(astVisitor);

	}

	private static char[] readFile(File path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path.getAbsolutePath()));
		return new String(encoded, Charset.defaultCharset()).toCharArray();
	}

	private static ASTParser createParser() {
		ASTParser parser = ASTParser.newParser(AST.JLS8);

		setConf(parser);

		return parser;
	}

	private static void setConf(ASTParser parser) {
		@SuppressWarnings("unchecked")
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);

		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.setCompilerOptions(options);
	}

}
