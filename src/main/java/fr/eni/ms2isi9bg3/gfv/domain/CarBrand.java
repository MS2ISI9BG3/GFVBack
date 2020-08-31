package fr.eni.ms2isi9bg3.gfv.domain;
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

    @NotNull
    @Column(name = "isArchived", nullable = false)
    private boolean archived = false;

}
