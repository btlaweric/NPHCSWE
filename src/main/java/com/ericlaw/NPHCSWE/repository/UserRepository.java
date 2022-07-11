package com.ericlaw.NPHCSWE.repository;

import com.ericlaw.NPHCSWE.model.user.*;
import com.ericlaw.NPHCSWE.model.user.UserFilter;
import java.util.List;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  @Query(
    value = "SELECT u FROM User u WHERE " +
    "u.salary >= :#{#filter.minSalary} AND " +
    "u.salary <= :#{#filter.maxSalary} "
  )
  List<User> findAndFilterUsers(
    @Param("filter") UserFilter filter,
    Pageable pageable
  );

  public boolean existsById(String id);

  public boolean existsByLogin(String login);

  public boolean existsByLoginAndIdNot(String login, String id);

  public User getUserById(String id);
}
