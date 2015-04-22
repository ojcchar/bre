package edu.utdallas.seers.bre.javabre.controller.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import au.com.bytecode.opencsv.CSVWriter;
import edu.utdallas.seers.bre.javabre.entity.words.Token;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.entity.words.bt.VarBT;

public class BTWriter {

	private static final char SEMI = ';';
	private CSVWriter writerPatt;
	private final String[] header1 = { "Business Term", "File", "Variable" };

	private static HashMap<String, String> POS_TAGS = new HashMap<String, String>();
	static {
		POS_TAGS.put("CC", "Coordinating conjunction");
		POS_TAGS.put("CD", "Cardinal number");
		POS_TAGS.put("DT", "Determiner");
		POS_TAGS.put("EX", "Existential there");
		POS_TAGS.put("FW", "Foreign word");
		POS_TAGS.put("IN", "Preposition or subordinating conjunction");
		POS_TAGS.put("JJ", "Adjective");
		POS_TAGS.put("JJR", "Adjective, comparative");
		POS_TAGS.put("JJS", "Adjective, superlative");
		POS_TAGS.put("LS", "List item marker");
		POS_TAGS.put("MD", "Modal");
		POS_TAGS.put("NN", "Noun, singular or mass");
		POS_TAGS.put("NNS", "Noun, plural");
		POS_TAGS.put("NNP", "Proper noun, singular");
		POS_TAGS.put("NNPS", "Proper noun, plural");
		POS_TAGS.put("PDT", "Predeterminer");
		POS_TAGS.put("POS", "Possessive ending");
		POS_TAGS.put("PRP", "Personal pronoun");
		POS_TAGS.put("PRP$", "Possessive pronoun");
		POS_TAGS.put("RB", "Adverb");
		POS_TAGS.put("RBR", "Adverb, comparative");
		POS_TAGS.put("RBS", "Adverb, superlative");
		POS_TAGS.put("RP", "Particle");
		POS_TAGS.put("SYM", "Symbol");
		POS_TAGS.put("TO", "to");
		POS_TAGS.put("UH", "Interjection");
		POS_TAGS.put("VB", "Verb, base form");
		POS_TAGS.put("VBD", "Verb, past tense");
		POS_TAGS.put("VBG", "Verb, gerund or present participle");
		POS_TAGS.put("VBN", "Verb, past participle");
		POS_TAGS.put("VBP", "Verb, non-3rd person singular present");
		POS_TAGS.put("VBZ", "Verb, 3rd person singular present");
		POS_TAGS.put("WDT", "Wh-determiner");
		POS_TAGS.put("WP", "Wh-pronoun");
		POS_TAGS.put("WP$", "Possessive wh-pronoun");
		POS_TAGS.put("WRB", "Wh-adverb");
	}

	// pos -> [ lemma -> freq,statement ]

	public BTWriter(File outFilePatt) throws IOException {
		writerPatt = new CSVWriter(new FileWriter(outFilePatt), SEMI);
		// writerPatt.writeNext(new String[] { "sep=" + SEMI });
		writerPatt.writeNext(header1);
	}

	public void close() throws IOException {
		writerPatt.close();
	}

	public void writeBtMatches(
			HashMap<Term, HashMap<String, Set<VarBT>>> matches) {
		Set<Entry<Term, HashMap<String, Set<VarBT>>>> entrySet = matches
				.entrySet();
		for (Entry<Term, HashMap<String, Set<VarBT>>> entry : entrySet) {
			Set<Entry<String, Set<VarBT>>> entrySet2 = entry.getValue()
					.entrySet();
			for (Entry<String, Set<VarBT>> entry2 : entrySet2) {
				Set<VarBT> value = entry2.getValue();
				for (VarBT varBT : value) {
					writerPatt.writeNext(new String[] {
							Token.getOrigStr(entry.getKey().getTokens()),
							entry2.getKey(), varBT.getOrigVar() });
				}
			}
		}
	}

}
