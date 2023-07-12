package org.dragon.yunpeng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SQLLogParser {

	public static void main(String[] args) {

		String logFilePath = getFilePathFromClasspath("sql.log");

		if (args.length == 1) {
			logFilePath = args[0];
		}

		List<String> tableNames = extractTableNamesFromLog(logFilePath);

		System.out.println("Table Names:");
		for (String tableName : tableNames) {
			System.out.println(tableName);
		}
	}

	public static String getFilePathFromClasspath(String fileName) {
		ClassLoader classLoader = SQLLogParser.class.getClassLoader();
		URL url = classLoader.getResource(fileName);

		if (url != null) {
			File file = new File(url.getFile());
			return file.getAbsolutePath();
		}

		return null;
	}

	public static List<String> extractTableNamesFromLog(String logFilePath) {
		List<String> tableNames = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				List<String> tbNames = SQLTableExtractor.extractTableNames(line);
				tableNames.addAll(tbNames);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tableNames;
	}
}
