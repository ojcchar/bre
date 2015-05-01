package edu.utdallas.seers.bre.javabre.controller.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import au.com.bytecode.opencsv.CSVWriter;
import edu.utdallas.seers.bre.javabre.entity.BusinessRule;
import edu.utdallas.seers.bre.javabre.entity.words.bt.Term;
import edu.utdallas.seers.bre.javabre.util.Utils;

public class RulesWriter {
	// private static Logger LOGGER =
	// LoggerFactory.getLogger(RulesWriter.class);

	private static final char SEMI = ';';
	private CSVWriter writer;
	private final String[] header = { "Rule Type", "Rule", "File",
			"Line Number", "Contains sys term?" };
	private Set<Term> sysTerms;

	public RulesWriter(File file, Set<Term> sysTerms) throws IOException {
		writer = new CSVWriter(new FileWriter(file), SEMI);
		// writer.writeNext(new String[] { "sep=" + SEMI });
		writer.writeNext(header);
		this.sysTerms = sysTerms;
	}

	public void writeRules(List<BusinessRule> rules) throws IOException {

		for (BusinessRule br : rules) {

			Set<String> keySet = br.getFileLoc().keySet();

			int useSysTerms = 0;
			for (String file : keySet) {
				StringBuffer buf = new StringBuffer(file);
				int i = buf.lastIndexOf("\\");

				if (i == -1) {
					i = buf.lastIndexOf("/");
				}

				if (i != -1) {
					String fileName = buf.substring(i, buf.length());
					fileName = fileName.replace(".java", "");

					if (Utils.isTermContained(fileName, sysTerms, false)) {
						useSysTerms = 1;
						break;
					}
				}
			}

			String[] stringArray = br.toStringArray();

			List<String> list = new ArrayList<String>(
					Arrays.asList(stringArray));
			list.add(String.valueOf(useSysTerms));

			String[] array = new String[list.size()];
			list.toArray(array); // fill the array

			writer.writeNext(array);
		}

		writer.flush();

	}

	public void close() throws IOException {
		writer.close();
	}

}
