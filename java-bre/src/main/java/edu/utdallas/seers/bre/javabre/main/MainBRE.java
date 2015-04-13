package edu.utdallas.seers.bre.javabre.main;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.controller.RulesController;

/**
 *
 */
public class MainBRE {

	private static Logger LOGGER = LoggerFactory.getLogger(MainBRE.class);
	private static String[] sourceFolders;
	private static String[] classPaths;
	private static File outFile;
	private static File bussFile;

	public static void main(String args[]) {

		RulesController rulesController = null;
		try {
			parseArguments(args);

			rulesController = new RulesController(sourceFolders, classPaths,
					outFile, bussFile);
			rulesController.processRules();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();

		} finally {
			if (rulesController != null) {
				try {
					rulesController.close();
				} catch (IOException e1) {
					LOGGER.error(e1.getMessage());
					e1.printStackTrace();
				}
			}
		}

	}

	private static void parseArguments(String[] args) throws Exception {
		if (args.length != 4) {
			throw new Exception(
					"Arguments must be 4: [source_folders] [classpaths] [file_business_words] [out_csv_file]");
		}

		sourceFolders = args[0].split(",");
		classPaths = args[1].split(",");
		bussFile = new File(args[2]);
		outFile = new File(args[3]);
	}

}
