package edu.utdallas.seers.bre.javabre.visitor;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;

import edu.utdallas.seers.bre.javabre.entity.words.Token;

public class MethodInvVisitor extends ASTVisitor {

	private List<Token> tokens;
	private boolean dependOnIf = false;
	private CompilationUnit cu;
	private int lineNumber;
	private MethodInvocation mInv;

	public MethodInvVisitor(List<Token> tokens, CompilationUnit cu,
			int lineNumber, MethodInvocation mInv) {
		this.tokens = tokens;
		this.cu = cu;
		this.lineNumber = lineNumber;
		this.mInv = mInv;
	}

	@Override
	public boolean visit(IfStatement node) {
		int lineNumber2 = cu.getLineNumber(node.getStartPosition());

		if (lineNumber2 > lineNumber) {
			return false;
		}

		// if (!mInv.getParent().equals(node)) {
		// return false;
		// }

		String expStr = node.getExpression().toString();
		for (Token token : tokens) {
			if (expStr.toLowerCase().contains(token.getWord())) {
				dependOnIf = true;
				break;
			}
		}

		return false;
	}

	public boolean doesDependOnIf() {
		return dependOnIf;
	}

}
