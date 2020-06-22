package com.bulgaria.musalasoft.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
public class NotOwnerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String owner;
	private String ownerFieldName;
	private Object ownerFieldValue;

	private String property;
	private String propertyFieldName;
	private Object propertyFieldValue;

	public NotOwnerException(String owner, String ownerFieldName, Object ownerFieldValue, String property,
			String propertyFieldName, Object propertyFieldValue) {
		super(String.format("%s with %s : '%s', is not owner of %s with %s : %s. ", owner, ownerFieldName,
				ownerFieldValue, property, propertyFieldName, propertyFieldValue));
		this.owner = owner;
		this.ownerFieldName = ownerFieldName;
		this.ownerFieldValue = ownerFieldValue;
		this.property = property;
		this.propertyFieldName = propertyFieldName;
		this.propertyFieldValue = propertyFieldValue;
	}

	public String getOwner() {
		return owner;
	}

	public String getOwnerFieldName() {
		return ownerFieldName;
	}

	public Object getOwnerFieldValue() {
		return ownerFieldValue;
	}

	public String getProperty() {
		return property;
	}

	public String getPropertyFieldName() {
		return propertyFieldName;
	}

	public Object getPropertyFieldValue() {
		return propertyFieldValue;
	}

}
