package fr.eni.ms2isi9bg3.gfv.service;

import fr.eni.ms2isi9bg3.gfv.domain.CarBrand;
import fr.eni.ms2isi9bg3.gfv.enums.CommonStatus;
import fr.eni.ms2isi9bg3.gfv.repository.CarBrandRepository;
import org.springframework.stereotype.Service;

@Service
public class CarBrandService {
    private final CarBrandRepository carBrandRepository;

    public CarBrandService(CarBrandRepository carBrandRepository) {
        this.carBrandRepository = carBrandRepository;
    }

    public CarBrand saveCarBrand(CarBrand carBrand) {
        CarBrand newCarBrand = new CarBrand();
        newCarBrand.setBrandName(carBrand.getBrandName());
        newCarBrand.setStatus(CommonStatus.AVAILABLE);
        carBrandRepository.save(newCarBrand);
        return newCarBrand;
    }

    public CarBrand updateCarBrand(CarBrand carBrand) {
        carBrand.setBrandName(carBrand.getBrandName());
        carBrand.setStatus(carBrand.getStatus());
        carBrandRepository.save(carBrand);
        return carBrand;
    }
}
