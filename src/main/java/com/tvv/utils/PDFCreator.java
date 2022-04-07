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
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Class for PDF operation
 */
public class PDFCreator {

    /**
     * Create PDF byte stream
     * @param payment current payment which will be transformed to PDF
     * @param path path to custom fonts
     * @return byte array stream
     */
    public static ByteArrayOutputStream createPDF(Payment payment, String path, String local){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            /**
             * Use Apache PDFBox for create PDF document
             */
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            /**
             * Load font
             */
            String fileFont = path + "\\cour.ttf";
            PDFont font = PDType0Font.load(doc, new File(fileFont));
            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            contentStream.setFont(font, 14);

            /**
             * Coordinates for markup (lines and text)
             */
            float maxY = page.getCropBox().getUpperRightY();
            float startX = page.getCropBox().getLowerLeftX() + 30;
            float endX = page.getCropBox().getUpperRightX() - 30;
            float startX1 = page.getCropBox().getUpperRightX() / 2;
            float startY1 = page.getCropBox().getUpperRightY() - 10;

            /**
             * Draw lines
             */
            //horizontal line
            contentStream.moveTo(startX, maxY - 70);
            contentStream.lineTo(endX, maxY - 70);
            contentStream.stroke();
            // vertical line
            contentStream.moveTo(startX1, startY1);
            contentStream.lineTo(startX1, maxY - 70);
            contentStream.stroke();

            /**
             * General X points
             */
            float divX1 = 40;
            float divX2 = 315;
            float divX3 = 35;
            float divX4 = 310;

            /**
             * Decimal format for double value
             */
            DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
            double sumWithoutCommission = Double.parseDouble(df.format(payment.getTotal() - payment.getCommission()));

            /**
             * Create content and draw text with localization
             */
            Locale locale = new Locale(local);
            ResourceBundle message = ResourceBundle.getBundle("resources",locale);


            drawText(contentStream, font, 14, divX1, maxY - 40, message.getString("pdf.system.name"));

            drawText(contentStream, font, 8, divX2, maxY - 36, message.getString("pdf.company.name"));
            drawText(contentStream, font, 8, divX2, maxY - 46, message.getString("pdf.company.phone"));
            drawText(contentStream, font, 8, divX2, maxY - 56, message.getString("pdf.company.address"));

            drawText(contentStream, font, 14, divX3, maxY - 95, message.getString("pdf.body.payment")+" #" +
                    payment.getGuid() + " " +
                    message.getString("pdf.body.from") + " " +
                    payment.getTimeOfLog());

            drawText(contentStream, font, 11, divX3, maxY - 125, message.getString("pdf.body.sender.title")+": ");
            drawText(contentStream, font, 11, divX3, maxY - 145, payment.getUser().getFirstName() + " " +
                    payment.getUser().getLastName());
            drawText(contentStream, font, 11, divX3, maxY - 165, message.getString("pdf.body.sender.account")+": "+
                    payment.getSenderId().getName());
            drawText(contentStream, font, 11, divX3, maxY - 185, "(" + payment.getSenderId().getIban() + ")");
            drawText(contentStream, font, 11, divX3, maxY - 215, message.getString("pdf.body.sender.sum")+
                    " (" + payment.getCurrency() + "): " + sumWithoutCommission);
            drawText(contentStream, font, 11, divX3, maxY - 235, message.getString("pdf.body.sender.commission") +
                    " (" + payment.getCurrency() + "): " + payment.getCommission());
            drawText(contentStream, font, 11, divX3, maxY - 255, message.getString("pdf.body.sender.total")+
                    " (" + payment.getCurrency() + "): " + payment.getTotal());
            drawText(contentStream, font, 11, divX3, maxY - 295, message.getString("pdf.body.sender.datetime_title")+
                    ": " + payment.getTimeOfLog());

            drawText(contentStream, font, 11, divX4, maxY - 125, message.getString("pdf.body.recipient.title")+":");
            drawText(contentStream, font, 11, divX4, maxY - 145, message.getString("pdf.body.recipient.type")+
                    ": " + payment.getRecipientType());
            drawText(contentStream, font, 11, divX4, maxY - 165, message.getString("pdf.body.recipient.number")+
                    ": " + payment.getRecipientId());
            drawText(contentStream, font, 11, divX4, maxY - 215, message.getString("pdf.body.recipient.sum")+
                    " (" + payment.getCurrencySum() + "): " + payment.getSum());

            contentStream.close();

            /**
             * Save PDF to stream
             */
            doc.save(outputStream);
            doc.close();

        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return outputStream;
    }

    /**
     * Function for draw formatted text
     * @param contentStream PDF document content
     * @param font font object
     * @param size font size
     * @param x X coordinate at page
     * @param y Y coordinate at page
     * @param text text for drawing
     * @throws IOException
     */
    private static void drawText(PDPageContentStream contentStream, PDFont font, float size, float x, float y, String text) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.setFont(font, size);
        contentStream.showText(text);
        contentStream.endText();
    }
}
