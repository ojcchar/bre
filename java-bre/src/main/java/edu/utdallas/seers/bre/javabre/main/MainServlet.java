package edu.utdallas.seers.bre.javabre.main;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utdallas.seers.bre.javabre.controller.ServletController;

/**
 *
 */
public class MainServlet {

	private static Logger LOGGER = LoggerFactory.getLogger(MainServlet.class);
	private static String[] sourceFolders;
	private static String[] classPaths;
	private static File outFile;
	private static File bussFile;
	private static String[] processFolders;
	private static File sysFile;

	public static void main(String args[]) {

		ServletController rulesController = null;
		try {
			parseArguments(args);

			rulesController = new ServletController(sourceFolders, classPaths,
					outFile, bussFile, processFolders, sysFile);
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
		if (args.length != 6) {
			throw new Exception(
					"Arguments must be 6: [process_folders] [source_folders] [classpaths] [file_business_words] [file_system_words] [out_csv_file]");
		}

		processFolders = args[0].split(",");
		sourceFolders = args[1].split(",");
		classPaths = args[2].split(",");
		bussFile = new File(args[3]);
		sysFile = new File(args[4]);
		outFile = new File(args[5]);
	}

}
