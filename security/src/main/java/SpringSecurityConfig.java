import com.google.api.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    // create two global users
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user").password("password").roles("USER")
            .and()
            .withUser("admin").password("adminpass").roles("USER", "ADMIN");
    }

    // secure endpoints w/ basic http auth
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(HttpMethod.DELETE, "/v2/service_instances/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "v2/service_instance/**/service_bindings/**").hasRole("USER")
                    .antMatchers(HttpMethod.PUT, "v2/service_instance/**/service_bindings/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PATCH, "v2/service_instances/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "v2/service_instances/**").hasRole("USER")
                    .antMatchers(HttpMethod.PUT, "v2/service_instances/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "v2/service_instances/**/service_bindings/**/last_operation").hasRole("USER")
                    .antMatchers(HttpMethod.GET, "/v2/service_instances/**/last_operation").hasRole("USER")
                    .antMatchers(HttpMethod.GET, "/v2/catalog").hasRole("USER")
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }

}
