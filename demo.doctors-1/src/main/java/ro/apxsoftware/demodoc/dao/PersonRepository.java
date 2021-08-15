package ro.apxsoftware.demodoc.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ro.apxsoftware.demodoc.entities.Person;

@Repository
public interface PersonRepository extends CrudRepository <Person, Long>{

	
		public Person findPersonByPersonId(long id);
		
		@Query(nativeQuery= true, value= "select persons.person_id, first_name, last_name, persons.email, app_status, "
				+ "employment_status, location, current_job, private_currentjob, active_job, work_experience, "
				+ " birth_date, job_wish_desc, total_hours, status_start_date, start_job, end_job,  "
				+ " last_img_id, unread_notifs, is_affiliated_to_agency "				
				+ " FROM persons "  
				+ " left outer join user_accounts ON  persons.person_id = user_accounts.user_id " 
				+ " WHERE user_accounts.user_id = ?1 ;")
		public Person findPersonFromUserId(long personId);
		
		public Person findPersonByEmailIgnoreCase(String email);
		
		@Query(nativeQuery= true, value= "select persons.person_id, first_name, last_name, persons.email, app_status, "
				+ "employment_status, location, current_job, private_currentjob, active_job, work_experience, "
				+ " availability, birth_date, job_wish_desc, total_hours, status_start_date, start_job, end_job,  "
				+ "	 last_img_id, unread_notifs, is_affiliated_to_agency "	
				+ " FROM persons "
				+ " left outer join person_jobs_applied ON person_jobs_applied.person_id = persons.person_id "
				+ " WHERE person_jobs_applied.job_id = ?1 ;")
		public Set<Person> personsAppliedToJob(long jobId );
		
		@Modifying
		@Query(nativeQuery= true, value= "DELETE FROM bpeople_BZBees.person_jobs_applied WHERE person_id = ?1 ; ")
		public void deletePersonApplications(long personId);
		
		@Query(nativeQuery= true, value= "select persons.person_id, first_name, last_name, persons.email, app_status, "
				+ "employment_status, location, current_job, private_currentjob, active_job, work_experience, "
				+ " availability, birth_date, job_wish_desc, total_hours, status_start_date, start_job, end_job,  "
				+ "	 last_img_id, unread_notifs, is_affiliated_to_agency "	
				+ " FROM persons "
				+ " left outer join person_jobs_approved ON person_jobs_approved.person_id = persons.person_id "
				+ " WHERE person_jobs_approved.job_id = ?1 ;")
		public Set<Person> personsApprovedToJob(long jobId );
		
		@Query(nativeQuery= true, value= "select persons.person_id, first_name, last_name, persons.email, app_status, "
				+ "employment_status, location, current_job, private_currentjob, active_job, work_experience, "
				+ " availability, birth_date, job_wish_desc, total_hours, status_start_date, start_job, end_job,  "
				+ "	 last_img_id, unread_notifs, is_affiliated_to_agency "	
				+ " FROM persons "
				+ " left outer join person_jobs_valid_date ON person_jobs_valid_date.person_id = persons.person_id "
				+ " WHERE person_jobs_valid_date.job_id = ?1 ;")
		public Set<Person> personsValidDateToJob(long jobId );
		
		
		
		@Query(nativeQuery= true, value = " select person_id "
				+ " FROM person_jobs_approved "
				+ " WHERE person_jobs_approved.job_id = ?1 ;")
		public Set<Long> personsIdsApprovedToJob(long jobId);
		
		@Query(nativeQuery= true, value = " select person_id "
				+ " FROM person_jobs_valid_date "
				+ " WHERE person_jobs_valid_date.job_id = ?1 ;")
		public Set<Long> personsIdsWithValidDateToJob(long jobId);

		@Query(nativeQuery= true, value= "select persons.person_id, first_name, last_name, persons.email, app_status, "
				+ "employment_status, location, current_job, private_currentjob, active_job, work_experience, "
				+ " availability, birth_date, job_wish_desc, total_hours, status_start_date, start_job, end_job,  "
				+ "	 last_img_id, unread_notifs, is_affiliated_to_agency "	
				+ " FROM persons "
				+ " left outer join person_jobs_rejected ON person_jobs_rejected.person_id = persons.person_id "
				+ " WHERE person_jobs_rejected.job_id = ?1 ;")
		public Set<Person> personsRejectedToJob(long jobId );
		
		
		@Query(nativeQuery= true, value = " select person_id "
				+ " FROM person_jobs_rejected "
				+ " WHERE person_jobs_rejected.job_id = ?1 ;")
		public Set<Long> personsIdsRejectedToJob(long jobId);	
		
		@Query(nativeQuery = true, value = "select * from persons "
				+ "left outer join  user_accounts on persons.person_id = user_accounts.user_id "
				+ "left outer join user_role on user_accounts.user_id = user_role.user_id "
				+ "left outer join userrole on user_role.role_id = userrole.role_id "
				+ "where userrole.permission = 'DOCTOR';")
		public Set<Person> Doctors();
	

}
