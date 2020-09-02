package fr.eni.ms2isi9bg3.gfv.service.dto;

import fr.eni.ms2isi9bg3.gfv.config.Constants;
import fr.eni.ms2isi9bg3.gfv.domain.Authority;
import fr.eni.ms2isi9bg3.gfv.domain.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
@Data
public class UserDTO {

	private Long id;

	@NotBlank
	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	private String login;

	@Size(max = 50)
	private String firstName;

	@Size(max = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 254)
	private String email;

	@Pattern(regexp = Constants.PHONE_NUMBER_REGEX)
	private String phoneNumber;

	@Size(max = 256)
	private String imageUrl;

	private boolean activated = false;

	private boolean archived = false;

	private String createdBy;

	private Instant createdDate;

	private String lastModifiedBy;

	private Instant lastModifiedDate;

	private Set<String> authorities;

	public UserDTO() {
		// Empty constructor needed for Jackson.
	}

	public UserDTO(User user) {
		this.id = user.getId();
		this.login = user.getLogin();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.phoneNumber = user.getPhoneNumber();
		this.activated = user.isActivated();
		this.archived = user.isArchived();
		this.imageUrl = user.getImageUrl();
		this.createdBy = user.getCreatedBy();
		this.createdDate = user.getCreatedDate();
		this.lastModifiedBy = user.getLastModifiedBy();
		this.lastModifiedDate = user.getLastModifiedDate();
		this.authorities = user.getAuthorities().stream()
						.map(Authority::getName)
						.collect(Collectors.toSet());
	}

	@Override
	public String toString() {
		return "UserDTO{" +
						"login='" + login + '\'' +
						", firstName='" + firstName + '\'' +
						", lastName='" + lastName + '\'' +
						", email='" + email + '\'' +
						", phoneNumber='" + phoneNumber + '\'' +
						", imageUrl='" + imageUrl + '\'' +
						", activated=" + activated +
						", archived='" + archived + '\'' +
						", createdBy=" + createdBy +
						", createdDate=" + createdDate +
						", lastModifiedBy='" + lastModifiedBy + '\'' +
						", lastModifiedDate=" + lastModifiedDate +
						", authorities=" + authorities +
						"}";
	}
}
