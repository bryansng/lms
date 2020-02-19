package ie.ucd.lms.controller;

import javax.persistence.*;

@Entity
@Table(name = "login")
public class Login {
	private String email;
	private String hash;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}