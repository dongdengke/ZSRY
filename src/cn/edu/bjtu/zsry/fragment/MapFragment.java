package cn.edu.bjtu.zsry.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.edu.bjtu.zsry.R;

import com.baidu.mapapi.map.MapView;

public class MapFragment extends Fragment implements OnClickListener {

	private View view;
	MapView mMapView = null;
	private EditText ed_start;
	private EditText ed_end;
	private Button btn_end;
	private Button btn_start;
	private LinearLayout ll_title;
	private LinearLayout ll_location;
	private LinearLayout ll_detail;
	private LinearLayout ll_route;
	private Button btn_transit;
	private Button btn_driving;
	private Button btn_walking;
	private ImageButton btn_search;

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
		view = inflater.inflate(R.layout.map_fragment, null);
		// 获取地图控件引用
		mMapView = (MapView) view.findViewById(R.id.bmapView);
		ed_start = (EditText) view.findViewById(R.id.ed_start);
		ed_end = (EditText) view.findViewById(R.id.ed_end);
		btn_end = (Button) view.findViewById(R.id.btn_end);
		btn_start = (Button) view.findViewById(R.id.btn_start);
		ll_title = (LinearLayout) view.findViewById(R.id.ll_title);
		ll_location = (LinearLayout) view.findViewById(R.id.ll_location);
		ll_detail = (LinearLayout) view.findViewById(R.id.ll_detail);
		ll_route = (LinearLayout) view.findViewById(R.id.ll_route);
		btn_transit = (Button) view.findViewById(R.id.btn_transit);
		btn_driving = (Button) view.findViewById(R.id.btn_driving);
		btn_walking = (Button) view.findViewById(R.id.btn_walking);
		btn_search = (ImageButton) view.findViewById(R.id.btn_search);
		btn_start.setOnClickListener(this);
		btn_end.setOnClickListener(this);
		btn_transit.setOnClickListener(this);
		btn_driving.setOnClickListener(this);
		btn_walking.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		ll_location.setOnClickListener(this);
		ll_detail.setOnClickListener(this);
		ll_route.setOnClickListener(this);

		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_start:
			Toast.makeText(getActivity(), "选择提点", 0).show();
			break;
		case R.id.btn_end:
			Toast.makeText(getActivity(), "选择终点", 0).show();
			break;
		case R.id.ll_location:
			Toast.makeText(getActivity(), "正在定位", 0).show();
			ll_title.setVisibility(View.GONE);
			break;
		case R.id.ll_detail:
			Toast.makeText(getActivity(), "您的位置的详细信息", 0).show();
			ll_title.setVisibility(View.GONE);
			break;
		case R.id.ll_route:
			ll_title.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_transit:
			Toast.makeText(getActivity(), "公交", 0).show();
			break;
		case R.id.btn_driving:
			Toast.makeText(getActivity(), "驾车", 0).show();
			break;
		case R.id.btn_walking:
			Toast.makeText(getActivity(), "不行", 0).show();
			break;
		case R.id.btn_search:
			Toast.makeText(getActivity(), "搜索路线", 0).show();
			break;
		default:
			break;
		}
	}

}
