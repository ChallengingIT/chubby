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
public class AppuntamentoRequest {

    @NotBlank
    private String oggetto;

    @NotBlank
    private String luogo;

    @NotBlank
    private String note;

    @NotBlank
    private String destinatari;

    @NotBlank
    private String inizio;

    @NotBlank
    private String fine;

    @NotBlank
    private String ownerIds;

}