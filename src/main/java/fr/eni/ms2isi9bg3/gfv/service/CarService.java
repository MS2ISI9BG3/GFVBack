package fr.eni.ms2isi9bg3.gfv.service;

import fr.eni.ms2isi9bg3.gfv.domain.*;
import fr.eni.ms2isi9bg3.gfv.enums.BookingStatus;
import fr.eni.ms2isi9bg3.gfv.repository.BookingRepository;
import fr.eni.ms2isi9bg3.gfv.repository.CarRepository;
import fr.eni.ms2isi9bg3.gfv.service.dto.CarFromBookingList;
import fr.eni.ms2isi9bg3.gfv.service.exception.RegistrationNumberAlreadyUsedException;
import fr.eni.ms2isi9bg3.gfv.service.exception.VinAlreadyUsedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class CarService {
    private final CarRepository carRepository;
    private final BookingRepository bookingRepository;

    public CarService(CarRepository carRepository,
                      BookingRepository bookingRepository) {
        this.carRepository = carRepository;
        this.bookingRepository = bookingRepository;
    }

    public Car saveCar(Car car) {
        Optional<Car> carWithVin = carRepository.findOneByVin(car.getVin());
        Optional<Car> carWithRegNb = carRepository.findOneByRegistrationNumber(car.getRegistrationNumber());
        if (carWithVin.isPresent()) {
            throw new VinAlreadyUsedException();
        }
        if (carWithRegNb.isPresent()) {
            throw new RegistrationNumberAlreadyUsedException();
        }
        Car newCar = new Car();
        if (car.getVin() != null) {
            newCar.setVin(car.getVin().toLowerCase());
        }
        if (car.getRegistrationNumber() != null) {
            newCar.setRegistrationNumber(car.getRegistrationNumber().toLowerCase());
        }
        setCarProperties(newCar, car.getPower(), car.getNumberOfSeats(), car.getOdometer(),
                car.getInsuranceValidityDate(), car.getServiceValidityDate(), car.getCarBrand(), car.getCarModel(),
                car.getCarSite());
        newCar.setArchived(false);
        carRepository.save(newCar);
        return newCar;
    }

    public Car updateCar(Car car) {
        Optional<Car> carByVin = carRepository.findOneByVin(car.getVin());
        if (carByVin.isPresent()) {
            if (!carByVin.get().getCarId().equals(car.getCarId())
                    && carByVin.get().getVin().equals(car.getVin().toLowerCase())) {
                throw new VinAlreadyUsedException();
            }
        }
        car.setVin(car.getVin().toLowerCase());

        Optional<Car> carByRegNb = carRepository.findOneByRegistrationNumber(car.getRegistrationNumber());
        if (carByRegNb.isPresent()) {
            if (!carByRegNb.get().getCarId().equals(car.getCarId())
                    && carByRegNb.get().getRegistrationNumber().equals(car.getRegistrationNumber().toLowerCase())) {
                throw new RegistrationNumberAlreadyUsedException();
            }
        }
        car.setRegistrationNumber(car.getRegistrationNumber().toLowerCase());

        setCarProperties(car, car.getPower(), car.getNumberOfSeats(), car.getOdometer(), car.getInsuranceValidityDate(),
                car.getServiceValidityDate(), car.getCarBrand(), car.getCarModel(), car.getCarSite());
        car.setArchived(car.isArchived());
        carRepository.save(car);
        return car;
    }

    public Car archiveCar(Car car) {
        CarFromBookingList cfB = bookingRepository.findCarLastBooking(car.getCarId());
        if (cfB.getCarId() != null) {
            if(cfB.getStatus().equals(BookingStatus.COMPLETED)) {
                car.setArchived(true);
            } else {
                log.warn("Car Booking is on going. Booking status is {}", cfB.getStatus());
            }
        } else {
            car.setArchived(true);
        }

        carRepository.save(car);
        return  car;
    }

    public List<Car> getCarsToBeReservedBySite(Long dsId, Long asId, String dDate, String aDate) throws ParseException {
        List<Car> carToBeReserved;

        List<Car> cFNsb = getCarsFromNextSiteInBooking(asId, aDate);
        List<Car> cFLsb = getCarsFromLastSiteInBooking(dsId, dDate);

        List<Car> cInB = Stream.concat(cFNsb.stream(), cFLsb.stream())
                .distinct()
                .collect(Collectors.toList());
        List<Car> cNInb = getCarsNotInListFromBooking(dsId);

        carToBeReserved = Stream.concat(cInB.stream(), cNInb.stream())
                .distinct()
                .collect(Collectors.toList());
        return carToBeReserved;
    }

    private List<Car> getCarsFromNextSiteInBooking(Long asId, String aDate) throws ParseException {
        List<Car> carsList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(aDate);

        List<CarFromBookingList> cFNsb = bookingRepository.findCarsFromNextSiteInBooking(asId);
        List<Car> archivedCars = carRepository.findAllByArchivedIsTrue();
        if(cFNsb.size() > 0) {
            if(archivedCars.size() > 0) {
                List<CarFromBookingList> cFblNotArchived = cFNsb.stream()
                        .filter(cFbl -> archivedCars.stream()
                                .anyMatch(ac -> !ac.getCarId().equals(cFbl.getCarId())))
                        .collect(Collectors.toList());

                for (CarFromBookingList bkg : cFblNotArchived) {
                    if(date.before(bkg.getBDate())) {
                        Car car= carRepository.findByCarId(bkg.getCarId());
                        carsList.add(car);
                    }
                }
            } else {
                for (CarFromBookingList bkg : cFNsb) {
                    if(date.before(bkg.getBDate())) {
                        Car car= carRepository.findByCarId(bkg.getCarId());
                        carsList.add(car);
                    }
                }
            }
        }
        return carsList;
    }

    private List<Car> getCarsFromLastSiteInBooking(Long dsId, String dDate) throws ParseException {
        List<Car> carsList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(dDate);

        List<CarFromBookingList> cFLsb = bookingRepository.findCarsFromLastSiteInBooking(dsId);
        List<Car> archivedCars = carRepository.findAllByArchivedIsTrue();
        if(cFLsb.size() > 0) {
            if(archivedCars.size() > 0) {
                List<CarFromBookingList> cFblNotArchived = cFLsb.stream()
                        .filter(cFbl -> archivedCars.stream()
                                .anyMatch(ac -> !ac.getCarId().equals(cFbl.getCarId())))
                        .collect(Collectors.toList());

                for (CarFromBookingList bkg : cFblNotArchived) {
                    if(date.after(bkg.getBDate())) {
                        Car car= carRepository.findByCarId(bkg.getCarId());
                        carsList.add(car);
                    }
                }
            } else {
                for (CarFromBookingList bkg : cFLsb) {
                    if(date.after(bkg.getBDate())) {
                        Car car= carRepository.findByCarId(bkg.getCarId());
                        carsList.add(car);
                    }
                }
            }
        }
        return carsList;
    }

    private List<Car> getCarsNotInListFromBooking(Long dsId) {
        List<Car> avCars = carRepository.findAvailableCarsBySite(dsId);

        List<Car> carsList = new ArrayList<>();
        List<CarFromBookingList> cFLsb = bookingRepository.findCarsFromLastSiteInBooking(dsId);
        if(cFLsb.size() > 0) {
            if (avCars.size() > 0) {
                List<Car> archivedCars = carRepository.findAllByArchivedIsTrue();
                if (archivedCars.size() > 0) {
                    List<CarFromBookingList> cFbNotArchived = cFLsb.stream()
                            .filter(cFbl -> archivedCars.stream()
                                    .anyMatch(ac -> !ac.getCarId().equals(cFbl.getCarId())))
                            .collect(Collectors.toList());

                    carsList = avCars.stream()
                            .filter(avc -> cFbNotArchived.stream()
                                    .noneMatch(cl -> cl.getCarId().equals(avc.getCarId())))
                            .collect(Collectors.toList());
                } else {
                    carsList = avCars.stream()
                            .filter(avc -> cFLsb.stream()
                                    .noneMatch(cl -> cl.getCarId().equals(avc.getCarId())))
                            .collect(Collectors.toList());
                }
            }
        } else {
            carsList = new ArrayList<>(avCars);
        }
        return carsList;
    }

    /*private static <T> Predicate<Car> distinctByKey(Function<? super Car, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }*/

    private void setCarProperties(Car car, int power, int numberOfSeats, int odometer, Date insuranceValidityDate,
                                  Date serviceValidityDate, CarBrand carBrand, CarModel carModel, Site carSite) {
        car.setPower(power);
        car.setNumberOfSeats(numberOfSeats);
        car.setOdometer(odometer);
        car.setInsuranceValidityDate(insuranceValidityDate);
        car.setServiceValidityDate(serviceValidityDate);
        car.setCarBrand(carBrand);
        car.setCarModel(carModel);
        car.setCarSite(carSite);
    }
}
