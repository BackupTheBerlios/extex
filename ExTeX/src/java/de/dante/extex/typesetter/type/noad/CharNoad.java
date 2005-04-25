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

package de.dante.extex.typesetter.type.noad;

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.primitives.register.font.NumberedFont;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.node.CharNode;

/**
 * This class provides a container for a mathamatical character.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class CharNoad extends AbstractNoad implements Noad {

    /**
     * The field <tt>uc</tt> contains the character representation.
     */
    private MathGlyph mg;

    /**
     * Creates a new object.
     *
     * @param character the character representation
     */
    protected CharNoad(final MathGlyph character) {

        super();
        this.mg = character;
    }

    /**
     * Getter for the character.
     *
     * @return the character.
     */
    public MathGlyph getChar() {

        return this.mg;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.AbstractNoad#stringName()
     */
    protected String stringName() {

        return "mathchar";
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        mg.toString(sb);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void typeset(final NodeList list, final MathContext mathContext,
            final TypesetterOptions context) {

        String type = mathContext.getStyle().getStyleName();
        Font font = context.getFont(NumberedFont.key(context, //
                type, Integer.toString(mg.getFamily())));
        if (font == null) {
            //gene: impossible
            throw new NullPointerException("font");
        }
        TypesettingContext tc = context.getTypesettingContext().copy();
        tc.setFont(font);
        list.addGlyph(new CharNode(tc, mg.getCharacter()));
    }

}