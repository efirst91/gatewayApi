package com.bulgaria.musalasoft.model;

import java.io.Serializable;
import java.util.List;
import com.bulgaria.musalasoft.model.PeripheralD;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "gateway")
@EntityListeners(AuditingEntityListener.class)
@ApiModel("Gateway")
public class Gateway implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "serial_number", nullable = false)
	@ApiModelProperty(notes = "the Gateway's serial number", required = true, example = "RECM12345M1C", position = 0)
	private String serialNumber;
	@Column(name = "human_readable_name", nullable = false)
	@ApiModelProperty(notes = "the Gateway's human-readable name", required = true, example = "Admon RRHH", position = 1)
	private String humanReadableName;
	@Column(name = "ipv4_address", nullable = false)
	@ApiModelProperty(notes = "the Gateway's ipv4 address. Must be unique", required = true, example = "192.168.10.254", position = 2)
	private String ipv4Address;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "gateway_serial_number")
	@ApiModelProperty(notes = "the Gateway's associated  peripheral devices", position = 3)
	private List<PeripheralD> peripheralDevices;

	public Gateway() {
		super();

	}

	public Gateway(String serialNumber, String humanReadableName, String ipv4Address) {
		super();
		this.serialNumber = serialNumber;
		this.humanReadableName = humanReadableName;
		this.ipv4Address = ipv4Address;
	}

	public Gateway(String serialNumber, String humanReadableName, String ipv4Address,
			List<PeripheralD> peripheralDevices) {
		super();
		this.serialNumber = serialNumber;
		this.humanReadableName = humanReadableName;
		this.ipv4Address = ipv4Address;
		this.peripheralDevices = peripheralDevices;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getHumanReadableName() {
		return humanReadableName;
	}

	public void setHumanReadableName(String humanReadableName) {
		this.humanReadableName = humanReadableName;
	}

	public String getIpv4Address() {
		return ipv4Address;
	}

	public void setIpv4Address(String ipv4Address) {
		this.ipv4Address = ipv4Address;
	}

	public List<PeripheralD> getPeripheralDevices() {
		return peripheralDevices;
	}

	public void setPeripheralDevices(List<PeripheralD> peripheralDevices) {
		this.peripheralDevices = peripheralDevices;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serialNumber == null) ? 0 : serialNumber.hashCode());
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
		Gateway other = (Gateway) obj;
		if (serialNumber == null) {
			if (other.serialNumber != null)
				return false;
		} else if (!serialNumber.equals(other.serialNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Gateway [serialNumber=" + serialNumber + ", humanReadableName=" + humanReadableName + ", ipv4Address="
				+ ipv4Address + ", peripheralDevices=" + peripheralDevices + "]";
	}
	
	

}
