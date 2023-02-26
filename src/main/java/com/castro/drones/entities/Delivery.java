package com.castro.drones.entities;

import java.sql.Date;

import com.castro.drones.enums.DeliveryStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Data @ToString
@Entity
@Table
public class Delivery {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDelivery;
	
	@Column
	private int idUser;
	
	@Column
	private int invoice;
	
	@Column
	private String adress;
	
	@Column
	private Date date;
	
	@Column
	private DeliveryStatus deliveryStatus;
	
	@ManyToOne
	@JoinColumn(name = "idDron", nullable = true, updatable = true)
	private Dron dron;

	public Delivery(int idUser, int invoice, String adress, Dron dron) {
		super();
		this.idUser = idUser;
		this.invoice = invoice;
		this.adress = adress;
		this.dron = dron;
//		this.date = Calendar.
		this.deliveryStatus = DeliveryStatus.ORDERED;
	}
	
	
	

}
