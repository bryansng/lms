package ie.ucd.lms.entity;

import ie.ucd.lms.service.Common;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.persistence.*;

@Entity
@Table(name = "loan_history")
public class LoanHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String isbn;

	@Column(name = "member_id", insertable = false, updatable = false)
	private Long memberId;
	private LocalDateTime issuedOn = LocalDateTime.now();
	private LocalDateTime returnOn;
	private LocalDateTime returnedOn;
	private LocalDateTime finedOn;
	private LocalDateTime lostOn;
	private Boolean wasLost = false;
	private BigDecimal fine;
	private String status;

	@ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "isbn", insertable = false, updatable = false)
	private Artifact artifact;

	@ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "id", insertable = false, updatable = false)
	private Member member;

	public void setAll(String isbn, String memberId, String returnOn, String fine, String status, Artifact artifact,
			Member member) {
		setIsbn(isbn);
		setMemberId(Common.convertStringToLong(memberId));
		setReturnOn(Common.convertStringDateToDateTime(returnOn));
		setFine(Common.convertStringToBigDecimal(fine));
		setStatus(status);
		setArtifact(artifact);
		setMember(member);
	}

	public Artifact getArtifact() {
		return artifact;
	}

	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public LocalDateTime getIssuedOn() {
		return issuedOn;
	}

	public void setIssuedOn(LocalDateTime issuedOn) {
		this.issuedOn = issuedOn;
	}

	public LocalDateTime getReturnOn() {
		return returnOn;
	}

	public void setReturnOn(LocalDateTime returnOn) {
		this.returnOn = returnOn;
	}

	public LocalDateTime getReturnedOn() {
		return returnedOn;
	}

	public void setReturnedOn(LocalDateTime returnedOn) {
		this.returnedOn = returnedOn;
	}

	public LocalDateTime getFinedOn() {
		return finedOn;
	}

	public void setFinedOn(LocalDateTime finedOn) {
		this.finedOn = finedOn;
	}

	public LocalDateTime getLostOn() {
		return lostOn;
	}

	public void setLostOn(LocalDateTime lostOn) {
		this.lostOn = lostOn;
	}

	public Boolean getWasLost() {
		return wasLost;
	}

	public void setWasLost(Boolean wasLost) {
		this.wasLost = wasLost;
	}

	public BigDecimal getFine() {
		return fine;
	}

	public void setFine(BigDecimal fine) {
		this.fine = fine;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDueDate() {
		String date = returnOn.getDayOfMonth() + "/" + returnOn.getMonthValue() + " @ "
				+ String.format("%02d:%02d", returnOn.getHour(), returnOn.getMinute());
		return date;
	}

	@Override
	public String toString() {
		String buf = " - ";
		return id + buf + isbn + buf + memberId + buf + issuedOn + buf + fine + buf + status + "\n" + artifact + '\n';
	}

	public String toStringWithoutArtifact() {
		String buf = " - ";
		return id + buf + isbn + buf + memberId + buf + issuedOn + buf + fine + buf + status;
	}
}