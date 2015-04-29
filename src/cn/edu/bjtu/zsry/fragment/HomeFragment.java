package cn.edu.bjtu.zsry.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.bjtu.zsry.MainActivity;
import cn.edu.bjtu.zsry.R;
import cn.edu.bjtu.zsry.global.GlobalParam;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class HomeFragment extends Fragment implements OnClickListener {

	private View view;
	private ViewPager viewpager;
	private List<Fragment> pagers = new ArrayList<Fragment>();
	private ImageView selector;
	private int currentPage = 0;
	private TextView tv_news;
	private TextView tv_benkesheng;
	private TextView tv_yanjiusheng;
	private TextView tv_international;
	private TextView tv_other;
	private SlidingMenu menu;
	private MyAdapter adapter;
	private MainActivity activity;

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
		view = inflater.inflate(R.layout.activity_main, null);
		viewpager = (ViewPager) view.findViewById(R.id.viewpager);
		tv_news = (TextView) view.findViewById(R.id.tv_news);
		tv_benkesheng = (TextView) view.findViewById(R.id.tv_benkesheng);
		tv_yanjiusheng = (TextView) view.findViewById(R.id.tv_yanjiusheng);
		tv_international = (TextView) view.findViewById(R.id.tv_international);
		tv_other = (TextView) view.findViewById(R.id.tv_other);
		activity = new MainActivity();
		initPagers();
		adapter = new MyAdapter(getActivity().getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		selector = (ImageView) view.findViewById(R.id.iv_selector);
		initSelector();
		// 初始化标题内容
		viewpager.setCurrentItem(0);
		tv_news.setTextColor(Color.RED);
		tv_news.setOnClickListener(this);
		tv_benkesheng.setOnClickListener(this);
		tv_yanjiusheng.setOnClickListener(this);
		tv_international.setOnClickListener(this);
		tv_other.setOnClickListener(this);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				tv_news.setTextColor(Color.BLACK);
				tv_benkesheng.setTextColor(Color.BLACK);
				tv_yanjiusheng.setTextColor(Color.BLACK);
				tv_international.setTextColor(Color.BLACK);
				tv_other.setTextColor(Color.BLACK);
				activity.menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
				switch (position) {
				case 0:
					tv_news.setTextColor(Color.RED);
					break;
				case 1:
					tv_benkesheng.setTextColor(Color.RED);
					break;
				case 2:
					tv_yanjiusheng.setTextColor(Color.RED);
					break;
				case 3:
					tv_international.setTextColor(Color.RED);
					break;
				case 4:
					tv_other.setTextColor(Color.RED);
					break;
				}
				TranslateAnimation animation = new TranslateAnimation(
						currentPage * GlobalParam.SCREENWIDTH / 5, position
								* GlobalParam.SCREENWIDTH / 5, 0, 0);
				animation.setFillAfter(true);
				animation.setDuration(300);
				selector.startAnimation(animation);
				currentPage = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		return view;
	}

	/**
	 * 初始化导航和viewpager之间的下划线
	 */
	private void initSelector() {

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.id_category_selector);
		int bitmapWidth = bitmap.getWidth();
		int left = (GlobalParam.SCREENWIDTH / 5 - bitmapWidth) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(left, 0);
		selector.setImageMatrix(matrix);
	}

	private void initPagers() {
		pagers.add(new NewsFragment());
		pagers.add(new BenkeshengFragment());
		pagers.add(new YanjiushengFragment());
		pagers.add(new InternationalFragment());
		pagers.add(new MoreFragment());
	}

	class MyAdapter extends FragmentStatePagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pagers.size();
		}

		@Override
		public Fragment getItem(int postion) {
			Fragment fm = null;
			switch (postion) {
			case 0:
				fm = new NewsFragment();
				break;
			case 1:
				fm = new BenkeshengFragment();
				break;
			case 2:
				fm = new YanjiushengFragment();
				break;
			case 3:
				fm = new InternationalFragment();
				break;
			case 4:
				fm = new MoreFragment();
				break;
			}
			return fm;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_news:
			viewpager.setCurrentItem(0);
			break;
		case R.id.tv_benkesheng:
			viewpager.setCurrentItem(1);
			break;
		case R.id.tv_yanjiusheng:
			viewpager.setCurrentItem(2);
			break;
		case R.id.tv_international:
			viewpager.setCurrentItem(3);
			break;
		case R.id.tv_other:
			viewpager.setCurrentItem(4);
			break;
		}
	}

}
