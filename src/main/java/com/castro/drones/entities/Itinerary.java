package com.castro.drones.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Data @ToString
@Entity
@Table
public class Itinerary implements Serializable {
	
	private static final long serialVerisionUID =1L;
	
	@EmbeddedId
	private ItineraryPK itinerary;
	
	@ManyToOne
	@EqualsAndHashCode.Exclude
	@JoinColumn(name = "idShipping", nullable = true, updatable = false)
	@JsonBackReference
	private Shipping shipping;
	

	public Itinerary(Shipping shipping) {
		super();
		this.shipping = shipping;
		this.itinerary = new ItineraryPK();
	}
	
	

}
