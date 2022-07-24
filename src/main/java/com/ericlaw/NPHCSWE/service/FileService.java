package com.ericlaw.NPHCSWE.service;

import com.ericlaw.NPHCSWE.constant.CustomException;
import com.ericlaw.NPHCSWE.model.customException.FileException;
import com.ericlaw.NPHCSWE.model.user.User;
import com.opencsv.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService implements IFileService {

  @Autowired
  private IUserService userService;

  @Autowired
  private IGlobalService globalServce;

  public void uploadUsersCSVFile(MultipartFile file) {
    List<User> users = new ArrayList<User>();

    if (!isCSVFile(file.getOriginalFilename()))
      throw new FileException(
          CustomException.INVALID_FILE);

    if (isEmptyFile(file))
      throw new FileException(CustomException.EMPTY_FILE);

    try {
      Reader reader = new InputStreamReader(file.getInputStream(), "UTF-8");

      CSVParser parser = new CSVParserBuilder()
          .withSeparator(',')
          .withIgnoreQuotations(true)
          .build();

      CSVReader csvReader = new CSVReaderBuilder(reader)
          .withSkipLines(1)
          .withCSVParser(parser)
          .build();

      String[] line;

      while ((line = csvReader.readNext()) != null) {

        String message = checkLineForErrors(line, csvReader.getLinesRead());
        if (!message.isEmpty()) {
          throw new FileException(message);
        }

        //Skip if character is #
        if (!line[0].isEmpty() && Character.compare(line[0].charAt(0), '#') != 0) {
          User user = new User(
              line[0],
              line[1],
              line[2],
              new BigDecimal(line[3]),
              globalServce.getDateFromString(line[4]));

          users.add(user);

        }
      }
      reader.close();
      csvReader.close();
    } catch (IOException e) {
      throw new FileException(e.getMessage());
    }

    if (hasDuplicateIDInFile(users))
      throw new FileException(
          CustomException.DUPLICATE_ID);

    if (hasDuplicateLoginInFile(users))
      throw new FileException(
          CustomException.DUPLICATE_LOGIN);

    userService.createOrUpdateUsers(users);
  }

  private boolean isCSVFile(String fileName) {
    String fileExt = FilenameUtils.getExtension(fileName);

    if (fileExt.equalsIgnoreCase("csv")) {
      return true;
    }
    return false;
  }

  private boolean isEmptyFile(MultipartFile file) {
    if (file.isEmpty()) {
      return true;
    }

    return false;
  }

  private String checkLineForErrors(String[] line, Long currentLine) {
    String message = "";
    BigDecimalValidator bigDecimalValidator = BigDecimalValidator.getInstance();

    // Logic below to check null/big decimal/date
    if (line[0].isEmpty()) {
      message = String.format("Missing id value on line %s.", currentLine);
    } else if (line[1].isEmpty()) {
      message = String.format("Missing login value on line %s.", currentLine);
    } else if (line[2].isEmpty()) {
      message = String.format("Missing name value on line %s.", currentLine);
    } else if (line[3].isEmpty() || bigDecimalValidator.validate(line[3]) == null) {
      message = String.format(
          "Missing or invalid salary value on line %s.",
          currentLine);
    } else if (line[4].isEmpty() || !globalServce.isValidDate(line[4])) {
      message = String.format("Missing or invalid date value on line %s.", currentLine);
    }

    return message;
  }

  private boolean hasDuplicateIDInFile(List<User> users) {
    List<User> duplicates = users
        .stream()
        .collect(Collectors.groupingBy(p -> p.getId(), Collectors.toList()))
        .values()
        .stream()
        .filter(i -> i.size() > 1)
        .flatMap(j -> j.stream())
        .collect(Collectors.toList());

    if (!duplicates.isEmpty()) {
      return true;
    }
    return false;
  }

  private boolean hasDuplicateLoginInFile(List<User> users) {
    List<User> duplicates = users
        .stream()
        .collect(Collectors.groupingBy(p -> p.getLogin(), Collectors.toList()))
        .values()
        .stream()
        .filter(i -> i.size() > 1)
        .flatMap(j -> j.stream())
        .collect(Collectors.toList());

    if (!duplicates.isEmpty()) {
      return true;
    }
    return false;
  }
}
