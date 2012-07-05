package edu.ptit.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.htmlcleaner.CleanerProperties;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import org.htmlcleaner.XPatherException;
import org.xml.sax.SAXException;

import edu.ptit.analysis.VnTokenizer;

public class Main {

	/**
	 * @param args
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws XPatherException
	 */

	static VnTokenizer vnTokenizer = new VnTokenizer();
	static File file = null;
	static BufferedWriter out = null;

	public static void main(String[] args) throws Exception {

		CleanerProperties props = new CleanerProperties();
		file = new File("D:/products.xml");
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				file, false), "UTF8"));

		out.write("<?xml version='1.0' encoding='UTF-8'?>");
		out.write("\n");
		out.write("<products>");
		out.write("\n");

		// set some properties to non-default values
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);

		String startUrl = "http://www.tinhte.vn/forums/71/";

		List<String> productlinks = new ArrayList<String>();
		List<String> productDetailLinks = new ArrayList<String>();
		for (int i = 1; i <= 10; i++) {

			productlinks.add(startUrl + "page-" + i);
		}

		for (String productlink : productlinks) {

			// do parsing
			TagNode tagNode = new HtmlCleaner(props)
					.clean(new URL(productlink));

			Object[] links = tagNode
					.evaluateXPath("//h3[@class='title']/a[1]/@href");

			for (Object link : links) {

				// System.out.println(link);
				productDetailLinks.add((String) link);
			}
		}

		for (String link : productDetailLinks) {
			System.out.println(link);

			// Proccess eached node

			String threadUrl = "http://www.tinhte.vn" + "/" + link;

			TagNode threadNode = new HtmlCleaner(props)
					.clean(new URL(threadUrl));

			// Title Node

			// Node danh gia
			// *[@id="content"]/div/div/div[1]/div/div[2]/h1
			Object[] titles = threadNode
					.evaluateXPath("//div[@id='content']/div/div/div[1]/div/div[2]/h1");
			TagNode titleNode = (TagNode) titles[0];

			Object[] navigaters = threadNode
					.evaluateXPath("//span[@class='pageNavHeader']");
			int pageNum = 0;
			if (navigaters.length > 0) {
				TagNode navigatersNode = (TagNode) navigaters[0];

				// System.out.println(navigatersNode.getText());
				String[] navi = navigatersNode.getText().toString().split("/");
				pageNum = Integer.parseInt(navi[1].trim());
			}
			out.write("<product>");
			out.write("\n");

			out.write("<title>" + titleNode.getText() + "</title>");
			out.write("\n");
			Object[] nodes = threadNode
					.evaluateXPath("//ol[@id='messageList']");

			TagNode node = (TagNode) nodes[0];

			Object[] commendnodes = node.evaluateXPath("//li");
			// //*[@id="post-4999031"]/div[2]/div[1]/article/blockquote
			// Get commend

			// NODE 1 is content

			TagNode commend = (TagNode) commendnodes[0];

			Object[] details = commend
					.evaluateXPath("//div[@class='messageContent']/article/blockquote");

			if (details.length > 0) {
				TagNode commendDeatails = (TagNode) details[0];

				out.write("<description>"
						+ vnTokenizer.remove(commendDeatails.getText()
								.toString()) + "</description>");
			}

			out.write("\n");
			out.write("<comments>");

			processComments(threadNode, true);

			// Process other

			// List<String> otherThreads = new ArrayList<String>();
			for (int i = 2; i <= pageNum; i++) {

				threadUrl = "http://www.tinhte.vn" + "/" + link + "page-" + i;

				threadNode = new HtmlCleaner(props).clean(new URL(threadUrl));

				processComments(threadNode, false);

			}

			out.write("</comments>");
			out.write("\n");
			out.write("</product>");
			out.write("\n");

		}

		out.write("\n");
		out.write("</products>");

		out.close();
	}

	public static void processComments(TagNode threadNode, boolean isFirst)
			throws Exception {
		// Node danh gia

		Object[] nodes = threadNode.evaluateXPath("//ol[@id='messageList']");

		TagNode node = (TagNode) nodes[0];

		Object[] commendnodes = node.evaluateXPath("//li");
		// //*[@id="post-4999031"]/div[2]/div[1]/article/blockquote
		// Get commend

		// NODE 1 is content

		TagNode commend = (TagNode) commendnodes[0];

		Object[] details = commend
				.evaluateXPath("//div[@class='messageContent']/article/blockquote");

		if (details.length > 0) {
			TagNode commendDeatails = (TagNode) details[0];

		}

		int start = 0;
		if (isFirst)
			start = 1;

		for (int i = start; i < commendnodes.length; i++) {

			// System.err.println("node[" + i + "] :");

			commend = (TagNode) commendnodes[i];

			details = commend
					.evaluateXPath("//div[@class='messageContent']/article/blockquote");

			if (details.length > 0) {
				TagNode commendDeatails = (TagNode) details[0];

				System.out.println("<comment>"
						+ vnTokenizer.remove(commendDeatails.getText()
								.toString()) + "</comment>");
				out.write("<comment>"
						+ vnTokenizer.remove(commendDeatails.getText()
								.toString()) + "</comment>");
				out.write("\n");
			}

		}

	}
}
