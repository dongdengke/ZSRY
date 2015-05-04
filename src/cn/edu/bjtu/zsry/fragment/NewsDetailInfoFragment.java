package cn.edu.bjtu.zsry.fragment;

import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bjtu.zsry.R;
import cn.edu.bjtu.zsry.bean.News;
import cn.edu.bjtu.zsry.utils.NetWorkUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

public class NewsDetailInfoFragment extends Fragment {
	protected static final int UPDATEUI = 1;
	protected static final int NET_ERROR = 2;
	private View view;
	private LinearLayout ll_loading;
	private LinearLayout ll_container;
	private String baseUrl;
	private String newsId;
	private String flag;
	private News news;
	// 存放html中包含的图片的路径
	private ArrayList<String> iamgeUrl = new ArrayList<String>();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATEUI:
				ArrayList<String> newLists = (ArrayList<String>) msg.obj;
				TextView tv;
				tv_title.setText(news.getTitle());
				ll_loading.setVisibility(View.GONE);
				for (String string : newLists) {
					if (string.startsWith("/")) {// 加载图片
						final ImageView imageView = new ImageView(getActivity());
						RequestQueue mRequestQueue = Volley
								.newRequestQueue(getActivity());
						final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(
								20);
						ImageCache imageCache = new ImageCache() {
							@Override
							public void putBitmap(String key, Bitmap value) {
								mImageCache.put(key, value);
							}

							@Override
							public Bitmap getBitmap(String key) {
								return mImageCache.get(key);
							}
						};
						ImageLoader mImageLoader = new ImageLoader(
								mRequestQueue, imageCache);
						// imageView是一个ImageView实例
						// ImageLoader.getImageListener的第二个参数是默认的图片resource
						// 第三个参数是请求失败时候的资源id，可以指定为0
						ImageListener listener = ImageLoader.getImageListener(
								imageView, android.R.drawable.ic_menu_rotate,
								android.R.drawable.ic_delete);
						ImageContainer imageContainer = mImageLoader.get(
								"http://rjxy.bjtu.edu.cn" + string, listener);
						ll_container.addView(imageView);
					} else {// 加载文字
						tv = new TextView(getActivity());
						tv.setText("		" + string);
						tv.setTextSize(18);
						ll_container.addView(tv);
					}
				}
				tv = new TextView(getActivity());
				tv.setText("发布人：北京交通大学软件学院(SCHOOL OF SOFTWARE BJTU) — 国际交流中心(INTERNATIONAL EXCHANGE CENTER)");
				tv.setTextSize(14);
				ll_container.addView(tv);
				tv = new TextView(getActivity());
				tv.setText("发布时间：" + news.getDate());
				tv.setTextSize(14);
				ll_container.addView(tv);

				break;
			case NET_ERROR:
				Toast.makeText(getActivity(), "网络联接错误", 0).show();
				break;

			default:
				break;
			}
		};
	};
	private TextView tv_title;
	private RequestQueue queue;

	public NewsDetailInfoFragment(String baseUrl, String newsId, String flag,
			News news) {
		super();
		this.baseUrl = baseUrl;
		this.newsId = newsId;
		this.flag = flag;
		this.news = news;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.employee_detail_info, null);
		ll_container = (LinearLayout) view.findViewById(R.id.ll_container);
		ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		final String urlStr = baseUrl + newsId + "&flag=" + flag;
		ll_loading.setVisibility(View.VISIBLE);
		queue = Volley.newRequestQueue(getActivity());
		new Thread(new Runnable() {
			Message message;

			@Override
			public void run() {
				if (NetWorkUtils.checkNetState(getActivity())) {
					ArrayList<String> newLists = paseHtml(urlStr, "div");
					if (newLists.isEmpty()) {
						newLists = paseHtmlContainSpan(urlStr, "span");
					}
					message = Message.obtain();
					message.obj = newLists;
					message.what = UPDATEUI;
					handler.sendMessage(message);
				} else {
					message.what = NET_ERROR;
					handler.sendMessage(message);
				}
			}
		}).start();
		return view;
	}

	public static ArrayList<String> paseHtml(String url, String tag) {
		Elements newsElements = null;
		ArrayList<String> newsList = new ArrayList<String>();
		try {
			URL newsUrl = new URL(url);
			Document parse;
			parse = Jsoup.parse(newsUrl, 8000);
			newsElements = parse.getElementsByTag(tag);
			for (Element element : newsElements) {
				Elements elementsByTag = element.getElementsByTag("img");
				for (Element element2 : elementsByTag) {
					String attr = element2.attr("src");
					if (attr != null) {
						newsList.add(attr);
					}
				}
				newsList.add(element.text());
			}
			return newsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<String> paseHtmlContainSpan(String url, String tag) {
		Elements newsElements = null;
		ArrayList<String> newsList = new ArrayList<String>();
		try {
			URL newsUrl = new URL(url);
			Document parse;
			parse = Jsoup.parse(newsUrl, 8000);
			newsElements = parse.getElementsByTag(tag);
			for (Element element : newsElements) {
				Element parent = element.parent();
				if (parent.tagName().equals("p")
						|| parent.tagName().equals("div")) {
					Elements elementsByTag = element.getElementsByTag("img");
					for (Element element2 : elementsByTag) {
						String attr = element2.attr("src");
						if (attr != null) {
							newsList.add(attr);
						}
					}
					newsList.add(element.text());
				}
			}
			return newsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<String> paseHtml1(String url) {
		Elements newsElements = null;
		ArrayList<String> newsList = new ArrayList<String>();
		// StringBuffer buffer = new StringBuffer();
		try {
			URL newsUrl = new URL(url);
			Document parse;
			parse = Jsoup.parse(newsUrl, 8000);
			newsElements = parse.getElementsByTag("p");
			for (Element element : newsElements) {
				// buffer.append(element.text());
				newsList.add(element.text());
			}
			return newsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
