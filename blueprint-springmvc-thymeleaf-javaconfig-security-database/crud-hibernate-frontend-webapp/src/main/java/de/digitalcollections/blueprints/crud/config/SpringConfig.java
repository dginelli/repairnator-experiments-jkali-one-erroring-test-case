package de.digitalcollections.blueprints.crud.config;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;

@Configuration
@Import({SpringConfigSecurity.class, SpringConfigWeb.class}) //, SpringConfigBusiness.class, SpringConfigBackend.class})
@ComponentScan(basePackages = {"de.digitalcollections.blueprints.crud.config"})
public class SpringConfig implements EnvironmentAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfig.class);

  /**
   * Create a resource bundle for your messages ("messages.properties").
   *
   * This file goes in src/main/resources because you want it to appear at the root of the classpath on deployment.
   *
   * @return message source
   */
  @Bean(name = "messageSource")
  public MessageSource configureMessageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setCacheSeconds(5);
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Override
  public void setEnvironment(Environment environment) {
    String[] activeProfiles = environment.getActiveProfiles();
    String toString = Arrays.toString(activeProfiles);
    LOGGER.info("##### Active Profiles: " + toString);
  }
}
