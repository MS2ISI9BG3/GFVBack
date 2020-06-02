package fr.eni.ms2isi9bg3.gfv.web.rest;

import fr.eni.ms2isi9bg3.gfv.domain.Car;
import fr.eni.ms2isi9bg3.gfv.domain.Unavailability;
import fr.eni.ms2isi9bg3.gfv.repository.UnavailabilityRepository;
import fr.eni.ms2isi9bg3.gfv.security.AuthoritiesConstants;
import fr.eni.ms2isi9bg3.gfv.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Transactional
@Slf4j
public class UnavailabilityResource {

    private static final String ENTITY_NAME = "unavailability";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnavailabilityRepository unavailabilityRepository;

    public UnavailabilityResource(UnavailabilityRepository unavailabilityRepository) {
        this.unavailabilityRepository = unavailabilityRepository;
    }

    @PostMapping("/unavailability")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Unavailability> createUnavailability(@Valid @RequestBody Unavailability unavailability) throws URISyntaxException {
        log.debug("REST request to save Unavailability : {}", unavailability);
        if (unavailability.getId() != null) {
            throw new BadRequestAlertException("Cannot already have an ID", ENTITY_NAME, "idExists");
        }
        Unavailability result = unavailabilityRepository.save(unavailability);
        return ResponseEntity.created(new URI("/api/unavailability/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/unavailability")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Unavailability> updateUnavailability(@Valid @RequestBody Unavailability unavailability) throws URISyntaxException {
        log.debug("REST request to update Unavailability : {}", unavailability);
        if (unavailability.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idNull");
        }
        Unavailability result = unavailabilityRepository.save(unavailability);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @GetMapping("/unavailability")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<Unavailability> getAllUnavailability() {
        log.debug("REST request to get all Unavailability");
        return unavailabilityRepository.findAll();
    }

    @GetMapping("/unavailability/{id}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Unavailability> getUnavailability(@PathVariable Long id) {
        log.debug("REST request to get Unavailability : {}", id);
        Optional<Unavailability> unavailability = unavailabilityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(unavailability);
    }

    @GetMapping("/carUnavailability/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<Unavailability> getCarUnavailability(@PathVariable Long id) {
        log.debug("REST request to get Car Unavailability : {}", id);
        return unavailabilityRepository.findAllCarUnavailability(id);
    }
}
