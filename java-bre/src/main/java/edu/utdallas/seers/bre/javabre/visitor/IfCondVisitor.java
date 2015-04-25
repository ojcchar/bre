package edu.utdallas.seers.bre.javabre.visitor;

import java.util.Set;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleName;

import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.util.Utils;

public class IfCondVisitor extends ASTVisitor {

	private Set<Term> businessTerms;
	private Set<Term> sysTerms;
	private boolean isInv;

	public IfCondVisitor(Set<Term> businessTerms, Set<Term> sysTerms) {
		this.businessTerms = businessTerms;
		this.sysTerms = sysTerms;
	}

	@Override
	public boolean visit(SimpleName node) {
		if (Utils.isTermContained(node.toString(), sysTerms, false) && !isInv) {
			// if (Utils.isInValidIdent(node.toString(), businessTerms,
			// sysTerms)
			// && !isInv) {
			isInv = true;
		}
		return super.visit(node);
	}

	public boolean isInv() {
		return isInv;
	}

}
