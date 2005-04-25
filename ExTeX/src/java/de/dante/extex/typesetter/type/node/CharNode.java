/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type.node;

import de.dante.extex.font.Glyph;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This is the Node which carries a single character.
 *
 * @see "TeX -- The Program [134]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public class CharNode extends AbstractNode implements Node {

    /**
     * The field <tt>character</tt> contains the single character represented
     * by this node.
     */
    private UnicodeChar character;

    /**
     * The field <tt>glyph</tt> contains the glyph from the font associated to
     * the Unicode character.
     */
    private Glyph glyph;

    /**
     * The field <tt>typesettingContext</tt> contains the typesetting context
     */
    private TypesettingContext typesettingContext;

    /**
     * Creates a new object.
     *
     * @param context the typesetting context
     * @param uc the unicode character
     */
    public CharNode(final TypesettingContext context, final UnicodeChar uc) {

        super();
        typesettingContext = context;
        character = uc;
        Font font = context.getFont();
        glyph = font.getGlyph(uc);

        if (glyph != null) {
            setWidth(glyph.getWidth());
            setHeight(glyph.getHeight());
            setDepth(glyph.getDepth());
        } else {
            setWidth(new Dimen(0));
            setHeight(new Dimen(0));
            setDepth(new Dimen(0));
            //character = null;
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#countChars()
     */
    public int countChars() {

        return 1;
    }

    /**
     * Getter for character.
     *
     * @return the character.
     */
    public UnicodeChar getCharacter() {

        return this.character;
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#getChars()
     */
    public CharNode[] getChars() {

        return new CharNode[]{this};
    }

    /**
     * Getter for glyph.
     *
     * @return the glyph.
     */
    public Glyph getGlyph() {

        return this.glyph;
    }

    /**
     * Getter for the space factor
     *
     * @return the space factor
     */
    public int getSpaceFactor() {

        return 0; //TODO gene: getSpaceFactor() incomplete
    }

    /**
     * Getter for typesettingContext.
     *
     * @return the typesettingContext.
     */
    public TypesettingContext getTypesettingContext() {

        return this.typesettingContext;
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toString(
     *      java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        Font font = typesettingContext.getFont();
        sb.append(getLocalizer()
                .format("String.Format",
                        (font == null ? "*" : font.getFontName()),
                        character.toString()));
        if (false) {
            sb.append(" (");
            sb.append(getHeight().toString());
            sb.append("+");
            sb.append(getDepth().toString());
            sb.append(")x");
            sb.append(getWidth().toString());
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#toText(
     *      java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toText(final StringBuffer sb, final String prefix) {

        Font font = typesettingContext.getFont();
        sb.append(getLocalizer()
                .format("String.Format",
                        (font == null ? "*" : font.getFontName()),
                        character.toString()));
    }

    /**
     * @see de.dante.extex.typesetter.type.Node#visit(
     *      de.dante.extex.typesetter.type.NodeVisitor,
     *      java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value)
            throws GeneralException {

        return visitor.visitChar(this, value);
    }

}