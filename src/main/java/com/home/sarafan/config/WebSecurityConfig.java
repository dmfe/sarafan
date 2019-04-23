package com.home.sarafan.config;

import com.home.sarafan.domain.User;
import com.home.sarafan.repositories.UserRepository;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserRepository userRepository) {
        return map -> {
            String id = String.valueOf(map.get("sub"));
            User user = userRepository.findById(id).orElseGet(() -> {
                User newUser = new User();

                newUser.setId(id);
                newUser.setName(String.valueOf(map.get("name")));
                newUser.setGender(String.valueOf(map.get("gender")));
                newUser.setEmail(String.valueOf(map.get("email")));
                newUser.setLocale(String.valueOf(map.get("locale")));
                newUser.setUserpic(String.valueOf(map.get("picture")));

                return newUser;
            });

            user.setLastVisit(LocalDateTime.now());

            return userRepository.save(user);
        };
    }
}
