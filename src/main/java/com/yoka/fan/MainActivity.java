package com.yoka.fan;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.yoka.fan.wiget.CommonListAdapter;
import com.yoka.fan.wiget.CommonListModel;
import com.yoka.fan.wiget.LinkModel;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends SlidingFragmentActivity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		initSlidingMenu();

		
		CommonListAdapter adapter = new CommonListAdapter(this,new ArrayList<CommonListModel>(){{
			CommonListModel model = new CommonListModel();
			model.setComment(111);
			model.setDatetime("20分钟前");
			model.setImg("http://a.hiphotos.baidu.com/image/w%3D1366%3Bcrop%3D0%2C0%2C1366%2C768/sign=667d4ab89c510fb378197394ef05f3f6/838ba61ea8d3fd1fc184a63b324e251f95ca5f25.jpg");
			model.setLinkList(new ArrayList<LinkModel>());
			model.setName("云儿");
			model.setPhoto("http://tp4.sinaimg.cn/2129028663/180/5684393877/1");
			model.setStar(11);
			model.setStared(false);
			model.setTags(new ArrayList<String>(){{
				add("街拍");
				add("复古");
			}});
			add(model);
		}});
		((ListView)findViewById(R.id.listview)).setAdapter(adapter);
		
	
	}
	
	private void initSlidingMenu(){
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new SidingMenuFragment())
		.commit();
		
		findViewById(R.id.actionbar_nav).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggle();
				
			}
		});
		
	}


}
