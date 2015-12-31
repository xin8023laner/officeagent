package com.common.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cn.socks.autoload.Mode;
import cn.socks.autoload.OnSwipeLayoutListener;
import cn.socks.autoload.SimpleSwipeListener;
import cn.socks.autoload.ZSwipeItem;

import com.common.model.IntentExtra;
import com.common.utils.IntentUtils;

/**
 * 如果要使用ZSwipeItem侧滑删除，请继承这个Adapter
 *
 * @author zhaokaiqiang
 * @class: com.socks.zlistview.adapter.BaseSwipeAdapter
 * @date 2015-1-8 上午10:05:04
 */
public abstract class BasicSwipeAdapter<T> extends BaseAdapter {

	public static final String TAG = "BaseSwipeAdapter";

	public final int INVALID_POSITION = -1;
	/**
	 * 显示模式，默认单开
	 */
	private Mode mode = Mode.Single;
	/**
	 * 当前打开的item的position
	 */
	protected int openPosition = INVALID_POSITION;
	/**
	 * 当前打开的所有item的position
	 */
	protected Set<Integer> openPositions = new HashSet<Integer>();
	/**
	 * 当前打开的所有ZSwipeItem对象
	 */
	protected Set<ZSwipeItem> mShownLayouts = new HashSet<ZSwipeItem>();

	public abstract int getSwipeLayoutResourceId(int position);

	public abstract View generateView(int position, ViewGroup parent);

	public abstract void fillValues(int position, View convertView);

	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {

		/**
		 * 禁止复用
		 */
//		if (convertView == null) {
//			convertView = generateView(position, parent);
//			initialize(convertView, position);
//		} else {
//			updateConvertView(convertView, position);
//		}
		convertView = generateView(position, parent);
		initialize(convertView, position);
		fillValues(position, convertView);
		return convertView;

	}

	/**
	 * 初始化item布局调用
	 *
	 * @param target
	 * @param position
	 */
	public void initialize(View target, int position) {

		int resId = getSwipeLayoutResourceId(position);

		OnLayoutListener onLayoutListener = new OnLayoutListener(position);
		ZSwipeItem swipeLayout = (ZSwipeItem) target.findViewById(resId);
		if (swipeLayout == null){
//			throw new IllegalStateException(
//					"can not find SwipeLayout in target view");
		}else{
			SwipeMemory swipeMemory = new SwipeMemory(position);
			// 添加滑动监听器
			swipeLayout.addSwipeListener(swipeMemory);
			// 添加布局监听器
			swipeLayout.addOnLayoutListener(onLayoutListener);
			swipeLayout.setTag(resId, new ValueBox(position, swipeMemory,
					onLayoutListener));
			
			mShownLayouts.add(swipeLayout);
			
		}


	}

	/**
	 * 复用item布局的时候调用
	 *
	 * @param target
	 * @param position
	 */
	public void updateConvertView(View target, int position) {

		int resId = getSwipeLayoutResourceId(position);

		ZSwipeItem swipeLayout = (ZSwipeItem) target.findViewById(resId);
		if (swipeLayout == null){
//			throw new IllegalStateException(
//					"can not find SwipeLayout in target view");
		}else{
			ValueBox valueBox = (ValueBox) swipeLayout.getTag(resId);
			valueBox.swipeMemory.setPosition(position);
			valueBox.onLayoutListener.setPosition(position);
			valueBox.position = position;
		}

	}

	private void closeAllExcept(ZSwipeItem layout) {

		for (ZSwipeItem s : mShownLayouts) {
			if (s != layout)
				s.close();
		}
	}

	/**
	 * 获取打开的所有的item的position信息
	 *
	 * @return
	 */
	public List<Integer> getOpenItems() {

		if (mode == Mode.Multiple) {
			return new ArrayList<Integer>(openPositions);
		} else {
			return Arrays.asList(openPosition);
		}
	}

	/**
	 * position位置的item是否打开
	 *
	 * @param position
	 * @return
	 */
	public boolean isOpen(int position) {
		if (mode == Mode.Multiple) {
			return openPositions.contains(position);
		} else {
			return openPosition == position;
		}
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		openPositions.clear();
		mShownLayouts.clear();
		openPosition = INVALID_POSITION;
	}

	class ValueBox {
		OnLayoutListener onLayoutListener;
		SwipeMemory swipeMemory;
		int position;

		ValueBox(int position, SwipeMemory swipeMemory,
		         OnLayoutListener onLayoutListener) {
			this.swipeMemory = swipeMemory;
			this.onLayoutListener = onLayoutListener;
			this.position = position;
		}
	}

	private class OnLayoutListener implements OnSwipeLayoutListener {

		private int position;

		OnLayoutListener(int position) {
			this.position = position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		@Override
		public void onLayout(ZSwipeItem v) {

			if (isOpen(position)) {
				v.open(false, false);
			} else {
				v.close(false, false);
			}
		}
	}

	class SwipeMemory extends SimpleSwipeListener {

		private int position;

		SwipeMemory(int position) {
			this.position = position;
		}

		@Override
		public void onClose(ZSwipeItem layout) {

			if (mode == Mode.Multiple) {
				openPositions.remove(position);
			} else {
				openPosition = INVALID_POSITION;
			}
		}

		@Override
		public void onStartOpen(ZSwipeItem layout) {

			if (mode == Mode.Single) {
				closeAllExcept(layout);
			}
		}

		@Override
		public void onOpen(ZSwipeItem layout) {

			if (mode == Mode.Multiple)
				openPositions.add(position);
			else {
				closeAllExcept(layout);
				openPosition = position;
			}
		}

		public void setPosition(int position) {
			this.position = position;
		}
	}


	/**
	 * BasicAdapter Content
	 */
	private List<T> data;
	private LayoutInflater inflater;
	private Context context;

	public BasicSwipeAdapter(Context context, List<T> data) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		return this.data == null ? 0 : this.data.size();
	}

	/**
	 * 获取数据集合
	 *
	 * @return List<T>
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * 删除全部数据
	 */
	public void removeAll() {
		if (this.data != null) {
			this.data.clear();
			notifyDataSetChanged();
		}
	}

	/**
	 * 根据位置删除数据中的数据项
	 *
	 * @param position 位置
	 */
	public void remove(int position) {
		if (this.data != null && position >= 0 && position < this.data.size()) {
			this.data.remove(position);
			notifyDataSetChanged();
		}
	}

	/**
	 * 更新一条数据
	 * @param position
	 * @param data
	 */
	public void update(int position,T data){
		if (this.data == null) {
			this.data = new ArrayList<T>();
		}
		this.data.set(position,data);
		notifyDataSetChanged();
	}

	/**
	 * 更新全部数据，数据是追加的
	 * @param data
	 */
	public void updateAllAppend(List<T> data){
		if (data == null)
			return;
		if (this.data == null) {
			this.data = data;
		} else {
			this.data.addAll(data);
		}
		notifyDataSetChanged();
	}

	/**
	 * 更新全部数据
	 * @param data
	 */
	public void updateAll(List<T> data){
		if (data == null)
			return;
		this.data = data;
		notifyDataSetChanged();
	}

	@Override
	public T getItem(int position) {
		return (this.data == null || position < 0 || position >= this.data.size()) ? null : this.data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取LayoutInflater实例
	 * @return
	 */
	public LayoutInflater getLayoutInflater(){
		return inflater;
	}
	/**
	 * 获取当前上下文
	 * @return
	 */
	public Context getContext() {
		return context;
	}

	 public void intent(Class desClass, IntentExtra value) {
			if (null != value) {
				context.startActivity(new Intent(context, desClass).putExtra(IntentExtra.intent,
						value));
			} else {
				context.startActivity(new Intent(context, desClass));
			}

		}
}
