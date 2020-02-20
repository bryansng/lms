package ie.ucd.lms.entity;

import javax.persistence.*;

@Entity
@Table(name = "reserve_queue")
public class ReserveQueue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long positionInQueue;
	private String isbn;
	private Long memberId;

	public Long getPositionInQueue() {
		return positionInQueue;
	}

	public void setPositionInQueue(Long positionInQueue) {
		this.positionInQueue = positionInQueue;
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
}