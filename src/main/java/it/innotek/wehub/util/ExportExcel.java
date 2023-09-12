/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.util;

import it.innotek.wehub.entity.Report;
import it.innotek.wehub.entity.timesheet.Giorno;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.MimeTypeUtils;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExportExcel {

    public static void export(
        HttpServletResponse response,
        List<Report> listReport,
        Integer mese,
        Integer anno
    ) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet    sheet    = workbook.createSheet("Report");
        Row          rowH     = sheet.createRow(0);
        int          rowCount = 1;

        rowH.setHeight((short) 500);

        CellStyle style   = stile(workbook, true, HSSFColor.HSSFColorPredefined.WHITE.getIndex(),  HSSFColor.HSSFColorPredefined.BLACK.getIndex(), true);
        CellStyle style2  = stile(workbook, true, HSSFColor.HSSFColorPredefined.BLACK.getIndex(),  HSSFColor.HSSFColorPredefined.WHITE.getIndex(), false);
        CellStyle style3  = stile(workbook, true, HSSFColor.HSSFColorPredefined.BLACK.getIndex(),  HSSFColor.HSSFColorPredefined.CORAL.getIndex(), true);
        CellStyle style4  = stile(workbook, true, HSSFColor.HSSFColorPredefined.BLUE.getIndex(),   HSSFColor.HSSFColorPredefined.WHITE.getIndex(), false);
        CellStyle style5  = stile(workbook, true, HSSFColor.HSSFColorPredefined.BLUE.getIndex(),   HSSFColor.HSSFColorPredefined.CORAL.getIndex(), true);
        CellStyle style6  = stile(workbook, true, HSSFColor.HSSFColorPredefined.ORANGE.getIndex(), HSSFColor.HSSFColorPredefined.WHITE.getIndex(), false);
        CellStyle style7  = stile(workbook, true, HSSFColor.HSSFColorPredefined.ORANGE.getIndex(), HSSFColor.HSSFColorPredefined.CORAL.getIndex(), true);
        CellStyle style8  = stile(workbook, true, HSSFColor.HSSFColorPredefined.GREEN.getIndex(),  HSSFColor.HSSFColorPredefined.WHITE.getIndex(), false);
        CellStyle style9  = stile(workbook, true, HSSFColor.HSSFColorPredefined.GREEN.getIndex(),  HSSFColor.HSSFColorPredefined.CORAL.getIndex(), true);
        CellStyle style10 = stile(workbook, false, HSSFColor.HSSFColorPredefined.WHITE.getIndex(), HSSFColor.HSSFColorPredefined.BLUE.getIndex(), true);
        CellStyle style11 = stile(workbook, false, HSSFColor.HSSFColorPredefined.WHITE.getIndex(), HSSFColor.HSSFColorPredefined.GREEN.getIndex(), true);
        CellStyle style12 = stile(workbook, false, HSSFColor.HSSFColorPredefined.WHITE.getIndex(), HSSFColor.HSSFColorPredefined.ORANGE.getIndex(), true);

        //font.setColor(IndexedColors.WHITE.getIndex());
        createCell(rowH, 0, "Dipendente", style);
        sheet.setColumnWidth(0, 10000);
        for (int i = 1; i <= UtilLib.calcolaFineMese(mese, anno); i++) {
            sheet.setColumnWidth(i, 1500);
            createCell(rowH, i, i, style);
        }

        sheet.setColumnWidth(UtilLib.calcolaFineMese(mese, anno)+1, 4000);
        sheet.setColumnWidth(UtilLib.calcolaFineMese(mese, anno)+2, 3000);

        createCell(rowH, UtilLib.calcolaFineMese(mese, anno)+1, "Straordinario", style);
        createCell(rowH, UtilLib.calcolaFineMese(mese, anno)+2, "Notturno",      style);
        createCell(rowH, UtilLib.calcolaFineMese(mese, anno)+3, "Totale",        style);
        createCell(rowH, UtilLib.calcolaFineMese(mese, anno)+5, "Ferie",         style10);
        createCell(rowH, UtilLib.calcolaFineMese(mese, anno)+6, "Permesso",      style11);
        createCell(rowH, UtilLib.calcolaFineMese(mese, anno)+7, "Malattia",      style12);

        for (Report report : listReport) {
            Row     row                    = sheet.createRow(rowCount++);
            int     columnCount            = 0;
            Integer oreTotaliStraordinario = 0;
            Integer oreTotaliStraNotturno  = 0;
            Integer oreTotali              = 0;

            row.setHeight((short) 500);
            createCell(row, columnCount++, report.getNome(), style2);

            for (Giorno giorno : report.getGiorni()) {
                oreTotaliStraordinario += giorno.getOreStraordinarie();
                oreTotaliStraNotturno  += giorno.getOreStraordinarieNotturne();
                oreTotali              += giorno.getOreTotali();

                if (giorno.isFestivo()) {
                    if (giorno.isFerie()) {
                        createCell(row, columnCount++, giorno.getOreTotali(), style5);
                    } else if (giorno.isMalattia()) {
                        createCell(row, columnCount++, giorno.getOreTotali(), style7);
                    } else if (giorno.isPermesso()) {
                        createCell(row, columnCount++, giorno.getOrePermesso() +" "+
                            (giorno.getOreTotali()-giorno.getOrePermesso()), style9);
                    } else {
                        createCell(row, columnCount++, giorno.getOreTotali(), style3);
                    }
                } else {
                    if (giorno.isFerie()) {
                        createCell(row, columnCount++, giorno.getOreTotali(), style4);
                    } else if (giorno.isMalattia()) {
                        createCell(row, columnCount++, giorno.getOreTotali(), style6);
                    } else if (giorno.isPermesso()) {
                        createCell(row, columnCount++, giorno.getOrePermesso() +" "+
                            (giorno.getOreTotali()-giorno.getOrePermesso()), style8);
                    } else {
                        createCell(row, columnCount++, giorno.getOreTotali(), style2);
                    }
                }
            }
            createCell(row, columnCount++, oreTotaliStraordinario, style2);
            createCell(row, columnCount++, oreTotaliStraNotturno, style2);
            createCell(row, columnCount++, oreTotali, style2);
        }
        sheet.createRow(rowCount++);
        sheet.createRow(rowCount++);
        sheet.createRow(rowCount++);
        sheet.createRow(rowCount++);
        sheet.createRow(rowCount++);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try (bos) {
            workbook.write(bos);
        }

        byte[] bytes = bos.toByteArray();

        response.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM.getType());
        response.setHeader("Content-Disposition", "attachment; filename=Report.xlsx" );
        response.setContentLength(bytes.length);

        try (OutputStream os = response.getOutputStream()) {
            os.write(bytes, 0, bytes.length);
        }
    }

    public static void createCell(
        Row row,
        int columnCount,
        Object value,
        CellStyle style
    ) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public static CellStyle stile(XSSFWorkbook wb, boolean fontHeight, short fontColor, short color, boolean solidForeground){

        CellStyle style = wb.createCellStyle();
        XSSFFont  font  = wb.createFont();

        font.setBold(false);
        font.setFontName("Arial");

        if (fontHeight) {
            font.setFontHeight(13);
        }

        font.setColor(fontColor);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(color);

        if (solidForeground) {
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        return style;
    }
}