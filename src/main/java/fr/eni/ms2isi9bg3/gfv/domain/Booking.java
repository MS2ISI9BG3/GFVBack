package fr.eni.ms2isi9bg3.gfv.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

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
}
