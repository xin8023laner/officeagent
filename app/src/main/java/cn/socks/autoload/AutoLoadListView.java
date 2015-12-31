package cn.socks.autoload;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author zhaokaiqiang
 *
 */
public class AutoLoadListView extends ListView implements
		AbsListView.OnScrollListener {

	private LoadingFooter mLoadingFooter;

	private OnLoadNextListener mLoadNextListener;
	
	private boolean isScroll = true;

	public AutoLoadListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public AutoLoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AutoLoadListView(Context context) {
		super(context);
		init();
	}

	private void init() {
		mLoadingFooter = new LoadingFooter(getContext());
		addFooterView(mLoadingFooter.getView());
		setOnScrollListener(this);
	}

	public void setOnLoadNextListener(OnLoadNextListener listener) {
		mLoadNextListener = listener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
			
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(totalItemCount<=visibleItemCount){
			mLoadingFooter.hideFooter();
		}else{
			mLoadingFooter.showFooter();
		}
		if (mLoadingFooter.getState() == LoadingFooter.State.Loading
				|| mLoadingFooter.getState() == LoadingFooter.State.TheEnd) {
			return;
		}
		if (firstVisibleItem + visibleItemCount >= totalItemCount
				&& totalItemCount != 0
				&& totalItemCount != (getHeaderViewsCount() + getFooterViewsCount())
				&& mLoadNextListener != null) {
			mLoadingFooter.setState(LoadingFooter.State.Loading);
			mLoadNextListener.onLoadNext();
		}
		
	}

	public void setState(LoadingFooter.State status) {
		mLoadingFooter.setState(status);
	}

	public void setState(LoadingFooter.State status, long delay) {
		mLoadingFooter.setState(status, delay);
	}

	public void setIsScroll(boolean isScroll){
		this.isScroll = isScroll;
	}
	
	public interface OnLoadNextListener {
		public void onLoadNext();
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(isScroll)
			return super.onInterceptTouchEvent(ev);
		else
			return false;
	}
	
	public TextView getLoadingText(){
		return mLoadingFooter.getLoadingText();
	}

}
