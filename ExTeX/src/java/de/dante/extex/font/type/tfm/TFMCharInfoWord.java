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
 * Class for TFM char info word.
 *
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
 * The tag field has four values that explain how to
 * interpret the remainder field.
 * </p>
 *
 * <table border="1">
 *   <thead>
 *     <tr><td>tag</td><td>description</td></tr>
 *   </thead>
 *   <tr><td>0  </td><td>no_tag: means that remainder is unused.</td></tr>
 *   <tr><td>1  </td><td>lig_tag: means that this character has a
 *                       ligature/kerning program starting at
 *                       lig_kern[remainder].</td></tr>
 *   <tr><td>2  </td><td>list_tag: means that this character is
 *                       part of a chain of characters of ascending sizes,
 *                       and not the largest in the chain.
 *                       The remainder field gives the character code of
 *                       the next larger character.</td></tr>
 *   <tr><td>3  </td><td>ext_tag: means that this character code
 *                       represents an extensible character, i.e.,
 *                       a character that is built up of smaller pieces
 *                       so that it can be made arbitrarily large.
 *                       The pieces are specified in exten[remainder].</td></tr>
 * </table>
 *
 * <p>
 * Information from:
 * The DVI Driver Standard, Level 0
 * The TUG DVI Driver Standards Committee
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */

public class TFMCharInfoWord implements XMLConvertible, PlFormat, Serializable {

    /**
     * no_tag: vanilla character
     */
    public static final Tag NO_TAG = new Tag();

    /**
     * no_tag: 0
     */
    private static final int TAG0 = 0;

    /**
     * lig_tag: character has a ligature/kerning program
     */
    public static final Tag LIG_TAG = new Tag();

    /**
     * no_tag: 1
     */
    private static final int TAG1 = 1;

    /**
     * list_tag: character has a successor in a charlist
     */
    public static final Tag LIST_TAG = new Tag();

    /**
     * no_tag: 2
     */
    private static final int TAG2 = 2;

    /**
     * ext_tag: character is extensible
     */
    public static final Tag EXT_TAG = new Tag();

    /**
     * no_tag: 3
     */
    private static final int TAG3 = 3;

    /**
     * the width index
     */
    private short widthindex;

    /**
     * the height index
     */
    private short heightindex;

    /**
     * the depth index
     */
    private short depthindex;

    /**
     * the italic index
     */
    private short italicindex;

    /**
     * the tag (as number)
     */
    private short tag;

    /**
     * the tag
     */
    private Tag tagT;

    /**
     * the remainder
     */
    private short remainder;

    /**
     * the char id
     */
    private int charid;

    /**
     * smallest character code in the font
     */
    private short bc;

    /**
     * Create a new object
     * @param rar   the input
     * @param id    the id
     * @throws IOException if an IO-error occurs.
     */
    public TFMCharInfoWord(final RandomAccessR rar, final int id)
            throws IOException {

        charid = id;
        widthindex = (short) rar.readByteAsInt();
        short heightdepthindex = (short) rar.readByteAsInt();
        heightindex = (short) (heightdepthindex >> TFMConstants.CONST_4 & TFMConstants.CONST_X0F);
        depthindex = (short) (heightdepthindex & TFMConstants.CONST_X0F);
        short italicindextag = (short) rar.readByteAsInt();
        italicindex = (short) (italicindextag >> 2 & TFMConstants.CONST_X3F);
        tag = (short) (italicindextag & TFMConstants.CONST_X03);
        remainder = (short) rar.readByteAsInt();

        switch (tag) {
            case TAG0 :
                tagT = NO_TAG;
                break;
            case TAG1 :
                tagT = LIG_TAG;
                break;
            case TAG2 :
                tagT = LIST_TAG;
                break;
            case TAG3 :
                tagT = EXT_TAG;
                break;
            default :
                // not defined: use no_tag
                tagT = NO_TAG;
        }
    }

    /**
     * Symbolic constant for nonexistent character code
     */
    public static final short NOCHARCODE = -1;

    /**
     * Symbolic constant for index which is not valid
     */
    public static final int NOINDEX = -1;

    /**
     * Character width
     */
    private TFMFixWord width = TFMFixWord.ZERO;

    /**
     * Character height
     */
    private TFMFixWord height = TFMFixWord.ZERO;

    /**
     * Character depth
     */
    private TFMFixWord depth = TFMFixWord.ZERO;

    /**
     * Character italic correction
     */
    private TFMFixWord italic = TFMFixWord.ZERO;

    /**
     * Index to newly created ligKernTable which is set
     * during translation of the original raw lig/kern table
     * in the tfm file.
     */
    private int ligkernstart = NOINDEX;

    /**
     * Next larger character code
     */
    private short nextchar = NOINDEX;

    /**
     * top part chracter code
     */
    private short top = NOCHARCODE;

    /**
     * middle part chracter code
     */
    private short mid = NOCHARCODE;

    /**
     * bottom part chracter code
     */
    private short bot = NOCHARCODE;

    /**
     * repeatable part chracter code
     */
    private short rep = NOCHARCODE;

    /**
     * Tag (type-safe class)
     */
    private static final class Tag {

        /**
         * Creates a new object.
         */
        public Tag() {

            super();
        }
    }

    /**
     * Returns the charid.
     * @return Returns the charid.
     */
    public int getCharid() {

        return charid;
    }

    /**
     * Returns the depthindex.
     * @return Returns the depthindex.
     */
    public short getDepthindex() {

        return depthindex;
    }

    /**
     * Returns the heightindex.
     * @return Returns the heightindex.
     */
    public short getHeightindex() {

        return heightindex;
    }

    /**
     * Returns the italicindex.
     * @return Returns the italicindex.
     */
    public short getItalicindex() {

        return italicindex;
    }

    /**
     * Returns the remainder.
     * @return Returns the remainder.
     */
    public short getRemainder() {

        return remainder;
    }

    /**
     * Returns the tag as number.
     * @return Returns the tag as number.
     */
    public short getTagNumber() {

        return tag;
    }

    /**
     * Returns the tag.
     * @return Returns the tag.
     */
    public Tag getTag() {

        return tagT;
    }

    /**
     * Returns the widthindex.
     * @return Returns the widthindex.
     */
    public short getWidthindex() {

        return widthindex;
    }

    /**
     * Returns the ligkernstart.
     * @return Returns the ligkernstart.
     */
    public int getLigkernstart() {

        return ligkernstart;
    }

    /**
     * Set the ligkernstart.
     * @param ligkerns  The ligkernstart to set.
     */
    public void setLigkernstart(final int ligkerns) {

        ligkernstart = ligkerns;
    }

    /**
     * Returns the bot.
     * @return Returns the bot.
     */
    public short getBot() {

        return bot;
    }

    /**
     * Set the bot.
     * @param abot The bot to set.
     */
    public void setBot(final short abot) {

        bot = abot;
    }

    /**
     * Returns the depth.
     * @return Returns the depth.
     */
    public TFMFixWord getDepth() {

        return depth;
    }

    /**
     * Det the depth.
     * @param adepth The depth to set.
     */
    public void setDepth(final TFMFixWord adepth) {

        depth = adepth;
    }

    /**
     * Returns the height.
     * @return Returns the height.
     */
    public TFMFixWord getHeight() {

        return height;
    }

    /**
     * Set the height.
     * @param aheight The height to set.
     */
    public void setHeight(final TFMFixWord aheight) {

        height = aheight;
    }

    /**
     * Returns the italic.
     * @return Returns the italic.
     */
    public TFMFixWord getItalic() {

        return italic;
    }

    /**
     * Set the italic
     * @param aitalic The italic to set.
     */
    public void setItalic(final TFMFixWord aitalic) {

        italic = aitalic;
    }

    /**
     * Returns the mid.
     * @return Returns the mid.
     */
    public short getMid() {

        return mid;
    }

    /**
     * Set the mid.
     * @param amid The mid to set.
     */
    public void setMid(final short amid) {

        mid = amid;
    }

    /**
     * Returns the nextchar.
     * @return Returns the nextchar.
     */
    public short getNextchar() {

        return nextchar;
    }

    /**
     * Set the nextchar.
     * @param anextchar The nextchar to set.
     */
    public void setNextchar(final short anextchar) {

        nextchar = anextchar;
    }

    /**
     * Returns the rep.
     * @return Returns the rep.
     */
    public short getRep() {

        return rep;
    }

    /**
     * Set the rep.
     * @param arep The rep to set.
     */
    public void setRep(final short arep) {

        rep = arep;
    }

    /**
     * Returns the top.
     * @return Returns the top.
     */
    public short getTop() {

        return top;
    }

    /**
     * Set the top.
     * @param atop The top to set.
     */
    public void setTop(final short atop) {

        top = atop;
    }

    /**
     * Returns the width.
     * @return Returns the width.
     */
    public TFMFixWord getWidth() {

        return width;
    }

    /**
     * Set the width
     * @param awidth The width to set.
     */
    public void setWidth(final TFMFixWord awidth) {

        width = awidth;
    }

    /**
     * Test, if the character exists in the font.
     * (a character exists, if it have a width)
     *
     * @return Returns <code>true</code> if the character exists.
     */
    public boolean exists() {

        return widthindex != 0;
    }

    /**
     * Resets the tag field to NOTAG (zero) value.
     */
    public void resetTag() {

        tag = TAG0;
        tagT = NO_TAG;
    }

    /**
     * Lig/kern programs in the final format
     */
    private TFMLigKern[] ligKernTable;

    /**
     * Set the ligKernTable
     * @param lk    The ligKernTable to set.
     */
    public void setLigKernTable(final TFMLigKern[] lk) {

        ligKernTable = lk;
    }

    /**
     * the glyphname
     */
    private String glyphname;

    /**
     * Returns the glyphname.
     * @return Returns the glyphname.
     */
    public String getGlyphname() {

        return glyphname;
    }

    /**
     * Set the glyphname.
     * @param gn The glyphname to set.
     */
    public void setGlyphname(final String gn) {

        glyphname = gn;
    }

    /**
     * Set bc.
     * @param abc The bc to set.
     */
    public void setBc(final short abc) {

        bc = abc;
    }

    /**
     * @see de.dante.extex.font.type.PlFormat#toPL(de.dante.extex.font.type.PlWriter)
     */
    public void toPL(final PlWriter out) throws IOException {

        out.addFixWord(width, "CHARWD");
        out.addFixWord(height, "CHARHT");
        out.addFixWord(depth, "CHARDP");
        out.addFixWord(italic, "CHARIC");

        if (foundEntry()) {
            out.plopen("COMMENT");
            if (glyphname != null) {
                out.plopen("NAME").addStr(glyphname).plclose();
            }
            if (getTop() != NOCHARCODE) {
                out.plopen("TOP").addDec(getTop()).plclose();
            }
            if (getMid() != NOCHARCODE) {
                out.plopen("MID").addDec(getMid()).plclose();
            }
            if (getBot() != NOCHARCODE) {
                out.plopen("BOT").addDec(getBot()).plclose();
            }
            if (getRep() != NOCHARCODE) {
                out.plopen("REP").addDec(getRep()).plclose();
            }
            // ligature
            int ligstart = getLigkernstart();
            if (ligstart != NOINDEX && ligKernTable != null) {

                for (int k = ligstart; k != NOINDEX; k = ligKernTable[k]
                        .nextIndex(k)) {
                    TFMLigKern lk = ligKernTable[k];

                    if (lk instanceof TFMLigature) {
                        TFMLigature lig = (TFMLigature) lk;

                        out.plopen("LIG").addChar(lig.getNextChar()).addChar(
                                lig.getAddingChar()).plclose();
                    } else if (lk instanceof TFMKerning) {
                        TFMKerning kern = (TFMKerning) lk;

                        out.plopen("KRN").addChar(kern.getNextChar()).addReal(
                                kern.getKern()).plclose();
                    }
                }
            }
            out.plclose();
        }
    }

    /**
     * Check, if char has a entry (glyphname, top, mid, bot, rep, ligature or kern.
     * @return Returns true, if the char has an entry.
     */
    private boolean foundEntry() {

        boolean found = false;
        if (glyphname != null) {
            found = true;
        } else if (getTop() != NOCHARCODE) {
            found = true;
        } else if (getMid() != NOCHARCODE) {
            found = true;
        } else if (getBot() != NOCHARCODE) {
            found = true;
        } else if (getRep() != NOCHARCODE) {
            found = true;
        } else {
            int ligstart = getLigkernstart();
            if (ligstart != TFMCharInfoWord.NOINDEX && ligKernTable != null) {

                for (int k = ligstart; k != TFMCharInfoWord.NOINDEX; k = ligKernTable[k]
                        .nextIndex(k)) {
                    TFMLigKern lk = ligKernTable[k];

                    if (lk instanceof TFMLigature || lk instanceof TFMKerning) {
                        found = true;
                        break;
                    }
                }
            }
        }
        return found;
    }

    /**
     * Returns the bc.
     * @return Returns the bc.
     */
    public short getBc() {

        return bc;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element element = new Element("char");
        element.setAttribute("id", String.valueOf(charid));
        element.setAttribute("glyph-number", String.valueOf(charid + bc));
        String c = Character.toString((char) (charid + bc));
        if (c != null && c.trim().length() > 0) {
            element.setAttribute("char", c);
        }
        if (glyphname != null) {
            element.setAttribute("glyph-name", glyphname.substring(1));
        }
        element.setAttribute("heightindex", String.valueOf(heightindex));
        element.setAttribute("depthindex", String.valueOf(depthindex));
        element.setAttribute("widthindex", String.valueOf(widthindex));
        element.setAttribute("italicindex", String.valueOf(italicindex));
        element.setAttribute("tagnr", String.valueOf(tag));
        String s;
        switch (tag) {
            case TAG0 :
                s = "NO_TAG";
                break;
            case TAG1 :
                s = "LIG_TAG";
                break;
            case TAG2 :
                s = "LIST_TAG";
                break;
            default :
                s = "EXT_TAG";
        }
        element.setAttribute("tag", s);
        element.setAttribute("remainder", String.valueOf(remainder));
        element.setAttribute("width_fw", String.valueOf(getWidth().getValue()));
        element.setAttribute("width", getWidth().toStringComma());
        element.setAttribute("height_fw", String
                .valueOf(getHeight().getValue()));
        element.setAttribute("height", getHeight().toStringComma());
        element.setAttribute("depth_fw", String.valueOf(getDepth().getValue()));
        element.setAttribute("depth", getDepth().toStringComma());
        element.setAttribute("italic_fw", String
                .valueOf(getItalic().getValue()));
        element.setAttribute("italic", getItalic().toStringComma());
        if (getTop() != NOCHARCODE) {
            element.setAttribute("top", String.valueOf(getTop()));
        }
        if (getMid() != NOCHARCODE) {
            element.setAttribute("mid", String.valueOf(getMid()));
        }
        if (getBot() != NOCHARCODE) {
            element.setAttribute("bot", String.valueOf(getBot()));
        }
        if (getRep() != NOCHARCODE) {
            element.setAttribute("rep", String.valueOf(getRep()));
        }
        if (getLigkernstart() != NOINDEX) {
            element.setAttribute("ligkernstart", String
                    .valueOf(getLigkernstart()));
        }
        if (getNextchar() != NOINDEX) {
            element.setAttribute("nextchar", String.valueOf(getNextchar()));
        }

        // ligature
        int ligstart = getLigkernstart();
        if (ligstart != NOINDEX && ligKernTable != null) {

            for (int k = ligstart; k != NOINDEX; k = ligKernTable[k]
                    .nextIndex(k)) {
                TFMLigKern lk = ligKernTable[k];

                if (lk instanceof TFMLigature) {
                    TFMLigature lig = (TFMLigature) lk;

                    Element ligature = new Element("ligature");

                    ligature.setAttribute("letter-id", String.valueOf(lig
                            .getNextChar()));
                    String sl = Character.toString((char) lig.getNextChar());
                    if (sl != null && sl.trim().length() > 0) {
                        ligature.setAttribute("letter", sl.trim());
                    }

                    ligature.setAttribute("lig-id", String.valueOf(lig
                            .getAddingChar()));
                    String slig = Character
                            .toString((char) lig.getAddingChar());
                    if (slig != null && slig.trim().length() > 0) {
                        ligature.setAttribute("lig", slig.trim());
                    }
                    element.addContent(ligature);
                } else if (lk instanceof TFMKerning) {
                    TFMKerning kern = (TFMKerning) lk;

                    Element kerning = new Element("kerning");

                    kerning.setAttribute("id", String.valueOf(kern
                            .getNextChar()));
                    String sk = Character.toString((char) kern.getNextChar());
                    if (sk != null && sk.trim().length() > 0) {
                        kerning.setAttribute("char", sk.trim());
                    }
                    kerning.setAttribute("size_fw", String.valueOf(kern
                            .getKern().getValue()));
                    kerning
                            .setAttribute("size", kern.getKern()
                                    .toStringComma());
                    element.addContent(kerning);
                }
            }
        }
        return element;
    }
}
