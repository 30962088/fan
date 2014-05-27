package com.yoka.fan.network;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yoka.fan.utils.RelativeDateFormat;
import com.yoka.fan.wiget.CommonListModel;
import com.yoka.fan.wiget.LinkModel;
import com.yoka.fan.wiget.CommonListModel.NameValuePair;

public class ListItemData {

	public String _id;
	
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
	
	public Map<String, String> meta_attr;
	
	public boolean stared = false;
	
	public static class Img{
		public String url;
		public int width;
		public int height;
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
		
		public String type_url;
		
		public String img;
		
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
		model.setId(_id);
		model.setTitle(title);
		model.setComment(comments);
		model.setDatetime(RelativeDateFormat.format(new Date(ctime)));
		List<LinkModel.Link> links = new ArrayList<LinkModel.Link>();
		for(String id : link_goods.keySet()){
			Link link = link_goods.get(id);
			LinkModel.Link link2 = new LinkModel.Link(id, link.brand+" "+link.tags, link.getLeft(), link.getTop());
			link2.setPrice(link.price);
			link2.setImg(link.img);
			link2.setTypeUrl(link.type_url);
			link2.setUrl(link.url);
			links.add(link2);
		}
		model.setLinkModel(new LinkModel(show_img.url,show_img.width,show_img.height, links));
		model.setName(owner_name);
		model.setPhoto(owner_face);
		model.setStar(likes);
		
		List<NameValuePair> nameValuePairs = new ArrayList<CommonListModel.NameValuePair>();
		
		if(meta_attr != null){
			for(String key : meta_attr.keySet()){
				nameValuePairs.add(new NameValuePair(key, meta_attr.get(key)));
			}
		}
		
		model.setTags(nameValuePairs);
		model.setUser_id(owner_id);
		model.setDescr(description);
		model.setMetaAttr(meta_attr);
		model.setStared(stared);
		return model;
	}
	
}
