package com.tvv.utils;

import com.tvv.db.entity.Payment;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class PDFCreator {

    public static ByteArrayOutputStream createPDF(Payment payment, String path) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            String fileFont = path + "\\cour.ttf";
            PDFont font = PDType0Font.load(doc, new File(fileFont));
            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            contentStream.setFont(font, 14);

            float maxY = page.getCropBox().getUpperRightY();
            float startX = page.getCropBox().getLowerLeftX() + 30;
            float endX = page.getCropBox().getUpperRightX() - 30;
            float startX1 = page.getCropBox().getUpperRightX() / 2;
            float startY1 = page.getCropBox().getUpperRightY() - 10;

            //horizontal line
            contentStream.moveTo(startX, maxY - 70);
            contentStream.lineTo(endX, maxY - 70);
            contentStream.stroke();
            // vertical line
            contentStream.moveTo(startX1, startY1);
            contentStream.lineTo(startX1, maxY - 70);
            contentStream.stroke();

            float divX1 = 40;
            float divX2 = 315;
            float divX3 = 35;
            float divX4 = 310;

            DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
            double sumWithoutCommission = Double.parseDouble(df.format(payment.getTotal() - payment.getCommission()));

            drawText(contentStream, font, 14, divX1, maxY - 40, "vPAY | MONEY TRANSFER SERVICE");

            drawText(contentStream, font, 8, divX2, maxY - 36, "vPay System ltd.");
            drawText(contentStream, font, 8, divX2, maxY - 46, "Tel. 08001234321");
            drawText(contentStream, font, 8, divX2, maxY - 56, "Address: Kyiv, Khreshatyk 1, 01000");

            drawText(contentStream, font, 14, divX3, maxY - 95, "Payment #" + payment.getGuid() + " from " + payment.getTimeOfLog());

            drawText(contentStream, font, 11, divX3, maxY - 125, "Sender: ");
            drawText(contentStream, font, 11, divX3, maxY - 145, payment.getUser().getFirstName() + " " + payment.getUser().getLastName());
            drawText(contentStream, font, 11, divX3, maxY - 165, "Account: "+payment.getSenderId().getName());
            drawText(contentStream, font, 11, divX3, maxY - 185, "(" + payment.getSenderId().getIban() + ")");
            drawText(contentStream, font, 11, divX3, maxY - 215, "Sum of transaction (" + payment.getCurrency() + "): " + sumWithoutCommission);
            drawText(contentStream, font, 11, divX3, maxY - 235, "Commission (" + payment.getCurrency() + "): " + payment.getCommission());
            drawText(contentStream, font, 11, divX3, maxY - 255, "Total (" + payment.getCurrency() + "): " + payment.getTotal());
            drawText(contentStream, font, 11, divX3, maxY - 295, "Date and time of transaction: " + payment.getTimeOfLog());

            drawText(contentStream, font, 11, divX4, maxY - 125, "Recipient:");
            drawText(contentStream, font, 11, divX4, maxY - 145, "type: " + payment.getRecipientType());
            drawText(contentStream, font, 11, divX4, maxY - 165, "number: " + payment.getRecipientId());
            drawText(contentStream, font, 11, divX4, maxY - 215, "Sum for recipient (" + payment.getCurrencySum() + "): " + payment.getSum());

            contentStream.close();

            doc.save(outputStream);

        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return outputStream;
    }

    private static void drawText(PDPageContentStream contentStream, PDFont font, float size, float x, float y, String text) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.setFont(font, size);
        contentStream.showText(text);
        contentStream.endText();
    }
}
