package com.example.lab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LogInDto {
    @Size(min = 4, max = 128)
    @NotBlank
    private String login;

    @Size(min = 8, max = 32)
    @NotBlank
    private String password;
}
