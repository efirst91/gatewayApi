package com.bulgaria.musalasoft.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class BlankFieldsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 372308194293192079L;
	private String actionName;
	private String className;

	public BlankFieldsException(String actioNname, String className) {
		//"Action %s is not possible in %s . Review the introduced data "

		super(String.format("Action %s is not possible in %s . Review the introduced data.", actioNname, className));
		this.actionName = actioNname;
		this.className = className;
	}

	public String getActionName() {
		return actionName;
	}

	public String getClassName() {
		return className;
	}

}
