/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type.tfm;

import java.io.IOException;
import java.io.Serializable;

import org.jdom.Element;

import de.dante.extex.font.type.PlFormat;
import de.dante.extex.font.type.PlWriter;
import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

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
 * @version $Revision: 1.2 $
 */

public class TFMCharInfoArray implements XMLConvertible, PlFormat, Serializable {

    /**
     * the char info
     */
    private TFMCharInfoWord[] charinfoword;

    /**
     * Create a new object
     * @param rar   the input
     * @param cc    number of character
     * @throws IOException if an IO-error occurs.
     */
    public TFMCharInfoArray(final RandomAccessR rar, final int cc)
            throws IOException {

        charinfoword = new TFMCharInfoWord[cc];
        for (int i = 0; i < cc; i++) {
            charinfoword[i] = new TFMCharInfoWord(rar, i);
        }
    }

    /**
     * Returns the charinfoword.
     * @return Returns the charinfoword.
     */
    public TFMCharInfoWord[] getCharinfoword() {

        return charinfoword;
    }

    /**
     * the width
     */
    private TFMWidthArray width;

    /**
     * the height
     */
    private TFMHeightArray height;

    /**
     * the depth
     */
    private TFMDepthArray depth;

    /**
     * the italic
     */
    private TFMItalicArray italic;

    /**
     * smallest character code in the font
     */
    private short bc;

    /**
     * the exten
     */
    private TFMExtenArray exten;

    /**
     * Lig/kern programs in the final format
     */
    private TFMLigKern[] ligKernTable;

    /**
     * Create the char table
     * @param widtha        the width table
     * @param heighta       the height table
     * @param deptha        the depth table
     * @param italica       the italic table
     * @param extena        the exten table
     * @param abc           the bc
     * @param lk            the ligKernTable
     */
    public void createCharTable(final TFMWidthArray widtha,
            final TFMHeightArray heighta, final TFMDepthArray deptha,
            final TFMItalicArray italica, final TFMExtenArray extena,
            final short abc, final TFMLigKern[] lk) {

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
                TFMCharInfoWord ciw = charinfoword[pos];
                ciw.setWidth(takeDimen(width.getTable(), ciw.getWidthindex(),
                        pos));
                ciw.setHeight(takeDimen(height.getTable(),
                        ciw.getHeightindex(), pos));
                ciw.setDepth(takeDimen(depth.getTable(), ciw.getDepthindex(),
                        pos));
                ciw.setItalic(takeDimen(italic.getTable(),
                        ciw.getItalicindex(), pos));
                if (ciw.getTag() == TFMCharInfoWord.LIST_TAG) {
                    if (validCharList(pos)) {
                        ciw.setNextchar(ciw.getRemainder());
                    }
                } else if (ciw.getTag() == TFMCharInfoWord.EXT_TAG) {
                    if (ciw.getRemainder() < exten.getExtensiblerecipe().length) {
                        TFMExtensibleRecipe er = exten.getExtensiblerecipe()[ciw
                                .getRemainder()];
                        ciw.setTop((er.getTop() != 0)
                                ? er.getTop()
                                : TFMCharInfo.NOCHARCODE);
                        ciw.setMid((er.getMid() != 0)
                                ? er.getMid()
                                : TFMCharInfo.NOCHARCODE);
                        ciw.setBot((er.getBot() != 0)
                                ? er.getBot()
                                : TFMCharInfo.NOCHARCODE);
                        ciw.setRep((er.getRep() != 0)
                                ? er.getRep()
                                : TFMCharInfo.NOCHARCODE);
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

        TFMCharInfoWord ciw = charinfoword[pos];
        short next = ciw.getRemainder();
        if (!charExists(next)) {
            ciw.resetTag();
            return false;
        }
        while ((next -= bc) < pos
                && (ciw = charinfoword[next]).getTag() == TFMCharInfoWord.LIST_TAG) {
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
    private TFMFixWord takeDimen(final TFMFixWord[] table, final short i,
            final int pos) {

        if (i < table.length) {
            return table[i];
        }
        return TFMFixWord.ZERO;
    }

    /**
     * Add all glyphs to the element
     * @param element   the element
     */
    public void addGlyphs(final Element element) {

        for (int i = 0; i < charinfoword.length; i++) {

            // get char
            TFMCharInfoWord ci = charinfoword[i];

            if (ci != null) {

                // create glyph
                Element glyph = new Element("glyph");

                glyph.setAttribute("ID", String.valueOf(i));
                glyph.setAttribute("glyph-number", String.valueOf(i + bc));
                String c = Character.toString((char) i);
                if (c != null && c.trim().length() > 0) {
                    glyph.setAttribute("char", c);
                }
                if (ci.getGlyphname() != null) {
                    element.setAttribute("glyph-name", ci.getGlyphname()
                            .substring(1));
                }
                glyph.setAttribute("width", ci.getWidth().toStringUnits());
                glyph.setAttribute("height", ci.getHeight().toStringUnits());
                glyph.setAttribute("depth", ci.getDepth().toStringUnits());
                glyph.setAttribute("italic", ci.getItalic().toStringUnits());

                // ligature
                int ligstart = ci.getLigkernstart();
                if (ligstart != TFMCharInfo.NOINDEX) {

                    for (int k = ligstart; k != TFMCharInfo.NOINDEX; k = ligKernTable[k]
                            .nextIndex(k)) {
                        TFMLigKern lk = ligKernTable[k];

                        if (lk instanceof TFMLigature) {
                            TFMLigature lig = (TFMLigature) lk;

                            Element ligature = new Element("ligature");

                            ligature.setAttribute("letter-id", String
                                    .valueOf(lig.getNextChar()));
                            String sl = Character.toString((char) lig
                                    .getNextChar());
                            if (sl != null && sl.trim().length() > 0) {
                                ligature.setAttribute("letter", sl.trim());
                            }

                            ligature.setAttribute("lig-id", String.valueOf(lig
                                    .getAddingChar()));
                            String slig = Character.toString((char) lig
                                    .getAddingChar());
                            if (slig != null && slig.trim().length() > 0) {
                                ligature.setAttribute("lig", slig.trim());
                            }
                            glyph.addContent(ligature);
                        } else if (lk instanceof TFMKerning) {
                            TFMKerning kern = (TFMKerning) lk;

                            Element kerning = new Element("kerning");

                            kerning.setAttribute("glyph-id", String
                                    .valueOf(kern.getNextChar()));
                            String sk = Character.toString((char) kern
                                    .getNextChar());
                            if (sk != null && sk.trim().length() > 0) {
                                kerning.setAttribute("char", sk.trim());
                            }
                            kerning.setAttribute("size", kern.getKern()
                                    .toStringUnits());

                            glyph.addContent(kerning);
                        }
                    }
                }
                element.addContent(glyph);
            }
        }
    }

    /**
     * encdoing table
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
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element element = new Element("charinfo");
        for (int i = 0; i < charinfoword.length; i++) {
            element.addContent(charinfoword[i].toXML());
        }
        return element;
    }
}
