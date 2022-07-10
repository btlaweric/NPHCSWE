package com.ericlaw.NPHCSWE.constant;

import java.math.BigDecimal;

public class UserFilterDefaults {

  public static final BigDecimal MIN_SALARY = BigDecimal.ZERO;
  public static final BigDecimal MAX_SALARY = new BigDecimal(4000);
  public static final int OFFSET = 0;
  public static final int LIMIT = Integer.MAX_VALUE;
  public static final String SORTBY = "Id";
}
