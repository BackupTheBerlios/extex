/*
 * Copyright (C) 2004 The ExTeX Group
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

import java.awt.Color;

import org.apache.fop.pdf.PDFColor;

/**
 * Some operators for PDF.
 *
 * @author <a href="mailto:Rolf.Niepraschk@ptb.de">Rolf Niepraschk </a>
 * @version $Revision: 1.6 $
 */
public class PDFOperators {

    /**
     * The last x position
     */
    private float lastX = 0;

    /**
     * The last y position
     */
    private float lastY = 0;

    /**
     * Create a new object
     */
    public PDFOperators() {
        super();
    }

    /**
     * "GSave", save the graphic state
     * @return The pdf statements
     */
    public String gSave() {
        return " q\n";
    }

    /**
     * "GRestore", restore the graphic state
     * @return The pdf statements
     */
    public String gRestore() {
        return " Q\n";
    }

    /**
     * "MoveTo"
     *
     * @param x the end x location
     * @param y the end y location
     * @return The pdf statements
     */
    public String moveTo(final float x, final float y) {
        this.lastX = x;
        this.lastY = y;
        return " " + x + " " + y + " m\n";
    }

    /**
     * "RMoveTo"
     *
     * @param dx the x difference to the move position
     * @param dy the y difference to the move position
     * @return The pdf statements
     */
    public String rMoveTo(final float dx, final float dy) {
        this.lastX = this.lastX + dx;
        this.lastY = this.lastY + dy;
        return " " + this.lastX + " " + this.lastY + " m\n";
    }

    /**
     * "LineTo"
     *
     * @param x the end x location
     * @param y the end y location
     * @return The pdf statements
     */
    public String lineTo(final float x, final float y) {
        this.lastX = x;
        this.lastY = y;
        return " " + x + " " + y + " l\n";
    }

    /**
     * Appends a Bêzier curve to the path, starting from the current point.
     *
     * @param x1 x-coordinate of the first control point
     * @param y1 y-coordinate of the first control point
     * @param x2 x-coordinate of the second control point
     * @param y2 y-coordinate of the second control point
     * @param x3 x-coordinaat of the ending point (= new current point)
     * @param y3 y-coordinaat of the ending point (= new current point)
     * @return The pdf statements
     */
    public String curveTo(final float x1, final float y1, final float x2,
      final float y2, final float x3, final float y3) {
        this.lastX = x3;
        this.lastY = y3;
        return " " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + x3 + " " + y3
          + " c\n";
    }

    /**
     * Appends a Bêzier curve to the path, starting from the current point.
     *
     * @param x2 x-coordinate of the second control point
     * @param y2 y-coordinate of the second control point
     * @param x3 x-coordinaat of the ending point (= new current point)
     * @param y3 y-coordinaat of the ending point (= new current point)
     * @return The pdf statements
     */
    public String curveTo(final float x2, final float y2, final float x3,
      final float y3) {
        this.lastX = x3;
        this.lastY = y3;
        return " " + x2 + " " + y2 + " " + x3 + " " + y3 + " v\n";
    }

    /**
     * Appends a Bêzier curve to the path, starting from the current point.
     *
     * @param x1 x-coordinate of the first control point
     * @param y1 y-coordinate of the first control point
     * @param x3 x-coordinaat of the ending point (= new current point)
     * @param y3 y-coordinaat of the ending point (= new current point)
     * @return The pdf statements
     */
    public String curveFromTo(final float x1, final float y1, final float x3,
      final float y3) {
        this.lastX = x3;
        this.lastY = y3;
        return " " + x1 + " " + y1 + " " + x3 + " " + y3 + " y\n";
    }

    /**
     * Draws a circle. The endpoint will (x+r, y).
     *
     * @param x x center of circle
     * @param y y center of circle
     * @param r radius of circle
     * @return The pdf statements
     */
    public String circle(final float x, final float y, final float r) {
        float b = 0.55228474f;
        return moveTo(x + r, y)
          + curveTo(x + r, y + r * b, x + r * b, y + r, x, y + r)
          + curveTo(x - r * b, y + r, x - r, y + r * b, x - r, y)
          + curveTo(x - r, y - r * b, x - r * b, y - r, x, y - r)
          + curveTo(x + r * b, y - r, x + r, y - r * b, x + r, y);
    }

    /**
     * "RLineTo"
     *
     * @param dx the x difference to the end of the line
     * @param dy the y difference to the end of the line
     * @return The pdf statements
     */
    public String rLineTo(final float dx, final float dy) {
        this.lastX = this.lastX + dx;
        this.lastY = this.lastY + dy;
        return " " + this.lastX + " " + this.lastY + " l\n";
    }

    /**
     * "LineWidth"
     *
     * @param wd width of the line
     * @return The pdf statements
     */
    public String lineWidth(final float wd) {
        return " " + wd + " w\n";
    }

    /**
     * Changes the value of the <VAR>line dash pattern </VAR>. <P>The line
     * dash pattern controls the pattern of dashes and gaps used to stroke
     * paths. It is specified by an <I>array </I> and a <I>phase </I>. The
     * array specifies the length of the alternating dashes and gaps. The phase
     * specifies the distance into the dash pattern to start the dash. <BR>
     *
     * @param phase the value of the phase
     * @return The pdf statements
     */
    public String setLineDash(final float phase) {
        return " [] " + phase + " d\n";
    }

    /**
     * Changes the value of the <VAR>line dash pattern </VAR>. <P>The line
     * dash pattern controls the pattern of dashes and gaps used to stroke
     * paths. It is specified by an <I>array </I> and a <I>phase </I>. The
     * array specifies the length of the alternating dashes and gaps. The phase
     * specifies the distance into the dash pattern to start the dash. <BR>
     *
     * @param phase the value of the phase
     * @param unitsOn the number of units that must be 'on' (equals the number
     * of units that must be 'off').
     * @return The pdf statements
     */
    public String setLineDash(final float unitsOn, final float phase) {
        return "[" + unitsOn + "] " + phase + " d\n";
    }

    /**
     * Changes the value of the <VAR>line dash pattern </VAR>. <P>The line
     * dash pattern controls the pattern of dashes and gaps used to stroke
     * paths. It is specified by an <I>array </I> and a <I>phase </I>. The
     * array specifies the length of the alternating dashes and gaps. The phase
     * specifies the distance into the dash pattern to start the dash. <BR>
     *
     * @param phase the value of the phase
     * @param unitsOn the number of units that must be 'on'
     * @param unitsOff the number of units that must be 'off'#
     * @return The pdf statements
     */
    public String setLineDash(final float unitsOn, final float unitsOff,
      final float phase) {
        return "[" + unitsOn + " " + unitsOff + "] " + phase + " d\n";
    }

    /**
     * "Stroke"
     * @return The pdf statements
     */
    public String stroke() {
        return " S\n";
    }

    /**
     * "Fill"
     * @return The pdf statements
     */
    public String fill() {
        return " f\n";
    }

    /**
     * "ClosePath"
     * @return The pdf statements
     */
    public String closepath() {
        return " h\n";
    }

    /**
     * "CloseStroke"
     * @return The pdf statements
     */
    public String closeStroke() {
        return " s\n";
    }

    /**
     * "CloseFillStroke"
     * @return The pdf statements
     */
    public String closeFillStroke() {
        return " b\n";
    }

    /**
     * "FillStroke"
     * @return The pdf statements
     */
    public String fillStroke() {
        return " B\n";
    }

    /**
     * "CloseFill"
     * @return The pdf statements
     */
    public String closeFill() {
        return " h f\n";
    }

    /**
     * "Concat"
     * @param sx 1. parameter of the matrix
     * @param u  2. parameter of the matrix
     * @param v  3. parameter of the matrix
     * @param sy 4. parameter of the matrix
     * @param tx 5. parameter of the matrix
     * @param ty 6. parameter of the matrix
     * @return The pdf statements
     */
    public String concat(final float sx, final float u, final float v,
      final float sy, final float tx, final float ty) {
        return " " + sx + " " + u + " " + v + " " + sy + " " + tx + " "
          + ty + " cm\n";
    }

    /**
     * "AddRectangle"
     *
     * @param x the x position of left edge
     * @param y the y position of top edge
     * @param wd the width
     * @param ht the height
     * @return The pdf statements
     */
    public String addRectangle(final float x, final float y, final float wd,
      final float ht) {
        this.lastX = x;
        this.lastY = y;
        return " " + x + " " + y + " " + wd + " " + ht + " re\n";
    }

    /**
     * Adds a comment
     *
     * @param s the comment
     * @return The pdf statements
     */
    public String addComment(final String s) {
        return "\n% " + s + "\n";
    }

    /**
     * Adds color for stroking
     *
     * @param col the color
     * @return The pdf statements
     */
    public String strokeColor(final Color col) {
        return applyColor(col, false);
    }

    /**
     * Adds color for filling
     *
     * @param col the color
     * @return The pdf statements
     */
    public String fillColor(final Color col) {
        return applyColor(col, true);
    }

    /**
     * Adds color for filling or stroking
     *
     * @param col the color
     * @param fill fill color if true, else stroke color
     * @see org.apache.fop.svg.PDFGraphics2D#applyColor(java.awt.Color,
     * boolean)
     * @return The pdf statements
     */
    private String applyColor(final Color col, final boolean fill) {
        Color c = col;
        PDFColor currentColor = new PDFColor(0, 0, 0);

        if (c.getColorSpace().getType() == java.awt.color.ColorSpace.TYPE_RGB) {
            currentColor = new PDFColor(c.getRed(), c.getGreen(), c.getBlue());
            return currentColor.getColorSpaceOut(fill);
        } else if (c.getColorSpace().getType() == java.awt.color.ColorSpace.TYPE_CMYK) {
            float[] cComps = c.getColorComponents(new float[3]);
            double[] cmyk = new double[3];
            for (int i = 0; i < 3; i++) {
                // convert the float elements to doubles for pdf
                cmyk[i] = cComps[i];
            }
            currentColor = new PDFColor(cmyk[0], cmyk[1], cmyk[2], cmyk[3]);
            return currentColor.getColorSpaceOut(fill);
        } else if (c.getColorSpace().getType() == java.awt.color.ColorSpace.TYPE_2CLR) {
            // used for black/magentacurrentColor.getColorSpaceOut(fill)
            float[] cComps = c.getColorComponents(new float[1]);
            double[] blackMagenta = new double[1];
            for (int i = 0; i < 1; i++) {
                blackMagenta[i] = cComps[i];
            }
            // currentColor = new PDFColor(blackMagenta[0], blackMagenta[1]);
            return currentColor.getColorSpaceOut(fill);
        } else {
            System.err.println("Color Space not supported.");
            return null;
        }
    }

}
