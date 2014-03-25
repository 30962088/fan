package com.yoka.fan;

import java.io.File;
import java.io.FileOutputStream;
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
import com.yoka.fan.wiget.AlertDialog;
import com.yoka.fan.wiget.LoadingPopup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
		listView.removeAllViews();
		int i = 0;
		for(final Result result : list){
			View view = LayoutInflater.from(this).inflate(R.layout.confirm_list_item,null);
			TextView textView = (TextView) view.findViewById(R.id.text);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.topMargin = DisplayUtils.Dp2Px(this, 10);
			view.setLayoutParams(layoutParams);
			textView.setText(result.getName());
			final int k = i;
			view.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SelectMainActivity activity = SelectMainActivity.getInstance();
					
					list.remove(result);
					initListView();
					if(activity != null){
						activity.remove(k);
					}
				}
			});
			listView.addView(view);
			i++;
		}
	}
	
	
	private void scale(File file){
		
		Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
		int width = bitmap.getWidth(),height = bitmap.getHeight();
		if(width<480){
			bitmap = Bitmap.createScaledBitmap(bitmap, 480, 640, true);
			this.width = 480;
			this.height = 640;
			FileOutputStream out = null;
			try {
			       out = new FileOutputStream(file.toString());
			       bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			} catch (Exception e) {
			    e.printStackTrace();
			} finally {
			       try{
			           out.close();
			       } catch(Throwable ignore) {}
			}
		}
		Log.d("zzm", file.toString());
	}
	
	
	private void onSave(){
		final User user = User.readUser();
		final String desc = descView.getText().toString();
		if(user == null){
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		if(desc == null || desc.length() <5){
			AlertDialog.show(this, "描述不能少于5个字哦:)");
			return;
		}
		final Map<String, Link> link_goods = new HashMap<String, CollSave.Link>();
		for(int i = 0;i<list.size();i++){
			link_goods.put("k"+i, list.get(i).getLink());
		}
		final File uploadimg =  new File(Uri.parse(url).getPath());
		scale(uploadimg);
		LoadingPopup.show(this);
		new AsyncTask<Void, Void, Status>() {

			@Override
			protected com.yoka.fan.network.Request.Status doInBackground(
					Void... params) {
				CollSave request = new CollSave(link_goods, user.access_token,desc , uploadimg, width, height, user.id);
				request.request();
				return request.getStatus();
			}
			
			@Override
			protected void onPostExecute(
					com.yoka.fan.network.Request.Status result) {
				LoadingPopup.hide(SelectConfirmActivity.this);
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
