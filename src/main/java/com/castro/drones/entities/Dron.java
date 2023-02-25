package com.castro.drones.entities;

import com.castro.drones.enums.DronModel;
import com.castro.drones.enums.Status;

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
public class Dron {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false, length=100)
	private String serialNumber;
	
	@Column(nullable=false)
	private DronModel dronModel;
	
	@Column(nullable=false)
	private static final int WEIGHT_LIMIT = 500;
	
	@Column(nullable=false)
	private int batteryCapacity;
	
	@Column(nullable=false)
	private Status status;
	
	
	public Dron(String serialNumber, DronModel dronModel) {
		this.serialNumber = serialNumber;
		this.dronModel = dronModel;
		this.status = Status.IDLE;
		this.batteryCapacity = 100;
		
	}


	

}
