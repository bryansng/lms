package ie.ucd.lms.entity;

import javax.persistence.*;
import ie.ucd.lms.service.admin.Common;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserve_queue")
public class ReserveQueue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String isbn;

	@Column(name = "member_id", insertable = false, updatable = false)
	private Long memberId;
	private LocalDateTime expiredOn;

	@ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "isbn", insertable = false, updatable = false)
	private Artifact artifact;

	@ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "id", insertable = false, updatable = false)
	private Member member;

	public void setAll(String isbn, String memberId, String expiredOn, Artifact artifact, Member member) {
		setIsbn(isbn);
		setMemberId(Common.convertStringToLong(memberId));
		setExpiredOn(Common.convertStringDateToDateTime(expiredOn));
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

	public LocalDateTime getExpiredOn() {
		return expiredOn;
	}

	public void setExpiredOn(LocalDateTime expiredOn) {
		this.expiredOn = expiredOn;
	}

	public String toString() {
		String buf = " - ";
		return id + buf + isbn + buf + memberId + "\n" + artifact + '\n';
	}
}