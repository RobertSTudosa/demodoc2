package ro.apxsoftware.demodoc.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.tomcat.util.codec.binary.Base64;

@Entity
@Table(name="profileImg")
public class ProfileImg implements Serializable {
	

	@Id()
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pics_generator")
	@SequenceGenerator(name = "pics_generator", sequenceName = "pics_seq", allocationSize = 1)
	@Column(name="pic_id")
	private long picId;
	
	private String picName;
	private String picType;
	

	
	@Lob
	private byte[] data;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	private Person person;
	
	

	
	public ProfileImg () {
		
	}

	public ProfileImg(String picName, String picType, byte[] data) {
		super();
		this.picName = picName;
		this.picType = picType;
		this.data = data;
		
	}
	
	

	public long getPicId() {
		return picId;
	}

	public void setPicId(long picId) {
		this.picId = picId;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicType() {
		return picType;
	}

	public void setPicType(String picType) {
		this.picType = picType;
	}
	
	public String generateBase64Image(){
	    return Base64.encodeBase64String(this.getData());
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		result = prime * result + (int) (picId ^ (picId >>> 32));
		result = prime * result + ((picName == null) ? 0 : picName.hashCode());
		result = prime * result + ((picType == null) ? 0 : picType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProfileImg other = (ProfileImg) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		if (picId != other.picId)
			return false;
		if (picName == null) {
			if (other.picName != null)
				return false;
		} else if (!picName.equals(other.picName))
			return false;
		if (picType == null) {
			if (other.picType != null)
				return false;
		} else if (!picType.equals(other.picType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProfileImg [picId=" + picId + ", picName=" + picName + ", picType=" + picType + ", person=" + person
				+ "]";
	}

		
	
		
	

}
