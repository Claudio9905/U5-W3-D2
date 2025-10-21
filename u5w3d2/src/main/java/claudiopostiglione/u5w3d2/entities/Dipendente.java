package claudiopostiglione.u5w3d2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dipendente")
@NoArgsConstructor
@Getter
@Setter

public class Dipendente {

    //Attributi
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(name = "Nome")
    private String nome;
    @Column(name = "Cognome")
    private String congnome;
    @Column(name = "Username")
    private String username;
    @Column(name = "Email")
    private String email;
    @Column(name = "Immagine_Profilo")
    private String imageProfile;
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "dipendente")
    private List<Prenotazione> listaPrenotazioni = new ArrayList<>();


    //Costruttori
    public Dipendente(String nome, String congnome, String username, String email, String password) {
        this.nome = nome;
        this.congnome = congnome;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //Metodi


    @Override
    public String toString() {
        return "|-- Dipendente " +
                " ID = " + id +
                " Nome= " + nome + '\'' +
                " Congnome= " + congnome + '\'' +
                " Username= " + username + '\'' +
                " E-mail= " + email + '\'' +
                " ImageProfile='" + imageProfile + '\'' +
                "-- |";
    }
}
