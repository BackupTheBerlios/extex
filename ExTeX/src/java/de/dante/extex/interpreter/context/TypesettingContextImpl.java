/*
 * Copyright (C) 2003  Gerd Neugebauer
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
 * @version $Revision: 1.3 $
 */
public class TypesettingContextImpl implements TypesettingContext {
    /** This is the color to use. The effect depends on the object to be colored.
     *  E.g.
     *  in a CharNode it is the color of the text (background is always transparent)
     *  in a RuleNode it is the color of the rule
     *  in a HListNode or VListNode it is the background color
     */
    private Color color;

    /** ... */
    private Direction direction = Direction.LR;

    /** ... */
    private Font font;

    /** ... */
    private HyphenationTable hyphenation;

    /** ... */
    private int angle;

    /**
     * Creates a new object.
     */
    public TypesettingContextImpl() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#setAngle(int)
     */
    public void setAngle(int angle) {
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
    public void setColor(Color color) {
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
    public void setDirection(Direction direction) {
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
    public void setFont(Font font) {
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
    public void setLanguage(HyphenationTable language) {
		hyphenation = language;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getLanguage()
     */
    public HyphenationTable getLanguage() {
        return hyphenation;
    }
}
