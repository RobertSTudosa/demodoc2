package ro.apxsoftware.demodoc.entities;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;



@Entity
@Table(name="persons")
public class Person implements Serializable {

	@Id()
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_generator")
	@SequenceGenerator(name = "person_generator", sequenceName = "person_seq", allocationSize = 1)
	@Column(name="person_id")
	private long personId;

	private String firstName;

	private String lastName;
	
	private String email;

	private int appStatus;

	private String employmentStatus;
	
	private long lastImgId;
	
	private boolean unreadNotifs = false;	
		


	@DateTimeFormat(iso = ISO.TIME)
	private LocalTime startDay;
	
	@DateTimeFormat(iso=ISO.TIME)
	private LocalTime endDay;
	
	@OneToOne(mappedBy="person")
	private User user;
	
	private boolean isAffiliatedToAgency = false;

	private String location;

	private String currentJob;
	
	private boolean privateCurrentjob;

	private boolean activeJob;

	private int workExperience;

	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date BirthDate;
	
	private String jobWishDesc;

	private double totalHours;
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date statusStartDate;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startJob;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endJob;
	
	@OneToMany(mappedBy="doctor" ,
			cascade= {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST})
	private Set<Appointment> docAppointments = new HashSet<>();
	
	@OneToMany(mappedBy="pacient" ,
			cascade= {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST})
	@OrderBy("appointmentId")
	private Set<Appointment> pacientAppointments = new HashSet<>();
	
	private String phone;
	
	@OneToMany(mappedBy="person" ,
			cascade= {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST})
	private List<ProfileImg> profileImg;
	
	
	@Column(name="day_start_time")
	private LocalTime dayStartTime;
	
	@Column(name="day_end_time")
	private LocalTime dayEndTime;
	
	
	@Column(name="lunch_start_time")
	private LocalTime lunchStartTime;
	
	@Column(name="lunch_end_time")
	private LocalTime lunchEndTime;
	
	
	
	public Person() {
		
		
	}
	

	public Person(long personId, String firstName, String lastName, String email, int appStatus,
			String employmentStatus, String location, String currentJob, boolean privateCurrentjob,
			boolean activeJob, int workExperience,  Date birthDate,
			String jobWishDesc, double totalHours, Date statusStartDate, Date startJob, Date endJob, String phone) {
		super();
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.appStatus = appStatus;
		this.employmentStatus = employmentStatus;
		this.location = location;
		this.currentJob = currentJob;
		this.privateCurrentjob = privateCurrentjob;
		this.activeJob = activeJob;
		this.workExperience = workExperience;
		
		BirthDate = birthDate;
		this.jobWishDesc = jobWishDesc;
		this.totalHours = totalHours;
		this.statusStartDate = statusStartDate;
		this.startJob = startJob;
		this.endJob = endJob;
		this.phone = phone;
	}
	
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public long getPersonId() {
		return personId;
	}

//	public void setPersonId(long personId) {
//		this.personId = personId;
//	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(int appStatus) {
		this.appStatus = appStatus;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
	
	


//	public List<Skill> getSkills() {
//		
//		return skills;
//	}
//
//
//	public void setSkills(List<Skill> skills) {
//		this.skills = skills;
//	}
//
//
//	public void addSkill(Skill skill) {
//		if (skills == null) {
//			
//			skills = new ArrayList<>();
//		}
//		
//		skills.add(skill);
//		
//	}
//	
//	public void removeSkill(long skillId) {
//		this.skills.remove(skillId);
//	}
//	
//	public List<Language> getLanguages() {
//		return languages;
//	}
//
//
//	public void setLanguages(List<Language> languages) {
//		this.languages = languages;
//	}
//	
//	public void removeLang(long langId) {
//		this.languages.remove(langId);
//	}
//	
//	public void addLang(Language lang) {
//		if (languages == null) {
//			
//			languages = new ArrayList<>();
//		}
//		
//		languages.add(lang);
//	}
//
//	public List<Job> getJobs() {
//		return jobs;
//	}
//
//
//	public void addJob(Job job) {
//		if (jobs == null) {
//			
//			jobs = new ArrayList<>();
//		}
//		
//		jobs.add(job);
//	}
//
//	public void setJobs(List<Job> jobs) {
//		this.jobs = jobs;
//	}
//	
//	public List<Doc> getDocs() {
//		return docs;
//	}
//
//
//	public void setDocs(List<Doc> docs) {
//		this.docs = docs;
//	}
//	
//	
//	public void addDoc(Doc doc) {
//		if(docs == null) {
//			docs = new ArrayList<>();
//		}
//		
//		docs.add(doc);
//	}
//	
//	public void addPic (ProfileImg img) {
//		if(pics == null) {
//			pics = new ArrayList<>();
//		}
//		
//		pics.add(img);
//	}
//
//	public List<ProfileImg> getPics() {
//		return pics;
//	}
//
//
//
//
//	public void setPics(List<ProfileImg> pics) {
//		this.pics = pics;
//	}




	public Set<Appointment> getDocAppointments() {
		return docAppointments;
	}


	public void setDocAppointments(Set<Appointment> docAppointments) {
		this.docAppointments = docAppointments;
	}
	


	public Set<Appointment> getPacientAppointments() {
		return pacientAppointments;
	}
	
	/*
	 * public List<Appointment> getSortedPacientAppointments() { List<Appointment>
	 * sortedList = new ArrayList<>(); sortedList.addAll(this.pacientAppointments);
	 * Collections.sort( sortedList); return sortedList; }
	 */
 

	public void setPacientAppointments(Set<Appointment> pacientAppointments) {
		this.pacientAppointments = pacientAppointments;
	}


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCurrentJob() {
		return currentJob;
	}

	public void setCurrentJob(String currentJob) {
		this.currentJob = currentJob;
	}

	public boolean isPrivateCurrentjob() {
		return privateCurrentjob;
	}

	public void setPrivateCurrentjob(boolean privateCurrentjob) {
		this.privateCurrentjob = privateCurrentjob;
	}

	public boolean isActiveJob() {
		return activeJob;
	}

	public void setActiveJob(boolean activeJob) {
		this.activeJob = activeJob;
	}

	public int getWorkExperience() {
		return workExperience;
	}
	

	public void setWorkExperience(int workExperience) {
		this.workExperience = workExperience;
	}
	
	public Date getBirthDate() {
		return BirthDate;
	}


	public void setBirthDate(Date birthDate) {
		BirthDate = birthDate;
	}



	public String getJobWishDesc() {
		return jobWishDesc;
	}

	public void setJobWishDesc(String jobWishDesc) {
		this.jobWishDesc = jobWishDesc;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}

	public Date getStatusStartDate() {
		return statusStartDate;
	}

	public void setStatusStartDate(Date statusStartDate) {
		this.statusStartDate = statusStartDate;
	}

	public Date getStartJob() {
		return startJob;
	}

	public void setStartJob(Date startJob) {
		this.startJob = startJob;
	}

	public Date getEndJob() {
		return endJob;
	}

	public void setEndJob(Date endJob) {
		this.endJob = endJob;
	}





	public long getLastImgId() {
		return lastImgId;
	}


	public void setLastImgId(long lastImgId) {
		this.lastImgId = lastImgId;
	}


//	public List<SocialMedia> getSocialMedia() {
//		return socialMedia;
//	}
//
//
//	public void setSocialMedia(List<SocialMedia> socialMedia) {
//		this.socialMedia = socialMedia;
//	}
//
//
//	public List<Notification> getNotifications() {
//		return notifications;
//	}
//
//
//	public void setNotifications(List<Notification> notifications) {
//		this.notifications = notifications;
//	}
//	
//	public void addNotification(Notification notification) {
//		this.notifications.add(notification);
//	}


	public void setPersonId(long personId) {
		this.personId = personId;
	}


	public boolean isUnreadNotifs() {
		return unreadNotifs;
	}


	public void setUnreadNotifs(boolean unreadNotifs) {
		this.unreadNotifs = unreadNotifs;
	}




	public boolean isAffiliatedToAgency() {
		return isAffiliatedToAgency;
	}




	public void setAffiliatedToAgency(boolean isAffiliatedToAgency) {
		this.isAffiliatedToAgency = isAffiliatedToAgency;
	}
	
	
	
	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	


	public List<ProfileImg> getProfileImg() {
		return profileImg;
	}


	public void setProfileImg(List<ProfileImg> profileImg) {
		this.profileImg = profileImg;
	}
	
	


	public LocalTime getStartDay() {
		return startDay;
	}


	public void setStartDay(LocalTime startDay) {
		this.startDay = startDay;
	}


	public LocalTime getEndDay() {
		return endDay;
	}


	public void setEndDay(LocalTime endDay) {
		this.endDay = endDay;
	}
	
	
	


	public LocalTime getDayStartTime() {
		return dayStartTime;
	}


	public void setDayStartTime(LocalTime dayStartTime) {
		this.dayStartTime = dayStartTime;
	}


	public LocalTime getDayEndTime() {
		return dayEndTime;
	}


	public void setDayEndTime(LocalTime dayEndTime) {
		this.dayEndTime = dayEndTime;
	}


	public LocalTime getLunchStartTime() {
		return lunchStartTime;
	}


	public void setLunchStartTime(LocalTime lunchStartTime) {
		this.lunchStartTime = lunchStartTime;
	}


	public LocalTime getLunchEndTime() {
		return lunchEndTime;
	}


	public void setLunchEndTime(LocalTime lunchEndTime) {
		this.lunchEndTime = lunchEndTime;
	}


	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if( !(obj instanceof Person)) return false;
		
		Person person = (Person) obj;
		
		if(!getFirstName().equals(person.getFirstName())) return false;
		return personId != 0L && personId == person.getPersonId();

	}


//	public Set<Job> getJobsInList() {
//		return jobsInList;
//	}
//
//
//	public void setJobsInList(Set<Job> jobsInList) {
//		this.jobsInList = jobsInList;
//	}
//
//
//	public Set<Job> getJobsApplied() {
//		return jobsApplied;
//	}
//
//
//	public void setJobsApplied(Set<Job> jobsApplied) {
//		this.jobsApplied = jobsApplied;
//	}
//	
//	public void addJobsInList(Job job) {
//		if(this.jobsInList == null) {
//			this.jobsInList = new HashSet<>();
//			this.jobsInList.add(job);
//		} else {
//			this.jobsInList.add(job);
//		}
//		
//	}
//	
//	public void addJobsApplied(Job job) {
//		if(this.jobsApplied == null) {
//			this.jobsApplied = new HashSet<>();
//			this.jobsApplied.add(job);
//		} else {
//			this.jobsApplied.add(job);
//		}
//		
//	}
//
//
//	public Set<Job> getJobsApproved() {
//		return jobsApproved;
//	}
//
//
//	public void setJobsApproved(Set<Job> jobsApproved) {
//		this.jobsApproved = jobsApproved;
//	}
//
//
//	public Set<Job> getJobsValidDate() {
//		return jobsValidDate;
//	}
//
//
//	public void setJobsValidDate(Set<Job> jobsValidDate) {
//		this.jobsValidDate = jobsValidDate;
//	}
//
//
//	public Set<Job> getJobsRejected() {
//		return jobsRejected;
//	}
//
//
//	public void setJobsRejected(Set<Job> jobsRejected) {
//		this.jobsRejected = jobsRejected;
//	}
//	
	


}
