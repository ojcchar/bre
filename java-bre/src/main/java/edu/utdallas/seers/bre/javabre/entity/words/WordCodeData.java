package edu.utdallas.seers.bre.javabre.entity.words;

import java.io.File;

public class WordCodeData {

	private String nodeType;
	private int line;
	private File file;

	public WordCodeData(File file, String nodeType, int line) {
		this.setFile(file);
		this.setNodeType(nodeType);
		this.setLine(line);
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	@Override
	public String toString() {
		return "WordCodeData [nodeType=" + nodeType + ", line=" + line + "]";
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
