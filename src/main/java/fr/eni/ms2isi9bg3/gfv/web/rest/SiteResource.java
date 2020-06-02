package fr.eni.ms2isi9bg3.gfv.web.rest;

import fr.eni.ms2isi9bg3.gfv.domain.Site;
import fr.eni.ms2isi9bg3.gfv.repository.SiteRepository;
import fr.eni.ms2isi9bg3.gfv.security.AuthoritiesConstants;
import fr.eni.ms2isi9bg3.gfv.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
public class SiteResource {

    private static final String ENTITY_NAME = "car";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteRepository siteRepository;

    public SiteResource(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @PostMapping("/sites")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Site> createSite(@Valid @RequestBody Site site) throws URISyntaxException {
        log.debug("REST request to save Site : {}", site);
        if (site.getSiteId() != null) {
            throw new BadRequestAlertException("A new site cannot already have an ID", ENTITY_NAME, "idExists");
        }
        Site result = siteRepository.save(site);
        return ResponseEntity.created(new URI("/api/sites/" + result.getSiteId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getSiteId().toString()))
                .body(result);
    }

    @PutMapping("/sites")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Site> updateSite(@Valid @RequestBody Site site) throws URISyntaxException {
        log.debug("REST request to update Site : {}", site);
        if (site.getSiteId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idNull");
        }
        Site result = siteRepository.save(site);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getSiteId().toString()))
                .body(result);
    }

    @GetMapping("/sites")
    public ResponseEntity<List<Site>> getAllSites() {
        log.debug("REST request to get All Sites");
        List<Site> site = siteRepository.findAll();
        return new ResponseEntity<>(site, HttpStatus.OK);
    }

    @GetMapping("/sites/{id}")
    public ResponseEntity<Site> getSite(@PathVariable Long id) {
        log.debug("REST request to get Site : {}", id);
        Optional<Site> site = siteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(site);
    }
}
