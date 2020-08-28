package fr.eni.ms2isi9bg3.gfv.web.rest;

import fr.eni.ms2isi9bg3.gfv.domain.CarBrand;
import fr.eni.ms2isi9bg3.gfv.repository.CarBrandRepository;
import fr.eni.ms2isi9bg3.gfv.security.AuthoritiesConstants;
import fr.eni.ms2isi9bg3.gfv.service.CarBrandService;
import fr.eni.ms2isi9bg3.gfv.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
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

@RestController
@RequestMapping("/api")
@Transactional
@Slf4j
public class CarBrandResource {

    private static final String ENTITY_NAME = "CAR_BRAND";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarBrandRepository carBrandRepository;
    private final CarBrandService carBrandService;

    public CarBrandResource(CarBrandRepository carBrandRepository, CarBrandService carBrandService) {
        this.carBrandRepository = carBrandRepository;
        this.carBrandService = carBrandService;
    }

    /**
     * {@code POST  /brands} : Create a new brand.
     *
     * @param brand the car to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new brand,
     * or with status {@code 400 (Bad Request)} if the brand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/brands")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<CarBrand> createBrand(@Valid @RequestBody CarBrand brand) throws URISyntaxException {
        log.debug("REST request to save Brand : {}", brand);
        if (brand.getBrandId() != null) {
            throw new BadRequestAlertException("A new brand cannot already have an ID", ENTITY_NAME, "idExists");
        }
        CarBrand result = carBrandService.saveCarBrand(brand);
        return ResponseEntity.created(new URI("/api/brands/" + result.getBrandId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true,
                        ENTITY_NAME, result.getBrandId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /brands} : Updates an existing car brand.
     *
     * @param brand the brand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated brand,
     * or with status {@code 400 (Bad Request)} if the brand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the brand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/brands")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<CarBrand> updateBrand(@Valid @RequestBody CarBrand brand) throws URISyntaxException {
        log.debug("REST request to update brand : {}", brand);
        if (brand.getBrandId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idNull");
        }
        CarBrand result = carBrandService.updateCarBrand(brand);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, brand.getBrandId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /brands} : get all the brands.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of brands in body.
     */
    @GetMapping("/brands")
    public List<CarBrand> getAllBrands() {
        log.debug("REST request to get all brands");
        return carBrandRepository.findAllByOrderByBrandNameAsc();
    }

    /**
     * {@code GET  /brands/available} : get available the brands.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of brands in body.
     */
    @GetMapping("/brands/available")
    public List<CarBrand> getAvailableBrands() {
        log.debug("REST request to get all brands");
        return carBrandRepository.findAvailableCarBrands();
    }
}
