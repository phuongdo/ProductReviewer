package edu.ptit.crawler;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.htmlcleaner.CleanerProperties;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import org.htmlcleaner.XPatherException;
import org.xml.sax.SAXException;

public class Main {

	/**
	 * @param args
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws XPatherException
	 */

	public static void main(String[] args) throws Exception {

		CleanerProperties props = new CleanerProperties();

		// set some properties to non-default values
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);

		String startUrl = "http://www.tinhte.vn/forums/71/";
		// do parsing
		TagNode tagNode = new HtmlCleaner(props).clean(new URL(startUrl));

		Object[] links = tagNode
				.evaluateXPath("//h3[@class='title']/a[1]/@href");

		for (Object link : links) {
			System.out.println(link);

			// Proccess eached node

			String threadUrl = "http://www.tinhte.vn" + "/" + link;

			TagNode threadNode = new HtmlCleaner(props)
					.clean(new URL(threadUrl));
			// Node danh gia

			Object[] nodes = threadNode
					.evaluateXPath("//ol[@id='messageList']");

			TagNode node = (TagNode) nodes[0];

			Object[] commendnodes = node.evaluateXPath("//li");
			// //*[@id="post-4999031"]/div[2]/div[1]/article/blockquote
			// Get commend

			for (int i = 1; i < commendnodes.length; i++) {

				// System.err.println("node[" + i + "] :");

				TagNode commend = (TagNode) commendnodes[i];

				TagNode commendDeatails = (TagNode) commend
						.evaluateXPath("//div[@class='messageContent']/article/blockquote")[0];

				System.out.println("<comment>" + commendDeatails.getText()
						+ "</comment>");
				// System.out.println("id: "+commend.getAttributeByName("id"));

				// TagNode commend = (TagNode) ((TagNode) commendnode)
				// .evaluateXPath("//div[@class='messageContent']/article/blockquote")[0];
				// System.out.println(commend.getText());

			}

			break;
		}

	}
}
