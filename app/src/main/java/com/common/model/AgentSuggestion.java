package com.common.model;
/**
 * 建议实体类
 * @author zhangruntao
 *
 */
public class AgentSuggestion {

	private Long suggestionId;//建议id 
	private Integer suggestionType;//建议类型 Android ios
	private Long userId;
	private String telphone;//电话
	private String email;//邮箱
	private String content;//建议内容
	private String createTime;//创建时间
	private String handlerTime;//处理时间
	public Long getSuggestionId() {
		return suggestionId;
	}
	public void setSuggestionId(Long suggestionId) {
		this.suggestionId = suggestionId;
	}
	public Integer getSuggestionType() {
		return suggestionType;
	}
	public void setSuggestionType(Integer suggestionType) {
		this.suggestionType = suggestionType;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getHandlerTime() {
		return handlerTime;
	}
	public void setHandlerTime(String handlerTime) {
		this.handlerTime = handlerTime;
	}
	
}
