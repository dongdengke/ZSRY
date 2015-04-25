package cn.edu.bjtu.zsry;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.bjtu.zsry.bean.News;
import cn.edu.bjtu.zsry.fragment.BenkeshengFragment;
import cn.edu.bjtu.zsry.fragment.InternationalFragment;
import cn.edu.bjtu.zsry.fragment.MoreFragment;
import cn.edu.bjtu.zsry.fragment.NewsFragment;
import cn.edu.bjtu.zsry.fragment.YanjiushengFragment;
import cn.edu.bjtu.zsry.global.GlobalParam;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private ViewPager viewpager;
	private List<Fragment> pagers = new ArrayList<Fragment>();
	private ImageView selector;
	private int currentPage = 0;
	private TextView tv_news;
	private TextView tv_benkesheng;
	private TextView tv_yanjiusheng;
	private TextView tv_international;
	private TextView tv_other;
	// 校园新闻的集合
	private List<News> newsLists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		tv_news = (TextView) findViewById(R.id.tv_news);
		tv_benkesheng = (TextView) findViewById(R.id.tv_benkesheng);
		tv_yanjiusheng = (TextView) findViewById(R.id.tv_yanjiusheng);
		tv_international = (TextView) findViewById(R.id.tv_international);
		tv_other = (TextView) findViewById(R.id.tv_other);
		initPagers();
		MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		selector = (ImageView) findViewById(R.id.iv_selector);
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
				// TODO Auto-generated method stub
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

	}

	/**
	 * 初始化导航和viewpager之间的下划线
	 */
	private void initSelector() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		GlobalParam.SCREENWIDTH = displayMetrics.widthPixels;
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

	class MyAdapter extends FragmentPagerAdapter {
		private FragmentManager fragmentManager;

		public MyAdapter(FragmentManager fm) {
			super(fm);
			fragmentManager = fm;
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
