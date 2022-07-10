package com.ericlaw.NPHCSWE.service;

import com.ericlaw.NPHCSWE.model.user.*;
import java.util.List;
import java.util.Map;

public interface IUserService {
  public void createOrUpdateUsers(List<User> users);

  public void createUser(UserJSON user);

  public List<User> getUsersFromFilters(Map<String, String> filters);

  public User getUserbyId(String id);

  public void deleteUserbyId(String id);

  public void updateUserById(String id, UserJSON user);
}
