package edu.utdallas.seers.bre.javabre.main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.entity.BussinesRule;
import edu.utdallas.seers.bre.javabre.entity.JavaFileInfo;
import edu.utdallas.seers.bre.javabre.extractor.RuleExtractor;
import edu.utdallas.seers.bre.javabre.extractor.SymbolicLiteralExtractor;
import edu.utdallas.seers.bre.javabre.visitor.GeneralVisitor;

/**
 *
 */
public class MainBRE {

	private static Logger LOGGER = LoggerFactory.getLogger(MainBRE.class);

	public static void main(String args[]) throws IOException {

		LOGGER.debug("test");

		File file = new File("test_data//test_dummy//SubClass1.java");

		ASTParser parser = createParser();

		String[] classpathEntries = { "test_data//test_dummy" };
		String[] sourcepathEntries = { "test_data//test_dummy" };
		String[] encodings = { "UTF-8" };
		parser.setEnvironment(classpathEntries, sourcepathEntries, encodings,
				true);
		char[] charArray = readFile(file);
		parser.setUnitName(file.getName());

		parser.setSource(charArray);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		if (cu.getAST().hasBindingsRecovery()) {
			System.out.println("Binding activated.");
		}

		// ---------------------

		GeneralVisitor astVisitor = new GeneralVisitor();
		cu.accept(astVisitor);
		JavaFileInfo fileInfo = astVisitor.getFileInfo();
		fileInfo.setFile(file);
		System.out.println(fileInfo);
		
		RuleExtractor extractor = new SymbolicLiteralExtractor();
		List<BussinesRule> rules = extractor.extract(fileInfo);
		System.out.println(rules);
	
	}

	private static ASTParser createParser() {
		ASTParser parser = ASTParser.newParser(AST.JLS8);

		@SuppressWarnings("unchecked")
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				JavaCore.VERSION_1_6);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);

		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.setCompilerOptions(options);

		return parser;
	}

	private static char[] readFile(File path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path.getAbsolutePath()));
		return new String(encoded, Charset.defaultCharset()).toCharArray();
	}
}
