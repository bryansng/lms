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

	@ManyToOne
	@JoinColumn(name = "isbn", insertable = false, updatable = false)
	private Artifact artifact;

	@ManyToOne
	@JoinColumn(name = "id", insertable = false, updatable = false)
	private Member member;

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