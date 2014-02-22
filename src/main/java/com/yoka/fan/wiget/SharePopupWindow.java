package com.yoka.fan.wiget;

import java.util.List;
import java.util.zip.Inflater;

import com.yoka.fan.R;

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
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class SharePopupWindow implements OnClickListener{

	
	
	private PopupWindow mPopupWindow;
	
	public SharePopupWindow(Context context,final List<Share> shares) {
		View view = LayoutInflater.from(context).inflate(R.layout.share_layout,null);
		view.setOnClickListener(this);
		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4D000000")));
		mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Share share = shares.get(position);
				
				if(share.onClickListener != null){
					share.onClickListener.onClick(view);
				}
				
				
			}

			
		});
        GridViewAdapter adapter = new GridViewAdapter(shares, context);
        gridView.setAdapter(adapter);
        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_bottom);
        
        View bottomBar = view.findViewById(R.id.bottom_bar);
        bottomBar.setOnClickListener(this);
        bottomBar.startAnimation(rotation);
        view.findViewById(R.id.cancel);
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_layout:
		case R.id.bottom_bar:
			mPopupWindow.dismiss();
		default:
			break;
		}
		
	}
	
	
	
	public static class Share{
		public String name;
		public int img;
		public OnClickListener onClickListener;
		public Share(String name, int img, OnClickListener onClickListener) {
			super();
			this.name = name;
			this.img = img;
			this.onClickListener = onClickListener;
		}
		
	}
	
	private static class GridViewAdapter extends BaseAdapter{

		private List<Share> list;
		
		private Context context;
		
		public GridViewAdapter(List<Share> list, Context context) {
			super();
			this.list = list;
			this.context = context;
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
			Share share = list.get(position);
			
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.share_item,null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.imageView.setImageResource(share.img);
			holder.textView.setText(share.name);
			convertView.setOnClickListener(share.onClickListener);
			return convertView;
		}
		
		private static class ViewHolder{
			
			public ImageView imageView;
			
			public TextView textView;
			
			public ViewHolder(View view) {
				imageView = (ImageView) view.findViewById(R.id.img);
				textView = (TextView) view.findViewById(R.id.name);
			}
			
		}
		
	}

	
}
