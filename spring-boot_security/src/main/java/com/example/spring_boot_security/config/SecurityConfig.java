package com.example.spring_boot_security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.apache.coyote.http11.Constants.a;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider ()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

          provider.setUserDetailsService(userDetailsService);
          // provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()) //use for when you give a password without encryted and matches the password which is saves in DataBase
          provider.setPasswordEncoder(new BCryptPasswordEncoder()); // use when you send the password which is encrypted and stores in database

        return provider;
    }

    @Bean
    public SecurityFilterChain SFC(HttpSecurity http) throws Exception {

        http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/register","/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                        .httpBasic(Customizer.withDefaults())
                          .sessionManagement(ses -> ses.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                         .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();


    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }














//    @Bean
//    public UserDetailsService usd()
//    {
//        UserDetails admin = User.withDefaultPasswordEncoder().username("Radha").password("1212").roles("ADMIN").build();
//        UserDetails user = User.withDefaultPasswordEncoder().username("Nonu").password("1212").roles("USER").build();
//
//
//        return new InMemoryUserDetailsManager(admin,user);
//    }
}
