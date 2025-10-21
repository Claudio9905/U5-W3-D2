package claudiopostiglione.u5w3d2.services;

import claudiopostiglione.u5w3d2.entities.Dipendente;
import claudiopostiglione.u5w3d2.entities.Role;
import claudiopostiglione.u5w3d2.exceptions.BadRequestException;
import claudiopostiglione.u5w3d2.exceptions.IdNotFoundException;
import claudiopostiglione.u5w3d2.exceptions.NotFoundExcpetion;
import claudiopostiglione.u5w3d2.payload.DipendenteDTO;
import claudiopostiglione.u5w3d2.repositories.DipendenteRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class DipendenteService {
    @Autowired
    private DipendenteRepository dipendenteRepository;
    @Autowired
    private Cloudinary imageUploader;
    @Autowired
    private PasswordEncoder bcrypt;

    //Questi attributi mi serviranno per controllare alcuni parametri del file
    private static final long MAX_SIZE = 5 * 1024 * 1024; // corrispondono a 5MB
    private static final List<String> ALLOWED_FORMAT = List.of("image/jpeg", "image/png");

    // 1. per la chiamata POST
    public Dipendente saveDipendente(DipendenteDTO body) {

        //Controllo che l'email non è già in uso
        this.dipendenteRepository.findByEmail(body.email()).ifPresent(dipendente -> {
            throw new BadRequestException("Attenzione, l'email " + dipendente.getEmail() + " esiste già");
        });

        Dipendente newDipendente = new Dipendente(body.nome(), body.cognome(), body.username(), body.email(), bcrypt.encode(body.password()));
        newDipendente.setImageProfile("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());

        this.dipendenteRepository.save(newDipendente);

        log.info("Il dipendente " + body.nome() + body.cognome() + " è stato salvato corretamente");
        return newDipendente;

    }

    //2. per la chiamata GET
    public Page<Dipendente> findAllDipendenti(int numPage, int sizePage, String sortBy) {

        if (sizePage > 50) sizePage = 50;
        sortBy = "nome";
        Pageable pageable = PageRequest.of(numPage, sizePage, Sort.by(sortBy).ascending());

        return this.dipendenteRepository.findAll(pageable);

    }

    //3 per la chiamata GET ID
    public Dipendente findDipendenteById(UUID dipendenteID) {
        return this.dipendenteRepository.findById(dipendenteID).orElseThrow(() -> new IdNotFoundException("Il dipendente con ID " + dipendenteID + " non è stato trovato"));
    }

    //4. per la chiamata PUT
    public Dipendente findDipendenteByIdAndUpdate(UUID dipendenteId, DipendenteDTO newBody) {

        //Controllo l'ID del dipendente se esiste
        Dipendente dipendenteFound = this.findDipendenteById(dipendenteId);

        if (!(dipendenteFound.getEmail().equals(newBody.email()))) {
            this.dipendenteRepository.findByEmail(newBody.email()).ifPresent(dipendente -> {
                throw new BadRequestException("L'email " + dipendente.getEmail() + " esiste già");
            });
        }

        dipendenteFound.setNome(newBody.nome());
        dipendenteFound.setCongnome(newBody.cognome());
        dipendenteFound.setUsername(newBody.username());
        dipendenteFound.setEmail(newBody.email());
        dipendenteFound.setImageProfile("https://ui-avatars.com/api/?name=" + newBody.nome() + "+" + newBody.cognome());
        dipendenteFound.setRole(newBody.ruolo());

        Dipendente updateDipendente = this.dipendenteRepository.save(dipendenteFound);
        log.info("Il dipendente " + newBody.nome() + newBody.cognome() + " con ID " + updateDipendente.getId() + " è stato aggiornato correttamente");
        return updateDipendente;
    }

    //5. per la chiamata DELETE
    public void findDipendenteByIdAndDelete(UUID dipendenteId) {
        Dipendente dipendenteFound = this.findDipendenteById(dipendenteId);
        this.dipendenteRepository.delete(dipendenteFound);
    }

    //6. per la chiamata PATCH (riguardo all'upload dell'immagine)
    public Dipendente uploadImageProfile(MultipartFile file, UUID dipendenteId) {

        if (file.isEmpty()) throw new BadRequestException("File vuoto!");
        if (file.getSize() > MAX_SIZE)
            throw new BadRequestException("La dimensione del file è troppo grande, il limite è di 5MB");
        if (!(ALLOWED_FORMAT.contains(file.getContentType())))
            throw new BadRequestException("Attenzione, il formato del file è sbagliato. Formato file acconsentito: .jpeg, .png");

        Dipendente dipendenteFound = this.findDipendenteById(dipendenteId);

        try {
            //Cattura dell'URL dell'immagine
            Map resultMap = imageUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageURL = (String) resultMap.get("url");

            //Salvataggio dell'immagine catturata
            dipendenteFound.setImageProfile(imageURL);
            this.dipendenteRepository.save(dipendenteFound);
            return dipendenteFound;
        } catch (Exception e) {
            throw new BadRequestException("Errore nell'upload dell'immagine");
        }

    }

    public Dipendente findDipendenteByEmail(String email) {
        return this.dipendenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundExcpetion("L'email " + email + " non è stato trovata"));
    }

}
