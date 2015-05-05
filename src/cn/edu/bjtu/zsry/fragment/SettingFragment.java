package cn.edu.bjtu.zsry.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.edu.bjtu.zsry.R;

public class SettingFragment extends Fragment {

	private View view;
	private TextView tv_version;

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
}
