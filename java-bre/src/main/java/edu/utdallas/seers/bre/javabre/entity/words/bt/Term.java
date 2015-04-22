package edu.utdallas.seers.bre.javabre.entity.words.bt;

import java.util.List;

import edu.utdallas.seers.bre.javabre.entity.words.Token;

public class Term {

	private List<Token> tokens;

	public Term(List<Token> tokens2) {
		this.tokens = tokens2;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	@Override
	public String toString() {
		return "Term [tokens=" + tokens + "]";
	}

}
