package com.yoka.fan.network;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class Request implements Response{

	public static final int REFRESH_CACHE = 1;
	
	public static final int REFRESH_NO_CACHE = 2;
	
	public static final int REFRESH_READ_CACHE = 0;
	
	public static String HOST = "http://songaimin.fan.yoka.com/api/";
	
	public static enum Status{
		SUCCESS,ERROR,SERVER_ERROR,DATA_ERROR
	}
	
	
	
	private Status status;
	
	private String data;
	
	public Request() {
		
	}
	
	public void request(){
		try{
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpost = new HttpPost(getURL());
			Map<String, File> fileMap = fillFiles();
			HttpEntity requsetEntity = null;
			if(fileMap != null){
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();        
			    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			    
			    for(String filename:fileMap.keySet()){
			    	builder.addPart(filename, new FileBody(fileMap.get(filename)));
			    }
			    for(NameValuePair param : fillParams()){
			    	builder.addTextBody(param.getName(), param.getValue());
			    }
			    requsetEntity = builder.build();
			}else{
				requsetEntity = new UrlEncodedFormEntity(fillParams(), HTTP.UTF_8);
			}
			httpost.setEntity(requsetEntity);
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			int status_code = response.getStatusLine().getStatusCode();
			if(status_code == 200){
				
				try {
					JSONObject result = new JSONObject(IOUtils.toString(entity.getContent()));
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
				serverError(status_code);
				status = Status.SERVER_ERROR;
				status = Status.ERROR;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}
	
	protected Map<String, File> fillFiles(){
		return null;
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
		onResultError(code, msg);
		onError(code,msg);
	}
	
	private void serverError(int code){
		String msg = "";
		if(code == 500){
			msg = "服务器内部错误";
		}
		onServerError(code,msg);
		onError(code,msg);
	}
	
	
}
