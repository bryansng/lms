package ie.ucd.lms.entity;

import javax.persistence.*;
import java.time.*;
import java.util.Set;

@Entity
@Table(name = "members")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;
	private String fullName;
	private String mobileNumber;
	private String address;
	private LocalDateTime bornOn;
	private LocalDateTime joinedOn = LocalDateTime.now();
	private LocalDateTime lastActiveOn;
	private String bio;
	private String type = "member";

	@OneToMany(mappedBy = "member")
	private Set<LoanHistory> loanHistories;

	@OneToMany(mappedBy = "member")
	private Set<ReserveQueue> reserveQueues;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		String buf = " - ";
		return id + buf + fullName + buf + email + buf + mobileNumber + buf + address + buf + type;
	}
}