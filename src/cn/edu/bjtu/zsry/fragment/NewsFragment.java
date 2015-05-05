package cn.edu.bjtu.zsry.fragment;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bjtu.zsry.MainActivity;
import cn.edu.bjtu.zsry.R;
import cn.edu.bjtu.zsry.bean.News;
import cn.edu.bjtu.zsry.global.GlobalParam;
import cn.edu.bjtu.zsry.pulltorefresh.RefreshableView;
import cn.edu.bjtu.zsry.pulltorefresh.RefreshableView.PullToRefreshListener;
import cn.edu.bjtu.zsry.utils.NetWorkUtils;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class NewsFragment extends Fragment {
	protected static final int GET_NEWS_INFO = 1;
	// private static final int DETAIL_INFO = 2;
	private View view;
	private ListView listview;
	private List<News> newLists;
	private String baseUrlStr = "http://rjxy.bjtu.edu.cn/forLogin/news_apply_show.jsp?id=";
	private MyListviewAdapter adapter;
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_NEWS_INFO:
				ll_loading.setVisibility(View.GONE);
				adapter = new MyListviewAdapter();
				listview.setAdapter(adapter);
				break;

			default:
				break;
			}
		};
	};
	private LinearLayout ll_loading;
	private TextView tv_loading_more;
	private RefreshableView refreshable_view;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		System.out.println("onActivityCreated");

	}

	private List<News> paseHtml(String url) {
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
				String text = element.text();
				String[] split = text.split(" ");
				news = new News();
				news.setDate(split[0]);
				news.setTitle(split[1]);
				for (Element element2 : elementsByTag) {
					Element select = element2.select("a").first();
					String attr = select.attr("onClick");
					attr = attr.substring(12, 17);
					news.setId(attr);
				}
				newsLists.add(news);
			}
			return newsLists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.news_fragment, null);
		listview = (ListView) view.findViewById(R.id.listview);
		ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);
		tv_loading_more = (TextView) view.findViewById(R.id.tv_loading_more);
		System.out.println("onCreateView");
		ll_loading.setVisibility(View.VISIBLE);
		if (NetWorkUtils.checkNetState(getActivity())) {

			new Thread(new Runnable() {
				private Message msg;

				@Override
				public void run() {
					newLists = paseHtml(GlobalParam.NEWSURL);
					msg = Message.obtain();
					msg.what = GET_NEWS_INFO;
					handler.sendMessage(msg);
				}
			}).start();
		} else {
			Toast.makeText(getActivity(), "网络联接超时", 1).show();
			ll_loading.setVisibility(View.GONE);
		}
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == newLists.size() - 1) {
						tv_loading_more.setVisibility(View.VISIBLE);
					} else if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
						tv_loading_more.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
		refreshable_view = (RefreshableView) view
				.findViewById(R.id.new_refreshable_view);
		refreshable_view.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(2000);
					refreshable_view.finishRefreshing();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 0);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("SetJavaScriptEnabled")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				News news = (News) listview.getItemAtPosition(position);
				final String newsId = news.getId();
				final String flag = news.getFlag();
				if (NetWorkUtils.checkNetState(getActivity())) {
					// Intent intent = new Intent(getActivity(),
					// NewsDetailInfoActivity.class);
					// intent.putExtra("newsId", newsId);
					// intent.putExtra("baseUrl", baseUrlStr);
					// startActivity(intent);
					if (getActivity() instanceof MainActivity) {
						MainActivity activity = (MainActivity) getActivity();
						activity.menu
								.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
						NewsDetailInfoFragment newsDetailInfoFragment = new NewsDetailInfoFragment(
								baseUrlStr, newsId, flag, news);
						((MainActivity) getActivity())
								.switchFragment(newsDetailInfoFragment);
					}
				} else {
					Toast.makeText(getActivity(), "网络链接超时", 1).show();
				}
			}
		});
		return view;
	}

	/**
	 * 根据url解析新闻详情
	 * 
	 * @param urlStr
	 */
	private ArrayList<String> parseNewsDetail(String urlStr) {
		try {
			ArrayList<String> newsArray = new ArrayList<String>();
			URL url = new URL(urlStr);
			Document doc = Jsoup.parse(url, 3000);
			Elements elements = doc.getElementsByTag("div");
			for (Element element : elements) {
				String text = element.text();
				newsArray.add(text);
				Elements elementsByTag = element.getElementsByTag("img");
				for (Element element2 : elementsByTag) {
					String attr = element2.attr("src");
					newsArray.add("rjxy.bjtu.edu.cn" + attr);
				}
			}
			return newsArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
			holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			News news = newLists.get(position);
			holder.tv_date.setText(news.getDate());
			holder.tv_title.setText(news.getTitle());
			return view;
		}

	}

	class ViewHolder {
		TextView tv_date;
		TextView tv_title;
	}
}
