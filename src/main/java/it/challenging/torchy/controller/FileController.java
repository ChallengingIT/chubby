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
import org.apache.xmlbeans.XmlException;
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
    private static final String NOME_AZIENDA_INNOTEK  = "Innotek Srl";
    private static final String LUOGO_AZIENDA_INNOTEK = "Piazza San Bernando, 106 – 00185 ROMA (RM)";
    private static final String PI_AZIENDA_INNOTEK   = "C.F. e P.I.: 15726311002";
    private static final String REA_AZIENDA_INNOTEK   = "REA: VV – 1609855";

    private static final String NOME_AZIENDA  = "Challenging Srl";
    private static final String LUOGO_AZIENDA = "Piazza San Bernando, 106 – 00185 ROMA (RM)";
    private static final String PI_AZIENDA    = "C.F. e P.I.: 15938691001";
    private static final String REA_AZIENDA   = "PEC: challenging@legalmail.it";


    private static final String SYSTEM_MESSAGE = """
            Sei un recruiter che deve condividere le informazioni di un tuo candidato ad un'azienda per proporre un colloquio conoscitivo.
            Per costruire queste informazioni hai bisogno di estrarre le esperienze che ti manderò a seguire in questa modalità in ordine decrescente: Inizio e fine Attività,
            Job title posizione svolta senza esporre il nome dell'azienda, settore azienda, attività svolta durante l'esperienza lavorativa suddivisa in tre massimo
            cinque punti con numero massimo di 80 caratteri, stack tecnologico utilizzato. Potresti estrarre queste informazioni da questo testo senza mettermi stringhe introduttive?
            """;
    private static final String SYSTEM_MESSAGE_LINGUE = """
            Sei un recruiter che deve condividere le informazioni di un tuo candidato ad un'azienda per proporre un colloquio conoscitivo.
            Per costruire queste informazioni hai bisogno di estrarre le lingue conosciute dal candidato che ti manderò a seguire in questa modalità: Lingue conosciute in un elenco
            puntato con numero massimo di 75 caratteri. Potresti estrarre queste informazioni da questo testo senza mettermi stringhe introduttive?
            """;

    private static final String SYSTEM_MESSAGE_BACKGROUND = """
            Sei un recruiter che deve condividere le informazioni di un tuo candidato ad un'azienda per proporre un colloquio conoscitivo.
            Per costruire queste informazioni hai bisogno di estrarre l'istruzione, la formazione e i corsi effettuati: Prima eventuali studi accademici in ordine decrescente
            con titolo, data, corsi rilevanti e voto finale, poi i vari corsi effettuati con titolo ed un piccolo riassunto. Il tutto con numero massimo di 75 caratteri
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
            @RequestParam("tipo") Integer tipo,
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
                        tipo,
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
            Integer tipo,
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

        String nomeAzienda = tipo == 1 ? NOME_AZIENDA_INNOTEK : NOME_AZIENDA;
        String luogoAzienda = tipo == 1 ? LUOGO_AZIENDA_INNOTEK : LUOGO_AZIENDA;
        String piAzienda = tipo == 1 ? PI_AZIENDA_INNOTEK : PI_AZIENDA;
        String reaAzienda = tipo == 1 ? REA_AZIENDA_INNOTEK : REA_AZIENDA;

        PDPageContentStream contentStream;
        PDPageContentStream contentStreamPage2;
        PDPageContentStream contentStreamPage3;
        PDPageContentStream contentStreamPage4;
        PDPageContentStream contentStreamPage5;
        PDPageContentStream contentStreamPage6;
        ByteArrayOutputStream output =new ByteArrayOutputStream();
        PDDocument document =new PDDocument();
        PDFont font = PDType0Font.load(document, new java.io.File(Objects.requireNonNull(FileController.class.getResource("/static/fonts/Roboto-Regular.ttf")).getPath()));
        PDPage page = new PDPage();
        PDPage page2 = new PDPage();
        PDPage page3 = new PDPage();
        PDPage page4 = new PDPage();
        PDPage page5 = new PDPage();
        PDPage page6 = new PDPage();

        int marginTop = 330; // Or whatever margin you want.

        int fontSize = 26; // Or whatever font size you want.
        int fontSizeFooter = 6; // Or whatever font size you want.

        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * 15;

        float centroX = centraScritta(page,font, fontSize, nomeCompleto);
        float centroTipologiaX = centraScritta(page,font, fontSize, tipologia);
        float centroAziendaX = centraScritta(page,font, fontSizeFooter, nomeAzienda);
        float centroLuogoX = centraScritta(page,font, fontSizeFooter, luogoAzienda);
        float centroPIX = centraScritta(page,font, fontSizeFooter, piAzienda);
        float centroREAX = centraScritta(page,font, fontSizeFooter, reaAzienda);
        float centroY = (page.getMediaBox().getHeight()/2) - titleHeight;

        document.addPage(page);
        document.addPage(page2);
        document.addPage(page3);

        PDImageXObject pdImage = PDImageXObject.createFromFile(
                Objects.requireNonNull(
                        FileController
                                .class
                                .getResource(tipo == 1 ? "/static/images/innotekCF.jpg" : "/static/images/challenging.jpg")
                ).getPath(),
                document);
        contentStream = new PDPageContentStream(document, page);
        contentStream.drawImage(pdImage, 110, 675);
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(centroX, centroY+10);
        contentStream.showText(nomeCompleto != null ? nomeCompleto : "");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(centroTipologiaX, centroY-25);
        contentStream.showText(tipologia != null ? tipologia : "");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font, fontSizeFooter);
        contentStream.newLineAtOffset(centroAziendaX, 39);
        contentStream.showText(nomeAzienda);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font, fontSizeFooter);
        contentStream.newLineAtOffset(centroLuogoX, 27);
        contentStream.showText(luogoAzienda);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font, fontSizeFooter);
        contentStream.newLineAtOffset(centroPIX, 15);
        contentStream.showText(piAzienda);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font, fontSizeFooter);
        contentStream.newLineAtOffset(centroREAX, 3 );
        contentStream.showText(reaAzienda);
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
        contentStreamPage2.newLineAtOffset(centroX+10, 400);
        contentStreamPage2.showText("Skills");
        contentStreamPage2.endText();

        contentStreamPage2.moveTo(30, 610);
        contentStreamPage2.lineTo(page.getMediaBox().getWidth()-30, 609);
        contentStreamPage2.stroke();
        contentStreamPage2.moveTo(30, 390);
        contentStreamPage2.lineTo(page.getMediaBox().getWidth()-30, 389);
        contentStreamPage2.stroke();

        Integer offsetX = 20;
        Integer offsetY = 370;

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
        contentStreamPage2.showText("- Anno di nascita: " + (annoNascita != null ? annoNascita : ""));
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, 14);
        contentStreamPage2.newLineAtOffset(20, 570);
        contentStreamPage2.showText("- Domicilio attuale: " + (domicilio != null ? domicilio : ""));
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, 14);
        contentStreamPage2.newLineAtOffset(20, 550);
        contentStreamPage2.showText("- Anni di esperienza nel ruolo: " + (anniEsperienza != null ? anniEsperienza : ""));
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, 14);
        contentStreamPage2.newLineAtOffset(20, 530);
        contentStreamPage2.showText("- Background accademico: " + (livello != null ? livello : ""));
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
        contentStreamPage2.showText(nomeAzienda);
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, fontSizeFooter);
        contentStreamPage2.newLineAtOffset(centroLuogoX, 27);
        contentStreamPage2.showText(luogoAzienda);
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, fontSizeFooter);
        contentStreamPage2.newLineAtOffset(centroPIX, 15);
        contentStreamPage2.showText(piAzienda);
        contentStreamPage2.endText();

        contentStreamPage2.beginText();
        contentStreamPage2.setFont(font, fontSizeFooter);
        contentStreamPage2.newLineAtOffset(centroREAX, 3 );
        contentStreamPage2.showText(reaAzienda);
        contentStreamPage2.endText();

        contentStreamPage2.close();


        contentStreamPage3 = new PDPageContentStream(document, page3);
        contentStreamPage3.drawImage(pdImage, 110, 675);

        contentStreamPage3.beginText();
        contentStreamPage3.setFont(font, fontSize);
        contentStreamPage3.newLineAtOffset(centroX-60, 650);
        contentStreamPage3.showText("Professional Experiences");
        contentStreamPage3.endText();

        //contentStreamPage3.drawLine(20,610,page.getMediaBox().getWidth()-90,609);
        contentStreamPage3.moveTo(30, 640);
        contentStreamPage3.lineTo(page.getMediaBox().getWidth()-30, 639);
        contentStreamPage3.stroke();

        contentStreamPage3.beginText();
        contentStreamPage3.setFont(font, 14);
        contentStreamPage3.newLineAtOffset(offsetX, 595);
        contentStreamPage3.setLeading(18.5f);

        contentStreamPage4 = new PDPageContentStream(document, page4);
        contentStreamPage4.drawImage(pdImage, 110, 675);
        contentStreamPage4.beginText();
        contentStreamPage4.setFont(font, 14);
        contentStreamPage4.newLineAtOffset(offsetX, 650);
        contentStreamPage4.setLeading(18.5f);

        int countRows      = 0;
        int countRowsPage4 = 0;
        int countRowsPage5 = 0;

        boolean quartaPagina = false;
        boolean quintaPagina = false;
        boolean sestaPagina  = false;

        if (null != rispostaOpenAI) {
            String[] rows = rispostaOpenAI.split("\n");

            for (String row : rows) {
                row = row.replace("-","  -");
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
        contentStreamPage3.showText(nomeAzienda);
        contentStreamPage3.endText();

        contentStreamPage3.beginText();
        contentStreamPage3.setFont(font, fontSizeFooter);
        contentStreamPage3.newLineAtOffset(centroLuogoX, 27);
        contentStreamPage3.showText(luogoAzienda);
        contentStreamPage3.endText();

        contentStreamPage3.beginText();
        contentStreamPage3.setFont(font, fontSizeFooter);
        contentStreamPage3.newLineAtOffset(centroPIX, 15);
        contentStreamPage3.showText(piAzienda);
        contentStreamPage3.endText();

        contentStreamPage3.beginText();
        contentStreamPage3.setFont(font, fontSizeFooter);
        contentStreamPage3.newLineAtOffset(centroREAX, 3 );
        contentStreamPage3.showText(reaAzienda);
        contentStreamPage3.endText();

        contentStreamPage3.close();

        if (quartaPagina) {
            contentStreamPage4.endText();

            contentStreamPage4.beginText();
            contentStreamPage4.setFont(font, fontSizeFooter);
            contentStreamPage4.newLineAtOffset(centroAziendaX, 39);
            contentStreamPage4.showText(nomeAzienda);
            contentStreamPage4.endText();

            contentStreamPage4.beginText();
            contentStreamPage4.setFont(font, fontSizeFooter);
            contentStreamPage4.newLineAtOffset(centroLuogoX, 27);
            contentStreamPage4.showText(luogoAzienda);
            contentStreamPage4.endText();

            contentStreamPage4.beginText();
            contentStreamPage4.setFont(font, fontSizeFooter);
            contentStreamPage4.newLineAtOffset(centroPIX, 15);
            contentStreamPage4.showText(piAzienda);
            contentStreamPage4.endText();

            contentStreamPage4.beginText();
            contentStreamPage4.setFont(font, fontSizeFooter);
            contentStreamPage4.newLineAtOffset(centroREAX, 3 );
            contentStreamPage4.showText(reaAzienda);
            contentStreamPage4.endText();

            contentStreamPage4.close();

            contentStreamPage5 = new PDPageContentStream(document, page5);
            contentStreamPage5.drawImage(pdImage, 110, 675);

            contentStreamPage5.beginText();
            contentStreamPage5.setFont(font, fontSize);
            contentStreamPage5.newLineAtOffset(centroX-60, 650);
            contentStreamPage5.setLeading(18.5f);
            contentStreamPage5.showText("Education and Training");
            contentStreamPage5.endText();

            document.addPage(page5);

            contentStreamPage5.moveTo(30, 640);
            contentStreamPage5.lineTo(page.getMediaBox().getWidth()-30, 639);
            contentStreamPage5.stroke();

            contentStreamPage5.beginText();

            contentStreamPage6 = new PDPageContentStream(document, page6);
            contentStreamPage6.drawImage(pdImage, 110, 675);
            contentStreamPage6.beginText();
            contentStreamPage6.setFont(font, 14);
            contentStreamPage6.newLineAtOffset(offsetX, 620);
            contentStreamPage6.setLeading(18.5f);

            if (null != rispostaBackgroundOpenAI) {
                String[] rowsBackground = rispostaBackgroundOpenAI.split("\n");
                contentStreamPage5.newLine();

                for (String row : rowsBackground) {
                    countRowsPage5++;
                    row = row.replace("\r", "");

                    if(countRowsPage5 > 26 && !sestaPagina) {
                        sestaPagina = true;

                        document.addPage(page6);

                        contentStreamPage6.showText(row);
                        contentStreamPage6.newLine();
                    } else if (countRowsPage5 > 26){
                        contentStreamPage6.showText(row);
                        contentStreamPage6.newLine();
                    } else {
                        contentStreamPage5.showText(row);
                        contentStreamPage5.newLine();
                    }
                }
            }

            contentStreamPage5.endText();

            contentStreamPage5.beginText();
            contentStreamPage5.setFont(font, fontSizeFooter);
            contentStreamPage5.newLineAtOffset(centroAziendaX, 39);
            contentStreamPage5.showText(nomeAzienda);
            contentStreamPage5.endText();

            contentStreamPage5.beginText();
            contentStreamPage5.setFont(font, fontSizeFooter);
            contentStreamPage5.newLineAtOffset(centroLuogoX, 27);
            contentStreamPage5.showText(luogoAzienda);
            contentStreamPage5.endText();

            contentStreamPage5.beginText();
            contentStreamPage5.setFont(font, fontSizeFooter);
            contentStreamPage5.newLineAtOffset(centroPIX, 15);
            contentStreamPage5.showText(piAzienda);
            contentStreamPage5.endText();

            contentStreamPage5.beginText();
            contentStreamPage5.setFont(font, fontSizeFooter);
            contentStreamPage5.newLineAtOffset(centroREAX, 3 );
            contentStreamPage5.showText(reaAzienda);
            contentStreamPage5.endText();

            contentStreamPage5.close();

            if (sestaPagina) {
                contentStreamPage6.endText();

                contentStreamPage6.beginText();
                contentStreamPage6.setFont(font, fontSizeFooter);
                contentStreamPage6.newLineAtOffset(centroAziendaX, 39);
                contentStreamPage6.showText(nomeAzienda);
                contentStreamPage6.endText();

                contentStreamPage6.beginText();
                contentStreamPage6.setFont(font, fontSizeFooter);
                contentStreamPage6.newLineAtOffset(centroLuogoX, 27);
                contentStreamPage6.showText(luogoAzienda);
                contentStreamPage6.endText();

                contentStreamPage6.beginText();
                contentStreamPage6.setFont(font, fontSizeFooter);
                contentStreamPage6.newLineAtOffset(centroPIX, 15);
                contentStreamPage6.showText(piAzienda);
                contentStreamPage6.endText();

                contentStreamPage6.beginText();
                contentStreamPage6.setFont(font, fontSizeFooter);
                contentStreamPage6.newLineAtOffset(centroREAX, 3);
                contentStreamPage6.showText(reaAzienda);
                contentStreamPage6.endText();

                contentStreamPage6.close();
            }
        } else {
            document.addPage(page4);

            contentStreamPage4.showText("Education and Training");
            contentStreamPage4.endText();


            contentStreamPage4.moveTo(30, 640);
            contentStreamPage4.lineTo(page.getMediaBox().getWidth()-30, 639);
            contentStreamPage4.stroke();

            contentStreamPage4.beginText();

            contentStreamPage5 = new PDPageContentStream(document, page6);
            contentStreamPage5.drawImage(pdImage, 110, 675);
            contentStreamPage5.beginText();
            contentStreamPage5.setFont(font, 14);
            contentStreamPage5.newLineAtOffset(offsetX, 620);
            contentStreamPage5.setLeading(18.5f);

            if (null != rispostaBackgroundOpenAI) {
                String[] rowsBackground = rispostaBackgroundOpenAI.split("\n");
                contentStreamPage4.newLine();

                for (String row : rowsBackground) {
                    countRowsPage4++;
                    row = row.replace("\r", "");

                    if(countRowsPage4 > 26 && !quintaPagina) {
                        quintaPagina = true;

                        document.addPage(page5);

                        contentStreamPage5.showText(row);
                        contentStreamPage5.newLine();
                    } else if (countRowsPage4 > 26){
                        contentStreamPage5.showText(row);
                        contentStreamPage5.newLine();
                    } else {
                        contentStreamPage4.showText(row);
                        contentStreamPage4.newLine();
                    }
                }
            }

            contentStreamPage4.endText();

            contentStreamPage4.beginText();
            contentStreamPage4.setFont(font, fontSizeFooter);
            contentStreamPage4.newLineAtOffset(centroAziendaX, 39);
            contentStreamPage4.showText(nomeAzienda);
            contentStreamPage4.endText();

            contentStreamPage4.beginText();
            contentStreamPage4.setFont(font, fontSizeFooter);
            contentStreamPage4.newLineAtOffset(centroLuogoX, 27);
            contentStreamPage4.showText(luogoAzienda);
            contentStreamPage4.endText();

            contentStreamPage4.beginText();
            contentStreamPage4.setFont(font, fontSizeFooter);
            contentStreamPage4.newLineAtOffset(centroPIX, 15);
            contentStreamPage4.showText(piAzienda);
            contentStreamPage4.endText();

            contentStreamPage4.beginText();
            contentStreamPage4.setFont(font, fontSizeFooter);
            contentStreamPage4.newLineAtOffset(centroREAX, 3 );
            contentStreamPage4.showText(reaAzienda);
            contentStreamPage4.endText();

            contentStreamPage4.close();

            if (quintaPagina) {
                contentStreamPage5.endText();

                contentStreamPage5.beginText();
                contentStreamPage5.setFont(font, fontSizeFooter);
                contentStreamPage5.newLineAtOffset(centroAziendaX, 39);
                contentStreamPage5.showText(nomeAzienda);
                contentStreamPage5.endText();

                contentStreamPage5.beginText();
                contentStreamPage5.setFont(font, fontSizeFooter);
                contentStreamPage5.newLineAtOffset(centroLuogoX, 27);
                contentStreamPage5.showText(luogoAzienda);
                contentStreamPage5.endText();

                contentStreamPage5.beginText();
                contentStreamPage5.setFont(font, fontSizeFooter);
                contentStreamPage5.newLineAtOffset(centroPIX, 15);
                contentStreamPage5.showText(piAzienda);
                contentStreamPage5.endText();

                contentStreamPage5.beginText();
                contentStreamPage5.setFont(font, fontSizeFooter);
                contentStreamPage5.newLineAtOffset(centroREAX, 3);
                contentStreamPage5.showText(reaAzienda);
                contentStreamPage5.endText();

                contentStreamPage5.close();
            }
        }

        document.save(output);
        document.close();
        return output;
    }

    public static ByteArrayOutputStream createWord(
            Integer tipo,
            String nomeCompleto,
            String tipologia,
            Integer annoNascita,
            String livello,
            String domicilio,
            Double anniEsperienza,
            Set<Skill> skills,
            String rispostaOpenAI,
            String rispostaLinguaOpenAI,
            String rispostaBackgroundOpenAI) throws IOException, XmlException {
        //PDFont font = new PDType0Font(Standard14Fonts.FontName.RHELVETICA);

        String nomeAzienda = tipo == 1 ? NOME_AZIENDA_INNOTEK : NOME_AZIENDA;
        String luogoAzienda = tipo == 1 ? LUOGO_AZIENDA_INNOTEK : LUOGO_AZIENDA;
        String piAzienda = tipo == 1 ? PI_AZIENDA_INNOTEK : PI_AZIENDA;
        String reaAzienda = tipo == 1 ? REA_AZIENDA_INNOTEK : REA_AZIENDA;

       return null;
    }

    public static float centraScritta(PDPage page,PDFont font, int fontSize, String stringa) throws IOException {

        float width = font.getStringWidth(stringa) / 1000 * fontSize;

        return (page.getMediaBox().getWidth() - width) / 2;
    }
}
