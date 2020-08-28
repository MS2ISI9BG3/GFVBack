package fr.eni.ms2isi9bg3.gfv.web.rest;

import fr.eni.ms2isi9bg3.gfv.domain.CarModel;
import fr.eni.ms2isi9bg3.gfv.repository.CarModelRepository;
import fr.eni.ms2isi9bg3.gfv.security.AuthoritiesConstants;
import fr.eni.ms2isi9bg3.gfv.service.CarModelService;
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
public class CarModelResource {

    private static final String ENTITY_NAME = "CAR_MODEL";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarModelRepository carModelRepository;
    private final CarModelService carModelService;

    public CarModelResource(CarModelRepository carModelRepository, CarModelService carModelService) {
        this.carModelRepository = carModelRepository;
        this.carModelService = carModelService;
    }

    /**
     * {@code POST  /models} : Create a new model.
     *
     * @param model the model to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new model,
     * or with status {@code 400 (Bad Request)} if the model has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/models")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<CarModel> createModel(@Valid @RequestBody CarModel model) throws URISyntaxException {
        log.debug("REST request to save Model : {}", model);
        if (model.getModelId() != null) {
            throw new BadRequestAlertException("A new model cannot already have an ID", ENTITY_NAME, "idExists");
        }
        CarModel result = carModelService.saveCarModel(model);
        return ResponseEntity.created(new URI("/api/brands/" + result.getModelId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true,
                        ENTITY_NAME, result.getModelId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /models} : Updates an existing car model.
     *
     * @param model the model to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated model,
     * or with status {@code 400 (Bad Request)} if the model is not valid,
     * or with status {@code 500 (Internal Server Error)} if the model couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/models")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<CarModel> updateModel(@Valid @RequestBody CarModel model) throws URISyntaxException {
        log.debug("REST request to update model : {}", model);
        if (model.getModelId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idNull");
        }
        CarModel result = carModelService.updateCarModel(model);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, model.getModelId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /models} : get all the models.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of models in body.
     */
    @GetMapping("/models")
    public List<CarModel> getAllModels() {
        log.debug("REST request to get all models");
        return carModelRepository.findAll();
    }

    /**
     * {@code GET  /models} : get available the models.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of models in body.
     */
    @GetMapping("/models/available")
    public List<CarModel> getAvailableModels() {
        log.debug("REST request to get available models");
        return carModelRepository.findAvailableCarModels();
    }

    /**
     * {@code GET  /models/available/{id}} : get available models for a given brand.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of models in body.
     */
    @GetMapping("/models/available/{id}")
    public List<CarModel> getAvailableModelsByBrand(@PathVariable Long id) {
        log.debug("REST request to get available models for a given Brand");
        return carModelService.findAvailableModelByBrand(id);
    }
}
