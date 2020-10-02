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
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
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
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Booking> updateBooking(@Valid @RequestBody Booking booking) throws Exception {
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
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<Booking>> getAllBookings(final Pageable pageable) {
        log.debug("REST request to get all Bookings");
        List<Booking> bookings = bookingRepository.findAll();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PutMapping(value = "/bookings/confirmed/{bookingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Booking> bookingConfirmed (@PathVariable Long bookingId) {
        log.debug("REST request to validate Booking Id {}", bookingId);
        Optional<Booking> bk = bookingRepository.findById(bookingId);
        if (!bk.isPresent()) {
            throw new RuntimeException("Booking with id " + bookingId + " does not exist");
        }
        Optional<Booking> bkg = bookingService.bookingConfirmed(bookingId);
        mailService.sendBookingConfirmedEmail(bk.get().getUser());
        return ResponseUtil.wrapOrNotFound(bkg);
    }

    @PutMapping(value = "/bookings/refused/{bookingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Booking> bookingRefused (@PathVariable Long bookingId) {
        log.debug("REST request to refuse Booking Id {}", bookingId);
        Optional<Booking> bk = bookingRepository.findById(bookingId);
        if (!bk.isPresent()) {
            throw new RuntimeException("Booking with id " + bookingId + " does not exist");
        }
        Optional<Booking> bkg = bookingService.bookingRefused(bookingId);
        mailService.sendBookingRefusedEmail(bk.get().getUser());
        return ResponseUtil.wrapOrNotFound(bkg);
    }

    @PutMapping(value = "/bookings/returned/{bookingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Booking> bookingReturned (@PathVariable Long bookingId) throws Exception {
        log.debug("REST request to validate Booking Id {} return", bookingId);
        Optional<Booking> bk = bookingRepository.findById(bookingId);
        if (!bk.isPresent()) {
            throw new RuntimeException("Booking with id " + bookingId + " does not exist");
        }
        Optional<Booking> bkg = bookingService.bookingReturned(bookingId);
        return ResponseUtil.wrapOrNotFound(bkg);
    }

    @PutMapping(value = "/bookings/canceled/{bookingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Booking> bookingCanceled (@PathVariable Long bookingId) {
        log.debug("REST request to cancel Booking Id {}", bookingId);
        Optional<Booking> bk = bookingRepository.findById(bookingId);
        if (!bk.isPresent()) {
            throw new RuntimeException("Booking with id " + bookingId + " does not exist");
        }
        Optional<Booking> bkg = bookingService.bookingCanceled(bookingId);
        return ResponseUtil.wrapOrNotFound(bkg);
    }

    /**
     * {@code GET  /bookings/:id} : get the "id" booking.
     *
     * @param id the id of the booking to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booking,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bookings/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        log.debug("REST request to get Booking Id {}", id);
        Optional<Booking> booking = bookingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(booking);
    }

    /**
     * {@code GET  /bookings/:id} : get bookings for "user".
     *
     * @param id the login of the bookings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     * and with body the bookings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bookings/user/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<Booking>> getBookingByLogin(@PathVariable Long id) {
        log.debug("REST request to get Booking for user id : {}", id);
        List<Booking> bookings = bookingRepository.findAllByUserId(id);
        if(!bookings.stream().anyMatch(u -> u.getUser().getId().equals(id))) {
            log.error("There is no booking for the user ID {}", id);
            //throw new RuntimeException("There is no booking for the user");
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}
