package de.ndhbr.ynvest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConf extends WebSecurityConfigurerAdapter {

    /**
     * Those paths are available for every user
     */
    public static final String[] STATIC_PATHS = {
            "/style/**", "/js/**", "/img/**", "/fonts/**"
    };
    private static final String[] ALLOW_ACCESS_WITHOUT_AUTHENTICATION = {
            "/", "/login", "/error", "/register", "/faq", "/support",
            "/about"
    };

    /**
     * Those paths are only available for API Access
     */
    public static final String[] ALLOW_API_ACCESS = {
            "/api/**"
    };

    /**
     * Those paths are only available if the user is
     * authenticated, but not verified
     */
    private static final String[] ALLOW_NON_VERIFIED_ACCESS = {
            "/verify/**"
    };

    /**
     * Those paths are only available if the user is
     * authenticated and verified
     */
    private static final String[] ALLOW_VERIFIED_ACCESS = {
            "/orders/**", "/portfolio/**", "/performance/**",
            "/search/**", "/bank-account/**"
    };

    @Autowired
    private UserDetailsService userSecurityService;

    @Autowired
    private SecurityUtils securityUtilities;

    private BCryptPasswordEncoder passwordEncoder() {
        return securityUtilities.passwordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // No login required
                .antMatchers(
                        Stream.of(ALLOW_ACCESS_WITHOUT_AUTHENTICATION, STATIC_PATHS)
                                .flatMap(Stream::of)
                                .toArray(String[]::new)
                )
                .permitAll()
                .and()
                // Not verified login
                .authorizeRequests()
                .antMatchers(ALLOW_NON_VERIFIED_ACCESS)
                .hasRole("NOT_VERIFIED")
                .and()
                // Verified login required
                .authorizeRequests()
                .antMatchers(ALLOW_VERIFIED_ACCESS)
                .hasRole("VERIFIED")
                .and()
                // API Access
                .authorizeRequests()
                .antMatchers(ALLOW_API_ACCESS)
                .hasRole("API_USER")
                .and()
                // Not verified login sufficient
                .authorizeRequests()
                .anyRequest()
                .denyAll();

        http
                .formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/?logout")
                .permitAll()
                .and()
                .httpBasic();

        // Cross-Site Request Forgery ausschalten
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService)
                .passwordEncoder(passwordEncoder());
    }
}
