package com.yoka.fan.wiget;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.MainActivity;
import com.yoka.fan.R;
import com.yoka.fan.network.Info;
import com.yoka.fan.network.Info.Result;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.CommonPagerAdapter.Page;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ZoneFragment extends Fragment{

	private TextView matchView;
	
	private TextView focusView;
	
	private TextView fansView;
	
	private ImageView photoView;
	
	private String target_id;
	
	private ImageLoader imageLoader;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(User.readUser() == null){
			getActivity().finish();
		}
		imageLoader = Utils.getImageLoader(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.zone_header, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		matchView = (TextView) view.findViewById(R.id.match);
		focusView = (TextView) view.findViewById(R.id.focus);
		fansView = (TextView) view.findViewById(R.id.fans);
		photoView = (ImageView)view.findViewById(R.id.photo);
		initView();
		
	}
	
	
	private void initView() {
		final User user = User.readUser();
		target_id = getArguments().getString("target_id");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Info info = new Info(user.access_token, user.id,target_id){
					@Override
					public void onSuccess(Map<String, Result> map) {
						final Result result = map.get(target_id);
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								initUserView(result);
								
							}
						});
					}
					
				};
				info.request();
			}
		}).start();
		
	}
	
	private void initUserView(Result result){
		matchView.setText(""+result.getShow_specials());
		fansView.setText(""+result.getFollowers());
		focusView.setText(""+result.getFollows());
		imageLoader.displayImage(result.getHead_url(), photoView);
		initPage(result.getId());
	}

	private void initPage(String target_id){
		User user = User.readUser();
		View view = getView();
		final Bundle arguments = new Bundle();
		arguments.putString("target_id", target_id);
		arguments.putString("user_id", user.id);
		arguments.putString("access_token", user.access_token);
		List<CommonPagerAdapter.Page> pages = new ArrayList<CommonPagerAdapter.Page>(){{
			add(new Page("我的搭配",new CollListFragment(){{
				setArguments(arguments);
			}},false));
			add(new Page("我的喜欢",new CollListFragment(){{
				setArguments(arguments);
			}},false));
		}};
		FragmentPagerAdapter adapter = new CommonPagerAdapter(getChildFragmentManager(),pages);
		
		

        ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        
        

        TabPageIndicator indicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        indicator.setModel(pages);
        indicator.setViewPager(pager);
        
        indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) { }

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }

			@Override
			public void onPageSelected(int position) {
				Activity activity = getActivity();
				if(activity instanceof MainActivity){
					MainActivity mainActivity = (MainActivity)activity;
					switch (position) {
					case 0:
						mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
						break;
					default:
						mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
						break;
					}
				}
				
			}

		});
		
	}
	
	
	
}
