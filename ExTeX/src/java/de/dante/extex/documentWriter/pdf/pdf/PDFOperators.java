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

// FOP
import org.apache.fop.pdf.PDFStream;
import org.apache.fop.pdf.PDFColor;

// Java
import java.awt.Color;

/**
 *  Some operators for PDF.
 *
 * @author <a href="mailto:Rolf.Niepraschk@ptb.de">Rolf Niepraschk</a>
 * @version $Revision: 1.1 $
 */
public class PDFOperators {

    protected float lastX = 0;
    protected float lastY = 0;
    
    protected PDFStream stream;
    
    public PDFOperators(PDFStream stream) {
        this.stream = stream;
    }
    
    /**
     * "GSave", save the graphic state
     */
    public void gSave() {
      this.stream.add(" q\n");
    } 
      
    /**
     * "GRestore", restore the graphic state
     */
    public void gRestore() {
      this.stream.add(" Q\n");
    } 
          
    /**
     * "MoveTo"
     * @param x      the end x location 
     * @param y      the end y location 
     */
    public void moveTo(float x, float y) {
      this.lastX = x; this.lastY = y;
      this.stream.add(" " + x + " " + y + " m\n");
    }
    
    /**
     * "RMoveTo"
     * @param dx      the x difference to the move position
     * @param dy      the y difference to the move position
     */
    public void rMoveTo(float dx, float dy) {
      this.lastX = this.lastX + dx; this.lastY = this.lastY + dy;
      this.stream.add(" " + this.lastX + " " + this.lastY + " m\n");
    }
    
    /**
     * "LineTo"
     * @param x      the end x location 
     * @param y      the end y location 
     */
    public void lineTo(float x, float y) {
      this.lastX = x; this.lastY = y;
      this.stream.add(" " + x + " " + y + " l\n");
    }
    
    /**
     * "RLineTo"
     * @param dx      the x difference to the end of the line
     * @param dy      the y difference to the end of the line
     */
    public void rLineTo(float dx, float dy) {
      this.lastX = this.lastX + dx; this.lastY = this.lastY + dy;
      this.stream.add(" " + this.lastX + " " + this.lastY + " l\n");
    }
        
    /**
     * "LineWidth"
     * @param wd     width of the line 
     */
    public void lineWidth(float wd) {
      this.stream.add(" " + wd + " w\n");
    }
    
    /**
     * "Stroke"
     */
    public void stroke() {
      this.stream.add(" S\n");
    }
        
    /**
     * "Fill"
     */
    public void fill() {
      this.stream.add(" f\n");
    } 
    
    /**
     * "ClosePath"
     */
    public void closepath() {
      this.stream.add(" h\n");
    } 

    /**
     * "CloseStroke"
     */     
    public void closeStroke() {
      this.stream.add(" s\n");
    }
    
    /**
     * "CloseFillStroke"
     */     
    public void closeFillStroke() {
      this.stream.add(" b\n");
    }
    
    /**
     * "FillStroke"
     */     
    public void fillStroke() {
      this.stream.add(" B\n");
    }
    
    /**
     * "CloseFill"
     */   
    public void closeFill() {
      this.stream.add(" h f\n");
    }
    
    /**
     * "Concat"
     */
    public void concat(float sx, float u, float v, float sy, float tx, float ty) {
      this.stream.add(" " + sx + " " + u + " " + v + " " + 
                            sy + " " + tx + " " + ty + " cm\n");
    }
    
    /**
     * "CurveTo"
     * @param x1      first point x
     * @param y1      first point y
     * @param x2      second point x
     * @param y2      second point y
     * @param x3      third point x
     * @param y3      third point y
     */
    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
      this.lastX = x3; this.lastY = y3;
      this.stream.add(" " + x1 + " " + y1 + " " + x2 + " " + 
                            y2 + " " + x3 + " " + y3 + " c\n");
    } 
       
    /**
     * "AddRectangle"
     * @param x       the x position of left edge 
     * @param y       the y position of top edge 
     * @param wd      the width 
     * @param ht      the height      
     */    
    public void addRectangle(float x, float y, float wd, float ht) {
      this.lastX = x; this.lastY = y;
      this.stream.add(" " + x + " " + y + " " + wd + " " + ht + " re\n");
    } 
    
    /**
     * Adds color for stroking
     * @param col      the color
     */       
    public void strokeColor(Color col) {
      applyColor(col, false);
    }
    
    /**
     * Adds color for filling
     * @param col      the color
     */      
    public void fillColor(Color col) {
      applyColor(col, true);
    }
      
    /**
     * Adds color for filling or stroking
     * @param col      the color
     * @param fill     fill color if true, else stroke color
     * @see org.apache.fop.svg.PDFGraphics2D#applyColor(java.awt.Color, boolean)
     */    
    private void applyColor(Color col, boolean fill) {
      Color c = col; PDFColor currentColor = new PDFColor(0, 0, 0);
      
      if (c.getColorSpace().getType()
              == java.awt.color.ColorSpace.TYPE_RGB) {
          currentColor = new PDFColor(c.getRed(), c.getGreen(),
                                       c.getBlue());
          this.stream.add(currentColor.getColorSpaceOut(fill));              
      } else if (c.getColorSpace().getType()
                 == java.awt.color.ColorSpace.TYPE_CMYK) {
          float[] cComps = c.getColorComponents(new float[3]);
          double[] cmyk = new double[3];
          for (int i = 0; i < 3; i++) {
              // convert the float elements to doubles for pdf
              cmyk[i] = cComps[i];
          }
          currentColor = new PDFColor(cmyk[0], cmyk[1], cmyk[2], cmyk[3]);
          this.stream.add(currentColor.getColorSpaceOut(fill));
      } else if (c.getColorSpace().getType()
                 == java.awt.color.ColorSpace.TYPE_2CLR) {
          // used for black/magentacurrentColor.getColorSpaceOut(fill)
          float[] cComps = c.getColorComponents(new float[1]);
          double[] blackMagenta = new double[1];
          for (int i = 0; i < 1; i++) {
              blackMagenta[i] = cComps[i];
          }
          // currentColor = new PDFColor(blackMagenta[0], blackMagenta[1]);
          this.stream.add(currentColor.getColorSpaceOut(fill));
      } else {
          System.err.println("Color Space not supported.");
      }
    }  
    
    /**
     * Gets the current pdf stream.
     */      
    public PDFStream getStream() {
      return this.stream;
    }
}

    
