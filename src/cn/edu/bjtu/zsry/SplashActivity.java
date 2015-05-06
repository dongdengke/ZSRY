package cn.edu.bjtu.zsry;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bjtu.zsry.global.GlobalParam;
import cn.edu.bjtu.zsry.utils.NetWorkUtils;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

public class SplashActivity extends Activity {

	private RelativeLayout ll_splash;
	private SharedPreferences preferences;
	private int count;
	private ViewPager new_version_viewpager;
	private Button startBtn;
	private TextView tv_new_text;

	private ArrayList<View> views;
	private int oldPosition = 0;// 记录上一次点的位置
	private int currentItem; // 当前页面
	private ArrayList<View> dots;
	private ViewPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SDKInitializer.initialize(getApplicationContext());
		LocationClient locationClient = new LocationClient(
				getApplicationContext());
		GlobalParam.LOCATIONCLIENT = locationClient;
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.SetIgnoreCacheException(true);
		option.setIsNeedAddress(true);
		locationClient.setLocOption(option);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		GlobalParam.SCREENWIDTH = displayMetrics.widthPixels;
		preferences = getSharedPreferences("count", MODE_WORLD_READABLE);
		count = preferences.getInt("count", 0);
		if (count == 0) {
			setContentView(R.layout.splash);
			new_version_viewpager = (ViewPager) findViewById(R.id.new_version_viewpager);
			View intro_1 = View.inflate(this, R.layout.intro_1, null);
			View intro_2 = View.inflate(this, R.layout.intro_2, null);
			View intro_3 = View.inflate(this, R.layout.intro_3, null);
			View intro_4 = View.inflate(this, R.layout.intro_4, null);
			final View intro_5 = View.inflate(this, R.layout.intro_5, null);
			startBtn = (Button) intro_5.findViewById(R.id.startBtn);
			tv_new_text = (TextView) intro_5.findViewById(R.id.tv_new_text);
			views = new ArrayList<View>();
			views.add(intro_1);
			views.add(intro_2);
			views.add(intro_3);
			views.add(intro_4);
			views.add(intro_5);
			dots = new ArrayList<View>();
			dots.add(findViewById(R.id.dot_0));
			dots.add(findViewById(R.id.dot_1));
			dots.add(findViewById(R.id.dot_2));
			dots.add(findViewById(R.id.dot_3));
			dots.add(findViewById(R.id.dot_4));
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
						public void onPageScrolled(int arg0, float arg1,
								int arg2) {
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
					ScaleAnimation animation = new ScaleAnimation(1.0f, 4.0f,
							1.0f, 4.0f, Animation.RELATIVE_TO_SELF, 0.5f,
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
									0, 360, RotateAnimation.RELATIVE_TO_SELF,
									0.5f, RotateAnimation.RELATIVE_TO_SELF,
									0.5f);
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
											loadMainUI();
										}
									});
						}
					});

				}
			});
		} else {
			setContentView(R.layout.splash_0);
			ll_splash = (RelativeLayout) findViewById(R.id.ll_splash);
			// 实现splash界面的动画效果
			AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
			aa.setDuration(2000);
			ll_splash.startAnimation(aa);
			aa.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					loadMainUI();
				}
			});
		}

		Editor editor = preferences.edit();
		editor.putInt("count", ++count);
		editor.commit();
	}

	private void loadMainUI() {
		boolean isNetConnected = NetWorkUtils
				.checkNetState(SplashActivity.this);
		if (isNetConnected) {
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			SplashActivity.this.finish();
		} else {
			Toast.makeText(SplashActivity.this, "暂时没有网络链接", 0).show();
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			SplashActivity.this.finish();
		}
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
