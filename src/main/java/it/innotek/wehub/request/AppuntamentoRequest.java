package it.innotek.wehub.request;

import com.google.api.client.util.DateTime;
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
    private String location;

    @NotBlank
    private String note;

    @NotBlank
    private String email;

    @NotBlank
    private DateTime dataInizio;

    @NotBlank
    private DateTime dataFine;

}