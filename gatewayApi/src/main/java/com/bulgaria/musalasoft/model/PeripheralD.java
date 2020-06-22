package com.bulgaria.musalasoft.model;

import java.io.Serializable;
import java.util.Date;
import com.bulgaria.musalasoft.model.Gateway;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
 

@Entity
@Table(name = "peripheral_device")
@EntityListeners(AuditingEntityListener.class)
@ApiModel("Peripheral Device")
public class PeripheralD implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "uid", nullable = false)
	@ApiModelProperty(notes = "the Peripheral's UID", required = true, example = "20013", position = 0)
	private int uid;
	@Column(name = "vendor", nullable = false)
	@ApiModelProperty(notes = "the Peripheral's vendor", required = true, example = "ASUS", position = 1)
	private String vendor;
	@Column(name = "date_created", nullable = false)
	@Temporal(TemporalType.DATE)
	@ApiModelProperty(notes = "the Peripheral's created date", required = true, example = "2020-1-11", position = 2)
	private Date dateCreated;
	@Column(name = "status_pd", nullable = false)
	@ApiModelProperty(notes = "the Peripheral's status", required = true, example = "false", position = 3)
	private boolean statusPd;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gateway_serial_number")
	@JsonIgnore
	private Gateway gateway;

	public PeripheralD() {
		super();

	}

	public PeripheralD(int uid, String vendor, Date dateCreated, boolean statusPd) {
		super();
		this.uid = uid;
		this.vendor = vendor;
		this.dateCreated = dateCreated;
		this.statusPd = statusPd;
	}

	public PeripheralD(int uid, String vendor, Date dateCreated, boolean statusPd, Gateway gateway) {
		super();
		this.uid = uid;
		this.vendor = vendor;
		this.dateCreated = dateCreated;
		this.statusPd = statusPd;
		this.gateway = gateway;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public boolean isStatusPd() {
		return statusPd;
	}

	public void setStatusPd(boolean statusPd) {
		this.statusPd = statusPd;
	}

	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + uid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeripheralD other = (PeripheralD) obj;
		if (uid != other.uid)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PeripheralD [uid=" + uid + ", vendor=" + vendor + ", dateCreated=" + dateCreated + ", statusPd="
				+ statusPd + ", gateway=" + gateway + "]";
	}
	
	

}
