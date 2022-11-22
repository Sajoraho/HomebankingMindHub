package com.mindhub.homebanking.services.implement;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class PDFGeneratorService implements PDFService {
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void export(Account account, Set<Transaction> transactions, HttpServletResponse response) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();


        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontTitle.setSize(18);

        Paragraph title = new Paragraph("Transacciones", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        Font fontParagraph = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontParagraph.setSize(12);
        Paragraph subtitle = new Paragraph("Transacciones de cuentas." + account.getNumber(), fontParagraph);
        subtitle.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph space = new Paragraph("                                                             ");
        Paragraph space2 = new Paragraph("                                                            ");
        Paragraph space3 = new Paragraph("                                                            ");



//        com.lowagie.text.Image image = null;
//        image= Image.getInstance("src/main/resources/static/web/assets/img/LogoRockBank.png");
//        image.scaleAbsolute(100,100);
//        image.setAbsolutePosition(415,750);



        var table = new PdfPTable(4);
        Stream.of("Fecha de Creacion","Monto","Tipo", "Descripcion").forEach(table::addCell);

        transactions.forEach(transaction -> {
            table.addCell(transaction.getDate().toString());
            table.addCell("$" +transaction.getAmount());
            table.addCell(transaction.getType().toString());
            table.addCell(transaction.getDescription());

        });
//        document.add(image);
        document.add(title);
        document.add(space);
        document.add(subtitle);
        document.add(space2);
        document.add(space3);
        document.add(table);
        document.close();

    }
}

