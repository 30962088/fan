package com.yoka.fan.wiget;

import java.util.ArrayList;
import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yoka.fan.LoginActivity;
import com.yoka.fan.MainActivity;
import com.yoka.fan.R;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.CommonListView.OnVerticalScrollListener;
import com.yoka.fan.wiget.CommonPagerAdapter.Page;
import com.yoka.fan.wiget.TabPageIndicator.OnTabClickLisenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment implements OnVerticalScrollListener{

	private TabPageIndicator indicator;
	
	private View headerContainer;
	
	private View indicatorWrap;
	
	public HomeFragment() {
		// TODO Auto-generated constructor stub
	}
	
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
		indicatorWrap = view.findViewById(R.id.indicator_wrap);
		headerContainer = getActivity().findViewById(R.id.base_actionbar_content);
		headerContainer.setVisibility(View.VISIBLE);
		List<CommonPagerAdapter.Page> pages = new ArrayList<CommonPagerAdapter.Page>(){{
			add(new Page("推荐",new CollRecommandListFragment(){{
				setOnVerticalScrollListener(HomeFragment.this);
			}},false ));
			
			add(new Page("最新", new GetTopNewListFragment(){{
				setOnVerticalScrollListener(HomeFragment.this);
			}},false));
			
			add(new Page("关注", new CollFollowListFragment(){{
				setOnVerticalScrollListener(HomeFragment.this);
			}}, false));
		}};
		
		
		CommonPagerAdapter adapter = new CommonPagerAdapter(getChildFragmentManager(),pages);
		
		

        ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(-1);
        
        indicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        indicator.setModel(pages);
        indicator.setViewPager(pager);
        indicator.setOnTabClickLisenter(new OnTabClickLisenter() {
			
			@Override
			public boolean onclick(int index, View tabView) {
				if(index == 2){
					User user = User.readUser();
					if(user == null){
						getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));
						return false;
					}
				}
				return true;
			}
		});
        
		
	}

	@Override
	public void onScrollUp() {
		if(indicatorWrap.getVisibility() == View.GONE){
			Utils.expand(indicatorWrap);
			if(headerContainer != null){
				Utils.expand(headerContainer);
			}
		}
		
		
	}

	@Override
	public void onScrollDown() {
		if(indicatorWrap.getVisibility() == View.VISIBLE){
			Utils.collapse(indicatorWrap);
			if(headerContainer != null){
				Utils.collapse(headerContainer);
			}
		}
		
	}
	
	
	
}
