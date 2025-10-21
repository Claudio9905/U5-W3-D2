package claudiopostiglione.u5w3d2.security;

import claudiopostiglione.u5w3d2.entities.Dipendente;
import claudiopostiglione.u5w3d2.exceptions.UnauthorizedExcpetion;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String keySecret;

    public String createToken(Dipendente dipendente) {
        //Questa classe sfrutta due metodi:
        // - Il builder() che serve per creare il token
        // - Il parser() ver la verifica del token

        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // questo metodo ci da la data di emissione del token, messa in millisecondi
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) //questo metodi ci da la data di scadenza, sempre in millisecondi
                .subject(String.valueOf(dipendente.getId())) // questo metodo ci riporta a chi appartiene il token
                .signWith(Keys.hmacShaKeyFor(keySecret.getBytes())) // Viene firmato il token con un algoritmo specifico HMAC fornendogli un SECRET che solo il server conosce (serve per generare token e verificare)
                .compact();
    }

    public void verifyToken(String accessToken) {
        try {
            //parser lancia varie eccezioni a seconda del problema che ha il token
            // quindi un'eccezione sul token che è scaduto, un'altro che è stato manipolato, un altro se il token è malformato
            // il tipo di eccezione viene convertito in -> 401
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(keySecret.getBytes())).build().parse(accessToken);
        } catch (Exception ex) {
            throw new UnauthorizedExcpetion("Ci sono stati errori nel token, effettua il login");
        }
    }


}
