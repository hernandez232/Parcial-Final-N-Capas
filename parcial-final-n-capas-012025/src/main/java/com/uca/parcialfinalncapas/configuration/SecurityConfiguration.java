package com.uca.parcialfinalncapas.configuration;

import com.uca.parcialfinalncapas.security.JwtAuth;
import com.uca.parcialfinalncapas.security.JwtAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private JwtAuthFilter jwtAuthFilter;
    private JwtAuth jwtAuth;

    @Bean
        // Intercepta las peticiones HTTP para aplicar medidas de seguridad como autenticación, autorización, CORS, etc.
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests
                            //.anyRequest().authenticated();
                            /*.requestMatchers(HttpMethod.GET, "/api/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/employee/**").hasRole("USER")*/
                            .requestMatchers("/api/auth/**").permitAll() //Permits urls to be public, no authorization needed
                            .anyRequest().authenticated();
                }).httpBasic(Customizer.withDefaults());

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuth));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    // Para que la contraseña sea válida, ya que Spring espera una contraseña encriptada
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    AuthenticationManager es el componente central responsable de autenticar a los usuarios. Se encarga de procesar una
    solicitud de autenticación y determinar si un usuario tiene credenciales válidas.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
