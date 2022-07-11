package com.ericlaw.NPHCSWE.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;

import com.ericlaw.NPHCSWE.constant.CustomException;
import com.ericlaw.NPHCSWE.model.customException.UserException;
import com.ericlaw.NPHCSWE.model.customException.UserFilterException;
import com.ericlaw.NPHCSWE.model.user.*;
import com.ericlaw.NPHCSWE.repository.UserRepository;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private GlobalService globalService;

  //Filter Errors
  @Test
  public void givenInvalidMinSalaryFilterShouldReturnError() {
    Map<String, String> filterParams = Map.of("minSalary", "abc");

    Throwable exception = assertThrows(
      UserFilterException.class,
      () -> {
        userService.getUsersFromFilters(filterParams);
      }
    );

    assertTrue(
      exception.getMessage().contains(CustomException.INVALID_MINSALARY)
    );
  }

  @Test
  public void givenInvalidMaxSalaryUpdateShouldReturnError() {
    Map<String, String> filterParams = Map.of("maxSalary", "abc");

    Throwable exception = assertThrows(
      UserFilterException.class,
      () -> {
        userService.getUsersFromFilters(filterParams);
      }
    );

    assertTrue(
      exception.getMessage().contains(CustomException.INVALID_MAXSALARY)
    );
  }

  @Test
  public void givenInvalidOffsetUpdateShouldReturnError() {
    Map<String, String> filterParams = Map.of("offset", "abc");

    Throwable exception = assertThrows(
      UserFilterException.class,
      () -> {
        userService.getUsersFromFilters(filterParams);
      }
    );

    assertTrue(exception.getMessage().contains(CustomException.INVALID_OFFSET));
  }

  @Test
  public void givenInvalidLimitUpdateShouldReturnError() {
    Map<String, String> filterParams = Map.of("limit", "abc");

    Throwable exception = assertThrows(
      UserFilterException.class,
      () -> {
        userService.getUsersFromFilters(filterParams);
      }
    );

    assertTrue(exception.getMessage().contains(CustomException.INVALID_LIMIT));
  }

  //Update Errors
  @Test
  public void givenInvalidIdUpdateShouldReturnErrorTest() {
    String id = "1";
    when(userRepository.existsById(id)).thenReturn(false);

    Throwable exception = assertThrows(
      UserException.class,
      () -> {
        userService.updateUserById(id, any());
      }
    );

    assertTrue(exception.getMessage().contains(CustomException.USER_NOT_FOUND));
  }

  @Test
  public void givenInvalidDateUpdateShouldReturnErrorTest() {
    UserJSON testUserJson = new UserJSON(
      "1",
      "John Doe",
      "JDoe",
      "1000",
      "2022-13-01"
    );
    when(userRepository.existsById(testUserJson.getId())).thenReturn(true);
    when(globalService.isValidDate(testUserJson.getStartDate()))
      .thenReturn(false);

    Throwable exception = assertThrows(
      UserException.class,
      () -> {
        userService.updateUserById(testUserJson.getId(), testUserJson);
      }
    );

    assertTrue(exception.getMessage().contains(CustomException.INVALID_DATE));
  }

  @Test
  public void givenDupeLoginUpdateShouldReturnErrorTest() {
    UserJSON testUserJson = new UserJSON(
      "1",
      "John Doe",
      "JDoe",
      "1000",
      "2022-01-01"
    );
    when(userRepository.existsById(testUserJson.getId())).thenReturn(true);
    when(globalService.isValidDate(testUserJson.getStartDate()))
      .thenReturn(true);
    when(
      userRepository.existsByLoginAndIdNot(
        testUserJson.getLogin(),
        testUserJson.getId()
      )
    )
      .thenReturn(false);

    Throwable exception = assertThrows(
      UserException.class,
      () -> {
        userService.updateUserById(testUserJson.getId(), testUserJson);
      }
    );

    assertTrue(
      exception.getMessage().contains(CustomException.USER_LOGIN_EXISTS)
    );
  }

  @Test
  public void givenInvalidSalaryUpdateShouldReturnErrorTest() {
    UserJSON testUserJson = new UserJSON(
      "1",
      "John Doe",
      "JDoe",
      "abc",
      "2022-01-01"
    );
    when(userRepository.existsById(testUserJson.getId())).thenReturn(true);

    Throwable exception = assertThrows(
      UserException.class,
      () -> {
        userService.updateUserById(testUserJson.getId(), testUserJson);
      }
    );

    assertTrue(exception.getMessage().contains(CustomException.INVALID_SALARY));
  }

  //Create Errors
  @Test
  public void givenInvalidDateCreateShouldReturnErrorTest() {
    UserJSON testUserJson = new UserJSON(
      "1",
      "John Doe",
      "JDoe",
      "1000",
      "2022-13-01"
    );

    when(globalService.isValidDate(testUserJson.getStartDate()))
      .thenReturn(false);

    Throwable exception = assertThrows(
      Exception.class,
      () -> {
        userService.createUser(testUserJson);
      }
    );

    assertTrue(exception.getMessage().contains(CustomException.INVALID_DATE));
  }

  @Test
  public void givenDupeIDCreateShouldReturnErrorTest() {
    UserJSON testUserJson = new UserJSON(
      "1",
      "John Doe",
      "JDoe",
      "1000",
      "2022-01-01"
    );

    when(globalService.isValidDate(testUserJson.getStartDate()))
      .thenReturn(true);
    when(userRepository.existsById(testUserJson.getId())).thenReturn(true);

    Throwable exception = assertThrows(
      Exception.class,
      () -> {
        userService.createUser(testUserJson);
      }
    );

    assertTrue(exception.getMessage().contains(CustomException.USER_ID_EXISTS));
  }

  @Test
  public void givenInvalidSalaryCreateShouldReturnErrorTest() {
    UserJSON testUserJson = new UserJSON(
      "1",
      "John Doe",
      "JDoe",
      "abc",
      "2022-01-01"
    );

    Throwable exception = assertThrows(
      Exception.class,
      () -> {
        userService.createUser(testUserJson);
      }
    );

    assertTrue(exception.getMessage().contains(CustomException.INVALID_SALARY));
  }

  @Test
  public void givenDupeLoginCreateShouldReturnErrorTest() {
    UserJSON testUserJson = new UserJSON(
      "1",
      "John Doe",
      "JDoe",
      "1000",
      "2022-01-01"
    );

    when(globalService.isValidDate(testUserJson.getStartDate()))
      .thenReturn(true);
    when(userRepository.existsByLogin(any())).thenReturn(true);

    Throwable exception = assertThrows(
      Exception.class,
      () -> {
        userService.createUser(testUserJson);
      }
    );

    assertTrue(
      exception.getMessage().contains(CustomException.USER_LOGIN_EXISTS)
    );
  }

  //Delete Errors
  @Test
  public void givenInvalidIDDeletehouldReturnErrorTest() {
    String id = "1";
    when(userRepository.existsById(id)).thenReturn(false);

    Throwable exception = assertThrows(
      Exception.class,
      () -> {
        userService.deleteUserbyId(id);
      }
    );

    assertTrue(exception.getMessage().contains(CustomException.USER_NOT_FOUND));
  }
}
