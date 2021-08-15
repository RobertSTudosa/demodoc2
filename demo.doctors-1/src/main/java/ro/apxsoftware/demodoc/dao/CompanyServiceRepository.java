package ro.apxsoftware.demodoc.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ro.apxsoftware.demodoc.entities.CompanyService;

@Repository
public interface CompanyServiceRepository extends CrudRepository < CompanyService, Long> {
	
	public CompanyService findCompanyServiceBycompanyservId(long companyServId);
	
	@Modifying
	@Query(nativeQuery= true, value ="Delete from bpeople_demo_doctors.companyserv where appointment_appointment_id = ?1 ;")
	public void deleteAllByAppointmentId(long appointmentId);
	
	
}
