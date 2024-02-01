/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.File;
import it.innotek.wehub.entity.FileCandidatoId;
import it.innotek.wehub.entity.staff.FileStaffId;
import it.innotek.wehub.repository.FileCandidatoRepository;
import it.innotek.wehub.repository.FileRepository;
import it.innotek.wehub.repository.FileStaffRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileRepository          fileRepository;
    @Autowired
    private FileStaffRepository     fileStaffRepository;
    @Autowired
    private FileCandidatoRepository fileCandidatoRepository;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @GetMapping("/react/download/file/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public void downloadFile(
        @PathVariable Integer id,
        HttpServletResponse resp
    ) {
        logger.info("Download file");

        try {
            File   dbFile    = fileRepository.findById(id).get();
            byte[] byteArray = dbFile.getData();

            resp.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM.getType());
            resp.setHeader("Content-Disposition", "attachment; filename=" + dbFile.getDescrizione());
            resp.setContentLength(byteArray.length);

            try (OutputStream os = resp.getOutputStream()) {
                os.write(byteArray, 0, byteArray.length);
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage());

        }
    }

    @DeleteMapping("/react/elimina/file/{idf}/{ids}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String eliminaFile(
        @PathVariable Integer idf,
        @PathVariable Integer ids
    ) {
        logger.info("Elimina file");

        try {
            FileStaffId fileStaffId = new FileStaffId();
            fileStaffId.setIdFile(idf);
            fileStaffId.setIdStaff(ids);

            fileStaffRepository.deleteById(fileStaffId);

            logger.debug("Eliminato riferimento del file allo staff");

            fileRepository.deleteById(idf);

            logger.debug("File eliminato correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }

    @DeleteMapping("/react/elimina/file/candidato/{idf}/{idc}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public String eliminaFileCandidato(
        @PathVariable Integer idf,
        @PathVariable Integer idc
    ) {
        logger.info("Elimina file candidato");

        try {
            FileCandidatoId fileCandidatoId = new FileCandidatoId();
            fileCandidatoId.setIdFile(idf);
            fileCandidatoId.setIdCandidato(idc);

            fileCandidatoRepository.deleteById(fileCandidatoId);

            logger.debug("Eliminato riferimento del file al candidato");

            fileRepository.deleteById(idf);

            logger.debug("File eliminato correttamente");

            return "OK";
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "ERRORE";
        }
    }
}
