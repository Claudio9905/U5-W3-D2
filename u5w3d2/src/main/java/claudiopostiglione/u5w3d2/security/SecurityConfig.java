package claudiopostiglione.u5w3d2.security;

import io.jsonwebtoken.security.Password;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity // Tale annotazione indica una configurazione speciale per Spring Security
@EnableMethodSecurity // Lo si utilizza per le regole di AUTHORIZATION specifiche per ogni endpoint
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


        // Ulteriori funzionalità
        httpSecurity.cors((Customizer.withDefaults()));  //Obbligatorio per il bean sottostante per la configurazione cors

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder gerBCrypt(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3001"));
        // Mi sto creando una WHITELIST di uno o più indirizzi FRONTEND che voglio possano accedere a questo BE senza problemi di CORS
        // Volendo (anche se rischioso, ma utile per API pubbliche) potrei mettere '*' che permette l'accesso a tutti
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // applico la configurazione di sopra a tutti gli endpoint ("/**")
        return source;
    }// N.B. Non dimenticarsi di aggiungere nella filter chain httpSecurity.cors(Customizer.withDefaults())!!!!!!!!!!!!!!!


}
