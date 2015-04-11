package edu.utdallas.seers.bre.javabre.util;

public class Utils {

	public static String replaceTemplate(String template, String[] values) {

		String replacedText = template;
		for (int i = 0; i < values.length; i++) {
			String val = values[i];
			replacedText = replacedText.replace("{" + i + "}", val);
		}

		return replacedText;
	}

}
