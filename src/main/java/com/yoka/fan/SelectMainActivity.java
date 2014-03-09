package com.yoka.fan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.yoka.fan.SelectCategoryActivity.Model;
import com.yoka.fan.network.CollSave;
import com.yoka.fan.network.CollSave.Link;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.LinkModel;
import com.yoka.fan.wiget.LinkedView;
import com.yoka.fan.wiget.LinkedView.onImageClickListener;
import com.yoka.fan.wiget.LinkedView.onTagClickListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SelectMainActivity extends Activity implements onTagClickListener{

	public static final String ACTION_COMPLETE = "ACTION_COMPLETE";

	public static final String PARAM_TOP = "PARAM_TOP";
	
	public static final String PARAM_LEFT = "PARAM_LEFT";
	
	public static final String PARAM_WIDTH = "PARAM_WIDTH";
	
	public static final String PARAM_HEIGHT = "PARAM_HEIGHT";
	
	private LinkedView linkedView;
	
	private TextView countView;
	
	private ArrayList<Result> results = new ArrayList<Result>();
	
	private String url;
	
	private int width;
	
	private int height;
	
	private ImageLoader imageLoader;
	
	public static class Result implements Serializable{
		private Link link;
		private String name;
		public Result(Link link,String name) {
			this.link = link;
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public Link getLink() {
			return link;
		}
		public static Result toResult(List<Model> list,float left,float top){
			String type_en = null,color = null,url = null,brand = null,type = null;
			float price = 0;
			for(Model model : list){
				switch (model.getType()) {
				case Model.TYPE_BRAND:
					brand = model.getName();
					break;
				case Model.TYPE_COLOR:
					color = model.getName();
					break;
				case Model.TYPE_LINK:
					url = model.getName();
					break;
				case Model.TYPE_PRICE:
					price = Float.parseFloat(model.getName());
					break;
				case Model.TYPE_TAG:
					type = model.getName();
					type_en = model.getId();
					break;
				default:
					break;
				}
			}
			return new Result(new Link(brand, color, left, top, type_en, price, url), brand+" "+type);
		}
		
	}
	
	private void load(){
		List<LinkModel.Link> list = new ArrayList<LinkModel.Link>();
		for(Result result : results){
			list.add(new LinkModel.Link("", result.name, result.link.getLeftFloat(), result.link.getTopFloat()));
		}
		
		countView.setText(getString(R.string.share_count, 5-list.size()));

		linkedView.load(new LinkModel(url, -1, -1, list));
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		url = getIntent().getDataString();
		imageLoader = Utils.getImageLoader(this);
		setContentView(R.layout.select_main_layout);
		linkedView = (LinkedView) findViewById(R.id.linked_view);
		linkedView.setOnTagClickListener(this);
		linkedView.setClosed(true);
		countView = (TextView) findViewById(R.id.count);
		linkedView.setOnImageClickListener(new onImageClickListener() {
			
			@Override
			public void onClick(final float left, final float top) {
				if(left > 0 && top>0){
					Intent intent = new Intent(SelectMainActivity.this,SelectCategoryActivity.class);
					intent.putExtra(SelectBrandActivity.PARAM_IMG_PATH, url);
					intent.putExtra(PARAM_LEFT, left);
					intent.putExtra(PARAM_TOP, top);
					startActivity(intent);
				}
				
			}
		});
		
		
		imageLoader.loadImage(url, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				width = loadedImage.getWidth();
				height = loadedImage.getHeight();
				load();
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
		findViewById(R.id.base_prev).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		findViewById(R.id.base_next).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(SelectMainActivity.this,SelectConfirmActivity.class);
				intent.putExtra(SelectConfirmActivity.PARAM_RESULT, results);
				intent.putExtra(BaseSelectActivity.PARAM_IMG_PATH, url);
				intent.putExtra(PARAM_WIDTH, width);
				intent.putExtra(PARAM_HEIGHT, height);
				startActivity(intent);
				
			}
		});
		
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(ACTION_COMPLETE.equals(intent.getAction())){
			List<Model> list = (List<Model>) intent.getSerializableExtra(BaseSelectActivity.PARAM_SELECTED_LIST);
			float top = intent.getFloatExtra(PARAM_TOP, 0);
			float left = intent.getFloatExtra(PARAM_LEFT, 0);
			results.add(Result.toResult(list,left,top));
			load();
		}
	}



	@Override
	public void onClose(com.yoka.fan.wiget.LinkModel.Link link) {
		for(Iterator<Result> itor = results.iterator();itor.hasNext();){
			Result result = itor.next();
			if(result.name == link.getName()){
				itor.remove();
				break;
			}
		}
		load();
		
	}



	@Override
	public void onClick(com.yoka.fan.wiget.LinkModel.Link link) {
		// TODO Auto-generated method stub
		
	}
	
}
