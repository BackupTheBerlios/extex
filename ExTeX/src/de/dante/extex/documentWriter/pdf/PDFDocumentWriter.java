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
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.extex.interpreter.type.node.AbstractNodeList;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.interpreter.type.node.GlueNode;
import de.dante.util.GeneralException;

/**
 * Implementation of a pdf document writer.
 *
 * @author <a href="mailto:Rolf.Niepraschk@ptb.de">Rolf Niepraschk</a>
 * @version $Revision: 1.7 $
 * @see org.apache.fop.render.pdf.PDFRenderer
 * @see org.apache.fop.svg.PDFGraphics2D
 */
public class PDFDocumentWriter implements DocumentWriter, NodeVisitor {

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
        this.out = outStream;
    }
    
    public void shipout(final NodeList nodes) throws IOException {
      newPage(); shippedPages++;
      processNodes(nodes);
    }
    
    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() throws IOException {
      //pdfDoc.getOutlineRoot().setTitle("Erste Schritte");
      FontSetup.addToResources(this.pdfDoc, fontInfo); // ??? //
      pdfDoc.outputTrailer(this.out);
    }
        
    private PDFDocument pdfDoc = null;
    private PDFStream cs = null;
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
    
    private float lastX, lastY, currentX, currentY;
    private boolean onlyStroke, show, markOrigin; 
    private StringBuffer operators;
    
    private float sp2bp(long sp) {
      return Math.round((double) sp / ((long)1 << 16) * 72.0 / 72.27);
    }

    public Object visitAdjust(Object value, Object Value2){return null;}
    public Object visitAfterMath(Object value, Object Value2){return null;}
    public Object visitAlignedLeaders(Object value, Object Value2){return null;}
    public Object visitBeforeMath(Object value, Object Value2){return null;}
    public Object visitCenteredLeaders(Object value, Object Value2){return null;}
    public Object visitDiscretionary(Object value, Object Value2){return null;}
    public Object visitExpandedLeaders(Object value, Object Value2){return null;}
    public Object visitGlue(Object value, Object Value2){return null;}
    public Object visitInsertion(Object value, Object Value2){return null;}
    public Object visitKern(Object value, Object Value2){return null;}
    public Object visitLigature(Object value, Object Value2){return null;}
    public Object visitMark(Object value, Object Value2){return null;}
    public Object visitPenalty(Object value, Object Value2){return null;}
    public Object visitRule(Object value, Object Value2){return null;}
    public Object visitSpace(Object value, Object Value2){return null;}
    public Object visitWhatsIt(Object value, Object Value2){return null;} 

    public Object visitVerticalList(Object value, Object Value2){
      operators.append(op.fillColor(Color.LIGHT_GRAY)); markOrigin = true;
      return null;
    }
    
    public Object visitHorizontalList(Object value, Object Value2){
      show = false; 
      return null;
    }
        
    public Object visitChar(Object value, Object Value2){
      operators.append(op.fillColor(Color.GREEN));
      return null;
    }   
    
    private String debugNode(Node node){ 
      StringBuffer sb = new StringBuffer();
      String nodeName = node.getClass().getName();    
      nodeName = nodeName.substring(nodeName.lastIndexOf('.')+1);
      nodeName = nodeName.substring(0, nodeName.lastIndexOf("Node"));
      // Haben wir irgendwo "regular expressions" bei der Hand?
      
      if (nodeName.equals("Glue")) node.toString(sb, "");
      
      return nodeName + 
       " (wd=" + node.getWidth().toPT() + 
       "\tht=" + node.getHeight().toPT() +
       "\tdp=" + node.getDepth().toPT() + ")" + "\t" +
       (nodeName.equals("Char") ? node.toString() : "") + sb.toString();
    }
    
    private void showNode(Node node) { 
    
      float wd = sp2bp(node.getWidth().getValue());
      float ht = sp2bp(node.getHeight().getValue());
      float dp = sp2bp(node.getDepth().getValue());
          
      onlyStroke = false; show = true; markOrigin = false;
      operators = new StringBuffer();

      try {
       node.visit(this, node, null);
      }
      catch (GeneralException e) {
       e.printStackTrace();
      } 

      if (show)
      {
       cs.add(op.gSave());
       
       if (markOrigin)
       {
         cs.add(op.gSave());
         cs.add(op.lineWidth(.6f));
         cs.add(op.strokeColor(Color.RED));
         cs.add(op.circle(72, 72, 5));
         cs.add(op.moveTo(72-5, 72)); cs.add(op.rLineTo(5, 0));
         cs.add(op.moveTo(72, 72-5)); cs.add(op.rLineTo(0, 5));
         cs.add(op.stroke());
         cs.add(op.gRestore());
       }
       
       cs.add(operators.toString());
       cs.add(op.lineWidth(.3f));
       cs.add(op.addRectangle(currentX, currentY-ht, wd, ht+dp));
       if (onlyStroke) cs.add(op.stroke()); else cs.add(op.fillStroke());
       
       if (dp > 0.0) // Basislinie
       {
         cs.add(op.gSave());
         cs.add(op.setLineDash(.3f, .3f));
         cs.add(op.moveTo(currentX, currentY));
         cs.add(op.rLineTo(wd, 0f)); 
         cs.add(op.stroke());
         cs.add(op.gRestore());
       }
       
       cs.add(op.gRestore());
      } 
      
      currentX = currentX + wd; 

      System.out.println(debugNode(node));
        
    }
   
    // Getestet mit "./extex -output pdf testdata/testlinebreak"
    //
    private void processNodes(final NodeList nodes) {
      NodeIterator it = nodes.iterator();
      float lastDP = 0.0f; float wd; float ht; float dp;  
               
      if (nodes instanceof VerticalListNode)
      {
        wd = sp2bp(nodes.getWidth().getValue());
        ht = sp2bp(nodes.getHeight().getValue());
        dp = sp2bp(nodes.getDepth().getValue());  
        lastX = currentX = 72; lastY = currentY = 72 + ht; 
        // Basepoint setzen, so dass linke obere Ecke der 
        // Vbox bei 1in Offset erscheint.
        lastDP = 0.0f; 
        System.out.println("\n");
        showNode(nodes);
        lastX = 72; lastY = 72 - ht; lastDP = ht;
      }
      
      while(it.hasNext()) 
      {
        Node n = it.next();
        if (n instanceof HorizontalListNode) 
        {
          wd = sp2bp(n.getWidth().getValue());
          ht = sp2bp(n.getHeight().getValue());
          dp = sp2bp(n.getDepth().getValue()); 
          currentX = lastX; currentY = lastY + lastDP + ht; 
          lastDP = dp;
          System.out.println(debugNode(n)); 
          processNodes((NodeList)n);
          lastY = currentY;
        }
        else showNode(n);
      }
    }
    
    /**
     * Opens/setups the document    
     */      
    private void initDocument() throws IOException {
      
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
      
      op = new PDFOperators();  
      
    }
    
    /**
     * Creates/setups a new page 
     */  
    private void newPage() throws IOException {
    
      if (pdfDoc == null) initDocument();
    
      pdfDoc.output(this.out); 
      
      cs = pdfDoc.makeStream();

      currentPage = pdfDoc.makePage(pdfResources, cs,
        pageWD, pageHT, page);
        
      // TeX/SVG-Koordinatensystem.
      cs.add(op.concat(1, 0, 0, -1, 0, pageHT));
    }    
}
