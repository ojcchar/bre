package edu.utdallas.seers.bre.javabre.main.test;

import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

public class POSParser {
	private final static String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";

	private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer
			.factory(new CoreLabelTokenFactory(), "invertible=true");

	private final LexicalizedParser parser = LexicalizedParser
			.loadModel(PCG_MODEL);

	public Tree parse(String str) {
		List<CoreLabel> tokens = tokenize(str);
		Tree tree = parser.apply(tokens);
		return tree;
	}

	private List<CoreLabel> tokenize(String str) {
		Tokenizer<CoreLabel> tokenizer = tokenizerFactory
				.getTokenizer(new StringReader(str));
		return tokenizer.tokenize();
	}

	public static void main(String[] args) {
		String str = "My dog also liked eating sausage.";
		// String str ="PDFLoaderSimpleXMLParserGL11VersionBFG9000";
		POSParser parser = new POSParser();
		Tree tree = parser.parse(splitCamelCase(str));

		System.out.println("tree:");
		System.out.println(tree);

		List<Tree> leaves = tree.getLeaves();
		// Print words and Pos Tags
		for (Tree leaf : leaves) {
			Tree parent = leaf.parent(tree);
			System.out.print(leaf.label().value() + "-"
					+ parent.label().value() + " ");
		}
		System.out.println();
		Morphology morpha = new Morphology();
		for (Tree leaf : leaves) {
			Tree parent = leaf.parent(tree);
			morpha.stem(((CoreLabel) leaf.label()));
			System.out.println(leaf.label().value() + "-"
					+ parent.label().value() + " -  "
					+ ((CoreLabel) leaf.label()).lemma());
		}
		System.out.println();
	}

	static String splitCamelCase(String s) {
		String regex = String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])",
				"(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])");
		return s.replaceAll(regex, " ");
	}
}
