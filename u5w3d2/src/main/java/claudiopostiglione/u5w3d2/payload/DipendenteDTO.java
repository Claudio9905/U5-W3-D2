package claudiopostiglione.u5w3d2.payload;

import claudiopostiglione.u5w3d2.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DipendenteDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 20, message = "Il nome deve avere un minimo 2 due caratteri e un massimo di 20")
        String nome,
        @NotBlank(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 20, message = "Il cognome deve avere un minimo 2 due caratteri e un massimo di 20")
        String cognome,
        @NotBlank(message = "Lo username è obbligatorio")
        @Size(min = 2, max = 20, message = "Lo username deve avere un minimo 2 due caratteri e un massimo di 20")
        String username,
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'indirizzo e-mail inserito non è del formato corretto")
        String email,
        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 8, max = 20, message = "La password deve avere un minimo di 8 caratteri e un massimo di 20")
        String password,
        @NotNull(message = "Il ruolo non può essere nullo")
        Role ruolo
) {
}
