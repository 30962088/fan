package com.yoka.fan.network;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class Request implements Response{

	public static String HOST = "http://songaimin.fan.yoka.com/api/";
	
	private URL url;
	
	public static enum Status{
		SUCCESS,ERROR,SERVER_ERROR,DATA_ERROR
	}
	
	
	
	private Status status;
	
	private String data;
	
	public Request() {
		
	}
	
	public void request(){
		try {
			url = new URL(getURL());
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			for(NameValuePair pair:fillParams()){
				conn.addRequestProperty(pair.getName(), pair.getValue());
			}
			conn.connect();

			if(conn.getResponseCode() == 200){
				
				try {
					JSONObject result = new JSONObject(IOUtils.toString(conn.getInputStream()));
					int code = Integer.parseInt(result.getString("code"));
					String msg = result.getString("msg");
					String data = result.getString("data");
					if(code == 200){
						success(data);
						status = Status.SUCCESS;
					}else{
						error(code,msg);
						status = Status.DATA_ERROR;
						status = Status.ERROR;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}else{
				serverError(conn.getResponseCode());
				status = Status.SERVER_ERROR;
				status = Status.ERROR;
			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public abstract String getURL();
	
	public abstract List<NameValuePair> fillParams();
	
	public Status getStatus() {
		return status;
	}
	
	public String getData() {
		return data;
	}
	
	private void success(String str){
		data = str;
		onSuccess(str);
	}
	
	private void error(int code,String msg){
		onError(code,msg);
	}
	
	private void serverError(int code){
		onServerError(code);
	}
	
	
}
