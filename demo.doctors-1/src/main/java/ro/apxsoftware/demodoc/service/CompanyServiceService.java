package ro.apxsoftware.demodoc.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.apxsoftware.demodoc.dao.CompanyServiceRepository;
import ro.apxsoftware.demodoc.entities.CompanyService;

@Service
public class CompanyServiceService {
	@Autowired
	private CompanyServiceRepository coservRepo;
	
	
	public CompanyService saveCompanyService(CompanyService companyServ) {
		 return coservRepo.save(companyServ);
	}
	
	public void deleteCompanyServicesSet (Set<CompanyService> coServ) {
		coservRepo.deleteAll(coServ);
	}
	
	public void deleteAllByAppointmentId(long appointmentId) {
		coservRepo.deleteAllByAppointmentId(appointmentId);
	}

}
