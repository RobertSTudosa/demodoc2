package ro.apxsoftware.demodoc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.apxsoftware.demodoc.dao.AppointmentRepository;
import ro.apxsoftware.demodoc.entities.Appointment;

@Service
public class AppointmentService {
	
	@Autowired
	AppointmentRepository appRepo;
	
	public void saveApp(Appointment appointment) {
		appRepo.save(appointment);
	}

}
