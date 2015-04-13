package edu.utdallas.seers.bre.javabre.entity;

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

}
