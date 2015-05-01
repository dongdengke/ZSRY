package cn.edu.bjtu.zsry.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bjtu.zsry.R;
import cn.edu.bjtu.zsry.global.GlobalParam;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

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
	private BDLocationListener locationListener;

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
		locationListener = new MyLocationListener();
		GlobalParam.LOCATIONCLIENT.registerLocationListener(locationListener);
		baiDuMap = mMapView.getMap();
		return view;
	}

	private String addStr;
	private PopupWindow menu;
	private String city;
	private String district;
	private String longAndLan;
	private float radius;
	private String province;
	private String time;
	private TextView tv_latAndlong;
	private TextView tv_accurce;
	private TextView tv_time;
	private TextView tv_code;
	private TextView tv_addess;
	private TextView tv_distruct;
	private String code;

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location == null) {
				Toast.makeText(getActivity(), "定位失败", 0).show();
				return;
			}
			addStr = location.getAddrStr();
			city = location.getCity();
			district = location.getDistrict();
			int stteNum = location.getSatelliteNumber();
			province = location.getProvince();
			String num = location.getStreetNumber();
			time = location.getTime();
			longAndLan = "(" + location.getLongitude() + ","
					+ location.getLatitude() + ")";
			radius = location.getRadius();
			code = location.getCityCode();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		GlobalParam.LOCATIONCLIENT.unRegisterLocationListener(locationListener);
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		GlobalParam.LOCATIONCLIENT.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
		GlobalParam.LOCATIONCLIENT.stop();
	}

	// 出行方案的标识
	int flag = 1;
	private BaiduMap baiDuMap;

	@Override
	public void onClick(View v) {
		LayoutInflater inflater;
		View layout;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_start:
			Toast.makeText(getActivity(), "选择提点", 0).show();
			dismissPopupWindow();
			baiDuMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public boolean onMapPoiClick(MapPoi poi) {
					// TODO Auto-generated method stub
					String name = poi.getName();
					Toast.makeText(getActivity(), name, 0).show();
					ed_start.setText(name);
					return true;
				}

				@Override
				public void onMapClick(LatLng arg0) {
					// TODO Auto-generated method stub

				}
			});
			break;
		case R.id.btn_end:
			Toast.makeText(getActivity(), "选择终点", 0).show();
			dismissPopupWindow();
			baiDuMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public boolean onMapPoiClick(MapPoi poi) {
					// TODO Auto-generated method stub
					String name = poi.getName();
					Toast.makeText(getActivity(), name, 0).show();
					ed_end.setText(name);
					return true;
				}

				@Override
				public void onMapClick(LatLng arg0) {
					// TODO Auto-generated method stub

				}
			});
			break;
		case R.id.ll_location:
			ll_title.setVisibility(View.GONE);
			Toast.makeText(getActivity(), addStr, 0).show();
			GlobalParam.LOCATIONCLIENT.stop();
			dismissPopupWindow();
			break;
		case R.id.ll_detail:
			ll_title.setVisibility(View.GONE);
			// 获取LayoutInflater实例
			inflater = (LayoutInflater) getActivity().getSystemService(
					getActivity().LAYOUT_INFLATER_SERVICE);
			// 获取弹出菜单的布局
			layout = inflater.inflate(R.layout.pop, null);
			tv_latAndlong = (TextView) layout.findViewById(R.id.tv_latAndlong);
			tv_accurce = (TextView) layout.findViewById(R.id.tv_accurce);
			tv_time = (TextView) layout.findViewById(R.id.tv_time);
			tv_code = (TextView) layout.findViewById(R.id.tv_code);
			tv_addess = (TextView) layout.findViewById(R.id.tv_addess);
			tv_distruct = (TextView) layout.findViewById(R.id.tv_distruct);
			menu = new PopupWindow(layout, 600, LayoutParams.WRAP_CONTENT, true);
			menu.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.list_activated_holo));
			menu.showAtLocation(view.findViewById(R.id.map_fragment),
					Gravity.CENTER, 0, 0);
			GlobalParam.LOCATIONCLIENT.start();
			// 给pop设置数据
			tv_accurce.setText("精确度:" + radius);
			tv_latAndlong.setText("经纬度:" + longAndLan);
			tv_addess.setText("详细地址" + addStr);
			tv_code.setText("城市编码:" + code);
			tv_distruct.setText("区:" + district);
			tv_time.setText("定位时间:" + time);
			GlobalParam.LOCATIONCLIENT.stop();
			break;
		case R.id.ll_route:
			dismissPopupWindow();
			ll_title.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_transit:
			dismissPopupWindow();
			btn_transit.setBackgroundResource(R.drawable.mode_transit_on);
			btn_driving.setBackgroundResource(R.drawable.mode_driving_off);
			btn_walking.setBackgroundResource(R.drawable.mode_walk_off);
			flag = 1;
			break;
		case R.id.btn_driving:
			dismissPopupWindow();
			btn_transit.setBackgroundResource(R.drawable.mode_transit_off);
			btn_driving.setBackgroundResource(R.drawable.mode_driving_on);
			btn_walking.setBackgroundResource(R.drawable.mode_walk_off);
			flag = 2;
			break;
		case R.id.btn_walking:
			dismissPopupWindow();
			btn_transit.setBackgroundResource(R.drawable.mode_transit_off);
			btn_driving.setBackgroundResource(R.drawable.mode_driving_off);
			btn_walking.setBackgroundResource(R.drawable.mode_walk_on);
			flag = 3;
			break;
		case R.id.btn_search:
			dismissPopupWindow();
			String start = ed_start.getText().toString().trim();
			String end = ed_end.getText().toString().trim();
			if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
				Toast.makeText(getActivity(), "请输入起点或者终点", 0).show();
			}
			if (flag == 1) {
				RoutePlanSearch search = RoutePlanSearch.newInstance();
				OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {

					@Override
					public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onGetTransitRouteResult(
							TransitRouteResult result) {
						// TODO Auto-generated method stub
						if (result == null
								|| result.error != SearchResult.ERRORNO.NO_ERROR) {
							Toast.makeText(getActivity(), "抱歉，未找到结果",
									Toast.LENGTH_SHORT).show();
						}
						if (result.error == SearchResult.ERRORNO.NO_ERROR) {
							TransitRouteOverlay overlay = new TransitRouteOverlay(
									baiDuMap);
							baiDuMap.setOnMarkerClickListener(overlay);
							overlay.setData(result.getRouteLines().get(0));
							overlay.addToMap();
							overlay.zoomToSpan();
						}
					}

					@Override
					public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
						// TODO Auto-generated method stub

					}
				};
				PlanNode stNode = PlanNode
						.withCityNameAndPlaceName(city, start);
				PlanNode enNode = PlanNode.withCityNameAndPlaceName(city, end);
				search.transitSearch((new TransitRoutePlanOption())
						.from(stNode).city(city).to(enNode));
				search.setOnGetRoutePlanResultListener(listener);
				search.destroy();
			} else if (flag == 2) {
				RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
				OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
					public void onGetWalkingRouteResult(
							WalkingRouteResult result) {
						// 获取步行线路规划结果
					}

					public void onGetTransitRouteResult(
							TransitRouteResult result) {
						// 获取公交换乘路径规划结果
					}

					public void onGetDrivingRouteResult(
							DrivingRouteResult result) {
						// 获取驾车线路规划结果
						if (result == null
								|| result.error != SearchResult.ERRORNO.NO_ERROR) {
							Toast.makeText(getActivity(), "抱歉，未找到结果",
									Toast.LENGTH_SHORT).show();
						}
						if (result.error == SearchResult.ERRORNO.NO_ERROR) {
							DrivingRouteOverlay overlay = new DrivingRouteOverlay(
									baiDuMap);
							baiDuMap.setOnMarkerClickListener(overlay);
							overlay.setData(result.getRouteLines().get(0));
							overlay.addToMap();
							overlay.zoomToSpan();
						}
					}
				};
				mSearch.setOnGetRoutePlanResultListener(listener);
				PlanNode stNode = PlanNode
						.withCityNameAndPlaceName("北京", "西直门");
				PlanNode enNode = PlanNode
						.withCityNameAndPlaceName("北京", "西二旗");
				mSearch.drivingSearch((new DrivingRoutePlanOption()).from(
						stNode).to(enNode));
				mSearch.destroy();
			} else if (flag == 3) {
				Toast.makeText(getActivity(), "步行", 0).show();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 关闭popupwindow
	 */
	private void dismissPopupWindow() {
		if (menu != null && menu.isShowing()) {
			menu.dismiss();
			menu = null;
		}
	}

}
