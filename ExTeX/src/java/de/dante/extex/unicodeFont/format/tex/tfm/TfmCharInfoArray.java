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

import de.dante.extex.unicodeFont.format.pl.PlFormat;
import de.dante.extex.unicodeFont.format.pl.PlWriter;
import de.dante.util.EFMWriterConvertible;
import de.dante.util.XMLWriterConvertible;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Class for TFM char info.
 *
 * <p>Each character has one char_info_word.</p>
 * <p>Each char_info_word contains six fields packed
 *    into four bytes as follows.</p>
 *
 * <table border="1">
 *   <thead>
 *     <tr><td>byte</td><td>description</td></tr>
 *   </thead>
 *   <tr><td>first  </td><td>width_index (8 bits)</td></tr>
 *   <tr><td>second </td><td>height_index (4 bits) times 16,
 *                           plus depth_index (4 bits)</td></tr>
 *   <tr><td>third  </td><td>italic_index (6 bits) times 4,
 *                           plus tag (2 bits)</td></tr>
 *   <tr><td>fourth </td><td>remainder (8 bits)</td></tr>
 * </table>
 *
 * <p>
 * Information from:
 * The DVI Driver Standard, Level 0
 * The TUG DVI Driver Standards Committee
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class TfmCharInfoArray
        implements
            XMLWriterConvertible,
            EFMWriterConvertible,
            PlFormat,
            Serializable {

    /**
     * the char info.
     */
    private TfmCharInfoWord[] charinfoword;

    /**
     * Create a new object.
     * @param rar   the input
     * @param cc    number of character
     * @throws IOException if an IO-error occurs.
     */
    public TfmCharInfoArray(final RandomAccessR rar, final int cc)
            throws IOException {

        charinfoword = new TfmCharInfoWord[cc];
        for (int i = 0; i < cc; i++) {
            charinfoword[i] = new TfmCharInfoWord(rar, i);
        }
    }

    /**
     * Returns the charinfoword.
     * @return Returns the charinfoword.
     */
    public TfmCharInfoWord[] getCharinfoword() {

        return charinfoword;
    }

    /**
     * the width.
     */
    private TfmWidthArray width;

    /**
     * the height.
     */
    private TfmHeightArray height;

    /**
     * the depth.
     */
    private TfmDepthArray depth;

    /**
     * the italic.
     */
    private TfmItalicArray italic;

    /**
     * smallest character code in the font.
     */
    private short bc;

    /**
     * the exten.
     */
    private TfmExtenArray exten;

    /**
     * Lig/kern programs in the final format.
     */
    private TfmLigKern[] ligKernTable;

    /**
     * Create the char table.
     * @param widtha        the width table
     * @param heighta       the height table
     * @param deptha        the depth table
     * @param italica       the italic table
     * @param extena        the exten table
     * @param abc           the bc
     * @param lk            the ligKernTable
     */
    public void createCharTable(final TfmWidthArray widtha,
            final TfmHeightArray heighta, final TfmDepthArray deptha,
            final TfmItalicArray italica, final TfmExtenArray extena,
            final short abc, final TfmLigKern[] lk) {

        width = widtha;
        height = heighta;
        depth = deptha;
        italic = italica;
        bc = abc;
        exten = extena;
        ligKernTable = lk;

        for (int pos = 0; pos < charinfoword.length; pos++) {
            charinfoword[pos].setLigKernTable(ligKernTable);
            charinfoword[pos].setBc(bc);
            if (charinfoword[pos].exists()) {
                TfmCharInfoWord ciw = charinfoword[pos];
                ciw.setWidth(takeDimen(width.getTable(), ciw.getWidthindex(),
                        pos));
                ciw.setHeight(takeDimen(height.getTable(),
                        ciw.getHeightindex(), pos));
                ciw.setDepth(takeDimen(depth.getTable(), ciw.getDepthindex(),
                        pos));
                ciw.setItalic(takeDimen(italic.getTable(),
                        ciw.getItalicindex(), pos));
                if (ciw.getTag() == TfmCharInfoWord.LIST_TAG) {
                    if (validCharList(pos)) {
                        ciw.setNextchar(ciw.getRemainder());
                    }
                } else if (ciw.getTag() == TfmCharInfoWord.EXT_TAG) {
                    if (ciw.getRemainder() < exten.getExtensiblerecipe().length) {
                        TfmExtensibleRecipe er = exten.getExtensiblerecipe()[ciw
                                .getRemainder()];
                        ciw.setTop((er.getTop() != 0)
                                ? er.getTop()
                                : TfmCharInfoWord.NOCHARCODE);
                        ciw.setMid((er.getMid() != 0)
                                ? er.getMid()
                                : TfmCharInfoWord.NOCHARCODE);
                        ciw.setBot((er.getBot() != 0)
                                ? er.getBot()
                                : TfmCharInfoWord.NOCHARCODE);
                        ciw.setRep((er.getRep() != 0)
                                ? er.getRep()
                                : TfmCharInfoWord.NOCHARCODE);
                    }
                }
            }
        }
    }

    /**
     * Checks the consistency of larger character chain. It checks only the
     * characters which have less position in |charTable| then the given
     * character position and are supossed to have the corresponding <code>CharInfo</code>
     * already created.
     *
     * @param pos position of currently processed character in <code>charTable</code>.
     * @return <code>true</code> if the associated chain is consistent.
     */
    private boolean validCharList(final int pos) {

        TfmCharInfoWord ciw = charinfoword[pos];
        short next = ciw.getRemainder();
        if (!charExists(next)) {
            ciw.resetTag();
            return false;
        }
        while ((next -= bc) < pos
                && (ciw = charinfoword[next]).getTag() == TfmCharInfoWord.LIST_TAG) {
            next = ciw.getRemainder();
        }
        return true;
    }

    /**
     * Check the existence of particular character in the font.
     *
     * @param pos   the checked character code.
     * @return <code>true</code> if the character is present.
     */
    private boolean charExists(final short pos) {

        int c = pos - bc;
        return (c >= 0 && c < charinfoword.length && charinfoword[c].exists());
    }

    /**
     * Gets referenced character dimension from apropriate table.
     *
     * @param table     referenced table of dimensions.
     * @param i         referenced index to the dimension table.
     * @param pos       the position of character in <code>charTable</code> for
     *                  error messages.
     * @return Returns the FixWord
     */
    private TfmFixWord takeDimen(final TfmFixWord[] table, final short i,
            final int pos) {

        if (i < table.length) {
            return table[i];
        }
        return TfmFixWord.ZERO;
    }

    /**
     * @see de.dante.util.EFMWriterConvertible#writeEFM(de.dante.util.xml.XMLStreamWriter)
     */
    public void writeEFM(final XMLStreamWriter writer) throws IOException {

        for (int i = 0; i < charinfoword.length; i++) {

            // get char
            TfmCharInfoWord ci = charinfoword[i];

            if (ci != null) {

                // create glyph
                writer.writeStartElement("glyph");

                writer.writeAttribute("ID", String.valueOf(i));
                writer.writeAttribute("glyph-number", String.valueOf(i + bc));
                String c = Character.toString((char) i);
                if (c != null && c.trim().length() > 0) {
                    writer.writeAttribute("char", c);
                }
                if (ci.getGlyphname() != null) {
                    writer.writeAttribute("glyph-name", ci.getGlyphname()
                            .replaceAll("/", ""));
                }
                writer.writeAttribute("width", String.valueOf(ci.getWidth()
                        .getValue()));
                writer.writeAttribute("height", String.valueOf(ci.getHeight()
                        .getValue()));
                writer.writeAttribute("depth", String.valueOf(ci.getDepth()
                        .getValue()));
                writer.writeAttribute("italic", String.valueOf(ci.getItalic()
                        .getValue()));

                // ligature
                int ligstart = ci.getLigkernstart();
                if (ligstart != TfmCharInfoWord.NOINDEX) {

                    for (int k = ligstart; k != TfmCharInfoWord.NOINDEX; k = ligKernTable[k]
                            .nextIndex(k)) {
                        TfmLigKern lk = ligKernTable[k];

                        if (lk instanceof TfmLigature) {
                            TfmLigature lig = (TfmLigature) lk;

                            writer.writeStartElement("ligature");
                            writer.writeAttribute("letter-id", String
                                    .valueOf(lig.getNextChar()));
                            String sl = Character.toString((char) lig
                                    .getNextChar());
                            if (sl != null && sl.trim().length() > 0) {
                                writer.writeAttribute("letter", sl.trim());
                            }

                            writer.writeAttribute("lig-id", String.valueOf(lig
                                    .getAddingChar()));
                            String slig = Character.toString((char) lig
                                    .getAddingChar());
                            if (slig != null && slig.trim().length() > 0) {
                                writer.writeAttribute("lig", slig.trim());
                            }
                            writer.writeEndElement();
                        } else if (lk instanceof TfmKerning) {
                            TfmKerning kern = (TfmKerning) lk;

                            writer.writeStartElement("kerning");

                            writer.writeAttribute("glyph-id", String
                                    .valueOf(kern.getNextChar()));
                            String sk = Character.toString((char) kern
                                    .getNextChar());
                            if (sk != null && sk.trim().length() > 0) {
                                writer.writeAttribute("char", sk.trim());
                            }
                            writer.writeAttribute("size", String.valueOf(kern
                                    .getKern().getValue()));

                            writer.writeEndElement();
                        }
                    }
                }
                writer.writeEndElement();
            }
        }
    }

    /**
     * encdoing table.
     */
    private String[] enctable;

    /**
     * Set the encdoing table.
     * @param et    the encoding table
     */
    public void setEncodingTable(final String[] et) {

        enctable = et;
        if (enctable != null) {
            for (int i = 0; i < charinfoword.length; i++) {
                if (i < enctable.length) {
                    charinfoword[i].setGlyphname(enctable[i]);
                }
            }
        }
    }

    /**
     * Returns the charinfoword for the character.
     * @param i the position of the character
     * @return Returns the charinfoword for the character.
     */
    public TfmCharInfoWord getCharInfoWord(final int i) {

        if (i >= 0 && i < charinfoword.length) {
            return charinfoword[i];
        }
        return null;
    }

    /**
     * @see de.dante.extex.font.type.PlFormat#toPL(de.dante.extex.font.type.PlWriter)
     */
    public void toPL(final PlWriter out) throws IOException {

        for (int i = 0; i < charinfoword.length; i++) {
            if (charinfoword[i] != null) {
                out.plopen("CHARACTER").addChar((short) (i + bc));
                charinfoword[i].toPL(out);
                out.plclose();
            }
        }
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(XMLStreamWriter writer) throws IOException {

        writer.writeStartElement("charinfo");
        for (int i = 0; i < charinfoword.length; i++) {
            charinfoword[i].writeXML(writer);
        }
        writer.writeEndElement();
    }

    /**
     * Returns the depth.
     * @return Returns the depth.
     */
    public TfmDepthArray getDepth() {

        return depth;
    }

    /**
     * Returns the height.
     * @return Returns the height.
     */
    public TfmHeightArray getHeight() {

        return height;
    }

    /**
     * Returns the italic.
     * @return Returns the italic.
     */
    public TfmItalicArray getItalic() {

        return italic;
    }

    /**
     * Returns the width.
     * @return Returns the width.
     */
    public TfmWidthArray getWidth() {

        return width;
    }
}
