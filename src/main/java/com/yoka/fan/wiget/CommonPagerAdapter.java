package com.yoka.fan.wiget;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CommonPagerAdapter extends FragmentPagerAdapter{

	private List<Page> pages;
	
	public CommonPagerAdapter(FragmentManager fm,List<Page> pages) {
		super(fm);
		this.pages = pages;
	}

	public static class Page{
		private String name;
		private Fragment fragment;
		public Page(String name, Fragment fragment) {
			super();
			this.name = name;
			this.fragment = fragment;
		}
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return pages.get(position).name;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return pages.get(position).fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pages.size();
	}
	
}
