package claudiopostiglione.u5w3d2.services;

import claudiopostiglione.u5w3d2.entities.Viaggio;
import claudiopostiglione.u5w3d2.exceptions.IdNotFoundException;
import claudiopostiglione.u5w3d2.payload.ViaggioDTO;
import claudiopostiglione.u5w3d2.repositories.ViaggioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ViaggioService {

    @Autowired
    private ViaggioRepository viaggioRepository;

    // 1. per la chiamata POST
    public Viaggio saveViaggio(ViaggioDTO body) {

        Viaggio newViaggio = new Viaggio(body.destinazione(), body.dataDestinazione(), body.orarioPartenza(), body.stato());

        this.viaggioRepository.save(newViaggio);

        log.info("Il viaggio " + body.destinazione() + " è stato salvato corretamente");
        return newViaggio;

    }

    //2. per la chiamata GET
    public Page<Viaggio> findAllViaggi(int numPage, int sizePage, String sortBy) {

        if (sizePage > 50) sizePage = 50;
        sortBy = "destinazione";
        Pageable pageable = PageRequest.of(numPage, sizePage, Sort.by(sortBy).ascending());

        return this.viaggioRepository.findAll(pageable);

    }

    //3 per la chiamata GET ID
    public Viaggio findViaggioById(UUID viaggioId) {
        return this.viaggioRepository.findById(viaggioId).orElseThrow(() -> new IdNotFoundException("Il viaggio con ID " + viaggioId + " non è stato trovato"));
    }

    //4. per la chiamata PUT
    public Viaggio findViaggioByIdAndUpdate(UUID viaggioId, ViaggioDTO newBody) {

        //Controllo l'ID del viaggio se esiste
        Viaggio viaggioFound = this.findViaggioById(viaggioId);

        viaggioFound.setDestinazione(newBody.destinazione());
        viaggioFound.setDataDestinazione(newBody.dataDestinazione());
        viaggioFound.setStato(newBody.stato());

        Viaggio updateViaggio = this.viaggioRepository.save(viaggioFound);
        log.info("Il viaggio " + newBody.destinazione() + " con ID " + updateViaggio.getId() + " è stato aggiornato correttamente");
        return updateViaggio;
    }

    //5. per la chiamata DELETE
    public void findViaggioByIdAndDelete(UUID viaggioId) {
        Viaggio viaggioFound = this.findViaggioById(viaggioId);
        this.viaggioRepository.delete(viaggioFound);
    }

    //6. per la chiamata PATCH riguardo alla modifica dello stato del viaggio
    public Viaggio findViaggioByIdAndUpdateStato(ViaggioDTO body, UUID viaggioId){
        Viaggio viaggioFound = findViaggioById(viaggioId);
        viaggioFound.setStato(body.stato());
        this.viaggioRepository.save(viaggioFound);
        log.info("| Lo stato del viaggio con ID: " + viaggioFound.getId() + " e destinazione: " + viaggioFound.getDestinazione() + " è stato aggiornato" );
        return viaggioFound;
    }

}
