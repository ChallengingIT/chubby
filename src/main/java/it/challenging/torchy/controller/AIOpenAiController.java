/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.entity.Candidato;
import it.challenging.torchy.entity.Tipologia;
import it.challenging.torchy.repository.CandidatoRepository;
import it.challenging.torchy.repository.TipologiaRepository;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/ai/open")
public class AIOpenAiController {

    @Autowired
    private TipologiaRepository tipologiaRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    private final EmbeddingClient embeddingClient;

    private final OpenAiChatClient chatClient;

    private final OpenAiImageClient openaiImageClient;

    private static final String SYSTEM_MESSAGE = """
            Sei un utilissimo server API che risponse in formato JSON.
            Non sai altro. Rispondi solo con il JSON.
            
            L'utente ti farà una domanda sui nostri dati e tu risponderai in JSON.
            """;

    public AIOpenAiController(EmbeddingClient embeddingClient, OpenAiChatClient chatClient, OpenAiImageClient openaiImageClient) {

        this.embeddingClient   = embeddingClient;
        this.chatClient        = chatClient;
        this.openaiImageClient = openaiImageClient;
    }

    @GetMapping("/embedding")
    public Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingClient.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }

    @GetMapping("/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", chatClient.call(message));
    }

    @GetMapping("/generate/assistant")
    public Flux<ChatResponse>  generateAssistant(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {

        Prompt prompt = new Prompt(new AssistantMessage(message));
        return chatClient.stream(prompt);
    }

    @GetMapping("/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatClient.stream(prompt);
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