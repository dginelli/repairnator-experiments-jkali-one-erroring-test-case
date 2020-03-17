package com.casafacilimoveis.configuracao;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * The type Security configuration.
 * @author Wender Galan
 * Created on: 10/08/2018
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * O método configura a segunraça do projeto para que todas as URL possam ser acessadas sem a necessidade de TOKEN
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //https://spring.io/blog/2013/08/21/spring-security-3-2-0-rc1-highlights-csrf-protection/
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll();
    }
}
