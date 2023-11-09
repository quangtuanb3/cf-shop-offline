package com.cg.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }


    @Configuration
    public static class PasswordEncoderConfiguration {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(10);
        }
    }

    @Bean
    public DaoAuthenticationProvider uniProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationEntryPoint restServicesEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/**").disable();
        http.httpBasic().authenticationEntryPoint(restServicesEntryPoint());

        http.authorizeRequests()
                .antMatchers(
                        "/",
                        "/api/auth/login",
                        "/api/auth/register",
                        "/login", "/login/**",
                        "/logout", "/logout/**",
                        "/api/test"
                ).permitAll()
                .antMatchers("/dashboard", "/dashboard/**").hasAnyAuthority("ADMIN")
                .antMatchers("/shop", "/shop/**").hasAnyAuthority("STAFF")
//                .antMatchers("/shop","/shop/**")
                .antMatchers("/resources/**", "/assets/**").permitAll()
//                .antMatchers(
//
//                        "/v3/api-docs",
//                        "/swagger-resources/configuration/ui",
//                        "/configuration/ui",
//                        "/swagger-resources",
//                        "/swagger-resources/configuration/security",
//                        "/configuration/security",
//                        "/swagger-ui/**"
//                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
//                .loginProcessingUrl("/login")
                .loginPage("/login")
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JWT")
                .invalidateHttpSession(true)
        ;

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).exceptionHandling().accessDeniedPage("/403");

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();
    }
}
