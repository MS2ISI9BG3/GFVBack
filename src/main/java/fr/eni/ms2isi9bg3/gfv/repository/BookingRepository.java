package fr.eni.ms2isi9bg3.gfv.repository;

import fr.eni.ms2isi9bg3.gfv.domain.Booking;
import fr.eni.ms2isi9bg3.gfv.service.dto.CarFromBookingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUserId(Long id);

    @Query("SELECT NEW fr.eni.ms2isi9bg3.gfv.service.dto.CarFromBookingList(lb.car.carId, max(lb.arrivalDate))" +
            "FROM Booking lb WHERE lb.bookingStatus not in" +
            "(fr.eni.ms2isi9bg3.gfv.enums.BookingStatus.REJECTED, fr.eni.ms2isi9bg3.gfv.enums.BookingStatus.CANCELED)" +
            "and lb.arrivalSite.siteId = :id group by lb.car.carId")
    List<CarFromBookingList> findCarsFromLastSiteInBooking(Long id);

    @Query("SELECT  NEW fr.eni.ms2isi9bg3.gfv.service.dto.CarFromBookingList(nb.car.carId, max(nb.departureDate))" +
            "FROM Booking nb WHERE nb.bookingStatus not in" +
            "(fr.eni.ms2isi9bg3.gfv.enums.BookingStatus.REJECTED, fr.eni.ms2isi9bg3.gfv.enums.BookingStatus.CANCELED)" +
            "and nb.departureSite.siteId = :id group by nb.car.carId")
    List<CarFromBookingList> findCarsFromNextSiteInBooking(Long id);

    @Query("SELECT NEW fr.eni.ms2isi9bg3.gfv.service.dto.CarFromBookingList(lb.car.carId, lb.bookingStatus, max(lb.arrivalDate))" +
            "FROM Booking lb WHERE lb.bookingStatus not in" +
            "(fr.eni.ms2isi9bg3.gfv.enums.BookingStatus.REJECTED, fr.eni.ms2isi9bg3.gfv.enums.BookingStatus.CANCELED)" +
            "and lb.car.carId = :id")
    CarFromBookingList findCarLastBooking(Long id);
}
