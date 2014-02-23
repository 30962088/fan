package com.yoka.fan;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.SharePopupWindow.Share;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CommentActivity extends BaseActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_layout);
		
		
		ListView gridView = (ListView) findViewById(R.id.listview);
		ListViewAdapter adapter = new ListViewAdapter(new ArrayList<CommentActivity.Comment>(){{
			for(int i = 0;i<30;i++){
				Comment comment = new Comment();
				comment.comment = "呵呵，你好啊";
				comment.datetime = "3年前";
				comment.name = "威廉萌";
				comment.photo = "http://tp4.sinaimg.cn/2129028663/180/5684393877/1";
				add(comment);
			}
		}},this);
		final EditText commentView = (EditText) findViewById(R.id.comment);
		final View publishView = findViewById(R.id.publish);
        gridView.setAdapter(adapter);
        findViewById(R.id.comment_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(commentView.getWindowToken(), 0);
				
			}
		});
        gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
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
	
	private static class Comment{
		public String photo;
		public String name;
		public String datetime;
		public String comment;
		
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
	
}
