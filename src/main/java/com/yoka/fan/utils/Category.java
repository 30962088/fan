package com.yoka.fan.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.yoka.fan.network.CategoryRequest;
import com.yoka.fan.network.Request.Status;

public class Category implements Serializable {

	public static class Brand implements Serializable {

		private String _id;

		private String desc;

		private String en_name;

		private String name;

		private String pinyin;

		private String py_first;

		private String recommend;

		public String get_id() {
			return _id;
		}

		public String getDesc() {
			return desc;
		}

		public String getEn_name() {
			return en_name;
		}

		public String getName() {
			return name;
		}

		public String getPinyin() {
			return pinyin;
		}

		public String getPy_first() {
			return py_first;
		}

		public String getRecommend() {
			return recommend;
		}
	}

	public static class Tag implements Serializable {
		private String tag_en;
		private String tag_url;
		private String tag_zh;

		public String getTag_en() {
			return tag_en;
		}

		public String getTag_url() {
			return tag_url;
		}

		public String getTag_zh() {
			return tag_zh;
		}

	}

	public static class Color implements Serializable {
		private String en;
		private String url;
		private String zh;

		public String getEn() {
			return en;
		}

		public String getUrl() {
			return url;
		}

		public String getZh() {
			return zh;
		}
	}

	public static interface OperatorListener {
		public void success(Category category);
	}

	private List<Brand> brands;

	private List<Map<String, List<Tag>>> cats;

	private List<Color> colors;

	private static Category instance;

	public Map<String, List<Tag>> getCats() {
		if(cats == null || cats.size() == 0){
			return new HashMap<String, List<Tag>>();
		}
		return cats.get(0);
	}
	
	public static void sync(final OperatorListener listener) {
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				CategoryRequest request = new CategoryRequest();
				request.request();
				if (request.getStatus() == Status.SUCCESS) {
					Category category = request.getCategory();
					save(category);
					if (listener != null) {
						listener.success(category);
					}
				}
				
			}
		});
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
		
	}

	public static void read(final OperatorListener listener) {
		if (instance != null) {
			if (listener != null) {
				listener.success(instance);
				return;
			}
		}
		Category category = readFile();
		if(category != null){
			instance = category;
			if(listener != null){
				listener.success(category);
				return;
			}
		}
		sync(new OperatorListener() {

			@Override
			public void success(Category category) {
				instance = category;
				if (listener != null) {
					listener.success(instance);
				}

			}
		});

	}
	
	private static Category readFile(){
		Category category = null;
		ObjectInputStream oi = null;
		try {
			FileInputStream fi = new FileInputStream(
					Dirctionary.getCateObjectFile());
			oi = new ObjectInputStream(fi);
			category = (Category) oi.readObject();
		} catch (Exception e) {
//			Dirctionary.getCateObjectFile().delete();
			e.printStackTrace();
		}finally{
			try {
				if(oi != null){
					oi.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return category;
	}

	private static void save(final Category category) {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					FileOutputStream fout = new FileOutputStream(
							Dirctionary.getCateObjectFile());
					ObjectOutputStream oos = new ObjectOutputStream(fout);
					oos.writeObject(category);
					oos.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});
		thread.setPriority(Thread.NORM_PRIORITY - 2);
		thread.start();

	}

	public static interface findCatsListener {
		public void success(Map<String, List<Tag>> result);
	}
	
	public static interface findColorListener{
		public void success(List<Color> result);
	}
	public static interface findBrandListener{
		public void success(List<Brand> result);
	}
	
	public static void findCatsByPinyin(findCatsListener listener){
		findCatsByPinyin(null,listener);
	}
	
	public static void findColorByPinyin(findColorListener listener){
		findColorByPinyin(null,listener);
	}
	
	public static void findColorByPinyin(final String pinyin,final findColorListener listener){
		if (listener != null) {
			read(new OperatorListener() {

				@Override
				public void success(Category category) {
					if (TextUtils.isEmpty(pinyin)) {
						listener.success(category.colors);
					}else{
						List<Color> result = new ArrayList<Category.Color>();
						for(Color color:category.colors){
							if(color.zh.indexOf(pinyin) != -1){
								result.add(color);
							}
						}
						listener.success(result);
					}
					

				}
			});
		}
	}
	public static void findBrandByPinyin(findBrandListener listener){
		findBrandByPinyin(null,listener);
	}
	public static void findBrandByPinyin(final String pinyin,final findBrandListener listener){
		if (listener != null) {
			read(new OperatorListener() {

				@Override
				public void success(Category category) {
					if (TextUtils.isEmpty(pinyin)) {
						listener.success(category.brands);
					}else{
						List<Brand> result = new ArrayList<Category.Brand>();
						for(Brand brand:category.brands){
							if(brand.name.indexOf(pinyin) != -1 || brand.en_name.indexOf(pinyin) != -1 || brand.py_first.indexOf(pinyin) != -1){
								result.add(brand);
							}
						}
						listener.success(result);
					}
					

				}
			});
		}
	}

	public static void findCatsByPinyin(final String pinyin,
			final findCatsListener listener) {
		if (listener != null) {
			read(new OperatorListener() {

				@Override
				public void success(Category category) {
					if (TextUtils.isEmpty(pinyin)) {
						listener.success(category.getCats());
					}else{
						Map<String,List<Tag>> result = new HashMap<String, List<Tag>>();
						for(String cate:category.getCats().keySet()){
							List<Tag> list = category.getCats().get(cate);
							List<Tag> array = null;
							for(Tag tag : list){
								if(tag.getTag_zh().indexOf(pinyin) != -1){
									if(array == null){
										array = new ArrayList<Category.Tag>();
									}
									array.add(tag);
								}
							}
							if(array !=null){
								result.put(cate, array);
							}
						}
						listener.success(result);
					}
					

				}
			});
		}

	}

}
