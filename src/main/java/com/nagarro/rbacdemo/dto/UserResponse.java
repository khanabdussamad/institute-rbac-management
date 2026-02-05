package com.nagarro.rbacdemo.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private String username;
    private String email;
    private String userType;
    private String status;
    private String firstName;
    private String lastName;
}
