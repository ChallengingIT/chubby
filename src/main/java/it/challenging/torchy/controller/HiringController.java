package it.challenging.torchy.controller;

import it.challenging.torchy.entity.Hiring;
import it.challenging.torchy.entity.SchedaCandidato;
import it.challenging.torchy.entity.TerminePagamento;
import it.challenging.torchy.entity.TipoServizio;
import it.challenging.torchy.repository.HiringRepository;
import it.challenging.torchy.repository.SchedaCandidatoRepository;
import it.challenging.torchy.repository.TerminePagamentoRepository;
import it.challenging.torchy.repository.TipoServizioRepository;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/hiring")
public class HiringController {

    @Autowired
    private HiringRepository           hiringRepository;
    @Autowired
    private SchedaCandidatoRepository  schedaCandidatoRepository;
    @Autowired
    private TipoServizioRepository     tipoServizioRepository;
    @Autowired
    private TerminePagamentoRepository terminePagamentoRepository;

    private static final Logger logger = LoggerFactory.getLogger(HiringController.class);

    @GetMapping("/termini")
    public List<TerminePagamento> findAllTermini() {
        logger.info("Lista termini di pagamento");

        try  {

            return terminePagamentoRepository.findAll();

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/servizi")
    public List<TipoServizio> findAllServizi() {
        logger.info("Lista tipi di servizio");

        try  {

            return tipoServizioRepository.findAll();

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }
    @GetMapping
    public List<Hiring> findAll() {
        logger.info("Lista hiring totale");

        try  {

            return hiringRepository.findAll();

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/cliente")
    public List<Hiring> findAllByIdCliente(
            @RequestParam("idCliente") Integer idCliente,
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista hiring per idCliente");

        try  {
            Pageable p = PageRequest.of(pagina, quantita);

            return hiringRepository.findByIdCliente(idCliente, p).getContent();

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    //ricerca per idCliente e tipoServizio
    @GetMapping("/ricerca")
    public List<Hiring> findAllByIdClienteTipoServizio(
            @RequestParam("idCliente")  @Nullable Integer idCliente,
            @RequestParam("idTipoServizio")  @Nullable Integer idTipoServizio,
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Ricerca hiring per idCliente e idTipoServizio");

        try  {
            Pageable p = PageRequest.of(pagina, quantita);

            if(null != idCliente && null != idTipoServizio) {
                return hiringRepository.findAllByIdClienteAndTipoServizio_Id(idCliente, idTipoServizio, p).getContent();
            } else if (null == idCliente && null != idTipoServizio) {
                return hiringRepository.findAllByTipoServizio_Id(idTipoServizio, p).getContent();
            } else if (null != idCliente){
                return hiringRepository.findAllByIdCliente(idCliente, p).getContent();
            } else {
                return hiringRepository.findAll();
            }

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @PostMapping("/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public ResponseEntity<String> saveHiring(
            @RequestBody Map<String, String> hiringMap
    ){
        logger.info("Salva hiring");

        try {
            Hiring hiringEntity = new Hiring();

            if(hiringMap.get("id") != null) {
                hiringEntity = hiringRepository.findById(Integer.parseInt(hiringMap.get("id"))).get();

                logger.debug("Hiring trovato si procede in modifica");
            }

            trasformaMappaInHiring(hiringEntity, hiringMap);

            hiringRepository.save(hiringEntity);

            logger.debug("Hiring salvato correttamente");

            return ResponseEntity.ok(hiringEntity.getId()+"");

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return ResponseEntity.ok("ERRORE");
        }
    }

    //get scheda candidato by idCandidato
    @GetMapping("/scheda/candidato")
    public List<SchedaCandidato> findSchedaByIdCandidato(
            @RequestParam("idCandidato") Integer idCandidato,
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista schede candidato per idCandidato");

        try  {
            Pageable p = PageRequest.of(pagina, quantita);

            return schedaCandidatoRepository.findByIdCandidato(idCandidato, p).getContent();

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }
    //get scheda candidato by id
    @GetMapping("/scheda")
    public SchedaCandidato findSchedaById(
            @RequestParam("idScheda") Integer idScheda
    ) {
        logger.info("Scheda candidato by id");

        try  {

            return schedaCandidatoRepository.findById(idScheda).get();

        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }
    @PostMapping("/salva/scheda")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public ResponseEntity<String> saveScheda(
            @RequestParam("idHiring") Integer idHiring,
            @RequestBody Map<String, String> schedaMap
    ){
        logger.info("Salva scheda candidato");

        try {
            boolean         modifica              = false;
            SchedaCandidato schedaCandidatoEntity = new SchedaCandidato();

            if(schedaMap.get("id") != null) {
                modifica              = true;
                schedaCandidatoEntity = schedaCandidatoRepository.findById(Integer.parseInt(schedaMap.get("id"))).get();

                logger.debug("Scheda candidato trovata si procede in modifica");
            }

            trasformaMappaInSchedaCandidato(schedaCandidatoEntity, schedaMap);

            if (!modifica) {
                Hiring hiring = hiringRepository.findById(idHiring).get();
                hiring.getSchedeCandidato().add(schedaCandidatoEntity);

                hiringRepository.save(hiring);
            } else {
                schedaCandidatoRepository.save(schedaCandidatoEntity);
            }

            logger.debug("Scheda candidato salvata correttamente");

            return ResponseEntity.ok("OK");

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return ResponseEntity.ok("ERRORE");
        }
    }


    public void trasformaMappaInHiring(Hiring hiring, Map<String,String> hiringMap) {

        logger.info("Trasforma mappa in hiring");

        hiring.setIdCliente(hiringMap.get("idCliente") != null ? Integer.parseInt(hiringMap.get("idCliente")) : null);
        hiring.setDenominazioneCliente(hiringMap.get("denominazione") != null ? hiringMap.get("denominazione") : null);

        if (hiringMap.get("idTipoServizio") != null) {
            TipoServizio tipoServizio = new TipoServizio();
            tipoServizio.setId(Integer.parseInt(hiringMap.get("idTipoServizio")));

            hiring.setTipoServizio(tipoServizio);
        }

    }

    public void trasformaMappaInSchedaCandidato(SchedaCandidato schedaCandidato, Map<String,String> schedaMap) {

        logger.info("Trasforma mappa in scheda candidato");

        schedaCandidato.setIdCandidato(schedaMap.get("idCandidato") != null ? Integer.parseInt(schedaMap.get("idCandidato")) : null);
        schedaCandidato.setNomeCandidato(schedaMap.get("nome") != null ? schedaMap.get("nome") : null);
        schedaCandidato.setCognomeCandidato(schedaMap.get("cognome") != null ? schedaMap.get("cognome") : null);
        schedaCandidato.setDescrizione(schedaMap.get("descrizione") != null ? schedaMap.get("descrizione") : null);
        schedaCandidato.setDurata(schedaMap.get("durata") != null ? schedaMap.get("durata") : null);
        schedaCandidato.setDataFatturazione(schedaMap.get("dataFatturazione") != null ? Date.valueOf(schedaMap.get("dataFatturazione")) : null);
        schedaCandidato.setInizioAttivita(schedaMap.get("inizioAttivita") != null ? Date.valueOf(schedaMap.get("inizioAttivita")) : null);
        schedaCandidato.setFineAttivita(schedaMap.get("fineAttivita") != null ? Date.valueOf(schedaMap.get("fineAttivita")) : null);

        Double economics = schedaMap.get("economics") != null ? Double.parseDouble(schedaMap.get("economics")) : null;

        schedaCandidato.setEconomics(economics);

        if (schedaMap.get("fee") != null) {
            Double fee = Double.parseDouble(schedaMap.get("fee"));

            schedaCandidato.setFatturato(economics * fee / 100);
            schedaCandidato.setFee(fee);
        }

        if (schedaMap.get("canoneMensile") != null) {
            Double canoneMensile = Double.parseDouble(schedaMap.get("canoneMensile"));

            schedaCandidato.setFatturato(canoneMensile);
            schedaCandidato.setCanoneMensile(canoneMensile);
        }


        if (schedaMap.get("rate") != null) {
            Double rate           = Double.parseDouble(schedaMap.get("rate"));
            Double giorniLavorati = Double.parseDouble(schedaMap.get("giorniLavorati"));

            schedaCandidato.setGiorniLavorati(giorniLavorati);
            schedaCandidato.setFatturato(giorniLavorati * rate);
            schedaCandidato.setRate(rate);
        }

        if (schedaMap.get("idTerminePagamento") != null) {
            TerminePagamento terminePagamento = new TerminePagamento();
            terminePagamento.setId(Integer.parseInt(schedaMap.get("idTerminePagamento")));

            schedaCandidato.setTerminePagamento(terminePagamento);
        }

    }
}