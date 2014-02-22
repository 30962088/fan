package com.yoka.fan.wiget;

import java.util.List;
import java.util.zip.Inflater;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.R;
import com.yoka.fan.utils.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class BuyPopupWindow implements OnClickListener{

	
	
	private PopupWindow mPopupWindow;
	
	public BuyPopupWindow(Context context,final List<GoodsItem> list) {
		View view = LayoutInflater.from(context).inflate(R.layout.popup_buy_layout,null);
		view.setOnClickListener(this);
		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        ListView gridView = (ListView) view.findViewById(R.id.listview);
       
        ListViewAdapter adapter = new ListViewAdapter(context,list);
        gridView.setAdapter(adapter);
        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.zoom_center);
        
        View bottomBar = view.findViewById(R.id.popup);
        bottomBar.setOnClickListener(this);
        bottomBar.startAnimation(rotation);
        view.findViewById(R.id.cancel);
        view.findViewById(R.id.close).setOnClickListener(this);
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        
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
		public int typeResId;
		public String name;
		public float price;
		public String url;
		public String img;
		public String title;
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
				holder.typeView.setBackgroundResource(item.typeResId);
				holder.nameView.setText(item.name);
				holder.priceView.setText("¥"+item.price);
				imageLoader.displayImage(item.url, holder.imgView);
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
	
	

	
}
