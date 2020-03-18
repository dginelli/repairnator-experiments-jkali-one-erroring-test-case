package de.digitalcollections.blueprints.crud.backend.api.repository;

import de.digitalcollections.blueprints.crud.model.api.security.User;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository for User persistence handling.
 *
 * @param <T> entity instance
 * @param <ID> unique id
 */
public interface UserRepository<T extends User, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

  T create();

  @Override
  List<T> findAll(Sort sort);

  T findByEmail(String email);

  List<T> findActiveAdminUsers();
}
