package fr.eni.ms2isi9bg3.gfv.service;

import fr.eni.ms2isi9bg3.gfv.domain.Car;
import fr.eni.ms2isi9bg3.gfv.domain.CarBrand;
import fr.eni.ms2isi9bg3.gfv.domain.CarModel;
import fr.eni.ms2isi9bg3.gfv.domain.Site;
import fr.eni.ms2isi9bg3.gfv.enums.CarStatus;
import fr.eni.ms2isi9bg3.gfv.repository.CarRepository;
import fr.eni.ms2isi9bg3.gfv.service.exception.RegistrationNumberAlreadyUsedException;
import fr.eni.ms2isi9bg3.gfv.service.exception.VinAlreadyUsedException;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Date;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
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
        setCarProperties(newCar, car.getPower(), car.getNumberOfSeats(), car.getOdometer(), car.getInsuranceValidityDate(), car.getServiceValidityDate(), car.getCarBrand(), car.getCarModel(), car.getCarSite());
        newCar.setCarStatus(CarStatus.AVAILABLE);
        carRepository.save(newCar);
        return newCar;
    }

    public Car updateCar(Car car) {
        Optional<Car> carByVin = carRepository.findOneByVin(car.getVin());
        if (carByVin.isPresent()) {
            if (carByVin.get().getCarId() != car.getCarId()
                    && carByVin.get().getVin().equals(car.getVin().toLowerCase())) {
                throw new VinAlreadyUsedException();
            }
        }
        car.setVin(car.getVin().toLowerCase());

        Optional<Car> carByRegNb = carRepository.findOneByRegistrationNumber(car.getRegistrationNumber());
        if (carByRegNb.isPresent()) {
            if (carByRegNb.get().getCarId() != car.getCarId()
                    && carByRegNb.get().getRegistrationNumber().equals(car.getRegistrationNumber().toLowerCase())) {
                throw new RegistrationNumberAlreadyUsedException();
            }
        }
        car.setRegistrationNumber(car.getRegistrationNumber().toLowerCase());

        setCarProperties(car, car.getPower(), car.getNumberOfSeats(), car.getOdometer(), car.getInsuranceValidityDate(), car.getServiceValidityDate(), car.getCarBrand(), car.getCarModel(), car.getCarSite());
        car.setCarStatus(car.getCarStatus());
        carRepository.save(car);
        return car;
    }

    public void updateCarStatus(Car car, CarStatus status) {
        car.setCarStatus(status);
        carRepository.save(car);
    }

    private void setCarProperties(Car car, int power, int numberOfSeats, int odometer, Date insuranceValidityDate, Date serviceValidityDate, CarBrand carBrand, CarModel carModel, Site carSite) {
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
