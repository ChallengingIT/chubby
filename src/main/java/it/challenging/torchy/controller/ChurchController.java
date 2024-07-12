package it.challenging.torchy.controller;

import it.challenging.torchy.entity.Chiesa;
import it.challenging.torchy.repository.ChurchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/church")
public class ChurchController {

    @Autowired
    private ChurchRepository churchRepository;

    private static final Logger logger = LoggerFactory.getLogger(ChurchController.class);

    @GetMapping("/{username}")
    public List<Chiesa> getAllByUserId(
        @PathVariable("username") String username
    ) {
        logger.info("Lista chiese tramite utenza");
        return churchRepository.findByUsername(username);
    }

    @PostMapping("/save")
    public String save(
        @RequestParam("username") String username,
        @RequestParam("chiesa") Integer idChiesa
    ) {
        logger.info("Save church");

        try {
            churchRepository.updateOttenutaByIdChiesaAndUsername((byte)1, idChiesa, username);

        } catch (Exception e) {
            return "KO";
        }

        return "OK";
    }

}