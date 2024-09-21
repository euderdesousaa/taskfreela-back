package tech.engix.auth_service.model;

import jakarta.persistence.*;
import lombok.*;
import tech.engix.auth_service.model.enums.AuthProvider;

@Entity
@Table(name = "user_tb")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

}
