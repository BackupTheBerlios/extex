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

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractNucleusNoad extends AbstractNoad {

    /**
     * The field <tt>nucleus</tt> contains the nucleus.
     */
    private Noad nucleus;

    /**
     * Creates a new object.
     *
     * @param nucleus the nucleaus of inner noads
     */
    public AbstractNucleusNoad(final Noad nucleus) {

        super();
        this.nucleus = nucleus;
    }

    /**
     * Getter for nucleus.
     *
     * @return the nucleus
     */
    public Noad getNucleus() {

        return this.nucleus;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

}