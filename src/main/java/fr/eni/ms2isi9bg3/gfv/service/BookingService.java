package fr.eni.ms2isi9bg3.gfv.service;

import fr.eni.ms2isi9bg3.gfv.domain.Booking;
import fr.eni.ms2isi9bg3.gfv.domain.User;
import fr.eni.ms2isi9bg3.gfv.enums.BookingStatus;
import fr.eni.ms2isi9bg3.gfv.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final MailService mailService;

    public BookingService(BookingRepository bookingRepository, UserService userService, MailService mailService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    public Booking createBooking (Booking booking) {
        Booking newBooking = new Booking();

        // TODO check if a car is already booked

        newBooking.setDepartureDate(booking.getDepartureDate());
        newBooking.setArrivalDate(booking.getArrivalDate());
        newBooking.setDepartureSite(booking.getDepartureSite());
        newBooking.setArrivalSite(booking.getArrivalSite());
        newBooking.setCar(booking.getCar());
        newBooking.setDescription(booking.getDescription());
        Optional<User> bkUser = userService.getCurrentUser();
        newBooking.setUser(bkUser.get());
        newBooking.setBookingStatus(BookingStatus.VALIDATION_PENDING);
        bookingRepository.save(newBooking);
        return newBooking;
    }

    public Booking updateBooking(Booking booking) throws Exception {
        booking.setDepartureDate(booking.getDepartureDate());
        booking.setArrivalDate(booking.getArrivalDate());
        booking.setDepartureSite(booking.getDepartureSite());
        booking.setArrivalSite(booking.getArrivalSite());

        Optional<Booking> bookedCar = bookingRepository.findById(booking.getBookingId());
        if(bookedCar.isPresent()) {
            if(!bookedCar.get().getCar().equals(booking.getCar())) {
                if(bookedCar.get().getBookingStatus().equals(BookingStatus.VALIDATION_PENDING)) {
                    booking.setBookingStatus(BookingStatus.VALIDATION_PENDING);
                    mailService.sendBookingNotification(userService.getCurrentUser());
                } else {
                    throw new Exception("Car Booking Not Pending Validation and Cannot Be Modified");
                }
            }
        } else {
            throw new RuntimeException("Booking with id " + bookedCar.get().getBookingId() + " does not exist");
        }

        booking.setCar(booking.getCar());
        booking.setDescription(booking.getDescription());

        Optional<User> bkUser = userService.getCurrentUser();
        booking.setUser(bkUser.get());

        bookingRepository.save(booking);
        return booking;
    }

    public Optional<Booking> bookingConfirmed(Long id) {
        Optional<Booking> bkg = bookingRepository.findById(id);
        if(bkg.get().getBookingStatus().equals(BookingStatus.VALIDATION_PENDING)) {
            bkg.get().setBookingStatus(BookingStatus.CONFIRMED);
        }
        return bkg;
    }

    public Optional<Booking> bookingRefused(Long id) {
        Optional<Booking> bkg = bookingRepository.findById(id);
        if(bkg.get().getBookingStatus().equals(BookingStatus.VALIDATION_PENDING)) {
            bkg.get().setBookingStatus(BookingStatus.REJECTED);
        }
        return bkg;
    }

    public Optional<Booking> bookingReturned(Long id) {
        Optional<Booking> bkg = bookingRepository.findById(id);
        LocalDateTime abDate = convertToLocalDateTime(bkg.get().getArrivalDate());
        if(LocalDateTime.now().isAfter(abDate) || LocalDateTime.now().equals(abDate)) {
            if(bkg.get().getBookingStatus().equals(BookingStatus.CONFIRMED)) {
                bkg.get().setBookingStatus(BookingStatus.COMPLETED);
            } else {
                log.warn("Booking status is {}", bkg.get().getBookingStatus());
            }
        } else {
            log.warn("Booking is on going and cannot be completed");
        }
        return bkg;
    }

    private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return new java.sql.Timestamp(
                dateToConvert.getTime()).toLocalDateTime();
    }

    public Optional<Booking> bookingCanceled(Long id) {
        Optional<Booking> bkg = bookingRepository.findById(id);
        if(bkg.get().getBookingStatus().equals(BookingStatus.VALIDATION_PENDING) ||
                bkg.get().getBookingStatus().equals(BookingStatus.CONFIRMED)) {
            bkg.get().setBookingStatus(BookingStatus.CANCELED);
        }
        return bkg;
    }
}
