package ie.ucd.lms.controller;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Blob;
import java.time.*;

@Entity
@Table(name="loan_history")
public class LoanHistory {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String isbn;
	private Long memberId;
	private LocalDateTime issuedOn = LocalDateTime.now();
	private LocalDateTime returnedOn;
	private LocalDateTime finedOn;
	private LocalDateTime lostOn;
	private Boolean wasLost;
	private BigDecimal fine;
	private String status;

	public Long getId() { return id; }
	public void setId(Long id)  { this.id = id; }
	public String getIsbn() { return isbn; }
	public void setIsbn(String isbn)  { this.isbn = isbn; }
	public Long getMemberId() { return memberId; }
	public void setMemberId(Long memberId)  { this.memberId = memberId; }
	public LocalDateTime getIssuedOn() { return issuedOn; }
	public void setIssuedOn(LocalDateTime issuedOn)  { this.issuedOn = issuedOn; }
	public LocalDateTime getReturnedOn() { return returnedOn; }
	public void setReturnedOn(LocalDateTime returnedOn)  { this.returnedOn = returnedOn; }
	public LocalDateTime getFinedOn() { return finedOn; }
	public void setFinedOn(LocalDateTime finedOn)  { this.finedOn = finedOn; }
	public LocalDateTime getLostOn() { return lostOn; }
	public void setLostOn(LocalDateTime lostOn)  { this.lostOn = lostOn; }
	public Boolean getWasLost() { return wasLost; }
	public void setWasLost(Boolean wasLost)  { this.wasLost = wasLost; }
	public BigDecimal getFine() { return fine; }
	public void setFine(BigDecimal fine)  { this.fine = fine; }
	public String getStatus() { return status; }
	public void setStatus(String status)  { this.status = status; }
}