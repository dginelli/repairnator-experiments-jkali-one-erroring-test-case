package de.digitalcollections.blueprints.crud.backend.impl.jpa.repository;

import de.digitalcollections.blueprints.crud.backend.api.repository.OperationRepository;
import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.OperationImplJpa;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class OperationRepositoryImplJpa extends AbstractPagingAndSortingRepositoryImplJpa<OperationImplJpa, Long, OperationRepositoryJpa>
        implements OperationRepository<OperationImplJpa, Long> {

  @Autowired
  @Override
  void setJpaRepository(OperationRepositoryJpa jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public OperationImplJpa create() {
    return new OperationImplJpa();
  }

  @Override
  public List<OperationImplJpa> findAll(Sort sort) {
    return jpaRepository.findAll(sort);
  }

  @Override
  public OperationImplJpa findByName(String name) {
    return jpaRepository.findByName(name);
  }

}
