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

package de.dante.extex.typesetter.type.noad.util;

import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.extex.interpreter.primitives.register.font.NumberedFont;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.interpreter.type.muskip.Mudimen;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.noad.StyleNoad;

/**
 * This class provides a container for the information on the current
 * mathematical appearance.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class MathContext {

    /**
     * The constant <tt>MU_UNIT</tt> contains the unit length for the math unit.
     */
    private static final long MU_UNIT = 18 * 0xffff;

    /**
     * The field <tt>style</tt> contains the current style.
     */
    private StyleNoad style;

    /**
     * The field <tt>context</tt> contains the data object for options.
     */
    private TypesetterOptions context;

    /**
     * Creates a new object.
     *
     * @param style the new style
     * @param context the typesetter context
     */
    public MathContext(final StyleNoad style, final TypesetterOptions context) {

        super();
        this.style = style;
        this.context = context;
    }

    /**
     * Getter for style.
     *
     * @return the style.
     */
    public StyleNoad getStyle() {

        return this.style;
    }

    /**
     * Setter for style.
     *
     * @param style the style to set.
     */
    public void setStyle(final StyleNoad style) {

        this.style = style;
    }

    /**
     * Convert a muglue into a glue.
     *
     * From The TeXbook:
     *
     * <p><i>
     * There are 18 mu to an em, where the em is taken from family~2
     * (the math symbols family). In other words, ^|\textfont|~|2| defines the em
     * value for |mu| in display and text styles; ^|\scriptfont|~|2| defines the
     * em for script size material; and ^|\scriptscriptfont|~|2| defines it for
     * scriptscript size.
     * </i></p>
     *
     * @param muglue the math glue to convert
     *
     * @return a new instance of a glue corresponding to the parameter
     *
     * @see "TTP [716]"
     */
    public Glue convert(final Muskip muglue) {

        Font fnt = null;

        if (style == StyleNoad.TEXTSTYLE || style == StyleNoad.DISPLAYSTYLE) {
            fnt = context.getFont(NumberedFont.key(context, "textfont", "2"));
        } else if (style == StyleNoad.SCRIPTSTYLE) {
            fnt = context.getFont(NumberedFont.key(context, "scriptfont", "2"));
        } else if (style == StyleNoad.SCRIPTSCRIPTSTYLE) {
            fnt = context.getFont(NumberedFont.key(context, "scriptscriptfont",
                    "2"));
        } else {
            throw new ImpossibleException("undefined style");
        }

        long factor = fnt.getEm().getValue();
        GlueComponent length = new GlueComponent(muglue.getLength());
        GlueComponent stretch = new GlueComponent(muglue.getStretch());
        GlueComponent shrink = new GlueComponent(muglue.getShrink());

        if (length.getOrder() != 0) {
            length.multiply(factor, MU_UNIT);
        }
        if (stretch.getOrder() != 0) {
            stretch.multiply(factor, MU_UNIT);
        }
        if (shrink.getOrder() != 0) {
            shrink.multiply(factor, MU_UNIT);
        }

        return new Glue(length, stretch, shrink);
    }

    /**
     * Convert a mudimen into a dimen.
     *
     * From The TeXbook:
     *
     * <p><i>
     * There are 18 mu to an em, where the em is taken from family~2
     * (the math symbols family). In other words, ^|\textfont|~|2| defines the em
     * value for |mu| in display and text styles; ^|\scriptfont|~|2| defines the
     * em for script size material; and ^|\scriptscriptfont|~|2| defines it for
     * scriptscript size.
     * </i></p>
     *
     * @param mudimen the math dimen to convert
     *
     * @return a new instance of a Dimen corresponding to the parameter
     *
     * @see "TTP [717]"
     */
    public Dimen convert(final Mudimen mudimen) {

        Font fnt = null;

        if (style == StyleNoad.TEXTSTYLE || style == StyleNoad.DISPLAYSTYLE) {
            fnt = context.getFont(NumberedFont.key(context, "textfont", "2"));
        } else if (style == StyleNoad.SCRIPTSTYLE) {
            fnt = context.getFont(NumberedFont.key(context, "scriptfont", "2"));
        } else if (style == StyleNoad.SCRIPTSCRIPTSTYLE) {
            fnt = context.getFont(NumberedFont.key(context, "scriptscriptfont",
                    "2"));
        } else {
            throw new ImpossibleException("undefined style");
        }

        Dimen length = new Dimen(mudimen.getLength());
        length.multiply(fnt.getEm().getValue(), MU_UNIT);

        return length;
    }

}
