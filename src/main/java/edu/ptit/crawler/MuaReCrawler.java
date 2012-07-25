package edu.ptit.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class MuaReCrawler {

	public void crawler() throws Exception {

		CleanerProperties props = new CleanerProperties();
		// set some properties to non-default values
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);

		String startUrl = "http://www.muare.vn";

		List<String> productlinks = new ArrayList<String>();

		// do parsing
		TagNode tagNode = new HtmlCleaner(props).clean(new URL(startUrl));

		Object[] nodes = tagNode.evaluateXPath("//div[6]/div/div/table[3]");

		TagNode node = (TagNode) nodes[0];

		Object[] links = node
				.evaluateXPath("//div[@class='subforum_title']/a[1]/@href");

		for (Object link : links) {

			// System.out.println(link);
			productlinks.add(startUrl + "/" + link);
			System.err.println(link);
			processThread((String) startUrl + "/" + link, props);

		}

	}

	private void processThread(String url, CleanerProperties props)
			throws Exception {

		System.out.println(url);
		// do parsing
		TagNode tagNode = new HtmlCleaner(props).clean(new URL(url));

		Object[] nodes = tagNode
				.evaluateXPath("//*[@id='inlinemodform']/table[2]/tbody");

		//*[@id="inlinemodform"]/table[2]/tbody/tr[21]/td[2]/div/a
		TagNode node = (TagNode) nodes[0];

		Object[] links = node.evaluateXPath("//tr[@class='Gray_content_tr']");

		for (Object link : links) {

			TagNode aNode = (TagNode)link;
			
			System.out.println(aNode.getText());
		}

	}

	public static void main(String[] args) throws Exception {

		MuaReCrawler crawler = new MuaReCrawler();

		crawler.crawler();
	}

}
