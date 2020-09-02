package fr.eni.ms2isi9bg3.gfv.web.rest;

import fr.eni.ms2isi9bg3.gfv.domain.Booking;
import fr.eni.ms2isi9bg3.gfv.repository.BookingRepository;
import fr.eni.ms2isi9bg3.gfv.security.AuthoritiesConstants;
import fr.eni.ms2isi9bg3.gfv.service.BookingService;
import fr.eni.ms2isi9bg3.gfv.service.MailService;
import fr.eni.ms2isi9bg3.gfv.service.UserService;
import fr.eni.ms2isi9bg3.gfv.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Transactional
@Slf4j
public class BookingResource {
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final MailService mailService;
    private final UserService userService;

    public BookingResource(BookingRepository bookingRepository, BookingService bookingService,
            MailService mailService, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
        this.mailService = mailService;
        this.userService = userService;
    }

    /**
     * {@code POST  /bookings} : Create a new booking.
     *
     * @param booking the booking to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new booking,
     * or with status {@code 400 (Bad Request)} if the booking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bookings")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) throws URISyntaxException {
        log.debug("REST request to save Booking : {}", booking);
        if (booking.getBookingId() != null) {
            throw new BadRequestAlertException("A new booking cannot already have an ID", "booking","already exists");
        }
        Booking bkg = bookingService.createBooking(booking);
        mailService.sendBookingNotification(userService.getCurrentUser());
        return ResponseEntity.created(new URI("/api/bookings/" + bkg.getBookingId()))
                .body(bkg);
    }

    /**
     * {@code PUT  /bookings} : Update a booking.
     *
     * @param booking the booking to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booking,
     * or with status {@code 400 (Bad Request)} if the booking is not valid,
     * or with status {@code 500 (Internal Server Error)} if the booking couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bookings")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Booking> updateBooking(@Valid @RequestBody Booking booking) throws URISyntaxException {
        log.debug("REST request to Update Booking : {}", booking);
        if (booking.getBookingId() == null) {
            throw new BadRequestAlertException("Invalid id", "booking","doesn't exists");
        }
        Booking bkg = bookingService.updateBooking(booking);
        return ResponseEntity.ok()
                .body(bkg);
    }

    /**
     * {@code GET  /bookings} : get all the bookings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookings in body.
     */
    @GetMapping("/bookings")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<Booking>> getAllBookings(final Pageable pageable) {
        log.debug("REST request to get all Bookings");
        List<Booking> bookings = bookingRepository.findAll();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PutMapping(value = "/bookings/reserved", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public Map bookingConfirmed (@RequestBody Booking booking) {
        log.debug("REST request to validate booking");
        String msg = bookingService.bookingConfirmed(booking);
        return Collections.singletonMap("message", msg);
    }

    @PutMapping(value = "/bookings/refused", produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public Map bookingRefused (@RequestBody Booking booking) {
        log.debug("REST request to validate booking");
        String msg = bookingService.bookingRefused(booking);
        return Collections.singletonMap("message", msg);
    }

    /**
     * {@code GET  /bookings/:id} : get the "id" booking.
     *
     * @param id the id of the booking to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booking,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bookings/{id}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        log.debug("REST request to get Booking : {}", id);
        Optional<Booking> booking = bookingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(booking);
    }

    /**
     * {@code GET  /bookings/:login} : get all booking for "login".
     *
     * @param login the login of the bookings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     * and with body the bookings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bookings/user/{login}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<Booking>> getBookingByLogin(@PathVariable String login) {
        log.debug("REST request to get Booking : {}", login);
        List<Booking> bookings = bookingRepository.findAllByUserLogin(login);
        if(!bookings.stream().filter(u -> u.getUser().getLogin().equals(login)).findFirst().isPresent()) {
            log.error("There is no booking for the user {}", login);
            //throw new RuntimeException("There is no booking for the user");
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}
