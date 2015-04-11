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

	public static void main(String args[]) {

		RulesController rulesController = null;
		try {
			parseArguments(args);

			rulesController = new RulesController(sourceFolders, classPaths,
					outFile);
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
		if (args.length != 3) {
			throw new Exception(
					"Arguments must be 3: [source_folders] [classpaths] [out_csv_file]");
		}

		sourceFolders = args[0].split(",");
		classPaths = args[1].split(",");
		outFile = new File(args[2]);
	}

}
