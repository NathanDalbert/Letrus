package br.com.letrus.infra.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sem sessão de estado
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Permite acesso livre ao login
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Permite acesso livre ao registro
                        .requestMatchers(HttpMethod.POST, "/api/chat-gpt/completar").permitAll() // Permite acesso ao chat GPT
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Permite acesso livre ao registro
                        .requestMatchers(HttpMethod.POST, "/text-correction/check-grammar").permitAll() // Permite acesso ao chat GPT
                        .requestMatchers(HttpMethod.POST, "/api/ocr/detect-text").permitAll() // Permite acesso ao OCR
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Acesso ao Swagger
                        .anyRequest().authenticated()) // Qualquer outra requisição requer autenticação
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Filtro de segurança
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configuração de CORS
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Codificador de senha
    }

    // Configuração de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:63342")); // Adiciona a origem do frontend corretamente
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Inclui "OPTIONS" para requisição prévia
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept")); // Cabeçalhos permitidos
        configuration.setAllowCredentials(true); // Permite credenciais (cookies, sessões, etc)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a configuração a todas as rotas
        return source;
    }
}
