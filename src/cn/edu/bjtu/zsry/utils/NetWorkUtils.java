package cn.edu.bjtu.zsry.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class NetWorkUtils {
	public static boolean checkNetState(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo[] networkInfos = connectivityManager
					.getAllNetworkInfo();
			for (int i = 0; i < networkInfos.length; i++) {
				State state = networkInfos[i].getState();
				if (NetworkInfo.State.CONNECTED == state) {
					return true;
				}
			}
		}
		return false;
	}
}
