
package de.dante.extex.documentWriter.pdf;

// FOP

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

// ExTeX

/**
 * Testprogramm für `fop'.
 * 
 * @author Rolf Niepraschk
 * @version $Revision: 1.2 $ $Date: 2004/02/11 07:49:18 $
 */
 
class FopTest2 {

        private FopTest2() {
        }
                
        private static void println(String s) {
                System.out.println(s);
        }

        private static void marker() {
                println("------------------------------------" + 
                        "------------------------------------"); // 72
        }
        
        public static void main(String[] args) {
                
          marker();
          println("Test der PDF-Erzeugung mit \"FOP\".");
          marker();

          PDFDocumentWriter pdw = new PDFDocumentWriter();
          try {
            pdw.open(new FileOutputStream("FopTest2" + "." + 
              pdw.getExtension()));

            pdw.op.lineWidth(5);

            pdw.op.gSave();
              pdw.op.strokeColor(Color.RED);
              pdw.op.fillColor(Color.YELLOW);  
              pdw.op.addRectangle(200,100,300,200); 
              pdw.op.fillStroke(); 
            pdw.op.gRestore();

            pdw.op.addRectangle(200,400,300,200); 
            pdw.op.stroke(); 
	    
	    if (false) // 
	    {
	      pdw.op.getStream().add( // PDF reference S. 225
	      "BT                         % Begin text object\n" +
	      "/F1 1 Tf                   % Set text font and size\n" +
	      "64 0 0 64 7.1771 2.4414 Tm % Set text matrix\n" +
	      "0 Tc                       % Set character spacing\n" +
	      "0 Tw                       % Set word spacing\n" +
	      "1.0 0.0 0.0 rg             % Set nonstroking color to red\n" +
	      "(\001) Tj                  % Show spade glyph\n" +
	      "0.7478 -0.007 TD           % Move text position\n" +
	      "0.0 1.0 0.0 rg             % Set nonstroking color to green\n" +
	      "(\002) Tj                  % Show heart glyph\n" +
	      "-0.7323 0.7813 TD          % Move text position\n" +
	      "0.0 0.0 1.0 rg             % Set nonstroking color to blue\n" +
	      "(\003) Tj                  % Show diamond glyph\n" +
	      "0.6913 0.007 TD            % Move text position\n" +
	      "0.0 0.0 0.0 rg             % Set nonstroking color to black\n" +
	      "(\004) Tj                  % Show club glyph\n" +
	      "ET                         % End text object\n");
            }
	    else // Alles noch sehr vage ;-|
	    {
	      //pdw.op.moveTo(200,600); 
	      pdw.op.getStream().add("BT\n");
	      pdw.op.getStream().add("/F1 1 Tf \n");
	      pdw.op.getStream().add("64 0 0 -64 7.1771 2.4414 Tm \n");
	      pdw.op.getStream().add(".5 Tc 0 Tw \n");
	      pdw.op.getStream().add("2 -12 TD \n");
	      pdw.op.getStream().add("(HUGO) Tj\n");
	      pdw.op.getStream().add("ET\n");
	    }
            pdw.newPage();

            pdw.op.lineWidth(5);

            pdw.op.addRectangle(200,100,300,200); 
            pdw.op.stroke();
            pdw.op.fillColor(Color.GREEN); 
            pdw.op.moveTo(200,100);
            pdw.op.curveTo(200,100,200,300,500,300); 
            pdw.op.curveTo(500,300,500,100,200,100); 
            pdw.op.fillStroke();

            pdw.close();
          } 
          catch (IOException e) {
            e.printStackTrace();
          }	  
        }
}

