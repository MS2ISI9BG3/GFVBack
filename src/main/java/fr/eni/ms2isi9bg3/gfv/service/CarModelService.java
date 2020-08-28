package fr.eni.ms2isi9bg3.gfv.service;

import fr.eni.ms2isi9bg3.gfv.domain.CarModel;
import fr.eni.ms2isi9bg3.gfv.enums.CommonStatus;
import fr.eni.ms2isi9bg3.gfv.repository.CarModelRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarModelService {
    private final CarModelRepository carModelRepository;
    private final MessageSource messageSource;

    public CarModelService(CarModelRepository carModelRepository, MessageSource messageSource) {
        this.carModelRepository = carModelRepository;
        this.messageSource = messageSource;
    }

    public CarModel saveCarModel(CarModel carModel) {
        CarModel newCarModel = new CarModel();
        newCarModel.setModelName(carModel.getModelName());
        newCarModel.setCarBrand(carModel.getCarBrand());
        newCarModel.setStatus(CommonStatus.AVAILABLE);
        carModelRepository.save(newCarModel);
        return newCarModel;
    }

    public CarModel updateCarModel(CarModel carModel) {
        carModel.setModelName(carModel.getModelName());
        carModel.setCarBrand(carModel.getCarBrand());
        carModel.setStatus(carModel.getStatus());
        carModelRepository.save(carModel);
        return carModel;
    }

    public List<CarModel> findAvailableModelByBrand(Long id) {
        List<CarModel> allAvailableCarModels = carModelRepository.findAvailableCarModels();
        List<CarModel> modelsByBrand = carModelRepository.findAllByCarBrandBrandIdOrderByModelNameAsc(id);

        List<CarModel> availableModelsByBrand = allAvailableCarModels.stream()
                .filter(am -> modelsByBrand.stream()
                    .anyMatch(mbb -> mbb.getModelId().equals(am.getModelId())))
                .collect(Collectors.toList());

        List result = availableModelsByBrand.stream().sorted(Comparator.comparing(CarModel::getModelName)).
                collect(Collectors.toList());
        return result;
    }
}
