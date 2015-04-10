package edu.utdallas.seers.bre.java_bre;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 *
 */
public class App {
	public static void main(String args[]) throws IOException {
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);

		String file = "test_data//DoubtFulDebtPickEditLines.java";
		char[] charArray = readFile(file);

		parser.setSource(charArray);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		ASTVisitor astVisitor = new ASTVisitor() {

			Set<String> names = new HashSet<String>();

			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				System.out.println("Declaration of '" + name + "' at line "
						+ cu.getLineNumber(name.getStartPosition()));
				return false; // do not continue to avoid usage info
			}

			public boolean visit(SimpleName node) {
				if (this.names.contains(node.getIdentifier())) {
					System.out.println("Usage of '" + node + "' at line "
							+ cu.getLineNumber(node.getStartPosition()));
				}
				return true;
			}

		};

		cu.accept(astVisitor);
	}

	private static char[] readFile(String path)
			throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, Charset.defaultCharset()).toCharArray();
	}
}
