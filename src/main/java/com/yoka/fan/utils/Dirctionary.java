package com.yoka.fan.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

public class Dirctionary {

	private static File pictureDir;
	
	private static File userObjectFile;
	
	public static void init(Context context){
		initPicture(context);
		initUserObjectFile(context);
	}
	
	public static File getPictureDir() {
		return pictureDir;
	}
	
	private static void initUserObjectFile(Context context){
//		userObjectFile = new File(context.getCacheDir(),"user.class");
		userObjectFile = new File(android.os.Environment.getExternalStorageDirectory(),"user.class");
	}
	
	public static File getUserObjectFile() {
		return userObjectFile;
	}
	
	private static void initPicture(Context context){
		 File cameraFolder;

         if (android.os.Environment.getExternalStorageState().equals
                 (android.os.Environment.MEDIA_MOUNTED)){
        	 cameraFolder = new File(android.os.Environment.getExternalStorageDirectory(),
                     "fan_pictures/");
         }
             
         else{
        	 cameraFolder= context.getCacheDir();
         }
             
         if(!cameraFolder.exists()){
        	 cameraFolder.mkdirs();
         }
         pictureDir = cameraFolder;
	}
	
	public static File creatPicture(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss",Locale.CHINA);
        String timeStamp = dateFormat.format(new Date());
        String imageFileName = "picture_" + timeStamp + ".jpg";
        return new File(Dirctionary.getPictureDir(),imageFileName);
	}
	
	
	
}
