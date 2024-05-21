package it.challenging.torchy.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LostPasswordRequest {
    @NotBlank
    private String email;

}