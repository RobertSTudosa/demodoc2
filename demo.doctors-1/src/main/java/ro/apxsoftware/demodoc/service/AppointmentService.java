package ro.apxsoftware.demodoc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.apxsoftware.demodoc.dao.AppointmentRepository;
import ro.apxsoftware.demodoc.entities.Appointment;
import ro.apxsoftware.demodoc.utils.AppDateFormater;

@Service
public class AppointmentService {
	
	@Autowired
	AppointmentRepository appRepo;
	
	@Autowired
	AppDateFormater appDF;
	
	public void saveApp(Appointment appointment) {
		appRepo.save(appointment);
	}
	
	public Appointment getAppointmentByToken(String token) {
		return appRepo.appointmentByToken(token);
	}
	
	public List<Appointment> getAppointmentsByDoctorIdandCurrentTimeNotCanceled(long doctorId) {
		return appRepo.appointmentByDoctorIdAndCurrentDateNotCanceled(doctorId);
	}
	
	public List<Appointment> getAppointmentsByDoctorIdandDateNotCanceled(long doctorId, String date) {
		return appRepo.appointmentByDoctorIdAndDateNotCanceled(doctorId, date);
	}
	
	public List<Appointment> getAppointmentsAfterCurrentDateNotCanceled(){
		return appRepo.getAllAppointmentsAfterCurrentDateNotCanceled();
	}
	
	public List<Appointment> getAllAppointmentsByDateNotCanceled(String date) {
		return appRepo.getAllAppointmentsByDateNotCanceled(date);
	}
	
	public List<Appointment> getAllAppointmentsByDoctorIdByCurrentMonthNotCanceled(long doctorId) {
		return appRepo.getAppointmentByDoctorIdAndCurrentMonthNotCanceled(doctorId);
	}
	
	public List<Appointment> getAllAppointmentsByPersonIdByCurrentMonthNotCanceled(long personId) {
		return appRepo.getAppointmentByPersonIdAndCurrentMonthNotCanceled(personId);
	}
	
	public List<Appointment> getAllAppointmentsByPersonIdAndUpToCurrentMonth(long personId) {
		return appRepo.getAppointmentByPersonIdAndUpToCurrentDate(personId);
	}
	
	public List<Appointment> getAllAppointmentsByDoctorIdAndUpToCurrentMonth(long doctorId) {
		return appRepo.getAppointmentByDoctorIdAndUpToCurrentDate(doctorId);
	}
	
	public List<Appointment> getAllAppointmentsByDoctorIdAndUpToCurrentMonthAndCanceled(long doctorId) {
		return appRepo.getAppointmentByDoctorIdAndUpToCurrentDateAndCanceled(doctorId);
	}
	
	public List<Appointment> getAllAppointmentsByDoctorIdByMonth(long doctorId, String month) {
		return appRepo.getAppointmentByDoctorIdAndMonth(doctorId, month);
	}
	
	public List<Appointment> getAllAppointmentsByPersonIdByMonth(long personId, String month) {
		return appRepo.getAppointmentByPersonIdAndMonth(personId, month);
	}
	
	public List<Appointment> getAllFutureAppointmentsByPersonIdByMonth(long personId, String month) {
		return appRepo.getFutureAppointmentsByPersonIdAndCurrentMonth(personId, month);
	}
	
	public List<Appointment> getAllPastAppointmentsByPersonIdByMonth(long personId, String month) {
		return appRepo.getPastAppointmentsByPersonIdAndCurrentMonth(personId, month);
	}
	
	public List<Appointment> getAllCanceledAppointmentsByPersonIdByMonth(long personId, String month) {
		return appRepo.getCanceledAppointmentsByPersonIdAndCurrentMonth(personId, month);
	}
	
	public List<Appointment> getAllPastAppointmentsByDoctorIdByMonth(long doctorId, String month) {
		return appRepo.getPastAppointmentsByDoctorIdAndCurrentMonth(doctorId, month);
	}
	
	public List<Appointment> getAllFutureAppointmentsByDoctorIdByMonth(long doctorId, String month) {
		return appRepo.getFutureAppointmentsByDoctorIdAndCurrentMonth(doctorId, month);
	}
	
	public List<Appointment> getAllFutureAppointmentsByDoctorIdByMonthNoLimit (long doctorId, String month) {
		return appRepo.getFutureAppointmentByDoctorIdAndMonthNoLimit(doctorId, month);
	}
	
	public List<Appointment> getAllFutureAppointmentsByDoctorIdByMonthNoLimitAndCanceled (long doctorId, String month) {
		return appRepo.getFutureAppointmentByDoctorIdAndMonthNoLimitAndCanceled(doctorId, month);
	}
	
	public List<Appointment> getAllPastAppointmentsByDoctorIdByMonthNoLimit (long doctorId, String month) {
		return appRepo.getPastAppointmentByDoctorIdAndMonthNoLimit(doctorId, month);
	}
	
	public List<Appointment> getAllPastAppointmentsByDoctorIdByMonthNoLimitAndCanceled (long doctorId, String month) {
		return appRepo.getPastAppointmentByDoctorIdAndMonthNoLimitAndCanceled(doctorId, month);
	}
	
	public List<Appointment> getAllFutureAppointmentsByPersonIdByMonthNoLimit (long personId, String month) {
		return appRepo.getFutureAppointmentByPersonIdAndMonthNoLimit(personId, month);
	}
	
	public List<Appointment> getAllFutureAppointmentsByPersonIdByMonthNoLimitAndCanceled (long personId, String month) {
		return appRepo.getFutureAppointmentByPersonIdAndMonthNoLimitAndCanceled(personId, month);
	}
	
	public List<Appointment> getAllPastAppointmentsByPersonIdByMonthNoLimit (long personId, String month) {
		return appRepo.getPastAppointmentByPersonIdAndMonthNoLimit(personId, month);
	}
	
	public List<Appointment> getAllPastAppointmentsByPersonIdByMonthNoLimitAndCanceled (long personId, String month) {
		return appRepo.getPastAppointmentByPersonIdAndMonthNoLimitAndCanceled(personId, month);
	}

	public List<Appointment> getAllAppointmentsByPersonIdAndUpToCurrentMonthCanceled(long personId) {
			
		return appRepo.getAppointmentByPersonIdAndCurrentMonthCanceled(personId);
	}

	public List<Appointment> getAllPastAppointmentsByDoctorIdByMonthAndCanceled(long userId, String month) {
		// TODO Auto-generated method stub
		return appRepo.getPastAppointmentsByDoctorIdAndCurrentMonthAndCanceled(userId, month);
		
	}
	
	public List<Appointment> listAll(long userId, String keyword) {
		if(keyword != null) {
			System.out.println("keyword is here " + keyword);
			
			List<Appointment> allResults = new ArrayList<>();
			List<Appointment> eachKeyList = new ArrayList<>();
			
			String[] trimAndSplitUnicode = keyword.trim().split(" ");
			
			for(String s : trimAndSplitUnicode) {
				System.out.println("s slice ==> " + s);
				
				if(appDF.checkIfDateOrNumber(s) == 1) {
					s = appDF.formatDate(s);
					eachKeyList =  appRepo.searchAppointmentsByDate(userId, s);
					System.out.println("====> is a date ===> " + Arrays.asList(eachKeyList));
				} else  if (appDF.checkIfDateOrNumber(s) == 2){ 
					
					eachKeyList = appRepo.searchAppointmentsByNumber(userId, s);
					System.out.println("====> is a number ===> " + Arrays.asList(eachKeyList));
					
				} else {
					
					eachKeyList =  appRepo.searchAppointments(userId, s);
					System.out.println("===> is a string ===> " + Arrays.asList(eachKeyList));
				}
				
				
				if(!eachKeyList.isEmpty()) {					
					
					allResults.addAll(eachKeyList);
					
				}
				
				
			}

		
			return allResults;
			
			
			
		}
		
		return (List<Appointment>) appRepo.findAll();
	}
	
	public List<Appointment> listAllNoLimit(long userId, String keyword) {
		if(keyword != null) {
			System.out.println("more keyword is here " + keyword);
			
			
			List<Appointment> allResults = new ArrayList<>();
			List<Appointment> eachKeyList = new ArrayList<>();
						
			String[] trimAndSplitUnicode = keyword.trim().split(" ");
			
			for(String s : trimAndSplitUnicode) {
				
				if(appDF.checkIfDateOrNumber(s) == 1) {
					s = appDF.formatDate(s);
					eachKeyList =  appRepo.searchAppointmentsByDateNoLimit(userId, s);
				} else  if (appDF.checkIfDateOrNumber(s) == 2){ 
					
					eachKeyList = appRepo.searchAppointmentsByNumberNoLimit(userId, s);

				} else {
					eachKeyList =  appRepo.searchAppointmentsNoLimit(userId, s);
				}
				
				System.out.println("eachKeyList is " + Arrays.asList(eachKeyList));
				if(!eachKeyList.isEmpty()) {					
					
					allResults.addAll(eachKeyList);
					
				}
			}

		
			return allResults;
			
			
		}
		
		return (List<Appointment>) appRepo.findAll();
	}

	public Appointment findNextAppointByDoctorId(long personId) {
		// TODO Auto-generated method stub
		return appRepo.findNextAppointmentByDoctorId(personId);
	}

	public Appointment findNextAppointByPacientId(long personId) {
		
		return appRepo.findNextAppointmentByPacientId(personId);
	}



}
