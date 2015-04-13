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
	private final String[] header1 = { "Pattern", "Frequency" };

	private CSVWriter writerData;
	private final String[] header2 = { "POS Tag", "Lemma", "Frequency", "File",
			"Statement Type", "Line" };

	// pos -> [ lemma -> freq,statement ]

	public WordsWriter(File outFilePatt, File outFileData) throws IOException {
		writerPatt = new CSVWriter(new FileWriter(outFilePatt), SEMI);
		writerPatt.writeNext(new String[] { "sep=" + SEMI });
		writerPatt.writeNext(header1);

		writerData = new CSVWriter(new FileWriter(outFileData), SEMI);
		writerData.writeNext(new String[] { "sep=" + SEMI });
		writerData.writeNext(header2);
	}

	public void writePatterns(HashMap<String, Integer> identPatterns) {

		Set<Entry<String, Integer>> entrySet = identPatterns.entrySet();
		for (Entry<String, Integer> entry : entrySet) {
			writerPatt.writeNext(new String[] { entry.getKey(),
					entry.getValue().toString() });
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
							entry2.getKey(),
							String.valueOf(entry2.getValue().getFreq()),
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
