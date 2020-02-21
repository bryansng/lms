package ie.ucd.lms.entity;

import javax.persistence.*;


@Entity
@Table(name = "login")
public class Login {

	public Login() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String email;
	private String hash;

	@OneToOne(optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "email", nullable = false, referencedColumnName = "email")
	private Member member;

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

	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}