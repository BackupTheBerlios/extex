/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import java.util.Map;

import org.apache.fop.fonts.FontDescriptor;
import org.apache.fop.fonts.FontType;
import org.apache.fop.fonts.Typeface;

import de.dante.extex.interpreter.type.font.Font;

/**
 * Font-Adapter for a efm-font with FOP.
 *
 * TODO incomplete; Gerd
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class EfmFopFont extends Typeface implements FontDescriptor {

    /**
     * The TeX-Font (efm)
     */
    private Font font;

    /**
     * Create a new Object
     * @param afont  the tex-font-metrik
     */
    public EfmFopFont(final Font afont) {

        font = afont;
    }

    //    /**
    //     * Main constructor
    //     * @param fontEmbedPath path to embeddable file (may be null)
    //     * @param metricsFileName path to the metrics XML file
    //     * @param useKerning True, if kerning should be enabled
    //     */
    //    public EfmFopFont(String fontEmbedPath, String metricsFileName,
    //            boolean useKerning) {
    //
    //        this.metricsFileName = metricsFileName;
    //        this.fontEmbedPath = fontEmbedPath;
    //        this.useKerning = useKerning;
    //    }

    //    private void load() {
    //
    //        if (!isMetricsLoaded) {
    //            isMetricsLoaded = true;
    //            try {
    //                /**@todo Possible thread problem here */
    //
    //                FontReader reader = new FontReader(metricsFileName);
    //                reader.setKerningEnabled(useKerning);
    //                reader.setFontEmbedPath(fontEmbedPath);
    //                realFont = reader.getFont();
    //                if (realFont instanceof FontDescriptor) {
    //                    realFontDescriptor = (FontDescriptor) realFont;
    //                }
    //                // System.out.println("Metrics " + metricsFileName + " loaded.");
    //            } catch (Exception ex) {
    //                ex.printStackTrace();
    //                /**@todo Log this exception */
    //                //log.error("Failed to read font metrics file "
    //                //                     + metricsFileName
    //                //                     + " : " + ex.getMessage());
    //            }
    //        }
    //    }

    /**
     * Gets the real font.
     * @return the real font
     */
    public Typeface getRealFont() {

        return this;
    }

    // ---- Font ----
    /**
     * @see org.apache.fop.fonts.Typeface#getEncoding()
     */
    public String getEncoding() {

        return null;//realFont.getEncoding();
    }

    /**
     * @see org.apache.fop.fonts.Typeface#mapChar(char)
     */
    public char mapChar(char c) {

        return 0;//realFont.mapChar(c);
    }

    /**
     * @see org.apache.fop.fonts.Typeface#hasChar(char)
     */
    public boolean hasChar(char c) {

        return false;//realFont.hasChar(c);
    }

    /**
     * @see org.apache.fop.fonts.Typeface#isMultiByte()
     */
    public boolean isMultiByte() {

        return false;//realFont.isMultiByte();
    }

    // ---- FontMetrics interface ----
    /**
     * @see org.apache.fop.fonts.FontMetrics#getFontName()
     */
    public String getFontName() {

        return font.getFontName();
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getAscender(int)
     */
    public int getAscender(int size) {

        return 0;//realFont.getAscender(size);
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getCapHeight(int)
     */
    public int getCapHeight(int size) {

        return 0;//realFont.getCapHeight(size);
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getDescender(int)
     */
    public int getDescender(int size) {

        return 0;//realFont.getDescender(size);
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getXHeight(int)
     */
    public int getXHeight(int size) {

        return 0;//font.getEx();
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getWidth(int, int)
     */
    public int getWidth(int i, int size) {

        return 0;//realFont.getWidth(i, size);
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getWidths()
     */
    public int[] getWidths() {

        return null;//realFont.getWidths();
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#hasKerningInfo()
     */
    public boolean hasKerningInfo() {

        return false;//realFont.hasKerningInfo();
    }

    /**
     * @see org.apache.fop.fonts.FontMetrics#getKerningInfo()
     */
    public Map getKerningInfo() {

        return null;//realFont.getKerningInfo();
    }

    // ---- FontDescriptor interface ----
    /**
     * @see org.apache.fop.fonts.FontDescriptor#getCapHeight()
     */
    public int getCapHeight() {

        return 0;//realFontDescriptor.getCapHeight();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getDescender()
     */
    public int getDescender() {

        return 0;//realFontDescriptor.getDescender();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getAscender()
     */
    public int getAscender() {

        return 0;//realFontDescriptor.getAscender();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getFlags()
     */
    public int getFlags() {

        return 0;//realFontDescriptor.getFlags();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getFontBBox()
     */
    public int[] getFontBBox() {

        return null;//realFontDescriptor.getFontBBox();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getItalicAngle()
     */
    public int getItalicAngle() {

        return 0;//realFontDescriptor.getItalicAngle();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getStemV()
     */
    public int getStemV() {

        return 0;//realFontDescriptor.getStemV();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#getFontType()
     */
    public FontType getFontType() {

        return null;//realFontDescriptor.getFontType();
    }

    /**
     * @see org.apache.fop.fonts.FontDescriptor#isEmbeddable()
     */
    public boolean isEmbeddable() {

        return true;//realFontDescriptor.isEmbeddable();
    }

}

