package fr.eni.ms2isi9bg3.gfv.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "gfv_city")
@Data
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;

    @NotNull
    @Column(name = "cityNmae", nullable = false)
    private String cityName;

    @NotNull
    @Column(name = "cityZipCode", nullable = false)
    private String cityZipCode;

}
