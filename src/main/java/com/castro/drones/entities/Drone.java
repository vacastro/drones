package com.castro.drones.entities;

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
public class Drone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idDron;
	
	@Column(nullable=false, length=100)
	private String serialNumber;
	
	@Column(nullable=false)
	private String dronModel;
	
	@Column(nullable=false)
	private final int WEIGHT_LIMIT = 500;
	
	@Column(nullable=false)
	private int batteryCapacity;
	
	@Column(nullable=false)
	private String status;
	
	
	public Drone(String serialNumber, String dronModel) {
		this.serialNumber = serialNumber;
		this.dronModel = dronModel;
		this.status = Status.IDLE.toString();
		this.batteryCapacity = 100;
		
	}


	

}
