package claudiopostiglione.u5w3d2.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record PrenotazioneDTO(

        @JsonFormat(pattern = "dd-MM-yyyy")
        @NotNull(message = "La data della richiesta non può essere nulla")
        LocalDate dataRichiesta,
        @Size(min = 0, max = 150, message = "Le note/preferenze possono non esserci oppure avere un massimo di 150 caratteri")
        String notePreferenze,
        @NotNull(message = "L'ID del dipendente non può essere nullo")
        UUID dipendenteId,
        @NotNull(message = "L'ID del viaggio non può essere nullo")
        UUID viaggioId
) {
}
