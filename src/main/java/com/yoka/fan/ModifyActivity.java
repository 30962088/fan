package com.yoka.fan;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.Constant.User;
import com.yoka.fan.wiget.PhotoSelectPopupWindow;
import com.yoka.fan.wiget.PhotoSelectPopupWindow.OnItemClickListener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ModifyActivity extends BaseActivity implements OnClickListener{
	
	private static final int ACTION_REQUEST_GALLERY = 1;
	
	private static final int ACTION_REQUEST_CAMERA = 2;
	
	private int sex;
	
	private View radioFemale;
	
	private View radioMale;
	
	private ImageView photoView;
	
	private Uri cameraPic;
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setContentView(R.layout.modify_layout);
		photoView = (ImageView) findViewById(R.id.user_photo);
		radioFemale = findViewById(R.id.sex_female);
		radioMale = findViewById(R.id.sex_male);
		radioMale.setOnClickListener(this);
		radioFemale.setOnClickListener(this);
		photoView.setOnClickListener(this);
		setSex(Constant.user.MALE);
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

			            File cameraFolder;

			            if (android.os.Environment.getExternalStorageState().equals
			                    (android.os.Environment.MEDIA_MOUNTED))
			                cameraFolder = new File(android.os.Environment.getExternalStorageDirectory(),
			                        "pictures/");
			            else
			                cameraFolder= getCacheDir();
			            if(!cameraFolder.exists())
			                cameraFolder.mkdirs();

			            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
			            String timeStamp = dateFormat.format(new Date());
			            String imageFileName = "picture_" + timeStamp + ".jpg";
			            cameraPic = Uri.fromFile(new File(cameraFolder,imageFileName));
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
		default:
			break;
		}
		
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data){
		
		super.onActivityResult(reqCode, resultCode, data);  
		
		switch (reqCode) {
		case ACTION_REQUEST_GALLERY:
			if (resultCode == Activity.RESULT_OK) {        
				Intent intent = new Intent(this, DragRectActivity.class);
				intent.setData(data.getData());
				startActivity(intent);
			}
			break;
		case ACTION_REQUEST_CAMERA:
			if (resultCode == Activity.RESULT_OK) {        
				Intent intent = new Intent(this, DragRectActivity.class);
				intent.setData(cameraPic);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
		
	}

}
