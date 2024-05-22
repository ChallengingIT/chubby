package it.challenging.torchy.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    private String username;

    private String nome;

    private String cognome;

    private String role;

    private String email;

    @NotBlank
    private String password;
}