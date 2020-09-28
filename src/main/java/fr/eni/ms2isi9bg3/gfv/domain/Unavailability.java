package fr.eni.ms2isi9bg3.gfv.domain;

import fr.eni.ms2isi9bg3.gfv.enums.CarStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "gfv_unavailability")
@Data
public class Unavailability extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String reason;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date start_date;

    @Temporal(TemporalType.DATE)
    private Date end_date;

    @ManyToOne
    @JoinColumn(name = "carId")
    private Car car;
}
