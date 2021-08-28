package ro.apxsoftware.demodoc.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.apxsoftware.demodoc.dao.PersonRepository;
import ro.apxsoftware.demodoc.dao.UserRepository;
import ro.apxsoftware.demodoc.entities.Person;
import ro.apxsoftware.demodoc.utils.AppDateFormater;

@Service
public class PersonService {
	
	@Autowired 
	PersonRepository persRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	AppDateFormater appDF;
	
	@Autowired
	DatesAndTimeService dtServ;
	
	
	final Pattern TRIM_UNICODE_PATTERN = Pattern.compile("^\\p{Blank}*(.*)\\p{Blank}$", Pattern.UNICODE_CHARACTER_CLASS);
	final Pattern SPLIT_SPACE_UNICODE_PATTERN = Pattern.compile("\\p{Blank}", Pattern.UNICODE_CHARACTER_CLASS);
	
	public Person save(Person person) {
		return persRepo.save(person);
	}
	
//	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//	
//	public boolean checkAvailability (Person person) {
//		
//
//		if (person.getAvailability().after(new Date())) {
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	public boolean checkBirthDate (Person person) {
		
		long nowT = new Date().getTime();
		nowT -= 18*24*60*60*1000;
		Date adult18 = new Date(nowT);
		if (person.getBirthDate().before(adult18)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Person findPersonById (long id) {
		return persRepo.findPersonByPersonId(id);
	}
	
	public List<Person> getAll () {
		return (List<Person>) persRepo.findAll();
		
	}
	
	public Person findPersonByUserId(long userId) {
		return persRepo.findPersonFromUserId(userId);
	}
	
	public Person findPersonByEmail (String email) {
		return persRepo.findPersonByEmailIgnoreCase(email);
	}
	
	public Set<Person> findCandidatesAppliedToJob (long jobId) {
		return persRepo.personsAppliedToJob(jobId);
	}
	
	public void deletePersonApplicationsBypersonId(long personId) {
		persRepo.deletePersonApplications(personId);
	}
	
	public Set<Person> getCandidatesApprovedToJob (long jobId) {
		return persRepo.personsApprovedToJob(jobId);
	}
	
	public Set<Long> getPersonsIdsApprovedToJob (long jobId) {
		return persRepo.personsIdsApprovedToJob(jobId);
	}
	
	public Set<Person> getCandidatesWithValidDateByJobId (long jobId) {
		return persRepo.personsValidDateToJob(jobId);
	}
	
	public Set<Long> getCandidatesIdsWithValidDatesByJobId(long jobId) {
		return persRepo.personsIdsWithValidDateToJob(jobId);
	}
	
	public Set<Person> getCandidatesRejectedByJobId (long jobId) {
		return persRepo.personsRejectedToJob(jobId); 
	}
	
	public Set<Long> getCandidatesIdsRejectedByJobId(long jobId) {
		return persRepo.personsIdsRejectedToJob(jobId); 
	}
	
	public Set<Person> getDoctors() {
		return persRepo.Doctors();
	}

	public List<Person> getClientsForProviderByProviderId(long personId) {
		// TODO Auto-generated method stub
		return persRepo.getClientsForProviderByProviderId(personId);
	}
	
	public List<Person> getClientsByName() {
		// TODO Auto-generated method stub
		return persRepo.getClientsByName();
	}

	public List<Person> getAllClientsByServiceIdByMonth(long userId, String stringMonth) {
		// TODO Auto-generated method stub
		return persRepo.getClientsByServiceIdByMonth(userId, stringMonth);
	}

	public List<Person> getAllClientsByServiceIdByMonthNoLimit(long personId, String stringMonth) {
		// TODO Auto-generated method stub
		return persRepo.getClientsByServiceIdByMonthNoLimit(personId, stringMonth);
	}
	
	
	private String[] trimSplitUnicodeBySpace(String str)
    {
        Matcher trimMatcher = TRIM_UNICODE_PATTERN.matcher(str);
        boolean ignore = trimMatcher.matches(); // always true but must be called since it does the actual matching/grouping
        System.out.println("ignore is " + ignore);
        return SPLIT_SPACE_UNICODE_PATTERN.split(trimMatcher.group(1));
    }
	
	public List<Person> listAll(long userId, String keyword) {
		if(keyword != null) {
			//split the keyword into an array of strings and run 
			//the search query for every string in the array
			//then add it to an arraylist as a return;
			//also check if there is a date and convert it to mysql necessities
			System.out.println("keyword is here " + keyword);
			
			
			
			List<Person> allResults = new ArrayList<>();
			String[] trimAndSplitUnicode = keyword.trim().split(" ");
			
			
			//			String[] trimAndSplitUnicode = trimSplitUnicodeBySpace(keyword);
			//System.out.println("trimAndSplitUnicode : [ " + Arrays.stream(trimAndSplitUnicode).collect(Collectors.joining("][")) + "]");
			//System.out.println("trimAndSplitUnicode : [ " + Arrays.asList(trimAndSplitUnicode));
			
			for(String s : trimAndSplitUnicode) {
				
				
				s = appDF.formatDate(s);					
				
				
				List<Person> eachKeyList =  persRepo.searchPersons(userId, s);
				System.out.println("eachKeyList is " + Arrays.asList(eachKeyList));
				if(!eachKeyList.isEmpty()) {					
					allResults.addAll(eachKeyList);
					
				}
			}

		
			return allResults;
		}
		
		System.out.println("results outside of keyword somehow");
		return (List<Person>) persRepo.findAll();
	}
	
	public List<Person> listAllNoLimit(long userId, String keyword) {
		if(keyword != null) {
			
			List<Person> allResults = new ArrayList<>();
			String[] trimAndSplitUnicode = keyword.trim().split(" ");
			
			for(String s : trimAndSplitUnicode) {
				
				
				s = appDF.formatDate(s);	
				
				List<Person> eachKeyList =  persRepo.searchPersonsNoLimit(userId, s);
				System.out.println("eachKeyList is " + Arrays.asList(eachKeyList));
				if(!eachKeyList.isEmpty()) {					
					allResults.addAll(eachKeyList);
					
				}
			}

		
			return allResults;
			
			
		}
		
		return (List<Person>) persRepo.findAll();
	}

	public List<Person> selectThreeMoreClients(long personId) {
		// TODO Auto-generated method stub
		return persRepo.selectThreeMoreClients(personId);
	}

	public Person findNextDoctorAvailable(LocalDate theDate, LocalTime theTime) {
		
		Person nextDoctor = new Person();
		String theDate2 = theDate.toString();
		String theTime2 = theTime.toString();
				
		
		List<Long> bookedDoctorsIds = (List<Long>) persRepo.findBookedDoctorIds(theDate2, theTime2);
		System.out.println("bookedDoctorsIds===> " + Arrays.asList(bookedDoctorsIds));
		List<Long> allDoctorsIds =  (List<Long>) userRepo.getListOfUsersIdsOfType("DOCTOR"); 
		int timeExists = 0;
		

		
		for(int i = 0; i < allDoctorsIds.size(); i++) {
			long checkId = allDoctorsIds.get(i);
			System.out.println("checked Id is ===> " + checkId);
			
			if(!bookedDoctorsIds.contains(checkId)) {
				System.out.println("doctor in the find --- > " + allDoctorsIds.get(i));
				nextDoctor = persRepo.findPersonByPersonId(allDoctorsIds.get(i));
				timeExists = 1;
				System.out.println("doctorul ales --> " + nextDoctor.getPersonId());
				return nextDoctor;
			}
						
		}
		
		//if it didn't find and I it means the hour is full. 
		//so we add one hour to the time to check 
		//maybe it's a while loop needed here
		
		
		while(timeExists < 1) {
			LocalTime nextTime = theTime.plusHours(1);	//the time to check	
			
			
			LocalTime timeLimit = LocalTime.of(18, 0,0); //the time limit

			if(nextTime.compareTo(timeLimit) != 0) {
				
				findNextDoctorAvailable(theDate, nextTime);
				
			} else {
				
				//search 11... search 12... search 14... search 18 ... goes to else
				//increases the day and resets to 9 
				//goes to search and searches 9 and theDate(which is nextDate)...search ...18... 
				//goes to else increase the day and searches 9 with the date increased.
				
				nextTime = LocalTime.of(8, 0, 0);//reset the time
				theDate = theDate.plusDays(1);
			}

		}
		
		return nextDoctor; 
	}
	
}
