package claudiopostiglione.u5w3d2.repositories;

import claudiopostiglione.u5w3d2.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {

    @Query("SELECT p FROM Prenotazione p WHERE p.dipendente.id = :dipId AND p.viaggio.dataDestinazione = :data")
    List<Prenotazione> findByDipendenteIdAndDataRichiesta(@Param("dipId") UUID dipendenteId, @Param("data") LocalDate dataRichiesta);

}
