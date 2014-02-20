package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yoka.fan.utils.RelativeDateFormat;
import com.yoka.fan.wiget.CommonListModel;
import com.yoka.fan.wiget.LinkModel;

public class ListItemData {

	public String owner_id;
	
	public String owner_name;
	
	public long ctime;
	
	public String title;
	
	public String description;
	
	public String owner_face;
	
	public Map<String, Link> link_goods;
	
	public int likes;
	
	public List<String> tags;
	
	public int comments;
	
	public Img show_img;
	
	public static class Img{
		public String url;
		public String width;
		public String height;
	}
	
	public static class Link{
		
		public String left;
		
		public String top;
		
		public String type;
		
		public String brand;
		
		public String color;
		
		public String url;
		
		public String price;
		
		public String tags;
		
		public String en_tags;
		
		private static float StringToFloat(String val){
			return Float.parseFloat(val.substring(0,val.length()-2))/100;
		}
		
		public float getLeft(){
			return StringToFloat(left);
		}
		
		public float getTop(){
			return StringToFloat(top);
		}
		
	}
	
	public CommonListModel toCommonListModel(){
		CommonListModel model = new CommonListModel();
		model.setComment(comments);
		model.setDatetime(RelativeDateFormat.format(new Date(ctime)));
		List<LinkModel.Link> links = new ArrayList<LinkModel.Link>();
		for(String id : link_goods.keySet()){
			Link link = link_goods.get(id);
			links.add(new LinkModel.Link(id, link.brand, link.getLeft(), link.getTop()));
		}
		model.setLinkModel(new LinkModel(show_img.url, links));
		model.setName(owner_name);
		model.setPhoto(owner_face);
		model.setStar(likes);
		model.setTags(tags);
		return model;
	}
	
}