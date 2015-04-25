package edu.utdallas.seers.bre.javabre.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.SimpleName;

public class IFHelperVisitor extends ASTVisitor {

	private boolean useVar = false;
	private String bVar;

	public IFHelperVisitor(String bVar) {
		this.bVar = bVar;
	}

	@Override
	public boolean visit(IfStatement node) {
		Expression expression = node.getExpression();
		expression.accept(this);
		// FIXME: it should be true
		return false;
	}

	@Override
	public boolean visit(SimpleName node) {
		if (node.toString().equals(bVar)) {
			useVar = true;
		}
		return false;
	}

	public boolean useVar() {
		return useVar;
	}

}
