package claudiopostiglione.u5w3d2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Tale annotazione indica una configurazione speciale per Spring Security
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // tale bean servirà per modificare la configurazione di default di Spring Security:
        //1. Disabilitazione dell'autenticazione basata sul form proposto da Spring
        httpSecurity.formLogin(formLogin -> formLogin.disable());
        //2.Togliere la protezione di CSRF
        httpSecurity.csrf(csrf -> csrf.disable());
        //Togliere anche le sessioni perchè l'autenticazione basata sul token è il contrario dell'utilizzare una sessione
        httpSecurity.sessionManagement((sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS)));

        //si modifica il comportamento di funzionalità pre-esistenti
        httpSecurity.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll());
        //Si va a disabilitare il comportamento di default di Security che restituisce 401 per ogni richiesta su ogni endpoint.
        //Si va ad implementare un meccanismo custom di autenticazione, dove esso si occuperà di fare i controlli e nel caso rispondere con 401.
        //Quindi, con questo meccanismo custom, andremo a selezionare quali sono gli endpoint da proteggere e quali no.

        return httpSecurity.build();
    }
}
