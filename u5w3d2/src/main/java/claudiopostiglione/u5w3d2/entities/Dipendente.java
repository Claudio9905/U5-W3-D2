package claudiopostiglione.u5w3d2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "dipendente")
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"password","authorities", "enabled", "accountNonLocked", "accountNonExpired", "credentialsNonExpired"})
public class Dipendente implements UserDetails {

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
    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "dipendente")
    private List<Prenotazione> listaPrenotazioni = new ArrayList<>();


    //Costruttori
    public Dipendente(String nome, String congnome, String username, String email, String password) {

        Random rdm = new Random();
        String[] roleList = {"ADMIN", "USER"};
        Role typeRole = Role.valueOf(roleList[rdm.nextInt(0, 2)]);

        this.nome = nome;
        this.congnome = congnome;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = typeRole;
    }

    //Metodi
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }



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
