package ro.apxsoftware.demodoc.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="appointment")
public class Appointment implements Serializable {
	
	@Id()
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_generator")
	@SequenceGenerator(name = "appointment_generator", sequenceName = "appointment_seq", allocationSize = 1)
	@Column(name="appointment_id")
	private long appointmentId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date date; 
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private Date dateCreated = new Date(); 
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	private Person doctor;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	private Person pacient;
	
	
	@OneToMany(mappedBy="appointment" ,
			cascade= {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.PERSIST})
	private Set<CompanyService> companyServices = new HashSet<>();
	
	 @Column(name="appointment_token")
	    private String appointmentToken=UUID.randomUUID().toString();
	 
	 private String pacientName;
	 
	 private String pacientEmail;
	 
	 private String pacientPhone;
	 

	public Appointment() {
		
	}

	public Appointment(Date date, Person doctor, Person pacient, String pacientName, String pacientEmail, String pacientPhone) {
		
		this.date = date;
		this.appointmentToken=UUID.randomUUID().toString();
		this.dateCreated = new Date();
		this.pacientName = pacientName;
		this.pacientEmail = pacientEmail;
		this.pacientPhone = pacientPhone;
		
		
	}
	
	
	public long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
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

	public void setCompanyServices(Set<CompanyService> companyServices) {
		this.companyServices = companyServices;
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
		return "Appointment [appointmentId=" + appointmentId + ", date=" + date + ", doctor=" + doctor + ", pacient="
				+ pacient + "]";
	}
	
	

}
