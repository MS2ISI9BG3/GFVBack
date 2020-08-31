package fr.eni.ms2isi9bg3.gfv.repository;

import fr.eni.ms2isi9bg3.gfv.domain.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {

    List<CarModel> findAllByCarBrandBrandId(Long brandId);

    List<CarModel> findAllByArchivedIsFalse();
}
