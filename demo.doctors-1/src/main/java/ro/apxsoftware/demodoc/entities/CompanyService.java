package ro.apxsoftware.demodoc.entities;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="companyserv")
public class CompanyService {
	
	@Id()
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "companyserv_generator")
	@SequenceGenerator(name = "companyserv_generator", sequenceName = "companyserv_seq", allocationSize = 1)
	@Column(name="companyserv_id")
	private long companyservId;
	
	private String name;
	
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	private Appointment appointment;
	
	@Column(name="duration")
	private LocalTime duration;

	public CompanyService() {
		
	}

	public CompanyService(String name, Appointment appointement, LocalTime duration) {
		this.appointment = appointment;
		this.name = name;
		this.duration = duration;
	}

	public long getCompanyservId() {
		return companyservId;
	}

	public void setCompanyservId(long companyservId) {
		this.companyservId = companyservId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name + " ";
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	
	
	
	public LocalTime getDuration() {
		return duration;
	}

	public void setDuration(LocalTime duration) {
		this.duration = duration;
	}

		//overridden methods
		@Override
		public int hashCode() {
			return getClass().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if(this == obj) return true;
			if( !(obj instanceof CompanyService)) return false;
			
			CompanyService coServ = (CompanyService) obj;
			
			if(!getName().equals(coServ.getName())) return false;
			// TODO Auto-generated method stub
			return companyservId != 0L && companyservId== coServ.getCompanyservId();
		}

		@Override
		public String toString() {
			return name;
		}



		

		
	
	
	
	
}
