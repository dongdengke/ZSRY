package cn.edu.bjtu.zsry.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import cn.edu.bjtu.zsry.utils.NetWorkUtils;

public class NetWorkChangedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (!NetWorkUtils.checkNetState(context)) {
			Toast.makeText(context, "网络联接超时，请稍后重试！～～", Toast.LENGTH_LONG)
					.show();
		}
	}

}
