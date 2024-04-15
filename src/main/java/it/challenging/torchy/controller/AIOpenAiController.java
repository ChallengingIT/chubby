/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiImageClient;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/ai/open")
public class AIOpenAiController {

    private final EmbeddingClient embeddingClient;

    private final OpenAiChatClient chatClient;

    private final OpenAiImageClient openaiImageClient;

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

    @GetMapping("/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatClient.stream(prompt);
    }

    @GetMapping("/image")
    public ImageResponse generateImage(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return openaiImageClient.call(
            new ImagePrompt(message,
                OpenAiImageOptions.builder()
                                  .withQuality("hd")
                                  .withN(4)
                                  .withHeight(1024)
                                  .withWidth(1024).build())

        );
    }
}