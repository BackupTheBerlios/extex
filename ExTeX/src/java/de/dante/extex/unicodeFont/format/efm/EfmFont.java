/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.efm;

import java.io.IOException;
import java.io.OutputStream;

import de.dante.extex.unicodeFont.format.tex.tfm.TfmReader;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Class to handle efm font metrics.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class EfmFont {

    /**
     * The metric.
     */
    private EfmMetric metric;

    /**
     * The font name.
     */
    private String fontName;

    /**
     * The external font file, e.g. cmr10.pfb
     */
    private String exFontFile;

    /**
     * tThe design size.
     */
    private double designSize = 10.0d;

    /**
     * Create a new object.
     *
     * @param tfm      The tfm metric.
     */
    public EfmFont(final TfmReader tfm) {

        metric = new EfmMetric(tfm);
        fontName = tfm.getFontname();
        exFontFile = tfm.getPfbfilename();
        designSize = tfm.getDesignSizeAsDouble();
    }

    /**
     * Write the data to a efm file.
     * @param out       The output.
     * @param encoding  The encoding for the xml file.
     * @throws IOException if an IO-error occurred.
     */
    public void write(final OutputStream out, final String encoding)
            throws IOException {

        XMLStreamWriter writer = new XMLStreamWriter(out, encoding);
        writer.setBeauty(true);

        writer.writeStartDocument();
        writer.writeStartElement("efm");
        writer.writeAttribute("fontname", fontName);
        writer.writeAttribute("externalfontfile", exFontFile);
        writer.writeAttribute("designsize", designSize);

        metric.write(writer);

        writer.writeEndElement();
        writer.writeEndDocument();
        writer.close();

    }
}
