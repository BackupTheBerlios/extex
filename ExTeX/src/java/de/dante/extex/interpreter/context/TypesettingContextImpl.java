/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
package de.dante.extex.interpreter.context;

import de.dante.extex.hyphenation.HyphenationTable;
import de.dante.extex.interpreter.type.Font;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class TypesettingContextImpl implements TypesettingContext {
    /**
     * The field <tt>color</tt> contains the color to use.
     * The effect depends on the object to be colored.
     * For instance
     * <ul>
     * <li>in a CharNode it is the color of the text (background is always
     * transparent)</li>
     * <li>in a RuleNode it is the color of the rule</li>
     * <li>in a HListNode or VListNode it is the background color</li>
     * </ul>
     */
    private Color color;

    /**
     * The field <tt>direction</tt> contains the direction for advancing the
     * cursor. This is one of the constants in {@link Direction Direction}.
     */
    private Direction direction = Direction.LR;

    /**
     * The field <tt>font</tt> contains the font to use.
     */
    private Font font;

    /**
     * The field <tt>hyphenation</tt> contains the ...
     */
    private HyphenationTable hyphenation;

    /**
     * The field <tt>angle</tt> contains the ...
     */
    private int angle;

    /**
     * Creates a new object.
     */
    public TypesettingContextImpl() {
        super();
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#setAngle(int)
     */
    public void setAngle(final int angle) {
        this.angle = angle;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getAngle()
     */
    public int getAngle() {
        return angle;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#setColor(de.dante.extex.interpreter.context.Color)
     */
    public void setColor(final Color color) {
        this.color = color;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getColor()
     */
    public Color getColor() {
        return color;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#setDirection(de.dante.extex.interpreter.context.Direction)
     */
    public void setDirection(final Direction direction) {
        this.direction = direction;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getDirection()
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#setFont(de.dante.extex.interpreter.type.Font)
     */
    public void setFont(final Font font) {
        this.font = font;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getFont()
     */
    public Font getFont() {
        return font;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#setLanguage(HyphenationTable)
     */
    public void setLanguage(final HyphenationTable language) {
        hyphenation = language;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getLanguage()
     */
    public HyphenationTable getLanguage() {
        return hyphenation;
    }

}
