package edu.utdallas.seers.bre.javabre.main;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.controller.BTController;

/**
 *
 */
public class MainBTs {

	private static Logger LOGGER = LoggerFactory.getLogger(MainBTs.class);
	private static String[] sourceFolders;
	private static String[] classPaths;
	private static File outFile;
	private static File inFile;
	private static String[] processFolders;

	public static void main(String args[]) {

		BTController controller = null;
		try {
			parseArguments(args);

			controller = new BTController(sourceFolders, classPaths, inFile,
					outFile, processFolders);
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
		if (args.length != 5) {
			throw new Exception(
					"Arguments must be 5: [process_folders] [source_folders] [classpaths] [in_bt_file] [out_csv_file]");
		}
		processFolders = args[0].split(",");
		sourceFolders = args[1].split(",");
		classPaths = args[2].split(",");
		inFile = new File(args[3]);
		outFile = new File(args[4]);
	}

}
