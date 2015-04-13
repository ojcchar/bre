package edu.utdallas.seers.bre.javabre.visitor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;

import edu.utdallas.seers.bre.javabre.controller.NLPProcessor;
import edu.utdallas.seers.bre.javabre.entity.Token;
import edu.utdallas.seers.bre.javabre.entity.WordCodeData;
import edu.utdallas.seers.bre.javabre.entity.WordData;

public class IdentifiersVisitor extends ASTVisitor {

	private final static List<String> JAVA_KEYWORDS = Arrays
			.asList(new String[] { "abstract", "continue", "for", "new",
					"switch", "assert", "default", "package", "synchronized",
					"boolean", "do", "if", "private", "this", "break",
					"double", "implements", "protected", "throw", "byte",
					"else", "import", "public", "throws", "case", "enum",
					"instanceof", "return", "transient", "catch", "extends",
					"int", "short", "try", "char", "final", "interface",
					"static", "void", "class", "finally", "long", "strictfp",
					"volatile", "float", "native", "super", "while" });

	private CompilationUnit cu;
	private File file;

	// pos -> [ lemma -> freq,statement ]
	private HashMap<String, HashMap<String, WordData>> data;
	private HashMap<String, Integer> identPatterns;

	public IdentifiersVisitor() {
		data = new HashMap<String, HashMap<String, WordData>>();
		identPatterns = new HashMap<String, Integer>();
	}

	@Override
	public boolean visit(SimpleName node) {
		String identifier = node.toString();

		if (!JAVA_KEYWORDS.contains(identifier)) {
			List<Token> tokens = NLPProcessor.getInstance().processText(
					identifier);
			updateData(tokens, node);
			updateTokens(tokens);
		}

		return super.visit(node);
	}

	private void updateTokens(List<Token> tokens) {
		String patt = getIdentPattern(tokens);
		if (patt == null) {
			return;
		}

		Integer freq = identPatterns.get(patt);
		if (freq == null) {
			freq = 0;
		}
		++freq;
		identPatterns.put(patt, freq);
	}

	private String getIdentPattern(List<Token> tokens) {

		if (tokens == null || tokens.isEmpty()) {
			return null;
		}

		StringBuffer buf = new StringBuffer();
		final String SC = "-";
		for (Token token : tokens) {
			buf.append(token.getPos());
			buf.append(SC);
		}
		int i = buf.lastIndexOf(SC);
		buf.replace(i, i + SC.length(), "");

		return buf.toString();
	}

	private void updateData(List<Token> tokens, SimpleName node) {

		for (Token token : tokens) {

			HashMap<String, WordData> wData = data.get(token.getPos());
			if (wData == null) {
				wData = new HashMap<String, WordData>();
				data.put(token.getPos(), wData);
			}

			WordData wordData = wData.get(token.getLemma());
			if (wordData == null) {
				wordData = new WordData(token.getLemma(), 0,
						new ArrayList<WordCodeData>());
				wData.put(token.getLemma(), wordData);
			}

			wordData.addFreq();
			WordCodeData codeData = new WordCodeData(getFile(), node
					.getParent().getClass().getSimpleName(),
					cu.getLineNumber(node.getStartPosition()));
			wordData.getCodeData().add(codeData);

		}

	}

	public HashMap<String, HashMap<String, WordData>> getData() {
		return data;
	}

	public HashMap<String, Integer> getIdentPatterns() {
		return identPatterns;
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

}
