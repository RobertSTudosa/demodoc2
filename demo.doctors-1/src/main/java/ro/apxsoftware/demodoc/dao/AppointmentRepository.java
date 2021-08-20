package ro.apxsoftware.demodoc.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ro.apxsoftware.demodoc.entities.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	
	public Appointment findAppointmentByDate(Date date);
	
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment WHERE appointment_token = ?1 ;")
	public Appointment appointmentByToken(String appointmentToken); 
	
//	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
//					+ " and appointment.date > addtime(now(), '0 7:0:0.000000') "
//					+ " order by appointment.date ASC ;")
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and appointment.date >= date(now()) "
			+ " and appointment.canceled = 0 "
			+ " order by appointment.date ASC ;")
	public List<Appointment> appointmentByDoctorIdAndCurrentDateNotCanceled(long doctorId);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and appointment.date = ?2 "
			+ " and appointment.canceled = 0 "
			+ " order by appointment.date ASC ;")
	public List<Appointment> appointmentByDoctorIdAndDateNotCanceled(long doctorId, String date);
	
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment "
			+ " WHERE appointment.date >= date(now()) "
			+ " and appointment.canceled = 0 "
			+ " order by appointment.date ASC ;")
	public List<Appointment> getAllAppointmentsAfterCurrentDateNotCanceled();
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment "
			+ " WHERE appointment.date = ?1 "
			+ " and appointment.canceled = 0 ; ")
	public List<Appointment> getAllAppointmentsByDateNotCanceled(String date);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and appointment.date >= date(now()) "
			+ " and Month(appointment.date) = MONTH(CURRENT_DATE()) "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 0 "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getAppointmentByDoctorIdAndCurrentMonthNotCanceled (long doctorId);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment "
			+ " where appointment.date >= date(now()) "
			+ " and Month(appointment.date) = MONTH(CURRENT_DATE()) "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 0 "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getAppointmentByCurrentMonthNotCanceled ();
	
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where pacient_person_id = ?1 "
			+ " and Month(appointment.date) = ?2 "
			+ " and appointment.date >= date(now()) "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getFutureAppointmentsByPersonIdAndCurrentMonth (long personId, String month);
	
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where pacient_person_id = ?1 "
			+ " and Month(appointment.date) = ?2 "
			+ " and appointment.date <= date(now()) "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getPastAppointmentsByPersonIdAndCurrentMonth (long personId, String month);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where pacient_person_id = ?1 "
			+ " and Month(appointment.date) = ?2 "
			+ " and appointment.canceled = 1 "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getCanceledAppointmentsByPersonIdAndCurrentMonth (long personId, String month);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and Month(appointment.date) = ?2 "
			+ " and appointment.date <= date(now()) "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getPastAppointmentsByDoctorIdAndCurrentMonth (long doctorId, String month);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and Month(appointment.date) = ?2 "
			+ " and appointment.date <= date(now()) "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 1 "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getPastAppointmentsByDoctorIdAndCurrentMonthAndCanceled(long userId, String month);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and Month(appointment.date) = ?2 "
			+ " and appointment.date >= date(now()) "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getFutureAppointmentsByDoctorIdAndCurrentMonth (long doctorId, String month);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where pacient_person_id = ?1 "
			+ " and appointment.date >= date(now())  "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 0 "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getAppointmentByPersonIdAndCurrentMonthNotCanceled (long personId);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where pacient_person_id = ?1 "
			+ " and appointment.date <= date(now()) "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getAppointmentByPersonIdAndUpToCurrentDate (long personId);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and appointment.date <= date(now()) "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getAppointmentByDoctorIdAndUpToCurrentDate (long doctorId);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment  "
			+ " where appointment.date <= date(now()) "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getAppointmentAndUpToCurrentDate ();
	
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and appointment.date <= date(now()) "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 1 "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getAppointmentByDoctorIdAndUpToCurrentDateAndCanceled (long doctorId);
	
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment   "
			+ " where appointment.date <= date(now()) "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 1 "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getAppointmentAndUpToCurrentDateAndCanceled ();
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and Month(appointment.date) = ?2 "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getAppointmentByDoctorIdAndMonth (long doctorId, String month);
	
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where pacient_person_id = ?1 "
			+ " and Month(appointment.date) = ?2 "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getAppointmentByPersonIdAndMonth (long doctorId, String month);
	
	
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and appointment.date >= date(now()) "
			+ " and Month(appointment.date) = ?2 "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 0 "
			+ " order by appointment.date ASC ;")
	public List<Appointment> getFutureAppointmentByDoctorIdAndMonthNoLimit (long doctorId, String month);
	
	
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and appointment.date >= date(now()) "
			+ " and Month(appointment.date) = ?2 "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 1 "
			+ " order by appointment.date ASC ;")
	public List<Appointment> getFutureAppointmentByDoctorIdAndMonthNoLimitAndCanceled (long doctorId, String month);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and appointment.date <= date(now()) "
			+ " and Month(appointment.date) = ?2 "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 0 "
			+ " order by appointment.date ASC ;")
	public List<Appointment> getPastAppointmentByDoctorIdAndMonthNoLimit (long doctorId, String month);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where doctor_person_id = ?1 "
			+ " and appointment.date <= date(now()) "
			+ " and Month(appointment.date) = ?2 "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 1 "
			+ " order by appointment.date ASC ;")
	public List<Appointment> getPastAppointmentByDoctorIdAndMonthNoLimitAndCanceled (long doctorId, String month);
	
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where pacient_person_id = ?1 "
			+ " and appointment.date >= date(now()) "
			+ " and Month(appointment.date) = ?2 "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 0 "
			+ " order by appointment.date ASC ;")
	public List<Appointment> getFutureAppointmentByPersonIdAndMonthNoLimit (long personId, String month);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where pacient_person_id = ?1 "
			+ " and appointment.date >= date(now()) "
			+ " and Month(appointment.date) = ?2 "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 1 "
			+ " order by appointment.date ASC ;")
	public List<Appointment> getFutureAppointmentByPersonIdAndMonthNoLimitAndCanceled (long personId, String month);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where pacient_person_id = ?1 "
			+ " and appointment.date <= date(now()) "
			+ " and Month(appointment.date) = ?2 "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 0 "
			+ " order by appointment.date ASC ;")
	public List<Appointment> getPastAppointmentByPersonIdAndMonthNoLimit (long personId, String month);
	
	@Query(nativeQuery= true, value= "SELECT * FROM appointment where pacient_person_id = ?1 "
			+ " and appointment.date <= date(now()) "
			+ " and Month(appointment.date) = ?2 "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 1 "
			+ " order by appointment.date ASC ;")
	public List<Appointment> getPastAppointmentByPersonIdAndMonthNoLimitAndCanceled (long personId, String month);
	

	@Query(nativeQuery= true, value= "SELECT * FROM appointment where pacient_person_id = ?1 "
			+ "   "
			+ " and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ " and appointment.canceled = 1 "
			+ " order by appointment.date ASC "
			+ " LIMIT 6 ;")
	public List<Appointment> getAppointmentByPersonIdAndCurrentMonthCanceled(long personId);


    @Query(nativeQuery= true, value= "SELECT * FROM appointment app "
    		+ " Left outer join companyserv on companyserv.appointment_appointment_id = app.appointment_id "
    		+ " WHERE  "
    		+ "	app.doctor_person_id = ?1 "
    		+ " and app.date > date(now()) "
    		+ " and CONCAT( app.pacient_email, ' ', app.pacient_name, ' ', companyserv.name  ) "
    		+ " LIKE %?2% "
    		+ " group by app.appointment_id "
    		+ " LIMIT 6 ; ")	
	public List<Appointment> searchAppointments(long userId, String keyword);
    
    
    @Query(nativeQuery= true, value= "SELECT * FROM appointment app "
    		+ " Left outer join companyserv on companyserv.appointment_appointment_id = app.appointment_id "
    		+ " WHERE  "
    		+ "	 "
    		+ " and app.date > date(now()) "
    		+ " and CONCAT( app.pacient_email, ' ', app.pacient_name, ' ', companyserv.name  ) "
    		+ " LIKE %?1% "
    		+ " group by app.appointment_id "
    		+ " LIMIT 6 ; ")	
	public List<Appointment> searchAdminAppointments(String keyword);
    

    @Query(nativeQuery= true, value= "SELECT * FROM appointment app "
    		+ " Left outer join companyserv on companyserv.appointment_appointment_id = app.appointment_id "
    		+ " WHERE  "
    		+ "	app.doctor_person_id = ?1 "
    		+ " and CONCAT(app.pacient_email, ' ', app.pacient_name, ' ',  companyserv.name  ) "
    		+ " LIKE %?2% "
    		+ " group by app.appointment_id "
    		+ " order by app.date ASC ; ")	
	public List<Appointment> searchAppointmentsNoLimit(long userId, String keyword);
    
    
    @Query(nativeQuery= true, value= "    SELECT * FROM appointment app "
    		+ "	 Left outer join companyserv on companyserv.appointment_appointment_id = app.appointment_id "
    		+ "	 WHERE app.doctor_person_id = ?1 "
    		+ "	 and app.date = ?2 "
    		+ "	 group by app.appointment_id "
    		+ "	 order by app.date ASC "
    		+ "  LIMIT 6 ; ")	
	public List<Appointment> searchAppointmentsByDate(long userId, String keyword);
    
    
    @Query(nativeQuery= true, value= "    SELECT * FROM appointment app "
    		+ "	 Left outer join companyserv on companyserv.appointment_appointment_id = app.appointment_id "
    		+ "	 WHERE "
    		+ "	 and app.date = ?1 "
    		+ "	 group by app.appointment_id "
    		+ "	 order by app.date ASC "
    		+ "  LIMIT 6 ; ")	
	public List<Appointment> searchAdminAppointmentsByDate(String keyword);
    
    @Query(nativeQuery= true, value= "    SELECT * FROM appointment app "
    		+ "	 Left outer join companyserv on companyserv.appointment_appointment_id = app.appointment_id "
    		+ "	 WHERE app.doctor_person_id = ?1 "
    		+ "	 and app.date = ?2 "
    		+ "	 group by app.appointment_id ")	
	public List<Appointment> searchAppointmentsByDateNoLimit(long userId, String keyword);


    @Query(nativeQuery = true, value="select * from appointment where doctor_person_id = ?1 "
    		+ " and appointment.date > date(now()) "
    		+ " order by appointment.date ASC "
    		+ " limit 1 ;")
    public Appointment findNextAppointmentByDoctorId(long personId);

    @Query(nativeQuery = true, value="select * from appointment where pacient_person_id = ?1 "
    		+ " and appointment.date > date(now()) "
    		+ " where pacient_person_id = ?1 "
    		+ " order by appointment.date ASC "
    		+ " limit 1 ;")
	public Appointment findNextAppointmentByPacientId(long personId);

    @Query(nativeQuery= true, value= "    SELECT * FROM appointment app "
    		+ "	 Left outer join companyserv on companyserv.appointment_appointment_id = app.appointment_id "
    		+ "	 WHERE app.doctor_person_id = ?1 "
    		+ "	 and app.pacient_phone like %?2% "
    		+ "	 group by app.appointment_id "
    		+ " Limit 6 ;")	
	public List<Appointment> searchAppointmentsByNumber(long userId, String s);
    
    @Query(nativeQuery= true, value= "    SELECT * FROM appointment app "
    		+ "	 Left outer join companyserv on companyserv.appointment_appointment_id = app.appointment_id "
    		+ "	 WHERE  "
    		+ "	 and app.pacient_phone like %?1% "
    		+ "	 group by app.appointment_id "
    		+ " Limit 6 ;")	
	public List<Appointment> searchAdminAppointmentsByNumber(String s);

    @Query(nativeQuery= true, value= "    SELECT * FROM appointment app "
    		+ "	 Left outer join companyserv on companyserv.appointment_appointment_id = app.appointment_id "
    		+ "	 WHERE app.doctor_person_id = ?1 "
    		+ "	 and app.pacient_phone = ?2 "
    		+ "	 group by app.appointment_id ;")	
	public List<Appointment> searchAppointmentsByNumberNoLimit(long userId, String s);

    @Query(nativeQuery= true, value= "    SELECT * FROM appointment app "
    		+ "	 Left outer join companyserv on companyserv.appointment_appointment_id = app.appointment_id "
    		+ "	 WHERE app.pacient_person_id = ?1 "
    		+ "	 and temporary = 1 "
    		+ "	 group by app.appointment_id ;")	
	public List<Appointment> searchAppointmentsByPacientAndTemporary(long userId);

    @Query(nativeQuery= true, value= " SELECT count(number_of_clients) from "
    		+ " (SELECT count(appointment.pacient_name) as number_of_clients FROM bpeople_demo_doctors.appointment "
    		+ " group by pacient_name) as sum;   ")	
	public int getNumberOfClients();

    @Query(nativeQuery= true, value= "select count(count)  from "
    		+ " (select count(appointment.appointment_id) as count from appointment "
    		+ " where appointment.date < date(now())  "
    		+ " and appointment.canceled = 0 "
    		+ " group by appointment_id) as mCount;   ")	
	public int getTotalAppointmentsNotCanceledPast();

    @Query(nativeQuery= true, value= "select count(count)  from "
    		+ " (select count(appointment.appointment_id) as count from appointment "
    		+ " where appointment.canceled = 1 \r\n"
    		+ " group by appointment_id) as mCount;  ")	
	public int getTotalCanceledAppointments();

    @Query(nativeQuery= true, value= " select * from appointment where "
    		+ " appointment.date > date(now()) "
    		+ " order by appointment.date ASC "
    		+ " limit 1 ;  ")	
	public Appointment findNextAppointment();

    
	@Query(nativeQuery= true, value= "(SELECT * FROM appointment where doctor_person_id = ?1 "
			+ "			 and appointment.date >= date(now()) "
			+ "			 and Month(appointment.date) = ?2 "
			+ "			 and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ "             and appointment_id <= ?3 "
			+ "			 and appointment.canceled = 0 "
			+ "			 order by appointment.date ASC) "
			+ "	UNION "
			+ " (SELECT * FROM appointment where doctor_person_id = ?1 "
			+ "			 and appointment.date >= date(now()) "
			+ "			 and Month(appointment.date) = ?2 "
			+ "			 and YEAR(appointment.date) = YEAR(CURRENT_DATE()) "
			+ "             and appointment_id > ?3 "
			+ "			 and appointment.canceled = 0 "
			+ "			 order by appointment.date ASC "
			+ "             limit 3)    ")
	public List<Appointment> getSixMoreFutureAppointmentsByDoctorIdByMonthByLastAppId(long userId, String month, long lastAppId);


	
}
