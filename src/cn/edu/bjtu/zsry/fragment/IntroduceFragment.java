package cn.edu.bjtu.zsry.fragment;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import cn.edu.bjtu.zsry.global.GlobalParam;
import cn.edu.bjtu.zsry.utils.NetWorkUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

public class IntroduceFragment extends Fragment {

	protected static final int UPDATEUI = 1;
	private View view;
	private LinearLayout ll_container;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATEUI:
				TextView tv;
				ll_loading.setVisibility(View.GONE);
				iv_icon1.setImageResource(R.drawable.introduce1);
				iv_icon2.setImageResource(R.drawable.introduce2);
				// for (int i = 0; i < spit.length; i++) {
				// tv = new TextView(getActivity());
				// tv.setText("		" + spit[i]);
				// tv.setTextSize(20);
				// ll_container.addView(tv);
				// }
				ArrayList<String> newLists = (ArrayList<String>) msg.obj;
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
				break;
			default:
				break;
			}
		};
	};
	private LinearLayout ll_loading;
	private ImageView iv_icon1;
	private ImageView iv_icon2;

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
		view = inflater.inflate(R.layout.introduce_fragment, null);
		ll_container = (LinearLayout) view.findViewById(R.id.ll_container);
		ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);
		iv_icon1 = (ImageView) view.findViewById(R.id.iv_icon1);
		iv_icon2 = (ImageView) view.findViewById(R.id.iv_icon2);
		ll_loading.setVisibility(View.VISIBLE);
		if (NetWorkUtils.checkNetState(getActivity())) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message message = Message.obtain();
					ArrayList<String> content = paseHtml(GlobalParam.INTRODUCE_URL);
					message.what = UPDATEUI;
					message.obj = content;
					handler.sendMessage(message);
				}
			}).start();
		} else {
			Toast.makeText(getActivity(), "网络联接超时", 0).show();
		}
		return view;
	}

	public static ArrayList<String> paseHtml(String url) {
		Elements newsElements = null;
		ArrayList<String> newsList = new ArrayList<String>();
		try {
			URL newsUrl = new URL(url);
			Document parse;
			parse = Jsoup.parse(newsUrl, 8000);
			newsElements = parse.getElementsByTag("p");
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

	public static String paseHtml1(String url) {
		Elements newsElements = null;
		List<News> newsLists = new ArrayList<News>();
		StringBuffer buffer = new StringBuffer();
		News news;
		try {
			URL newsUrl = new URL(url);
			Document parse;
			parse = Jsoup.parse(newsUrl, 2000);
			newsElements = parse.getElementsByTag("p");
			for (Element element : newsElements) {
				buffer.append(element.text());
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
