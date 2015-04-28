package cn.edu.bjtu.zsry.fragment;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

public class IntroduceFragment extends Fragment {

	protected static final int UPDATEUI = 1;
	protected static final int NET_ERROR = 2;
	private View view;
	private LinearLayout ll_container;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATEUI:
				String[] spit = (String[]) msg.obj;
				TextView tv;
				ll_loading.setVisibility(View.GONE);
				iv_icon1.setImageResource(R.drawable.introduce1);
				iv_icon2.setImageResource(R.drawable.introduce2);
				for (int i = 0; i < spit.length; i++) {
					tv = new TextView(getActivity());
					tv.setText("		" + spit[i]);
					tv.setTextSize(20);
					ll_container.addView(tv);
				}
				break;
			case NET_ERROR:
				Toast.makeText(getActivity(), "网络联接错误", 0).show();
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

		new Thread(new Runnable() {

			private Message message;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (NetWorkUtils.checkNetState(getActivity())) {
					String text = paseHtml(GlobalParam.INTRODUCE_URL);
					// paseHtml1(GlobalParam.INTRODUCE_URL);
					String[] split = text.split(" ");
					message = Message.obtain();
					message.obj = split;
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

	public static String paseHtml(String url) {
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

	public static List<String> paseHtml1(String url) {
		Elements newsElements = null;
		List<String> newsLists = new ArrayList<String>();
		News news;
		try {
			URL newsUrl = new URL(url);
			Document parse;
			parse = Jsoup.parse(newsUrl, 2000);
			newsElements = parse.getElementsByTag("p");
			for (Element element : newsElements) {
				// Elements elementsByTag = element.getElementsByTag("img");
				// for (Element element2 : elementsByTag) {
				// Element select = element2.select("img").first();
				// String linkHrefw = select.attr("src");
				// newsLists.add(linkHrefw);
				// }
				Elements siblingElements = element.siblingElements();
				for (Element element2 : siblingElements) {
					Elements siblingElements2 = element2.siblingElements();
					for (Element element3 : siblingElements2) {

					}
				}
			}
			System.out.println(newsLists.size() + "=================");
			return newsLists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
