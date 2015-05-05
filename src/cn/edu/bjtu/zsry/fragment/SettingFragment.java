package cn.edu.bjtu.zsry.fragment;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bjtu.zsry.MainActivity;
import cn.edu.bjtu.zsry.R;
import cn.edu.bjtu.zsry.bean.UpdateInfo;
import cn.edu.bjtu.zsry.engine.UpdateInfoParse;
import cn.edu.bjtu.zsry.manager.DataCleanManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SettingFragment extends Fragment implements OnClickListener {

	protected static final int SERVER_ERROR = 1;
	protected static final int URL_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	protected static final int PARSE_XML_ERROR = 4;
	protected static final int PARSE_SUCCESS = 5;
	private static final int LOAD_MAINUI = 6;
	private View view;
	private TextView tv_version;
	private UpdateInfo updateInfo;

	private RelativeLayout rl_about_zsry;
	private RelativeLayout rl_share;
	private RelativeLayout rl_newversion;
	private RelativeLayout rl_checkandupdate;
	private RelativeLayout rl_cleancache;
	private RelativeLayout rl_feedback;
	// 消息机制,处理在子线程中更新ui
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SERVER_ERROR:
				Toast.makeText(getActivity(), "服务器繁忙，稍后重试", Toast.LENGTH_SHORT)
						.show();
				break;
			case NETWORK_ERROR:
				Toast.makeText(getActivity(), "服务器繁忙，稍后重试", Toast.LENGTH_SHORT)
						.show();
				break;
			case PARSE_SUCCESS:
				// 检查版本,确定是否需要更新
				checkVersion();
				break;
			}
		}

	};

	/**
	 * 检查版本,确定是否需要更新
	 */
	private void checkVersion() {
		// TODO Auto-generated method stub
		if (getVersion().equals(updateInfo.getVersion())) {
			// 版本相同,不需要更新
			Toast.makeText(getActivity(), "版本相同", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(), "版本不同", Toast.LENGTH_SHORT).show();
		}
	}

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
		view = inflater.inflate(R.layout.setting_fragment, null);
		tv_version = (TextView) view.findViewById(R.id.tv_version);
		String versionName = getVersion();
		tv_version.setText("当前版本:" + versionName);
		rl_about_zsry = (RelativeLayout) view.findViewById(R.id.rl_about_zsry);
		rl_share = (RelativeLayout) view.findViewById(R.id.rl_share);
		rl_newversion = (RelativeLayout) view.findViewById(R.id.rl_newversion);
		rl_checkandupdate = (RelativeLayout) view
				.findViewById(R.id.rl_checkandupdate);
		rl_cleancache = (RelativeLayout) view.findViewById(R.id.rl_cleancache);
		rl_feedback = (RelativeLayout) view.findViewById(R.id.rl_feedback);
		rl_newversion.setOnClickListener(this);
		rl_checkandupdate.setOnClickListener(this);
		rl_cleancache.setOnClickListener(this);
		rl_feedback.setOnClickListener(this);
		rl_about_zsry.setOnClickListener(this);
		rl_share.setOnClickListener(this);
		return view;
	}

	/**
	 * 获取请用程序的版本信息
	 */
	private String getVersion() {
		// TODO Auto-generated method stub
		PackageManager packageManager = getActivity().getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getActivity().getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_newversion:
			if (getActivity() instanceof MainActivity) {
				MainActivity activity = (MainActivity) getActivity();
				activity.menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				((MainActivity) getActivity())
						.switchFragment(new NewVersionFeature());
			}
			break;
		case R.id.rl_checkandupdate:

			Toast.makeText(getActivity(), "正在检查ing", 0).show();
			checkVersionTask();
			break;
		case R.id.rl_cleancache:
			try {
				String cacheSize = DataCleanManager.getCacheSize(new File(
						"/data/data/cn.edu.bjtu.zsry"));
				Toast.makeText(getActivity(), "正在清理ing", 0).show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						DataCleanManager.cleanApplicationData(getActivity(),
								null);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				Toast.makeText(getActivity(), "清理完毕", 0).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.rl_feedback:
			if (getActivity() instanceof MainActivity) {
				MainActivity activity = (MainActivity) getActivity();
				activity.menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				((MainActivity) getActivity())
						.switchFragment(new FeedBackFragment());
			}
			break;
		case R.id.rl_share:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT,
					"小伙伴们，给你们分享一个好用的app，名字叫做：掌尚软院，给你最爽的移动体验，交大人必备哦，快到应用市场下载哦！！");
			startActivity(intent);

			break;
		case R.id.rl_about_zsry:
			if (getActivity() instanceof MainActivity) {
				// MainActivity activity = (MainActivity) getActivity();
				// activity.menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				((MainActivity) getActivity())
						.switchFragment(new AboutZsryFragment());
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 检查版本信息,判断是否需要更新
	 */
	private void checkVersionTask() {
		// TODO Auto-generated method stub
		final Message msg = Message.obtain();
		new Thread() {
			public void run() {
				int code;
				try {
					URL url = new URL(getString(R.string.serverurl));
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					code = conn.getResponseCode();
					if (code == 200) {
						// 请求成功
						InputStream in = conn.getInputStream();
						updateInfo = UpdateInfoParse.getUpdateInfo(in);
						if (updateInfo != null) {
							// 解析成功
							msg.what = PARSE_SUCCESS;
						}
					} else {
						// 请求失败
						msg.what = NETWORK_ERROR;
					}
				} catch (Exception e) {
					e.printStackTrace();
					// 网络联接错误
					msg.what = NETWORK_ERROR;
				} finally {
					handler.sendMessage(msg);
				}
			};
		}.start();
	}
}
