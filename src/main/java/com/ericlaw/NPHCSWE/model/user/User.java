package com.ericlaw.NPHCSWE.model.user;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "USEREMPLOYEE")
public class User {

  @Id
  @Column(name = "ID")
  private String id;

  @Column(name = "LOGIN")
  private String login;

  @Column(name = "NAME")
  private String name;

  @Column(name = "SALARY")
  private BigDecimal salary;

  @Column(name = "STARTDATE")
  private Date startDate;

  public User(
    String id,
    String login,
    String name,
    BigDecimal salary,
    Date startDate
  ) {
    this.id = id;
    this.login = login;
    this.name = name;
    this.salary = salary;
    this.startDate = startDate;
  }

  public User() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
}
