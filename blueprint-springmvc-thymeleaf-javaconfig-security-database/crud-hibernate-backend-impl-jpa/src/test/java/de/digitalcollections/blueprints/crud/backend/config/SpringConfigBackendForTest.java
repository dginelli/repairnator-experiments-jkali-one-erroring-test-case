package de.digitalcollections.blueprints.crud.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Backend configuration.
 */
@Configuration
@Import({SpringConfigBackendDatabaseForTest.class})
@ComponentScan(basePackages = {
  "de.digitalcollections.blueprints.crud.backend.impl.jpa.repository"})
@PropertySource(value = {
  "classpath:de/digitalcollections/blueprints/crud/config/SpringConfigBackend-${spring.profiles.active:local}.properties"
})
public class SpringConfigBackendForTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfigBackendForTest.class);

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}
