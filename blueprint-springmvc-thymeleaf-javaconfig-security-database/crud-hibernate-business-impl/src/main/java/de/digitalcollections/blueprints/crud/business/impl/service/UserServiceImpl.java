package de.digitalcollections.blueprints.crud.business.impl.service;

import de.digitalcollections.blueprints.crud.backend.api.repository.UserRepository;
import de.digitalcollections.blueprints.crud.business.api.service.RoleService;
import de.digitalcollections.blueprints.crud.business.api.service.UserService;
import de.digitalcollections.blueprints.crud.model.api.security.Operation;
import de.digitalcollections.blueprints.crud.model.api.security.Role;
import de.digitalcollections.blueprints.crud.model.api.security.User;
import java.util.ArrayList;
import java.util.List;
import de.digitalcollections.blueprints.crud.business.impl.validators.PasswordsValidatorParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Service for User handling.
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService<User, Long> {

  @Autowired
  @Qualifier("passwordsValidator")
  private Validator passwordsValidator;

  @Autowired
  @Qualifier("uniqueUsernameValidator")
  private Validator uniqueUsernameValidator;

  @Autowired
  private RoleService roleService;

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional(readOnly = false)
  public User activate(Long id) {
    User user = (User) userRepository.findOne(id);
    user.setEnabled(true);
    userRepository.save(user);
    return user;
  }

  @Override
  public User create() {
    return userRepository.create();
  }

  @Override
  @Transactional(readOnly = false)
  public User create(User user, String password1, String password2, Errors results) {
    uniqueUsernameValidator.validate(user, results);
    if (!results.hasErrors()) {
      return save(password1, password2, user, results);
    }
    return null;
  }

  @Override
  public User createAdminUser() {
    User user = create();
    Role adminRole = roleService.getAdminRole();
    List<Role> roles = new ArrayList<>();
    roles.add(adminRole);
    user.setRoles(roles);
    return user;
  }

  @Override
  @Transactional(readOnly = false)
  public User deactivate(Long id) {
    User user = (User) userRepository.findOne(id);
    user.setEnabled(false);
    userRepository.save(user);
    return user;
  }

  @Override
  public boolean doesActiveAdminUserExist() {
    List findActiveAdminUsers = userRepository.findActiveAdminUsers();
    if (findActiveAdminUsers != null && !findActiveAdminUsers.isEmpty()) {
      return true;
    }
    return false;
  }

  @Override
  public User get(Long id) {
    return (User) userRepository.findOne(id);
  }

  @Override
  public List<User> getAll() {
    return userRepository.findAll(new Sort(Sort.Direction.ASC, "lastname"));
  }

  /*
     see: http://stackoverflow.com/questions/19302196/transaction-marked-as-rollback-only-how-do-i-find-the-cause
     When you mark your method as @Transactional, occurrence of any exception inside your method will mark the surrounding TX as roll-back only (even if you catch them). You can use other attributes of @Transactional annotation to prevent it of rolling back like:

     @Transactional(rollbackFor=MyException.class, noRollbackFor=MyException2.class)
   */
  @Override
  @Transactional(readOnly = true, noRollbackFor = UsernameNotFoundException.class)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username);
    if (user == null || !user.isEnabled()) {
      throw new UsernameNotFoundException(String.format("User \"%s\" was not found.", username));
    }
    List<GrantedAuthority> authorities = collectUserAuthorities(user);

    return buildUserForAuthentication(user, authorities);
  }

  private List<GrantedAuthority> collectUserAuthorities(User user) {
    List<GrantedAuthority> result = new ArrayList<>();
    // Build user's authorities
    List<Role> userRoles = user.getRoles();
    for (Role userRole : userRoles) {
      result.add(new SimpleGrantedAuthority(userRole.getName()));
      List<Operation> allowedOperations = userRole.getAllowedOperations();
      for (Operation allowedOperation : allowedOperations) {
        result.add(new SimpleGrantedAuthority(allowedOperation.getName()));
      }
    }
    return result;
  }

  private org.springframework.security.core.userdetails.User buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(),
            user.isEnabled(), true, true, true, authorities);
  }

  @Override
  @Transactional(readOnly = false)
  public User update(User user, String password1, String password2, Errors results) {
    return save(password1, password2, user, results);
  }

  private User save(String password1, String password2, User user, Errors results) {
    final PasswordsValidatorParams passwordsValidatorParams = new PasswordsValidatorParams(password1, password2, user.
            getPasswordHash());
    passwordsValidator.validate(passwordsValidatorParams, results);
    if (!results.hasErrors()) {
      String password = passwordsValidatorParams.getPassword1();
      if (!StringUtils.isEmpty(password)) {
        user.setPasswordHash(password);
      }
      userRepository.save(user);
    }
    return user;
  }
}
