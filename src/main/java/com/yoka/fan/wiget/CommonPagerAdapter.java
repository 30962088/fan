package com.yoka.fan.wiget;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class CommonPagerAdapter extends FragmentPagerAdapter{

	private List<Page> pages;
	

	
	public CommonPagerAdapter(FragmentManager fm,List<Page> pages) {
		super(fm);
		this.pages = pages;
	}

	public static class Page{
		private String name;
		private Fragment fragment;
		private boolean isNew;
		public Page(String name, Fragment fragment,boolean isNew) {
			super();
			this.name = name;
			this.fragment = fragment;
			this.isNew = isNew;
		}
		public String getName() {
			return name;
		}
		public Fragment getFragment() {
			return fragment;
		}
		public boolean isNew() {
			return isNew;
		}
		
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
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
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
//		super.destroyItem(container, position, object);
	}
	
}
