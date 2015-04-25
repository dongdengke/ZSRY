package cn.edu.bjtu.zsry.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.bjtu.zsry.bean.News;

/**
 * 解析html css的工具类
 * 
 * @author dong
 *
 */
public class ParseUtils {
	public static List<News> paseHtml(String url) {
		Elements newsElements = null;
		List<News> newsLists = new ArrayList<News>();
		News news;
		try {
			URL newsUrl = new URL(url);
			Document parse;
			parse = Jsoup.parse(newsUrl, 2000);
			newsElements = parse.getElementsByTag("li");
			for (Element element : newsElements) {
				String text = element.text();
				String[] split = text.split(" ");
				news = new News();
				news.setDate(split[0]);
				news.setTitle(split[1]);
				newsLists.add(news);
			}
			return newsLists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
