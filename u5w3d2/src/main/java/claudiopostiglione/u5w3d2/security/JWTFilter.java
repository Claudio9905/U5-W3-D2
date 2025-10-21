package claudiopostiglione.u5w3d2.security;

import claudiopostiglione.u5w3d2.entities.Dipendente;
import claudiopostiglione.u5w3d2.services.DipendenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component // per potero inserire nella FilterChain
public class JWTFilter extends OncePerRequestFilter {

    //L'importanza dei filtri è che hanno accesso alle richieste che arrivano e inoltre possono anche mandare risposte
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private DipendenteService dipendenteService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Eseguiremo vari steps:
        // 1. Verifica della presenza di un header chiamato Authorization e che sia nel formato giusto
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new UnavailableException("Inserire il token nell'authorization header e nel formato giusto!");
        }

        // 2. Se l'header esiste, catturiamo il token
        String accessToken = authHeader.replace("Bearer ", "");

        // 3. Verifichiamo che il token ricevuto sia valido ( se non è stato modificato, se è scaduto o meno, se è malformato
        jwtTools.verifyToken(accessToken);


        // Se qualcosa è andato storto -> 401

        //--------------------------------------- AUTORIZZAZIONE -------------------------------------------

        // Ricerca del dipendente nel DB
        UUID idDipendente = jwtTools.extractIdFromToken(accessToken);
        Dipendente dipendenteFound = this.dipendenteService.findDipendenteById(idDipendente);

        // Associazione del dipendente al Security Context
        Authentication authentication = new UsernamePasswordAuthenticationToken(dipendenteFound, null, dipendenteFound.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Aggiorniamo il Security Context associando ad esso l'utente corrente ed i suoi ruoli

        // 4. se tutto è andato bene, passiamo la richiesta al prossimo filtro, o direttamente al controller
        filterChain.doFilter(request, response); // questo metodo chiama il prossimo elemento della catena ( o un filtro o direttamente il controller)
    }

    //Questo override permette di disabilitare il lavoro del filtro per determinati tipi di endpoint
    // Ad esempio, posso disabilitare tutte le richieste dirette al controller /auth

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/login", request.getServletPath());
    }
}
