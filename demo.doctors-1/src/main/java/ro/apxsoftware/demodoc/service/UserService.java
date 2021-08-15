package ro.apxsoftware.demodoc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ro.apxsoftware.demodoc.dao.PersonRepository;
import ro.apxsoftware.demodoc.dao.UserRepository;
import ro.apxsoftware.demodoc.entities.Person;
import ro.apxsoftware.demodoc.entities.User;

@Service("userDetailsService")
public class UserService implements UserDetailsService {
	
	@Autowired
	UserRepository userAccountRepo;
	
	@Autowired
	PersonRepository persRepo;
	
	public User save (User userAccount) {
		return userAccountRepo.save(userAccount);
	}
	
	public User saveUserAccountAndPerson(User userAccount, Person person) {
		
		persRepo.save(person);
		return userAccountRepo.save(userAccount);
		
	}
	
	public List<User> getAll() {

		return (List<User>)  userAccountRepo.findAll();
	}
	
	public Optional<User> findUserById(long userId) {
		return userAccountRepo.findById(userId);
	}
	
	
	public User findUserByPersonId (long personId) {
		return userAccountRepo.findUserFromPersonId(personId);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			User user = userAccountRepo.findUserByUsername(username);
//			return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),user.getRoles());
		return user;

	}
	
	public User findUserByEmailAddress(String userEmail) {
		return userAccountRepo.findByEmailIgnoreCase(userEmail);
	}
	
	
	public int getHowManyUserOfType(String type) {
		return userAccountRepo.getNumberOfUsersOfType(type);
	}
	
	public List<Long> getListOfUsersIdsOfType(String type) {
		return userAccountRepo.getListOfUsersIdsOfType(type);
	}


}
