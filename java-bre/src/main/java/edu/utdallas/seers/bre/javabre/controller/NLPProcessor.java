package edu.utdallas.seers.bre.javabre.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.google.common.base.CaseFormat;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.utdallas.seers.bre.javabre.entity.words.Token;

public class NLPProcessor {

	private static NLPProcessor instance;
	private static StanfordCoreNLP pipeline;

	private NLPProcessor() {
	}

	public synchronized static NLPProcessor getInstance() {
		if (instance == null) {
			Properties props = new Properties();
			props.setProperty("annotators",
					"tokenize, ssplit, pos, lemma, parse");
			pipeline = new StanfordCoreNLP(props);

			instance = new NLPProcessor();
		}
		return instance;
	}

	public List<Token> processText(String text, boolean breakCamelCase) {
		// create an empty Annotation just with the given text
		Annotation document = new Annotation(breakCamelCase ? splitCamelCase(text) : text);

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		List<Token> tokens = new ArrayList<Token>();

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				String lemma = token.get(LemmaAnnotation.class);

				tokens.add(new Token(word, pos, lemma));
			}

			// this is the parse tree of the current sentence
			// Tree tree = sentence.get(TreeAnnotation.class);
			// System.out.println("tree");
			// System.out.println(tree);
		}

		return tokens;
	}

	static String splitCamelCase(String s) {
		
		//FIXME: sometimes underscore and camel case are mixed
		if (s.contains("_")) {
			s = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s);
		}

		String regex = String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])",
				"(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])");
		String text = s.replaceAll(regex, " ");

		return text;
	}

}
