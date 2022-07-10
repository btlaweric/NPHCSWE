package com.ericlaw.NPHCSWE.model.user;

public class UserJSON {

  private String id;
  private String name;
  private String login;
  private String salary;
  private String startDate;

  public UserJSON(
    String id,
    String login,
    String name,
    String salary,
    String startDate
  ) {
    this.id = id;
    this.login = login;
    this.name = name;
    this.salary = salary;
    this.startDate = startDate;
  }

  public UserJSON() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getSalary() {
    return salary;
  }

  public void setSalary(String salary) {
    this.salary = salary;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }
}
