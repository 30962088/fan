package com.yoka.fan;

import com.yoka.fan.network.Request;
import com.yoka.fan.network.Tickling;
import com.yoka.fan.utils.Utils;
import com.yoka.fan.wiget.LoadingPopup;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class TicklingActivity extends BaseActivity implements OnClickListener{

	private EditText adviceText;
	
	private EditText phoneText;
	
	private Context context;
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		context = this;
		setContentView(R.layout.tickling_layout);
		adviceText = (EditText) findViewById(R.id.advice);
		phoneText = (EditText) findViewById(R.id.phone);
		findViewById(R.id.save_btn).setOnClickListener(this);
	}
	
	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "有奖意见反馈";
	}
	
	private void onSave(){
		final String advice = adviceText.getText().toString();
		final String phone = phoneText.getText().toString();
		LoadingPopup.show(context);
		new AsyncTask<Void, Void, Request>() {

			@Override
			protected Request doInBackground(Void... params) {
				Tickling tickling = new Tickling(advice, phone);
				tickling.request();
				return tickling;
			}
			
			protected void onPostExecute(Request request) {
				if(request.getStatus() == Request.Status.SUCCESS){
					Utils.tip(context, "感谢你的意见反馈");
					finish();
				}
				LoadingPopup.hide(context);
			};
			
		}.execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save_btn:
			onSave();
			break;

		default:
			break;
		}
		
	}

}
