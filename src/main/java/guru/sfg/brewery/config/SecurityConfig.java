package guru.sfg.brewery.config;

import guru.sfg.brewery.security.CustomPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests(autorize -> {
                    // antMatchers
                    autorize.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
                    autorize.antMatchers("/beers/find", "/beers*").permitAll();
                    autorize.antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll();
                    // mvcMatcher
                    autorize.mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
                })
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and().httpBasic();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
//        Spring's default PasswordEncoderFactories
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

//        Own implementation
//        return CustomPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
                .password("{bcrypt}$2a$10$LJYXeuWkZ8m8s5j5J3.3QeukoVNu/7m4z/KAm6UzOCzXD1I770BQS")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{sha256}9154b3c5177db39e4398fc38811de0bdc56797232a5c414987d051df926fc0cd63311c3df1a6d515")
                .roles("USER")
                .and()
                .withUser("scott")
                .password("{ldap}{SSHA}yHI9Z5qpaqCWvLndb/AmzYTrIc0ZsmUZ059mCw==")
                .roles("CUSTOMER");
    }
}