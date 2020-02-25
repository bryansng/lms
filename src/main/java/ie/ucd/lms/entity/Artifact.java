package ie.ucd.lms.entity;

import ie.ucd.lms.service.Common;
import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "artifacts")
public class Artifact implements Comparable<Artifact> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String isbn;
	private String type;
	private String genre;
	// private String edition;
	@Column(length = 65535)
	private String pdf;

	@Column(length = 512)
	private String authors;

	private String title;
	private String originalTitle;

	@Column(length = 65535)
	private String subtitle;

	@Column(length = 65535)
	private String description;

	@Column(length = 512)
	private String publishers;
	private LocalDateTime publishedOn;
	private LocalDateTime createdOn = LocalDateTime.now();
	private BigDecimal itemPrice = BigDecimal.valueOf(10.00);
	private Integer quantity; // current quantity in stock.
	private Integer totalQuantity; // how many quantity we should have for this artifact.
	private String rackLocation;
	private Integer totalLoans;

	@OneToMany(mappedBy = "artifact", cascade = CascadeType.ALL)
	private List<LoanHistory> loanHistories = new ArrayList<>();

	@OneToMany(mappedBy = "artifact", cascade = CascadeType.ALL)
	private List<ReserveQueue> reserveQueues;

	public void setAll(String isbn, String type, String genre, String authors, String title, String subtitle,
			String description, String publishers, String publishedOn, String itemPrice, String quantity,
			String totalQuantity, String rackLocation, Integer totalLoans) {
		setIsbn(isbn);
		setType(type);
		setGenre(genre);
		setAuthors(authors);
		setTitle(title);
		setSubtitle(subtitle);
		setDescription(description);
		setPublishers(publishers);
		setPublishedOn(Common.convertStringDateToDateTime(publishedOn));
		setItemPrice(Common.convertStringToBigDecimal(itemPrice));
		setQuantity(Common.convertStringToInteger(quantity));
		setTotalQuantity(Common.convertStringToInteger(totalQuantity));
		setRackLocation(rackLocation);
		setTotalLoans(totalLoans);
	}

	public boolean inStock() {
		return quantity > 0;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPublishers() {
		return publishers;
	}

	public void setPublishers(String publishers) {
		this.publishers = publishers;
	}

	public LocalDateTime getPublishedOn() {
		return publishedOn;
	}

	public void setPublishedOn(LocalDateTime publishedOn) {
		this.publishedOn = publishedOn;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void incrementQuantity() {
		this.quantity += 1;
	}

	public void decrementQuantity() {
		this.quantity -= 1;
	}

	public Integer getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getRackLocation() {
		return rackLocation;
	}

	public void setRackLocation(String rackLocation) {
		this.rackLocation = rackLocation;
	}

	public Integer getTotalLoans() {
		return this.totalLoans;
	}

	public void setTotalLoans(Integer totalLoans) {
		this.totalLoans = totalLoans;
	}

	@Override
	public int compareTo(Artifact a) {
		return createdOn.compareTo(a.getCreatedOn());
	}

	@Override
	public String toString() {
		String buf = " - ";
		return id + buf + isbn + buf + title + buf + authors + buf + type;
	}

	public String toStringWithLoanHistory() {
		String buf = " - ";
		String res = id + buf + isbn + buf + title + buf + authors + buf + type + "\n";
		for (LoanHistory loanHistory : loanHistories) {
			res += "\t" + loanHistory.toStringWithoutArtifact() + "\n";
		}
		return res;
	}

	public String toStringWithReserveQueue() {
		String buf = " - ";
		String res = id + buf + isbn + buf + title + buf + authors + buf + type + "\n";
		for (ReserveQueue reserveQueue : reserveQueues) {
			res += "\t" + reserveQueue.toStringWithoutArtifact() + "\n";
		}
		return res;
	}
}
