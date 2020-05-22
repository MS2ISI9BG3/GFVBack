package fr.eni.ms2isi9bg3.gfv.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "gfv_carBrand")
@Data
public class CarBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;

    private String brandName;

}
