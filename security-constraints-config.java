import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "security-constraints")
public class SecurityConstraintsProperties {
    private List<SecurityConstraint> securityConstraints;

    public List<SecurityConstraint> getSecurityConstraints() {
        return securityConstraints;
    }

    public void setSecurityConstraints(List<SecurityConstraint> securityConstraints) {
        this.securityConstraints = securityConstraints;
    }

    public static class SecurityConstraint {
        private List<String> authRoles;
        private List<SecurityCollection> securityCollections;

        public List<String> getAuthRoles() {
            return authRoles;
        }

        public void setAuthRoles(List<String> authRoles) {
            this.authRoles = authRoles;
        }

        public List<SecurityCollection> getSecurityCollections() {
            return securityCollections;
        }

        public void setSecurityCollections(List<SecurityCollection> securityCollections) {
            this.securityCollections = securityCollections;
        }
    }

    public static class SecurityCollection {
        private List<String> paths;
        private List<String> methods;

        public List<String> getPaths() {
            return paths;
        }

        public void setPaths(List<String> paths) {
            this.paths = paths;
        }

        public List<String> getMethods() {
            return methods;
        }

        public void setMethods(List<String> methods) {
            this.methods = methods;
        }
    }
}

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityConstraintsProperties securityConstraints;

    public SecurityConfig(SecurityConstraintsProperties securityConstraints) {
        this.securityConstraints = securityConstraints;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> {
                // Parcourt chaque contrainte de sécurité
                securityConstraints.getSecurityConstraints().forEach(constraint -> {
                    // Parcourt chaque collection de sécurité
                    constraint.getSecurityCollections().forEach(collection -> {
                        // Parcourt chaque chemin
                        collection.getPaths().forEach(path -> {
                            // Parcourt chaque méthode
                            collection.getMethods().forEach(method -> {
                                RequestMatcher matcher = createRequestMatcher(path, method);
                                
                                authz.requestMatchers(matcher)
                                    .hasAnyAuthority(
                                        constraint.getAuthRoles().stream()
                                            .map(role -> "ROLE_" + role.toUpperCase())
                                            .toArray(String[]::new)
                                    );
                            });
                        });
                    });
                });
                
                // Configuration par défaut
                authz.anyRequest().authenticated();
            })
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    private RequestMatcher createRequestMatcher(String path, String method) {
        return request -> 
            request.getRequestURI().matches(path.replace("**", ".*")) && 
            request.getMethod().equalsIgnoreCase(method);
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}
