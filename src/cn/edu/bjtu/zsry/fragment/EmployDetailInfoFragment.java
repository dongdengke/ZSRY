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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bjtu.zsry.R;
import cn.edu.bjtu.zsry.bean.News;
import cn.edu.bjtu.zsry.utils.NetWorkUtils;

public class EmployDetailInfoFragment extends Fragment {
	protected static final int UPDATEUI = 1;
	protected static final int NET_ERROR = 2;
	private View view;
	private LinearLayout ll_loading;
	private LinearLayout ll_container;
	private String baseUrl;
	private String newsId;
	private String flag;
	private News news;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATEUI:
				String[] spit = (String[]) msg.obj;
				TextView tv;
				tv_title.setText(news.getTitle());
				ll_loading.setVisibility(View.GONE);
				for (int i = 0; i < spit.length; i++) {
					tv = new TextView(getActivity());
					tv.setText("		" + spit[i]);
					tv.setTextSize(20);
					ll_container.addView(tv);
				}
				tv = new TextView(getActivity());
				tv.setText("发布人：北京交通大学软件学院(SCHOOL OF SOFTWARE BJTU) — 就业办公室(EMPLOYMENT OFFICE)");
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

	public EmployDetailInfoFragment(String baseUrl, String newsId, String flag,
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
		new Thread(new Runnable() {
			Message message = Message.obtain();;

			@Override
			public void run() {
				if (NetWorkUtils.checkNetState(getActivity())) {
					String text = paseHtml(urlStr);
					// paseHtml1(GlobalParam.INTRODUCE_URL);
					String[] split = text.split(" ");
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

}
