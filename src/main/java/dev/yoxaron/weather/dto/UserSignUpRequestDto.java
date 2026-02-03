package dev.yoxaron.weather.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignUpRequestDto {

        @NotBlank(message = "Login cannot be empty")
        @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]+$",
                message = "Login may contain only Latin letters, digits, and underscores"
        )
        private String login;

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 3, max = 100, message = "Password must be between 3 and 100 characters")
        private String password;
}
