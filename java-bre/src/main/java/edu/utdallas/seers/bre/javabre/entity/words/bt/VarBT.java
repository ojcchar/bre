package edu.utdallas.seers.bre.javabre.entity.words.bt;

import java.util.List;

import edu.utdallas.seers.bre.javabre.entity.words.Token;

public class VarBT {

	private String origVar;
	private List<Token> tokens;

	public VarBT(List<Token> tokens2, String origVar) {
		this.tokens = tokens2;
		this.origVar = origVar;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public String getOrigVar() {
		return origVar;
	}

	public void setOrigVar(String origVar) {
		this.origVar = origVar;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((origVar == null) ? 0 : origVar.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VarBT other = (VarBT) obj;
		if (origVar == null) {
			if (other.origVar != null)
				return false;
		} else if (!origVar.equals(other.origVar))
			return false;
		return true;
	}

}
