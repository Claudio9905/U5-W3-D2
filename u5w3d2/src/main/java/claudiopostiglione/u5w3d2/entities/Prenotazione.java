package claudiopostiglione.u5w3d2.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prenotazione")
@NoArgsConstructor
@Getter
@Setter
public class Prenotazione {

    //Attributi
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(name = "Data_Richiesta")
    @JsonFormat(pattern = "dd-WW-yyyy")
    private LocalDate dataRichiesta;
    @Column(name = "Note/Preferenze_dipendente")
    private String notePreferenze;

    @ManyToOne
    @JoinColumn(name = "dipendente")
    private Dipendente dipendente;

    @ManyToOne
    @JoinColumn(name = "viaggio")
    private Viaggio viaggio;

    //Costruttori
    public Prenotazione(LocalDate dataRichiesta, String notePreferenze, Dipendente dipendente, Viaggio viaggio){
        this.dataRichiesta = dataRichiesta;
        this.notePreferenze = notePreferenze;
        this.dipendente = dipendente;
        this.viaggio = viaggio;
    }

    //Metodi

    @Override
    public String toString() {
        return "|-- Prenotazione: " +
                " ID: " + id +
                " Data richiesta: " + dataRichiesta +
                " Note/Preferenze: " + notePreferenze + '\'' +
                "--|";
    }
}
