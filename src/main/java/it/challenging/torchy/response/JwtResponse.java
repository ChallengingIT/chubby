package it.challenging.torchy.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String nome;
    private String cognome;
    private List<String> roles;

    public JwtResponse(String accessToken, String username, String nome, String cognome, List<String> roles) {
        this.token = accessToken;
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.roles = roles;
    }

}