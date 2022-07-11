package com.ericlaw.NPHCSWE.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ericlaw.NPHCSWE.constant.*;
import com.ericlaw.NPHCSWE.model.customException.*;
import com.ericlaw.NPHCSWE.model.user.*;
import com.ericlaw.NPHCSWE.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.*;

@WebMvcTest
public class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  private IUserService userService;

  @MockBean
  private IFileService fileService;

  @Autowired
  private ObjectMapper objectMapper;

  //SUCCESS
  @Test
  public void givenUsersWhenGetUsersThenReturnUsers() throws Exception {
    List<User> users = new ArrayList<User>();
    User user1 = new User(
      "f0001",
      "JDoe",
      "John Doe",
      new BigDecimal(1000),
      new Date()
    );
    User user2 = new User(
      "f0002",
      "KJane",
      "Ken Jane",
      new BigDecimal(2000),
      new Date()
    );

    User user3 = new User(
      "f0003",
      "MLee",
      "May Lee",
      new BigDecimal(3000),
      new Date()
    );
    users.add(user1);
    users.add(user2);
    users.add(user3);

    when(userService.getUsersFromFilters(anyMap())).thenReturn(users);

    ResultActions response = mockMvc.perform(get("/users"));

    response
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.results.length()", is(3)));
  }

  @Test
  public void givenUserWhenCreateUserThenReturnSuccess() throws Exception {
    String id = "1";
    UserJSON userJson = new UserJSON(id, "2", "abc", "ddvc", "2022-01-01");
    String jsonString = objectMapper.writeValueAsString(userJson);

    doNothing().when(userService).createUser(userJson);

    ResultActions response = mockMvc.perform(
      post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonString)
    );

    response
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(Success.USER_CREATE));
  }

  @Test
  public void givenIDAndUserWhenUpdateUserThenReturnSuccess() throws Exception {
    String id = "1";
    UserJSON userJson = new UserJSON(id, "2", "abc", "ddvc", "2022-01-01");
    String jsonString = objectMapper.writeValueAsString(userJson);

    doNothing().when(userService).updateUserById(id, userJson);

    ResultActions response = mockMvc.perform(
      put("/users/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString)
    );

    response
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(Success.USER_UPDATE));
  }

  @Test
  public void givenIDWhenDeleteUserThenReturnSuccess() throws Exception {
    String id = "1";

    doNothing().when(userService).deleteUserbyId(id);

    ResultActions response = mockMvc.perform(delete("/users/" + id));

    response
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(Success.USER_DELETE));
  }

  @Test
  public void givenCSVWhenCreateUsersThenReturnSuccess() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
      "file",
      "test.csv",
      MediaType.TEXT_PLAIN_VALUE,
      "testing".getBytes()
    );

    doNothing().when(fileService).uploadUsersCSVFile(file);

    ResultActions response = mockMvc.perform(
      multipart("/users/upload").file(file)
    );

    response
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(Success.FILE_PROCESSED));
  }

  //ERROR
  @Test
  public void givenInvalidMinSalaryWhenGetUsersThenReturnError()
    throws Exception {
    given(userService.getUsersFromFilters(anyMap()))
      .willThrow(new UserException(CustomException.INVALID_MINSALARY));
    ResultActions response = mockMvc.perform(get("/users?minSalary=abc"));

    response
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(
        jsonPath("$.message").value(CustomException.INVALID_MINSALARY)
      );
  }

  @Test
  public void givenInvalidMaxSalaryWhenGetUsersThenReturnError()
    throws Exception {
    given(userService.getUsersFromFilters(anyMap()))
      .willThrow(new UserException(CustomException.INVALID_MAXSALARY));
    ResultActions response = mockMvc.perform(get("/users?maxSalary=abc"));

    response
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(
        jsonPath("$.message").value(CustomException.INVALID_MAXSALARY)
      );
  }

  @Test
  public void givenInvalidOffsetWhenGetUsersThenReturnError() throws Exception {
    given(userService.getUsersFromFilters(anyMap()))
      .willThrow(new UserException(CustomException.INVALID_OFFSET));
    ResultActions response = mockMvc.perform(get("/users?offset=abc"));

    response
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(CustomException.INVALID_OFFSET));
  }

  @Test
  public void givenInvalidLimitWhenGetUsersThenReturnError() throws Exception {
    given(userService.getUsersFromFilters(anyMap()))
      .willThrow(new UserException(CustomException.INVALID_LIMIT));
    ResultActions response = mockMvc.perform(get("/users?limit=abc"));

    response
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(CustomException.INVALID_LIMIT));
  }

  @Test
  public void givenInvalidIDWhenGetUserThenReturnError() throws Exception {
    String id = "1";

    given(userService.getUserbyId(id))
      .willThrow(new UserException(CustomException.USER_NOT_FOUND));

    ResultActions response = mockMvc.perform(get("/users/" + id));

    response
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void givenInvalidIDWhenDeleteUserThenReturnError() throws Exception {
    String id = "1";

    willThrow(new UserException(CustomException.USER_NOT_FOUND))
      .given(userService)
      .deleteUserbyId(id);

    ResultActions response = mockMvc.perform(delete("/users/" + id));

    response
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(CustomException.USER_NOT_FOUND));
  }

  @Test
  public void givenEmptyCSVWhenCreateUsersThenReturnError() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
      "file",
      "test.csv",
      MediaType.TEXT_PLAIN_VALUE,
      "testing".getBytes()
    );

    willThrow(new FileException(CustomException.EMPTY_FILE))
      .given(fileService)
      .uploadUsersCSVFile(file);

    ResultActions response = mockMvc.perform(
      multipart("/users/upload").file(file)
    );

    response
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(CustomException.EMPTY_FILE));
  }

  @Test
  public void givenDupeIDCSVWhenCreateUsersThenReturnError() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
      "file",
      "test.csv",
      MediaType.TEXT_PLAIN_VALUE,
      "testing".getBytes()
    );

    willThrow(new FileException(CustomException.DUPLICATE_ID))
      .given(fileService)
      .uploadUsersCSVFile(file);

    ResultActions response = mockMvc.perform(
      multipart("/users/upload").file(file)
    );

    response
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(CustomException.DUPLICATE_ID));
  }

  @Test
  public void givenDupeLoginCSVWhenCreateUsersThenReturnError()
    throws Exception {
    MockMultipartFile file = new MockMultipartFile(
      "file",
      "test.csv",
      MediaType.TEXT_PLAIN_VALUE,
      "testing".getBytes()
    );

    willThrow(new FileException(CustomException.DUPLICATE_LOGIN))
      .given(fileService)
      .uploadUsersCSVFile(file);

    ResultActions response = mockMvc.perform(
      multipart("/users/upload").file(file)
    );

    response
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(CustomException.DUPLICATE_LOGIN));
  }

  @Test
  public void givenNotCSVWhenCreateUsersThenReturnError() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
      "file",
      "test.xls",
      MediaType.TEXT_PLAIN_VALUE,
      "testing".getBytes()
    );

    willThrow(new FileException(CustomException.INVALID_FILE))
      .given(fileService)
      .uploadUsersCSVFile(file);

    ResultActions response = mockMvc.perform(
      multipart("/users/upload").file(file)
    );

    response
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(CustomException.INVALID_FILE));
  }

  @Test
  public void givenUserWhenCreateUserThenReturnError() throws Exception {
    UserJSON userJson = new UserJSON();
    String jsonString = objectMapper.writeValueAsString(userJson);

    willThrow(new UserException(CustomException.USER_ID_EXISTS))
      .given(userService)
      .createUser(any());

    ResultActions response = mockMvc.perform(
      post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonString)
    );

    response
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(CustomException.USER_ID_EXISTS));
  }

  @Test
  public void givenInvalidUserWhenUpdateUserThenReturnError() throws Exception {
    String id = "1";
    UserJSON userJson = new UserJSON();
    String jsonString = objectMapper.writeValueAsString(userJson);

    willThrow(new UserException(CustomException.USER_NOT_FOUND))
      .given(userService)
      .updateUserById(any(), any());

    ResultActions response = mockMvc.perform(
      put("/users/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString)
    );

    response
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value(CustomException.USER_NOT_FOUND));
  }
}
