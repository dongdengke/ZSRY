package cn.edu.bjtu.zsry.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.edu.bjtu.zsry.MainActivity;
import cn.edu.bjtu.zsry.R;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("InflateParams")
public class FeedBackFragment extends Fragment {

	private EditText ed_content;
	private Button btn_send;
	private Button btn_cancel;
	private BroadcastReceiver smsSentReceiver;
	private BroadcastReceiver smsDeliveredReceiver;
	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";
	private PendingIntent sentPI;
	private PendingIntent deliveredPI;

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
		View view = inflater.inflate(R.layout.feedback, null);
		ed_content = (EditText) view.findViewById(R.id.ed_content);
		btn_send = (Button) view.findViewById(R.id.btn_send);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		final String text = ed_content.getText().toString().trim();
		sentPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SENT),
				0);
		deliveredPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(
				DELIVERED), 0);
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (text != null) {
					SmsManager smsManager = SmsManager.getDefault();
					List<String> divideContents = smsManager
							.divideMessage(text);
					for (String text : divideContents) {
						smsManager.sendTextMessage("18813073696", null, text,
								sentPI, deliveredPI);
					}
					Toast.makeText(getActivity(), "反馈成功，我们会酌情考虑", 0).show();
				} else {
					Toast.makeText(getActivity(), "请输入反馈内容", 0).show();
				}
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (getActivity() instanceof MainActivity) {
					MainActivity activity = (MainActivity) getActivity();
					activity.menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
					((MainActivity) getActivity())
							.switchFragment(new SettingFragment());
				}
			}
		});

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		smsSentReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getActivity(), "无短信服务", Toast.LENGTH_SHORT)
							.show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getActivity(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getActivity(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
		smsDeliveredReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getActivity(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getActivity(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}

			}
		};
		getActivity().registerReceiver(smsDeliveredReceiver,
				new IntentFilter(DELIVERED));
		getActivity().registerReceiver(smsSentReceiver, new IntentFilter(SENT));

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().unregisterReceiver(smsDeliveredReceiver);
		getActivity().unregisterReceiver(smsSentReceiver);

	}
}
