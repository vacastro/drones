package com.castro.drones.response;

import java.io.Serializable;
import java.sql.Timestamp;

import com.castro.drones.entities.Shipping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ShippingResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private boolean success;
	private String resultString;
	private Timestamp timestamp;
	private Shipping shipping;
	
	public ShippingResponse(String resultString, Shipping shipping) {
		super();
		this.success = Boolean.TRUE;
		this.timestamp  = new Timestamp(System.currentTimeMillis());
		this.resultString = resultString;
		this.shipping = shipping;
	}
	
	

}
