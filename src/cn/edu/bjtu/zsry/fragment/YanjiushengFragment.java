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
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.edu.bjtu.zsry.R;
import cn.edu.bjtu.zsry.bean.News;
import cn.edu.bjtu.zsry.global.GlobalParam;
import cn.edu.bjtu.zsry.utils.NetWorkUtils;
import cn.edu.bjtu.zsry.view.FocuesedView;

public class YanjiushengFragment extends Fragment {
	protected static final int GET_NEWS_INFO = 1;
	private View view;
	private ListView listview;
	private List<News> newLists;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_NEWS_INFO:
				ll_loading.setVisibility(View.GONE);
				MyListviewAdapter adapter = new MyListviewAdapter();
				listview.setAdapter(adapter);
				break;

			default:
				break;
			}
		};
	};
	private LinearLayout ll_loading;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		System.out.println("onActivityCreated");

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("onCreate");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.yanjiusheng_fragment, null);
		listview = (ListView) view.findViewById(R.id.benke_listview);
		ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);
		System.out.println("onCreateView");
		ll_loading.setVisibility(View.VISIBLE);
		if (NetWorkUtils.checkNetState(getActivity())) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					newLists = paseHtml(GlobalParam.YNAJIUSHENG_NEWS_FIRST);
					Message msg = Message.obtain();
					msg.what = GET_NEWS_INFO;
					handler.sendMessage(msg);
				}
			}).start();
		} else {
			Toast.makeText(getActivity(), "网络联接超时", 1).show();
			ll_loading.setVisibility(View.GONE);
		}
		return view;
	}

	private class MyListviewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newLists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return newLists.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			ViewHolder holder = null;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getActivity(), R.layout.news_listview_item,
						null);
				holder = new ViewHolder();
				view.setTag(holder);
			}
			holder.tv_date = (FocuesedView) view.findViewById(R.id.tv_date);
			holder.tv_title = (FocuesedView) view.findViewById(R.id.tv_title);
			News news = newLists.get(position);
			holder.tv_date.setText(news.getDate());
			holder.tv_title.setText(news.getTitle());
			return view;
		}

	}

	class ViewHolder {
		FocuesedView tv_date;
		FocuesedView tv_title;
	}

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
				Elements elementsByTag = element.getElementsByTag("a");
				for (Element element2 : elementsByTag) {
					Element select = element2.select("a").first();
					String linkHrefw = select.attr("href");
					if (linkHrefw.equals("#")) {
						String text = element.text();
						news = new News();
						news.setTitle(text);
						newsLists.add(news);
					}
				}
			}
			return newsLists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
