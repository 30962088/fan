package com.yoka.fan.wiget;

import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;

import com.yoka.fan.wiget.BuyPopupWindow.GoodsItem;

import android.content.Context;
import android.util.Log;

public class LinkModel{
	
	private String url;
	
	private int width;
	
	private int height;
	

	
	private List<Link> linkList;
	
	private boolean showLink = false;
		
	public LinkModel(String url,int width,int height, List<Link> linkList) {
		this.width = width;
		this.height = height;
		this.url = url;
		this.linkList = linkList;
	}
	
	public void setShowLink(boolean showLink) {
		this.showLink = showLink;
	}
	
	public static String insert(String bag, String marble, int index) {
	    String bagBegin = bag.substring(0,index);
	    String bagEnd = bag.substring(index);
	    return bagBegin + marble + bagEnd;
	}
	
	public boolean isShowLink() {
		return showLink;
	}
	
	public String getUrl(int width,int height){
		String url = insert(this.url,"/resize/"+width+"/"+height,this.url.indexOf("/", 7));
		return url;
	}

	public String getUrl() {
		
		return url;
	}
	
	public List<Link> getLinkList() {
		return linkList;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getWidth(Context context) {
		return (int) (width*context.getResources().getDisplayMetrics().density);
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getHeight(Context context) {
		return (int) (height*context.getResources().getDisplayMetrics().density);
	}
	
	
	public static class Link {
		
		private String id;
		
		private String name;
		
		private float left;
		
		private float top;
		
		private String url;
		
		private String price;
		
		private String typeUrl;
		
		private String img;
		
		
		public Link(String id,String name, float left, float top) {
			this.name = name;
			this.left = left;
			this.top = top;
		}
		
		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public float getLeft() {
			return left;
		}

		public float getTop() {
			return top;
		}
		
		public void setUrl(String url) {
			this.url = url;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getPrice() {
			return price;
		}
		public void setTypeUrl(String typeUrl) {
			this.typeUrl = typeUrl;
		}
		public String getTypeUrl() {
			return typeUrl;
		}
		public String getUrl() {
			return url;
		}
		
		public void setImg(String img) {
			this.img = img;
		}
		public String getImg() {
			return img;
		}
		
		public void setTop(float top) {
			this.top = top;
		}
		
		public void setLeft(float left) {
			this.left = left;
		}
		
		public GoodsItem toGoodsItem(){
			String text = "去购买>";
			if(!UrlValidator.getInstance().isValid(url)){
				text = "暂无购买";
			}
			GoodsItem item = new GoodsItem(typeUrl, name, price, url, img, text, null);
			return item;
		}
		
		
	}
	
}


