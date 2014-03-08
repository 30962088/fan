package com.yoka.fan;



import com.yoka.fan.utils.Category;
import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.Dirctionary;
import com.yoka.fan.utils.Utils;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

public class App extends Application{

	private static App instance;
	
	public static App getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		Constant.uuid = tm.getDeviceId();
//		Constant.token = Utils.getMD5(Constant.uuid + "fan.yoka.com/app");
		Constant.uuid = "uuid12345678";
		Constant.token = "258c60d8f7ec200a0aae782dca59d0a2";
		Dirctionary.init(this);
		Category.sync(null);
		
	}
	
}
