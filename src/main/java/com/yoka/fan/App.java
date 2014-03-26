package com.yoka.fan;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.yoka.fan.utils.Category;
import com.yoka.fan.utils.Constant;
import com.yoka.fan.utils.Dirctionary;
import com.yoka.fan.utils.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

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
		
		
		Thread.setDefaultUncaughtExceptionHandler(handler);

	}
	
	public static final String DOUBLE_LINE_SEP = "\n\n";
	
	public static final String SINGLE_LINE_SEP = "\n";
	
	UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {

		@Override
		public void uncaughtException(Thread thread, Throwable e) {
			System.err.println(e);
			StackTraceElement[] arr = e.getStackTrace();
	        final StringBuffer report = new StringBuffer(e.toString());
	        final String lineSeperator = "-------------------------------\n\n";
	        report.append(DOUBLE_LINE_SEP);
	        report.append("--------- Stack trace ---------\n\n");
	        for (int i = 0; i < arr.length; i++) {
	            report.append( "    ");
	            report.append(arr[i].toString());
	            report.append(SINGLE_LINE_SEP);
	        }
	        report.append(lineSeperator);
	        // If the exception was thrown in a background thread inside
	        // AsyncTask, then the actual exception can be found with getCause
	        report.append("--------- Cause ---------\n\n");
	        Throwable cause = e.getCause();
	        if (cause != null) {
	            report.append(cause.toString());
	            report.append(DOUBLE_LINE_SEP);
	            arr = cause.getStackTrace();
	            for (int i = 0; i < arr.length; i++) {
	                report.append("    ");
	                report.append(arr[i].toString());
	                report.append(SINGLE_LINE_SEP);
	            }
	        }
	        // Getting the Device brand,model and sdk verion details.
	        report.append(lineSeperator);
	        report.append("--------- Device ---------\n\n");
	        report.append("Brand: ");
	        report.append(Build.BRAND);
	        report.append(SINGLE_LINE_SEP);
	        report.append("Device: ");
	        report.append(Build.DEVICE);
	        report.append(SINGLE_LINE_SEP);
	        report.append("Model: ");
	        report.append(Build.MODEL);
	        report.append(SINGLE_LINE_SEP);
	        report.append("Id: ");
	        report.append(Build.ID);
	        report.append(SINGLE_LINE_SEP);
	        report.append("Product: ");
	        report.append(Build.PRODUCT);
	        report.append(SINGLE_LINE_SEP);
	        report.append(lineSeperator);
	        report.append("--------- Firmware ---------\n\n");
	        report.append("SDK: ");
	        report.append(Build.VERSION.SDK);
	        report.append(SINGLE_LINE_SEP);
	        report.append("Release: ");
	        report.append(Build.VERSION.RELEASE);
	        report.append(SINGLE_LINE_SEP);
	        report.append("Incremental: ");
	        report.append(Build.VERSION.INCREMENTAL);
	        report.append(SINGLE_LINE_SEP);
	        report.append(lineSeperator);
	        
	        CrashActivity.open(getApplicationContext(), report.toString());
	        
	        System.exit(0);
		}
	};
	
	
	
	
}
