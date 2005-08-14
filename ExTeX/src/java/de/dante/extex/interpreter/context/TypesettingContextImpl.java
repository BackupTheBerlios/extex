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

package de.dante.extex.interpreter.context;

import de.dante.extex.color.model.ColorFactory;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.language.Language;

/**
 * This implementation of a typesetting context provides the required
 * functionality for the container or attributes describing the
 * appearance of glyphs or other nodes.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.23 $
 */
public class TypesettingContextImpl implements ModifiableTypesettingContext {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

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
    private Direction direction;

    /**
     * The field <tt>font</tt> contains the font to use.
     */
    private Font font;

    /**
     * The field <tt>hyphenation</tt> contains the hyphenation table for the
     * current language.
     */
    private Language language;

    /**
     * Creates a new object filled with default values.
     */
    public TypesettingContextImpl() {

        super();
        this.font = null;
        this.color = ColorFactory.BLACK;
        this.direction = Direction.LR;
        this.language = null;
    }

    /**
     * Creates a new object with the given initial font, the color black and no
     * hyphenation.
     *
     * @param theFont the font to use
     */
    public TypesettingContextImpl(final Font theFont) {

        super();
        this.font = theFont;
        this.color = ColorFactory.BLACK;
        this.direction = Direction.LR;
        this.language = null;
    }

    /**
     * Creates a new object (copy constructor).
     *
     * @param tc the typesetting context to copy
     */
    public TypesettingContextImpl(final TypesettingContext tc) {

        super();
        if (tc == null) {
            this.font = null;
            this.color = ColorFactory.BLACK;
            this.direction = Direction.LR;
            this.language = null;

        } else {
            this.font = tc.getFont();
            this.color = tc.getColor();
            this.direction = tc.getDirection();
            this.language = tc.getLanguage();
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getColor()
     */
    public Color getColor() {

        return this.color;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getDirection()
     */
    public Direction getDirection() {

        return this.direction;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getFont()
     */
    public Font getFont() {

        if (this.font == null) {
            throw new IllegalStateException("font undefined");
        }
        return this.font;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#getLanguage()
     */
    public Language getLanguage() {

        if (this.font == null) {
            throw new IllegalStateException("language undefined");
        }
        return this.language;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#set(
     *      de.dante.extex.interpreter.context.TypesettingContext)
     */
    public void set(final TypesettingContext context) {

        if (context != null) {
            this.font = context.getFont();
            this.color = context.getColor();
            this.direction = Direction.LR;
            this.language = context.getLanguage();
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#setColor(
     *      de.dante.extex.interpreter.context.Color)
     */
    public void setColor(final Color theColor) {

        this.color = theColor;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#setDirection(
     *       de.dante.extex.interpreter.context.Direction)
     */
    public void setDirection(final Direction theDirection) {

        this.direction = theDirection;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#setFont(
     *      de.dante.extex.interpreter.type.font.Font)
     */
    public void setFont(final Font theFont) {

        this.font = theFont;
    }

    /**
     * @see de.dante.extex.interpreter.context.TypesettingContext#setLanguage(
     *      de.dante.extex.language.Language)
     */
    public void setLanguage(final Language language) {

        this.language = language;
    }

}