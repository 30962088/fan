package com.yoka.fan.wiget;

import java.util.List;

import com.yoka.fan.LoginActivity;
import com.yoka.fan.R;
import com.yoka.fan.network.CollFollow;
import com.yoka.fan.network.ListItemData;
import com.yoka.fan.utils.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
public class CollFollowListFragment extends Fragment implements OnClickListener{
	
	private User user;
	
	private View loginView;
	
	private ViewGroup contentView;
	
	private MyListView listView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user = User.readUser();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.coll_follow_layout, null);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		loginView = view.findViewById(R.id.login);
		loginView.setOnClickListener(this);
		contentView = (ViewGroup) view.findViewById(R.id.content);
		if(user != null){
			
			showContent();
		}else{
			loginView.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(this.user == null){
			User user = User.readUser();
			if(user != null){
				this.user = user;
				showContent();
			}
		}
		
	}
	
	private void showContent(){
		loginView.setVisibility(View.GONE);
		if(listView == null){
			listView = new MyListView(getActivity());
			contentView.addView(listView);
		}
	}

	
	
	
	private class MyListView extends CommonListView{

		public MyListView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected List<ListItemData> load(int offset, int limit) {
			CollFollow request = new CollFollow( offset, limit,user.access_token,user.id);
			request.request();
			return request.getListData();
		}

		@Override
		public String getEmptyTip() {
			// TODO Auto-generated method stub
			return "没有任何搭配";
		}
		
	}




	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;

		default:
			break;
		}
		
	}
	
}
