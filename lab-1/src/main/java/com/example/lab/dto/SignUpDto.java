package com.example.lab.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpDto {
    @Size(min = 4, max = 28)
    @NotBlank
    private String username;

    @Size(min = 4, max = 32)
    @NotBlank
    private String name;

    @Size(min = 5, max = 128)
    @NotBlank
    @Email
    private String email;

    @Size(min = 8, max = 32)
    @NotBlank
    private String password;
}
