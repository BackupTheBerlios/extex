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

package de.dante.extex.typesetter.paragraphBuilder;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class HangingParagraphShape extends ParagraphShape {

    /**
     * The field <tt>hsize</tt> contains the width of the line.
     */
    private FixedDimen hsize;

    /**
     * The field <tt>hangafter</tt> contains the number of lines to start or
     * end hanging.
     */
    private int hangafter;

    /**
     * The field <tt>hangindent</tt> contains the amount of indentation.
     */
    private FixedDimen hangindent;

    /**
     * Creates a new object.
     *
     * @param theHangafter the number of lines to start or end hanging
     * @param theHangindent the amount of indentation
     * @param theHsize the width of the line
     */
    public HangingParagraphShape(final int theHangafter,
            final FixedDimen theHangindent, final FixedDimen theHsize) {

        super();
        this.hsize = theHsize;
        this.hangafter = theHangafter;
        this.hangindent = theHangindent;
    }

    /**
     * @see de.dante.extex.typesetter.paragraphBuilder.ParagraphShape#getLeft(int)
     */
    public FixedDimen getLeft(final int index) {

        if (hangafter > 0) {
            if (index >= hangafter) {
                return hangindent;
            }
        } else if (index < -hangafter) {
            return hangindent;
        }

        return Dimen.ZERO_PT;
    }

    /**
     * @see de.dante.extex.typesetter.paragraphBuilder.ParagraphShape#getRight(int)
     */
    public FixedDimen getRight(final int index) {

        return hsize;
    }

    /**
     * Setter for hsize.
     *
     * @param hsize the hsize to set.
     */
    public void setHsize(final FixedDimen hsize) {

        this.hsize = hsize;
    }
    /**
     * Setter for hangafter.
     *
     * @param hangafter the hangafter to set.
     */
    public void setHangafter(final int hangafter) {

        this.hangafter = hangafter;
    }
    /**
     * Setter for hangindent.
     *
     * @param hangindent the hangindent to set.
     */
    public void setHangindent(final FixedDimen hangindent) {

        this.hangindent = hangindent;
    }
}