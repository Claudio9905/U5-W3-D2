package claudiopostiglione.u5w3d2.controllers;


import claudiopostiglione.u5w3d2.entities.Dipendente;
import claudiopostiglione.u5w3d2.entities.Prenotazione;
import claudiopostiglione.u5w3d2.exceptions.ValidationException;
import claudiopostiglione.u5w3d2.payload.PrenotazioneDTO;
import claudiopostiglione.u5w3d2.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    // endpoint "/me"
    @PutMapping("/me/{prenotazioneId}")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Prenotazione updatePrenotazioneMe(@AuthenticationPrincipal Dipendente currentDipendente, @PathVariable UUID prenotazioneId, @RequestBody PrenotazioneDTO bodyUpdate){
        return this.prenotazioneService.findPrenotazioneByIdAndUpdate(prenotazioneId,bodyUpdate);
    }



    // POST http://localhost:3001/prenotazione + payload
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione createPrenotazione(@RequestBody @Validated PrenotazioneDTO body, BindingResult validationResult) {
        if(validationResult.hasErrors()){
            throw new ValidationException(validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }

        return this.prenotazioneService.savePrenotazione(body);
    }

    // GET  http://localhost:3001/prenotazione
    @GetMapping
    @PreAuthorize(("hasAuthority('ADMIN')"))
    public Page<Prenotazione> getAllPrenotazioni(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "nome") String sortBy) {
            return this.prenotazioneService.findAllViaggi(page, size, sortBy);
    }

    // GET  http://localhost:3001/prenotazione/{prenotazioneId}
    @GetMapping("/{prenotazioneId}")
    @ResponseStatus(HttpStatus.FOUND)
    @PreAuthorize(("hasAuthority('ADMIN')"))
    public Prenotazione getPrenotazioneById(@PathVariable UUID prenotazioneId) {
        return  this.prenotazioneService.findPrenotazioneById(prenotazioneId);
    }

    // PUT  http://localhost:3001/prenotazione/{prenotazioneId} + payload
    @PutMapping("/{prenotazioneId}")
    @PreAuthorize(("hasAuthority('ADMIN')"))
    public Prenotazione getPrenotazioneByIdAndUpdate(@PathVariable UUID prenotazioneId, @RequestBody PrenotazioneDTO bodyUpdate) {
        return this.prenotazioneService.findPrenotazioneByIdAndUpdate(prenotazioneId, bodyUpdate);
    }

    // DELETE http://localhost:3001/prenotazione/{prenotazioneId}
    @DeleteMapping("/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(("hasAuthority('ADMIN')"))
    public void getPrenotazioneByIdAndDelete(@PathVariable UUID prenotazioneId) {
        this.prenotazioneService.findPrenotazioneByIdAndDelete(prenotazioneId);
    }
}
