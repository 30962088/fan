package com.yoka.fan.wiget;

import java.net.Authenticator.RequestorType;
import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.CommentActivity;
import com.yoka.fan.LoginActivity;
import com.yoka.fan.R;
import com.yoka.fan.TagActivity;
import com.yoka.fan.ZoneActivity;
import com.yoka.fan.SelectCategoryActivity.Model;
import com.yoka.fan.network.Accuse;
import com.yoka.fan.network.Like;
import com.yoka.fan.network.Request;
import com.yoka.fan.network.Request.Status;
import com.yoka.fan.network.UnLike;
import com.yoka.fan.utils.DisplayUtils;
import com.yoka.fan.utils.ShareUtils.Weibo;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.BuyPopupWindow.GoodsItem;
import com.yoka.fan.wiget.LinkModel.Link;
import com.yoka.fan.wiget.LinkedView.onImageClickListener;
import com.yoka.fan.wiget.LinkedView.onTagClickListener;
import com.yoka.fan.wiget.SharePopupWindow.Share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CommonListAdapter extends BaseAdapter  {

	private Context context;

	private List<CommonListModel> list;

	private ImageLoader imageLoader;
	
	private Animation zoomAnim;

	public CommonListAdapter(Context context, List<CommonListModel> list) {
		this.zoomAnim = AnimationUtils.loadAnimation(context, R.anim.zoom_center_closure);
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

	private void openZoneActivity(CommonListModel model){
		Intent intent = new Intent(context, ZoneActivity.class);
		intent.putExtra(ZoneActivity.PARAM_TARGET_ID,
				"" + model.getUser_id());
		intent.putExtra(ZoneActivity.PARAM_NAME, "" + model.getName());
		context.startActivity(intent);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommonListModel model = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_layout, null);
			holder = new ViewHolder(convertView);
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			int width = display.getWidth() - DisplayUtils.Dp2Px(context, 90); // deprecated
			int height = width / 3 * 4;

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					width, height);

			holder.mLinkedView.setLayoutParams(layoutParams);
			holder.mLinkedView.setOnTagClickListener(new onTagClickListener() {
				
				@Override
				public void onClose(Link link) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onClick(Link link) {
					new BuyPopupWindow(context, model.getId());
					
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mPhotoView.setImageBitmap(null);

		holder.mLinkedView.load(model.getLinkModel());

		if (model.getPhoto() == null) {
			holder.mPhotoView.setVisibility(View.GONE);
		} else {
			holder.mPhotoView.setVisibility(View.VISIBLE);
			imageLoader.displayImage(model.getPhoto(), holder.mPhotoView);
		}

		if (model.getName() == null) {
			holder.mNameView.setVisibility(View.GONE);
		} else {
			holder.mNameView.setVisibility(View.VISIBLE);
			holder.mNameView.setText(model.getName());
			holder.mNameView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openZoneActivity(model);
					
				}
			});
		}
		
		holder.mJubaoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ConfirmDialog.open(context, "举报", "举报本条搭配？", new ConfirmDialog.OnClickListener() {
					
					@Override
					public void onPositiveClick() {
						
						new AsyncTask<Void, Void, Accuse>(){

							@Override
							protected Accuse doInBackground(Void... params) {
								User user = User.readUser();
								Accuse accuse = null;
								if(user == null){
									accuse = new Accuse("", "", model.getId());
								}else{
									accuse = new Accuse(user.id, user.access_token, model.getId());
								}
								accuse.request();
								return accuse;
							}
							
							protected void onPostExecute(Accuse result) {
								if(result.getStatus() == Request.Status.SUCCESS){
									Utils.tip(context, "举报成功");
								}
							};
							
						}.execute();
						
						
					}
					
					@Override
					public void onNegativeClick() {
						// TODO Auto-generated method stub
						
					}
				});
				
			}
		});

		holder.mDatetimeView.setText(model.getDatetime());
		holder.mStarCount.setText("" + model.getStar());
		holder.mCommentCount.setText("" + model.getComment());
		holder.setTags(context, model.getTags(),model.getMetaAttr().get("风格"));
		holder.mStarBtn.setSelected(model.isStared());
		holder.mPhotoView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openZoneActivity(model);

			}
		});
		holder.mCommentBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CommentActivity.class);
				intent.putExtra(CommentActivity.PARAM_COLL_ID, model.getId());
				context.startActivity(intent);

			}
		});

		final View mMoreBtn = holder.mMoreBtn;
		holder.mMoreBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popup(context, model.getDescr(), mMoreBtn);

			}
		});
		final LinkedView mLinkedView = holder.mLinkedView;
		holder.mLinkedView.setOnImageClickListener(new onImageClickListener() {

			@Override
			public void onClick(float left, float top) {
				LinkModel linkModel = model.getLinkModel();
				boolean isshow = !linkModel.isShowLink();
				linkModel.setShowLink(isshow);
				mLinkedView.load(linkModel);

			}
		});
		final TextView mStarCount = holder.mStarCount;
		final View mStarBtn = holder.mStarBtn;
		holder.mStarBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				final boolean isStared = !model.isStared();
				model.setStared(isStared);
				int count = model.getStar();
				
				if (isStared) {
					count++;
					
				} else {
					count--;
					
				}
				model.setStar(count);
				
				mStarBtn.setSelected(model.isStared());
				mStarCount.setText("" + model.getStar());
				mStarCount.startAnimation(zoomAnim);
				// notifyDataSetChanged();

				final User user = User.readUser();

				if(user != null){
					new AsyncTask<String, Void, Status>() {

						@Override
						protected Request.Status doInBackground(String... params) {
							Request request = null;
							if(isStared){
								request = new Like(user.id, user.access_token, model
										.getId());
							}else{
								request = new UnLike(user.id, user.access_token, model
										.getId());
							}
							request.request();
							return request.getStatus();
						}

						protected void onPostExecute(Request.Status result) {
							if (result == Request.Status.ERROR) {
								int count = model.getStar();
								if (isStared) {
									count--;
								} else {
									count++;
								}
								model.setStar(count);
								model.setStared(!isStared);
								mStarBtn.setSelected(model.isStared());
								mStarCount.setText("" + model.getStar());
								// notifyDataSetChanged();
							}
						};
					}.execute(model.getId());
				}
				
				

			}
		});

		holder.mShareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new SharePopupWindow(context,model);
			}
		});

		return convertView;
	}
	
	

	private static class ViewHolder {

		private ImageView mPhotoView;

		private TextView mNameView;

		private TextView mDatetimeView;

		private TextView mStarCount;

		private View mStarBtn;

		private TextView mCommentCount;

		private View mCommentBtn;

		private ViewGroup mTagContainer;

		private LinkedView mLinkedView;

		private View mMoreBtn;

		private View mShareBtn;
		
		private View mJubaoBtn;

		public ViewHolder(View view) {
			mPhotoView = (ImageView) view.findViewById(R.id.photo);
			mNameView = (TextView) view.findViewById(R.id.name);
			mDatetimeView = (TextView) view.findViewById(R.id.datetime);
			mStarCount = (TextView) view.findViewById(R.id.starCount);
			mCommentCount = (TextView) view.findViewById(R.id.commentCount);
			mTagContainer = (ViewGroup) view.findViewById(R.id.tagList);
			mLinkedView = (LinkedView) view.findViewById(R.id.linked);
			mMoreBtn = view.findViewById(R.id.more);
			mStarBtn = view.findViewById(R.id.star);
			mShareBtn = view.findViewById(R.id.share);
			mCommentBtn = view.findViewById(R.id.comment);
			mJubaoBtn = view.findViewById(R.id.jubao);
		}

		public void setTags(final Context context, List<String> tags,final String style) {
			mTagContainer.removeAllViews();
			for (final String tag : tags) {
				TextView textView = (TextView) LayoutInflater.from(context)
						.inflate(R.layout.tag_view, null);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, DisplayUtils.Dp2Px(context,
								30));
				params.setMargins(0, 0, DisplayUtils.Dp2Px(context, 3), 0);
				textView.setLayoutParams(params);
				textView.setText(tag);
				textView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, TagActivity.class);
						intent.putExtra(TagActivity.PARAM_TAG, tag);
						intent.putExtra(TagActivity.PARAM_STYLE, style);
						context.startActivity(intent);

					}
				});
				mTagContainer.addView(textView);
			}

		}

	}

	private static void popup(Context context, String text, View anchor) {
		View popupView = LayoutInflater.from(context).inflate(
				R.layout.popup_intro_layout, null);

		Animation zoom = AnimationUtils.loadAnimation(context,
				R.anim.zoom_center);
		popupView.findViewById(R.id.popup).startAnimation(zoom);
		((TextView) popupView.findViewById(R.id.text)).setText(text);
		final PopupWindow mPopupWindow = new PopupWindow(popupView,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#4D000000")));
		popupView.findViewById(R.id.close).setOnClickListener(
				new OnClickListener() {

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
		popupView.findViewById(R.id.popup).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				});

		mPopupWindow.showAtLocation(anchor, Gravity.BOTTOM, 0, 0);

	}

	

}
