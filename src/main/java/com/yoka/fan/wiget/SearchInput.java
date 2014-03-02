package com.yoka.fan.wiget;

import com.yoka.fan.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;

public class SearchInput extends FrameLayout implements TextWatcher,OnClickListener{

	private EditText searchInput;
	
	private View clearBtn;
	
	private String hint;
	
	
	public SearchInput(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchInput, defStyle, 0);
		hint = a.getString(R.styleable.SearchInput_search_hint);
		a.recycle();
		init();
	}
	
	

	

	public SearchInput(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}





	public SearchInput(Context context) {
		super(context);
		init();
	}





	private void init(){
		LayoutInflater.from(getContext()).inflate(R.layout.search_input, this);
		searchInput = (EditText) findViewById(R.id.input);
		searchInput.setHint(hint);
		searchInput.addTextChangedListener(this);
		clearBtn = findViewById(R.id.clear);
		clearBtn.setOnClickListener(this);
	}
	
	public void addTextChangedListener(TextWatcher textWatcher){
		searchInput.addTextChangedListener(textWatcher);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		 if(s.toString().length() > 0 ){
			 clearBtn.setVisibility(View.VISIBLE);
		 }else{
			 clearBtn.setVisibility(View.GONE);
		 }
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clear:
			searchInput.setText("");
			break;

		default:
			break;
		}
		
	}
	
	public EditText getSearchInput() {
		return searchInput;
	}
	
	
}
