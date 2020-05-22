package fr.eni.ms2isi9bg3.gfv.web.rest;

import fr.eni.ms2isi9bg3.gfv.domain.CarModel;
import fr.eni.ms2isi9bg3.gfv.repository.CarModelRepository;
import fr.eni.ms2isi9bg3.gfv.security.AuthoritiesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Transactional
@Slf4j
public class CarModelResource {

    private final CarModelRepository carModelRepository;

    public CarModelResource(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    /**
     * {@code GET  /models} : get all the models.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of models in body.
     */
    @GetMapping("/models")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<CarModel> getAllModels() {
        log.debug("REST request to get all models");
        return carModelRepository.findAll();
    }

    /**
     * {@code GET  /models} : get all the models.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of models in body.
     */
    @GetMapping("/models/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<CarModel> getAllModelsByBrand(@PathVariable Long id) {
        log.debug("REST request to get all models");
        return carModelRepository.findAllByCarBrandBrandIdOrderByModelNameAsc(id);
    }
}
