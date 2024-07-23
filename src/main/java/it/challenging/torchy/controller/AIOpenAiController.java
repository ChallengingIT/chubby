/*
 * Copyright (c) 2023-2024.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiImageClient;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/open")
public class AIOpenAiController {

    private final EmbeddingClient embeddingClient;

    private final OpenAiChatClient chatClient;

    private final OpenAiImageClient openaiImageClient;

    @PersistenceContext
    private EntityManager em;

    private static final String SYSTEM_MESSAGE = """
            Sei un gestionale che deve gestire tutti i dati interni della nostra organizzazione. Il sistema si basa su mysql.
            Sapendo che la tabella delle associazioni è associazione_candidato_need con colonne id e data_modifica ,
            la tabella delle attività è attivita, la tabella di congiunzione tra attivita e cliente è attivita_cliente con colonne id_attivita e id_cliente,
            la tabella di congiunzione tra attivita e contatto o key people è attivita_key_people con colonne id_attivita e id_candidato,
            la tabella delle azioni è azioni, la tabella dei candidati è candidato,
            la tabella di congiunzione tra candidato e associazione è candidato_associazione con colonne id_associazione e id_candidato,
            la tabella di congiunzione tra candidato e intervista è candidato_intervista con colonne id_intervista e id_candidato,
            la tabella di congiunzione tra candidato e owner è candidato_owner con colonne id_owner e id_candidato,
            la tabella dei clienti o dei business o delle aziende è cliente,
            la tabella di congiunzione tra cliente/business/azienda e owner è cliente_owner,
            la tabella delle facoltà è facolta, la tabella di congiunzione tra facolta e candidato è facolta_candidato,
            la tabella dei file è file, la tabella di congiunzione tra file e candidato è file_candidato,
            la tabella delle funzioni aziendali è funzioni_aziendali,
            la tabella di congiunzione tra funzioni aziendali e tipologie delle funzioni aziendali è funzioni_tipologie,
            la tabella degli hiring è hiring, la tabella delle interviste è intervista,
            la tabella di congiunzione tra interviste e owner per i futuri follow up è intervista_next_owner,
            la tabella di congiunzione tra interviste e owner è intervista_owner,
            la tabella dei contatti o key people è key_people,
            la tabella di congiunzione tra contatto o key people e azioni è key_people_azioni,
            la tabella di congiunzione tra contatto o key people e cliente/business/azienda è key_people_cliente,
            la tabella di congiunzione tra contatto o key people e owner è key_people_owner,
            la tabella di congiunzione tra contatto o key people e stato è key_people_stato,
            la tabella dei livelli di studio è livelli_scolastici,
            la tabella di congiunzione tra livelli di studio e candidati è livello_candidato,
            la tabella delle modalità di lavoro è modalita_lavoro,
            la tabella di congiunzione tra modalità di lavoro e need è modalita_lavoro_need,
            la tabella dei need è need, la tabella di congiunzione tra need e associazione è need_associazione con colonne id_need e id_associazione,
            la tabella di congiunzione tra need e candidato è need_candidato,
            la tabella di congiunzione tra need e cliente/business/azienda è need_cliente,
            la tabella di congiunzione tra need e job title è need_job_title,
            la tabella di congiunzione tra need e contact/key people è need_keypeople,
            la tabella di congiunzione tra need e owner è need_owner,
            la tabella degli owner è owner, la tabella di congiunzione tra owner e associazione è owner_associazione,
            la tabella di congiunzione tra owner e attività è owner_attivita,
            la tabella delle skill è skill, la tabella di congiunzione tra skill e candidato è skill_candidato,
            la tabella di congiunzione tra skill e need è skill_need,
            la tabella di congiunzione tra stato dell'associazione e l'associazione stessa è stato_associazione,
            la tabella di congiunzione tra stato del candidato e il candidato stesso è stato_candidato,
            la tabella di congiunzione tra stato dell'intervista e l'intervista stessa è stato_intervista,
            la tabella di congiunzione tra stato del need e il need stesso è stato_need,
            la tabella degli stati dell'associazione è statoa, la tabella degli stati del candidato è statoc,
            la tabella degli stati del contact/key people è statok, la tabella degli stati del need è staton,
            la tabelle dei tipi o tipologie del candidato è tipo,
            la tabelle di congiunzione tra i tipi o tipologie del candidato ed il candidato stesso è tipo_candidato,
            la tabella delle tipologie delle attività è tipologia_attivita,
            la tabella di congiunzione tra tipologie delle attività ed attività è tipologia_attivita_attivita,
            la tabella di congiunzione tra tipologie delle azioni ed azioni è tipologia_azione,
            la tabella di congiunzione tra job title e candidati è tipologia_candidato,
            la tabella di congiunzione tra tipologie dei file e file è tipologia_file,
            la tabella di congiunzione tra tipologie delle interviste ed interviste è tipologia_intervista,
            la tabella di congiunzione tra tipologie dei need e need è tipologia_need,
            la tabella dei job title è tipologie, la tabella delle tipologie delle azioni è tipologieaz,
            la tabella delle tipologie dei file è tipologief, la tabella delle tipologie delle interviste è tipologiei,
            la tabella delle tipologie dei need è tipologien. Potresti estrarre le informazioni richieste dal prossimo messaggio?
            """;

    @GetMapping("/embedding")
    public Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingClient.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }

    @GetMapping("/generate")
    public ResponseEntity generate(@RequestParam(value = "message", defaultValue = "Raccontami una barzelletta") String message) throws IOException {

        var          systemMessage = new SystemMessage(SYSTEM_MESSAGE);
        ChatResponse chatResponse  = chatClient.call(new Prompt(List.of(systemMessage, new UserMessage(message))));
        String query = null;
        String response = chatResponse.getResults().get(0).getOutput().getContent();
        if( response.contains("sql\n") ) {
            query = response.split("sql\n")[1].split(";")[0];

            List lista = em.createQuery(query).getResultList();

            if (null != lista) {
                return ResponseEntity.badRequest().body("KO");
            } else {
                return ResponseEntity.ok(response);
            }
        } else  {
            return ResponseEntity.ok(response);
        }
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