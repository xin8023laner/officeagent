package com.common.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.catcry.softview.SortModel;
import com.common.base.BaseApplication;
import com.common.utils.Constant;
import com.gt.inteface.OnViewClickListener;
import com.gt.officeagent.R;

public class CarrierSoftAdapt extends BaseAdapter implements SectionIndexer {
	// 填充数据的list
	private List<SortModel> list = null;
	private Context context;
	public int selectItem = -1;
	// 用来导入布局
	private LayoutInflater inflater = null;
	private OnViewClickListener mcallback = null;

	// 构造器
	public CarrierSoftAdapt(Context context, List<SortModel> list, int type,
			OnViewClickListener mcallback) {
		this.list = list;
		this.context = context;
		this.mcallback = mcallback;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final SortModel mContent = list.get(position);
		if (convertView == null) {

			// 获得ViewHolder对象
			holder = new ViewHolder();
			// 导入布局并赋值给convertview
			convertView = inflater
					.inflate(R.layout.softlist_item_carrier, null);
			holder.logo = (ImageView) convertView
					.findViewById(R.id.carrier_logo_iv);
			holder.name = (TextView) convertView
					.findViewById(R.id.carrier_name_tv);
			holder.phone = (TextView) convertView
					.findViewById(R.id.carrier_phone_tv);
			holder.ll_softlist = (LinearLayout) convertView
					.findViewById(R.id.ll_softlist);
			holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			// 为view设置标签
			convertView.setTag(holder);
		} else {
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}

		
		int section = getSectionForPosition(position);

		if (position == getPositionForSection(section)) {
			holder.tvLetter.setVisibility(View.VISIBLE);
			holder.tvLetter.setText(mContent.getSortLetters());
		} else {
			holder.tvLetter.setVisibility(View.GONE);
		}

		holder.name.setText(mContent.getCarrier().getParentCarrierName());

		BaseApplication.baseApplication.getBitmapUtils()
				.display(
						holder.logo,
						Constant.CARRIERLOGO_SERVER
								+ mContent.getCarrier().getParentCarrierCode()
								+ ".png");

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				mcallback.OnViewClicked(arg0, 0, mContent.getCarrier());

			}
		});

		return convertView;
	}

	public void updateValues(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void setSelectItem(int position) {
		this.selectItem = position;
		notifyDataSetChanged();
	}

	public class ViewHolder {
		public ImageView logo = null;
		public TextView name = null;
		public TextView distance = null;
		public TextView url = null;
		public TextView phone = null;
		public TextView tvLetter = null;
		public LinearLayout ll_softlist = null;

	}

	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	@SuppressLint("DefaultLocale")
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	@SuppressWarnings("unused")
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}