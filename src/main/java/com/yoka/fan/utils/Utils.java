package com.yoka.fan.utils;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yoka.fan.LoginActivity;
import android.net.NetworkInfo;
import android.view.animation.Transformation;

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

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public synchronized static ImageLoader getImageLoader(Context context) {
		if (imageLoader == null) {
			DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true).cacheOnDisc(true).build();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context).defaultDisplayImageOptions(options).memoryCacheSizePercentage(33).build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
		}

		return imageLoader;
	}

	public static void setGridViewHeightBasedOnChildren(GridView gridView) {
		BaseAdapter listAdapter = (BaseAdapter) gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		int count = listAdapter.getCount();
		View listItem = listAdapter.getView(0, null, gridView);
		listItem.measure(0, 0); // 计算子项View 的宽高
		totalHeight = listItem.getMeasuredHeight(); // 统计所有子项的总高度
		int yu = count % 4;
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		if (yu > 0) {
			params.height = (count - yu) / 4 * (totalHeight + 10) + totalHeight;
		} else {
			params.height = count / 4 * totalHeight + (count / 4 - 1) * 10;
		}
		gridView.setLayoutParams(params);
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

	public static boolean isValidEmailAddress(String email) {
		boolean stricterFilter = true;
		String stricterFilterString = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
		String laxString = ".+@.+\\.[A-Za-z]{2}[A-Za-z]*";
		String emailRegex = stricterFilter ? stricterFilterString : laxString;
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailRegex);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	public static void tip(final Context context, final String str) {
		new Handler(context.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

			}
		});

	}

	private static ConnectivityManager connMgr;

	public static boolean isMobileNetworkAvailable(Context con) {
		if (null == connMgr) {
			connMgr = (ConnectivityManager) con
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		NetworkInfo wifiInfo = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobileInfo = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (wifiInfo != null && wifiInfo.isAvailable()) {
			return true;
		} else if (mobileInfo != null && mobileInfo.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}

	public static void expand(final View v) {
		v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final int targtetHeight = v.getMeasuredHeight();

		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {

			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
						: (int) (targtetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		a.setDuration(200);
		v.startAnimation(a);
	}

	public static void collapse(final View v) {
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight
							- (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		a.setDuration(200);
		v.startAnimation(a);
	}
	
	public static boolean isValidURL(String url) {  

	    URL u = null;

	    try {  
	        u = new URL(url);  
	    } catch (MalformedURLException e) {  
	        return false;  
	    }

	    try {  
	        u.toURI();  
	    } catch (URISyntaxException e) {  
	        return false;  
	    }  

	    return true;  
	} 

}
