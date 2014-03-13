package com.yoka.fan.wiget;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.R;
import com.yoka.fan.WebViewActivity;
import com.yoka.fan.network.CollDetail;
import com.yoka.fan.network.CollDetail.Goods;
import com.yoka.fan.network.CollDetail.Result;
import com.yoka.fan.utils.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class BuyPopupWindow implements OnClickListener,AnimationListener,OnItemClickListener{

	
	
	private PopupWindow mPopupWindow;
	
	private ListView listView;
	
	private View loadingView;
	
	private Context context;
	
	private String coll_id;
	
	private List<GoodsItem> list;
	
	public BuyPopupWindow(Context context,String coll_id) {
		View view = LayoutInflater.from(context).inflate(R.layout.popup_buy_layout,null);
		view.setOnClickListener(this);
		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4D000000")));
		mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        this.context = context;
        this.coll_id = coll_id;
        listView = (ListView) view.findViewById(R.id.listview);
        listView.setOnItemClickListener(this);
        loadingView = view.findViewById(R.id.loading);
        
        
        
        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_top);
        rotation.setAnimationListener(this);
        
        View bottomBar = view.findViewById(R.id.popup);
        bottomBar.setOnClickListener(this);
        bottomBar.startAnimation(rotation);
        view.findViewById(R.id.cancel);
        view.findViewById(R.id.close).setOnClickListener(this);
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        
        
	}
	
	private void load(final String coll_id){
		new AsyncTask<Void, Void, List<GoodsItem>>(){

			@Override
			protected List<GoodsItem> doInBackground(Void... params) {
				CollDetail request = new CollDetail(coll_id);
				request.request();
				Result result = request.getResult();
				List<GoodsItem> list = new ArrayList<BuyPopupWindow.GoodsItem>();
				if(result != null){
					
					for(Goods goods : result.getLinkGoods().values()){
						list.add(new GoodsItem(goods.getType_url(),goods.getBrand()+goods.getTags(),goods.getPrice(),goods.getUrl(),goods.getImg(),"去购买>",null));
					}
					if(result.getLinkGoodsType().size() > 0){
						list.add(new GoodsItem("编辑推荐"));
						for(Goods goods : result.getLinkGoodsType()){
							list.add(new GoodsItem(goods.getType_url(),goods.getBrand()+goods.getTags(),goods.getPrice(),goods.getUrl(),goods.getImg(),"相似推荐>",null));
						}
					}
				}
				
				
				return list;
			}
			
			protected void onPostExecute(java.util.List<GoodsItem> list) {
				onload(list);
			};
			
		}.execute();
	}
	
	private void onload(final List<GoodsItem> list){
		this.list = list;
		ListViewAdapter adapter = new ListViewAdapter(context,list);
        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popupWindow:
		case R.id.close:
		case R.id.bottom_bar:
			mPopupWindow.dismiss();
		default:
			break;
		}
		
	}
	
	public static class GoodsItem{
		private String typeResId;
		private String name;
		private double price;
		private String url;
		private String img;
		private String title;
		private String link;
		
		public GoodsItem(String title) {
			super();
			this.title = title;
		}
		public GoodsItem(String typeResId, String name, double price, String url,
				String img,String link, String title) {
			super();
			this.typeResId = typeResId;
			this.name = name;
			this.price = price;
			this.url = url;
			this.img = img;
			this.link = link;
			this.title = title;
		}
		
		
		
	}
	
	private static class ListViewAdapter extends BaseAdapter{

		private Context context;
		
		private List<GoodsItem> list;
		
		private ImageLoader imageLoader;
		
		public ListViewAdapter(Context context, List<GoodsItem> list) {
			this.context = context;
			this.list = list;
			imageLoader = Utils.getImageLoader(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GoodsItem item = list.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.popup_buy_item,null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(item.title != null){
				holder.goodsItemView.setVisibility(View.GONE);
				holder.titleView.setVisibility(View.VISIBLE);
				holder.titleView.setText(item.title);
			}else{
				holder.goodsItemView.setVisibility(View.VISIBLE);
				holder.titleView.setVisibility(View.GONE);
				imageLoader.displayImage(item.typeResId, holder.typeView);
				holder.nameView.setText(item.name);
				holder.jumpView.setText(item.link);
				holder.priceView.setText("¥"+item.price);
				if(item.img == null){
					holder.imgView.setVisibility(View.GONE);
				}else{
					holder.imgView.setVisibility(View.VISIBLE);
					imageLoader.displayImage(item.img, holder.imgView);
				}
				
			}
			
			
			return convertView;
		}
		
		private static class ViewHolder{
			
			public View goodsItemView;
			
			public TextView titleView;
			
			public ImageView typeView;
			
			public TextView nameView;
			
			public TextView priceView;
			
			public ImageView imgView;
			
			public TextView jumpView;
			
			public ViewHolder(View view) {
				goodsItemView = view.findViewById(R.id.goods_item);
				titleView = (TextView) view.findViewById(R.id.title);
				typeView = (ImageView) view.findViewById(R.id.type);
				nameView = (TextView) view.findViewById(R.id.name);
				priceView = (TextView) view.findViewById(R.id.price);
				imgView = (ImageView) view.findViewById(R.id.img);
				jumpView = (TextView) view.findViewById(R.id.jump);
			}
			
		}
		

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		load(coll_id);
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		GoodsItem item = list.get(position);
		if(item.url != null){
			WebViewActivity.open(context, item.url);
		}
		
	}
	
	

	
}
