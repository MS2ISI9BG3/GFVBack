package fr.eni.ms2isi9bg3.gfv.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.eni.ms2isi9bg3.gfv.config.Constants;
import lombok.Data;

import javax.persistence.*;
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

    @Pattern(regexp = Constants.VIN_REGEX)
    private String vin;

    @Pattern(regexp = Constants.PLATE_NUMBER_REGEX)
    private String plateNumber;

    private int power;

    private int numberOfSeats;

    private int odometer;

    @Temporal(TemporalType.DATE)
    private Date insuranceValidityDate;

    @Temporal(TemporalType.DATE)
    private Date serviceValidityDate;

    @NotNull
    @Column(name = "IS_ARCHIVED", nullable = false)
    private boolean archived = false;

    @NotNull
    @Column(name = "IS_AVAILABLE", nullable = false)
    private boolean available = false;

    @NotNull
    @Column(name = "IS_RESERVED", nullable = false)
    private boolean reserved = false;

    @ManyToOne
    @JoinColumn(name = "CAR_BRAND_ID")
    private CarBrand carBrand;

    @ManyToOne
    @JoinColumn(name = "CAR_MODEL_ID")
    private CarModel carModel;
}
