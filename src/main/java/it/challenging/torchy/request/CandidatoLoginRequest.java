package it.challenging.torchy.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidatoLoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}