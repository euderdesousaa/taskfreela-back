package tech.engix.auth_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import tech.engix.auth_service.model.enums.AuthProvider;

@Entity
@Table(name = "user_tb")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Email
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private String resetPasswordToken;

}
