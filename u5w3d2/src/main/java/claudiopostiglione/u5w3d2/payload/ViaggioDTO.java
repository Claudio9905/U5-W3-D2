package claudiopostiglione.u5w3d2.payload;

import claudiopostiglione.u5w3d2.entities.StatoDestinazione;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record ViaggioDTO(
        @NotBlank(message = "Il nome della destinazione è obbligatorio")
        @Size(min = 2, max = 30, message = "Il nome della destinazione deve essere compreso tra i 2 e i 30 caratteri")
        String destinazione,
        @NotNull(message = "La data di destinazione è obbligatoria")
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dataDestinazione,
        @NotNull(message = "L'orario di partenza è obbligatorio")
        @JsonFormat(pattern = "HH:mm")
        LocalTime orarioPartenza,
        @NotNull(message = "Lo stato della destinazione non può essere nullo")
        StatoDestinazione stato
) {
}
