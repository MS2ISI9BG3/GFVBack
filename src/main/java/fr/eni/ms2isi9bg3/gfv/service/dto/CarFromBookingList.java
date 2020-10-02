package fr.eni.ms2isi9bg3.gfv.service.dto;

import fr.eni.ms2isi9bg3.gfv.domain.Booking;
import fr.eni.ms2isi9bg3.gfv.enums.BookingStatus;
import lombok.Data;

import java.util.Date;

@Data
public class CarFromBookingList extends Booking {
    private Long carId;
    private Date bDate;
    private BookingStatus status;

    public CarFromBookingList(Long carId, Date bDate){
        this.carId = carId;
        this.bDate = bDate;
    }

    public CarFromBookingList(Long carId, BookingStatus status, Date bDate){
        this.carId = carId;
        this.status = status;
        this.bDate = bDate;
    }
}
