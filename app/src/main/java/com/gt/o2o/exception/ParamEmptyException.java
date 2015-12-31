/**
 * 贯通云网参数异常
 */
package com.gt.o2o.exception;

/**
 * 贯通云网参数异常
 * @author uimagine
 */
public class ParamEmptyException extends Exception {

	// Fields
	
	/**
	 * default SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	// Constructor
	public ParamEmptyException(String exMessage) {
		super(exMessage);
	}

}