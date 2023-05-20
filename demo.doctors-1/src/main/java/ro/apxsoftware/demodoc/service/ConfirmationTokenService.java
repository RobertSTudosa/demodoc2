package ro.apxsoftware.demodoc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.apxsoftware.demodoc.dao.ConfirmationTokenRepository;
import ro.apxsoftware.demodoc.entities.ConfirmationToken;

@Service
public class ConfirmationTokenService {
	
	@Autowired
	ConfirmationTokenRepository confTokenRepo;
	
	public ConfirmationToken findConfirmationTokenByConfirmationToken (String confirmationToken) {
		return confTokenRepo.findByConfirmationToken(confirmationToken);
	}
	
	public void saveToken (ConfirmationToken confToken) {
		confTokenRepo.save(confToken);
	}

}
