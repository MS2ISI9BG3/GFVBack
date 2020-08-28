package fr.eni.ms2isi9bg3.gfv.repository;

import fr.eni.ms2isi9bg3.gfv.domain.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {

    List<CarBrand> findAllByOrderByBrandNameAsc();

    @Query("SELECT cb FROM CarBrand cb " +
            "WHERE cb.status = fr.eni.ms2isi9bg3.gfv.enums.CommonStatus.AVAILABLE " +
            "ORDER BY cb.brandName")
    List<CarBrand> findAvailableCarBrands();
}
