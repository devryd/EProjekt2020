
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Random;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    SpringSecurityConfig() {
        super();
        generatePassword();
    }

    // create global admin user
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("admin").password(passwordEncoder.encode(password)).roles("USER", "ADMIN");
    }

    // ToDo random pw generate + print
    // lower case-letters a->z (97-122)
    // 10 chars
    private void generatePassword() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < targetStringLength; i++) {
            stringBuilder.append(random.ints(leftLimit, rightLimit+1).toString());
        }
        password = stringBuilder.toString();
        System.out.println("Auto-generated password:" + password);
    }

    // password encoder
    @Autowired
    private PasswordEncoder passwordEncoder;
    // password
    private String password;

}
