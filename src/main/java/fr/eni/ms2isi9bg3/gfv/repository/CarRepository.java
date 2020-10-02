package fr.eni.ms2isi9bg3.gfv.repository;

import fr.eni.ms2isi9bg3.gfv.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findOneByCarId(Long id);
    Car findByCarId(Long id);

    Optional<Car> findOneByVin(String vin);

    Optional<Car> findOneByRegistrationNumber(String regNb);

    @Query("SELECT c FROM Car c WHERE c.archived = false")
    List<Car> findAvailableCars();

    //@Query("SELECT c FROM Car c WHERE c.carSite.siteId = :id")
    //List<Car> findCarsBySite(Long id);

    @Query("SELECT c FROM Car c WHERE c.archived = false and c.carSite.siteId = :id")
    List<Car> findAvailableCarsBySite(Long id);

    List<Car> findAllByArchivedIsTrue();
}
