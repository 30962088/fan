package com.yoka.fan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.network.CommentList;
import com.yoka.fan.network.CommentList.Result;
import com.yoka.fan.network.CreateComment;
import com.yoka.fan.network.Request;
import com.yoka.fan.utils.RelativeDateFormat;
import com.yoka.fan.utils.User;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.BaseListView;
import com.yoka.fan.wiget.LoadingPopup;
import com.yoka.fan.wiget.BaseListView.OnLoadListener;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;

import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentActivity extends BaseActivity implements OnClickListener,OnLoadListener{

	public static final String PARAM_COLL_ID = "PARAM_COLL_ID";
	
	private String collId;
	
	
	private int limit = 20;
	
	private User user;

	
	private ListViewAdapter adapter;
	
	private List<Comment> list;
	
	private BaseListView listView;

	
	private EditText commentView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user = User.readUser();
		if(user == null){
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			return;
		}
		collId = getIntent().getStringExtra(PARAM_COLL_ID);
		setContentView(R.layout.comment_layout);
		
		
		listView = (BaseListView) findViewById(R.id.listview);
		listView.setOnLoadListener(this);
		list = new ArrayList<CommentActivity.Comment>();
		adapter = new ListViewAdapter(list, this);
		commentView = (EditText) findViewById(R.id.comment);
		final View publishView = findViewById(R.id.publish);
		publishView.setOnClickListener(this);
		listView.setAdapter(adapter);
		listView.setLimit(limit);
        findViewById(R.id.comment_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(commentView.getWindowToken(), 0);
				
			}
		});
        
        commentView.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length()>0){
					publishView.setEnabled(true);
				}else{
					publishView.setEnabled(false);
				}
				
			}
        	
        });
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}
	
	public static class Comment{
		private String photo;
		private String name;
		private String datetime;
		private String comment;
		public Comment(String photo, String name, String datetime,
				String comment) {
			super();
			this.photo = photo;
			this.name = name;
			this.datetime = datetime;
			this.comment = comment;
		}
		public String getPhoto() {
			return photo;
		}
		public String getName() {
			return name;
		}
		public String getDatetime() {
			return datetime;
		}
		public String getComment() {
			return comment;
		}
		
		
		
	}
	
	private static class ListViewAdapter extends BaseAdapter{

		private List<Comment> list;
		
		private Context context;
		
		private ImageLoader imageLoader;
		
		public ListViewAdapter(List<Comment> list, Context context) {
			super();
			this.list = list;
			this.context = context;
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
			Comment comment = list.get(position);
			
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.comment_item,null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			imageLoader.displayImage(comment.photo, holder.photoView);
			holder.datetimeView.setText(comment.datetime);
			holder.nameView.setText(comment.name);
			holder.commentView.setText(comment.comment);
			return convertView;
		}
		
		private static class ViewHolder{
			
			public ImageView photoView;
			
			public TextView nameView;
			
			public TextView datetimeView;
			
			public TextView commentView;
			
			public ViewHolder(View view) {
				photoView = (ImageView) view.findViewById(R.id.photo);
				nameView = (TextView) view.findViewById(R.id.name);
				datetimeView = (TextView) view.findViewById(R.id.datetime);
				commentView = (TextView) view.findViewById(R.id.comment);
			}	
		}
		
	}

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "评论";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.publish:
			onPublish();
			break;

		default:
			break;
		}
		
	}

	private void onPublish() {
		final String content = commentView.getText().toString();
		LoadingPopup.show(this);
		new AsyncTask<Void, Void, CreateComment>(){

			@Override
			protected CreateComment doInBackground(
					Void... params) {
				CreateComment request = new CreateComment(user.id, user.access_token, collId, content);
				request.request();
				return request;
			}
			
			protected void onPostExecute(CreateComment result) {
				if(result.getStatus() == Request.Status.SUCCESS){
					onPublishSuccess(result.getResult());
				}
				LoadingPopup.hide(CommentActivity.this);
			};
			
		}.execute();
		
	}
	
	private void onPublishSuccess(CreateComment.Result result){
		User user = User.readUser();
		list.add(0, new Comment(user.photo, user.nickname, RelativeDateFormat.format(new Date(result.getCreate_date())) , result.getTxt()));
		adapter.notifyDataSetChanged();
		commentView.setText("");
	}

	@Override
	public boolean onLoad(int offset, int limit) {
		CommentList request = new CommentList(user.id, user.access_token, collId, offset, limit);
		request.request();
		List<CommentList.Result> results = request.getResults();
		if(results == null){
			results = new ArrayList<CommentList.Result>();
		}
		if(offset == 0){
			list.clear();
		}
		for(Result result : results){
			list.add(result.toComment());
		}
		return results.size()>=limit?true:false;
	}

	@Override
	public void onLoadSuccess() {
		adapter.notifyDataSetChanged();
		
	}
	
}
