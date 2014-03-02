package com.yoka.fan;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.SelectCategoryActivity.GridAdapter;
import com.yoka.fan.SelectCategoryActivity.Model;
import com.yoka.fan.SelectMainActivity.Result;
import com.yoka.fan.network.CollSave;
import com.yoka.fan.network.CollSave.Link;
import com.yoka.fan.network.Request.Status;
import com.yoka.fan.utils.DisplayUtils;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SelectConfirmActivity extends Activity implements OnClickListener{

	public static final String PARAM_RESULT = "PARAM_RESULT";
	
	private ImageLoader imageLoader;
	
	public List<Result> list;
	
	private ViewGroup listView;
	
	private EditText descView;
	
	private int width;
	
	private int height;
	
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.select_confirm_layout);
		descView = (EditText) findViewById(R.id.desc);
		findViewById(R.id.base_prev).setOnClickListener(this);
		findViewById(R.id.base_next).setOnClickListener(this);
		imageLoader = Utils.getImageLoader(this);
		url = getIntent().getStringExtra(BaseSelectActivity.PARAM_IMG_PATH);
		width = getIntent().getIntExtra(SelectMainActivity.PARAM_WIDTH, 0);
		height = getIntent().getIntExtra(SelectMainActivity.PARAM_HEIGHT, 0);
		list = (List<Result>) getIntent().getSerializableExtra(PARAM_RESULT);
		imageLoader.displayImage(url, (ImageView)findViewById(R.id.thumbnail));
		imageLoader.displayImage(url, (ImageView)findViewById(R.id.img));
		listView = ((ViewGroup)findViewById(R.id.listview));
		initListView();
	}
	
	private void initListView(){
		

		for(Result result : list){
			TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.category_item,null);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = DisplayUtils.Dp2Px(this, 10);
			int padding = DisplayUtils.Dp2Px(this, 12);
			textView.setPadding(padding, padding/2, padding, padding/2);
			textView.setLayoutParams(layoutParams);
			textView.setText(result.getName());
			listView.addView(textView);
		}
	}
	
	
	private void onSave(){
		final Map<String, Link> link_goods = new HashMap<String, CollSave.Link>();
		for(int i = 0;i<list.size();i++){
			link_goods.put("k"+i, list.get(i).getLink());
		}
		final File uploadimg =  new File(Uri.parse(url).getPath());
		final User user = User.readUser();
		new AsyncTask<Void, Void, Status>() {

			@Override
			protected com.yoka.fan.network.Request.Status doInBackground(
					Void... params) {
				CollSave request = new CollSave(link_goods, user.access_token, descView.getText().toString(), uploadimg, width, height, user.id);
				request.request();
				return request.getStatus();
			}
			
			@Override
			protected void onPostExecute(
					com.yoka.fan.network.Request.Status result) {
				if(result == com.yoka.fan.network.Request.Status.SUCCESS){
					finish();
				}
				
			}
		}.execute();
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_prev:
			finish();
			break;
		case R.id.base_next:
			onSave();
			break;
		default:
			break;
		}
		
	}
	
}