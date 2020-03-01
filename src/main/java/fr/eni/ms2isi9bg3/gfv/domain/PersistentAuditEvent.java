package fr.eni.ms2isi9bg3.gfv.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Persist AuditEvent managed by the Spring Boot actuator.
 *
 * @see org.springframework.boot.actuate.audit.AuditEvent
 */
@Entity
@Table(name = "gfv_persistent_audit_event")
@Data
public class PersistentAuditEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id")
	private Long id;

	@NotNull
	@Column(nullable = false)
	private String principal;

	@Column(name = "event_date")
	private Instant auditEventDate;

	@Column(name = "event_type")
	private String auditEventType;

	@ElementCollection
	@MapKeyColumn(name = "name")
	@Column(name = "value")
	@CollectionTable(name = "gfv_persistent_audit_evt_data", joinColumns = @JoinColumn(name = "event_id"))
	private Map<String, String> data = new HashMap<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PersistentAuditEvent)) {
			return false;
		}
		return id != null && id.equals(((PersistentAuditEvent) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return "PersistentAuditEvent{" +
						"principal='" + principal + '\'' +
						", auditEventDate=" + auditEventDate +
						", auditEventType='" + auditEventType + '\'' +
						'}';
	}
}
