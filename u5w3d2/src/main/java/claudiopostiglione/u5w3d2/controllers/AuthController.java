package claudiopostiglione.u5w3d2.controllers;

import claudiopostiglione.u5w3d2.entities.Dipendente;
import claudiopostiglione.u5w3d2.exceptions.ValidationException;
import claudiopostiglione.u5w3d2.payload.DipendenteDTO;
import claudiopostiglione.u5w3d2.payload.LoginDTO;
import claudiopostiglione.u5w3d2.payload.LoginResponseDTO;
import claudiopostiglione.u5w3d2.services.AuthService;
import claudiopostiglione.u5w3d2.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//Viene creato un controller per due endpoint che useranno l'autenticazione:
// - Login
// - Register

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private DipendenteService dipendenteService;
    @Autowired
    private AuthService authService;

    //1.
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO loginBody) {
        return new LoginResponseDTO(this.authService.CheckAndCreate(loginBody));
    }

    //2.
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente createDipendente(@RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }

        return this.dipendenteService.saveDipendente(body);
    }

}
