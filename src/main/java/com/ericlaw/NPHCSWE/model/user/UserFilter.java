package com.ericlaw.NPHCSWE.model.user;

import java.math.BigDecimal;

public class UserFilter {

  private BigDecimal minSalary;
  private BigDecimal maxSalary;
  private int offset;
  private int limit;
  private String sortBy;

  public UserFilter() {
    this.minSalary = BigDecimal.ZERO;
    this.maxSalary = new BigDecimal(4000);
    this.offset = 0;
    this.limit = Integer.MAX_VALUE;
    this.sortBy = "id";
  }

  public BigDecimal getMinSalary() {
    return minSalary;
  }

  public void setMinSalary(BigDecimal minSalary) {
    this.minSalary = minSalary;
  }

  public BigDecimal getMaxSalary() {
    return maxSalary;
  }

  public void setMaxSalary(BigDecimal maxSalary) {
    this.maxSalary = maxSalary;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }
}
