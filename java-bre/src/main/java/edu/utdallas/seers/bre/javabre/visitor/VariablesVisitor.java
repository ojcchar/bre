package edu.utdallas.seers.bre.javabre.visitor;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;

import edu.utdallas.seers.bre.javabre.controller.NLPProcessor;
import edu.utdallas.seers.bre.javabre.entity.words.Token;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.entity.words.bt.VarBT;
import edu.utdallas.seers.bre.javabre.util.Utils;

public class VariablesVisitor extends ASTVisitor {

	private final static List<Integer> NODE_TYPES = Arrays
			.asList(new Integer[] { ASTNode.VARIABLE_DECLARATION_FRAGMENT,
					ASTNode.SINGLE_VARIABLE_DECLARATION });
	private CompilationUnit cu;
	private File file;

	private Set<Term> bTerms;

	// pos -> [ lemma -> freq,statement ]
	private HashMap<Term, HashMap<String, Set<VarBT>>> btMatches;
	private Set<Term> sysTerms;

	public VariablesVisitor(Set<Term> sysTerms2) {
		this.sysTerms = sysTerms2;
		setBtMatches(new HashMap<Term, HashMap<String, Set<VarBT>>>());
	}

	@Override
	public boolean visit(SimpleName node) {
		String identifier = node.toString();
		int parentType = node.getParent().getNodeType();

		if (!IdentifiersVisitor.JAVA_KEYWORDS
				.contains(identifier.toLowerCase())
				&& NODE_TYPES.contains(parentType) && identifier.length() > 2) {

			findMatches(identifier, node);
		}

		return super.visit(node);
	}

	private void findMatches(String identifier, SimpleName node) {

		boolean sys = Utils.isTermContained(identifier, sysTerms, false);
		Term bTerm = Utils.getTermContained(identifier, bTerms);

		boolean invalid = sys || bTerm == null;

		if (!invalid) {
			List<Token> tokens = NLPProcessor.getInstance().processText(
					identifier, true);

			HashMap<String, Set<VarBT>> map = this.getBtMatches().get(bTerm);

			if (map == null) {
				map = new HashMap<String, Set<VarBT>>();
				getBtMatches().put(bTerm, map);
			}
			Set<VarBT> list = map.get(file.getAbsolutePath());
			if (list == null) {
				list = new HashSet<VarBT>();
				map.put(file.getAbsolutePath(), list);
			}
			list.add(new VarBT(tokens, identifier));

		}

	}

	// private boolean contains(BusTerm bTerm, List<Token> tokens) {
	// List<Token> tokens2 = bTerm.getTokens();
	//
	// if (tokens2.size() != tokens.size()) {
	// return false;
	// }
	//
	// for (int i = 0; i < tokens2.size(); i++) {
	// if (!tokens2.get(i).getLemma()
	// .equalsIgnoreCase(tokens.get(i).getLemma())) {
	// return false;
	// }
	// }
	//
	// return true;
	// }

	public CompilationUnit getCu() {
		return cu;
	}

	public void setCu(CompilationUnit cu) {
		this.cu = cu;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Set<Term> getbTerms() {
		return bTerms;
	}

	public void setbTerms(Set<Term> bTerms) {
		this.bTerms = bTerms;
	}

	public HashMap<Term, HashMap<String, Set<VarBT>>> getBtMatches() {
		return btMatches;
	}

	public void setBtMatches(
			HashMap<Term, HashMap<String, Set<VarBT>>> btMatches) {
		this.btMatches = btMatches;
	}

}
