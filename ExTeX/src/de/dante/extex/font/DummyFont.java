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
package de.dante.extex.font;

import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Font;
import de.dante.extex.interpreter.type.Glue;

/**
 * This class implements a dummy font which does not contain any characters.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class DummyFont implements Font {

    /**
     * Creates a new object.
     */
    public DummyFont(String name) {
        super();
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getWidth(java.lang.String)
     */
    public Dimen getWidth(String c) {
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getHeight(java.lang.String)
     */
    public Dimen getHeight(String c) {
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getDepth(java.lang.String)
     */
    public Dimen getDepth(String c) {
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#isDefined(java.lang.String)
     */
    public boolean isDefined(String c) {
        return false;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#kern(java.lang.String, java.lang.String)
     */
    public Dimen kern(String c1, String c2) {
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#ligature(java.lang.String, java.lang.String)
     */
    public String ligature(String c1, String c2) {
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getSpace()
     */
    public Glue getSpace() {
        return new Glue(12*Dimen.ONE);
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getEm()
     */
    public Dimen getEm() {
        return new Dimen(12*Dimen.ONE);
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getEx()
     */
    public Dimen getEx() {
        return new Dimen(6*Dimen.ONE);
    }

}
