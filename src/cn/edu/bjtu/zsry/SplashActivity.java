package cn.edu.bjtu.zsry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.edu.bjtu.zsry.global.GlobalParam;
import cn.edu.bjtu.zsry.utils.NetWorkUtils;

public class SplashActivity extends Activity {

	private LinearLayout ll_splash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		GlobalParam.SCREENWIDTH = displayMetrics.widthPixels;
		ll_splash = (LinearLayout) findViewById(R.id.ll_splash);
		// 实现splash界面的动画效果
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(2000);
		ll_splash.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

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
				// TODO Auto-generated method stub
				boolean isNetConnected = NetWorkUtils
						.checkNetState(SplashActivity.this);
				if (isNetConnected) {
					Intent intent = new Intent(SplashActivity.this,
							MainActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				} else {
					Toast.makeText(SplashActivity.this, "暂时没有网络链接", 0).show();
					Intent intent = new Intent(SplashActivity.this,
							MainActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();

				}
			}
		});
	}
}
