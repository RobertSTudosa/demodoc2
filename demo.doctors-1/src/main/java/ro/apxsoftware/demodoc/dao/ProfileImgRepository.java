package ro.apxsoftware.demodoc.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ro.apxsoftware.demodoc.entities.ProfileImg;

@Repository
public interface ProfileImgRepository extends CrudRepository<ProfileImg, Long> {
	
	 @Query(nativeQuery= true,
			 value="select profile_img.pic_id , pic_name, pic_type, data " + 
			 		"	FROM profile_img " + 
			 		"	left outer join persons ON profile_img.person_person_id = persons.person_id " + 
			 		"       WHERE profile_img.person_person_id is NOT NULL;") 
			 public List<ProfileImg> getProfilePicsToSave ();
	 
	 @Query(nativeQuery= true,
			 value="select profile_img.pic_id , pic_name, pic_type, data, person_person_id " + 
			 		"	FROM profile_img " + 
			 		"	left outer join persons ON profile_img.person_person_id = persons.person_id " + 
			 		" 	where persons.person_id = ?1 " + 
			 		" ORDER BY pic_id DESC LIMIT 1;") 
			 public ProfileImg getLastProfilePic(long personId);
	 
	 @Query(nativeQuery= true,
			 value="select profile_img.pic_id , pic_name, pic_type, data, person_person_id " + 
			 		"	FROM profile_img " + 
			 		"   WHERE profile_img.person_person_id = ?1 ; ") 
	public List<ProfileImg> getSavedPicsByPersonId (long personId);
	 
	 
	// must be camelcase or else it will not find the instance of the object
	 public ProfileImg findProfileImgByPicId (long id);
 
	 
	

}
