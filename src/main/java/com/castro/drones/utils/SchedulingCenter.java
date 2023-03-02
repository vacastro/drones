package com.castro.drones.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.castro.drones.entities.Drone;
import com.castro.drones.entities.RiskHistory;
import com.castro.drones.enums.RiskStatus;
import com.castro.drones.enums.Status;
import com.castro.drones.repository.DroneRepository;
import com.castro.drones.repository.RiskHistoryRepository;

@Component
public class SchedulingCenter {
	
	@Autowired
	DroneRepository dronRepository;
	
	@Autowired
	RiskHistoryRepository riskHistoryRepository;
	
	@Scheduled(cron ="0 0/3 * * * ?")
	public void logScheduled() {
		
		List<Drone> listDrones = dronRepository.findAll();
		
		if(!listDrones.isEmpty()) {
			
			for(Drone dron: listDrones) {
				RiskHistory log= new RiskHistory();
				log.setDate(new Date());
				log.setDron(dron);
				if(dron.getBatteryCapacity()>50) {
					log.setRiskStatus(RiskStatus.OK.toString());
				}else if(dron.getBatteryCapacity()>25 && dron.getBatteryCapacity()<=50) {
					log.setRiskStatus(RiskStatus.WARNING.toString());
				}else {
					log.setRiskStatus(RiskStatus.CRITICAL.toString());
				}
				riskHistoryRepository.save(log);		
			}
		}	
	}
	
	@Scheduled(cron ="0 0/1 * * * ?")
	public void dronScheduled() {
		
		List<Drone> listDrones = dronRepository.findAll();
		List<Drone> dronInTransit = new ArrayList<Drone>();
		
		if(!listDrones.isEmpty()) {
			
			for(Drone dron: listDrones) {
				if(dron.getStatus().equals(Status.DELIVERING.toString()) || dron.getStatus().equals(Status.RETURNING.toString())) {
					dronInTransit.add(dron);
				}
			}
		}
		
		for(Drone dron: dronInTransit) {
			if(dron.getBatteryCapacity()!=0) {
			dronRepository.updateBattery(dron.getBatteryCapacity()-1, dron.getIdDron());	
			}
		}
	}

}
