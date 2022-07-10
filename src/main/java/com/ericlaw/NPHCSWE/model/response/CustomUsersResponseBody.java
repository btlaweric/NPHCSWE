package com.ericlaw.NPHCSWE.model.response;

import com.ericlaw.NPHCSWE.model.user.User;
import java.util.List;

public class CustomUsersResponseBody {

  private List<User> results;

  public CustomUsersResponseBody(List<User> results) {
    this.results = results;
  }

  public List<User> getResults() {
    return results;
  }

  public void setResults(List<User> results) {
    this.results = results;
  }
}
