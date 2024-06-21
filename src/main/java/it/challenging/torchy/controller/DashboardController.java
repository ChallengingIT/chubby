package it.challenging.torchy.controller;

import it.challenging.torchy.entity.*;
import it.challenging.torchy.repository.*;
import it.challenging.torchy.util.Constants;
import it.challenging.torchy.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private NeedRepository       needRepository;
    @Autowired
    private CandidatoRepository  candidatoRepository;
    @Autowired
    private StatoCRepository     statoCRepository;
    @Autowired
    private IntervistaRepository intervistaRepository;
    @Autowired
    private KeyPeopleRepository  keyPeopleRepository;

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @GetMapping("/pipeline")
    public List<NeedPipeline> getAllNeedPipeline(
            @RequestParam("username") String username,
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista pipeline need");
        try  {
            Pageable             p               = PageRequest.of(pagina, quantita);
            Page<Need>           pageableNeed    = needRepository.ricercaByUsername(username, p);
            List<Need>           needs           = pageableNeed.getContent();
            List<NeedPipeline>   needsModificati = new ArrayList<>();

            for (Need need : needs) {
                NeedPipeline needSolo = new NeedPipeline();

                needSolo.setId(need.getId());
                needSolo.setDescrizione(need.getDescrizione());

                Cliente cliente = new Cliente();

                cliente.setId(need.getCliente().getId());
                cliente.setDenominazione(need.getCliente().getDenominazione());
                //cliente.setLogo(need.getCliente().getLogo());

                needSolo.setCliente(cliente);
                needSolo.setPriorita(need.getPriorita());
                needSolo.setOwner(need.getOwner());
                needSolo.setStato(need.getStato());

                needSolo.setPipeline(creaPipeline(need.getId()));

                needsModificati.add(needSolo);

            }

            return needsModificati;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/pipeline/admin")
    public List<NeedPipeline> getAllNeedPipeline(
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista pipeline need");
        try  {
            Pageable             p               = PageRequest.of(pagina, quantita);
            Page<Need>           pageableNeed    = needRepository.ricercaOrdinata(p);
            List<Need>           needs           = pageableNeed.getContent();
            List<NeedPipeline>   needsModificati = new ArrayList<>();

            for (Need need : needs) {
                NeedPipeline needSolo = new NeedPipeline();

                needSolo.setId(need.getId());
                needSolo.setDescrizione(need.getDescrizione());

                Cliente cliente = new Cliente();

                cliente.setId(need.getCliente().getId());
                cliente.setDenominazione(need.getCliente().getDenominazione());
                //cliente.setLogo(need.getCliente().getLogo());

                needSolo.setCliente(cliente);
                needSolo.setPriorita(need.getPriorita());
                needSolo.setOwner(need.getOwner());
                needSolo.setStato(need.getStato());

                needSolo.setPipeline(creaPipeline(need.getId()));

                needsModificati.add(needSolo);

            }

            return needsModificati;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/attivita/recruting/personal")
    public List<AttivitaRecruiting> getAttivitaRecruitingPersonal(
            @RequestParam("username") String username,
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista attivita recruiting personal");
        try  {
            Pageable                 p                  = PageRequest.of(pagina, quantita);
            Page<Intervista>         pageableIntervista = intervistaRepository.ricercaAttivitaByUsername(username, p);
            List<Intervista>         interviste         = pageableIntervista.getContent();
            List<AttivitaRecruiting> attivitaRecruiting = new ArrayList<>();

            for (Intervista intervista : interviste) {
                AttivitaRecruiting attivita = new AttivitaRecruiting();

                Candidato candidato = intervista.getCandidato();
                Owner     owner     = intervista.getNextOwner();

                attivita.setIdCandidato(candidato.getId());
                attivita.setNomeCandidato(candidato.getNome());
                attivita.setCognomeCandidato(candidato.getCognome());
                attivita.setIdOwner(owner.getId());
                attivita.setSiglaOwner(owner.getDescrizione());
                attivita.setIdIntervista(intervista.getId());
                attivita.setAzione(intervista.getTipo() != null ? intervista.getTipo().getDescrizione() : null);
                attivita.setIdAzione(intervista.getTipo() != null ? intervista.getTipo().getId() : null);
                attivita.setData(intervista.getDataAggiornamento());

                attivitaRecruiting.add(attivita);

            }

            return attivitaRecruiting;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/attivita/recruting")
    public List<AttivitaRecruiting> getAttivitaRecruiting(
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista attivita recruiting");
        try  {
            Pageable                 p                  = PageRequest.of(pagina, quantita);
            Page<Intervista>         pageableIntervista = intervistaRepository.ricercaAttivita(p);
            List<Intervista>         interviste         = pageableIntervista.getContent();
            List<AttivitaRecruiting> attivitaRecruiting = new ArrayList<>();

            for (Intervista intervista : interviste) {
                AttivitaRecruiting attivita = new AttivitaRecruiting();

                Candidato candidato = intervista.getCandidato();
                Owner     owner     = intervista.getNextOwner();

                attivita.setIdCandidato(candidato.getId());
                attivita.setNomeCandidato(candidato.getNome());
                attivita.setCognomeCandidato(candidato.getCognome());
                attivita.setIdOwner(owner.getId());
                attivita.setSiglaOwner(owner.getDescrizione());
                attivita.setIdIntervista(intervista.getId());
                attivita.setAzione(intervista.getTipo() != null ? intervista.getTipo().getDescrizione() : null);
                attivita.setIdAzione(intervista.getTipo() != null ? intervista.getTipo().getId() : null);
                attivita.setData(intervista.getDataAggiornamento());

                attivitaRecruiting.add(attivita);

            }

            return attivitaRecruiting;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/attivita/recruting/personal/interval")
    public List<AttivitaRecruiting> getAttivitaRecruitingPersonal(
            @RequestParam("username") String username,
            @RequestParam("interval") Integer interval,
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista attivita recruiting personal");
        try  {
            Pageable                 p                  = PageRequest.of(pagina, quantita);
            Page<Intervista>         pageableIntervista = intervistaRepository.ricercaAttivitaByUsernameInterval(username, interval, p);
            List<Intervista>         interviste         = pageableIntervista.getContent();
            List<AttivitaRecruiting> attivitaRecruiting = new ArrayList<>();

            for (Intervista intervista : interviste) {
                AttivitaRecruiting attivita = new AttivitaRecruiting();

                Candidato candidato = intervista.getCandidato();
                Owner     owner     = intervista.getNextOwner();

                attivita.setIdCandidato(candidato.getId());
                attivita.setNomeCandidato(candidato.getNome());
                attivita.setCognomeCandidato(candidato.getCognome());
                attivita.setIdOwner(owner.getId());
                attivita.setSiglaOwner(owner.getDescrizione());
                attivita.setIdIntervista(intervista.getId());
                attivita.setAzione(intervista.getTipo() != null ? intervista.getTipo().getDescrizione() : null);
                attivita.setIdAzione(intervista.getTipo() != null ? intervista.getTipo().getId() : null);
                attivita.setData(intervista.getDataAggiornamento());

                attivitaRecruiting.add(attivita);

            }

            return attivitaRecruiting;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/attivita/recruting/interval")
    public List<AttivitaRecruiting> getAttivitaRecruitingInterval(
            @RequestParam("interval") Integer interval,
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista attivita recruiting");
        try  {
            Pageable                 p                  = PageRequest.of(pagina, quantita);
            Page<Intervista>         pageableIntervista = intervistaRepository.ricercaAttivitaInterval(interval, p);
            List<Intervista>         interviste         = pageableIntervista.getContent();
            List<AttivitaRecruiting> attivitaRecruiting = new ArrayList<>();

            for (Intervista intervista : interviste) {
                AttivitaRecruiting attivita = new AttivitaRecruiting();

                Candidato candidato = intervista.getCandidato();
                Owner     owner     = intervista.getNextOwner();

                attivita.setIdCandidato(candidato.getId());
                attivita.setNomeCandidato(candidato.getNome());
                attivita.setCognomeCandidato(candidato.getCognome());
                attivita.setIdOwner(owner.getId());
                attivita.setSiglaOwner(owner.getDescrizione());
                attivita.setIdIntervista(intervista.getId());
                attivita.setAzione(intervista.getTipo() != null ? intervista.getTipo().getDescrizione() : null);
                attivita.setIdAzione(intervista.getTipo() != null ? intervista.getTipo().getId() : null);
                attivita.setData(intervista.getDataAggiornamento());

                attivitaRecruiting.add(attivita);

            }

            return attivitaRecruiting;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/attivita/business/personal")
    public List<AttivitaBusiness> getAttivitaBusinessPersonal(
            @RequestParam("username") String username,
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista attivita business personal");
        try  {
            Pageable               p                 = PageRequest.of(pagina, quantita);
            Page<KeyPeople>        pageableKeyPeople = keyPeopleRepository.ricercaAzioniByUsername(username, p);
            List<KeyPeople>        keyPeoples        = pageableKeyPeople.getContent();
            List<AttivitaBusiness> attivitaBusiness  = new ArrayList<>();

            for (KeyPeople keyPeople : keyPeoples) {
                for(AzioneKeyPeople azione : keyPeople.getAzioni()) {
                    AttivitaBusiness attivita = new AttivitaBusiness();

                    if (DateUtils.isToday(Date.from(azione.getDataModifica().atZone(ZoneId.systemDefault()).toInstant()))) {
                        Owner owner = keyPeople.getOwner();
                        Cliente cliente = keyPeople.getCliente();

                        attivita.setIdContatto(keyPeople.getId());
                        attivita.setNomeContatto(keyPeople.getNome());
                        attivita.setIdCliente(cliente.getId());
                        attivita.setDescrizioneCliente(cliente.getDenominazione());
                        attivita.setIdOwner(owner.getId());
                        attivita.setSiglaOwner(owner.getDescrizione());
                        attivita.setIdAzioneKeyPeople(azione.getId());
                        attivita.setAzione(azione.getTipologia().getDescrizione());
                        attivita.setIdAzione(azione.getTipologia().getId());
                        attivita.setData(azione.getDataModifica().toLocalDate().atStartOfDay());

                        attivitaBusiness.add(attivita);
                    }
                }
            }
            return attivitaBusiness;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/attivita/business/personal/interval")
    public List<AttivitaBusiness> getAttivitaBusinessPersonalInterval(
            @RequestParam("username") String username,
            @RequestParam("interval") Integer interval,
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista attivita business personal interval");
        try  {
            Pageable               p                 = PageRequest.of(pagina, quantita);
            Page<KeyPeople>        pageableKeyPeople = keyPeopleRepository.ricercaAzioniByUsernameInterval(username, interval, p);
            List<KeyPeople>        keyPeoples        = pageableKeyPeople.getContent();
            List<AttivitaBusiness> attivitaBusiness  = new ArrayList<>();

            for (KeyPeople keyPeople : keyPeoples) {
                for(AzioneKeyPeople azione : keyPeople.getAzioni()) {
                    AttivitaBusiness attivita = new AttivitaBusiness();

                    if (DateUtils.isToday(Date.from(azione.getDataModifica().atZone(ZoneId.systemDefault()).toInstant()))) {
                        Owner owner = keyPeople.getOwner();
                        Cliente cliente = keyPeople.getCliente();

                        attivita.setIdContatto(keyPeople.getId());
                        attivita.setNomeContatto(keyPeople.getNome());
                        attivita.setIdCliente(cliente.getId());
                        attivita.setDescrizioneCliente(cliente.getDenominazione());
                        attivita.setIdOwner(owner.getId());
                        attivita.setSiglaOwner(owner.getDescrizione());
                        attivita.setIdAzioneKeyPeople(azione.getId());
                        attivita.setAzione(azione.getTipologia().getDescrizione());
                        attivita.setIdAzione(azione.getTipologia().getId());
                        attivita.setData(azione.getDataModifica().toLocalDate().atStartOfDay());

                        attivitaBusiness.add(attivita);
                    }
                }
            }
            return attivitaBusiness;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/attivita/business")
    public List<AttivitaBusiness> getAttivitaBusinessPersonal(
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista attivita business");
        try  {
            Pageable               p                 = PageRequest.of(pagina, quantita);
            Page<KeyPeople>        pageableKeyPeople = keyPeopleRepository.ricercaAzioni(p);
            List<KeyPeople>        keyPeoples        = pageableKeyPeople.getContent();
            List<AttivitaBusiness> attivitaBusiness  = new ArrayList<>();

            for (KeyPeople keyPeople : keyPeoples) {
                for(AzioneKeyPeople azione : keyPeople.getAzioni()) {
                    AttivitaBusiness attivita = new AttivitaBusiness();

                    if (DateUtils.isToday(Date.from(azione.getDataModifica().atZone(ZoneId.systemDefault()).toInstant()))) {
                        Owner owner = keyPeople.getOwner();
                        Cliente cliente = keyPeople.getCliente();

                        attivita.setIdContatto(keyPeople.getId());
                        attivita.setNomeContatto(keyPeople.getNome());
                        attivita.setIdCliente(cliente.getId());
                        attivita.setDescrizioneCliente(cliente.getDenominazione());
                        attivita.setIdOwner(owner.getId());
                        attivita.setSiglaOwner(owner.getDescrizione());
                        attivita.setIdAzioneKeyPeople(azione.getId());
                        attivita.setAzione(azione.getTipologia().getDescrizione());
                        attivita.setIdAzione(azione.getTipologia().getId());
                        attivita.setData(azione.getDataModifica().toLocalDate().atStartOfDay());

                        attivitaBusiness.add(attivita);
                    }
                }
            }
            return attivitaBusiness;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    @GetMapping("/attivita/business/interval")
    public List<AttivitaBusiness> getAttivitaBusinessPersonalInterval(
            @RequestParam("interval") Integer interval,
            @RequestParam("pagina") Integer pagina,
            @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista attivita business");
        try  {
            Pageable               p                 = PageRequest.of(pagina, quantita);
            Page<KeyPeople>        pageableKeyPeople = keyPeopleRepository.ricercaAzioniInterval(interval, p);
            List<KeyPeople>        keyPeoples        = pageableKeyPeople.getContent();
            List<AttivitaBusiness> attivitaBusiness  = new ArrayList<>();

            for (KeyPeople keyPeople : keyPeoples) {
                for(AzioneKeyPeople azione : keyPeople.getAzioni()) {
                    AttivitaBusiness attivita = new AttivitaBusiness();

                    if (DateUtils.isToday(Date.from(azione.getDataModifica().atZone(ZoneId.systemDefault()).toInstant()))) {
                        Owner owner = keyPeople.getOwner();
                        Cliente cliente = keyPeople.getCliente();

                        attivita.setIdContatto(keyPeople.getId());
                        attivita.setNomeContatto(keyPeople.getNome());
                        attivita.setIdCliente(cliente.getId());
                        attivita.setDescrizioneCliente(cliente.getDenominazione());
                        attivita.setIdOwner(owner.getId());
                        attivita.setSiglaOwner(owner.getDescrizione());
                        attivita.setIdAzioneKeyPeople(azione.getId());
                        attivita.setAzione(azione.getTipologia().getDescrizione());
                        attivita.setIdAzione(azione.getTipologia().getId());
                        attivita.setData(azione.getDataModifica().toLocalDate().atStartOfDay());

                        attivitaBusiness.add(attivita);
                    }
                }
            }
            return attivitaBusiness;
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    public Pipeline creaPipeline(Integer idNeed) {
        Integer itwPianificate = 0, itwFatte = 0, cfInviati = 0, cfDisponibili= 0,
                qmPianificate = 0, qmFatte = 0, followUpPool = 0, followUpPositivi = 0;

        Pipeline pipeline = new Pipeline();

        List<StatoC> stati = statoCRepository.findAll();

        cfDisponibili = candidatoRepository
                .countCandidatiAssociatiByStato(
                        idNeed,
                        stati
                            .stream()
                            .filter(s -> s.getDescrizione().equalsIgnoreCase(Constants.STATO_CANDIDATO_CF_DISPONIBILE))
                            .findFirst()
                            .get()
                            .getId()
                );

        cfInviati = candidatoRepository
                .countCandidatiAssociatiByStato(
                        idNeed,
                        stati
                                .stream()
                                .filter(s -> s.getDescrizione().equalsIgnoreCase(Constants.STATO_CANDIDATO_CF_INVIATO))
                                .findFirst()
                                .get()
                                .getId()
                );

        qmPianificate = candidatoRepository
                .countCandidatiAssociatiByStato(
                        idNeed,
                        stati
                                .stream()
                                .filter(s -> s.getDescrizione().equalsIgnoreCase(Constants.STATO_CANDIDATO_QM_PIANIFICATA))
                                .findFirst()
                                .get()
                                .getId()
                );

        qmFatte = candidatoRepository
                .countCandidatiAssociatiByStato(
                        idNeed,
                        stati
                                .stream()
                                .filter(s -> s.getDescrizione().equalsIgnoreCase(Constants.STATO_CANDIDATO_QM_FATTA))
                                .findFirst()
                                .get()
                                .getId()
                );

        /*itwPianificate = candidatoRepository
                .countCandidatiAssociatiByStato(
                        idNeed,
                        stati
                                .stream()
                                .filter(s -> s.getDescrizione().equalsIgnoreCase(Constants.STATO_CANDIDATO_ITW_PIANIFICATA))
                                .findFirst()
                                .get()
                                .getId()
                );

        itwFatte = candidatoRepository
                .countCandidatiAssociatiByStato(
                        idNeed,
                        stati
                                .stream()
                                .filter(s -> s.getDescrizione().equalsIgnoreCase(Constants.STATO_CANDIDATO_ITW_FATTA))
                                .findFirst()
                                .get()
                                .getId()
                );*/

        followUpPool = candidatoRepository
                .countCandidatiAssociatiByStato(
                        idNeed,
                        stati
                                .stream()
                                .filter(s -> s.getDescrizione().equalsIgnoreCase(Constants.STATO_CANDIDATO_FOLLOW_UP_POOL))
                                .findFirst()
                                .get()
                                .getId()
                );

        followUpPositivi = candidatoRepository
                .countCandidatiAssociatiByStato(
                        idNeed,
                        stati
                                .stream()
                                .filter(s -> s.getDescrizione().equalsIgnoreCase(Constants.STATO_CANDIDATO_FOLLOW_UP_POSITIVO))
                                .findFirst()
                                .get()
                                .getId()
                );

        pipeline.setCfDisponibili(cfDisponibili);
        pipeline.setCfInviati(cfInviati);
        pipeline.setItwFatte(itwFatte);
        pipeline.setItwPianificate(itwPianificate);
        pipeline.setQmFatte(qmFatte);
        pipeline.setQmPianificate(qmPianificate);
        pipeline.setFollowUpPool(followUpPool);
        pipeline.setFollowUpPositivi(followUpPositivi);

        return pipeline;
    }
}