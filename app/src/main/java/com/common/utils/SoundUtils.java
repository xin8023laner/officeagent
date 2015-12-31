/**
 * Project Name:YTOInfield
 * File Name:SoundManager.java
 * Package Name:cn.net.yto.infield.biz
 * Date:2013-3-7 am 10:10:13
 * Copyright (c) 2013, zhiliantiandi All Rights Reserved.
 *
 */

package com.common.utils;

import com.gt.officeagent.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * ClassName:SoundManager <br/>
 * Date: 2013-3-7 am 10:10:13 <br/>
 * 
 * @author Liliang
 * @version
 * @since JDK 1.6
 * @see
 */
public final class SoundUtils {

	public static final int SOUND_TYPE_SUCCESS = 0;
	public static final int SOUND_TYPE_WARNING = 1;
	public static final int SOUND_TYPE_QUERY = 2;

	private static SoundUtils sManager;
	private float mStreamVolume = 0.1f;
	private Context mContext;

	private int mWarningId = R.raw.warning;
	private int mSuccessId = R.raw.success;
	private int mQueryId = R.raw.query;
	private int mZeroId = R.raw.zero;
	private int mOneId = R.raw.one;
	private int mTwoId = R.raw.two;
	private int mThreeId = R.raw.three;
	private int mFourId = R.raw.four;
	private int mFiveId = R.raw.five;
	private int mSixId = R.raw.six;
	private int mSevenId = R.raw.seven;
	private int mEightId = R.raw.eight;
	private int mNineId = R.raw.nine;

	private SoundUtils() {

	}

	public static SoundUtils getInstance() {
		if (sManager == null) {
			sManager = new SoundUtils();
		}

		return sManager;
	}

	public void init(Context context) {
		if (mContext == null || mSoundPool == null) {
			mContext = context;
			loadSoundResources(context);
		}
	}

	public void init(Context context, int warningId, int successId, int queryId) {
		if (mContext == null || mSoundPool == null) {
			mContext = context;
			mWarningId = warningId;
			mSuccessId = successId;
			mQueryId = queryId;
			loadSoundResources(context);
		}
	}

	/**
	 * 
	 * playSound:Play sound for scan. <br/>
	 * 
	 * @author Liliang
	 * @param soundType
	 *            One of {@link #SOUND_TYPE_SUCCESS},
	 *            {@link #SOUND_TYPE_WARNING}, or {@link #SOUND_TYPE_QUERY}
	 * @since JDK 1.6
	 */
	public void playSound(int soundType) {
		int soundResId = mSoundSuccessId;
		switch (soundType) {
		case SOUND_TYPE_SUCCESS:
			soundResId = mSoundSuccessId;
			break;

		case SOUND_TYPE_WARNING:
			soundResId = mSoundWarningId;
			break;

		case SOUND_TYPE_QUERY:
			soundResId = mSoundQueryId;
			break;

		default:
			break;
		}

		mSoundPool.play(soundResId, getVolume(), getVolume(), 1, 0, 1f);
	}

	public void playOther(int resourceId) {
init(mContext);
		// int id = mSoundPool.load(mContext, resourceId, 1);
		switch (resourceId) {
		case 0:
			mSoundPool.play(mSoundZeroId, getVolume(), getVolume(), 1, 0, 1f);
			break;
		case 1:
			mSoundPool.play(mSoundOneId, getVolume(), getVolume(), 1, 0, 1f);
			break;
		case 2:
			mSoundPool.play(mSoundTwoId, getVolume(), getVolume(), 1, 0, 1f);
			break;
		case 3:
			mSoundPool.play(mSoundThreeId, getVolume(), getVolume(), 1, 0, 1f);
			break;
		case 4:
			mSoundPool.play(mSoundFourId, getVolume(), getVolume(), 1, 0, 1f);
			break;
		case 5:
			mSoundPool.play(mSoundFiveId, getVolume(), getVolume(), 1, 0, 1f);
			break;
		case 6:
			mSoundPool.play(mSoundSixId, getVolume(), getVolume(), 1, 0, 1f);
			break;
		case 7:
			mSoundPool.play(mSoundSevenId, getVolume(), getVolume(), 1, 0, 1f);
			break;
		case 8:
			mSoundPool.play(mSoundEightId, getVolume(), getVolume(), 1, 0, 1f);
			break;
		case 9:
			mSoundPool.play(mSoundNineId, getVolume(), getVolume(), 1, 0, 1f);
			break;

		default:
			break;
		}
		

	}

	public void warn() {
		mSoundPool.play(mSoundWarningId, getVolume(), getVolume(), 1, 0, 1f);
	}

	public void warn(float leftVolume, float rightVolume) {
		mSoundPool.play(mSoundWarningId, leftVolume, rightVolume, 1, 0, 1f);
	}

	public void success() {
		mSoundPool.play(mSoundSuccessId, getVolume(), getVolume(), 1, 0, 1f);
	}

	public void success(float leftVolume, float rightVolume) {
		mSoundPool.play(mSoundSuccessId, leftVolume, rightVolume, 1, 0, 1f);
	}

	public void query() {
		mSoundPool.play(mSoundQueryId, getVolume(), getVolume(), 1, 0, 1f);
	}

	public void query(float leftVolume, float rightVolume) {
		mSoundPool.play(mSoundQueryId, leftVolume, rightVolume, 1, 0, 1f);
	}

	private float getVolume() {
		return 0.8f;
	}

	private void loadSoundResources(Context context) {
		release();
		if (mSoundPool == null) {
			mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
			mSoundWarningId = mSoundPool.load(context, mWarningId, 1);
			mSoundSuccessId = mSoundPool.load(context, mSuccessId, 1);
			mSoundQueryId = mSoundPool.load(context, mQueryId, 1);
			mSoundZeroId = mSoundPool.load(context, mZeroId, 1);
			mSoundOneId = mSoundPool.load(context, mOneId, 1);
			mSoundTwoId = mSoundPool.load(context, mTwoId, 1);
			mSoundThreeId = mSoundPool.load(context, mThreeId, 1);
			mSoundFourId = mSoundPool.load(context, mFourId, 1);
			mSoundFiveId = mSoundPool.load(context, mFiveId, 1);
			mSoundSixId = mSoundPool.load(context, mSixId, 1);
			mSoundSevenId = mSoundPool.load(context, mSevenId, 1);
			mSoundEightId = mSoundPool.load(context, mEightId, 1);
			mSoundNineId = mSoundPool.load(context, mNineId, 1);
		}
	}

	public void release() {
		if (mSoundPool != null) {
			mSoundPool.release();
		}
		mSoundPool = null;
	}

	private SoundPool mSoundPool;
	private int mSoundWarningId;
	private int mSoundSuccessId;
	private int mSoundQueryId;
	private int mSoundZeroId;
	private int mSoundOneId;
	private int mSoundTwoId;
	private int mSoundThreeId;
	private int mSoundFourId;
	private int mSoundFiveId;
	private int mSoundSixId;
	private int mSoundSevenId;
	private int mSoundEightId;
	private int mSoundNineId;

}