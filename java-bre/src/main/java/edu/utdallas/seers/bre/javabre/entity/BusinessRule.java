package edu.utdallas.seers.bre.javabre.entity;

import java.io.File;

public class BusinessRule {

	private String text;
	private File file;
	private int line;

	public BusinessRule(String brText, File file2, int startPosition) {
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

	public String[] toStringArray() {
		return new String[] { text, file.getAbsolutePath(),
				Integer.toString(line) };
	}

	@Override
	public String toString() {
		return "BussinesRule [text=" + text + ", line=" + line + "]";
	}

}
