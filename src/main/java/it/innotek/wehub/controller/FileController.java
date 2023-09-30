/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.File;
import it.innotek.wehub.entity.staff.FileStaffId;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.repository.FileRepository;
import it.innotek.wehub.repository.FileStaffRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileRepository      fileRepository;
    @Autowired
    private FileStaffRepository fileStaffRepository;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @RequestMapping("/download/file/{id}")
    public void downloadFile(
        @PathVariable Integer id,
        HttpServletResponse resp
    ) throws IOException, ElementoNonTrovatoException {
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

    @RequestMapping("/elimina/file/{idf}/{ids}")
    public String eliminaFile(
        @PathVariable Integer idf,
        @PathVariable Integer ids
    ) {
        try {
            FileStaffId fileStaffId = new FileStaffId();
            fileStaffId.setIdFile(idf);
            fileStaffId.setIdStaff(ids);

            fileStaffRepository.deleteById(fileStaffId);

            fileRepository.deleteById(idf);

            return "redirect:/hr/staff/modifica/" + ids;
        } catch (Exception exception) {
            logger.error(exception.getMessage());

            return "error";
        }
    }
}
