package edu.utdallas.seers.bre.javabre.main;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.controller.WordsController;

/**
 *
 */
public class MainWords {

	private static Logger LOGGER = LoggerFactory.getLogger(MainWords.class);
	private static String[] sourceFolders;
	private static String[] classPaths;
	private static File outFilePatterns;
	private static File outFileData;

	public static void main(String args[]) {

		WordsController controller = null;
		try {
			parseArguments(args);

			controller = new WordsController(sourceFolders, classPaths,
					outFilePatterns, outFileData);
			controller.processRules();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();

		} finally {
			if (controller != null) {
				try {
					controller.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}

	}

	private static void parseArguments(String[] args) throws Exception {
		if (args.length != 4) {
			throw new Exception(
					"Arguments must be 4: [source_folders] [classpaths] [out_csv_file_patterns] [out_csv_file_data]");
		}

		sourceFolders = args[0].split(",");
		classPaths = args[1].split(",");
		outFilePatterns = new File(args[2]);
		outFileData = new File(args[3]);
	}

}
