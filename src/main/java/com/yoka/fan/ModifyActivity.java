package com.yoka.fan;



import java.io.File;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.network.Modify;
import com.yoka.fan.network.Request;
import com.yoka.fan.network.Request.Status;
import com.yoka.fan.utils.ChangeHead;
import com.yoka.fan.utils.Dirctionary;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.PhotoSelectPopupWindow;
import com.yoka.fan.wiget.PhotoSelectPopupWindow.OnItemClickListener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class ModifyActivity extends BaseActivity implements OnClickListener{
	
	private static final int ACTION_REQUEST_GALLERY = 1;
	
	private static final int ACTION_REQUEST_CAMERA = 2;
	
	private static final int ACTION_REQUEST_SELECTION = 3;
	
	private int sex;
	
	private View radioFemale;
	
	private View radioMale;
	
	private ImageView photoView;
	
	private Uri cameraPic;
	
	private ImageLoader imageLoader;
	
	private File photoFile;
	
	private EditText nickView;
	
	private EditText jobView;
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setContentView(R.layout.modify_layout);
		jobView = (EditText) findViewById(R.id.job);
		nickView = (EditText) findViewById(R.id.nick);
		nickView.setText(User.readUser().nickname);
		photoView = (ImageView) findViewById(R.id.user_photo);
		radioFemale = findViewById(R.id.sex_female);
		radioMale = findViewById(R.id.sex_male);
		radioMale.setOnClickListener(this);
		radioFemale.setOnClickListener(this);
		photoView.setOnClickListener(this);
		setSex(User.readUser().sex);
		imageLoader = Utils.getImageLoader(this);
		findViewById(R.id.login_btn).setOnClickListener(this);
	}
	
	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "个人信息";
	}
	
	private void setSex(int sex){
		this.sex = sex;
		switch (sex) {
		case User.MALE:
			radioMale.setSelected(true);
			radioFemale.setSelected(false);
			break;
		case User.FEMALE:
			radioMale.setSelected(false);
			radioFemale.setSelected(true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sex_female:
			setSex(User.FEMALE);
			break;
		case R.id.sex_male:
			setSex(User.MALE);
			break;
		case R.id.user_photo:
			new PhotoSelectPopupWindow(this,new OnItemClickListener() {
				
				@Override
				public void onItemClick(int id) {
					switch (id) {
					case R.id.take_photo:
						Intent getCameraImage = new Intent("android.media.action.IMAGE_CAPTURE");
			            cameraPic = Uri.fromFile(Dirctionary.creatPicture());
			            getCameraImage.putExtra(MediaStore.EXTRA_OUTPUT, cameraPic);
			            startActivityForResult(getCameraImage, ACTION_REQUEST_CAMERA);
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
			});
			break;
		case R.id.login_btn:
			onlogin();
			break;
		default:
			break;
		}
		
	}
	
	private void onlogin() {
		final User user = User.readUser();
		final String user_id = user.id;
		final String access_token = user.access_token;
		final String job = jobView.getText().toString();
		final String nick = nickView.getText().toString();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(photoFile != null){
					ChangeHead changeHead = new ChangeHead(user_id, photoFile, access_token){
						public void onError(int code, String msg) {
							Utils.tip(ModifyActivity.this, msg);
						};
					};
					changeHead.request();	
					user.photo = changeHead.getFileUrl();
				}
				
				Modify modify = new Modify(access_token, job, nick, sex, user_id){
					public void onSuccess(String data) {
						user.nickname = nick;
						user.sex = sex;
						user.job = job;
						User.saveUser(user);
						Utils.tip(ModifyActivity.this,"修改成功");
						finish();
					};
					
					public void onError(int code, String msg) {
						Utils.tip(ModifyActivity.this, msg);
					};
				};
				modify.request();
				
				
			}
		}).start();
		
	}
	

	private void onSelectSuccess(Uri uri){
		Intent intent = new Intent(this, DragRectActivity.class);
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
				photoFile = new File(data.getData().getPath());
				imageLoader.displayImage(data.getDataString(), photoView);
			}
			break;
		default:
			break;
		}
		
	}

}
