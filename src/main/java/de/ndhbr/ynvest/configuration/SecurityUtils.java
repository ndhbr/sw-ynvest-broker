package de.ndhbr.ynvest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class SecurityUtils {

    //@Value("${user_password_salt}") // TODO: Does not work
    private static String salt = "ynvest";

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(9, new SecureRandom(salt.getBytes()));
    }
}
