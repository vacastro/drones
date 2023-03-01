package com.castro.drones.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
public class Shipping implements Serializable{
	
	private static final long serialVerisionUID =1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idShipping;
	
	@Column
	private int idUser;
	
	@Column
	private int invoice;
	
	@Column
	private String adress;
	
	@Column
	private Date date;
	
	@Column
	private int weight;
	
	@ManyToOne
	@JoinColumn(name = "idDron", nullable = true, updatable = false)
	private Dron dron;
	
	@OneToMany (fetch= FetchType.EAGER ,cascade = CascadeType.ALL, mappedBy = "shipping", orphanRemoval=true)	
	@JsonManagedReference
	private List<Itinerary> listItinerary;
	
	@OneToMany (fetch= FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "shipping",orphanRemoval=true )
	@JsonManagedReference
	private List<MedicineDispensed> listMedicine;

	public Shipping(int idUser, int invoice, String adress) {
		super();
		this.idUser = idUser;
		this.invoice = invoice;
		this.adress = adress;
		this.date = new Date();
		this.weight = 0;
		this.listItinerary = new ArrayList<Itinerary>();
		this.listMedicine = new ArrayList<MedicineDispensed>();
	}
	

	
}
