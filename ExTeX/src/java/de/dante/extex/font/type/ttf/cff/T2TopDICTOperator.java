/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type.ttf.cff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import de.dante.util.file.random.RandomAccessR;

/**
 * Top DICT Operator.
 *
 * <p>Top DICT Operator Entries<p>
 * <table border="1">
 *   <thead>
 *     <tr><td><b>Name</b></td><td><b>Value</b></td>
 *         <td><b>Operand(s)</b></td><td><b>Default, notes</b></td>
 *     </tr>
 *   </thead>
 *   <tr><td>version</td><td>0</td><td>SID</td><td>- , FontInfo</td></tr>
 *   <tr><td>Notice</td><td>1</td><td>SID</td>- , FontInfo</td></tr>
 *   <tr><td>Copyright</td><td>12 0</td><td>SID</td><td>- , FontInfo</td></tr>
 *   <tr><td>FullName</td><td>2</td><td>SID</td><td>- , FontInfo</td></tr>
 *   <tr><td>FamilyName</td><td>3</td><td>SID</td><td>- , FontInfo</td></tr>
 *   <tr><td>Weight</td><td>4</td><td>SID</td><td>- , FontInfo</td></tr>
 *   <tr><td>isFixedPitch</td><td>12 1</td><td>boolean</td><td>0 (false), FontInfo</td></tr>
 *   <tr><td>ItalicAngle</td><td>12 2</td><td>number</td><td>0, FontInfo</td></tr>
 *   <tr><td>UnderlinePosition</td><td>12 3</td><td>number</td><td>-100, FontInfo</td></tr>
 *   <tr><td>UnderlineThickness</td><td>12 4</td><td>number</td><td>50, FontInfo</td></tr>
 *   <tr><td>PaintType</td><td>12 5</td><td>number</td><td>0</td></tr>
 *   <tr><td>CharstringType</td><td>12 6</td><td>number</td><td>2</td></tr>
 *   <tr><td>FontMatrix</td><td>12 7</td><td>array</td><td>0.001 0 0 0.001 0 0</td></tr>
 *   <tr><td>UniqueID</td><td>13</td><td>number</td><td>-</td></tr>
 *   <tr><td>FontBBox</td><td>5</td><td>array</td><td>0 0 0 0</td></tr>
 *   <tr><td>StrokeWidth</td><td>12 8</td><td>number</td><td>0</td></tr>
 *   <tr><td>XUID</td><td>14</td><td>array</td><td>-</td></tr>
 *   <tr><td>charset</td><td>15</td><td>number</td><td>0, charset offset (0)</td></tr>
 *   <tr><td>Encoding</td><td>16</td><td>number</td><td>0, encoding offset (0)</td></tr>
 *   <tr><td>CharStrings</td><td>17</td><td>number</td><td>
 *      - , CharStrings offset (0)</td></tr>
 *   <tr><td>Private</td><td>18</td><td>number number</td><td>
 *      - , Private DICT size and offset (0)</td></tr>
 *   <tr><td>SyntheticBase</td><td>12 20</td><td>number</td><td>
 *      - , synthetic base font index</td></tr>
 *   <tr><td>PostScript</td><td>12 21</td><td>SID</td><td>
 *      - , embedded PostScript language code</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public abstract class T2TopDICTOperator extends T2Operator {

    /**
     * Create a new object
     */
    protected T2TopDICTOperator() {

        super();
    }

    /**
     * @see de.dante.extex.font.type.ttf.cff.T2CharString#isTopDICTOperator()
     */
    public boolean isTopDICTOperator() {

        return true;
    }

    /**
     * VERSION
     */
    public static final int VERSION = 0;

    /**
     * NOTICE
     */
    public static final int NOTICE = 1;

    /**
     * FULLNAME
     */
    public static final int FULLNAME = 2;

    /**
     * FAMILYNAME
     */
    public static final int FAMILYNAME = 3;

    /**
     * WEIGHT
     */
    public static final int WEIGHT = 4;

    /**
     * FONTBBOX
     */
    public static final int FONTBBOX = 5;

    /**
     * COPYRIGHT
     */
    public static final int COPYRIGHT = 0;

    /**
     * ISFIXEDPITCH
     */
    public static final int ISFIXEDPITCH = 1;

    /**
     * ITALICANGLE
     */
    public static final int ITALICANGLE = 2;

    /**
     * UNDERLINEPOSITION
     */
    public static final int UNDERLINEPOSITION = 3;

    /**
     * UNDELINETHICKNESS
     */
    public static final int UNDERLINETHICKNESS = 4;

    /**
     * PAINTTYPE
     */
    public static final int PAINTTYPE = 5;

    /**
     * CHARSTRINGTYPE
     */
    public static final int CHARSTRINGTYPE = 6;

    /**
     * FONTMATRIX
     */
    public static final int FONTMATRIX = 7;

    /**
     * STROKEWIDTH
     */
    public static final int STROKEWIDTH = 8;

    /**
     * SYNTHETICBASE
     */
    public static final int SYNTHETICBASE = 20;

    /**
     * POSTSCRIPT
     */
    public static final int POSTSCRIPT = 21;

    /**
     * UNIQUEID
     */
    public static final int UNIQUEID = 13;

    /**
     * XUID
     */
    public static final int XUID = 14;

    /**
     * CHARSET
     */
    public static final int CHARSET = 15;

    /**
     * ENCODING
     */
    public static final int ENCODING = 16;

    /**
     * CHARSTRINGS
     */
    public static final int CHARSTRINGS = 17;

    /**
     * PRIVATE
     */
    public static final int PRIVATE = 18;

    /**
     * Create a new instance.
     *
     * @param rar       the input
     * @return Returns the new T2Operatorr object.
     * @throws IOException if an IO-error occurs.
     */
    public static T2Operator newInstance(final RandomAccessR rar)
            throws IOException {

        List stack = new ArrayList();

        while (true) {

            int b = rar.readUnsignedByte();

            switch (b) {
                case VERSION :
                    return new T2TDOVersion(stack);
                case NOTICE :
                    return new T2TDONotice(stack);
                case FULLNAME :
                    return new T2TDOFullName(stack);
                case FAMILYNAME :
                    return new T2TDOFamilyName(stack);
                case WEIGHT :
                    return new T2TDOWeight(stack);
                case FONTBBOX :
                    return new T2TDOFontBBox(stack);
                case ESCAPE_BYTE :
                    int b1 = rar.readUnsignedByte();
                    switch (b1) {
                        case COPYRIGHT :
                            return new T2TDOCopyright(stack);
                        case ISFIXEDPITCH :
                            return new T2TDOisFixedPitch(stack);
                        case ITALICANGLE :
                            return new T2TDOItalicAngle(stack);
                        case UNDERLINEPOSITION :
                            return new T2TDOUnderlinePosition(stack);
                        case UNDERLINETHICKNESS :
                            return new T2TDOUnderlineThickness(stack);
                        case PAINTTYPE :
                            return new T2TDOPaintType(stack);
                        case CHARSTRINGTYPE :
                            return new T2TDOCharStringType(stack);
                        case FONTMATRIX :
                            return new T2TDOFontMatrix(stack);
                        case STROKEWIDTH :
                            return new T2TDOStrokeWidth(stack);
                        case SYNTHETICBASE :
                            return new T2TDOSyntheticBase(stack);
                        case POSTSCRIPT :
                            return new T2TDOPostscript(stack);
                        default :
                            throw new T2NotAOperatorException();

                    }
                case UNIQUEID :
                    return new T2TDOUniqueID(stack);
                case XUID :
                    return new T2TDOXUID(stack);
                case CHARSET :
                    return new T2TDOCharset(stack);
                case ENCODING :
                    return new T2TDOEncoding(stack);
                case CHARSTRINGS :
                    return new T2TDOCharStrings(stack);
                case PRIVATE :
                    return new T2TDOPrivate(stack);
                default :
                    // number
                    T2Number number = T2CharString.readNumber(rar, b);
                    stack.add(number);
                    break;
            }
        }
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element op = new Element(getName());
        op.setAttribute("value", toString());
        return op;
    }

}