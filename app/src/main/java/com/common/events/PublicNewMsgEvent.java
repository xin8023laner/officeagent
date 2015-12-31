package com.common.events;

import com.common.db.PushModel;

/**
 * Created by zhangruntao on 15/5/19.
 */
public class PublicNewMsgEvent {

	private PushModel pushModel;

	public PublicNewMsgEvent(PushModel pushModel) {
		this.pushModel = pushModel;
	}

	public PushModel getPushModel() {
		return pushModel;
	}

	public void setPushModel(PushModel pushModel) {
		this.pushModel = pushModel;
	}

}
