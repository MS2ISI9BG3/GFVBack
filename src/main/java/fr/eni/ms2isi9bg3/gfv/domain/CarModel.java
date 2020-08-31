package fr.eni.ms2isi9bg3.gfv.domain;

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
    @JoinColumn(name = "carBrandId")
    private CarBrand carBrand;

    @NotNull
    @Column(name = "isArchived", nullable = false)
    private boolean archived = false;
}
