package com.NA.social.core.ultis;

import com.NA.social.core.entity.User;
import com.NA.social.core.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Random;

public class SecurityHelper {
    public static boolean isLogged() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    public static void setAuthentication(UsernamePasswordAuthenticationToken token) {
        SecurityContextHolder.getContext().setAuthentication(token);
    }
    public static String generateRandomNumberString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generates a random number between 0 and 9
            sb.append(digit);
        }

        return sb.toString();
    }


    public static String getLoggedUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    public static User getAccountFromLogged(UserRepository accountRepository) {
        String username = SecurityHelper.getLoggedUser();
        if (username == null) return null;
        User account = accountRepository.findByUsername(username).orElse(null);
        if (account == null) return null;
        return account;
    }
}
