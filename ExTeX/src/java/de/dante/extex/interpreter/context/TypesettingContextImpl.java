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

import de.dante.extex.hyphenation.HyphenationManager;
import de.dante.extex.interpreter.type.Font;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class TypesettingContextImpl implements TypesettingContext {
    /** ... */
    private Color color;

    /** ... */
    private Direction direction     = Direction.LR;

    /** ... */
    private Font font;

    /** ... */
    private HyphenationManager hyphenation;

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
        // TODO Auto-generated method stub
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getAngle()
     */
    public int getAngle() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#setColor(de.dante.extex.interpreter.context.Color)
     */
    public void setColor(Color color) {
        // TODO Auto-generated method stub
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getColor()
     */
    public Color getColor() {
        // TODO Auto-generated method stub
        return null;
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
     * @see de.dante.extex.interpreter.context.TypesettingContext#setLanguage(java.lang.String)
     */
    public void setLanguage(String language) {
        // TODO Auto-generated method stub
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getLanguage()
     */
    public String getLanguage() {
        // TODO Auto-generated method stub
        return null;
    }
}
