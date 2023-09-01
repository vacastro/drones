package com.castro.drones.entities;

import com.castro.drones.enums.DroneModel;
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
	@Enumerated(EnumType.STRING)
	private DroneModel dronModel;
	
	@Column(nullable=false)
	private final int WEIGHT_LIMIT = 500;
	
	@Column(nullable=false)
	private int batteryCapacity;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	
	public Drone(String serialNumber, DroneModel dronModel) {
		this.serialNumber = serialNumber;
		this.dronModel = dronModel;
		this.status = Status.IDLE;
		this.batteryCapacity = 100;
		
	}


	

}
