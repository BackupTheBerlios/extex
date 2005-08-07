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

package de.dante.extex.documentWriter.pdf;

import org.pdfbox.pdmodel.edit.PDPageContentStream;

import de.dante.extex.color.ColorVisitor;
import de.dante.extex.color.model.CmykColor;
import de.dante.extex.color.model.GrayscaleColor;
import de.dante.extex.color.model.HsvColor;
import de.dante.extex.color.model.RgbColor;
import de.dante.extex.documentWriter.pdf.exception.DocumentWriterPdfBoxColorException;
import de.dante.util.GeneralException;

/**
 * Color visitor for pdf.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class PdfColorVisitor implements ColorVisitor {

    /**
     * div for 16-bit to 8 bit
     */
    private static final int DIV = 0xff;

    /**
     * div for 16-bit to 0.0-1.0
     */
    private static final double DIVD = 0xffff;

    /**
     * @see de.dante.extex.color.ColorVisitor#visitCmyk(
     *      de.dante.extex.color.model.CmykColor, java.lang.Object)
     */
    public Object visitCmyk(final CmykColor color, final Object value)
            throws GeneralException {

        try {

            PDPageContentStream contentstream = (PDPageContentStream) value;
            contentstream.setStrokingColor(color.getCyan() / DIVD, color
                    .getMagenta()
                    / DIVD, color.getYellow() / DIVD, color.getBlack() / DIVD);

        } catch (Exception e) {
            throw new DocumentWriterPdfBoxColorException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.color.ColorVisitor#visitGray(
     *      de.dante.extex.color.model.GrayscaleColor, java.lang.Object)
     */
    public Object visitGray(final GrayscaleColor color, final Object value)
            throws GeneralException {

        try {

            PDPageContentStream contentstream = (PDPageContentStream) value;
            contentstream.setStrokingColor(color.getGray() / DIVD);

        } catch (Exception e) {
            throw new DocumentWriterPdfBoxColorException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.color.ColorVisitor#visitHsv(
     *      de.dante.extex.color.model.HsvColor, java.lang.Object)
     */
    public Object visitHsv(final HsvColor color, final Object value)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.color.ColorVisitor#visitRgb(
     *      de.dante.extex.color.model.RgbColor, java.lang.Object)
     */
    public Object visitRgb(final RgbColor color, final Object value)
            throws GeneralException {

        try {

            PDPageContentStream contentstream = (PDPageContentStream) value;
            contentstream.setStrokingColor(color.getRed() / DIV, color
                    .getGreen()
                    / DIV, color.getBlue() / DIV);

        } catch (Exception e) {
            throw new DocumentWriterPdfBoxColorException(e);
        }
        return null;
    }
}
