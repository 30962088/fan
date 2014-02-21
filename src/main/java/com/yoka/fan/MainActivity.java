package com.yoka.fan;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.yoka.fan.wiget.CommonListAdapter;
import com.yoka.fan.wiget.CommonListModel;
import com.yoka.fan.wiget.CommonListView;
import com.yoka.fan.wiget.LinkModel;
import com.yoka.fan.wiget.LinkModel.Link;

import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class MainActivity extends SlidingFragmentActivity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		initSlidingMenu();
		
		
		/*CommonListAdapter adapter = new CommonListAdapter(this,new ArrayList<CommonListModel>(){{
			for(int i = 0;i<10;i++){
				
			
			CommonListModel model = new CommonListModel();
			model.setComment(111);
			model.setDatetime("20分钟前");
			model.setLinkModel(new LinkModel("http://1101.169bb.com/169mm/201005/134/32.jpg", new ArrayList<LinkModel.Link>(){{
				add(new Link("123","ZARA 衬衫", 0.5f, 0.5f));
				add(new Link("123","ZARA 衬衫", 0.4f, 0.13f));
			}}));
			model.setName("云儿");
			model.setPhoto("http://tp4.sinaimg.cn/2129028663/180/5684393877/1");
			model.setStar(11);
			model.setStared(false);
			model.setTags(new ArrayList<String>(){{
				add("街拍");
				add("复古");
			}});
			add(model);
			}
		}});
		PullToRefreshListView listView = ((PullToRefreshListView)findViewById(R.id.listview));
		
		listView.setMode(Mode.BOTH);
		
		TextView textView = new TextView(this);
		textView.setText("底部");
		listView.getRefreshableView().addFooterView(textView);
		listView.setAdapter(adapter);*/
		
	
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
