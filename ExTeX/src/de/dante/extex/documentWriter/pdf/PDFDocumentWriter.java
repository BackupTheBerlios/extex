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
import java.awt.Color;

// FOP
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

// ExTeX
import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.interpreter.type.node.AbstractNodeList;

/**
 * Implementation of a pdf document writer.
 *
 * @author <a href="mailto:Rolf.Niepraschk@ptb.de">Rolf Niepraschk</a>
 * @version $Revision: 1.4 $
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
    
    private PDFDocument pdfDoc = null;
    private PDFStream currentStream = null;
    private PDFResources pdfResources = null;
    private PDFAnnotList currentAnnotList = null;
    private PDFPage currentPage = null;
    private PDFColor currentColor = null;
    private Page page = null; 
    private PDFOperators op = null;  
    
    private int pageWD = 595; // "bp"
    private int pageHT = 842; // "bp"  -- A4
    // Wo abfragen? \paperwidth / \paperheight, \mediawidth / \mediaheight ???
    
    private FontInfo fontInfo = null;
    private FontState fontState = null;
    
   private float sp2bp(long sp) {
   /*
       So oder per Transformation? Wie genau rechnet PDF? Einstellbar?
   */
        return (float)((sp >> 16) * 72.0 / 72.27);
   }
   
   private float lastX = 0f, lastY = 0f;
   
   private void showNode(Node node) {
   
        boolean onlyStroke = false, show = true; 
        
        float x = 0f, y = 0f;
        float wd = sp2bp(node.getWidth().getValue());
        float ht = sp2bp(node.getHeight().getValue());
        float dp = sp2bp(node.getDepth().getValue());
        
        op.gSave();
          
        if (node.getType().equals("char")) 
        {
          x = lastX; y = lastY; 
	  lastX = x + wd; lastY = y + ht + dp; 
	  // Nur damit es weniger eintönig ist ;-)
          op.fillColor(Color.GREEN);
        }
        if (node.getType().equals("horizontallist")) 
        {
          show = false;
        }
        if (node.getType().equals("verticallist"))  
        {
          op.fillColor(Color.LIGHT_GRAY);  //onlyStroke = true;
        }
        
        if (show)
        {
          op.addRectangle(x, y+ht, wd, ht+dp); 
          if (onlyStroke) op.stroke(); else op.fillStroke();
        }
        
        op.gRestore();
        
        System.out.println(node.getType() + 
          " (wd=" + node.getWidth().toPT() + 
          "\tht=" + node.getHeight().toPT() +
          "\tdp=" + node.getDepth().toPT() + ")" + "\t" +
	  ((node.getType().equals("char")) ? node.toString() : ""));
   }

   public void shipout(final NodeList nodes) throws IOException {
        newPage(); shippedPages++;
        System.out.println("\n");
          op.concat(1, 0, 0, 1, 72, 72);
          // Hier mal testweise zu Pos. (72bp, 72bp) verschieben ...
          processNodes(nodes);
        System.out.println("\n");
    }
    
    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() throws IOException {
      //pdfDoc.getOutlineRoot().setTitle("Erste Schritte");
      FontSetup.addToResources(this.pdfDoc, fontInfo); // ??? //
      pdfDoc.outputTrailer(this.out);

      // this.out.close();
    }
    
    private void processNodes(final NodeList nodes) {
      NodeIterator it = nodes.iterator(); 
      
      showNode(nodes);  
      
      System.out.println("---------------BEGIN----------------");
      while(it.hasNext()) 
      {
        Node n = it.next();
        if (n instanceof NodeList) processNodes((NodeList)n);
        else showNode(n);
      }
      System.out.println("---------------END------------------"); 
    }
    
    /**
     * Opens/setups the document    
     */      
    public void initDocument() throws IOException {
      
      pdfDoc = new PDFDocument();
      
      pdfDoc.setProducer("ExTeX-0.00"); // Wo abfragen?
      
      /*
         Wie setzt man das?
      
         /Author, /Title, /Creator, /Keywords, /CreationDate 
         
         Wie kann man testweise Kompression ausschalten?
      */
      
      fontInfo = new FontInfo();
      
      try {
        FontSetup.setup(fontInfo);
        fontState = new FontState(fontInfo, "Helvetica", "normal",
                                          "normal", 12, 0);
      }
      catch (FOPException e) {
        e.printStackTrace();
      }
      
      pdfResources = pdfDoc.getResources();   
           
      pdfDoc.outputHeader(this.out);
      
    }
    
    /**
     * Creates/setups a new page 
     */  
    public void newPage() throws IOException {
    
      if (pdfDoc == null) initDocument();
    
      pdfDoc.output(this.out); 
      
      currentStream = pdfDoc.makeStream();

      currentPage = pdfDoc.makePage(pdfResources, currentStream,
        pageWD, pageHT, page);
        
      op = new PDFOperators(currentStream);  
        
      // TeX/SVG-Koordinatensystem.
      op.concat(1, 0, 0, -1, 0, pageHT);
    }    
}
