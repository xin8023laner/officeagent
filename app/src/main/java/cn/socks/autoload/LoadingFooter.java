package cn.socks.autoload;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gt.officeagent.R;


public class LoadingFooter {

	private View mLoadingFooter;
	private TextView mLoadingText;
	private State mState = State.Idle;
	private JumpingBeans jumpingBeans;
	private Context context;
	
	public static enum State {
		Idle, TheEnd, Loading
	}

	public LoadingFooter(Context context) {
		this.context=context;
		mLoadingFooter = LayoutInflater.from(context).inflate(
				R.layout.autoload_loading_footer, null);
		mLoadingFooter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		mLoadingText = (TextView) mLoadingFooter.findViewById(R.id.textView);

		setState(State.Idle);
	}

	public View getView() {
		return mLoadingFooter;
	}

	public State getState() {
		return mState;
	}

	public void setState(final State state, long delay) {
		mLoadingFooter.postDelayed(new Runnable() {
			@Override
			public void run() {
				setState(state);
			}
		}, delay);
	}

	public void setState(State status) {
		if (mState == status) {
			return;
		}
		mState = status;

		switch (status) {
		case Loading:
			mLoadingFooter.setVisibility(View.VISIBLE);
			mLoadingText.setText("加载中");
			mLoadingText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			jumpingBeans = new JumpingBeans.Builder().appendJumpingDots(mLoadingText).build();
			
			
			break;
		case TheEnd:
			if (jumpingBeans != null) {
				jumpingBeans.stopJumping();
			}
			mLoadingText.setText("没有更多数据了");
			mLoadingText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			mLoadingFooter.setVisibility(View.VISIBLE);
			break;
		default:
			if (jumpingBeans != null) {
				jumpingBeans.stopJumping();
			}
			mLoadingFooter.setVisibility(View.GONE);
			break;
		}
	}
	
	public TextView getLoadingText(){
		return mLoadingText;
	}
	
	public void showFooter(){
		mLoadingFooter.setVisibility(View.VISIBLE);
	}
	
	public void hideFooter(){
		mLoadingFooter.setVisibility(View.GONE);
	}
}
