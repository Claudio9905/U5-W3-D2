package claudiopostiglione.u5w3d2.services;

import claudiopostiglione.u5w3d2.entities.Dipendente;
import claudiopostiglione.u5w3d2.exceptions.UnauthorizedExcpetion;
import claudiopostiglione.u5w3d2.payload.LoginDTO;
import claudiopostiglione.u5w3d2.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



// Questo service gestire la logica di business dell'autenticazione, quindi si baserà sulla creazione del token verificando le credenziali


@Service
public class AuthService {

    @Autowired
    private DipendenteService dipendenteService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String CheckAndCreate(LoginDTO bodyLogin){
        // Controllo sulle credenziali: verifico nel DB che esista un utente con qull'email e poi nel caso esista, verifico la password
        Dipendente dipendenteFound = this.dipendenteService.findDipendenteByEmail(bodyLogin.email());

        if(bcrypt.matches(bodyLogin.password(), dipendenteFound.getPassword())){
            return jwtTools.createToken(dipendenteFound);
        } else {
                throw new UnauthorizedExcpetion("Credenziali non valide");
        }
    }

}
