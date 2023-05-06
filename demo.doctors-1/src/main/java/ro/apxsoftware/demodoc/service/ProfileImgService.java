package ro.apxsoftware.demodoc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.apxsoftware.demodoc.dao.ProfileImgRepository;
import ro.apxsoftware.demodoc.entities.ProfileImg;

@Service
public class ProfileImgService {
	
	@Autowired
	private ProfileImgRepository picRepo;
	
	public ProfileImg savePic ( ProfileImg pic) {
		return picRepo.save(pic);
	}
	
	public Optional<ProfileImg> getProfilePic(Long picId) {
		return Optional.of(picRepo.findProfileImgByPicId(picId));
	}
	
	public List<ProfileImg> getProfilePics() {
		return (List<ProfileImg>) picRepo.findAll();
	}
	
	public List<ProfileImg> getProfilePicsToSave() {
		List<ProfileImg> picList = picRepo.getProfilePicsToSave();
		return picList;
	}
	
	public ProfileImg getLastProfilePic (long personId) {
		ProfileImg lastPicUploaded = (ProfileImg) picRepo.getLastProfilePic(personId);
		return lastPicUploaded;
	}
	
		
	public ProfileImg findProfilePicById(long id) {
		return picRepo.findProfileImgByPicId(id);
	}
	
	public List<ProfileImg> getPicsByPersonId(long personId) {
		List<ProfileImg> picList = picRepo.getSavedPicsByPersonId(personId);
		return picList;
	}
	

	
	public void deleteProfileImgById(ProfileImg pic) {
		picRepo.delete(pic);
	}
	

}
