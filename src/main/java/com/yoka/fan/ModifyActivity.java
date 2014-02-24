package com.yoka.fan;


import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.Constant.User;
import com.yoka.fan.wiget.PhotoSelectPopupWindow;
import com.yoka.fan.wiget.PhotoSelectPopupWindow.OnItemClickListener;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ModifyActivity extends BaseActivity implements OnClickListener{
	
	private int sex;
	
	private View radioFemale;
	
	private View radioMale;
	
	private ImageView photoView;
	
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
					// TODO Auto-generated method stub
					
				}
			});
			break;
		default:
			break;
		}
		
	}

}
