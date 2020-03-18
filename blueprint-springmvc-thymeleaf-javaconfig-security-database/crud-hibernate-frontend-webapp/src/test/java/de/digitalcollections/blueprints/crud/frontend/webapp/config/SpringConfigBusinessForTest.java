package de.digitalcollections.blueprints.crud.frontend.webapp.config;

import de.digitalcollections.blueprints.crud.business.api.service.RoleService;
import de.digitalcollections.blueprints.crud.business.api.service.UserService;
import de.digitalcollections.blueprints.crud.model.api.security.Role;
import java.io.Serializable;
import java.util.List;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Services context.
 */
@Configuration
public class SpringConfigBusinessForTest {

  @Bean
  public RoleService roleService() {
    final RoleService mock = Mockito.mock(RoleService.class);
    Mockito.when(mock.getAdminRole()).thenReturn(new Role() {

      @Override
      public List getAllowedOperations() {
        return null;
      }

      @Override
      public void setAllowedOperations(List allowedOperations) {
      }

      @Override
      public String getAuthority() {
        return Role.PREFIX + "ADMIN";
      }

      @Override
      public Serializable getId() {
        return null;
      }

      @Override
      public void setId(Serializable id) {
      }

      @Override
      public String getName() {
        return null;
      }

      @Override
      public void setName(String name) {
      }

    });
    return mock;
  }

  @Bean
  public UserService userService() {
    return Mockito.mock(UserService.class);
  }
}
