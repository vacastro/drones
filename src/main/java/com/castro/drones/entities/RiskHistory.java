package com.castro.drones.entities;

import java.util.Date;

import com.castro.drones.enums.RiskStatus;

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
public class RiskHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idRiskHistory;
	
	@Column
	private Date date;
	
	@ManyToOne
	@JoinColumn(name = "idDron", nullable = false, updatable = false)
	private Drone dron;
	
	@Column
	private String riskStatus;

}
