package com.yoka.fan;



import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.Utils;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

public class App extends Application{

	
	
	@Override
	public void onCreate() {
		super.onCreate();
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		Constant.uuid = tm.getDeviceId();
		Constant.token = Utils.getMD5(Constant.uuid + "fan.yoka.com/app");
	}
	
}
