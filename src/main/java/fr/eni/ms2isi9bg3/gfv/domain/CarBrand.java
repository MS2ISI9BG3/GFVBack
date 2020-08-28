package fr.eni.ms2isi9bg3.gfv.domain;

import fr.eni.ms2isi9bg3.gfv.enums.CommonStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "gfv_carBrand")
@Data
public class CarBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;

    @NotNull
    private String brandName;

    @Enumerated(EnumType.STRING)
    private CommonStatus status;

}
