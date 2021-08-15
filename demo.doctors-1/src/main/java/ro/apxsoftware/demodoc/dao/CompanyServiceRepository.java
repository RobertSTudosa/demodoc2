package ro.apxsoftware.demodoc.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ro.apxsoftware.demodoc.entities.CompanyService;

@Repository
public interface CompanyServiceRepository extends CrudRepository < CompanyService, Long> {
	
	public CompanyService findCompanyServiceBycompanyservId(long companyServId);
}
