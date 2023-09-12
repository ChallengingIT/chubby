/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.controller;

import it.innotek.wehub.entity.File;
import it.innotek.wehub.entity.staff.FileStaffId;
import it.innotek.wehub.exception.ElementoNonTrovatoException;
import it.innotek.wehub.service.*;
import jakarta.servlet.http.HttpServletResponse;
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
    private FileService serviceFile;
    @Autowired
    private FileStaffService serviceFileStaff;

    @RequestMapping("/download/file/{id}")
    public void downloadFile(
        @PathVariable Integer id,
        HttpServletResponse resp
    ) throws IOException, ElementoNonTrovatoException {

        File   dbFile    = serviceFile.get(id);
        byte[] byteArray = dbFile.getData();

        resp.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM.getType());
        resp.setHeader("Content-Disposition", "attachment; filename=" + dbFile.getDescrizione());
        resp.setContentLength(byteArray.length);

        try (OutputStream os = resp.getOutputStream()) {
            os.write(byteArray, 0, byteArray.length);
        }
    }

    @RequestMapping("/elimina/file/{idf}/{ids}")
    public String eliminaFile(
        @PathVariable Integer idf,
        @PathVariable Integer ids
    ) throws ElementoNonTrovatoException {

        FileStaffId fileStaffId = new FileStaffId();
        fileStaffId.setIdFile(idf);
        fileStaffId.setIdStaff(ids);

        serviceFileStaff.delete(fileStaffId);

        serviceFile.delete(idf);

        return "redirect:/hr/staff/modifica/"+ids;
    }
}
