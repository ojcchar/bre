package edu.utdallas.seers.bre.javabre.entity.words.bt;

import java.util.List;

import edu.utdallas.seers.bre.javabre.entity.words.Token;

public class VarBT {

	private String origVar;
	private List<Token> tokens;
	private int line;

	public VarBT(List<Token> tokens2, int lineNumber, String origVar) {
		this.tokens = tokens2;
		this.line = lineNumber;
		this.origVar = origVar;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getOrigVar() {
		return origVar;
	}

	public void setOrigVar(String origVar) {
		this.origVar = origVar;
	}

}
