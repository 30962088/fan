package com.yoka.fan.wiget;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.R;
import com.yoka.fan.utils.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonListAdapter extends BaseAdapter{
	
	private Context context;
	
	private List<CommonListModel> list;
	
	private ImageLoader imageLoader;
	
	public CommonListAdapter(Context context, List<CommonListModel> list) {
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
		CommonListModel model = list.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_layout,null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		imageLoader.displayImage(model.getPhoto(), holder.mPhotoView);
		imageLoader.displayImage(model.getImg(), holder.mImageView);
		holder.mNameView.setText(model.getName());
		holder.mDatetimeView.setText(model.getDatetime());
		holder.mStarCount.setText(""+model.getStar());
		holder.mCommentCount.setText(""+model.getComment());
		holder.setTags(context, model.getTags());
		
		return convertView;
	}
	
	private static class ViewHolder{
		
		private ImageView mPhotoView;
		
		private TextView mNameView;
		
		private TextView mDatetimeView;
		
		private TextView mStarCount;
		
		private TextView mCommentCount;
		
		private ViewGroup mTagContainer;
		
		private ImageView mImageView;
		
		public ViewHolder(View view) {
			mPhotoView = (ImageView) view.findViewById(R.id.photo);
			mNameView = (TextView) view.findViewById(R.id.name);
			mDatetimeView = (TextView) view.findViewById(R.id.datetime);
			mStarCount = (TextView) view.findViewById(R.id.starCount);
			mCommentCount = (TextView) view.findViewById(R.id.commentCount);
			mTagContainer = (ViewGroup) view.findViewById(R.id.tagList);
			mImageView = (ImageView) view.findViewById(R.id.img);
		}
		
		public void setTags(Context context,List<String> tags){
			mTagContainer.removeAllViews();
			for(String tag:tags){
				mTagContainer.addView(new TagView(context, tag));
			}
			
		}
		
	}
	
	

}
