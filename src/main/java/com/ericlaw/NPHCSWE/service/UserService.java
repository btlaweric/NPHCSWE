package com.ericlaw.NPHCSWE.service;

import com.ericlaw.NPHCSWE.constant.CustomException;
import com.ericlaw.NPHCSWE.constant.UserFilterDefaults;
import com.ericlaw.NPHCSWE.model.customException.*;
import com.ericlaw.NPHCSWE.model.user.*;
import com.ericlaw.NPHCSWE.repository.UserRepository;
import java.math.BigDecimal;
import java.util.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private IGlobalService globalService;

  public void createOrUpdateUsers(List<User> users) {
    userRepository.saveAll(users);
  }

  public void createUser(UserJSON user) {
    validateUserJson(user, "create");

    User userObj = new User(
      user.getId(),
      user.getLogin(),
      user.getName(),
      new BigDecimal(user.getSalary()),
      globalService.getDateFromString(user.getStartDate())
    );

    userRepository.save(userObj);
  }

  public void updateUserById(String id, UserJSON user) {
    if (!userRepository.existsById(id)) throw new UserException(
      CustomException.USER_NOT_FOUND
    );

    validateUserJson(user, "update");

    if (
      !userRepository.existsByLoginAndIdNot(user.getLogin(), id)
    ) throw new UserException(CustomException.USER_LOGIN_EXISTS);

    User userObj = new User(
      user.getId(),
      user.getLogin(),
      user.getName(),
      new BigDecimal(user.getSalary()),
      globalService.getDateFromString(user.getStartDate())
    );

    userRepository.save(userObj);

    return;
  }

  public List<User> getUsersFromFilters(Map<String, String> filterParams) {
    List<User> users = new ArrayList<>();
    UserFilter filter = new UserFilter();

    filter = validateAndSetUserFilter(filterParams);

    Pageable pageable = PageRequest.of(
      filter.getOffset(),
      filter.getLimit(),
      Sort.by(sortBy(filter.getSortBy()))
    );

    users = userRepository.findAndFilterUsers(filter, pageable);
    return users;
  }

  public User getUserbyId(String id) {
    User user = new User();

    user.setId(id);

    if (!userRepository.existsById(id)) throw new UserException(
      CustomException.USER_NOT_FOUND
    );

    user = userRepository.getUserById(id);
    return user;
  }

  public void deleteUserbyId(String id) {
    User deleteUser = new User();

    deleteUser.setId(id);

    if (!userRepository.existsById(id)) throw new UserException(
      CustomException.USER_NOT_FOUND
    );

    userRepository.delete(deleteUser);
  }

  private UserFilter validateAndSetUserFilter(
    Map<String, String> filterParams
  ) {
    UserFilter filter = new UserFilter();

    String minSalaryString = filterParams.getOrDefault(
      "minSalary",
      UserFilterDefaults.MIN_SALARY.toString()
    );
    String maxSalaryString = filterParams.getOrDefault(
      "maxSalary",
      UserFilterDefaults.MAX_SALARY.toString()
    );
    String offsetString = filterParams.getOrDefault(
      "offset",
      String.valueOf(UserFilterDefaults.OFFSET)
    );
    String limitString = filterParams.getOrDefault(
      "limit",
      String.valueOf(UserFilterDefaults.LIMIT)
    );
    String sortByString = filterParams.getOrDefault(
      "sortBy",
      String.valueOf(UserFilterDefaults.SORTBY)
    );

    if (!NumberUtils.isParsable(minSalaryString)) throw new UserFilterException(
      CustomException.INVALID_MINSALARY
    );
    if (!NumberUtils.isParsable(maxSalaryString)) throw new UserFilterException(
      CustomException.INVALID_MAXSALARY
    );

    if (!NumberUtils.isParsable(offsetString)) throw new UserFilterException(
      CustomException.INVALID_OFFSET
    );

    if (!NumberUtils.isParsable(limitString)) throw new UserFilterException(
      CustomException.INVALID_LIMIT
    );

    filter.setMinSalary(new BigDecimal(minSalaryString));
    filter.setMaxSalary(new BigDecimal(maxSalaryString));
    filter.setOffset(Integer.parseInt(offsetString));
    filter.setLimit(
      Integer.parseInt(limitString) != 0
        ? Integer.parseInt(limitString)
        : Integer.MAX_VALUE
    );
    filter.setSortBy(sortBy(sortByString));

    if (
      filter.getMinSalary().compareTo(filter.getMaxSalary()) > 0
    ) throw new UserFilterException(
      CustomException.MAXSALARY_SMALLER_THAN_MINSALARY
    );

    return filter;
  }

  private void validateUserJson(UserJSON user, String mode) {
    if (!NumberUtils.isParsable(user.getSalary())) throw new UserException(
      CustomException.INVALID_SALARY
    );

    if (
      !globalService.isValidDate(user.getStartDate())
    ) throw new UserException(CustomException.INVALID_DATE);

    if (
      mode == "create" && userRepository.existsById(user.getId())
    ) throw new UserException(CustomException.USER_ID_EXISTS);

    if (
      mode == "create" && userRepository.existsByLogin(user.getLogin())
    ) throw new UserException(CustomException.USER_LOGIN_EXISTS);
  }

  private String sortBy(String columnName) {
    switch (columnName) {
      case "id":
      case "login":
      case "name":
      case "salary":
      case "startdate":
        return columnName;
      default:
        return "id";
    }
  }
}
