package edu.pit.datalayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class DataReader {

	private Set<String> stopwords = new HashSet<String>();

	public Set<String> getStopwords() {
		return stopwords;
	}

	public static DataReader reader = null;

	public static DataReader getInstance() {

		if (reader == null) {
			reader = new DataReader();
		}

		return reader;
	}

	public DataReader() {

		this.getStopWords();

	}

	public Set<String> getStopWords() {

		File file = new File("models/stopwords.txt");
		String line = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF8"));

			while ((line = reader.readLine()) != null) {

				if (line.equals(""))
					continue;
				stopwords.add(line.trim());

			}
		} catch (Exception e) {

		}

		return stopwords;

	}

}
