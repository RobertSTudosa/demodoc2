package ro.apxsoftware.demodoc.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.apxsoftware.demodoc.entities.Appointment;

@Service
public class DatesAndTimeService {
	
	@Autowired
	UserService userServ;
	
	@Autowired
	AppointmentService appServ;
	
	@ResponseBody
	public List<String> getAllBusyDates (Model model, RedirectAttributes redirAttr) {
		

				int doctorsCount = userServ.getHowManyUserOfType("DOCTOR"); //get the number of doctors in the app 
				
				List<Long> doctorsIds = userServ.getListOfUsersIdsOfType("DOCTOR"); //get a list with docs ids 
				
				List<String> allBusyDates = new ArrayList<>();
					 
					
					List<Appointment> appointments = appServ.getAppointmentsAfterCurrentDateNotCanceled();
					System.out.println("appointments size = " + appointments.size());
				
					List<LocalDate> allDates = new ArrayList<>();
					
					List<LocalDate> datesToCheck = new ArrayList<>();
					
					List<LocalDate> busyDates = new ArrayList<>();
					
					//add all dates to an array
					for (int x = 0; x < appointments.size(); x++) {

						allDates.add(appointments.get(x).getDate());

					}
					
					//if one date is repeating more than one in the allDates then we add it
					//if it is NOT contained already
					for (int i = 0; i < allDates.size(); i++) {
						if(Collections.frequency(allDates, allDates.get(i)) >= 1) {
							if(!datesToCheck.contains(allDates.get(i))) {
								datesToCheck.add(allDates.get(i));
	
							}
						}
					}
					
					System.out.println("dates to check " + datesToCheck.toString());
					
					
					
					for(int i = 0; i < datesToCheck.size(); i++) {
						List<LocalTime> fixedTimeOfTheDay = getFixedTimesByDate(datesToCheck.get(i).toString(), model, redirAttr);
						System.out.println("fixedTimes for date : " + datesToCheck.get(i) + " are : " + fixedTimeOfTheDay.toString());
						if(fixedTimeOfTheDay.isEmpty()) {
							busyDates.add(datesToCheck.get(i));
						}
					}
					

					 //build a list of strings for javascript 	
					 List<String> stringBusyDates = new ArrayList<>();
					 for(int i = 0; i < busyDates.size(); i++) {
						 stringBusyDates.add(busyDates.get(i).toString());
					 }
					 
				
					 allBusyDates.addAll(stringBusyDates);
					 
				
					 System.out.println("all busy dates are ===> " + allBusyDates);
			
				return allBusyDates;
	}
	
	@ResponseBody
	public List<LocalTime> getFixedTimesByDate (@RequestParam("date") String date, Model model, RedirectAttributes redirAttr) {
		
		
		int doctorsCount = userServ.getHowManyUserOfType("DOCTOR"); // get the number of doctors in the app

		List<Long> doctorsIds = userServ.getListOfUsersIdsOfType("DOCTOR"); // get a list with docs ids
		
		List<LocalTime> allTimes = new ArrayList<>(); // prepare an arraylist for all times;
		
		List<LocalTime> noTimes = new ArrayList<>(); // prepare an arraylist to be sent to model
		
		// prepare a list of LocalTime as standard
		List<LocalTime> fixedTimes = new ArrayList<LocalTime>() {
						{
							add(LocalTime.of(9, 0, 0, 0));
							add(LocalTime.of(10, 0, 0, 0));
							add(LocalTime.of(11, 0, 0, 0));
							add(LocalTime.of(12, 0, 0, 0));
							add(LocalTime.of(14, 0, 0, 0));
							add(LocalTime.of(15, 0, 0, 0));
							add(LocalTime.of(16, 0, 0, 0));
							add(LocalTime.of(17, 0, 0, 0));
							add(LocalTime.of(18, 0, 0, 0));

						}
					};

		

			

			System.out.println("calling the time by date=================");

			
			List<Appointment> appointBydDate = appServ.getAllAppointmentsByDateNotCanceled(date);
			System.out.println("appointments retrieved --->" + appointBydDate.toString());
			// collect all LocalTimes from the result and subtract from the prepared
			// LocalTime list
			for (int i = 0; i < appointBydDate.size(); i++) {
				System.out.println("appointment time to add to allTimes " + appointBydDate.get(i).getAppointmentTime());
				
				allTimes.add(appointBydDate.get(i).getAppointmentTime());
			}


			
			

	
		
		System.out.println("print all times ----> " + allTimes.toString());
		
		for(int i = 0; i < allTimes.size(); i ++) {
			
			if(Collections.frequency(allTimes, allTimes.get(i)) == doctorsCount) {
								
				if(!noTimes.contains(allTimes.get(i))) {
					noTimes.add(allTimes.get(i));
					fixedTimes.remove(allTimes.get(i));

				}
			}
			
		}
		
		
		List<LocalTime> newFixedTimes = new ArrayList<> ();
		
		for(int i = 0; i < fixedTimes.size(); i++ ) {
			newFixedTimes.add(fixedTimes.get(i));
		}
		
		model.addAttribute("fixedTimes", fixedTimes);		
		
		System.out.println("no Times ===> " + noTimes.toString());
		System.out.println("newFixedTimes ===> " + newFixedTimes.toString());
		System.out.println(" date checked was ===> " + date);
		System.out.println("fixed Times ===> " + fixedTimes.toString());
		
		return newFixedTimes;
	}
	
	
	@ResponseBody
	public List<LocalTime> getFixedTimesByDateByDoctor (@RequestParam("date") String date, @RequestParam("doctor") long doctorId, Model model, RedirectAttributes redirAttr) {
		
		int doctorsCount = userServ.getHowManyUserOfType("DOCTOR"); // get the number of doctors in the app
		
		List<LocalTime> theFixedTimes = new ArrayList<>(); // this will be the resulted array
		
		//prepare a list of LocalTime as standard
		List<LocalTime> fixedTimes = new ArrayList<LocalTime>() {{
			 add(LocalTime.of(9,0,0,0));
			 add(LocalTime.of(10,0,0,0));
			 add(LocalTime.of(11,0,0,0));
			 add(LocalTime.of(12,0,0,0));
			 add(LocalTime.of(14,0,0,0));
			 add(LocalTime.of(15,0,0,0));
			 add(LocalTime.of(16,0,0,0));
			 add(LocalTime.of(17,0,0,0));
			 add(LocalTime.of(18,0,0,0));
			 
			 
		 }};

		System.out.println("calling the time by date=================");
		

		List<Appointment> appointByDate = appServ.getAppointmentsByDoctorIdandDateNotCanceled(doctorId, date);
		
		//collect all LocalTimes from the result and subtract from the prepared LocalTime list
		for(int i = 0; i < appointByDate.size(); i++) {
			System.out.println("appointment time to remove " + appointByDate.get(i).getAppointmentTime());
			fixedTimes.remove(appointByDate.get(i).getAppointmentTime());
		}

		model.addAttribute("fixedTimes", fixedTimes);
		
		System.out.println(appointByDate.toString());
		System.out.println("these are the fixedTimes printed ---> " + fixedTimes.toString());
		
		
		//run getFixedTimesByDate then cross with doctor's fixed times. 
		//allFixedTimes will always be an array equal to 9 or less(from start hour to end)
		List<LocalTime> allFixedTimes = this.getFixedTimesByDate(date, model, redirAttr);
		List<LocalTime> addAllTimes = new ArrayList<>();
		System.out.println("allFixedTimes ===>" + allFixedTimes.toString());
		addAllTimes.addAll(allFixedTimes);
		System.out.println("after adding allFixedTimes ran from getTimesByDate addAllTimes is " + addAllTimes.toString());
		addAllTimes.addAll(fixedTimes);
		System.out.println("after adding local fixedTimes addAllTimes is " + addAllTimes.toString());
		
		
		//check with 2 because of the allFixedTimes array
		for(int i = 0; i < addAllTimes.size(); i++) {
			if(Collections.frequency(addAllTimes, addAllTimes.get(i)) == 2) {
				
				if(!theFixedTimes.contains(addAllTimes.get(i))) {
					theFixedTimes.add(addAllTimes.get(i));					
				}

			}
		}
		
		System.out.println("theFixedTimes is the returning array ==> " + theFixedTimes.toString());
		
		
		return theFixedTimes;
	}
	
	public LocalDate formatToLocalDate (CharSequence date) {
		
		DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE;
		LocalDate theDate = LocalDate.parse(date, dtf);
		System.out.println("the date is the date --- > " + theDate);
		
		return theDate;
	}

}
