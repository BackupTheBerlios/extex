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
package de.dante.extex.typesetter.type.noad.util;

import de.dante.extex.typesetter.type.noad.StyleNoad;


/**
 * This class provides a container for the information on the current
 * mathematical appearance.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class MathContext {

    /**
     * The field <tt>style</tt> contains the current style.
     */
    private StyleNoad style;

    /**
     * Creates a new object.
     *
     * @param theStyle the new style
     */
    public MathContext(final StyleNoad theStyle) {

        super();
        style = theStyle;
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
}
