/*
 * Copyright (C) 2004 Rolf Niepraschk
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */
 
package de.dante.extex.documentWriter.pdf;

// Java
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.fop.apps.FOPException;
import org.apache.fop.layout.FontInfo;
import org.apache.fop.layout.FontState;
import org.apache.fop.layout.Page;
import org.apache.fop.pdf.PDFAnnotList;
import org.apache.fop.pdf.PDFColor;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFPage;
import org.apache.fop.pdf.PDFResources;
import org.apache.fop.pdf.PDFStream;
import org.apache.fop.render.pdf.FontSetup;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.typesetter.NodeList;

/**
 * Implementation of a pdf document writer.
 *
 * @author <a href="mailto:Rolf.Niepraschk@ptb.de">Rolf Niepraschk</a>
 * @version $Revision: 1.3 $
 * @see org.apache.fop.render.pdf.PDFRenderer
 * @see org.apache.fop.svg.PDFGraphics2D
 */
public class PDFDocumentWriter implements DocumentWriter {

    /**
     * The field <tt>out</tt> ...
     */
    private OutputStream out = null;

    /**
     * The field <tt>shippedPages</tt> ...
     */
    private int shippedPages = 0;

    /**
     * Creates a new object.
     */
    public PDFDocumentWriter() {
        super();
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {
        return shippedPages;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {
        return "pdf";
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setOutputStream(java.io.Writer)
     */
    public void setOutputStream(final OutputStream outStream) {
        out = outStream;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(de.dante.extex.typesetter.NodeList)
     */
    public void shipout(final NodeList nodes) throws IOException {
        StringBuffer sb = new StringBuffer();
        nodes.toString(sb,"\n");
        out.write(sb.toString().getBytes());
        out.write('\n');
        shippedPages++;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() throws IOException {
      FontSetup.addToResources(this.pdfDoc, fontInfo); // ??? //
      pdfDoc.outputTrailer(this.outputstream);
      this.outputstream.close();
    }
  
    private PDFDocument pdfDoc = null;
    private PDFStream currentStream = null;
    private PDFResources pdfResources = null;
    private PDFAnnotList currentAnnotList = null;
    private PDFPage currentPage = null;
    private PDFColor currentColor = null;
    private Page page = null; 
    
    /*private*/ public PDFOperators op = null;  
    
    private int pageWD = 595; // "bp"
    private int pageHT = 842; // "bp"  -- A4
    // Wo abfragen? \paperwidth / \paperheight, \mediawidth / \mediaheight ???
    
    private FileOutputStream outputstream = null;
    
    private FontInfo fontInfo = null;
    private FontState fontState = null;
    
    /**
     * Opens/setups the document and the first page
     * @param outputstream  the output file    
     */      
    public void open(FileOutputStream outputstream) throws IOException {
    
      this.outputstream = outputstream;
      
      pdfDoc = new PDFDocument();
      
      pdfDoc.setProducer("ExTeX-0.00"); // Wo abfragen?
      
      /*
         Wie setzt man das?
      
         /Author, /Title, /Creator, /Keywords, /CreationDate 
         
         Wie kann man testweise Kompression ausschalten?
      */
      
      /////////////////////////////////////////////////////////
      
      /* Irgendwelche Code-Fragmente zu Fonts... ????
      
      try {
        FontSetup.setup(fontInfo);
      }
      catch (FOPException e) {
        e.printStackTrace();
      }
      
      FontInfo fi = fontState.getFontInfo();
      
      fontInfo = new FontInfo(fontName, metricsFile, kerning,
                                    fontTriplets, embedFile);
      
      fontState = new FontState(fontState.getFontInfo(),
                                                  name, style, weight,
                                                  fsize * 1000, 0);
						  
      fontState = new FontState(fontInfo, fontFamily, fontStyle,
                                      fontWeight, fontSize, fontVariant);
				      						  
      Font font = new Font(fontState.getFontFamily(), fStyle,
                             (int)(fontState.getFontSize() / 1000));      
      
      try {      
        fontState = new FontState(fontInfo, "Helvetica", "italic",
                                      "bold", 14, 1);
      }
      catch (FOPException e) {
        e.printStackTrace();
      }	
      
      */
      			      
      /////////////////////////////////////////////////////////
      
      // Alles noch sehr vage ;-|
      
      fontInfo = new FontInfo();
      
      try {
        FontSetup.setup(fontInfo);
	fontState = new FontState(fontInfo, "Helvetica", "normal",
                                          "normal", 12, 0);
      }
      catch (FOPException e) {
        e.printStackTrace();
      }
      
      /////////////////////////////////////////////////////////
      				              
      pdfResources = pdfDoc.getResources();   
           
      pdfDoc.outputHeader(this.outputstream);
      
      newPage();
    }
    
    /**
     * Creates/setups a new page (update page size and transformation).
     */  
    public void newPage() throws IOException {
      pdfDoc.output(this.outputstream); 
      
      currentStream = pdfDoc.makeStream();

      currentPage = pdfDoc.makePage(pdfResources, currentStream,
        pageWD, pageHT, page);
        
      op = new PDFOperators(currentStream);  
        
      // TeX/SVG-Koordinatensystem.
      op.concat(1, 0, 0, -1, 0, pageHT);
    }

}
