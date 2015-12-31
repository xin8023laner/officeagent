package com.catcry.softview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.gt.officeagent.R;
/**
 * 组合控件ClearEditText，如果你需要额外的方法，自己封装吧
 * @author zhangruntao
 *
 */
public class ClearEditTextStrong extends LinearLayout implements OnClickListener,TextWatcher{
	
	private EditText mEditText;
	private ImageButton mImageButton;
	
	public ClearEditTextStrong(Context context) {
		super(context);
		init(context);
	}

	public ClearEditTextStrong(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.ClearEditTextStrong);
		float textSize = array.getDimension(R.styleable.ClearEditTextStrong_clearTextSize, 16);
		String hint = array.getString(R.styleable.ClearEditTextStrong_clearHint);
		mEditText.setTextSize(textSize);
		mEditText.setHint(hint);
		array.recycle();
	}
	
	private void init(Context context){
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		setBackgroundResource(R.drawable.bj_search);
		mEditText = new EditText(context);
		mImageButton = new ImageButton(context);
		
		LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1f);
		LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		
		mEditText.setLayoutParams(editParams);
		mEditText.setSingleLine(true);
		mEditText.setBackgroundResource(0);
		Drawable drawable= getResources().getDrawable(R.drawable.softlist_search_bar_icon_normal);  
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
		mEditText.setCompoundDrawables(drawable,null,null,null);  
		mEditText.addTextChangedListener(this);
		
		btnParams.rightMargin = 10;
		mImageButton.setLayoutParams(btnParams);
		mImageButton.setBackgroundResource(R.drawable.softlist_btn_emotionstore_progresscancel);
		mImageButton.setVisibility(View.GONE);
		mImageButton.setOnClickListener(this);
		
		addView(mEditText);
		addView(mImageButton);
	}

	@Override
	public void onClick(View v) {
		mEditText.setText(null);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(s.length() > 0){
			mImageButton.setVisibility(View.VISIBLE);
		}else{
			mImageButton.setVisibility(View.GONE);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}
	
	public void setOnTextChangedListener(TextWatcher textWatcher){
		mEditText.addTextChangedListener(textWatcher);
	}
	
	public void setText(String text){
		mEditText.setText(text);
	}

}
