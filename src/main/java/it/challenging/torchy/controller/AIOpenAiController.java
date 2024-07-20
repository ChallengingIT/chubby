/*
 * Copyright (c) 2023-2024.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.entity.Candidato;
import it.challenging.torchy.entity.KeyPeople;
import it.challenging.torchy.entity.Tipologia;
import it.challenging.torchy.repository.CandidatoRepository;
import it.challenging.torchy.repository.KeyPeopleRepository;
import it.challenging.torchy.repository.TipologiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiImageClient;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/open")
public class AIOpenAiController {

    @Autowired
    private TipologiaRepository tipologiaRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;
    @Autowired
    private KeyPeopleRepository keyPeopleRepository;

    private final EmbeddingClient embeddingClient;

    private final OpenAiChatClient chatClient;

    private final OpenAiImageClient openaiImageClient;

    private static final String SYSTEM_MESSAGE = """
            Sei un recruiter che deve condividere il cv schermato di un tuo candidato ad un'azienda per proporre un colloquio conoscitivo.
            Per costruire un cv schermato hai bisogno di estrarre le esperienze dal cv normale in questa modalità: Inizio e fine Attività,
            Job title posizione svolta, settore azienda (no nome), attività svolta durante l'esperienza lavorativa suddivisa in tre massimo
            cinque punti sintetici, stack tecnologico utilizzato. Potresti estrarre queste informazioni dal cv in allegato?
            """;

    @GetMapping("/embedding")
    public Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingClient.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }

    @GetMapping("/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) throws IOException {
        List<KeyPeople> keyPeople = keyPeopleRepository.findAll();

        Candidato candidato = new Candidato();

        byte[] pdf = candidato.getFiles().get(0).getData();

        var userMessage = new UserMessage(SYSTEM_MESSAGE,
                List.of(new Media(MimeTypeUtils.APPLICATION_OCTET_STREAM, pdf)));

        ChatResponse chatResponse = chatClient.call(new Prompt(List.of(userMessage)));

        return Map.of("response", chatResponse.getResults().get(0).getOutput());

    }

    @GetMapping("/try")
    public Map generateStreamWithDB(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        List<Tipologia> tipologieJson = tipologiaRepository.findAll();
        List<Candidato> candidatoList = candidatoRepository.findAll();

        message = message + " cerca il messaggio precedente in questa lista " + candidatoList;

        return Map.of("generation", chatClient.call(message));
    }

    @GetMapping("/image")
    public ImageResponse generateImage(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        ImageResponse response = openaiImageClient.call(
                new ImagePrompt(message,
                        OpenAiImageOptions.builder()
                                .withQuality("hd")
                                .withN(4)
                                .withHeight(1024)
                                .withWidth(1024).build())

        );

        return response;
    }
}