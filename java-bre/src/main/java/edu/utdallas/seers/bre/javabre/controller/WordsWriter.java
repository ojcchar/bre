package edu.utdallas.seers.bre.javabre.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import au.com.bytecode.opencsv.CSVWriter;
import edu.utdallas.seers.bre.javabre.entity.WordCodeData;
import edu.utdallas.seers.bre.javabre.entity.WordData;

public class WordsWriter {

	private static final char SEMI = ';';
	private CSVWriter writerPatt;
	private final String[] header1 = { "Statement Type", "POS Pattern",
			"NL Pattern" };

	private CSVWriter writerData;
	private final String[] header2 = { "POS Tag", "POS Tag Desc", "Lemma",
			"File", "Statement Type", "Line" };

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

	public WordsWriter(File outFilePatt, File outFileData) throws IOException {
		writerPatt = new CSVWriter(new FileWriter(outFilePatt), SEMI);
		// writerPatt.writeNext(new String[] { "sep=" + SEMI });
		writerPatt.writeNext(header1);

		writerData = new CSVWriter(new FileWriter(outFileData), SEMI);
		// writerData.writeNext(new String[] { "sep=" + SEMI });
		writerData.writeNext(header2);
	}

	public void writePatterns(
			HashMap<String, HashMap<String, List<String>>> identPatterns) {

		Set<Entry<String, HashMap<String, List<String>>>> entrySet = identPatterns
				.entrySet();
		for (Entry<String, HashMap<String, List<String>>> entry : entrySet) {

			Set<Entry<String, List<String>>> entrySet2 = entry.getValue()
					.entrySet();

			for (Entry<String, List<String>> entry2 : entrySet2) {

				List<String> patterns = entry2.getValue();

				for (String patt : patterns) {
					writerPatt.writeNext(new String[] { entry.getKey(),
							entry2.getKey(), patt });
				}
			}

		}
	}

	public void writeData(HashMap<String, HashMap<String, WordData>> data) {

		Set<Entry<String, HashMap<String, WordData>>> entrySet2 = data
				.entrySet();

		for (Entry<String, HashMap<String, WordData>> entry : entrySet2) {

			Set<Entry<String, WordData>> entrySet3 = entry.getValue()
					.entrySet();

			for (Entry<String, WordData> entry2 : entrySet3) {

				List<WordCodeData> codeData = entry2.getValue().getCodeData();

				for (WordCodeData wordCodeData : codeData) {
					// { "POS Tag", "Lemma", "Frequency", "File",
					// "Statement Type", "Line" };
					writerData.writeNext(new String[] { entry.getKey(),
							POS_TAGS.get(entry.getKey()),
							entry2.getKey(),
							// String.valueOf(entry2.getValue().getFreq()),
							wordCodeData.getFile().getAbsolutePath(),
							wordCodeData.getNodeType(),
							String.valueOf(wordCodeData.getLine()) });

				}

			}
		}

	}

	public void close() throws IOException {
		writerPatt.close();
		writerData.close();
	}

}
