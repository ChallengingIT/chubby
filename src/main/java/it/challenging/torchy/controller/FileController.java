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
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
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
    private final OpenAiChatClient  chatClient;

    private static final Logger logger        = LoggerFactory.getLogger(FileController.class);
    private static final String NOME_AZIENDA  = "Innotek Srl";
    private static final String LUOGO_AZIENDA = "Piazza San Bernando, 106 – 00185 ROMA (RM)";
    private static final String PI_AZIENDA    = "C.F. e P.I.: 15726311002";
    private static final String REA_AZIENDA   = "REA: VV – 1609855";
    private static final String SYSTEM_MESSAGE = """
            Sei un recruiter che deve condividere le informazioni di un tuo candidato ad un'azienda per proporre un colloquio conoscitivo.
            Per costruire queste informazioni hai bisogno di estrarre le esperienze che ti manderò a seguire in questa modalità: Inizio e fine Attività,
            Job title posizione svolta senza esporre il nome dell'azienda, settore azienda, attività svolta durante l'esperienza lavorativa suddivisa in tre massimo
            cinque punti con numero massimo di 80 caratteri, stack tecnologico utilizzato. Potresti estrarre queste informazioni da questo testo senza mettermi stringhe introduttive?
            """;
    private static final String SYSTEM_MESSAGE_LINGUE = """
            Sei un recruiter che deve condividere le informazioni di un tuo candidato ad un'azienda per proporre un colloquio conoscitivo.
            Per costruire queste informazioni hai bisogno di estrarre le lingue conosciute dal candidato che ti manderò a seguire in questa modalità: Lingue conosciute in un elenco
            puntato con numero massimo di 80 caratteri. Potresti estrarre queste informazioni da questo testo senza mettermi stringhe introduttive?
            """;

    private static final String SYSTEM_MESSAGE_BACKGROUND = """
            Sei un recruiter che deve condividere le informazioni di un tuo candidato ad un'azienda per proporre un colloquio conoscitivo.
            Per costruire queste informazioni hai bisogno di estrarre l'istruzione, la formazione e i corsi effettuati: Prima eventuali studi accademici in ordine decrescente
            con titolo, data, corsi rilevanti e voto finale, poi i vari corsi effettuati con titolo ed un piccolo riassunto. Il tutto con numero massimo di 80 caratteri
            per riga. Potresti estrarre queste informazioni da questo testo senza mettermi stringhe introduttive?
            """;

    public FileController(OpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

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

    @GetMapping("/download/cf/{id}")
    public void downloadCF(
            @PathVariable("id") Integer id,
            HttpServletResponse resp
    ) throws IOException {

        Candidato candidato       = candidatoRepository.findById(id).get();
        String nome               = candidato.getNome();
        String siglaCognome       = candidato.getCognome().substring(0,1);
        String nomeCompleto       = nome+" "+siglaCognome+".";
        Calendar cal              = Calendar.getInstance();
        AssistantMessage risposta = null;
        AssistantMessage rispostaLingua = null;
        AssistantMessage rispostaBackground = null;
        String           rispostaModificata = null;

        cal.setTime(candidato.getDataNascita());

        Integer annoNascita     = cal.get(Calendar.YEAR);
        var systemMessage = new SystemMessage(SYSTEM_MESSAGE);
        var systemMessageLingue = new SystemMessage(SYSTEM_MESSAGE_LINGUE);
        var systemMessageBackground = new SystemMessage(SYSTEM_MESSAGE_BACKGROUND);

        if (null != candidato.getFiles() && !candidato.getFiles().isEmpty()) {
            byte[] pdf = candidato.getFiles().get(0).getData();

            InputStream is = new ByteArrayInputStream(pdf);
            java.io.File f = new java.io.File(Objects.requireNonNull(FileController.class.getResource("/static/files/pdf.pdf")).getPath());
            FileUtils.copyInputStreamToFile(is, f);
            var userMessage = getUserMessage(f);

            ChatResponse chatResponseLingue = chatClient.call(new Prompt(List.of(systemMessageLingue, userMessage)));
            ChatResponse chatResponseBackground = chatClient.call(new Prompt(List.of(systemMessageBackground, userMessage)));
            ChatResponse chatResponse = chatClient.call(new Prompt(List.of(systemMessage, userMessage)));

            rispostaLingua = chatResponseLingue.getResults().get(0).getOutput();
            rispostaBackground = chatResponseBackground.getResults().get(0).getOutput();
            risposta = chatResponse.getResults().getFirst().getOutput();

            if (null != risposta) {

                rispostaModificata = risposta.getContent()
                        .replace("Inizio Esperienze Lavorative","")
                        .replace("Fine Esperienze Lavorative", "")
                        .replace("*", "")
                        .replace("\r", "")
                        .replace("\t", "    ")
                        .replaceAll("[0-9]\\.","•");
            }
        }

        ByteArrayOutputStream byteArrayOutputStream =
                createPDF(
                        nomeCompleto.toUpperCase(),
                        candidato.getTipologia().getDescrizione().toUpperCase(),
                        annoNascita,
                        candidato.getLivelloScolastico().getDescrizione(),
                        candidato.getCitta(),
                        candidato.getAnniEsperienza(),
                        candidato.getSkills(),
                        rispostaModificata,
                        rispostaLingua != null ? rispostaLingua.getContent() : null,
                        rispostaBackground != null ? rispostaBackground.getContent() : null);

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

    @GetMapping("/descrizione/cf/{id}")
    public String descrizioneCF(
            @PathVariable("id") Integer id
    ) throws IOException {

        Candidato        candidato          = candidatoRepository.findById(id).get();
        var              systemMessage      = new SystemMessage(SYSTEM_MESSAGE);
        AssistantMessage risposta           = null;
        String           rispostaModificata = null;

        if (null != candidato.getFiles() && !candidato.getFiles().isEmpty()) {
            if(null != candidato.getFiles().get(0)) {
                if(candidato.getFiles().get(0).getTipologia().getId() == 1) {
                    byte[] pdf = candidato.getFiles().get(0).getData();

                    InputStream is = new ByteArrayInputStream(pdf);
                    java.io.File f = new java.io.File(Objects.requireNonNull(FileController.class.getResource("/static/files/pdf.pdf")).getPath());
                    FileUtils.copyInputStreamToFile(is, f);
                    var userMessage = getUserMessage(f);

                    ChatResponse chatResponse = chatClient.call(new Prompt(List.of(systemMessage, userMessage)));

                    risposta = chatResponse.getResults().getFirst().getOutput();

                    if (null != risposta) {

                        rispostaModificata = risposta.getContent()
                                .replace("Inizio Esperienze Lavorative","")
                                .replace("Fine Esperienze Lavorative", "")
                                .replace("*", "")
                                .replace("\r", "")
                                .replace("\t", "    ")
                                .replaceAll("[0-9]\\.","•");
                    }
                }
            }
        }

        return rispostaModificata;
    }

    private static @NotNull UserMessage getUserMessage(java.io.File f) throws IOException {
        String parsedText;
        RandomAccessReadBuffer rar = new RandomAccessReadBuffer(new FileInputStream(f));
        PDFParser parser = new PDFParser(rar);
        COSDocument cosDoc = parser.parse().getDocument();
        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cosDoc);
        parsedText = pdfStripper.getText(pdDoc);

        var userMessage = new UserMessage(parsedText);
        return userMessage;
    }

    public static ByteArrayOutputStream createPDF(
            String nomeCompleto,
            String tipologia,
            Integer annoNascita,
            String livello,
            String domicilio,
            Double anniEsperienza,
            Set<Skill> skills,
            String rispostaOpenAI,
            String rispostaLinguaOpenAI,
            String rispostaBackgroundOpenAI) throws IOException {
        //PDFont font = new PDType0Font(Standard14Fonts.FontName.RHELVETICA);

        PDPageContentStream contentStream;
        PDPageContentStream contentStreamPage2;
        PDPageContentStream contentStreamPage3;
        PDPageContentStream contentStreamPage4;
        ByteArrayOutputStream output =new ByteArrayOutputStream();
        PDDocument document =new PDDocument();
        PDFont font = PDType0Font.load(document, new java.io.File(Objects.requireNonNull(FileController.class.getResource("/static/fonts/Roboto-Regular.ttf")).getPath()));
        PDPage page = new PDPage();
        PDPage page2 = new PDPage();
        PDPage page3 = new PDPage();
        PDPage page4 = new PDPage();

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
        document.addPage(page3);

        PDImageXObject pdImage = PDImageXObject.createFromFile(
                Objects.requireNonNull(FileController.class.getResource("/static/images/innotekCF.jpg")).getPath(),
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

        //contentStream.drawLine(30,centroY,page.getMediaBox().getWidth()-30,centroY+1);
        contentStream.moveTo(30, centroY);
        contentStream.lineTo(page.getMediaBox().getWidth()-30, centroY+1);
        contentStream.stroke();

        contentStream.close();

        contentStreamPage2 = new PDPageContentStream(document, page2);
        contentStreamPage2.drawImage(pdImage, 110, 675);

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, fontSize);
        contentStreamPage2.newLineAtOffset(centroX, 620);
        contentStreamPage2.showText("Summary");
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, fontSize);
        contentStreamPage2.newLineAtOffset(centroX+10, 450);
        contentStreamPage2.showText("Skills");
        contentStreamPage2.endText();

        //contentStreamPage2.drawLine(20,610,page.getMediaBox().getWidth()-90,609);
        //contentStreamPage2.drawLine(20,440,page.getMediaBox().getWidth()-90,439);
        contentStreamPage2.moveTo(30, 610);
        contentStreamPage2.lineTo(page.getMediaBox().getWidth()-30, 609);
        contentStreamPage2.stroke();
        contentStreamPage2.moveTo(30, 440);
        contentStreamPage2.lineTo(page.getMediaBox().getWidth()-30, 439);
        contentStreamPage2.stroke();

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
        if (!skillString.isEmpty()) {
            contentStreamPage2.showText(skillString.substring(0, skillString.length() - 2));
        }
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
        contentStreamPage2.setLeading(18.5f);

        if (null != rispostaLinguaOpenAI) {
            String[] rowsLingue = rispostaLinguaOpenAI.split("\n");
            contentStreamPage2.newLine();

            for (String row : rowsLingue) {

                row = row.replace("\r", "");

                contentStreamPage2.showText("   " + row);
                contentStreamPage2.newLine();
            }
        }

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


        contentStreamPage3 = new PDPageContentStream(document, page3);
        contentStreamPage3.drawImage(pdImage, 110, 675);

        contentStreamPage3.beginText();
        contentStreamPage3.setFont(font, fontSize);
        contentStreamPage3.newLineAtOffset(centroX-60, 620);
        contentStreamPage3.showText("Professional Experiences");
        contentStreamPage3.endText();

        //contentStreamPage3.drawLine(20,610,page.getMediaBox().getWidth()-90,609);
        contentStreamPage3.moveTo(30, 610);
        contentStreamPage3.lineTo(page.getMediaBox().getWidth()-30, 609);
        contentStreamPage3.stroke();

        contentStreamPage3.beginText();
        contentStreamPage3.setFont(font, 14);
        contentStreamPage3.newLineAtOffset(offsetX, 595);
        contentStreamPage3.setLeading(18.5f);

        contentStreamPage4 = new PDPageContentStream(document, page4);
        contentStreamPage4.drawImage(pdImage, 110, 675);
        contentStreamPage4.beginText();
        contentStreamPage4.setFont(font, 14);
        contentStreamPage4.newLineAtOffset(offsetX, 620);
        contentStreamPage4.setLeading(18.5f);

        int countRows = 0;
        boolean quartaPagina = false;
        if (null != rispostaOpenAI) {
            String[] rows = rispostaOpenAI.split("\n");

            for (String row : rows) {
                countRows++;
                if(countRows > 26 && !quartaPagina) {
                    quartaPagina = true;

                    document.addPage(page4);

                    contentStreamPage4.showText(row);
                    contentStreamPage4.newLine();
                } else if (countRows > 26){
                    contentStreamPage4.showText(row);
                    contentStreamPage4.newLine();
                } else {
                    contentStreamPage3.showText(row);
                    contentStreamPage3.newLine();
                }
            }
        }
        contentStreamPage3.endText();

        contentStreamPage3.beginText();
        contentStreamPage3.setFont(font, fontSizeFooter);
        contentStreamPage3.newLineAtOffset(centroAziendaX, 39);
        contentStreamPage3.showText(NOME_AZIENDA);
        contentStreamPage3.endText();

        contentStreamPage3.beginText();
        contentStreamPage3.setFont(font, fontSizeFooter);
        contentStreamPage3.newLineAtOffset(centroLuogoX, 27);
        contentStreamPage3.showText(LUOGO_AZIENDA);
        contentStreamPage3.endText();

        contentStreamPage3.beginText();
        contentStreamPage3.setFont(font, fontSizeFooter);
        contentStreamPage3.newLineAtOffset(centroPIX, 15);
        contentStreamPage3.showText(PI_AZIENDA);
        contentStreamPage3.endText();

        contentStreamPage3.beginText();
        contentStreamPage3.setFont(font, fontSizeFooter);
        contentStreamPage3.newLineAtOffset(centroREAX, 3 );
        contentStreamPage3.showText(REA_AZIENDA);
        contentStreamPage3.endText();

        contentStreamPage3.close();

        if(quartaPagina){
            contentStreamPage4.endText();

            contentStreamPage4.beginText();
            contentStreamPage4.setFont(font, fontSizeFooter);
            contentStreamPage4.newLineAtOffset(centroAziendaX, 39);
            contentStreamPage4.showText(NOME_AZIENDA);
            contentStreamPage4.endText();

            contentStreamPage4.beginText();
            contentStreamPage4.setFont(font, fontSizeFooter);
            contentStreamPage4.newLineAtOffset(centroLuogoX, 27);
            contentStreamPage4.showText(LUOGO_AZIENDA);
            contentStreamPage4.endText();

            contentStreamPage4.beginText();
            contentStreamPage4.setFont(font, fontSizeFooter);
            contentStreamPage4.newLineAtOffset(centroPIX, 15);
            contentStreamPage4.showText(PI_AZIENDA);
            contentStreamPage4.endText();

            contentStreamPage4.beginText();
            contentStreamPage4.setFont(font, fontSizeFooter);
            contentStreamPage4.newLineAtOffset(centroREAX, 3 );
            contentStreamPage4.showText(REA_AZIENDA);
            contentStreamPage4.endText();

            contentStreamPage4.close();
        }

        document.save(output);
        document.close();
        return output;
    }

    public static float centraScritta(PDPage page,PDFont font, int fontSize, String stringa) throws IOException {

        float width = font.getStringWidth(stringa) / 1000 * fontSize;

        return (page.getMediaBox().getWidth() - width) / 2;
    }
}
