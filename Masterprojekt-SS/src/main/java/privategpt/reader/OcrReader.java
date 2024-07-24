package privategpt.reader;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

public class OcrReader {
	public static String extractTextFromPDF(String pdfFilePath) {
	        StringBuilder extractedText = new StringBuilder();
	        
	        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
	            PDFTextStripper pdfStripper = new PDFTextStripper();
	            extractedText.append(pdfStripper.getText(document));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return extractedText.toString();
	    }
	
    public static  String readPdf(String filePath) {
        StringBuilder text = new StringBuilder();
        try {
            try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(filePath))) {
                int numberOfPages = pdfDoc.getNumberOfPages();
                // Schleife über jede PDF-Seite
                for (int i = 1; i <= numberOfPages; i++) {
                    // Der Inhalt einer Seite dem String "text" hinzufügen.
                    text.append(PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i))).append("\n");
                }
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }
	}