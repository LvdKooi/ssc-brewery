package guru.sfg.brewery.config;

import guru.sfg.brewery.security.PathParameterAuthFilter;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        var filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public PathParameterAuthFilter pathParameterAuthFilter(AuthenticationManager authenticationManager) {
        var filter = new PathParameterAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(pathParameterAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class).csrf().disable();
        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class).csrf().disable();
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