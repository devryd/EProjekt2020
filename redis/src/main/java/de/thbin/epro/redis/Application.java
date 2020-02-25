package de.thbin.epro.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * Application file for the project
 * generates ApplicationContext for Spring
 *
 * @author  Christian Gebhard
 */
@SpringBootApplication(exclude={SecurityAutoConfiguration.class}, scanBasePackages = {"de.thbin.epro"})
public class Application {

    /**
     * main method to start the application
     * @param args delegated to method SpringApplication.run
     */
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        Assert.notNull(ctx, "Application Context must not be null");
    }

}
