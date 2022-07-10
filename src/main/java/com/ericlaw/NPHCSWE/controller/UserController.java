package com.ericlaw.NPHCSWE.controller;

import com.ericlaw.NPHCSWE.constant.Success;
import com.ericlaw.NPHCSWE.model.customException.*;
import com.ericlaw.NPHCSWE.model.response.*;
import com.ericlaw.NPHCSWE.model.user.*;
import com.ericlaw.NPHCSWE.service.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IFileService fileService;

  @Autowired
  private IUserService userService;

  @RequestMapping(
    value = "/upload",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    method = RequestMethod.POST
  )
  public @ResponseBody ResponseEntity<CustomResponseBody> UploadCSVFile(
    @RequestParam("file") MultipartFile file
  ) {
    CustomResponseBody body = new CustomResponseBody(Success.FILE_PROCESSED);
    fileService.uploadUsersCSVFile(file);

    return ResponseEntity.ok().body(body);
  }

  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody ResponseEntity<CustomUsersResponseBody> GetUsers(
    @RequestParam Map<String, String> filterParams
  ) {
    CustomUsersResponseBody body = new CustomUsersResponseBody(null);

    List<User> users = userService.getUsersFromFilters(filterParams);
    body.setResults(users);

    return ResponseEntity.ok().body(body);
  }

  @RequestMapping(method = RequestMethod.POST)
  public @ResponseBody ResponseEntity<CustomResponseBody> CreateUser(
    @RequestBody UserJSON user
  ) {
    CustomResponseBody body = new CustomResponseBody(Success.USER_CREATE);

    userService.createUser(user);

    return ResponseEntity.ok().body(body);
  }

  @RequestMapping(
    value = "/{id}",
    method = { RequestMethod.PUT, RequestMethod.PATCH }
  )
  public @ResponseBody ResponseEntity<CustomResponseBody> UpdateUser(
    @PathVariable String id,
    @RequestBody UserJSON user
  ) {
    CustomResponseBody body = new CustomResponseBody(Success.USER_UPDATE);

    userService.updateUserById(id, user);

    return ResponseEntity.ok().body(body);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public @ResponseBody ResponseEntity<User> GetUserById(
    @PathVariable String id
  ) {
    return ResponseEntity.ok().body(userService.getUserbyId(id));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public @ResponseBody ResponseEntity<CustomResponseBody> DeleteUser(
    @PathVariable String id
  ) {
    CustomResponseBody body = new CustomResponseBody(Success.USER_DELETE);
    userService.deleteUserbyId(id);

    return ResponseEntity.ok().body(body);
  }

  @ControllerAdvice
  public class ControllerExceptionHandler {

    @ExceptionHandler(
      { UserException.class, FileException.class, UserFilterException.class }
    )
    public @ResponseBody ResponseEntity<CustomResponseBody> handleException(
      RuntimeException ex
    ) {
      CustomResponseBody body = new CustomResponseBody(ex.getMessage());

      return ResponseEntity.status(400).body(body);
    }
  }
}
