package ie.ucd.lms.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Blob;
import java.time.*;
import java.util.Set;

import ie.ucd.lms.service.admin.Common;

@Entity
@Table(name = "artifacts")
public class Artifact {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String isbn;
	private String type;
	private String genre;
	// private String edition;
	private Blob pdf;
	private String authors;
	private String title;
	private String originalTitle;
	private String subtitle;
	private String description;
	private String publishers;
	private LocalDateTime publishedOn;
	private LocalDateTime createdOn = LocalDateTime.now();
	private BigDecimal itemPrice = BigDecimal.valueOf(10.00);
	private Integer quantity;
	private String rackLocation;

	@OneToMany(mappedBy = "artifact")
	private Set<LoanHistory> loanHistories;

	@OneToMany(mappedBy = "artifact")
	private Set<ReserveQueue> reserveQueues;

	public void setAll(String isbn, String type, String genre, String authors, String title, String subtitle,
			String description, String publishers, String publishedOn, String itemPrice, String quantity,
			String rackLocation) {
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
		setRackLocation(rackLocation);
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

	public Blob getPdf() {
		return pdf;
	}

	public void setPdf(Blob pdf) {
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

	public String getRackLocation() {
		return rackLocation;
	}

	public void setRackLocation(String rackLocation) {
		this.rackLocation = rackLocation;
	}

	public String toString() {
		String buf = " - ";
		return id + buf + isbn + buf + title + buf + authors + buf + type;
	}
}