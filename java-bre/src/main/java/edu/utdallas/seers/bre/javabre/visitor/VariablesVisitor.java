package edu.utdallas.seers.bre.javabre.visitor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;

import edu.utdallas.seers.bre.javabre.controller.NLPProcessor;
import edu.utdallas.seers.bre.javabre.entity.words.Token;
import edu.utdallas.seers.bre.javabre.entity.words.bt.BusTerm;
import edu.utdallas.seers.bre.javabre.entity.words.bt.VarBT;

public class VariablesVisitor extends ASTVisitor {

	private final static List<Integer> NODE_TYPES = Arrays
			.asList(new Integer[] { ASTNode.VARIABLE_DECLARATION_FRAGMENT,
					ASTNode.SINGLE_VARIABLE_DECLARATION });
	private CompilationUnit cu;
	private File file;

	private Set<BusTerm> bTerms;

	// pos -> [ lemma -> freq,statement ]
	private HashMap<BusTerm, HashMap<String, List<VarBT>>> btMatches;

	public VariablesVisitor() {
		setBtMatches(new HashMap<BusTerm, HashMap<String, List<VarBT>>>());
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
		List<Token> tokens = NLPProcessor.getInstance().processText(identifier,
				true);

		for (BusTerm bTerm : this.bTerms) {

			boolean b = contains(bTerm, tokens);
			if (!b) {
				continue;
			}

			HashMap<String, List<VarBT>> map = this.getBtMatches().get(bTerm);

			if (map == null) {
				map = new HashMap<String, List<VarBT>>();
				getBtMatches().put(bTerm, map);
			}
			List<VarBT> list = map.get(file.getAbsolutePath());
			if (list == null) {
				list = new ArrayList<VarBT>();
				map.put(file.getAbsolutePath(), list);
			}
			list.add(new VarBT(tokens,
					cu.getLineNumber(node.getStartPosition()), identifier));

		}

	}

	private boolean contains(BusTerm bTerm, List<Token> tokens) {
		List<Token> tokens2 = bTerm.getTokens();
		for (Token tokenBt : tokens2) {
			for (Token token : tokens) {
				if (tokenBt.getLemma().equalsIgnoreCase(token.getLemma())) {
					return true;
				}
			}
		}
		return false;
	}

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

	public Set<BusTerm> getbTerms() {
		return bTerms;
	}

	public void setbTerms(Set<BusTerm> bTerms) {
		this.bTerms = bTerms;
	}

	public HashMap<BusTerm, HashMap<String, List<VarBT>>> getBtMatches() {
		return btMatches;
	}

	public void setBtMatches(
			HashMap<BusTerm, HashMap<String, List<VarBT>>> btMatches) {
		this.btMatches = btMatches;
	}

}
