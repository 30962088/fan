package com.yoka.fan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
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
	
	private TextView nextTextView;
	
	private static SelectMainActivity instance;
	
	public static SelectMainActivity getInstance() {
		return instance;
	}
	
	public static class Result implements Serializable{
		private String id = UUID.randomUUID().toString();
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
		public String getId() {
			return id;
		}
		
		public static void update(List<Model> list,Result result){
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
			result.link.setBrand(brand);
			result.link.setColor(color);
			result.link.setType(type_en);
			result.link.setPrice(price);
			result.link.setUrl(url);
			result.name = brand+" "+type;
		}
		
		public static Result toResult(List<Model> list,float left,float top){
			String type_en = null,color = null,url = null,brand = null,type = null;
			Float price = null;
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
		if(results.size() == 0){
			nextTextView.setText("重拍");
		}else{
			nextTextView.setText("完成");
		}
		List<LinkModel.Link> list = new ArrayList<LinkModel.Link>();
		for(Result result : results){
			list.add(new LinkModel.Link("", result.name, result.link.getLeftFloat(), result.link.getTopFloat()));
		}
		if(results.size() == 0){
			SpannableString string = new SpannableString("点击图片，为服饰添加标记或全部跳过");
			string.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), string.length()-5, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			countView.setText(string);
		}else{
			countView.setText(getString(R.string.share_count, 5-list.size()));
		}
		
		LinkModel linkModel = new LinkModel(url, -1, -1, list);
		linkModel.setShowLink(true);
		linkedView.load(linkModel);
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		url = getIntent().getDataString();
		imageLoader = Utils.getImageLoader(this);
		setContentView(R.layout.select_main_layout);
		linkedView = (LinkedView) findViewById(R.id.linked_view);
		linkedView.setMove(true);
		linkedView.setOnTagClickListener(this);
		linkedView.setClosed(true);
		linkedView.setOnline(false);
		nextTextView = (TextView) findViewById(R.id.next_text);
		countView = (TextView) findViewById(R.id.count);
		countView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(countView.getText().toString().indexOf("全部跳过")!=-1){
					doFinish();
				}
				
			}
		});
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
				if(results.size() == 0){
					finish();
					MainActivity.getInstance().openShare();
				}else{
					doFinish();
				}
				
				
			}
		});
		
		
	}
	
	private void doFinish(){
		Intent intent = new Intent(SelectMainActivity.this,SelectConfirmActivity.class);
		intent.putExtra(SelectConfirmActivity.PARAM_RESULT, results);
		intent.putExtra(BaseSelectActivity.PARAM_IMG_PATH, url);
		intent.putExtra(PARAM_WIDTH, width);
		intent.putExtra(PARAM_HEIGHT, height);
		startActivity(intent);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(ACTION_COMPLETE.equals(intent.getAction())){
			
			Result result = (Result) intent.getSerializableExtra(BaseSelectActivity.PARAM_SELECTED_RESULT);
			List<Model> list = (List<Model>) intent.getSerializableExtra(BaseSelectActivity.PARAM_SELECTED_LIST);
			if(result != null){
				Result.update(list, result);
				for(Iterator<Result> itor = results.iterator();itor.hasNext();){
					Result r = itor.next();
					if(r.id.equals(result.id)){
						itor.remove();
						break;
					}
				}
				results.add(result);
				
				
			}else{
				
				float top = intent.getFloatExtra(PARAM_TOP, 0);
				float left = intent.getFloatExtra(PARAM_LEFT, 0);
				results.add(Result.toResult(list,left,top));
			}
			
			
			load();
		}
	}
	
	public void remove(int index){
		if(results != null){
			results.remove(index);
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
		
		for(Iterator<Result> itor = results.iterator();itor.hasNext();){
			Result result = itor.next();
			if(result.name == link.getName()){
				Intent intent = new Intent(SelectMainActivity.this,SelectCategoryActivity.class);
				intent.putExtra(SelectBrandActivity.PARAM_IMG_PATH, url);
				intent.putExtra(SelectBrandActivity.PARAM_SELECTED_RESULT, result);
				startActivity(intent);
				break;
			}
		}
		
	}



	@Override
	public void onMove(com.yoka.fan.wiget.LinkModel.Link link) {
		for(Result result : results){
			if(result.name.equals(link.getName())){
				result.link.setLeft(link.getLeft()*100+"%");
				result.link.setTop(link.getTop()*100+"%");
				break;
			}
		}
		
	}
	
}
