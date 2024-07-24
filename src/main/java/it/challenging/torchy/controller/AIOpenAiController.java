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
            la tabella delle attività è attivita con colonne id, data e note,
            la tabella di congiunzione tra attivita e cliente è attivita_cliente con colonne id_attivita e id_cliente,
            la tabella di congiunzione tra attivita e contatto o key people è attivita_key_people con colonne id_attivita e id_candidato,
            la tabella delle azioni è azioni con colonne id, data_modifica e note,
            la tabella dei candidati è candidato con colonne id, nome, cognome, data_nascita, email, anni_esperienza, residenza, note,
            data_ultimo_contatto, rating, cellulare, ral_tariffa, ricerca, anni_esperienza_ruolo e disponibilita,
            la tabella di congiunzione tra candidato e associazione è candidato_associazione con colonne id_associazione e id_candidato,
            la tabella di congiunzione tra candidato e intervista è candidato_intervista con colonne id_intervista e id_candidato,
            la tabella di congiunzione tra candidato e owner è candidato_owner con colonne id_owner e id_candidato,
            la tabella dei clienti o dei business o delle aziende è cliente con colonne id, citta, denominazione, cf, pi,
            codice_destinatario, pec, sito, paese, settore_mercato, tipologia, sede_legale, sede_operativa, ida, semplicita, potenzialita,
            la tabella di congiunzione tra cliente/business/azienda e owner è cliente_owner con colonne id_owner e id_cliente,
            la tabella delle facoltà è facolta con colonne id e descrizione,
            la tabella di congiunzione tra facolta e candidato è facolta_candidato con colonne id_facolta e id_candidato,
            la tabella dei file è file con colonne id, data_inserimento, tipo, descrizione e data che contiene il longblob,
            la tabella di congiunzione tra file e candidato è file_candidato con colonne id_file e id_candidato,
            la tabella delle funzioni aziendali è funzioni_aziendali con colonne id e descrizione,
            la tabella di congiunzione tra funzioni aziendali e tipologie delle funzioni aziendali è funzioni_tipologie con colonne id_funzione e id_tipologia,
            la tabella delle interviste è intervista con colonne id, aderenza_posizione, attuale, coerenza_percorso,
            comunicazione, data_colloquio, descrizione_candidato_una, desiderata, disponibilita, energia, inglese,
            intervistatore, mobilita, motivazione_posizione, preavviso, proposta, standing, team_si_no e valutazione competenze,
            la tabella di congiunzione tra interviste e owner per i futuri follow up è intervista_next_owner con colonne id_intervista e id_owner,
            la tabella di congiunzione tra interviste e owner è intervista_owner con colonne id_intervista e id_owner,
            la tabella dei contatti o key people è key_people con colonne id,cellulare, data_ultima_attivita, email, nome, note e ruolo,
            la tabella di congiunzione tra contatto o key people e azioni è key_people_azioni con colonne id_key_people e id_azione,
            la tabella di congiunzione tra contatto o key people e cliente/business/azienda è key_people_cliente con colonne id_key_people e id_cliente,
            la tabella di congiunzione tra contatto o key people e owner è key_people_owner con colonne id_key_people e id_owner,
            la tabella di congiunzione tra contatto o key people e stato è key_people_stato con colonne id_key_people e id_stato,
            la tabella dei livelli di studio è livelli_scolastici con colonne id e descrizione,
            la tabella di congiunzione tra livelli di studio e candidati è livello_candidato con colonne id_livello e id_candidato,
            la tabella delle modalità di lavoro è modalita_lavoro con colonne id e descrizione,
            la tabella di congiunzione tra modalità di lavoro e need è modalita_lavoro_need con colonne id_modalita_lavoro e id_need,
            la tabella dei need è need, la tabella di congiunzione tra need e associazione è need_associazione con colonne id_need e id_associazione,
            la tabella di congiunzione tra need e candidato è need_candidato con colonne id_candidato e id_need,
            la tabella di congiunzione tra need e cliente/business/azienda è need_cliente con colonne id_need e id_cliente,
            la tabella di congiunzione tra need e job title è need_job_title con colonne id_job_title e id_need,
            la tabella di congiunzione tra need e contact/key people è need_keypeople con colonne id_need e id_keypeople,
            la tabella di congiunzione tra need e owner è need_owner con colonne id_need e id_owner,
            la tabella degli owner è owner con colonne id, descrizione, email, cognome e nome,
            la tabella di congiunzione tra owner e associazione è owner_associazione con colonne id_owner e id_associazione,
            la tabella di congiunzione tra owner e attività è owner_attivita con colonne id_owner e id_attivita,
            la tabella delle skill è skill con colonne id e descrizione,
            la tabella di congiunzione tra skill e candidato è skill_candidato con colonne id_candidato e id_skill,
            la tabella di congiunzione tra skill e need è skill_need con colonne id_need e id_skill,
            la tabella di congiunzione tra stato dell'associazione e l'associazione stessa è stato_associazione con colonne id_stato e id_associazione,
            la tabella di congiunzione tra stato del candidato e il candidato stesso è stato_candidato con colonne id_stato e id_candidato,
            la tabella di congiunzione tra stato dell'intervista e l'intervista stessa è stato_intervista con colonne id_stato e id_intervista,
            la tabella di congiunzione tra stato del need e il need stesso è stato_need con colonne id_stato e id_need,
            la tabella degli stati dell'associazione è statoa con colonne id e descrizione,
            la tabella degli stati del candidato è statoc con colonne id e descrizione,
            la tabella degli stati del contact/key people è statok con colonne id e descrizione,
            la tabella degli stati del need è staton con colonne id e descrizione,
            la tabelle dei tipi o tipologie del candidato è tipo con colonne id e descrizione,
            la tabelle di congiunzione tra i tipi o tipologie del candidato ed il candidato stesso è tipo_candidato con colonne id_tipo e id_candidato,
            la tabella delle tipologie delle attività è tipologia_attivita con colonne id e descrizione,
            la tabella di congiunzione tra tipologie delle attività ed attività è tipologia_attivita_attivita con colonne id_tipologia e id_attivita,
            la tabella di congiunzione tra tipologie delle azioni ed azioni è tipologia_azione con colonne id_tipologia e id_azione,
            la tabella di congiunzione tra job title e candidati è tipologia_candidato con colonne id_tipologia e id_candidato,
            la tabella di congiunzione tra tipologie dei file e file è tipologia_file con colonne id_tipologia e id_file,
            la tabella di congiunzione tra tipologie delle interviste ed interviste è tipologia_intervista con colonne id_tipologia e id_intervista,
            la tabella di congiunzione tra tipologie dei need e need è tipologia_need con colonne id_tipologia e id_need,
            la tabella dei job title è tipologie con colonne id e descrizione,
            la tabella delle tipologie delle azioni è tipologieaz con colonne id e descrizione,
            la tabella delle tipologie dei file è tipologief con colonne id e descrizione,
            la tabella delle tipologie delle interviste è tipologiei con colonne id e descrizione,
            la tabella delle tipologie dei need è tipologien con colonne id e descrizione.
            Potresti estrarre le informazioni richieste dal prossimo messaggio?
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
        String       query;
        String       response      = chatResponse.getResults().get(0).getOutput().getContent();

        if (response.contains("sql\n")) {
            query = response.split("sql\n")[1].split(";")[0];

            List lista = em.createNativeQuery(query).getResultList();

            if (null == lista) {
                return ResponseEntity.badRequest().body("KO");
            } else {
                return ResponseEntity.ok(lista);
            }
        } else {
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