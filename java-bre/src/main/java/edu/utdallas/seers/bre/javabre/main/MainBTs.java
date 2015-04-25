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
	private static File sysFile;

	public static void main(String args[]) {

		BTController controller = null;
		try {
			parseArguments(args);

			controller = new BTController(sourceFolders, classPaths, inFile,
					outFile, processFolders, sysFile);
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
		if (args.length != 6) {
			throw new Exception(
					"Arguments must be 6: [process_folders] [source_folders] [classpaths] [buss_terms_file] [sys_terms_file] [out_csv_file]");
		}
		processFolders = args[0].split(",");
		sourceFolders = args[1].split(",");
		classPaths = args[2].split(",");
		inFile = new File(args[3]);
		sysFile = new File(args[4]);
		outFile = new File(args[5]);
	}

}
