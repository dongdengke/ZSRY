package cn.edu.bjtu.zsry.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.edu.bjtu.zsry.MainActivity;
import cn.edu.bjtu.zsry.R;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MenuFragment extends Fragment implements OnItemClickListener {

	private View view;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ListView list_view = (ListView) view.findViewById(R.id.list_view);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.menue_item, R.id.tv_title, initData());
		list_view.setAdapter(adapter);
		list_view.setOnItemClickListener(this);
	}

	private List<String> initData() {
		List<String> list = new ArrayList<String>();
		list.add("首页");
		list.add("软院简介");
		list.add("招聘信息");
		list.add("地图搜索");
		list.add("国际交流");
		list.add("其他公告");
		list.add("设置更多");
		return list;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = LayoutInflater.from(getActivity()).inflate(R.layout.list_view,
				null);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		Fragment f = null;
		switch (arg2) {
		case 0:
			f = new HomeFragment();
			break;
		case 1:
			f = new IntroduceFragment();
			break;
		case 2:
			f = new EmployeeInfoFragment();
			break;
		case 3:
			f = new MapFragment();
			break;
		case 4:
			f = new InternationalFragment();
			break;
		case 5:
			f = new MoreFragment();
			break;
		case 6:
			f = new SettingFragment();
			break;
		}
		switchFragment(f);
	}

	private void switchFragment(Fragment f) {
		// TODO Auto-generated method stub
		if (f != null) {
			if (getActivity() instanceof MainActivity) {
				MainActivity activity = (MainActivity) getActivity();
				activity.menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				((MainActivity) getActivity()).menueSwitchFragment(f);
			}
		}
	}

}