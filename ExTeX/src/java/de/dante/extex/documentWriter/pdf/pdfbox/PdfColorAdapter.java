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

import org.pdfbox.pdmodel.edit.PDPageContentStream;

import de.dante.extex.color.model.CmykColor;
import de.dante.extex.color.model.GrayscaleColor;
import de.dante.extex.color.model.RgbColor;
import de.dante.extex.documentWriter.pdf.exception.DocumentWriterPdfBoxColorException;
import de.dante.extex.documentWriter.pdf.exception.DocumentWriterPdfException;
import de.dante.extex.documentWriter.pdf.exception.DocumentWriterPdfNotSupportedColorException;
import de.dante.extex.interpreter.context.Color;

/**
 * Coloradapter for PDFBox.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public final class PdfColorAdapter {

    /**
     * no instance
     */
    private PdfColorAdapter() {

    }

    /**
     * div for 16-bit to 8 bit
     */
    private static final int DIV = 0xff;

    /**
     * div for 16-bit to 0.0-1.0
     */
    private static final double DIVD = 0xffff;

    /**
     * Set the Color (RGB) to the content (without alpha).
     * ExTeX use 16 bit for the color, PDF only 8!
     *
     * @param contentstream The pdf content.
     * @param color         The ExTeX color
     * @throws DocumentWriterPdfException if an error occurs.
     */
    public static void setColor(final PDPageContentStream contentstream,
            final RgbColor color) throws DocumentWriterPdfException {

        try {
            contentstream.setStrokingColor(color.getRed() / DIV, color
                    .getGreen()
                    / DIV, color.getBlue() / DIV);
        } catch (Exception e) {
            throw new DocumentWriterPdfBoxColorException(e);
        }
    }

    /**
     * Set the Color (CMYK) to the content (without alpha).
     * ExTeX use 16 bit for the color, PDF only 8!
     *
     * @param contentstream The pdf content.
     * @param color         The ExTeX color
     * @throws DocumentWriterPdfException if an error occurs.
     */
    public static void setColor(final PDPageContentStream contentstream,
            final CmykColor color) throws DocumentWriterPdfException {

        try {
            contentstream.setStrokingColor(color.getCyan() / DIVD, color
                    .getMagenta()
                    / DIVD, color.getYellow() / DIVD, color.getBlack() / DIVD);
        } catch (Exception e) {
            throw new DocumentWriterPdfBoxColorException(e);
        }
    }

    /**
     * Set the Color (Gray) to the content (without alpha).
     * ExTeX use 16 bit for the color, PDF only 8!
     *
     * @param contentstream The pdf content.
     * @param color         The ExTeX color
     * @throws DocumentWriterPdfException if an error occurs.
     */
    public static void setColor(final PDPageContentStream contentstream,
            final GrayscaleColor color) throws DocumentWriterPdfException {

        try {
            contentstream.setStrokingColor(color.getGray() / DIVD);
        } catch (Exception e) {
            throw new DocumentWriterPdfBoxColorException(e);
        }
    }

    /**
     * Set the Color.
     *
     * @param contentstream The pdf content.
     * @param color         The ExTeX color
     * @throws DocumentWriterPdfException if an error occurs.
     */
    public static void setColor(final PDPageContentStream contentstream,
            final Color color) throws DocumentWriterPdfException {

        if (color instanceof RgbColor) {
            setColor(contentstream, (RgbColor) color);
        } else if (color instanceof CmykColor) {
            setColor(contentstream, (CmykColor) color);
        } else if (color instanceof GrayscaleColor) {
            setColor(contentstream, (GrayscaleColor) color);
        } else {

            throw new DocumentWriterPdfNotSupportedColorException(
                    "Color not supported!");
        }
    }
}
