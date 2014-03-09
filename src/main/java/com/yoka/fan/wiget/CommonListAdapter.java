package com.yoka.fan.wiget;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.CommentActivity;
import com.yoka.fan.LoginActivity;
import com.yoka.fan.R;
import com.yoka.fan.TagActivity;
import com.yoka.fan.ZoneActivity;
import com.yoka.fan.network.Like;
import com.yoka.fan.network.Request;
import com.yoka.fan.network.Request.Status;
import com.yoka.fan.network.UnLike;
import com.yoka.fan.utils.DisplayUtils;
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

public class CommonListAdapter extends BaseAdapter implements
		onTagClickListener {

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
		final CommonListModel model = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_layout, null);
			holder = new ViewHolder(convertView);
			WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			int width = display.getWidth() - DisplayUtils.Dp2Px(context, 95); // deprecated
			int height = width / 3 * 4;

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					width, height);

			holder.mLinkedView.setLayoutParams(layoutParams);
			holder.mLinkedView.setOnTagClickListener(this);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mPhotoView.setImageBitmap(null);

		if (model.isShowLinked()) {
			holder.mLinkedView.load(model.getLinkModel());
		} else {
			LinkModel linkModel = model.getLinkModel();
			holder.mLinkedView.load(new LinkModel(linkModel.getUrl(), linkModel
					.getWidth(), linkModel.getHeight(), null));
		}

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
		}

		holder.mDatetimeView.setText(model.getDatetime());
		holder.mStarCount.setText("" + model.getStar());
		holder.mCommentCount.setText("" + model.getComment());
		holder.setTags(context, model.getTags());
		holder.mStarBtn.setSelected(model.isStared());
		holder.mPhotoView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ZoneActivity.class);
				intent.putExtra(ZoneActivity.PARAM_TARGET_ID,
						"" + model.getUser_id());
				intent.putExtra(ZoneActivity.PARAM_NAME, "" + model.getName());
				// intent.putExtra(ZoneActivity.PARAM_TARGET_ID,
				// ""+User.readUser().id);
				// intent.putExtra(ZoneActivity.PARAM_NAME,
				// User.readUser().nickname);
				context.startActivity(intent);

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
				if (!model.isShowLinked()) {
					model.setShowLinked(true);
					mLinkedView.load(model.getLinkModel());
				}

			}
		});
		final TextView mStarCount = holder.mStarCount;
		final View mStarBtn = holder.mStarBtn;
		holder.mStarBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				User user = User.readUser();
				if (user == null) {
					context.startActivity(new Intent(context,
							LoginActivity.class));
					return;
				}
				final boolean isStared = !model.isStared();
				model.setStared(isStared);
				int count = model.getStar();
				Request request = null;
				if (isStared) {
					count++;
					request = new Like(user.id, user.access_token, model
							.getId());
				} else {
					count--;
					request = new UnLike(user.id, user.access_token, model
							.getId());
				}
				model.setStar(count);
				mStarBtn.setSelected(model.isStared());
				mStarCount.setText("" + model.getStar());
				// notifyDataSetChanged();

				final Request req = request;

				new AsyncTask<String, Void, Status>() {

					@Override
					protected Request.Status doInBackground(String... params) {

						req.request();
						return req.getStatus();
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
		});

		holder.mShareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new SharePopupWindow(context,
						new ArrayList<SharePopupWindow.Share>() {
							{
								add(new Share(
										context.getString(R.string.weibo),
										R.drawable.share_weibo, null));
								add(new Share(context
										.getString(R.string.tencent),
										R.drawable.share_tencent, null));
								add(new Share(context
										.getString(R.string.renren),
										R.drawable.share_renren, null));
								add(new Share(context
										.getString(R.string.wechat),
										R.drawable.share_wechat, null));
								add(new Share(context
										.getString(R.string.timeline),
										R.drawable.share_timeline, null));
							}
						});

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
		}

		public void setTags(final Context context, List<String> tags) {
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

	@Override
	public void onClose(Link link) {

	}

	@Override
	public void onClick(Link link) {

		new BuyPopupWindow(context, new ArrayList<BuyPopupWindow.GoodsItem>() {
			{
				for (int i = 0; i < 5; i++) {
					GoodsItem item = new GoodsItem();
					item.img = "http://image.iask.sina.com.cn/cidian/21/90/13715390212009-07-151006466.jpg";
					item.name = "goods";
					item.price = 323.11f;
					item.typeResId = R.drawable.blazers;
					add(item);
				}
				GoodsItem item1 = new GoodsItem();
				item1.title = "编辑推荐";
				add(item1);
				for (int i = 0; i < 10; i++) {
					GoodsItem item = new GoodsItem();
					item.img = "http://image.iask.sina.com.cn/cidian/21/90/13715390212009-07-151006466.jpg";
					item.name = "goods";
					item.price = 323.11f;
					item.typeResId = R.drawable.blazers;
					add(item);
				}

			}
		});
	}

}
