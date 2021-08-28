package ro.apxsoftware.demodoc.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="appointment")
public class Appointment implements Serializable {
	
	@Id()
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_generator")
	@SequenceGenerator(name = "appointment_generator", sequenceName = "appointment_seq", allocationSize = 1)
	@Column(name="appointment_id")
	private long appointmentId;
	
	@NotBlank
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd")
//	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;
	
//	@Temporal(TemporalType.TIME)
	@NotBlank
	@DateTimeFormat(iso = ISO.TIME)
	private LocalTime appointmentTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private Date dateCreated = new Date(); 
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	private Person doctor;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	private Person pacient;
	
	
	@OneToMany(mappedBy="appointment" ,
			cascade= {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REMOVE})
	private Set<CompanyService> companyServices = new HashSet<>();
	
	 @Column(name="appointment_token")
	    private String appointmentToken=UUID.randomUUID().toString();
	 
	 @NotBlank
	 @Size(min=3, max=50)
	 private String pacientName;
	 
	 @NotBlank
	 @Email(message="Introduceti o adresa valida")
	 @Size(min=11, max=50)
	 private String pacientEmail;
	 
	 @NotBlank
	 private String pacientPhone;
	 
	 @Column(name="canceled_id")
	 private long canceledId;
	 
	 @Column(name="rescheduled_id")
	 private long rescheduledId;
	 
	 @Column(name="canceled")
	 private boolean canceled;
	 
	 @Column(name="rescheduled")
	 private boolean rescheduled; 
	 
	 @Column(name="temporary")
	 private boolean temporary;
	 
	 @ElementCollection
	 private List<LocalTime> fixedTimes = new ArrayList<LocalTime>() {{
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
	 
	 @Size(min=2, max=4000)
	private String message;
	 
	 

	public Appointment() {
		
	}



	public Appointment(LocalDate date, Person doctor, Person pacient, String pacientName, String pacientEmail, String pacientPhone, String message) {
		
		this.date = date;
		this.appointmentToken=UUID.randomUUID().toString();
		this.dateCreated = new Date();
		this.pacientName = pacientName;
		this.pacientEmail = pacientEmail;
		this.pacientPhone = pacientPhone;
		this.message = message;
		
		
		
	}
	
	
	public Appointment(LocalDate date, LocalTime appointmentTime, Date dateCreated, Person doctor, Person pacient,
			Set<CompanyService> companyServices, String appointmentToken, String pacientName, String pacientEmail,
			String pacientPhone, long canceledId, long rescheduledId, boolean canceled, boolean rescheduled,boolean temporary,
			List<LocalTime> fixedTimes) {
		super();
		this.date = date;
		this.appointmentTime = appointmentTime;
		this.dateCreated = dateCreated;
		this.doctor = doctor;
		this.pacient = pacient;
		this.companyServices = companyServices;
		this.appointmentToken = appointmentToken;
		this.pacientName = pacientName;
		this.pacientEmail = pacientEmail;
		this.pacientPhone = pacientPhone;
		this.canceledId = canceledId;
		this.rescheduledId = rescheduledId;
		this.canceled = canceled;
		this.rescheduled = rescheduled;
		this.temporary = temporary;
		this.fixedTimes = fixedTimes;
	}

	public long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Person getDoctor() {
		return doctor;
	}

	public void setDoctor(Person doctor) {
		this.doctor = doctor;
	}

	public Person getPacient() {
		return pacient;
	}

	public void setPacient(Person pacient) {
		this.pacient = pacient;
	}
	
	public String getAppointmentToken() {
		return appointmentToken;
	}

	public void setAppointmentToken(String appointmentToken) {
		this.appointmentToken = appointmentToken;
	}
	
	
	public Date getDateCreated() {
			return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	

	public String getPacientName() {
		return pacientName;
	}

	public void setPacientName(String pacientName) {
		this.pacientName = pacientName;
	}

	public String getPacientEmail() {
		return pacientEmail;
	}

	public void setPacientEmail(String pacientEmail) {
		this.pacientEmail = pacientEmail;
	}

	public String getPacientPhone() {
		return pacientPhone;
	}

	public void setPacientPhone(String pacientPhone) {
		this.pacientPhone = pacientPhone;
	}
	
	


	public Set<CompanyService> getCompanyServices() {
		return companyServices;
	}
	
	//generate a String in the form of row list
	public String getStringCompanyServicesNames() {
		List<String> list = new ArrayList<>();
		for(CompanyService coServ : this.getCompanyServices()) {
			list.add(coServ.getName());
		}
		//used to generate a String version of the collection 
		return list.stream().map(Object::toString).collect(Collectors.joining(""+"," + ""));
	}

	public void setCompanyServices(Set<CompanyService> companyServices) {
		this.companyServices = companyServices;
	}
	
	

	public LocalTime getAppointmentTime() {
		if(this.appointmentTime != null) {
			return this.appointmentTime.minusHours(2);			
		}
		return null;

	}

	public void setAppointmentTime(LocalTime time) {
		this.appointmentTime = time.plusHours(2);
	}
	
	public void removeAppointmentFixedTime(LocalTime time) {
		this.fixedTimes.remove(time);
	}
	
	public void addAppointmentFixedTime(LocalTime time) {
		this.fixedTimes.add(time);
	}
	

	public List<LocalTime> getFixedTimes() {
		return fixedTimes;
	}



	public void setFixedTimes(List<LocalTime> fixedTimes) {
		this.fixedTimes = fixedTimes;
	}
	
	public String getDoctorName () {
		if (this.getDoctor() == null) {
			return null;
		}
		return this.getDoctor().getFirstName();
	}


	public long getCanceledId() {
		return canceledId;
	}



	public void setCanceledId(long canceledId) {
		this.canceledId = canceledId;
	}



	public long getRescheduledId() {
		return rescheduledId;
	}



	public void setRescheduledId(long rescheduledId) {
		this.rescheduledId = rescheduledId;
	}



	public boolean isCanceled() {
		return canceled;
	}



	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}



	public boolean isRescheduled() {
		return rescheduled;
	}



	public void setRescheduled(boolean rescheduled) {
		this.rescheduled = rescheduled;
	}



	public boolean isTemporary() {
		return temporary;
	}



	public void setTemporary(boolean temporary) {
		this.temporary = temporary;
	}

	

	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	//overridden methods
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if( !(obj instanceof User)) return false;
		
		Appointment app = (Appointment) obj;
		
		if(!getDoctor().getFirstName().equals(app.getDoctor().getFirstName())) return false;
		// TODO Auto-generated method stub
		return appointmentId != 0L && appointmentId == app.getAppointmentId();
	}

	@Override
	public String toString() {
		return "Appointment [appointmentId=" + appointmentId + ", date=" + date + ", time=" + appointmentTime + ", doctor=" + doctor + ", pacient="
				+ pacient + "]";
	}




	
	

}
