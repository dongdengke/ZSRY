package cn.edu.bjtu.zsry.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import cn.edu.bjtu.zsry.R;

public class PopUtils {
	private static PopupWindow menu;

	/**
	 * 显示PopupWindow
	 * 
	 * @param context
	 * @param parent
	 */
	public static void showLoadingPop(Context context, View parent) {
		View layout = View.inflate(context, R.layout.pop_lay, null);
		menu = new PopupWindow(layout, 600, LayoutParams.WRAP_CONTENT, true);
		menu.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.list_activated_holo));
		menu.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	/**
	 * 隐藏PopupWindow
	 */
	public static void dismissPop() {
		if (menu.isShowing()) {
			menu.dismiss();
		}
	}
}
