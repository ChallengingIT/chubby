/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.controller;

import it.challenging.torchy.entity.Candidato;
import it.challenging.torchy.entity.File;
import it.challenging.torchy.entity.FileCandidatoId;
import it.challenging.torchy.entity.Skill;
import it.challenging.torchy.repository.CandidatoRepository;
import it.challenging.torchy.repository.FileCandidatoRepository;
import it.challenging.torchy.repository.FileRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileRepository          fileRepository;
    @Autowired
    private FileCandidatoRepository fileCandidatoRepository;
    @Autowired
    private CandidatoRepository     candidatoRepository;

    private static final Logger logger        = LoggerFactory.getLogger(FileController.class);
    private static final String NOME_AZIENDA  = "Innotek Srl";
    private static final String LUOGO_AZIENDA = "Piazza San Bernando, 106 – 00185 ROMA (RM)";
    private static final String PI_AZIENDA    = "C.F. e P.I.: 15726311002";
    private static final String REA_AZIENDA   = "REA: VV – 1609855";

    @GetMapping("/react/download/file/{id}")
    //@PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('BM')")
    public void downloadFile(
            @PathVariable("id") Integer id,
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

    @RequestMapping("download/cf/{id}")
    public void downloadCF(@PathVariable Integer id, HttpServletResponse resp) throws IOException {

        Candidato candidato = candidatoRepository.findById(id).get();

        String nome = candidato.getNome();
        String siglaCognome = candidato.getCognome().substring(0,1);
        String nomeCompleto = nome+" "+siglaCognome+".";

        Calendar cal = Calendar.getInstance();
        cal.setTime(candidato.getDataNascita());

        Integer annoNascita = cal.get(Calendar.YEAR);

        ByteArrayOutputStream byteArrayOutputStream = createPDF(nomeCompleto.toUpperCase(), candidato.getTipologia().getDescrizione().toUpperCase(), annoNascita, candidato.getLivelloScolastico().getDescrizione(),candidato.getCitta(), candidato.getAnniEsperienza(), candidato.getSkills());

        resp.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM.getType());
        resp.setHeader("Content-Disposition", "attachment; filename=CF-" + siglaCognome + "." + nome + ".pdf" );
        resp.setContentLength(byteArrayOutputStream.toByteArray().length);

        OutputStream os = resp.getOutputStream();
        try {
            os.write(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.toByteArray().length);
        } finally {
            os.close();
        }

    }

    public static ByteArrayOutputStream createPDF(String nomeCompleto, String tipologia, Integer annoNascita, String livello, String domicilio, Double anniEsperienza, Set<Skill> skills) throws IOException {
        PDFont font = PDType1Font.TIMES_ROMAN;
        PDPageContentStream contentStream;
        PDPageContentStream contentStreamPage2;
        ByteArrayOutputStream output =new ByteArrayOutputStream();
        PDDocument document =new PDDocument();
        PDPage page = new PDPage();
        PDPage page2 = new PDPage();

        int marginTop = 330; // Or whatever margin you want.

        int fontSize = 26; // Or whatever font size you want.
        int fontSizeFooter = 6; // Or whatever font size you want.

        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 15;

        float centroX = centraScritta(page,font, fontSize, nomeCompleto);
        float centroTipologiaX = centraScritta(page,font, fontSize, tipologia);
        float centroAziendaX = centraScritta(page,font, fontSizeFooter, NOME_AZIENDA);
        float centroLuogoX = centraScritta(page,font, fontSizeFooter, LUOGO_AZIENDA);
        float centroPIX = centraScritta(page,font, fontSizeFooter, PI_AZIENDA);
        float centroREAX = centraScritta(page,font, fontSizeFooter, REA_AZIENDA);
        float centroY = (page.getMediaBox().getHeight()/2) - titleHeight;

        document.addPage(page);
        document.addPage(page2);

        PDImageXObject pdImage = PDImageXObject.createFromFile(
                CandidatoController.class.getResource("/static/images/innotekCF.jpg").getPath(),
                document);
        contentStream = new PDPageContentStream(document, page);
        contentStream.drawImage(pdImage, 110, 675);
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(centroX, centroY+7);
        contentStream.showText(nomeCompleto);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(centroTipologiaX, centroY-22);
        contentStream.showText(tipologia);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font, fontSizeFooter);
        contentStream.newLineAtOffset(centroAziendaX, 39);
        contentStream.showText(NOME_AZIENDA);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font, fontSizeFooter);
        contentStream.newLineAtOffset(centroLuogoX, 27);
        contentStream.showText(LUOGO_AZIENDA);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font, fontSizeFooter);
        contentStream.newLineAtOffset(centroPIX, 15);
        contentStream.showText(PI_AZIENDA);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font, fontSizeFooter);
        contentStream.newLineAtOffset(centroREAX, 3 );
        contentStream.showText(REA_AZIENDA);
        contentStream.endText();

        contentStream.drawLine(30,centroY,page.getMediaBox().getWidth()-30,centroY+1);

        contentStream.close();

        contentStreamPage2 = new PDPageContentStream(document, page2);
        contentStreamPage2.drawImage(pdImage, 110, 675);

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, fontSize);
        contentStreamPage2.newLineAtOffset(20, 620);
        contentStreamPage2.showText("Summary");
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, fontSize);
        contentStreamPage2.newLineAtOffset(20, 450);
        contentStreamPage2.showText("Skills");
        contentStreamPage2.endText();

        contentStreamPage2.drawLine(20,610,page.getMediaBox().getWidth()-90,609);
        contentStreamPage2.drawLine(20,440,page.getMediaBox().getWidth()-90,439);


        Integer offsetX = 20;
        Integer offsetY = 420;

        String skillString = "";
        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, 14);
        contentStreamPage2.newLineAtOffset(offsetX, offsetY);
        contentStreamPage2.setLeading(14.5f);

        for(Skill skill : skills) {

            Integer lunghezzaLista = skillString.length();
            Integer lunghezzaSkill = skill.getDescrizione().length();

            if (lunghezzaLista < 95 && lunghezzaLista + lunghezzaSkill < 95) {
                skillString += skill.getDescrizione() + " / ";
            } else {
                contentStreamPage2.showText(skillString);
                contentStreamPage2.newLine();
                skillString = "";
                skillString += skill.getDescrizione() + " / ";
            }
        }

        contentStreamPage2.showText(skillString.substring(0, skillString.length()-2));
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, 14);
        contentStreamPage2.newLineAtOffset(20, 590);
        contentStreamPage2.showText("- Anno di nascita: "+annoNascita);
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, 14);
        contentStreamPage2.newLineAtOffset(20, 570);
        contentStreamPage2.showText("- Domicilio attuale: "+domicilio);
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, 14);
        contentStreamPage2.newLineAtOffset(20, 550);
        contentStreamPage2.showText("- Anni di esperienza nel ruolo: "+anniEsperienza);
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, 14);
        contentStreamPage2.newLineAtOffset(20, 530);
        contentStreamPage2.showText("- Background accademico: "+livello);
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, 14);
        contentStreamPage2.newLineAtOffset(20, 510);
        contentStreamPage2.showText("- Lingue: ");
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, fontSizeFooter);
        contentStreamPage2.newLineAtOffset(centroAziendaX, 39);
        contentStreamPage2.showText(NOME_AZIENDA);
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, fontSizeFooter);
        contentStreamPage2.newLineAtOffset(centroLuogoX, 27);
        contentStreamPage2.showText(LUOGO_AZIENDA);
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, fontSizeFooter);
        contentStreamPage2.newLineAtOffset(centroPIX, 15);
        contentStreamPage2.showText(PI_AZIENDA);
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, fontSizeFooter);
        contentStreamPage2.newLineAtOffset(centroREAX, 3 );
        contentStreamPage2.showText(REA_AZIENDA);
        contentStreamPage2.endText();

        contentStreamPage2.close();

        document.save(output);
        document.close();
        return output;
    }

    public static float centraScritta(PDPage page,PDFont font, int fontSize, String stringa) throws IOException {

        float width = font.getStringWidth(stringa) / 1000 * fontSize;

        return (page.getMediaBox().getWidth() - width) / 2;
    }
}
