package cn.edu.bjtu.zsry;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;
import cn.edu.bjtu.zsry.fragment.HomeFragment;
import cn.edu.bjtu.zsry.fragment.MenuFragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
	public static SlidingMenu menu;
	private HomeFragment homeFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.menu);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content);
		homeFragment = new HomeFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, homeFragment).commit();
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		// 3 设置滑动菜单出来之后，内容页，显示的剩余宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 4 设置滑动菜单的阴影 设置阴影，阴影需要在开始的时候，特别暗，慢慢的变淡
		// menu.setShadowDrawable(R.drawable.bg_brand);
		menu.setBackgroundResource(R.drawable.bg_brand);
		// 5 设置阴影的宽度
		menu.setShadowWidth(R.dimen.shadow_width);
		// 6 设置滑动菜单的范围
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		MenuFragment fragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, fragment).commit();
	}

	public void switchFragment(Fragment f) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, f).commit();
		// 自动切换
		// menu.toggle();
	}

	public void menueSwitchFragment(Fragment f) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, f).commit();
		// 自动切换
		menu.toggle();
	}

	DoubleClickExitHelper doubleClick = new DoubleClickExitHelper(this);

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switchFragment(new HomeFragment());
			return doubleClick.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	class DoubleClickExitHelper {
		private final Activity mActivity;
		private boolean isOnKeyBacking;
		private Handler mHandler;
		private Toast mBackToast;

		public DoubleClickExitHelper(Activity activity) {
			mActivity = activity;
			mHandler = new Handler(Looper.getMainLooper());
		}

		/**
		 * Activity onKeyDown事件
		 * */
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode != KeyEvent.KEYCODE_BACK) {
				return false;
			}
			if (isOnKeyBacking) {
				mHandler.removeCallbacks(onBackTimeRunnable);
				if (mBackToast != null) {
					mBackToast.cancel();
				}
				mActivity.finish();
				return true;
			} else {
				isOnKeyBacking = true;
				if (mBackToast == null) {
					mBackToast = Toast.makeText(mActivity, "再按一次返回", 2000);
				}
				mBackToast.show();
				// 延迟两秒的时间，把Runable发出去
				mHandler.postDelayed(onBackTimeRunnable, 2000);
				return true;
			}
		}

		private Runnable onBackTimeRunnable = new Runnable() {

			@Override
			public void run() {
				isOnKeyBacking = false;
				if (mBackToast != null) {
					mBackToast.cancel();
				}
			}
		};
	}
}
