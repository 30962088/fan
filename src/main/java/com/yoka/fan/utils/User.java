package com.yoka.fan.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.yoka.fan.network.Info;
import com.yoka.fan.network.Info.BaseInfo;
import com.yoka.fan.network.Info.Result;

public class User implements Serializable{
	public static final int MALE = 1;

	public static final int FEMALE = 2;

	public String id;

	public String nickname;

	public String photo;

	public String access_token;
	
	public String job;
	
	public int sex;
	
	public WeiboToken qweibo;
	
	private static User user;
	
	

	public static User readUser() {
		if (user != null) {
			return user;
		}
		try {
			FileInputStream fi = new FileInputStream(
					Dirctionary.getUserObjectFile());
			ObjectInputStream oi = new ObjectInputStream(fi);
			user = (User) oi.readObject();
			oi.close();
			return user;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;

	}
	
	public static Result getInfo(User user){
		
		Info info = new Info(user.access_token,user.id,user.id);
		info.request();
		return info.getResult().get(user.id);
	}
	
	public static void fillInfo(User user){
		Result result = getInfo(user);
		BaseInfo baseInfo = result.getOther_info().getBase_info();
		user.sex = baseInfo.getSex();
		user.job = baseInfo.getJob();
	}

	public static void saveUser(User _user) {
		
		user = _user;
		try {

			FileOutputStream fout = new FileOutputStream(
					Dirctionary.getUserObjectFile());
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(user);
			oos.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
