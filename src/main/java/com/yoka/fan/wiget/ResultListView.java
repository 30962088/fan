package com.yoka.fan.wiget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.yoka.fan.network.ListItemData;

import android.content.Context;
import android.util.AttributeSet;

public abstract class ResultListView extends CommonListView{

	private static int limit = 20;
	
	private int offset = 0;
	
	public ResultListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ResultListView(
			Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode,
			com.handmark.pulltorefresh.library.PullToRefreshBase.AnimationStyle style) {
		super(context, mode, style);
		// TODO Auto-generated constructor stub
	}

	public ResultListView(Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	public ResultListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Result refresh() {
		offset = 0;
		load();
		return null;
	}

	@Override
	protected Result loadMore() {
		load();
		return null;
	}
	
	private Result load(){
		
		HttpURLConnection url = getURL(offset,limit);
		Result result = null;
		try {
			int status = ((HttpURLConnection) url).getResponseCode();
			switch (status) {
            case 200:
            case 201:
            	Gson gson = new Gson();
            	List<ListItemData> datas = gson.fromJson(IOUtils.toString(new InputStreamReader(url.getInputStream())), List.class);
            	boolean hasMore = datas.size()>=limit?true:false;
            	List<CommonListModel> models = new ArrayList<CommonListModel>();
            	for(ListItemData data : datas){
            		models.add(data.toCommonListModel());
            	}
            	result = new Result(hasMore, models);
            	offset += limit;
	        }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return result;
				
	}
	
	protected abstract HttpURLConnection getURL(int offset,int limit);

	
	
}
