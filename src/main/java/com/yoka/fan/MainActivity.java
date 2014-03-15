package com.yoka.fan;

import java.io.File;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.yoka.fan.utils.Dirctionary;
import com.yoka.fan.utils.Relation;
import com.yoka.fan.utils.User;
import com.yoka.fan.wiget.HomeFragment;
import com.yoka.fan.wiget.PhotoSelectPopupWindow;
import com.yoka.fan.wiget.SettingFragment;
import com.yoka.fan.wiget.ZoneFragment;
import com.yoka.fan.wiget.PhotoSelectPopupWindow.OnItemClickListener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {

	private Fragment mContent;

	private TextView mTitleView;

	private SidingMenuFragment menuFragment;

	private View actionbarCarema;

	private View actionbarSetting;

	private static MainActivity instance;

	public static MainActivity getInstance() {
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new HomeFragment();
		setContentView(R.layout.main_layout);
		mTitleView = (TextView) findViewById(R.id.actionbar_title);
		actionbarCarema = findViewById(R.id.actionbar_camera);
		actionbarCarema.setOnClickListener(this);
		actionbarSetting = findViewById(R.id.actionbar_setting);
		actionbarSetting.setOnClickListener(this);
		initSlidingMenu();

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		login(User.readUser());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	private void initSlidingMenu() {
		menuFragment = new SidingMenuFragment();
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, menuFragment).commit();

		findViewById(R.id.actionbar_nav).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						toggle();

					}
				});

	}

	public void switchContent(Fragment fragment) {
		if (fragment instanceof ZoneFragment) {
			actionbarCarema.setVisibility(View.GONE);
			actionbarSetting.setVisibility(View.VISIBLE);
			mTitleView.setText("我的空间");
		} else {
			actionbarCarema.setVisibility(View.VISIBLE);
			actionbarSetting.setVisibility(View.GONE);
			if (fragment instanceof HomeFragment) {
				mTitleView.setText("潮流搭配");
			} else if (fragment instanceof SettingFragment) {
				mTitleView.setText("设置");
			}
		}

		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}

	public void login(User user) {
		if (user != null) {
			menuFragment.login(user);
			Relation.sync(user, null);
		} else {
			menuFragment.logout();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.actionbar_setting:
			switchContent(new SettingFragment());
			break;
		case R.id.actionbar_camera:
			openShare();
			break;
		default:
			break;
		}

	}

	private Uri cameraPic;
	
	private static final int ACTION_REQUEST_CAMERA = 1;
	
	private static final int ACTION_REQUEST_GALLERY = 2;
	
	private static final int ACTION_REQUEST_SELECTION =3;
	
	public void openShare() {
		User user = User.readUser();
		if(user == null){
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
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
		Intent intent = new Intent(this, SelectPicActivity.class);
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
	
	
	private long currentBackPressedTime = 0;

	private static final int BACK_PRESSED_INTERVAL = 2000;

	
	@Override
	public void onBackPressed() {
		// 判断时间间隔
		if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
			currentBackPressedTime = System.currentTimeMillis();
			Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
		} else {
			// 退出
			finish();
		}
	}

}
