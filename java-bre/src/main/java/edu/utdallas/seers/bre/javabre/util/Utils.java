package edu.utdallas.seers.bre.javabre.util;

import java.util.List;

import edu.utdallas.seers.bre.javabre.controller.NLPProcessor;
import edu.utdallas.seers.bre.javabre.entity.words.Token;

public class Utils {

	public static String replaceTemplate(String template, String[] values) {

		String replacedText = template;
		for (int i = 0; i < values.length; i++) {
			String val = values[i];
			replacedText = replacedText.replace("{" + i + "}", val);
		}

		return replacedText;
	}

	public static String bracketizeStr(String txt) {
		return "[" + getNLText(txt) + "]";
	}

	public static String getNLText(String txt) {

		List<Token> tokensTerm = NLPProcessor.getInstance().processText(txt, true);

		return getNLTokens(tokensTerm);
	}

	public static String getNLTokens(List<Token> tokensTerm) {
		StringBuffer buf = new StringBuffer();
		for (Token token : tokensTerm) {
			buf.append(token.getWord().toLowerCase());
			buf.append(" ");
		}

		return buf.toString().trim();
	}

	public static boolean shareBusinessTerms(String termToMatch,
			List<String> businessTerms) {
		boolean contains = businessTerms.isEmpty() ? true : false;
		for (String term : businessTerms) {

			if (!termToMatch.toLowerCase().contains(term.toLowerCase())) {
				List<Token> tokensCl = NLPProcessor.getInstance().processText(
						termToMatch, true);

				List<Token> tokensTerm = NLPProcessor.getInstance()
						.processText(term, true);

				for (Token tokenCl : tokensCl) {

					for (Token tokenTerm : tokensTerm) {
						if (tokenCl.getLemma().equals(tokenTerm.getLemma())) {
							contains = true;
							break;
						}
					}

					if (contains) {
						break;
					}

				}

			} else {
				contains = true;
			}

		}
		return contains;
	}

}
