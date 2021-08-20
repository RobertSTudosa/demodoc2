package ro.apxsoftware.demodoc.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import ro.apxsoftware.demodoc.entities.Person;
import ro.apxsoftware.demodoc.utils.AppDateFormater;

@Service
public class PersonService {
	
	@Autowired 
	PersonRepository persRepo;
	
	@Autowired
	AppDateFormater appDF;
	
	
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
	
}
