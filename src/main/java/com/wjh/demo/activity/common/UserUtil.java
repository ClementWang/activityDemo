package com.wjh.demo.activity.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserUtil {

    public static UserDetails currentUser() {
        return (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    public static String currentUserName() {
        return currentUser().getUsername();
    }
    
    public static List<String> currentUserAuthoritys() {
        return currentUser().getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
    }
}
