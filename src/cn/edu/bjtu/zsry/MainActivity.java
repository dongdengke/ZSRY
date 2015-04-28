package cn.edu.bjtu.zsry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;
import cn.edu.bjtu.zsry.fragment.HomeFragment;
import cn.edu.bjtu.zsry.fragment.MenuFragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
	private SlidingMenu menu;
	private HomeFragment homeFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.menu);
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
		menu.toggle();
	}

	int count = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		count++;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (count == 1) {
				HomeFragment fragment = new HomeFragment();
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
			} else if (count == 2) {
				Toast.makeText(this, "再按一次退出应用程序", 0).show();
			} else {
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
