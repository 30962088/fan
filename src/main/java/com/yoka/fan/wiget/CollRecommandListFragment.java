package com.yoka.fan.wiget;

import java.util.List;

import com.yoka.fan.R;
import com.yoka.fan.network.CollRecommand;
import com.yoka.fan.network.GetTopNew;
import com.yoka.fan.network.ListItemData;
import com.yoka.fan.wiget.CommonListView.LoadResult;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class CollRecommandListFragment extends CommonListFragment{

	
	
	@Override
	protected LoadResult load(int offset, int limit) {
		CollRecommand request = new CollRecommand( offset, limit);
		request.request();
		return new LoadResult(request.getStatus(), request.getListData()) ;
	}

	@Override
	public String getEmptyTip() {
		// TODO Auto-generated method stub
		return "没有任何推荐搭配";
	}
	
}
