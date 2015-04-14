package edu.utdallas.seers.bre.javabre.visitor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;

import edu.utdallas.seers.bre.javabre.controller.NLPProcessor;
import edu.utdallas.seers.bre.javabre.entity.Token;
import edu.utdallas.seers.bre.javabre.entity.WordCodeData;
import edu.utdallas.seers.bre.javabre.entity.WordData;
import edu.utdallas.seers.bre.javabre.util.Utils;

public class IdentifiersVisitor extends ASTVisitor {

	private final static List<String> JAVA_KEYWORDS = Arrays
			.asList(new String[] { "abstract", "continue", "for", "new",
					"switch", "assert", "default", "package", "synchronized",
					"boolean", "do", "if", "private", "this", "break",
					"double", "implements", "protected", "throw", "byte",
					"else", "import", "public", "throws", "case", "enum",
					"instanceof", "return", "transient", "catch", "extends",
					"int", "short", "try", "char", "final", "interface",
					"static", "void", "class", "finally", "long", "strictfp",
					"volatile", "float", "native", "super", "while" });

	private final static List<Integer> NODE_TYPES = Arrays
			.asList(new Integer[] { ASTNode.VARIABLE_DECLARATION_FRAGMENT,
					ASTNode.METHOD_DECLARATION,
					ASTNode.SINGLE_VARIABLE_DECLARATION,
					ASTNode.TYPE_DECLARATION, ASTNode.ENUM_DECLARATION,
					ASTNode.ENUM_CONSTANT_DECLARATION });

	private final static List<String> STOP_WORDS = Arrays.asList(new String[] {
			"a", "about", "above", "according", "across", "after",
			"afterwards", "again", "against", "albeit", "all", "almost",
			"alone", "along", "already", "also", "although", "always", "am",
			"among", "amongst", "an", "and", "another", "any", "anybody",
			"anyhow", "anyone", "anything", "anyway", "anywhere", "apart",
			"are", "around", "as", "at", "av", "be", "became", "because",
			"become", "becomes", "becoming", "been", "before", "beforehand",
			"behind", "being", "below", "beside", "besides", "between",
			"beyond", "both", "but", "by", "can", "cannot", "canst", "certain",
			"cf", "choose", "contrariwise", "cos", "could", "cu", "day", "do",
			"does", "doesn't", "doing", "dost", "doth", "double", "down",
			"dual", "during", "each", "either", "else", "elsewhere", "enough",
			"et", "etc", "even", "ever", "every", "everybody", "everyone",
			"everything", "everywhere", "except", "excepted", "excepting",
			"exception", "exclude", "excluding", "exclusive", "far", "farther",
			"farthest", "few", "ff", "first", "for", "formerly", "forth",
			"forward", "from", "front", "further", "furthermore", "furthest",
			"get", "go", "had", "halves", "hardly", "has", "hast", "hath",
			"have", "he", "hence", "henceforth", "her", "here", "hereabouts",
			"hereafter", "hereby", "herein", "hereto", "hereupon", "hers",
			"herself", "him", "himself", "hindmost", "his", "hither",
			"hitherto", "how", "however", "howsoever", "i", "ie", "if", "in",
			"inasmuch", "inc", "include", "included", "including", "indeed",
			"indoors", "inside", "insomuch", "instead", "into", "inward",
			"inwards", "is", "it", "its", "itself", "just", "kind", "kg", "km",
			"last", "latter", "latterly", "less", "lest", "let", "like",
			"little", "ltd", "many", "may", "maybe", "me", "meantime",
			"meanwhile", "might", "moreover", "most", "mostly", "more", "mr",
			"mrs", "ms", "much", "must", "my", "myself", "namely", "need",
			"neither", "never", "nevertheless", "next", "no", "nobody", "none",
			"nonetheless", "noone", "nope", "nor", "not", "nothing",
			"notwithstanding", "now", "nowadays", "nowhere", "of", "off",
			"often", "ok", "on", "once", "one", "only", "onto", "or", "other",
			"others", "otherwise", "ought", "our", "ours", "ourselves", "out",
			"outside", "over", "own", "per", "perhaps", "plenty", "provide",
			"quite", "rather", "really", "round", "said", "sake", "same",
			"sang", "save", "saw", "see", "seeing", "seem", "seemed",
			"seeming", "seems", "seen", "seldom", "selves", "sent", "several",
			"shalt", "she", "should", "shown", "sideways", "since", "slept",
			"slew", "slung", "slunk", "smote", "so", "some", "somebody",
			"somehow", "someone", "something", "sometime", "sometimes",
			"somewhat", "somewhere", "spake", "spat", "spoke", "spoken",
			"sprang", "sprung", "stave", "staves", "still", "such",
			"supposing", "than", "that", "the", "thee", "their", "them",
			"themselves", "then", "thence", "thenceforth", "there",
			"thereabout", "thereabouts", "thereafter", "thereby", "therefore",
			"therein", "thereof", "thereon", "thereto", "thereupon", "these",
			"they", "this", "those", "thou", "though", "thrice", "through",
			"throughout", "thru", "thus", "thy", "thyself", "till", "to",
			"together", "too", "toward", "towards", "ugh", "unable", "under",
			"underneath", "unless", "unlike", "until", "up", "upon", "upward",
			"upwards", "us", "use", "used", "using", "very", "via", "vs",
			"want", "was", "we", "week", "well", "were", "what", "whatever",
			"whatsoever", "when", "whence", "whenever", "whensoever", "where",
			"whereabouts", "whereafter", "whereas", "whereat", "whereby",
			"wherefore", "wherefrom", "wherein", "whereinto", "whereof",
			"whereon", "wheresoever", "whereto", "whereunto", "whereupon",
			"wherever", "wherewith", "whether", "whew", "which", "whichever",
			"whichsoever", "while", "whilst", "whither", "who", "whoa",
			"whoever", "whole", "whom", "whomever", "whomsoever", "whose",
			"whosoever", "why", "will", "wilt", "with", "within", "without",
			"worse", "worst", "would", "wow", "ye", "yet", "year", "yippee",
			"you", "your", "yours", "yourself", "yourselves", "set" });

	private CompilationUnit cu;
	private File file;

	// pos -> [ lemma -> freq,statement ]
	private HashMap<String, HashMap<String, WordData>> data;
	// statement -> [ pos -> List<strings>]
	private HashMap<String, HashMap<String, List<String>>> identPatterns;

	public IdentifiersVisitor() {
		data = new HashMap<String, HashMap<String, WordData>>();
		identPatterns = new HashMap<String, HashMap<String, List<String>>>();
	}

	@Override
	public boolean visit(SimpleName node) {
		String identifier = node.toString();
		int parentType = node.getParent().getNodeType();

		if (!JAVA_KEYWORDS.contains(identifier)
				&& NODE_TYPES.contains(parentType)) {
			List<Token> tokens = NLPProcessor.getInstance().processText(
					identifier);
			updateData(tokens, node);
			updateTokens(tokens, node);
		}

		return super.visit(node);
	}

	private void updateTokens(List<Token> tokens, SimpleName node) {

		String parentType = node.getParent().getClass().getSimpleName();

		HashMap<String, List<String>> hashMap = identPatterns.get(parentType);
		if (hashMap == null) {
			hashMap = new HashMap<String, List<String>>();
			identPatterns.put(parentType, hashMap);
		}

		String posPatt = getIdentPattern(tokens);
		if (posPatt == null) {
			return;
		}

		String nlPatt = Utils.getNLTokens(tokens);

		List<String> nlPatts = hashMap.get(posPatt);
		if (nlPatts == null) {
			nlPatts = new ArrayList<String>();
			hashMap.put(posPatt, nlPatts);
		}
		nlPatts.add(nlPatt);
	}

	private String getIdentPattern(List<Token> tokens) {

		if (tokens == null || tokens.isEmpty()) {
			return null;
		}

		StringBuffer buf = new StringBuffer();
		final String SC = "-";
		for (Token token : tokens) {
			buf.append(token.getPos());
			buf.append(SC);
		}
		int i = buf.lastIndexOf(SC);
		buf.replace(i, i + SC.length(), "");

		return buf.toString();
	}

	private void updateData(List<Token> tokens, SimpleName node) {

		for (Token token : tokens) {

			String lemma = token.getLemma();

			// remove stop words and short words (length below 3)
			if (STOP_WORDS.contains(lemma) || lemma.length() < 3) {
				continue;
			}

			HashMap<String, WordData> wData = data.get(token.getPos());
			if (wData == null) {
				wData = new HashMap<String, WordData>();
				data.put(token.getPos(), wData);
			}

			WordData wordData = wData.get(lemma);
			if (wordData == null) {
				wordData = new WordData(lemma, 0, new ArrayList<WordCodeData>());
				wData.put(lemma, wordData);
			}

			wordData.addFreq();
			WordCodeData codeData = new WordCodeData(getFile(), node
					.getParent().getClass().getSimpleName(),
					cu.getLineNumber(node.getStartPosition()));
			wordData.getCodeData().add(codeData);

		}

	}

	public HashMap<String, HashMap<String, WordData>> getData() {
		return data;
	}

	public HashMap<String, HashMap<String, List<String>>> getIdentPatterns() {
		return identPatterns;
	}

	public CompilationUnit getCu() {
		return cu;
	}

	public void setCu(CompilationUnit cu) {
		this.cu = cu;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
