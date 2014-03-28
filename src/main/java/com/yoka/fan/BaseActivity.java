package com.yoka.fan;

import com.umeng.analytics.MobclickAgent;
import com.yoka.fan.utils.Dirctionary;
import com.yoka.fan.utils.User;
import com.yoka.fan.wiget.PhotoSelectPopupWindow;
import com.yoka.fan.wiget.PhotoSelectPopupWindow.OnItemClickListener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseActivity extends FragmentActivity{

	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		
		setContentView(LayoutInflater.from(this).inflate(layoutResID,null));
		
	}
	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(R.layout.base_activity_layout);
		
		((ViewGroup)findViewById(R.id.content_view)).addView(view);
		
		
		setWebTitle(getActionBarTitle());
		
		findViewById(R.id.actionbar_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
	
	public void setWebTitle(String title){
		((TextView)findViewById(R.id.actionbar_title)).setText(title);
	}
	
	@Override
	public void setContentView(View view) {
		setContentView(view,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	protected abstract String getActionBarTitle();
	
	
	protected ImageView getActionbarRight(){
		return (ImageView) findViewById(R.id.actionbar_right);
	}
	
	protected ImageView getActionbarLeft(){
		return (ImageView) findViewById(R.id.actionbar_left);
	}
	
	private Uri cameraPic;
	
	private static final int ACTION_REQUEST_CAMERA = 1;
	
	private static final int ACTION_REQUEST_GALLERY = 2;
	
	private static final int ACTION_REQUEST_SELECTION =3;
	
	public void openShare() {
		new PhotoSelectPopupWindow(this, new OnItemClickListener() {

			@Override
			public void onItemClick(int id) {
				switch (id) {
				case R.id.take_photo:
					Intent getCameraImage = new Intent(
							"android.media.action.IMAGE_CAPTURE");
					cameraPic = Uri.fromFile(Dirctionary.creatPicture());
					getCameraImage.putExtra(MediaStore.EXTRA_OUTPUT, cameraPic);
					startActivityForResult(getCameraImage,
							ACTION_REQUEST_CAMERA);
					break;
				case R.id.read_photo:
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");

					Intent chooser = Intent.createChooser(intent, "从本地相册读取");
					startActivityForResult(chooser, ACTION_REQUEST_GALLERY);
					break;
				default:
					break;
				}
			}
		},"分享你的搭配");
	}
	
	private void onSelectSuccess(Uri uri){
		Intent intent = new Intent(this, SelectMainActivity.class);
		intent.setData(uri);
		startActivityForResult(intent, ACTION_REQUEST_SELECTION);
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data){
		
		super.onActivityResult(reqCode, resultCode, data);  
		
		switch (reqCode) {
		case ACTION_REQUEST_GALLERY:
			if (resultCode == Activity.RESULT_OK) {        
				onSelectSuccess(data.getData());
			}
			break;
		case ACTION_REQUEST_CAMERA:
			if (resultCode == Activity.RESULT_OK) {        
				onSelectSuccess(cameraPic);
			}
			break;
		case ACTION_REQUEST_SELECTION:
			if (resultCode == Activity.RESULT_OK) {
				Intent intent = new Intent(this,SelectMainActivity.class);
				intent.setData(data.getData());
				startActivity(intent);
			}
			break;
		default:
			break;
		}
		
	}
	
}
