package edu.utdallas.seers.bre.javabre.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class BusinessRule {

	private static final String SEP = "; ";
	private String text;
	private HashMap<String, List<Integer>> fileLoc;
	private RuleType type;

	public enum RuleType {
		SYMBOL_LITERAL, CATEG_ENUMERATION, VALID_VALUE
	}

	public BusinessRule(String brText, RuleType type) {
		text = brText;
		this.setType(type);
		fileLoc = new HashMap<String, List<Integer>>();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String[] toStringArray() {
		String[] locs = getFileLines();
		return new String[] { type.toString(), text, locs[0], locs[1] };
	}

	/**
	 * Files = file1;file2;file3;... LOCS =
	 * {{l11;l12;l12;...};{{l21;l22;l22;...},{{l31;l32;l32;...};...}
	 * 
	 * @return
	 */
	private String[] getFileLines() {
		Set<Entry<String, List<Integer>>> entrySet = fileLoc.entrySet();

		StringBuffer files = new StringBuffer();
		StringBuffer fLocs = new StringBuffer("{");
		for (Entry<String, List<Integer>> entry : entrySet) {
			files.append(entry.getKey());
			files.append(SEP);

			List<Integer> value = entry.getValue();
			StringBuffer locs = new StringBuffer(" {");
			for (Integer val : value) {
				locs.append(val);
				locs.append(SEP);
			}
			int i = locs.lastIndexOf(SEP);
			locs.replace(i, i + SEP.length(), "}");

			fLocs.append(locs);
			fLocs.append(SEP);
		}

		int i = files.lastIndexOf(SEP);
		files.replace(i, i + SEP.length(), "");

		i = fLocs.lastIndexOf(SEP);
		fLocs.replace(i, i + SEP.length(), " }");

		return new String[] { files.toString(), fLocs.toString() };
	}

	public void addLocation(File file, int lineNumber) {
		List<Integer> list = fileLoc.get(file.getAbsolutePath());
		if (list == null) {
			list = new ArrayList<Integer>();
			fileLoc.put(file.getAbsolutePath(), list);
		}
		list.add(lineNumber);
	}

	@Override
	public String toString() {
		return "BusinessRule [text=" + text + "]";
	}

	public RuleType getType() {
		return type;
	}

	public void setType(RuleType type) {
		this.type = type;
	}

}
