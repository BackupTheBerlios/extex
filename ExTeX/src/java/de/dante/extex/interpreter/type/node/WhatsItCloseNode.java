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

package de.dante.extex.interpreter.type.node;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.file.OutFile;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class WhatsItCloseNode extends WhatsItNode {

    /**
     * The field <tt>key</tt> contains the ...
     */
    private String key;

    /**
     * Creates a new object.
     *
     * @param theKey the key of the file to open
     */
    public WhatsItCloseNode(final String theKey) {

        super();
        this.key = theKey;
    }

    /**
     * This method performs any action which are required to executed at the
     * time of shipping the node to the DocumentWriter.
     *
     * @param context the interpreter context
     *
     * @see de.dante.extex.typesetter.Node#atShipping(
     *      de.dante.extex.interpreter.context.Context)
     */
    public void atShipping(final Context context) throws GeneralException {

        OutFile file = context.getOutFile(key);
        if (file != null) {
            file.close();
        }
    }
}