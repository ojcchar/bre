package edu.utdallas.seers.bre.javabre.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTParser;

import edu.utdallas.seers.bre.javabre.controller.NLPProcessor;
import edu.utdallas.seers.bre.javabre.entity.words.Token;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;

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

		List<Token> tokensTerm = NLPProcessor.getInstance().processText(txt,
				true);

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

	public static Set<Term> readTermsFile(File inFile) throws IOException {

		Set<Term> terms = new HashSet<Term>();

		try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
			for (String line; (line = br.readLine()) != null;) {

				List<Token> tokens = NLPProcessor.getInstance().processText(
						line, false);
				// if (hasNouns(tokens)) {
				terms.add(new Term(tokens));
				// }
			}
		}

		return terms;
	}

	public static char[] readFile(File path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path.getAbsolutePath()));
		return new String(encoded, Charset.defaultCharset()).toCharArray();
	}

	// private static final List<String> NOUNS = Arrays.asList(new String[] {
	// "NN", "NNS", "NNP", "NNPS" });
	//
	// private static boolean hasNouns(List<Token> tokens) {
	//
	// for (Token token : tokens) {
	// if (NOUNS.contains(token.getPos())) {
	// return true;
	// }
	// }
	// return false;
	// }

	public static void setParserConf(ASTParser parser, String[] encodings,
			String[] sourceFolders, String[] classPaths) {
		@SuppressWarnings("unchecked")
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);

		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.setCompilerOptions(options);
		parser.setResolveBindings(true);

		parser.setEnvironment(classPaths, sourceFolders, encodings, true);
	}

	public static boolean contains(Term bTerm, List<Token> tokens) {
		List<Token> tokens2 = bTerm.getTokens();
		for (Token tokenBt : tokens2) {

			for (Token token : tokens) {
				// if (tokenBt.getWord().equalsIgnoreCase("ui")
				// && token.getWord().equalsIgnoreCase("ui")) {
				// System.out.println(tokenBt + " - " + token);
				// }
				if (tokenBt.getLemma().equalsIgnoreCase(token.getLemma())) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean containsAllTakens(Term bTerm, List<Token> tokens) {
		List<Token> tokens2 = bTerm.getTokens();
		for (Token tokenBt : tokens2) {
			boolean isTokenUsed = false;
			for (Token token : tokens) {
				if (tokenBt.getLemma().equalsIgnoreCase(token.getLemma())) {
					isTokenUsed = true;
					break;
				}
			}

			if (!isTokenUsed) {
				return false;
			}
		}
		return true;
	}

	public static Term getTermContained(String term, Set<Term> termsSet) {

		if (termsSet == null || termsSet.isEmpty()) {
			return null;
		}

		List<Token> tokensTerm = NLPProcessor.getInstance().processText(term,
				true);
		for (Term bTerm : termsSet) {
			if (term.toLowerCase().contains(bTerm.toString())) {
				return bTerm;
			}
			boolean c = containsAllTakens(bTerm, tokensTerm);
			if (c) {
				return bTerm;
			}
		}

		return null;
	}

	public static boolean isTermContained(String term, Set<Term> termsSet,
			boolean b) {

		if (termsSet == null || termsSet.isEmpty()) {
			return b;
		}

		List<Token> tokensTerm = NLPProcessor.getInstance().processText(term,
				true);
		for (Term bTerm : termsSet) {
			if (term.toLowerCase().contains(bTerm.toString())) {
				return true;
			}
			boolean c = containsAllTakens(bTerm, tokensTerm);
			if (c) {
				return c;
			}
		}

		return false;
	}

	public static boolean isInValidIdent(String term, Set<Term> businessTerms,
			Set<Term> sysTerms) {

		boolean sys = Utils.isTermContained(term, sysTerms, false);
		boolean bus = Utils.isTermContained(term, businessTerms, true);
		// return sys && !bus;
		boolean invalid = sys && !bus;
		// boolean invalid = sys || !bus;
		return invalid;

	}

}
