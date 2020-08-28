package fr.eni.ms2isi9bg3.gfv.domain;

import fr.eni.ms2isi9bg3.gfv.enums.CommonStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "gfv_carModel")
@Data
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modelId;

    @NotNull
    private String modelName;

    @ManyToOne
    @JoinColumn(name = "CAR_BRAND_ID")
    private CarBrand carBrand;

    @Enumerated(EnumType.STRING)
    private CommonStatus status;
}
