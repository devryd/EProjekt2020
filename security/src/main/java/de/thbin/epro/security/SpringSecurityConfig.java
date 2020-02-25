package de.thbin.epro.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Random;

/**
 * This class overwrites Spring standard Security Configuration
 *
 * @author  Christian Gebhard
 */
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * constructor
     * calls ctor of super class
     * invokes method to generate a random password and print it
     */
    SpringSecurityConfig() {
        super();
        generatePassword();
    }

    /**
     * create a global admin user to secure every rest endpoint
     * @param AuthenticationManagerBuilder
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("admin").password(passwordEncoder.encode(password)).roles("USER", "ADMIN");
    }

    /**
     * random password generator
     * prints out the generated password to console
     */
    private void generatePassword() {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        password = salt.toString();

        System.out.println("Auto-generated password: " + password);
    }

    /**
     * password Encoder
     */
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    /**
     * random generated password
     */
    private String password;

}
