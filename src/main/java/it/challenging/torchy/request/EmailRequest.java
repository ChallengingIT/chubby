package it.challenging.torchy.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EmailRequest {
    @NotBlank
    private String oggetto;

    @NotBlank
    private String note;

    @NotBlank
    private String destinatari;

}