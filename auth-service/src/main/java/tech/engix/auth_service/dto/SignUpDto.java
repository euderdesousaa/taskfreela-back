package tech.engix.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import tech.engix.auth_service.service.validation.auth.UserInsertValid;
@UserInsertValid
public record SignUpDto(

        String name,

        @Email(message = "This email is invalid")
        @NotEmpty(message = "Email cannot be empty")
        String email,

        //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$",
        //        message = "password must be min 4 and max 12 length containing at least 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
        @NotEmpty(message = "Password cannot be empty")
        String password) {
}
