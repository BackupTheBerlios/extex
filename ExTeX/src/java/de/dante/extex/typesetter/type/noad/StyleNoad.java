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

package de.dante.extex.typesetter.type.noad;

import java.io.ObjectStreamException;
import java.util.logging.Logger;

import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This Noad indicates a change in the style to be used for the further
 * processing.
 *
 * <p>
 *  <logo>TeX</logo> defined a set of variations to a style.
 *  They are defined in the following table:
 * </p>
 * <table>
 *  <tr>
 *   <th>Style</th>
 *   <th>No.</th>
 *   <th>Cramped</th>
 *   <th>Sub</th>
 *   <th>Sup</th>
 *   <th>Num</th>
 *   <th>Denom</th>
 *  </tr>
 *  <tr>
 *   <td>display style</td>
 *   <td>0</td>
 *   <td>1</td>
 *   <td>5</td>
 *   <td>4</td>
 *   <td>2</td>
 *   <td>3</td>
 *  </tr>
 *  <tr>
 *   <td>cramped display style</td>
 *   <td>1</td>
 *   <td>1</td>
 *   <td>5</td>
 *   <td>5</td>
 *   <td>3</td>
 *   <td>3</td>
 *  </tr>
 *  <tr>
 *   <td>text style</td>
 *   <td>2</td>
 *   <td>3</td>
 *   <td>5</td>
 *   <td>4</td>
 *   <td>4</td>
 *   <td>5</td>
 *  </tr>
 *  <tr>
 *   <td>cramped text style</td>
 *   <td>3</td>
 *   <td>3</td>
 *   <td>5</td>
 *   <td>5</td>
 *   <td>5</td>
 *   <td>5</td>
 *  </tr>
 *  <tr>
 *   <td>script style</td>
 *   <td>4</td>
 *   <td>5</td>
 *   <td>7</td>
 *   <td>6</td>
 *   <td>6</td>
 *   <td>7</td>
 *  </tr>
 *  <tr>
 *   <td>cramped script style</td>
 *   <td>5</td>
 *   <td>5</td>
 *   <td>7</td>
 *   <td>7</td>
 *   <td>7</td>
 *   <td>7</td>
 *   <td>7</td>
 *  </tr>
 *  <tr>
 *   <td>script script style</td>
 *   <td>6</td>
 *   <td>7</td>
 *   <td>7</td>
 *   <td>6</td>
 *   <td>6</td>
 *   <td>7</td>
 *  </tr>
 *  <tr>
 *   <td>cramped script script style</td>
 *   <td>7</td>
 *   <td>7</td>
 *   <td>7</td>
 *   <td>7</td>
 *   <td>7</td>
 *   <td>7</td>
 *  </tr>
 * </table>
 *
 * <p>
 *  This mapping and the numbers therein are not visible directly. Instead
 *  symbolic constants and methods are provided.
 * <p>
 *
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.21 $
 */
public final class StyleNoad implements Noad {

    /**
     * The field <tt>styles</tt> contains the list of styles prepared for use.
     * Some of them are available via constants; others can be acquired via
     * some methods-
     */
    private static final StyleNoad[] STYLE = {//
    new StyleNoad(0, "display", "\\displaystyle"),
            new StyleNoad(1, "display", "\\displaystyle"),
            new StyleNoad(2, "text", "\\textstyle"),
            new StyleNoad(3, "text", "\\textstyle"),
            new StyleNoad(4, "script", "\\scriptstyle"),
            new StyleNoad(5, "script", "\\scriptstyle"),
            new StyleNoad(6, "scriptscript", "\\scriptscriptstyle"),
            new StyleNoad(7, "scriptscript", "\\scriptscriptstyle")};

    /**
     * The field <tt>CRAMPED</tt> contains the mapping to cramped styles.
     */
    private static final StyleNoad[] CRAMPED = {STYLE[1], STYLE[1], STYLE[3],
            STYLE[3], STYLE[5], STYLE[5], STYLE[7], STYLE[7]};

    /**
     * The field <tt>DENOM</tt> contains the mapping to denom styles.
     */
    private static final StyleNoad[] DENOM = {STYLE[5], STYLE[6], STYLE[7],
            STYLE[7], STYLE[7], STYLE[7], STYLE[7], STYLE[7]};

    /**
     * The constant <tt>DISPLAYSTYLE</tt> contains the value for the display
     * style.
     */
    public static final StyleNoad DISPLAYSTYLE = STYLE[0];

    /**
     * The field <tt>NUM</tt> contains the mapping to num styles.
     */
    private static final StyleNoad[] NUM = {STYLE[2], STYLE[3], STYLE[4],
            STYLE[5], STYLE[6], STYLE[6], STYLE[6], STYLE[7]};

    /**
     * The constant <tt>SCRIPTSCRIPTSTYLE</tt> contains the value for the
     * scriptscript style.
     */
    public static final StyleNoad SCRIPTSCRIPTSTYLE = STYLE[6];

    /**
     * The constant <tt>SCRIPTSTYLE</tt> contains the value for the script
     * style.
     */
    public static final StyleNoad SCRIPTSTYLE = STYLE[4];

    /**
     * The field <tt>SUB</tt> contains the mapping to sub styles.
     */
    private static final StyleNoad[] SUB = {STYLE[5], STYLE[5], STYLE[5],
            STYLE[5], STYLE[7], STYLE[7], STYLE[7], STYLE[7]};

    /**
     * The field <tt>SUP</tt> contains the mapping to sup styles.
     */
    private static final StyleNoad[] SUP = {STYLE[4], STYLE[5], STYLE[4],
            STYLE[5], STYLE[6], STYLE[7], STYLE[6], STYLE[7]};

    /**
     * The constant <tt>TEXTSTYLE</tt> contains the value for the text
     * style.
     */
    public static final StyleNoad TEXTSTYLE = STYLE[2];

    /**
     * The field <tt>no</tt> contains the <logo>TeX</logo> encoding of the
     * style.
     */
    private int no;

    /**
     * The field <tt>printName</tt> contains the printable representation.
     */
    private String printName;

    /**
     * The field <tt>style</tt> contains the <logo>TeX</logo> name for the
     * style. It has the values <tt>textstyle</tt>, <tt>scriptstyle</tt>, or
     * <tt>scriptscriptstyle</tt>.
     */
    private String style;

    /**
     * Creates a new object.
     * This constructor is private since nobody is supposed to use it to create
     * new instances. The constants defined in this class should be used
     * instead.
     *
     * @param no the index of this style
     * @param style the style
     * @param printName the printable representation
     */
    private StyleNoad(final int no, final String style, final String printName) {

        super();
        this.no = no;
        this.style = style;
        this.printName = printName;
    }

    /**
     * Get the cramped style for this one.
     *
     * @return the cramped style
     */
    public StyleNoad cramped() {

        return CRAMPED[no];
    }

    /**
     * Get the denom style for this one.
     *
     * @return the denom style
     */
    public StyleNoad denom() {

        return DENOM[no];
    }

    /**
     * Getter for font name.
     *
     * @return the font name
     */
    public String getFontName() {

        return this.style + "font";
    }

    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName() {

        return this.style;
    }

    /**
     * Getter for style.
     *
     * @return the style
     */
    public String getStyleName() {

        return this.style + "style";
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSubscript()
     */
    public Noad getSubscript() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSuperscript()
     */
    public Noad getSuperscript() {

        return null;
    }

    /**
     * Ordering on the styles.
     *
     * @param other the style to compare to
     *
     * @return <code>true</code> iff the current style is less than the other
     *  according to the list in the description of StyleNoad.
     */
    public boolean less(final StyleNoad other) {

        return no < other.no;
    }

    /**
     * Get the num style for this one.
     *
     * @return the num style
     */
    public StyleNoad num() {

        return NUM[no];
    }

    /**
     * Return the singleton constant object after the serialized instance
     * has been read back in.
     * This is one of the magic methods of Java which is invoked when a
     * serialized object is deserialized.
     *
     * @return the one and only instance of this object
     *
     * @throws ObjectStreamException never
     */
    protected Object readResolve() throws ObjectStreamException {

        return STYLE[no];
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#setSubscript(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void setSubscript(final Noad subscript) {

        throw new UnsupportedOperationException("subscript in style");
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#setSuperscript(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void setSuperscript(final Noad superscript) {

        throw new UnsupportedOperationException("superscript in style");
    }

    /**
     * Get the sub style for this one.
     *
     * @return the sub style
     */
    public StyleNoad sub() {

        return SUB[no];
    }

    /**
     * Get the sup style for this one.
     *
     * @return the sup style
     */
    public StyleNoad sup() {

        return SUP[no];
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return printName;
    }

    /**
     * @see "TTP [694]"
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        sb.append(printName);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *       java.lang.StringBuffer, int)
     */
    public void toString(final StringBuffer sb, final int depth) {

        toString(sb);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger)
     */
    public int typeset(final NoadList noads, final int index,
            final NodeList list, final MathContext mathContext,
            final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        mathContext.setStyle(this);
        return index + 1;
    }

}
