package com.yoka.fan.wiget;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.R;
import com.yoka.fan.utils.DisplayUtils;
import com.yoka.fan.utils.Utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
		
		holder.mLinkedView.load(model.getLinkModel());
		imageLoader.displayImage(model.getPhoto(), holder.mPhotoView);
		holder.mNameView.setText(model.getName());
		holder.mDatetimeView.setText(model.getDatetime());
		holder.mStarCount.setText(""+model.getStar());
		holder.mCommentCount.setText(""+model.getComment());
		holder.setTags(context, model.getTags());
		final View mMoreBtn = holder.mMoreBtn;
		holder.mMoreBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup(context,"的撒旦撒旦撒旦撒旦撒的撒旦",mMoreBtn);
				
			}
		});
		
		return convertView;
	}
	
	private static class ViewHolder{
		
		private ImageView mPhotoView;
		
		private TextView mNameView;
		
		private TextView mDatetimeView;
		
		private TextView mStarCount;
		
		private TextView mCommentCount;
		
		private ViewGroup mTagContainer;
		
		private LinkedView mLinkedView;
		
		private View mMoreBtn;
		
		public ViewHolder(View view) {
			mPhotoView = (ImageView) view.findViewById(R.id.photo);
			mNameView = (TextView) view.findViewById(R.id.name);
			mDatetimeView = (TextView) view.findViewById(R.id.datetime);
			mStarCount = (TextView) view.findViewById(R.id.starCount);
			mCommentCount = (TextView) view.findViewById(R.id.commentCount);
			mTagContainer = (ViewGroup) view.findViewById(R.id.tagList);
			mLinkedView = (LinkedView) view.findViewById(R.id.linked);
			mMoreBtn = view.findViewById(R.id.more);
		}
		
		public void setTags(Context context,List<String> tags){
			mTagContainer.removeAllViews();
			for(String tag:tags){
				TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.tag_view,null);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,DisplayUtils.Dp2Px(context, 30));
				params.setMargins(0, 0, DisplayUtils.Dp2Px(context, 3), 0);
				textView.setLayoutParams(params);
				textView.setText(tag);
				mTagContainer.addView(textView);
			}
			
		}
		
	}
	

	
	private static void popup(Context context,String text,View anchor){
		View popupView = LayoutInflater.from(context).inflate(R.layout.popup_intro_layout,null);
		final PopupWindow mPopupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        popupView.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
        popupView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				
			}
		});
        popupView.findViewById(R.id.popup).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
        
        mPopupWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0);
        
	}
	
	

}
