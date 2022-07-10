package com.ericlaw.NPHCSWE.service;

import com.ericlaw.NPHCSWE.constant.Global;
import java.text.ParseException;
import java.util.*;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

@Service
public class GlobalService implements IGlobalService {

  //Returns date is valid, if not null
  public Date getDateFromString(String dateString) {
    String[] acceptedPatterns = Global.DATE_FORMAT;

    try {
      Date parsedDate = DateUtils.parseDateStrictly(
        dateString,
        acceptedPatterns
      );
      return parsedDate;
    } catch (ParseException e) {}

    return null;
  }

  public boolean isValidDate(String dateString) {
    String[] acceptedPatterns = Global.DATE_FORMAT;

    try {
      DateUtils.parseDateStrictly(dateString, acceptedPatterns);
      return true;
    } catch (ParseException e) {}

    return false;
  }
}
