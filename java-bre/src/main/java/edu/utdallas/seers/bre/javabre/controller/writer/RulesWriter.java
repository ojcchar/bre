package edu.utdallas.seers.bre.javabre.controller.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import edu.utdallas.seers.bre.javabre.entity.BusinessRule;

public class RulesWriter {
	// private static Logger LOGGER =
	// LoggerFactory.getLogger(RulesWriter.class);

	private static final char SEMI = ';';
	private CSVWriter writer;
	private final String[] header = { "Rule Type", "Rule", "File",
			"Line Number" };

	public RulesWriter(File file) throws IOException {
		writer = new CSVWriter(new FileWriter(file), SEMI);
		// writer.writeNext(new String[] { "sep=" + SEMI });
		writer.writeNext(header);
	}

	public void writeRules(List<BusinessRule> rules) throws IOException {

		for (BusinessRule br : rules) {
			writer.writeNext(br.toStringArray());
		}

		writer.flush();

	}

	public void close() throws IOException {
		writer.close();
	}

}
