package fpt.CapstoneSU24.util;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

@Service
public class DocumentGenerator {
    public  String htmlToPdf(String processedHtml){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {

            PdfWriter pdfwriter = new PdfWriter(byteArrayOutputStream);

            DefaultFontProvider defaultFont = new DefaultFontProvider(true, false, false);

            ConverterProperties converterProperties = new ConverterProperties();

            converterProperties.setFontProvider(defaultFont);

            HtmlConverter.convertToPdf(processedHtml, pdfwriter, converterProperties);
            String userHome = System.getProperty("user.home");
            String outputFilePath = userHome + "/TEST_CAPton.pdf";
            FileOutputStream fout = new FileOutputStream(outputFilePath);

            byteArrayOutputStream.writeTo(fout);
            byteArrayOutputStream.close();

            byteArrayOutputStream.flush();
            fout.close();

            return null;

        } catch(Exception ex) {

            System.out.println("Loi in PDFFFFFFF: " + ex.getMessage());
        }

        return null;
    }

    public byte[] onlineHtmlToPdf(String processedHtml) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);

            DefaultFontProvider defaultFont = new DefaultFontProvider(false, false, true);

            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setFontProvider(defaultFont);

            HtmlConverter.convertToPdf(processedHtml, pdfWriter, converterProperties);

            return byteArrayOutputStream.toByteArray();
        } catch (Exception ex) {
            System.out.println("Error generating PDF: " + ex.getMessage());
        }

        return null;
    }


}
