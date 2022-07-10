package com.ericlaw.NPHCSWE.constant;

public class CustomException {

  public static final String USER_NOT_FOUND = "No such employee";
  public static final String USER_ID_EXISTS = "Employee ID already exists";
  public static final String USER_LOGIN_EXISTS = "Employee login not unique";
  public static final String INVALID_SALARY = "Invalid salary";
  public static final String INVALID_DATE = "Invalid date";
  public static final String INVALID_FILE = "Only CSV file is allowed";
  public static final String EMPTY_FILE = "File is empty";
  public static final String DUPLICATE_ID = "Duplicate ID found on the file";
  public static final String DUPLICATE_LOGIN =
    "Duplicate login found on the file";
  public static final String INVALID_MINSALARY = "Invalid minSalary";
  public static final String INVALID_MAXSALARY = "Invalid maxSalary";
  public static final String INVALID_OFFSET = "Invalid Offset";
  public static final String INVALID_LIMIT = "Invalid Limit";
  public static final String MAXSALARY_SMALLER_THAN_MINSALARY = "maxSalary cannot be smaller than minSalary";
}
