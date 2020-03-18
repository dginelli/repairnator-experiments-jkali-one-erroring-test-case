package de.digitalcollections.blueprints.crud.config;

import de.digitalcollections.blueprints.crud.business.api.service.RoleService;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Contains Spring Security related configuration.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringConfigSecurity extends WebSecurityConfigurerAdapter {

  @Autowired(required = true)
  PersistentTokenRepository persistentTokenRepository;

  @Autowired(required = true)
  private UserDetailsService userDetailsService; // provided by component scan

  @Autowired(required = true)
  private RoleService roleService;

  @Bean
  public AuthenticationProvider authProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setPasswordEncoder(passwordEncoder());
    authProvider.setUserDetailsService(userDetailsService);
    return authProvider;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
    http.csrf().requireCsrfProtectionMatcher(new RequestMatcher() {
      private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
      private final RegexRequestMatcher apiMatcher = new RegexRequestMatcher("/api/.*", null);
      private final RegexRequestMatcher uploadsMatcher = new RegexRequestMatcher("/uploads", "POST", true);

      @Override
      public boolean matches(HttpServletRequest request) {
        // No CSRF due to allowedMethod
        if (allowedMethods.matcher(request.getMethod()).matches()) {
          return false;
        }

        // No CSRF due to api/upload call
        if (apiMatcher.matches(request) || uploadsMatcher.matches(request)) {
          return false;
        }

        // CSRF for everything else that is not an API or upload call or an allowedMethod
        return true;
      }
    });

    // Login and error pages must be accessible to all
    http.authorizeRequests().
            antMatchers("/api/**/*", "/cms/**/*", "/error*", "/oai*", "/object/**", "/login*", "/setup/**", "/uploads/**").
            permitAll();
    // Needed for preview
    http.authorizeRequests().antMatchers("/resource/**", "/iiif/**", "/pages/preview/**").permitAll();
    // Assets must be accessible to all, too
    http.authorizeRequests().
            antMatchers("/favicon.ico", "/css/**", "/images/**", "/img/**", "/js/**", "/fonts/*", "/less/*").permitAll();
    http.authorizeRequests().antMatchers("/users/**").hasAnyAuthority(roleService.getAdminRole().getAuthority());

    // else: authenticate please
    http.authorizeRequests()
            .anyRequest().authenticated()
            .and().formLogin()
            .loginPage("/login")
            .permitAll()
            .and().logout()
            .logoutUrl("/logout")
            .permitAll()
            .and().rememberMe()
            .tokenRepository(persistentTokenRepository)
            // 14 days = 14 * 24 h/d * 3600 s/h = 1209600 s
            .tokenValiditySeconds(14 * 24 * 3600)
            .userDetailsService(userDetailsService)
            .and().httpBasic();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authProvider());
  }

  @Bean
  public Object passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
