package com.yoka.fan.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;

public class ShareAccount implements Serializable {
	public static class SINAToken implements Serializable {
		public String uid;
		public String accessToken;
		public String expires_in;
		public String remind_in;

		public Oauth2AccessToken toOauth2AccessToken() {
			Oauth2AccessToken token = new Oauth2AccessToken(accessToken,
					String.valueOf(expires_in));
			token.setUid(uid);
			return token;
		}
	}

	public WeiboToken qweibo;

	public SINAToken weibo;
	
	private static ShareAccount account;

	public static ShareAccount read() {
		if (account != null) {
			return account;
		}
		try {
			FileInputStream fi = new FileInputStream(
					Dirctionary.getShareAccountFile());
			ObjectInputStream oi = new ObjectInputStream(fi);
			account = (ShareAccount) oi.readObject();
			oi.close();
			return account;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
		}
		return account;

	}

	public static void save(ShareAccount _account) {

		account = _account;
		try {

			FileOutputStream fout = new FileOutputStream(
					Dirctionary.getShareAccountFile());
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(account);
			oos.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
