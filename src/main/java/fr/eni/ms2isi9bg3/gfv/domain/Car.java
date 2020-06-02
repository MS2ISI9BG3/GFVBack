package fr.eni.ms2isi9bg3.gfv.domain;

import fr.eni.ms2isi9bg3.gfv.config.Constants;
import fr.eni.ms2isi9bg3.gfv.enums.CarStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Table(name = "gfv_car")
@Data
public class Car extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    @NotBlank
    @Pattern(regexp = Constants.VIN_REGEX)
    @Column(unique = true)
    private String vin;

    @NotBlank
    @Pattern(regexp = Constants.REGISTRATION_NUMBER_REGEX)
    @Column(unique = true)
    private String registrationNumber;

    private int power;

    private int numberOfSeats;

    @NotNull
    private int odometer;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date insuranceValidityDate;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceValidityDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CAR_BRAND_ID", nullable = false)
    private CarBrand carBrand;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CAR_MODEL_ID", nullable = false)
    private CarModel carModel;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CAR_SITE_ID", nullable = false)
    private Site carSite;

    @Enumerated(EnumType.STRING)
    @Column(name = "CAR_STATUS")
    private CarStatus carStatus;
}
