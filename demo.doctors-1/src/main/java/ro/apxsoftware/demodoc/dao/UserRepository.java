package ro.apxsoftware.demodoc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ro.apxsoftware.demodoc.entities.User;

@Repository
public interface UserRepository extends CrudRepository <User, Long> {
	
	@Override
	public List<User> findAll();
	
	
	//create a left outer join to retrieve the user entity via person id. join tables user_accounts with persons
	@Query(nativeQuery= true, value= "select user_accounts.user_id, username, user_accounts.email, password, user_accounts.enable, " +
 						" user_accounts.account_non_expired, user_accounts.account_non_locked, "
 						+ "user_accounts.credentials_non_expired,  "
 						+ "user_accounts.person_id,  job_approved"  
						+ " FROM user_accounts "  
						+ " left outer join persons ON user_accounts.user_id = persons.person_id " 
						+ " WHERE persons.person_id = ?1 ;")
	public User findUserFromPersonId(long personId);
	
	@Query(nativeQuery= true, value = "select user_accounts.user_id, username, user_accounts.email, password, user_accounts.enable, " +
 						" user_accounts.account_non_expired, user_accounts.account_non_locked, "
 						+ "user_accounts.credentials_non_expired, user_accounts.person_id, job_approved " 
						+ " FROM user_accounts "					 
						+ " WHERE user_accounts.username = ?1 ;")
	public User findUserByUsername(String username);
	


	
	
	public User findByEmailIgnoreCase(String email);

	@Query(nativeQuery= true, value = "select count(user_accounts.user_id) FROM user_accounts "
			+ " left outer join bpeople_demo_doctors.user_role ON user_role.user_id = user_accounts.user_id "
			+ " left outer join bpeople_demo_doctors.userrole ON user_role.role_id = userrole.role_id "
			+ "						 WHERE userrole.permission = ?1 ;")
	public int getNumberOfUsersOfType(String type);
	
	@Query(nativeQuery= true, value = "select user_accounts.user_id FROM user_accounts "
			+ " left outer join bpeople_demo_doctors.user_role ON user_role.user_id = user_accounts.user_id "
			+ " left outer join bpeople_demo_doctors.userrole ON user_role.role_id = userrole.role_id "
			+ "						 WHERE userrole.permission = ?1 ;")
	public List<Long> getListOfUsersIdsOfType(String type);

}
