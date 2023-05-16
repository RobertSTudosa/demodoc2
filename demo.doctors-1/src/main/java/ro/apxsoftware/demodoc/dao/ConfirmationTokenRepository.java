package ro.apxsoftware.demodoc.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ro.apxsoftware.demodoc.entities.ConfirmationToken;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository <ConfirmationToken, Long>  {
	
	public ConfirmationToken findByConfirmationToken(String confirmationToken);

}
