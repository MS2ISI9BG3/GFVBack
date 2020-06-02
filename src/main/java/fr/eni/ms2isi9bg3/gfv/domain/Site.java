package fr.eni.ms2isi9bg3.gfv.domain;

import fr.eni.ms2isi9bg3.gfv.config.Constants;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "gfv_site")
@Data
public class Site extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long siteId;

    @NotNull
    @Column(name = "SITE_NAME", nullable = false)
    private String siteName;

    @NotNull
    @Column(name = "SITE_ADDRESS", nullable = false)
    private String siteAddress;

    @NotNull
    @Pattern(regexp = Constants.PHONE_NUMBER_REGEX)
    @Column(name = "SITE_PHONE_NUMBER", nullable = false)
    private String sitePhoneNumber;

    @NotNull
    @Column(name = "IS_ARCHIVED", nullable = false)
    private boolean isArchived = false;

}
