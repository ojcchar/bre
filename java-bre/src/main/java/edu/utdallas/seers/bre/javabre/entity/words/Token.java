package edu.utdallas.seers.bre.javabre.entity.words;

import java.util.List;

public class Token {

	private String word;
	private String pos;
	private String lemma;

	public Token(String word, String pos, String lemma) {
		this.word = word;
		this.pos = pos;
		this.lemma = lemma;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	@Override
	public String toString() {
		return "Token [word=" + word + ", pos=" + pos + ", lemma=" + lemma
				+ "]";
	}
	
	public static String getIdentPattern(List<Token> tokens) {

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
	
	public static String getLemmaStr(List<Token> tokens) {

		if (tokens == null || tokens.isEmpty()) {
			return null;
		}

		StringBuffer buf = new StringBuffer();
		final String SC = " ";
		for (Token token : tokens) {
			buf.append(token.getLemma());
			buf.append(SC);
		}
		int i = buf.lastIndexOf(SC);
		buf.replace(i, i + SC.length(), "");

		return buf.toString();
	}
	
	public static String getOrigStr(List<Token> tokens) {

		if (tokens == null || tokens.isEmpty()) {
			return null;
		}

		StringBuffer buf = new StringBuffer();
		final String SC = " ";
		for (Token token : tokens) {
			buf.append(token.word);
			buf.append(SC);
		}
		int i = buf.lastIndexOf(SC);
		buf.replace(i, i + SC.length(), "");

		return buf.toString();
	}


}
