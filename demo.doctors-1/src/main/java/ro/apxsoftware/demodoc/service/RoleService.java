package ro.apxsoftware.demodoc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.apxsoftware.demodoc.dao.RoleRepository;
import ro.apxsoftware.demodoc.entities.UserRole;

@Service
public class RoleService {
	
	@Autowired
	RoleRepository roleRepo;
	
	public UserRole saveRole(UserRole role) {
		return roleRepo.save(role);
		
	}
	
	public List<UserRole> findRolesByTheUserId (long userId) {
		List<UserRole> roles = (List<UserRole>) roleRepo.findRolesForTheUser(userId);
		return roles;
	}

}
