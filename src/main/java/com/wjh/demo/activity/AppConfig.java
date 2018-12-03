package com.wjh.demo.activity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AppConfig {

    private Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public UserDetailsService myUserDetailsService() {

        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

        String[][] usersGroupsAndRoles = {
                {"小明", "password", "ROLE_ACTIVITI_USER", "GROUP_student"},
                {"班长", "password", "ROLE_ACTIVITI_USER", "GROUP_class_monitor"},
                {"班主任", "password", "ROLE_ACTIVITI_USER", "GROUP_class_adviser"},
                {"年级主任", "password", "ROLE_ACTIVITI_USER", "GROUP_grade_director"},
                {"校长", "password", "ROLE_ACTIVITI_USER", "GROUP_schoolmaster"},
                {"test", "123qwe", "ROLE_ACTIVITI_USER", "GROUP_other"},
                {"admin", "password", "ROLE_ACTIVITI_ADMIN"},
        };

        for (String[] user : usersGroupsAndRoles) {
            List<String> authoritiesStrings = Arrays.asList(Arrays.copyOfRange(user, 2, user.length));
            logger.info("> Registering new user: {} with the following Authorities [{}]"
                    + ", password [{}]", user[0], authoritiesStrings, passwordEncoder().encode(user[1]));
            inMemoryUserDetailsManager.createUser(new User(user[0], passwordEncoder().encode(user[1]),
                    authoritiesStrings.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList())));
        }

        return inMemoryUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
