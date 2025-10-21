package claudiopostiglione.u5w3d2.controllers;

import claudiopostiglione.u5w3d2.entities.Viaggio;
import claudiopostiglione.u5w3d2.exceptions.ValidationException;
import claudiopostiglione.u5w3d2.payload.ViaggioDTO;
import claudiopostiglione.u5w3d2.services.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/viaggi")
public class ViaggioController {

    @Autowired
    private ViaggioService viaggioService;

    // POST http://localhost:3005/viaggi + payload
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Viaggio createViaggio(@RequestBody @Validated ViaggioDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }

        return this.viaggioService.saveViaggio(body);
    }

    // GET  http://localhost:3005/viaggi
    @GetMapping
    public Page<Viaggio> getAllViaggi(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "nome") String sortBy) {
        return this.viaggioService.findAllViaggi(page, size, sortBy);
    }

    // GET  http://localhost:3005/viaggi/{viaggioId}
    @GetMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.FOUND)
    public Viaggio getViaggioById(@PathVariable UUID viaggioId) {
        return this.viaggioService.findViaggioById(viaggioId);
    }

    // PUT  http://localhost:3005/viaggi/{viaggioId} + payload
    @PutMapping("/{viaggioId}")
    public Viaggio getViaggioByIdAndUpdate(@PathVariable UUID viaggioId, @RequestBody ViaggioDTO bodyUpdate) {
        return this.viaggioService.findViaggioByIdAndUpdate(viaggioId, bodyUpdate);
    }

    // DELETE http://localhost:3005/viaggi/{viaggioId}
    @DeleteMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getViaggioByIdAndDelete(@PathVariable UUID viaggioId) {
        this.viaggioService.findViaggioByIdAndDelete(viaggioId);
    }

    //PATCH http://localhost:3005/viaggi/{viaggioId}/stato + payload
    @PatchMapping("/{viaggioId}/stato")
    public Viaggio updateStateViaggio(@RequestBody ViaggioDTO body, @PathVariable UUID viaggioId){
        return this.viaggioService.findViaggioByIdAndUpdateStato(body, viaggioId);
    }
}
