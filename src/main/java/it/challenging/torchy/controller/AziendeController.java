package it.challenging.torchy.controller;

import it.challenging.torchy.entity.*;
import it.challenging.torchy.repository.*;
import jakarta.annotation.Nullable;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/aziende")
public class AziendeController {

    @Autowired
    private ProvinceRepository          provinceRepository;
    @Autowired
    private ClienteRepository           clienteRepository;
    @Autowired
    private NeedRepository              needRepository;
    @Autowired
    private OwnerRepository             ownerRepository;
    @Autowired
    private TipologiaRepository         tipologiaRepository;
    @Autowired
    private KeyPeopleRepository         keyPeopleRepository;
    @Autowired
    private AssociazioniRepository      associazioniRepository;

    private static final Logger logger = LoggerFactory.getLogger(AziendeController.class);

    @GetMapping("/react")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Cliente> getAll() {
        logger.info("Lista aziende");
        return clienteRepository.findAll();
    }

    @GetMapping("/react/select")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    //@PreAuthorize("hasRole(@roles.ADMIN)")
    public List<ClienteSelect> getAllSelect() {
        logger.info("Lista aziende");

        List<Cliente> aziende = clienteRepository.findAllByOrderByDenominazioneAsc();
        List<ClienteSelect> aziendeModificate = new ArrayList<>();

        for (Cliente azienda : aziende) {
            ClienteSelect aziendaModificata = new ClienteSelect();

            aziendaModificata.setDenominazione(azienda.getDenominazione());
            aziendaModificata.setId(azienda.getId());

            aziendeModificate.add(aziendaModificata);
        }

        return aziendeModificate;
    }

    @GetMapping("/react/mod")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    //@PreAuthorize("hasRole(@roles.ADMIN)")
    public List<ClienteModificato> getAllMod(
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista aziende");

        Pageable                p                 = PageRequest.of(pagina, quantita);
        List<Cliente>           aziende           = clienteRepository.findAllByOrderByDenominazioneAsc(p).getContent();
        List<ClienteModificato> aziendeModificate = new ArrayList<>();

        for (Cliente azienda : aziende) {
            ClienteModificato aziendaModificata = new ClienteModificato();

            aziendaModificata.setDenominazione(azienda.getDenominazione());
            aziendaModificata.setEmail(azienda.getEmail());
            aziendaModificata.setId(azienda.getId());
            aziendaModificata.setPi(azienda.getPi());
            aziendaModificata.setNote(azienda.getNote());
            aziendaModificata.setCf(azienda.getCf());
            aziendaModificata.setCap(azienda.getCap());
            aziendaModificata.setCitta(azienda.getCitta());
            aziendaModificata.setProvincia(azienda.getProvincia());
            aziendaModificata.setPec(azienda.getPec());
            aziendaModificata.setSito(azienda.getSito());
            aziendaModificata.setStatus(azienda.getStatus());
            aziendaModificata.setOwner(azienda.getOwner());
            aziendaModificata.setTipologia(azienda.getTipologia());
            aziendaModificata.setTipologia(azienda.getTipologia());
            aziendaModificata.setSedeOperativa(azienda.getSedeOperativa());
            aziendaModificata.setSettoreMercato(azienda.getSettoreMercato());
            aziendaModificata.setLogo(azienda.getLogo());

            aziendeModificate.add(aziendaModificata);
        }

        return aziendeModificate;
    }

    @GetMapping("/react/ricerca/mod")
    public List<ClienteModificato> getAllModSearch(
        @RequestParam("stato") @Nullable Integer stato,
        @RequestParam("ragione") @Nullable String ragSociale,
        @RequestParam("owner") @Nullable Integer owner,
        @RequestParam("tipologia") @Nullable String tipologia,
        @RequestParam("pagina") Integer pagina,
        @RequestParam("quantita") Integer quantita
    ) {
        logger.info("Lista aziende");

        Pageable                p                 = PageRequest.of(pagina, quantita);
        List<Cliente>           aziende           = clienteRepository.ricercaByStatusAndOwner_IdAndTipologiaAndDenominazione(stato, owner, tipologia, ragSociale, p).getContent();
        List<ClienteModificato> aziendeModificate = new ArrayList<>();

        for (Cliente azienda : aziende) {
            ClienteModificato aziendaModificata = new ClienteModificato();

            aziendaModificata.setDenominazione(azienda.getDenominazione());
            aziendaModificata.setEmail(azienda.getEmail());
            aziendaModificata.setId(azienda.getId());
            aziendaModificata.setPi(azienda.getPi());
            aziendaModificata.setNote(azienda.getNote());
            aziendaModificata.setCf(azienda.getCf());
            aziendaModificata.setCap(azienda.getCap());
            aziendaModificata.setCitta(azienda.getCitta());
            aziendaModificata.setProvincia(azienda.getProvincia());
            aziendaModificata.setPec(azienda.getPec());
            aziendaModificata.setSito(azienda.getSito());
            aziendaModificata.setStatus(azienda.getStatus());
            aziendaModificata.setOwner(azienda.getOwner());
            aziendaModificata.setTipologia(azienda.getTipologia());
            aziendaModificata.setSedeOperativa(azienda.getSedeOperativa());
            aziendaModificata.setSettoreMercato(azienda.getSettoreMercato());
            aziendaModificata.setLogo(azienda.getLogo());

            aziendeModificate.add(aziendaModificata);
        }

        return aziendeModificate;
    }

    @GetMapping("/react/{id}")
    ////@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public Cliente getAzienda(
        @PathVariable("id") Integer id) {
        logger.info("Azienda tramite id");

        return clienteRepository.findById(id).get();
    }

    @DeleteMapping("/react/elimina/{id}")
    ////@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String eliminaCliente(
        @PathVariable("id") Integer id
    ){
        logger.info("Elimina azienda tramite id");

        try {

            List<Need>               needs     = needRepository.findByCliente_Id(id);
            List<KeyPeople>          keyPeople = keyPeopleRepository.findByCliente_Id(id);

            for(Need need : needs) {
                List<AssociazioneCandidatoNeed> associazioni = associazioniRepository.findByNeed_Id(id);

                for(AssociazioneCandidatoNeed associazione : associazioni) {
                    associazioniRepository.deleteById(associazione.getId());

                    logger.debug("Associazione " + associazione.getId() + " eliminata");
                }

                needRepository.deleteById(need.getId());

                logger.debug("Need " + need.getId() + " eliminato");
            }

            for(KeyPeople key : keyPeople) {
                keyPeopleRepository.deleteById(key.getId());

                logger.debug("KeyPeople " + key.getId() + " eliminato");
            }

            clienteRepository.deleteById(id);

            logger.debug("Azienda " + id + " eliminata");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERROR";
        }
    }

    @GetMapping("/react/tipologia")
    ////@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Tipologia> getAllTipologie() {
        logger.info("Job Title / Tipologie");

        return tipologiaRepository.findAllByOrderByDescrizioneAsc();
    }

    @GetMapping("/react/owner")
    ////@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Owner> getAllOwner() {
        logger.info("Owner");

        return ownerRepository.findAll();
    }

    @GetMapping("/react/keypeople/{id}")
    ////@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<KeyPeople> getAllKeyPeopleByIdCLiente(
        @PathVariable("id") Integer idCliente
    ) {
        logger.info("KeyPeople tramite id azienda");

        return keyPeopleRepository.findByCliente_Id(idCliente);
    }


    @GetMapping("/react/owner/{id}")
    ////@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public Owner getOwnerById(
        @PathVariable("id") Integer id
    ) {
        logger.info("Owner tramite id");

        return ownerRepository.findById(id).get();
    }

    @GetMapping("/react/province")
    ////@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public List<Province> getAllProvince() {
        logger.info("Province");

        return provinceRepository.findAll();
    }

    @PostMapping("/react/salva")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public ResponseEntity<String> saveAzienda(
        @RequestBody Map<String, String>  clienteMap,
        @RequestParam("logo") @Nullable File logo
    ){
        logger.info("Salva azienda");

        try {
            Cliente clienteEntity = new Cliente();

            if(clienteMap.get("id") != null) {
                clienteEntity = clienteRepository.findById(Integer.parseInt(clienteMap.get("id"))).get();

                logger.debug("Azienda trovata si procede in modifica");

            }

            byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(logo));
            String logoBase64 =  new String(encoded, StandardCharsets.UTF_8);

            trasformaMappaInCLiente(clienteEntity, clienteMap, logoBase64);

            if (controllaDuplicati(clienteEntity)) {
                logger.debug("Azienda duplicata, denominazione già presente");

                return ResponseEntity.ok("DUPLICATO");
            }

            clienteRepository.save(clienteEntity);

            logger.debug("Azienda salvata correttamente");

            return ResponseEntity.ok("OK");

        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return ResponseEntity.ok("ERRORE");
        }
    }

    private boolean controllaDuplicati(
        Cliente cliente
    ){
        logger.info("Azienda - controlla duplicati");

        boolean           toReturn       = false;
        Optional<Cliente> clienteToCheck = cliente.getId() != null ? clienteRepository.findById(cliente.getId()) : Optional.empty();

        List<Cliente> clientiByDenominazione = clienteRepository.findByDenominazione(cliente.getDenominazione());
        List<Cliente> clientiByEmail = clienteRepository.findByEmail(cliente.getEmail());

        if ((null != clientiByDenominazione && !clientiByDenominazione.isEmpty()) && clienteToCheck.isEmpty()) {
          toReturn = true;
        }

        if (((null != cliente.getEmail()) && !cliente.getEmail().isEmpty()) &&
            ((null != clientiByEmail) && !clientiByEmail.isEmpty()) &&
            clienteToCheck.isEmpty()
        ) {
          toReturn = true;
        }

        return toReturn;
    }

    public void trasformaMappaInCLiente(Cliente cliente, Map<String,String> clienteMap, String logo) {

        logger.info("Trasforma mappa in azienda");

        cliente.setAzioniCommerciali(clienteMap.get("azioniCommerciali") != null ? clienteMap.get("azioniCommerciali") : null);
        cliente.setCap(clienteMap.get("cap") != null ? clienteMap.get("cap") : null);
        cliente.setCf(clienteMap.get("cf") != null ? clienteMap.get("cf") : null);
        cliente.setCitta(clienteMap.get("citta") != null ? clienteMap.get("citta") : null);
        cliente.setCodiceDestinatario(clienteMap.get("codiceDestinatario") != null ? clienteMap.get("codiceDestinatario") : null);
        cliente.setCodicePa(clienteMap.get("codicePa") != null ? clienteMap.get("codicePa") : null);
        cliente.setComunicazioniNeed(clienteMap.get("comunicazioniNeed") != null ? clienteMap.get("comunicazioniNeed") : null);
        cliente.setDenominazione(clienteMap.get("denominazione") != null ? clienteMap.get("denominazione") : null);
        cliente.setEmail(clienteMap.get("email") != null ? clienteMap.get("email") : null);
        cliente.setIndirizzo(clienteMap.get("indirizzo") != null ? clienteMap.get("indirizzo") : null);
        cliente.setNote(clienteMap.get("note") != null ? clienteMap.get("note") : null);
        cliente.setNoteTrattative(clienteMap.get("noteTrattative") != null ? clienteMap.get("noteTrattative") : null);

        if (clienteMap.get("idOwner") != null) {
            Owner owner = new Owner();
            owner.setId(Integer.parseInt(clienteMap.get("idOwner")));

            cliente.setOwner(owner);
        }

        cliente.setPaese(clienteMap.get("paese") != null ? clienteMap.get("paese") : null);
        cliente.setPec(clienteMap.get("pec") != null ? clienteMap.get("pec") : null);
        cliente.setPi(clienteMap.get("pi") != null ? clienteMap.get("pi") : null);
        cliente.setProvincia(clienteMap.get("provincia") != null ? clienteMap.get("provincia") : null);
        cliente.setSedeLegale(clienteMap.get("sedeLegale") != null ? clienteMap.get("sedeLegale") : null);
        cliente.setSedeOperativa(clienteMap.get("sedeOperativa") != null ? clienteMap.get("sedeOperativa") : null);
        cliente.setSettoreMercato(clienteMap.get("settoreMercato") != null ? clienteMap.get("settoreMercato") : null);
        cliente.setSito(clienteMap.get("sito") != null ? clienteMap.get("sito") : null);
        cliente.setStatus(clienteMap.get("status") != null ? Integer.parseInt(clienteMap.get("status")) : null);
        cliente.setTipologia(clienteMap.get("tipologia") != null ? clienteMap.get("tipologia") : null);
        cliente.setLogo(logo);

    }
}