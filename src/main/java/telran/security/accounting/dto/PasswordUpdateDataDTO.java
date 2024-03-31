package telran.security.accounting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PasswordUpdateDataDTO(
		@NotEmpty(message = "missing email") @Email(message = "wrong email format") String email,
		@NotNull @Size(min = 8) String newPassword) {

}
