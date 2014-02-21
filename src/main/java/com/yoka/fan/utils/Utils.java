package com.yoka.fan.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 
 * @author zhangzimeng
 * 
 */
public class Utils {
	private static ImageLoader imageLoader = null;

	public static int dpToPx(Context context, int dp) {
		return (int) (dp * context.getResources().getDisplayMetrics().density);
	}

	public synchronized static ImageLoader getImageLoader(Context context) {
		if (imageLoader == null) {
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true).build();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context).defaultDisplayImageOptions(options).build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
		}

		return imageLoader;
	}

	/**
	 * Use md5 encoded code value
	 * 
	 * @param sInput
	 *            clearly @ return md5 encrypted password
	 */
	public static String getMD5(String sInput) {

		String algorithm = "";
		if (sInput == null) {
			return "null";
		}
		try {
			algorithm = System.getProperty("MD5.algorithm", "MD5");
		} catch (SecurityException se) {
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte buffer[] = sInput.getBytes();

		for (int count = 0; count < sInput.length(); count++) {
			md.update(buffer, 0, count);
		}
		byte bDigest[] = md.digest();
		BigInteger bi = new BigInteger(bDigest);
		return (bi.toString(16));
	}
}
