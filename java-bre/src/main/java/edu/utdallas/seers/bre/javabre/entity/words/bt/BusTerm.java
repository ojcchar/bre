package edu.utdallas.seers.bre.javabre.entity.words.bt;

import java.util.List;

import edu.utdallas.seers.bre.javabre.entity.words.Token;

public class BusTerm {

	private int id;
	private List<Token> tokens;

	public BusTerm(int id2, List<Token> tokens2) {
		this.id = id2;
		this.tokens = tokens2;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		BusTerm other = (BusTerm) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
