package edu.utdallas.seers.bre.javabre.controller.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import au.com.bytecode.opencsv.CSVWriter;
import edu.utdallas.seers.bre.javabre.entity.IFstmt;

public class IFWriter {

	private static final char SEMI = ';';
	private CSVWriter writerPatt;
	private final String[] header1 = { "File", "Variable", "IF condition",
			"Else Condition", "Line" };

	public IFWriter(File outFilePatt) throws IOException {
		writerPatt = new CSVWriter(new FileWriter(outFilePatt), SEMI);
		writerPatt.writeNext(header1);
	}

	public void close() throws IOException {
		writerPatt.close();
	}

	public void writeIfStmts(File file, HashMap<String, Set<IFstmt>> ifSt) {
		Set<Entry<String, Set<IFstmt>>> entrySet = ifSt.entrySet();

		for (Entry<String, Set<IFstmt>> entry : entrySet) {
			Set<IFstmt> value = entry.getValue();
			for (IFstmt iFstmt : value) {
				writerPatt.writeNext(new String[] { file.getAbsolutePath(),
						entry.getKey(), iFstmt.getCond(), iFstmt.getThen(),
						String.valueOf(iFstmt.getLine()) });
			}
		}
	}

}
