package cn.edu.bjtu.zsry.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import cn.edu.bjtu.zsry.MainActivity;
import cn.edu.bjtu.zsry.R;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class NewVersionFeature extends Fragment {

	private ViewPager new_version_viewpager;
	private ArrayList<View> views;
	private int oldPosition = 0;// 记录上一次点的位置
	private int currentItem; // 当前页面
	private ArrayList<View> dots;
	private ViewPagerAdapter adapter;
	private Button startBtn;
	private TextView tv_new_text;

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
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.new_version_feature, null);
		new_version_viewpager = (ViewPager) view
				.findViewById(R.id.new_version_viewpager);
		View intro_1 = View.inflate(getActivity(), R.layout.intro_1, null);
		View intro_2 = View.inflate(getActivity(), R.layout.intro_2, null);
		View intro_3 = View.inflate(getActivity(), R.layout.intro_3, null);
		View intro_4 = View.inflate(getActivity(), R.layout.intro_4, null);
		final View intro_5 = View
				.inflate(getActivity(), R.layout.intro_5, null);
		startBtn = (Button) intro_5.findViewById(R.id.startBtn);
		tv_new_text = (TextView) intro_5.findViewById(R.id.tv_new_text);
		views = new ArrayList<View>();
		views.add(intro_1);
		views.add(intro_2);
		views.add(intro_3);
		views.add(intro_4);
		views.add(intro_5);
		dots = new ArrayList<View>();
		dots.add(view.findViewById(R.id.dot_0));
		dots.add(view.findViewById(R.id.dot_1));
		dots.add(view.findViewById(R.id.dot_2));
		dots.add(view.findViewById(R.id.dot_3));
		dots.add(view.findViewById(R.id.dot_4));
		adapter = new ViewPagerAdapter();
		new_version_viewpager.setAdapter(adapter);
		new_version_viewpager
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// TODO Auto-generated method stub
						dots.get(oldPosition).setBackgroundResource(
								R.drawable.dot_normal);
						dots.get(position).setBackgroundResource(
								R.drawable.dot_focused);

						oldPosition = position;
						currentItem = position;
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
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 字体想外移动动画
				ScaleAnimation animation = new ScaleAnimation(1.0f, 4.0f, 1.0f,
						4.0f, Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				animation.setDuration(1000);// 设置动画持续时间
				tv_new_text.startAnimation(animation);
				startBtn.setVisibility(View.GONE);
				animation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// 开始播放背景向左右滑动
						RotateAnimation rotateAnimation = new RotateAnimation(
								0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
								RotateAnimation.RELATIVE_TO_SELF, 0.5f);
						rotateAnimation.setDuration(2000);
						tv_new_text.startAnimation(rotateAnimation);
						rotateAnimation
								.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(
											Animation animation) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onAnimationRepeat(
											Animation animation) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onAnimationEnd(
											Animation animation) {
										// TODO Auto-generated method stub
										if (getActivity() instanceof MainActivity) {
											MainActivity activity = (MainActivity) getActivity();
											activity.menu
													.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
											((MainActivity) getActivity())
													.switchFragment(new SettingFragment());
										}

									}
								});
					}
				});

			}
		});
		return view;
	}

	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		// 是否是同一张图片
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			// TODO Auto-generated method stub
			view.removeView(views.get(position));

		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			view.addView(views.get(position));
			return views.get(position);
		}
	}

}
