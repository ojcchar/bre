package edu.utdallas.seers.bre.javabre.entity;

public class IFstmt {

	private String then;
	private String cond;
	private int line;

	public IFstmt(String cond, String then, int line) {
		this.setCond(cond);
		this.setThen(then);
		this.setLine(line);
	}

	public String getThen() {
		return then;
	}

	public void setThen(String then) {
		this.then = then;
	}

	public String getCond() {
		return cond;
	}

	public void setCond(String cond) {
		this.cond = cond;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	@Override
	public String toString() {
		return "IFstmt [then=" + then + ", cond=" + cond + ", line=" + line
				+ "]";
	}

}
