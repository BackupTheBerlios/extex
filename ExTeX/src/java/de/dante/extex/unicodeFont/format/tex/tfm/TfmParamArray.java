/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.tex.tfm;

import java.io.IOException;
import java.io.Serializable;

import org.jdom.Element;

import de.dante.extex.unicodeFont.format.pl.PlFormat;
import de.dante.extex.unicodeFont.format.pl.PlWriter;
import de.dante.util.EFMWriterConvertible;
import de.dante.util.XMLWriterConvertible;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Class for TFM parameter table.
 *
 * <p>parameter : array [0 .. (np-1)] of fix word</p>
 *
 * <p>
 * Information from:
 * The DVI Driver Standard, Level 0
 * The TUG DVI Driver Standards Committee
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public class TfmParamArray
        implements
            XMLWriterConvertible,
            EFMWriterConvertible,
            PlFormat,
            Serializable {

    /**
     * the param table.
     */
    private TfmFixWord[] table;

    /**
     * the font type.
     */
    private TfmFontType fonttpye;

    /**
     * Create a new object.
     *
     * @param rar   the input
     * @param size  number of words in the table
     * @param ft    the font type
     * @throws IOException if an IO-error occurs.
     */
    public TfmParamArray(final RandomAccessR rar, final int size,
            final TfmFontType ft) throws IOException {

        fonttpye = ft;

        table = new TfmFixWord[size];

        for (int i = 0; i < size; i++) {
            table[i] = new TfmFixWord(rar.readInt(),
                    TfmFixWord.FIXWORDDENOMINATOR);
        }
    }

    /**
     * Returns the table.
     * @return Returns the table.
     */
    public TfmFixWord[] getTable() {

        return table;
    }

    /**
     * Labels for VANILLA.
     */
    public static final String[] LABEL_VANILLA = {"SLANT", "SPACE", "STRETCH",
            "SHRINK", "XHEIGHT", "QUAD", "EXTRASPACE"};

    /**
     * Labels for MATHSY.
     */
    public static final String[] LABEL_MATHSY = {"SLANT", "SPACE", "STRETCH",
            "SHRINK", "XHEIGHT", "QUAD", "EXTRASPACE", "NUM1", "NUM2", "NUM3",
            "DENOM1", "DENOM2", "SUP1", "SUP2", "SUP3", "SUB1", "SUB2",
            "SUPDROP", "SUBDROP", "DELIM1", "DELIM2", "AXISHEIGHT"};

    /**
     * Labels for MATHEX.
     */
    public static final String[] LABEL_MATHEX = {"SLANT", "SPACE", "STRETCH",
            "SHRINK", "XHEIGHT", "QUAD", "EXTRASPACE", "DEFAULTRULETHICKNESS",
            "BIGOPSPACING1", "BIGOPSPACING2", "BIGOPSPACING3", "BIGOPSPACING4",
            "BIGOPSPACING5"};

    /**
     * Returns the length of the parameter array.
     * @return Returns the length of the parameter array.
     */
    public int getLength() {

        return table.length;
    }

    /**
     * Returns the parameter with the index <code>i</code>.
     * @param i The index in the array.
     * @return Returns the parameter with the index <code>i</code>.
     */
    public TfmFixWord getParam(final int i) {

        if (i < table.length) {
            return table[i];
        }
        return TfmFixWord.ZERO;
    }

    /**
     * Returns the parameter with the name <code>name</code>.
     * @param name  The name of the parameter.
     * @return Returns the parameter with the name <code>name</code>.
     */
    public TfmFixWord getParam(final String name) {

        if (name != null) {
            String[] labels = null;
            if (fonttpye.getType() == TfmFontType.MATHEX) {
                labels = LABEL_MATHEX;
            } else if (fonttpye.getType() == TfmFontType.MATHSY) {
                labels = LABEL_MATHSY;
            } else {
                labels = LABEL_VANILLA;
            }
            for (int i = 0; i < labels.length; i++) {
                if (name.equals(labels[i])) {
                    return table[i];
                }
            }
        }
        return TfmFixWord.ZERO;
    }

    /**
     * Returns the label of the parameter, or a empty string,
     * if no label name exits.
     * @param id    the id
     * @return Returns the label of the parameter.
     */
    public String getLabelName(final int id) {

        String label = "";
        String[] labels = null;
        if (fonttpye.getType() == TfmFontType.MATHEX) {
            labels = LABEL_MATHEX;
        } else if (fonttpye.getType() == TfmFontType.MATHSY) {
            labels = LABEL_MATHSY;
        } else {
            labels = LABEL_VANILLA;
        }
        if (id >= 0 && id < labels.length) {
            label = labels[id];
        }
        return label;
    }

    /**
     * Add the parameter to the element.
     * @param element   the element
     */
    public void addParam(final Element element) {

        for (int i = 0; i < table.length; i++) {
            String name = getLabelName(i);
            if (name.length() != 0) {
                element.setAttribute(name, table[i].toStringComma());
            }
        }
    }

    /**
     * @see de.dante.extex.font.type.PlFormat#toPL(de.dante.extex.font.type.PlWriter)
     */
    public void toPL(final PlWriter out) throws IOException {

        if (table.length > 0) {
            out.plopen("FONTDIMEN");
            for (int i = 0; i < table.length; i++) {
                String name = getLabelName(i);
                if (name != null && name.length() > 0) {
                    out.plopen(name);
                } else {
                    out.plopen("PARAMETER").addDec(i + 1);
                }
                out.addReal(table[i]).plclose();
            }
            out.plclose();
        }
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writer.writeStartElement("params");
        for (int i = 0; i < table.length; i++) {
            writer.writeStartElement("param");
            writer.writeAttribute("id", String.valueOf(i));
            String name = getLabelName(i);
            if (name.length() != 0) {
                writer.writeAttribute("name", name);
            }
            writer.writeAttribute("value_fw", String.valueOf(table[i]
                    .getValue()));
            writer.writeAttribute("value", table[i].toStringComma());
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    /**
     * @see de.dante.util.EFMWriterConvertible#writeEFM(de.dante.util.xml.XMLStreamWriter)
     */
    public void writeEFM(final XMLStreamWriter writer) throws IOException {

        writer.writeStartElement("fontdimen");
        for (int i = 0; i < table.length; i++) {
            String name = getLabelName(i);
            if (name.length() != 0) {
                writer
                        .writeAttribute(name, String.valueOf(table[i]
                                .getValue()));
            }
        }
        writer.writeEndElement();
    }
}
