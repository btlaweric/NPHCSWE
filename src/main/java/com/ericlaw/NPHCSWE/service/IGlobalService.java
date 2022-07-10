package com.ericlaw.NPHCSWE.service;

import java.util.Date;

public interface IGlobalService {
  public Date getDateFromString(String dateString);

  public boolean isValidDate(String dateString);
}
