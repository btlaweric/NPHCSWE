package com.ericlaw.NPHCSWE;

import static org.assertj.core.api.Assertions.assertThat;

import com.ericlaw.NPHCSWE.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

  @Autowired
  private UserController userController;

  @Test
  public void contextLoads() throws Exception {
    assertThat(userController).isNotNull();
  }
}
