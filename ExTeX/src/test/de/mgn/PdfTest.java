/*
 * Created on 17.01.2004
 *
 */
package de.mgn;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * PdfTest
 * 
 * @author mgn
 *
 */
public class PdfTest {

	public static void main(String[] args) {

		System.out.println("MGN PdfTest");

		// step 1: creation of a document-object
		Document document = new Document();

		try {

			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("mgn.pdf"));
			document.open();
			PdfContentByte cb = writer.getDirectContent();

			BaseFont palatino = BaseFont.createFont("font/uplr8a.afm", BaseFont.WINANSI, BaseFont.EMBEDDED);

			System.out.println(palatino.getPostscriptFontName());

			cb.beginText();
			cb.setFontAndSize(palatino, 12);

			for (int i='A', pos=10 ; i<'Z'; i++, pos+=12) {
				cb.setTextMatrix(pos, 700);
				cb.showText(String.valueOf((char)i));
				
			}
			cb.endText();

		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
