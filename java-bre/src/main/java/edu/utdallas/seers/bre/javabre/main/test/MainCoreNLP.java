package edu.utdallas.seers.bre.javabre.main.test;

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
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class MainCoreNLP {

	public static void main(String[] args) {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		// props.setProperty("annotators",
		// "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// read some text in the text variable
		String text = "HELLO,_i_am"; // Add your
										// text
										// here!

		processText(pipeline, text);

		text = "hola soy yo "; // Add your
		// text
		// here!
		// for (int i = 0; i < 11; i++) {
		// processText(pipeline, text + i);
		// }

		// This is the coreference link graph
		// Each chain stores a set of mentions that link to each other,
		// along with a method for getting the most representative mention
		// Both sentence and token offsets start at 1!
		// Map<Integer, CorefChain> graph = document
		// .get(CorefChainAnnotation.class);
		// System.out.println("graph");
		// System.out.println(graph);
	}

	private static void processText(StanfordCoreNLP pipeline, String text) {
		// create an empty Annotation just with the given text
		Annotation document = new Annotation(splitCamelCase(text));

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				String lemma = token.get(LemmaAnnotation.class);

				System.out.println(word + " - " + pos + " - " + lemma);
			}

			// this is the parse tree of the current sentence
			Tree tree = sentence.get(TreeAnnotation.class);
			System.out.println("tree");
			System.out.println(tree);

			// this is the Stanford dependency graph of the current sentence
			// SemanticGraph dependencies = sentence
			// .get(CollapsedCCProcessedDependenciesAnnotation.class);
			// System.out.println("deps");
			// System.out.println(dependencies);
		}
	}

	static String splitCamelCase(String s) {

		s = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, s);

		String regex = String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])",
				"(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])");
		String text = s.replaceAll(regex, " ");

		// text = text.replaceAll("_", " ");
		return text;
	}

}
