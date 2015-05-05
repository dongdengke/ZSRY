package cn.edu.bjtu.zsry;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.edu.bjtu.zsry.fragment.HomeFragment;
import cn.edu.bjtu.zsry.fragment.MenuFragment;
import cn.edu.bjtu.zsry.serviced.ReceiveMsgService;
import cn.edu.bjtu.zsry.serviced.ReceiveMsgService.GetConnectState;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
	public static SlidingMenu menu;
	private HomeFragment homeFragment;
	ReceiveMsgService receiveMsgService;
	private LinearLayout ll_no_net;
	private boolean conncetState = true; // 记录当前连接状态，因为广播会接收所有的网络状态改变wifi/3g等等，所以需要一个标志记录当前状态

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.menu);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content);
		ll_no_net = (LinearLayout) findViewById(R.id.ll_no_net);
		homeFragment = new HomeFragment();
		getSupportFragmentManager().beginTransaction().addToBackStack("add ");
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, homeFragment).commit();
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		// 3 设置滑动菜单出来之后，内容页，显示的剩余宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 4 设置滑动菜单的阴影 设置阴影，阴影需要在开始的时候，特别暗，慢慢的变淡
		// menu.setShadowDrawable(R.drawable.bg_brand);
		menu.setBackgroundResource(R.drawable.bg);
		// 5 设置阴影的宽度
		menu.setShadowWidth(R.dimen.shadow_width);
		// 6 设置滑动菜单的范围
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		MenuFragment fragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, fragment).commit();
		bind();
		ll_no_net.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Settings.ACTION_SETTINGS));
			}
		});
	}

	private void bind() {
		Intent intent = new Intent(MainActivity.this, ReceiveMsgService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ReceiveMsgService receiveMsgService = ((ReceiveMsgService.MyBinder) service)
					.getService();
			receiveMsgService.setOnGetConnectState(new GetConnectState() { // 添加接口实例获取连接状态
						@Override
						public void GetState(boolean isConnected) {
							if (conncetState != isConnected) { // 如果当前连接状态与广播服务返回的状态不同才进行通知显示
								conncetState = isConnected;
								if (conncetState) {// 已连接
									handler.sendEmptyMessage(1);
								} else {// 未连接
									handler.sendEmptyMessage(2);
								}
							}
						}
					});
		}
	};

	private void unbind() {
		if (receiveMsgService != null) {
			unbindService(serviceConnection);
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:// 已连接
				ll_no_net.setVisibility(View.GONE);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, new HomeFragment())
						.commit();
				break;
			case 2:// 未连接
				ll_no_net.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		};

	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbind();
	}

	public void switchFragment(Fragment f) {
		FragmentTransaction beginTransaction = getSupportFragmentManager()
				.beginTransaction();
		beginTransaction.addToBackStack(f.getTag());
		beginTransaction.replace(R.id.content_frame, f).commit();
		// 自动切换
		// menu.toggle();
	}

	public void menueSwitchFragment(Fragment f) {
		FragmentTransaction beginTransaction = getSupportFragmentManager()
				.beginTransaction();
		beginTransaction.addToBackStack(f.getTag() + 1);
		beginTransaction.replace(R.id.content_frame, f).commit();
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
