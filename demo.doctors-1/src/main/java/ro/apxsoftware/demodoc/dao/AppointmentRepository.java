package ro.apxsoftware.demodoc.dao;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ro.apxsoftware.demodoc.entities.Appointment;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
	
	public Appointment findAppointmentByDate(Date date);
	
	
//	@Query(nativeQuery= true, value= "SELECT")
//	public Set<Date> availableAppointmentsDatesByDoctor(); 
	
}
