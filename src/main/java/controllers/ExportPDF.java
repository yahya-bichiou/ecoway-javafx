package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import models.Commandes;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.stream.Stream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.BaseColor;

public class ExportPDF {

    public static void generateFacture(Commandes commande, String filePath) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
            Paragraph title = new Paragraph("FACTURE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Info Box
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(10f);
            infoTable.setSpacingAfter(20f);

            PdfPCell cell1 = new PdfPCell(new Phrase("Date:"));
            PdfPCell cell2 = new PdfPCell(new Phrase(LocalDate.now().toString()));
            PdfPCell cell3 = new PdfPCell(new Phrase("Commande ID:"));
            PdfPCell cell4 = new PdfPCell(new Phrase(String.valueOf(commande.getId())));
            PdfPCell cell5 = new PdfPCell(new Phrase("Client ID:"));
            PdfPCell cell6 = new PdfPCell(new Phrase(String.valueOf(commande.getClient_id())));
            PdfPCell cell7 = new PdfPCell(new Phrase("Mode de Paiement:"));
            PdfPCell cell8 = new PdfPCell(new Phrase(commande.getMode_paiement()));

            // Make the cells look clean
            for (PdfPCell cell : new PdfPCell[]{cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8}) {
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPadding(5);
            }

            infoTable.addCell(cell1);
            infoTable.addCell(cell2);
            infoTable.addCell(cell3);
            infoTable.addCell(cell4);
            infoTable.addCell(cell5);
            infoTable.addCell(cell6);
            infoTable.addCell(cell7);
            infoTable.addCell(cell8);

            document.add(infoTable);

            // Product Table
            PdfPTable productTable = new PdfPTable(4); // 4 columns: Name, Quantity, Price, Subtotal
            productTable.setWidthPercentage(100);
            productTable.setSpacingBefore(10f);

            // Headers
            Stream.of("Produit", "Quantité", "Prix Unitaire (DT)", "Sous-Total (DT)")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(headerTitle));
                        productTable.addCell(header);
                    });

            // Parse and add products
            String produitsString = commande.getProduits(); // Your encoded products
            String[] produitsArray = produitsString.split("%");

            for (String produitInfo : produitsArray) {
                String[] details = produitInfo.split("\\|");
                if (details.length >= 4) {
                    String name = details[0];
                    float price = Float.parseFloat(details[1]);
                    int quantity = Integer.parseInt(details[2]);
                    float subtotal = price * quantity;

                    productTable.addCell(name);
                    productTable.addCell(String.valueOf(quantity));
                    productTable.addCell(String.format("%.2f", price));
                    productTable.addCell(String.format("%.2f", subtotal));
                }
            }

            document.add(productTable);

            // Total Price
            document.add(new Paragraph("\n"));
            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph total = new Paragraph("Total: " + commande.getPrix() + " DT", totalFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
            System.out.println("Facture PDF created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to create PDF.");
        }
    }
}
