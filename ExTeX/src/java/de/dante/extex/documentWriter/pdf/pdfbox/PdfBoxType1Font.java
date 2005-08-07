/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package de.dante.extex.documentWriter.pdf.pdfbox;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pdfbox.afmparser.AFMParser;
import org.pdfbox.afmtypes.CharMetric;
import org.pdfbox.afmtypes.FontMetric;
import org.pdfbox.encoding.AFMEncoding;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.common.PDRectangle;
import org.pdfbox.pdmodel.common.PDStream;
import org.pdfbox.pdmodel.font.PDFont;
import org.pdfbox.pdmodel.font.PDFontDescriptor;
import org.pdfbox.pdmodel.font.PDFontDescriptorDictionary;
import org.pdfbox.pdmodel.font.PDType1Font;
import org.pdfbox.pfbparser.PfbParser;

import de.dante.extex.font.FountKey;
import de.dante.extex.interpreter.type.font.Font;

/**
 * Type1 font with ExTeX values.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class PdfBoxType1Font extends PDType1Font {

    /**
     * Returns a instance for the font.
     *
     * @param doc   the PDDocument.
     * @param font  the extex font.
     * @return Returns the instance for the font.
     * @throws IOException if an IO-error occurred.
     */
    public static PDFont getInstance(final PDDocument doc, final Font font)
            throws IOException {

        FountKey key = font.getFontKey();
        PDFont pdfont = (PDFont) map.get(key);
        if (pdfont == null) {
            pdfont = new PdfBoxType1Font(doc, font);
            map.put(key, pdfont);
        }

        return pdfont;
    }

    /**
     * the map for the fonts
     */
    private static Map map = new HashMap();

    /**
     * Create a new object.
     *
     * @param doc   the PDDocument.
     * @param font  the extex font
     */
    private PdfBoxType1Font(final PDDocument doc, final Font font)
            throws IOException {

        fd = new PDFontDescriptorDictionary();
        setFontDescriptor(fd);

        //        // read the pfb 
        //        PfbParser pfbparser = new PfbParser(pfb);
        //        pfb.close();
        //
        //        PDStream fontStream = new PDStream(doc, pfbparser.getInputStream(),
        //                false);
        //        fontStream.getStream().setInt("Length", pfbparser.size());
        //        for (int i = 0; i < pfbparser.getLengths().length; i++) {
        //            fontStream.getStream().setInt("Length" + (i + 1),
        //                    pfbparser.getLengths()[i]);
        //        }
        //        fontStream.addCompression();
        //        fd.setFontFile(fontStream);
        //
        //        // read the afm
        //        AFMParser parser = new AFMParser(afm);
        //        parser.parse();
        //        metric = parser.getResult();
        //        setEncoding(new AFMEncoding(metric));
        //
        //        // set the values
        //        setBaseFont(metric.getFontName());
        //        fd.setFontName(metric.getFontName());
        //        fd.setFontFamily(metric.getFamilyName());
        //        fd.setNonSymbolic(true);
        //        fd.setFontBoundingBox(new PDRectangle(metric.getFontBBox()));
        //        fd.setItalicAngle(metric.getItalicAngle());
        //        fd.setAscent(metric.getAscender());
        //        fd.setDescent(metric.getDescender());
        //        fd.setCapHeight(metric.getCapHeight());
        //        fd.setXHeight(metric.getXHeight());
        //        fd.setAverageWidth(metric.getAverageCharacterWidth());
        //        fd.setCharacterSet(metric.getCharacterSet());
        //
        //        // use encoding ?
        //        if (encvec == null) {
        //            // no reencoding
        //
        //            // get firstchar, lastchar
        //            int firstchar = 255;
        //            int lastchar = 0;
        //
        //            // widths
        //            List listmetric = metric.getCharMetrics();
        //
        //            int maxWidths = 256;
        //            List widths = new ArrayList(maxWidths);
        //            Integer zero = new Integer(0);
        //            Iterator iter = listmetric.iterator();
        //            while (iter.hasNext()) {
        //                CharMetric m = (CharMetric) iter.next();
        //                int n = m.getCharacterCode();
        //                if (n > 0) {
        //                    firstchar = Math.min(firstchar, n);
        //                    lastchar = Math.max(lastchar, n);
        //                    if (m.getWx() > 0) {
        //                        float width = m.getWx();
        //                        widths.add(new Float(width));
        //                    } else {
        //                        widths.add(zero);
        //                    }
        //                }
        //            }
        //            setFirstChar(firstchar);
        //            setLastChar(lastchar);
        //            setWidths(widths);
        //        } else {
        //            // use reencoding
        //
        //            // widths
        //            List listmetric = metric.getCharMetrics();
        //            int maxWidths = 256;
        //            List widths = new ArrayList(maxWidths);
        //            Integer zero = new Integer(0);
        //
        //            // fill with zero
        //            for (int i = 0; i < encvec.length; i++) {
        //                if (encvec[i] != null) {
        //                    float w = getWithGlypName(encvec[i], listmetric);
        //                    widths.add(new Float(w));
        //                } else {
        //                    widths.add(zero);
        //                }
        //            }
        //            setFirstChar(0);
        //            setLastChar(255);
        //            setWidths(widths);
        //        }

    }

    /**
     * The font descriptor
     */
    private PDFontDescriptorDictionary fd;

    /**
     * the font metric
     */
    private FontMetric metric;

    /**
     * @see org.pdfbox.pdmodel.font.PDSimpleFont#getFontDescriptor()
     */
    public PDFontDescriptor getFontDescriptor() throws IOException {

        return fd;
    }
}
