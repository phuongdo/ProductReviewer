package edu.ptit.analysis;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import edu.pit.datalayer.DataReader;

public class VnTokenizer {
	private Set<String> stopwords = new HashSet<String>();

	public VnTokenizer() {

		stopwords = DataReader.getInstance().getStopwords();

	}

	public String remove(String in) {

		String out = "";
		StringTokenizer tokenizer = new StringTokenizer(in);
		while (tokenizer.hasMoreTokens()) {

			String token = tokenizer.nextToken();

			if (!stopwords.contains(token)) {
				token = this.filter(token);
				out += token + " ";

			}
		}

		return out;

	}

	private String filter(String in) {

		in = in.replaceAll("&quot;", "");
		in = in.replaceAll(":banghead:", "");
		in = in.replaceAll(":d", "");
		in = in.replaceAll("$", "");

		return in;
	}

	public static void main(String[] args) {

		String str = "";
	}

}
