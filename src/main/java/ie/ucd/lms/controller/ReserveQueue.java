package ie.ucd.lms.controller;

import javax.persistence.*;

@Entity
@Table(name = "reserve_queue")
public class ReserveQueue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String isbn;
	private Long memberId;
	private Long positionInQueue;

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

	public Long getPositionInQueue() {
		return positionInQueue;
	}

	public void setPositionInQueue(Long positionInQueue) {
		this.positionInQueue = positionInQueue;
	}
}