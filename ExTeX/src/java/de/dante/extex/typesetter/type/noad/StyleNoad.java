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
import de.dante.extex.typesetter.type.noad.util.MathContext;

/**
 * This Noad indicates a change in the style to be used for the further
 * processing.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public final class StyleNoad implements Noad {

    /**
     * The constant <tt>DISPLAYSTYLE</tt> contains the value for the display
     * style.
     */
    public static final StyleNoad DISPLAYSTYLE = new StyleNoad();

    /**
     * The constant <tt>SCRIPTSCRIPTSTYLE</tt> contains the value for the
     * scriptscript style.
     */
    public static final StyleNoad SCRIPTSCRIPTSTYLE = new StyleNoad();

    /**
     * The constant <tt>SCRIPTSTYLE</tt> contains the value for the script
     * style.
     */
    public static final StyleNoad SCRIPTSTYLE = new StyleNoad();

    /**
     * The constant <tt>TEXTSTYLE</tt> contains the value for the text
     * style.
     */
    public static final StyleNoad TEXTSTYLE = new StyleNoad();

    /**
     * Creates a new object.
     * This constructor is private since nobody is supposed to use it to create
     * new instances. The constants defined in this class should be usesd
     * instead.
     */
    private StyleNoad() {

        super();
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(MathContext)
     */
    public NodeList typeset(final MathContext mathContext) {

        mathContext.setStyle(this);
        return null;
    }

}