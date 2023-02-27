package com.castro.drones.utils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.castro.drones.entities.Dron;
import com.castro.drones.entities.RiskHistory;
import com.castro.drones.enums.RiskStatus;
import com.castro.drones.repository.DronRepository;
import com.castro.drones.repository.RiskHistoryRepository;

@Component
public class SchedulingCenter {
	
	@Autowired
	DronRepository dronRepository;
	
	@Autowired
	RiskHistoryRepository riskHistoryRepository;
	
	@Scheduled(cron ="0 0/3 * * * ?")
	public void creandoMiPrimerScheduled() {
		
		List<Dron> listDrones = dronRepository.findAll();
		
		if(!listDrones.isEmpty()) {
			
			for(Dron dron: listDrones) {
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
	
	

}
