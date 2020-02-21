package ie.ucd.lms.entity;

import javax.persistence.*;
import ie.ucd.lms.service.admin.Common;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_history")
public class LoanHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String isbn;
	private Long memberId;
	private LocalDateTime issuedOn = LocalDateTime.now();
	private LocalDateTime returnOn;
	private LocalDateTime returnedOn;
	private LocalDateTime finedOn;
	private LocalDateTime lostOn;
	private Boolean wasLost;
	private BigDecimal fine;
	private String status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "isbn", insertable = false, updatable = false)
	private Artifact artifact;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", insertable = false, updatable = false)
	private Member member;

	public void setAll(String isbn, String memberId, String returnOn, String fine, String status) {
		setIsbn(isbn);
		setMemberId(Common.convertStringToLong(memberId));
		setReturnOn(Common.convertStringDateToDateTime(returnOn));
		setFine(Common.convertStringToBigDecimal(fine));
		setStatus(status);
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
}