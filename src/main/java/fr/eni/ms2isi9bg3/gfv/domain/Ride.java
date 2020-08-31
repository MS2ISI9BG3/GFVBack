package fr.eni.ms2isi9bg3.gfv.domain;
/*
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "gfv_ride")
@Data
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideId;

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
}
*/