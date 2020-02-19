package ie.ucd.lms.controller;

import javax.persistence.*;
import java.time.*;

@Entity
@Table(name = "members")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String fullName;
	private String mobileNumber;
	private LocalDateTime bornOn;
	private LocalDateTime joinedOn = LocalDateTime.now();
	private LocalDateTime lastActiveOn;
	private String bio;
	private Boolean isLibrarian = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public LocalDateTime getBornOn() {
		return bornOn;
	}

	public void setBornOn(LocalDateTime bornOn) {
		this.bornOn = bornOn;
	}

	public LocalDateTime getJoinedOn() {
		return joinedOn;
	}

	public void setJoinedOn(LocalDateTime joinedOn) {
		this.joinedOn = joinedOn;
	}

	public LocalDateTime getLastActiveOn() {
		return lastActiveOn;
	}

	public void setLastActiveOn(LocalDateTime lastActiveOn) {
		this.lastActiveOn = lastActiveOn;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public Boolean getIsLibrarian() {
		return isLibrarian;
	}

	public void setIsLibrarian(Boolean isLibrarian) {
		this.isLibrarian = isLibrarian;
	}
}