/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */
package de.dante.extex.interpreter.type.node;

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Glyph;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @see "TeX -- The Program [143]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public class LigatureNode extends AbstractNode implements Node {

    /**
     * The field <tt>character</tt> contains the single character represented
     * by this node.
     */
    private UnicodeChar character;

    /**
     * The field <tt>typesettingContext</tt> contains the typesetting context
     */
    private TypesettingContext typesettingContext;

    /**
     * The field <tt>list</tt> contains the ...
     */
    private UnicodeChar[] list;

    /**
     * Creates a new object.
     *
     * @param context the typesetting context
     * @param uc the unicode character
     *
     * @see "TeX -- The Program [144]"
     */
    public LigatureNode(final TypesettingContext context, final UnicodeChar uc) {

        super();
        typesettingContext = context;
        character = uc;
        Glyph glyph = context.getFont().getGlyph(uc);

        if (glyph != null) {
            setWidth(glyph.getWidth());
            setHeight(glyph.getHeight());
            setDepth(glyph.getDepth());
        } else {
            setWidth(new Dimen(0));
            setHeight(new Dimen(0));
            setDepth(new Dimen(0));
        }
    }

    /**
     * Getter for character.
     *
     * @return the character.
     */
    public UnicodeChar getCharacter() {

        return character;
    }

    /**
     * Getter for the space factor
     *
     * @return the space factor
     */
    public int getSpaceFactor() {

        return 0; // TODO incomplete
    }

    /**
     * Getter for typesettingContext.
     *
     * @return the typesettingContext.
     */
    public TypesettingContext getTypesettingContext() {

        return typesettingContext;
    }

    /**
     * This method returns the printable representation.
     * This is meant to produce a exaustive form as it is used in tracing
     * output to the log file.
     *
     * @return the printable representation
     *
     * @see "TeX -- The Program [193]"
     */
    public String toString() {
        return "lig "; //TODO
    }

    /**
     * @see de.dante.extex.typesetter.Node#toString(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toString(final StringBuffer sb, final String prefix) {

        sb.append('\\');
        sb.append(typesettingContext.getFont().getFontName());
        sb.append(' ');
        sb.append(character.toString());
        sb.append(" (");
        sb.append(getHeight().toString());
        sb.append("+");
        sb.append(getDepth().toString());
        sb.append(")x");
        sb.append(getWidth().toString());
    }

    /**
     * @see de.dante.extex.typesetter.Node#toText(java.lang.StringBuffer,
     *      java.lang.String)
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append(character.toString());
    }

    /**
     * @see de.dante.extex.typesetter.Node#visit(de.dante.extex.typesetter.NodeVisitor,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visit(final NodeVisitor visitor, final Object value,
            final Object value2) throws GeneralException {

        return visitor.visitLigature(value, value2);
    }

}
