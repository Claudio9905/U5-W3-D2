package claudiopostiglione.u5w3d2.controllers;

import claudiopostiglione.u5w3d2.entities.Dipendente;
import claudiopostiglione.u5w3d2.exceptions.ValidationException;
import claudiopostiglione.u5w3d2.payload.DipendenteDTO;
import claudiopostiglione.u5w3d2.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/dipendenti")
public class DipendenteController {

    @Autowired
    private DipendenteService dipendenteService;


    // POST http://localhost:3005/dipendenti + payload
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente createDipendente(@RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.dipendenteService.saveDipendente(body);
    }

    // GET  http://localhost:3005/dipendenti
    @GetMapping
    public Page<Dipendente> getAllDipendenti(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return this.dipendenteService.findAllDipendenti(page, size, sortBy);
    }

    // GET  http://localhost:3005/dipendenti/{dipendenteId}
    @GetMapping("/{dipendenteId}")
    @ResponseStatus(HttpStatus.FOUND)
    public Dipendente getDipendenteById(@PathVariable UUID dipendenteId) {
        return this.dipendenteService.findDipendenteById(dipendenteId);
    }

    // PUT  http://localhost:3005/dipendenti/{dipendenteId} + payload
    @PutMapping("/{dipendenteId}")
    public Dipendente getDipendenteByIdAndUpdate(@PathVariable UUID dipendenteId, @RequestBody DipendenteDTO bodyUpdate) {
        return this.dipendenteService.findDipendenteByIdAndUpdate(dipendenteId, bodyUpdate);
    }

    // DELETE http://localhost:3005/dipendenti/{dipendenteId}
    @DeleteMapping("/{dipendenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getDipendenteByIdAndDelete(@PathVariable UUID dipendenteId) {
        this.dipendenteService.findDipendenteByIdAndDelete(dipendenteId);
    }

    //PATCH http://localhost:3005/dipendenti/{dipendenteId}/imageProfile
    @PatchMapping("/{dipendenteId}/imageProfile")
    public Dipendente uploadImageProfile(@RequestParam("imageProfile") MultipartFile file, @PathVariable UUID dipendenteId) {
        System.out.println("| Nome del file: " + file.getName());
        System.out.println("| Dimesione del file: " + file.getSize());
        return this.dipendenteService.uploadImageProfile(file, dipendenteId);
    }
}
