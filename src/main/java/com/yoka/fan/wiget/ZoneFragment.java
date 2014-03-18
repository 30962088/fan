package com.yoka.fan.wiget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.FansListActivity;
import com.yoka.fan.LoginActivity;
import com.yoka.fan.MainActivity;
import com.yoka.fan.R;
import com.yoka.fan.ZoneActivity;
import com.yoka.fan.network.Follow;
import com.yoka.fan.network.Info;
import com.yoka.fan.network.Request;
import com.yoka.fan.network.UnFollow;
import com.yoka.fan.network.Info.Result;
import com.yoka.fan.utils.Relation;
import com.yoka.fan.utils.Relation.OperatorListener;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.CommonListView.OnVerticalScrollListener;
import com.yoka.fan.wiget.CommonPagerAdapter.Page;
import com.yoka.fan.wiget.TabPageIndicator.OnTabClickLisenter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ZoneFragment extends Fragment implements OnClickListener,
		OnVerticalScrollListener {

	private TextView matchView;

	private TextView focusView;

	private TextView fansView;

	private ImageView photoView;

	private TextView followBtn;

	private String target_id;

	private ImageLoader imageLoader;

	private String name;

	private User user;

	private TabPageIndicator indicator;

	private View headerContainer;

	private View zoneHeader;

	private View indicatorHeader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (User.readUser() == null) {
			startActivity(new Intent(getActivity(), LoginActivity.class));
			getActivity().finish();
		} else {
			target_id = getArguments().getString(ZoneActivity.PARAM_TARGET_ID);
			name = getArguments().getString(ZoneActivity.PARAM_NAME);
			imageLoader = Utils.getImageLoader(getActivity());
			user = User.readUser();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.zone_header, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		headerContainer = getActivity().findViewById(
				R.id.base_actionbar_content);
		headerContainer.setVisibility(View.VISIBLE);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		if (User.readUser() == null) {
			return;
		}
		indicatorHeader = view.findViewById(R.id.indicator_wrap);
		zoneHeader = view.findViewById(R.id.zone_header);
		matchView = (TextView) view.findViewById(R.id.match);
		focusView = (TextView) view.findViewById(R.id.focus);
		fansView = (TextView) view.findViewById(R.id.fans);
		photoView = (ImageView) view.findViewById(R.id.photo);
		followBtn = (TextView) view.findViewById(R.id.follow_btn);
		followBtn.setOnClickListener(this);
		if (name == null) {
			followBtn.setVisibility(View.GONE);
		} else {
			followBtn.setVisibility(View.VISIBLE);

		}
		view.findViewById(R.id.fans_btn).setOnClickListener(this);
		view.findViewById(R.id.follow_list).setOnClickListener(this);
		User user = User.readUser();
		Relation.findFollower(user, target_id, new OperatorListener<Boolean>() {

			@Override
			public void success(Boolean result) {
				if (result) {
					followBtn.setSelected(true);
					followBtn.setText("已关注");
				}

			}
		});
		initView();

	}

	private void initView() {
		final User user = User.readUser();

		new Thread(new Runnable() {

			@Override
			public void run() {
				Info info = new Info(user.access_token, user.id, target_id) {
					@Override
					public void onSuccess(Map<String, Result> map) {
						final Result result = map.get(target_id);
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								initUserView(result);

							}
						});
					}

				};
				info.request();
			}
		}).start();

	}

	private void initUserView(Result result) {
		matchView.setText("" + result.getShow_count());
		fansView.setText("" + result.getFollowers());
		focusView.setText("" + result.getFollows());
		photoView.setImageResource(R.drawable.photo_default);
		imageLoader.displayImage(result.getHead_url(), photoView);
		initPage(result.getId());
		getView().findViewById(R.id.content).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.loading).setVisibility(View.GONE);
	}

	private void initPage(String target_id) {

		View view = getView();
		final Bundle arguments = new Bundle();
		arguments.putString("target_id", target_id);
		arguments.putString("user_id", user.id);
		arguments.putString("access_token", user.access_token);
		List<CommonPagerAdapter.Page> pages = new ArrayList<CommonPagerAdapter.Page>() {
			{
				add(new Page(name == null ? "我的搭配" : "TA的搭配",
						new CollListFragment() {
							{
								setArguments(arguments);
								setOnVerticalScrollListener(ZoneFragment.this);
							}
						}, false));
				add(new Page(name == null ? "我的喜欢" : "TA的喜欢",
						new CollLikeListFragment() {
							{
								setArguments(arguments);
								setOnVerticalScrollListener(ZoneFragment.this);
							}
						}, false));
			}
		};
		CommonPagerAdapter adapter = new CommonPagerAdapter(
				getChildFragmentManager(), pages);

		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		pager.setAdapter(adapter);

		indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		indicator.setModel(pages);
		indicator.setViewPager(pager);

		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				Activity activity = getActivity();

			}

		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fans_btn:
			Intent intent = new Intent(getActivity(), FansListActivity.class);
			intent.putExtra(FansListActivity.PARAM_TARGET_ID, target_id);
			intent.putExtra(FansListActivity.PARAM_TYPE,
					FansListActivity.PARAM_FANS);
			startActivity(intent);
			break;
		case R.id.follow_list:
			Intent intent1 = new Intent(getActivity(), FansListActivity.class);
			intent1.putExtra(FansListActivity.PARAM_TARGET_ID, target_id);
			intent1.putExtra(FansListActivity.PARAM_TYPE,
					FansListActivity.PARAM_FOLLOWS);
			startActivity(intent1);
			break;
		case R.id.follow_btn:
			onFollowClick();
			break;
		default:
			break;
		}

	}

	private void onFollowClick() {

		followBtn.setSelected(!followBtn.isSelected());
		followBtn.setText(followBtn.isSelected() ? "已关注" : "关注");
		new AsyncTask<Void, Void, Request.Status>() {

			@Override
			protected com.yoka.fan.network.Request.Status doInBackground(
					Void... params) {
				Request request = null;
				if (followBtn.isSelected()) {
					request = new Follow(user.id, target_id, user.access_token);
				} else {
					request = new UnFollow(user.id, target_id,
							user.access_token);
				}
				request.request();
				return request.getStatus();
			}

			@Override
			protected void onPostExecute(
					com.yoka.fan.network.Request.Status result) {
				if (Request.Status.SUCCESS == result) {
					if (followBtn.isSelected()) {
						Relation.addFollow(user, target_id);
					} else {
						Relation.removeFollow(user, target_id);
					}
				} else {
					followBtn.setSelected(!followBtn.isSelected());
					followBtn.setText(followBtn.isSelected() ? "已关注" : "关注");
				}
			}
		}.execute();

	}

	@Override
	public void onScrollUp() {
		if (indicatorHeader.getVisibility() == View.GONE) {
			Utils.expand(indicatorHeader);
			Utils.expand(zoneHeader);
			// indicator.setVisibility(View.VISIBLE);
			// zoneHeader.setVisibility(View.VISIBLE);
			if (headerContainer != null) {
				Utils.expand(headerContainer);
				// headerContainer.setVisibility(View.VISIBLE);
			}
		}

	}

	@Override
	public void onScrollDown() {
		if (indicatorHeader.getVisibility() == View.VISIBLE) {

			Utils.collapse(indicatorHeader);
			Utils.collapse(zoneHeader);
			// indicator.setVisibility(View.VISIBLE);
			// zoneHeader.setVisibility(View.VISIBLE);
			if (headerContainer != null) {
				Utils.collapse(headerContainer);
				// headerContainer.setVisibility(View.VISIBLE);
			}
		}
	}

}
