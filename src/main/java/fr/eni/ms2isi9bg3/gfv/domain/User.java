package fr.eni.ms2isi9bg3.gfv.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.eni.ms2isi9bg3.gfv.config.Constants;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "gfv_user")
@Data
public class User extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	@Column(length = 50, unique = true, nullable = false)
	private String login;

	@JsonIgnore
	@NotNull
	@Size(min = 60, max = 60)
	@Column(name = "password_hash", length = 60, nullable = false)
	private String password;

	@Size(max = 50)
	@Column(name = "first_name", length = 50)
	private String firstName;

	@Size(max = 50)
	@Column(name = "last_name", length = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 254)
	@Column(length = 254, unique = true)
	private String email;

	@NotNull
	@Pattern(regexp = Constants.PHONE_NUMBER_REGEX)
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@NotNull
	@Column(name = "is_activated", nullable = false)
	private boolean activated = false;

	@NotNull
	@Column(name = "is_archived", nullable = false)
	private boolean archived = false;

	@Size(max = 256)
	@Column(name = "image_url", length = 256)
	private String imageUrl;

	@Size(max = 20)
	@Column(name = "activation_key", length = 20)
	@JsonIgnore
	private String activationKey;

	@Size(max = 20)
	@Column(name = "reset_key", length = 20)
	@JsonIgnore
	private String resetKey;

	@Column(name = "reset_date")
	private Instant resetDate = null;

	@JsonIgnore
	@ManyToMany
	@JoinTable(
					name = "gfv_user_authority",
					joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
					inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
	@BatchSize(size = 20)
	private Set<Authority> authorities = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}
		return id != null && id.equals(((User) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return "User{" +
						"login='" + login + '\'' +
						", firstName='" + firstName + '\'' +
						", lastName='" + lastName + '\'' +
						", email='" + email + '\'' +
						", phoneNumber='" + phoneNumber + '\'' +
						", imageUrl='" + imageUrl + '\'' +
						", activated='" + activated + '\'' +
						", archived='" + archived + '\'' +
						", activationKey='" + activationKey + '\'' +
						"}";
	}
}
