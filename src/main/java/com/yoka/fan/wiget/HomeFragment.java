package com.yoka.fan.wiget;

import java.util.ArrayList;
import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yoka.fan.MainActivity;
import com.yoka.fan.R;
import com.yoka.fan.utils.User;
import com.yoka.fan.wiget.CommonPagerAdapter.Page;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.home_layout, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		List<CommonPagerAdapter.Page> pages = new ArrayList<CommonPagerAdapter.Page>(){{
			add(new Page("推荐", new CollRecommandListFragment(),false));
			
			add(new Page("最新", new GetTopNewListFragment(),false));
			
			add(new Page("关注", new CollFollowListFragment(), false));
		}};
		
		
		CommonPagerAdapter adapter = new CommonPagerAdapter(getChildFragmentManager(),pages);
		
		

        ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(-1);
        
        

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
