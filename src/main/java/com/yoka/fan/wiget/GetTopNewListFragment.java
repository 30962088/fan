package com.yoka.fan.wiget;

import java.util.List;

import com.yoka.fan.R;
import com.yoka.fan.network.GetTopNew;
import com.yoka.fan.network.ListItemData;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class GetTopNewListFragment extends CommonListFragment{

	
	
	@Override
	protected List<ListItemData> load(int offset, int limit) {
		GetTopNew request = new GetTopNew( offset, limit);
		request.request();
		return request.getListData();
	}

	@Override
	public String getEmptyTip() {
		// TODO Auto-generated method stub
		return "没有最新的搭配";
	}
	
}
