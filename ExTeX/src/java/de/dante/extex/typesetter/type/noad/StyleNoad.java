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

package de.dante.extex.typesetter.type.noad;

import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.noad.util.MathContext;

/**
 * This Noad indicates a change in the style to be used for the further
 * processing.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public final class StyleNoad implements Noad {

    /**
     * The constant <tt>DISPLAYSTYLE</tt> contains the value for the display
     * style.
     */
    public static final StyleNoad DISPLAYSTYLE = new StyleNoad("textstyle",
            "\\displaystyle");

    /**
     * The constant <tt>SCRIPTSCRIPTSTYLE</tt> contains the value for the
     * scriptscript style.
     */
    public static final StyleNoad SCRIPTSCRIPTSTYLE = new StyleNoad(
            "scriptscriptstyle", "\\scriptscriptstyle");

    /**
     * The constant <tt>SCRIPTSTYLE</tt> contains the value for the script
     * style.
     */
    public static final StyleNoad SCRIPTSTYLE = new StyleNoad("scriptstlye",
            "\\scriptstlye");

    /**
     * The constant <tt>TEXTSTYLE</tt> contains the value for the text
     * style.
     */
    public static final StyleNoad TEXTSTYLE = new StyleNoad("textstyle",
            "\\textstyle");

    /**
     * The field <tt>style</tt> contains the TeX name for the style. It has the
     * values <tt>textstyle</tt>, <tt>scriptstyle</tt>, or
     * <tt>scriptscriptstyle</tt>.
     */
    private String style;

    /**
     * The field <tt>printName</tt> contains the printable representation.
     */
    private String printName;

    /**
     * Creates a new object.
     * This constructor is private since nobody is supposed to use it to create
     * new instances. The constants defined in this class should be usesd
     * instead.
     *
     * @param style the style
     */
    private StyleNoad(final String style, final String printName) {

        super();
        this.style = style;
        this.printName = printName;
    }

    /**
     * Getter for style.
     *
     * @return the style
     */
    public String getStyleName() {

        return this.style;
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
     * @see de.dante.extex.typesetter.type.noad.Noad#setSubscript(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void setSubscript(final Noad subscript) {

    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#setSuperscript(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void setSuperscript(final Noad superscript) {

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
     *      de.dante.extex.typesetter.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void typeset(final NodeList list, final MathContext mathContext,
            final TypesetterOptions context) {

        mathContext.setStyle(this);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#visit(
     *      de.dante.extex.typesetter.type.noad.NoadVisitor)
     */
    public void visit(final NoadVisitor visitor) {

        visitor.visitStyle(this);
    }
}