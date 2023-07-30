package com.plot.plotserver.util;

import com.plot.plotserver.security.auth.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityContextHolderUtil {

    public static Long getUserId(){
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = principal.getUser().getId();
        return userId;
    }

    public static String getUsername(){
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        return username;
    }
}
