package ie.ucd.lms.entity;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import java.time.*;
import java.util.List;
import javax.persistence.*;
import ie.ucd.lms.service.Common;

@Entity
@Table(name = "members")
public class Member implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  @Column(name = "email", insertable = false, updatable = false)
  private String email;
  private String fullName;
  private String mobileNumber;
  private String address;
  private String website;
  private String roles;

  @Column(nullable = true)
  private LocalDateTime bornOn;
  private LocalDateTime joinedOn = LocalDateTime.now();
  private LocalDateTime lastActiveOn = LocalDateTime.now();
  private String bio;
  private String type = "member";

  // @OneToOne(cascade = CascadeType.ALL)
  // @JoinColumn(referencedColumnName = "email")
  @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
  private Login login;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<LoanHistory> loanHistories;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<ReserveQueue> reserveQueues;

  public void setAll(String email, String fullName, String mobileNumber, String address, String website, String bornOn,
      String bio, String type) {
    System.out.println(email.equals("") ? this.email : email);
    setEmail(email.equals("") ? this.email : email);
    setFullName(fullName.equals("") ? this.fullName : fullName);
    setMobileNumber(mobileNumber.equals("") ? this.mobileNumber : mobileNumber);
    setAddress(address.equals("") ? this.address : address);
    setWebsite(website.equals("") ? this.website : website);

    if (bornOn.equals("")) {
      setBornOn(null);
    } else {
      setBornOn(Common.convertStringDateToDateTime(bornOn));
    }

    setBio(bio);
    setType(type);
  }

  public String getInitials() {
    String[] words = fullName.split(" ");
    // return words.length > 1 ? String.valueOf(words[0].charAt(0)) + String.valueOf(words[words.length - 1].charAt(0))
    //     : String.valueOf(words[0].charAt(0));
    String initials = "";
    for (String word : words) {
      initials += String.valueOf(word.charAt(0)).toUpperCase();
    }
    return initials;
  }

  public Login getLogin() {
    return login;
  }

  public void setLogin(Login login) {
    this.login = login;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public LocalDateTime getBornOn() {
    return bornOn;
  }

  public void setBornOn(LocalDateTime bornOn) {
    this.bornOn = bornOn;
  }

  public LocalDateTime getJoinedOn() {
    return joinedOn;
  }

  public void setJoinedOn(LocalDateTime joinedOn) {
    this.joinedOn = joinedOn;
  }

  public LocalDateTime getLastActiveOn() {
    return lastActiveOn;
  }

  public void setLastActiveOn(LocalDateTime lastActiveOn) {
    this.lastActiveOn = lastActiveOn;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRoles() {
    return roles;
  }

  public void setRoles(String roles) {
    this.roles = roles;
  }

  public String toString() {
    String buf = " - ";
    return id + buf + fullName + buf + getInitials() + buf + email + buf + mobileNumber + buf + address + buf + type
        + "\n" + login.getEmail() + buf + login.getHash();
  }

  public String toStringDefault() {
    String buf = " - ";
    return id + buf + fullName + buf + getInitials() + buf + email + buf + mobileNumber + buf + address + buf + type;
  }

  public String toStringWithLoanHistory() {
    String buf = " - ";
    String res = id + buf + fullName + buf + email + buf + mobileNumber + buf + address + buf + type + "\n";
    for (LoanHistory loanHistory : loanHistories) {
      res += "\t" + loanHistory.toStringWithoutArtifact() + "\n";
    }
    return res;
  }

  public String toStringWithReserveQueue() {
    String buf = " - ";
    String res = id + buf + fullName + buf + email + buf + mobileNumber + buf + address + buf + type + "\n";
    for (ReserveQueue reserveQueue : reserveQueues) {
      res += "\t" + reserveQueue.toStringWithoutArtifact() + "\n";
    }
    return res;
  }
}