package fr.eni.ms2isi9bg3.gfv.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.eni.ms2isi9bg3.gfv.domain.Booking;
import fr.eni.ms2isi9bg3.gfv.domain.Car;
import fr.eni.ms2isi9bg3.gfv.enums.CarStatus;
import fr.eni.ms2isi9bg3.gfv.repository.BookingRepository;
import fr.eni.ms2isi9bg3.gfv.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final CarService carService;

    public BookingService(BookingRepository bookingRepository, CarRepository carRepository
            , CarService carService) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.carService = carService;
    }

    public Booking createBooking (Booking booking) {
        Booking newBooking = new Booking();

        // TODO check if a car is already booked

        newBooking.setStartDate(booking.getStartDate());
        newBooking.setEndDate(booking.getEndDate());
        newBooking.setDepartureSite(booking.getDepartureSite());
        newBooking.setArrivalSite(booking.getArrivalSite());

        carToBeBooked(booking.getCar().getCarId());

        newBooking.setCar(booking.getCar());
        newBooking.setDescription(booking.getDescription());
        bookingRepository.save(newBooking);
        return newBooking;
    }

    public Booking updateBooking(Booking booking) {
        booking.setStartDate(booking.getStartDate());
        booking.setEndDate(booking.getEndDate());
        booking.setDepartureSite(booking.getDepartureSite());
        booking.setArrivalSite(booking.getArrivalSite());

        Optional<Booking> bookedCar = bookingRepository.findById(booking.getBookingId());
        if(bookedCar.isPresent()) {
            if(!bookedCar.get().getCar().equals(booking.getCar())) {
                carService.updateCarStatus(bookedCar.get().getCar(), CarStatus.AVAILABLE);

                carToBeBooked(booking.getCar().getCarId());
            }
        } else {
            throw new RuntimeException("Booking with id " + bookedCar.get().getBookingId() + " does not exist");
        }

        booking.setCar(booking.getCar());
        booking.setDescription(booking.getDescription());
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

    public String bookingConfirmed(Booking booking) {

        Optional<Car> carToBeReserved = carRepository.findOneByCarId(booking.getCar().getCarId());
        if (!carToBeReserved.isPresent()) {
            throw new RuntimeException("Car with id " + carToBeReserved.get().getCarId() + " does not exist");
        }
        carService.updateCarStatus(carToBeReserved.get(), CarStatus.RESERVED);

        String regNum = carToBeReserved.get().getRegistrationNumber().toUpperCase();
        return "CAR WITH REG N° " + regNum + " RESERVED";
    }
}
