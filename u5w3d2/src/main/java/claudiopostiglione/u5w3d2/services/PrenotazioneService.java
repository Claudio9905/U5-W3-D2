package claudiopostiglione.u5w3d2.services;

import claudiopostiglione.u5w3d2.entities.Dipendente;
import claudiopostiglione.u5w3d2.entities.Prenotazione;
import claudiopostiglione.u5w3d2.entities.Viaggio;
import claudiopostiglione.u5w3d2.exceptions.BadRequestException;
import claudiopostiglione.u5w3d2.exceptions.IdNotFoundException;
import claudiopostiglione.u5w3d2.payload.PrenotazioneDTO;
import claudiopostiglione.u5w3d2.repositories.PrenotazioneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private ViaggioService viaggioService;

    @Autowired
    private DipendenteService dipendenteService;

    // 1. per la chiamata POST
    public Prenotazione savePrenotazione(PrenotazioneDTO body) {

        Dipendente dipendenteFound = this.dipendenteService.findDipendenteById(body.dipendenteId());
        Viaggio viaggioFound = this.viaggioService.findViaggioById(body.viaggioId());

        if (!(body.dataRichiesta().equals(viaggioFound.getDataDestinazione()))) {
            throw new BadRequestException("Impossibile fare la prenotazione, la data richiesta non corrisponde con la data della destinazione");
        }

        List<Prenotazione> prenotazioniAttive = prenotazioneRepository.findByDipendenteIdAndDataRichiesta(dipendenteFound.getId(),viaggioFound.getDataDestinazione());
        if(!prenotazioniAttive.isEmpty()) throw new BadRequestException("Il dipendente " + dipendenteFound.getNome() + dipendenteFound.getCongnome() + " con ID " + dipendenteFound.getId() + " è già impegnato in un viaggio");

        Prenotazione newPrenotazione = new Prenotazione(body.dataRichiesta(), body.notePreferenze(), dipendenteFound, viaggioFound);

        dipendenteFound.getListaPrenotazioni().add(newPrenotazione);
        viaggioFound.getListaPrenotazione().add(newPrenotazione);

        this.prenotazioneRepository.save(newPrenotazione);

        log.info("La prenotazione " + newPrenotazione + " è stata salvata corretamente");
        return newPrenotazione;

    }

    //2. per la chiamata GET
    public Page<Prenotazione> findAllViaggi(int numPage, int sizePage, String sortBy) {

        if (sizePage > 50) sizePage = 50;
        sortBy = "dataRichiesta";
        Pageable pageable = PageRequest.of(numPage, sizePage, Sort.by(sortBy).ascending());

        return this.prenotazioneRepository.findAll(pageable);

    }

    //3 per la chiamata GET ID
    public Prenotazione findPrenotazioneById(UUID prenotazioneId) {
        return this.prenotazioneRepository.findById(prenotazioneId).orElseThrow(() -> new IdNotFoundException("La prenotazione con ID " + prenotazioneId + " non è stato trovata"));
    }

    //4. per la chiamata PUT
    public Prenotazione findPrenotazioneByIdAndUpdate(UUID prenotazioneId, PrenotazioneDTO newBody) {

        //Controllo l'ID del prenotazione se esiste
        Prenotazione prenotazioneFound = this.findPrenotazioneById(prenotazioneId);

        prenotazioneFound.setDataRichiesta(newBody.dataRichiesta());
        prenotazioneFound.setNotePreferenze(newBody.notePreferenze());

        Prenotazione updatePrenotazione = this.prenotazioneRepository.save(prenotazioneFound);
        log.info("La prenotazione con ID " + updatePrenotazione.getId() + " è stata aggiornata correttamente");
        return updatePrenotazione;
    }

    //5. per la chiamata DELETE
    public void findPrenotazioneByIdAndDelete(UUID prenotazioneId) {
        Prenotazione prenotazioneFound = this.findPrenotazioneById(prenotazioneId);
        this.prenotazioneRepository.delete(prenotazioneFound);
    }
}
