package edu.utdallas.seers.bre.javabre.entity;

import java.io.File;

public class BussinesRule {

	private String text;
	private File file;
	private int line;

	public BussinesRule(String brText, File file2, int startPosition) {
		text = brText;
		file = file2;
		line = startPosition;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	@Override
	public String toString() {
		return "BussinesRule [text=" + text + ", line=" + line + "]";
	}
	
	

}
