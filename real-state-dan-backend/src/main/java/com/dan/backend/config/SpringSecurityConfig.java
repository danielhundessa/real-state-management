package com.dan.backend.config;

import com.dan.backend.enumSet.RoleType;
import com.dan.backend.filter.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //By default, AuthenticationManager(required to authenticate later) is not publicly accessible, and we need to explicitly expose it as a bean in our configuration class.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Enable CSRF and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();


        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                )
                .and();
        // Set permissions on endpoints
        http.authorizeHttpRequests()
                .requestMatchers("/v3/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/api/v1/authenticate/**").permitAll()

                .requestMatchers(HttpMethod.GET,"/api/v1/users").hasAuthority(RoleType.ADMIN.name())
                .requestMatchers(HttpMethod.GET,"/api/v1/users/*").hasAnyAuthority(RoleType.CUSTOMER.name(), RoleType.OWNER.name())

                .requestMatchers(HttpMethod.GET, "/api/v1/properties/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/properties").hasAuthority(RoleType.OWNER.name())
                .requestMatchers(HttpMethod.PUT, "/api/v1/properties/*").hasAuthority(RoleType.OWNER.name())

                .requestMatchers(HttpMethod.POST, "/api/v1/properties/*/inquiry").hasAuthority(RoleType.CUSTOMER.name())
                .requestMatchers(HttpMethod.GET, "/api/v1/properties/*/inquiry").hasAnyAuthority(RoleType.CUSTOMER.name(), RoleType.OWNER.name())

                .requestMatchers(HttpMethod.POST, "/api/v1/properties/*/favourite").hasAuthority(RoleType.CUSTOMER.name())
                .requestMatchers(HttpMethod.DELETE, "/api/v1/properties/*/favourite").hasAuthority(RoleType.CUSTOMER.name())


                .requestMatchers(HttpMethod.POST, "/api/v1/properties/*/offer").hasAuthority(RoleType.CUSTOMER.name())
                .requestMatchers(HttpMethod.GET, "/api/v1/properties/*/offer").hasAnyAuthority(RoleType.CUSTOMER.name(), RoleType.OWNER.name())
                .requestMatchers(HttpMethod.PUT, "/api/v1/properties/*/offer").hasAnyAuthority(RoleType.CUSTOMER.name(), RoleType.OWNER.name())

                .requestMatchers(HttpMethod.GET, "/api/v1/offers").hasAnyAuthority(RoleType.CUSTOMER.name(), RoleType.OWNER.name())
                .requestMatchers(HttpMethod.GET, "/api/v1/offers/owner/*").hasAuthority(RoleType.OWNER.name())


                .requestMatchers(HttpMethod.GET, "/api/v1/inquiry").hasAnyAuthority(RoleType.CUSTOMER.name(), RoleType.OWNER.name())


                .requestMatchers(HttpMethod.PUT, "/api/v1/offers").hasAnyAuthority(RoleType.CUSTOMER.name(), RoleType.OWNER.name())

                .requestMatchers("/api/v1/admin/**").hasAuthority(RoleType.ADMIN.name())
                .anyRequest().authenticated();

        // Add JWT token filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // We enabled CORS above.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
