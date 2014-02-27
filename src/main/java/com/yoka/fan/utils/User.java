package com.yoka.fan.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class User implements Serializable{
	public static final int MALE = 1;

	public static final int FEMALE = 2;

	public String id;

	public String nickname;

	public String photo;

	public String access_token;
	
	public String job;
	
	public int sex;

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
