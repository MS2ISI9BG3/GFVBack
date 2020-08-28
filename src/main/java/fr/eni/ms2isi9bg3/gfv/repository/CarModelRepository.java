package fr.eni.ms2isi9bg3.gfv.repository;

import fr.eni.ms2isi9bg3.gfv.domain.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {

    List<CarModel> findAllByCarBrandBrandIdOrderByModelNameAsc(Long brandId);

    @Query("SELECT cm FROM CarModel cm WHERE cm.status = fr.eni.ms2isi9bg3.gfv.enums.CommonStatus.AVAILABLE")
    List<CarModel> findAvailableCarModels();
}
