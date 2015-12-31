/*
package cn.socks.autoload;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

*/
/**
 * 该Adapter为示例，解决了当滑动删除的时候的Touch逻辑
 *//*


public class ExampleAdapter extends BaseSwipeAdapter{

	private SwipeRefreshLayout swipeRefreshLayout;
	private AutoLoadListView autoLoadListView;
	
	private ZSwipeItem openItem;
	

	public MainListAdapter(SwipeRefreshLayout swipeRefreshLayout
			,AutoLoadListView autoLoadListView) {
		this.swipeRefreshLayout = swipeRefreshLayout;
		this.autoLoadListView = autoLoadListView;
	}

	@Override
	public int getCount() {
		return waybills.size();
	}

	@Override
	public Object getItem(int position) {
		return waybills.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getSwipeLayoutResourceId(int position) {
		return R.id.zsi_main_swipe;
	}

	@Override
	public View generateView(int position, ViewGroup parent) {
		return context.getLayoutInflater().inflate(R.layout.item_main,
				parent, false);
	}

	@Override
	public void fillValues(final int position, final View convertView) {
		final ZSwipeItem swipeItem = (ZSwipeItem) convertView
				.findViewById(R.id.zsi_main_swipe);
		LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll_main_itemSwipeLayout);

		swipeItem.setShowMode(ShowMode.PullOut);
		swipeItem.setDragEdge(DragEdge.Right);
		

		swipeItem.addSwipeListener(new SimpleSwipeListener() {
			@Override
			public void onOpen(final ZSwipeItem layout) {
				Log.d(TAG, "打开:" + position);
				openItem = layout;
				swipeRefreshLayout.setEnabled(true);
				autoLoadListView.setIsScroll(true);
			}

			@Override
			public void onClose(ZSwipeItem layout) {
				Log.d(TAG, "关闭:" + position);
				swipeRefreshLayout.setEnabled(true);
				autoLoadListView.setIsScroll(true);
			}

			@Override
			public void onStartOpen(ZSwipeItem layout) {
				Log.d(TAG, "准备打开:" + position);
				swipeRefreshLayout.setEnabled(true);
				autoLoadListView.setIsScroll(true);
			}
			
			@Override
			public void onStartClose(ZSwipeItem layout) {
				Log.d(TAG, "准备关闭:" + position);
				swipeRefreshLayout.setEnabled(true);
				autoLoadListView.setIsScroll(true);
			}

			@Override
			public void onHandRelease(final ZSwipeItem layout, float xvel, float yvel) {
				Log.d(TAG, "手势释放");
				swipeRefreshLayout.setEnabled(true);
				autoLoadListView.setIsScroll(true);
			}

			@Override
			public void onUpdate(ZSwipeItem layout, int leftOffset,
			                     int topOffset) {
				swipeRefreshLayout.setEnabled(false);
				autoLoadListView.setIsScroll(false);
			}
		});
		
		convertView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(openItem != null)
						openItem.close();
					break;
				}
				return false;
			}
		});
		
		autoLoadListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(openItem != null && (openItem.getOpenStatus().equals(ZSwipeItem.Status.Open) || openItem.getOpenStatus().equals(ZSwipeItem.Status.Middle))){
					openItem.close();
					return;
				}
				Intent intent=new Intent(context,LogisticInfoActivity.class);
				intent.putExtra(Waybill.class.getSimpleName(),waybills.get(position));
				context.startActivityForResult(intent, MainActivity.REQUEST_QUERY_ITEM);
				context.overridePendingTransition(R.anim.right2left_translate,R.anim.right2left_set);
				
				try {
					PushModel pushModel = dbUtils.findFirst(Selector.from(PushModel.class).where(WhereBuilder.b("waybillId", "=", waybills.get(position).getWaybillId())));
					if(pushModel != null){
						pushModel.setRead(true);
						dbUtils.update(pushModel);
						notifyDataSetChanged();
					}
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		});

		ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				swipeItem.close();
			}
		});
		
	}
}
*/
