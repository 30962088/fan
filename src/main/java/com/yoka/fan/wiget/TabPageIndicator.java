package com.yoka.fan.wiget;

import java.util.List;

import com.yoka.fan.R;
import com.yoka.fan.utils.DisplayUtils;
import com.yoka.fan.wiget.CommonPagerAdapter.Page;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabPageIndicator extends LinearLayout{

	public static interface OnTabClickLisenter{
		public boolean onclick(int index,View tabView);
	}
	
	private OnPageChangeListener onPageChangeListener;
	
	
	
	private View viewSelected;
	
	private ViewPager viewPager;
	
	
	private OnTabClickLisenter onTabClickLisenter;
	
	public void setOnTabClickLisenter(OnTabClickLisenter onTabClickLisenter) {
		this.onTabClickLisenter = onTabClickLisenter;
	}
	
	public void setOnPageChangeListener(
			OnPageChangeListener onPageChangeListener) {
		this.onPageChangeListener = onPageChangeListener;
	}
	
	public TabPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TabPageIndicator(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	

	
	public void setModel(List<Page> list){
		removeAllViews();
		for(int i = 0;i<list.size();i++){
			
			final Page page = list.get(i);
			View view =  LayoutInflater.from(getContext()).inflate(R.layout.page_tab,null);
			final int pos = i;
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(viewPager != null && viewPager.getCurrentItem() == pos && page.getFragment() instanceof CommonListFragment){
						((CommonListFragment)page.getFragment()).refresh();
					}
					if(onTabClickLisenter == null || onTabClickLisenter.onclick(pos, v)){
						viewPager.setCurrentItem(pos);
					}
					
					
				}
			});
			((TextView)view.findViewById(R.id.tab_title)).setText(page.getName());
			if(page.isNew()){
				view.findViewById(R.id.new_dot).setVisibility(View.VISIBLE);
			}
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			params.weight = 1;
			addView(view, params);
			if(i != list.size() -1){
				View sep = new View(getContext());
				final int margin = DisplayUtils.Dp2Px(getContext(), 10);
				sep.setBackgroundResource(R.drawable.divider);
				sep.setLayoutParams(new LayoutParams(1,LayoutParams.MATCH_PARENT ){{
					setMargins(0,margin, 0, margin);
				}});
				addView(sep);
			}
		}
		setSelected(0);
	}

	public void setSelected(int position) {
		if(viewSelected != null){
			viewSelected.setSelected(false);
		}
		viewSelected = getTabView(position);
		viewSelected.setSelected(true);
		viewSelected.findViewById(R.id.new_dot).setVisibility(View.GONE);
		
	}
	
	private View getTabView(int position){
		
		for(int i = 0,k=0;i<getChildCount();i++){
			 if(getChildAt(i).getId() == R.id.tab){
				 if(k == position){
					 return getChildAt(i);
				 }else{
					 k++;
				 }
			 }
		}
		return null;
	}

	public void setViewPager(ViewPager pager){
		this.viewPager = pager;
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if(onPageChangeListener != null){
					onPageChangeListener.onPageSelected(position);
				}
				setSelected(position);
				
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if(onPageChangeListener != null){
					onPageChangeListener.onPageScrolled(arg0, arg1, arg2);
				}
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				if(onPageChangeListener != null){
					onPageChangeListener.onPageScrollStateChanged(arg0);
				}
				
			}
		});
	}
	

	
}
