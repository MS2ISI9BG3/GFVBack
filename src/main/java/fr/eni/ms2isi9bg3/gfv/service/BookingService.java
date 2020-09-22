package fr.eni.ms2isi9bg3.gfv.service;

import fr.eni.ms2isi9bg3.gfv.config.Constants;
import fr.eni.ms2isi9bg3.gfv.domain.Booking;
import fr.eni.ms2isi9bg3.gfv.domain.Car;
import fr.eni.ms2isi9bg3.gfv.domain.User;
import fr.eni.ms2isi9bg3.gfv.enums.BookingStatus;
import fr.eni.ms2isi9bg3.gfv.enums.CarStatus;
import fr.eni.ms2isi9bg3.gfv.repository.BookingRepository;
import fr.eni.ms2isi9bg3.gfv.repository.CarRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final CarService carService;
    private final MessageSource messageSource;
    private final UserService userService;
    private final MailService mailService;

    public BookingService(BookingRepository bookingRepository, CarRepository carRepository,
                          CarService carService, MessageSource messageSource, UserService userService,
                          MailService mailService) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.carService = carService;
        this.messageSource = messageSource;
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

        carToBeBooked(booking.getCar().getCarId());

        newBooking.setCar(booking.getCar());
        newBooking.setDescription(booking.getDescription());

        Optional<User> bkUser = userService.getCurrentUser();
        newBooking.setUser(bkUser.get());

        newBooking.setBookingStatus(BookingStatus.VALIDATION_PENDING);

        bookingRepository.save(newBooking);
        return newBooking;
    }

    public Booking updateBooking(Booking booking) {
        booking.setDepartureDate(booking.getDepartureDate());
        booking.setArrivalDate(booking.getArrivalDate());
        booking.setDepartureSite(booking.getDepartureSite());
        booking.setArrivalSite(booking.getArrivalSite());

        Optional<Booking> bookedCar = bookingRepository.findById(booking.getBookingId());
        if(bookedCar.isPresent()) {
            if(!bookedCar.get().getCar().equals(booking.getCar())) {
                carService.updateCarStatus(bookedCar.get().getCar(), CarStatus.AVAILABLE);

                booking.setBookingStatus(BookingStatus.VALIDATION_PENDING);
                mailService.sendBookingNotification(userService.getCurrentUser());

                carToBeBooked(booking.getCar().getCarId());
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

    private void carToBeBooked(Long id) {
        Optional<Car> carToBeBooked = carRepository.findOneByCarId(id);
        if (!carToBeBooked.isPresent()) {
            throw new RuntimeException("Car with id " + id + " does not exist");
        }
        carService.updateCarStatus(carToBeBooked.get(), CarStatus.VALIDATION_PENDING);
    }

    public String bookingConfirmed(Long id) {
        Optional<Booking> bkg = bookingRepository.findById(id);

        Optional<Car> carToBeReserved = carRepository.findOneByCarId(bkg.get().getCar().getCarId());
        if (!carToBeReserved.isPresent()) {
            throw new RuntimeException("Car with id " + carToBeReserved.get().getCarId() + " does not exist");
        }
        carService.updateCarStatus(carToBeReserved.get(), CarStatus.RESERVED);

        bkg.get().setBookingStatus(BookingStatus.CONFIRMED);

        String regNum = carToBeReserved.get().getRegistrationNumber().toUpperCase();
        final String[] params = new String[]{regNum};
        return messageSource.getMessage("booking.notification.confirmed", params,null, Constants.DEFAULT_LOCAL);
    }

    public String bookingRefused(Long id) {

        Optional<Booking> bkg = bookingRepository.findById(id);
        bkg.get().setBookingStatus(BookingStatus.REJECTED);

        Optional<Car> carToBeReserved = setCarStatusToAvailable(id);
        String regNum = carToBeReserved.get().getRegistrationNumber().toUpperCase();
        final String[] params = new String[]{regNum};
        return messageSource.getMessage("booking.notification.refused", params,null, Constants.DEFAULT_LOCAL);
    }

    public String bookingCanceled(Long id) {

        Optional<Booking> bkg = bookingRepository.findById(id);
        bkg.get().setBookingStatus(BookingStatus.CANCELED);

        setCarStatusToAvailable(id);

        return messageSource.getMessage("booking.message.canceled",null, Constants.DEFAULT_LOCAL);
    }

    private Optional<Car> setCarStatusToAvailable(Long id) {
        Optional<Booking> bkg = bookingRepository.findById(id);

        Optional<Car> booked = carRepository.findOneByCarId(bkg.get().getCar().getCarId());
        if (!booked.isPresent()) {
            throw new RuntimeException("Car with id " + booked.get().getCarId() + " does not exist");
        }
        carService.updateCarStatus(booked.get(), CarStatus.AVAILABLE);
        return  booked;
    }
}
