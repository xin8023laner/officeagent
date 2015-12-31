package com.gt.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.catcry.softview.CharacterParser;
import com.catcry.softview.ClearEditText;
import com.catcry.softview.PinyinComparator;
import com.catcry.softview.SideBar;
import com.catcry.softview.SideBar.OnTouchingLetterChangedListener;
import com.catcry.softview.SortModel;
import com.common.adapter.CarrierSoftAdapt;
import com.common.db.CarrierDBManager;
import com.common.model.AgentCarrier;
import com.common.model.IntentExtra;
import com.gt.inteface.OnViewClickListener;
import com.gt.officeagent.R;

public class CarrierBookActivity extends Activity implements OnViewClickListener,OnClickListener{

	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private CarrierSoftAdapt adapter;
	private ClearEditText mClearEditText;
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	private PinyinComparator pinyinComparator;
	private int type=0;
 
	// 使用时，将如下list替换为你自己的list即可
	private List<AgentCarrier> carriers = new ArrayList<AgentCarrier>();

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.softlist_carrier_main);
		carriers=CarrierDBManager.getDBManager(this).getCarrierList();
		
		
		initViews();
		
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	private void initViews() {
		// 初始化处理，得到要排序的list

		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});

		SourceDateList = filledData(carriers);

		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new CarrierSoftAdapt(CarrierBookActivity.this, SourceDateList,type,this);
		sortListView.setAdapter(adapter);

		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		InputMethodManager imm = (InputMethodManager) CarrierBookActivity.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mClearEditText.getApplicationWindowToken(),
				0);
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	

	/**
	 * 
	 * @param date
	 * @return
	 */

	@SuppressLint("DefaultLocale")
	private List<SortModel> filledData(List<AgentCarrier> date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setCarrier(date.get(i));
			String pinyin = characterParser.getSelling(date.get(i)
					.getParentCarrierName());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getCarrier().getParentCarrierName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateValues(filterDateList);
	}

	@Override
	public void OnViewClicked(View v, int type, Object obj) {
		
		setResult(0, new Intent().putExtra("result", new IntentExtra(0,obj)));
		finish();
	}

	@Override
	public void doSomeThing(View v, int type, Object obj) {
		
	}

	@Override
	public void onClick(View arg0) {
		
	}

	
	


}
