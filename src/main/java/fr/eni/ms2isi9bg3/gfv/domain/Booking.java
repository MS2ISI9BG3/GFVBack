package fr.eni.ms2isi9bg3.gfv.domain;

import fr.eni.ms2isi9bg3.gfv.enums.BookingStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "gfv_booking")
@Data
public class Booking extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date departureDate;

    @Temporal(TemporalType.DATE)
    private Date arrivalDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "departureSiteId", nullable = false)
    private Site departureSite;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "arrivalSiteId", nullable = false)
    private Site arrivalSite;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "carId", nullable = false)
    private Car car;

    private String description;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "bookingStatus")
    private BookingStatus bookingStatus;
}
