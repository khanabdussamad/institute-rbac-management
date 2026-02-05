package com.nagarro.rbacdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nagarro.rbacdemo.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @JsonProperty("id")
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @JsonProperty("username")
    private String username;

    @NotBlank
    @Email
    @Size(max = 255)
    @JsonProperty("email")
    private String email;

    @Size(max = 255)
    @JsonProperty("password_hash")
    private String passwordHash;

    @Size(max = 255)
    @JsonProperty("auth_provider")
    private String authProvider;

    @Size(max = 255)
    @JsonProperty("external_id")
    private String externalId;

    @Size(max = 50)
    @JsonProperty("user_type")
    private String userType;

    @Size(max = 50)
    @JsonProperty("status")
    private String status;

    @JsonProperty("roles")
    private Set<Role> roles;

    @NotBlank
    @Size(max = 255)
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank
    @Size(max = 255)
    @JsonProperty("last_name")
    private String lastName;

}
