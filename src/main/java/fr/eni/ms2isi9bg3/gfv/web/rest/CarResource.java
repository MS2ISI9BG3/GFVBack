package fr.eni.ms2isi9bg3.gfv.web.rest;

import fr.eni.ms2isi9bg3.gfv.domain.Car;
import fr.eni.ms2isi9bg3.gfv.repository.CarRepository;
import fr.eni.ms2isi9bg3.gfv.security.AuthoritiesConstants;
import fr.eni.ms2isi9bg3.gfv.service.CarService;
import fr.eni.ms2isi9bg3.gfv.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/api")
@Transactional
@Slf4j
public class CarResource {

    private static final String ENTITY_NAME = "car";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarRepository carRepository;
    private final CarService carService;

    public CarResource(CarRepository carRepository, CarService carService) {
        this.carRepository = carRepository;
        this.carService = carService;
    }

    /**
     * {@code POST  /cars} : Create a new car.
     *
     * @param car the car to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new car, or with status {@code 400 (Bad Request)} if the car has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cars")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Car> createCar(@Valid @RequestBody Car car) throws URISyntaxException {
        log.debug("REST request to save Car : {}", car);
        if (car.getCarId() != null) {
            throw new BadRequestAlertException("A new car cannot already have an ID", ENTITY_NAME, "idExists");
        }
        Car result = carService.saveCar(car);
        return ResponseEntity.created(new URI("/api/cars/" + result.getCarId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getCarId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /cars} : Updates an existing car.
     *
     * @param car the car to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated car,
     * or with status {@code 400 (Bad Request)} if the car is not valid,
     * or with status {@code 500 (Internal Server Error)} if the car couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cars")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Car> updateCar(@Valid @RequestBody Car car) throws URISyntaxException {
        log.debug("REST request to update Car : {}", car);
        if (car.getCarId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idNull");
        }
        Car result = carService.updateCar(car);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, car.getCarId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /cars} : get all the cars.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cars in body.
     */
    @GetMapping("/cars")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<Car>> getAllCars(final Pageable pageable) {
        log.debug("REST request to get all Cars");
        List<Car> cars = carRepository.findAll();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    /**
     * {@code GET  /cars/available} : get all the cars.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of available cars in body.
     */
    @GetMapping("/cars/available")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public List<Car> getAvailableCars() {
        log.debug("REST request to get all available Cars");
        return carRepository.findAvailableCars();
    }

    /**
     * {@code GET  /cars/sites/:siteId} : get all the cars by site.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of available cars in body.
     */
    /*@GetMapping("/cars/sites/{siteId}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public List<Car> getCarsBySite(@PathVariable Long siteId) {
        log.debug("REST request to get all Cars by Site : {}", siteId);
        return carRepository.findCarsBySite(siteId);
    }*/

    /**
     * {@code GET  /cars/available/sites/:siteId} : get all available cars by site.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of available cars in body.
     */
    @GetMapping("/cars/available/sites")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public List<Car> getAvailableCarsBySite(@RequestParam("departureSiteId") Long dsId,
                                            @RequestParam("arrivalSiteId") Long asId,
                                            @RequestParam("departureDate") String dDate,
                                            @RequestParam("arrivalDate") String aDate) throws ParseException {
        log.debug("REST request to get all Cars available for booking");
        return carService.getCarsToBeReservedBySite(dsId, asId, dDate, aDate);
    }

    /**
     * {@code GET  /cars/:id} : get the "id" car.
     *
     * @param id the id of the car to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the car, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cars/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Car> getCar(@PathVariable Long id) {
        log.debug("REST request to get Car : {}", id);
        Optional<Car> car = carRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(car);
    }

    @PutMapping("/cars/archive")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Car> carArchived(@Valid @RequestBody Car car) {
        log.debug("REST request to archive Car : {}", car);
        if (car.getCarId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idNull");
        }
        Car result = carService.archiveCar(car);
        return ResponseEntity.ok().body(result);
    }
}
