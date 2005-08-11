/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type.node;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.file.OutFile;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;

/**
 * This WhatsIt node opens an out file on shipping.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class WhatsItOpenNode extends WhatsItNode {

    /**
     * The field <tt>file</tt> contains the output file.
     */
    private OutFile file;

    /**
     * The field <tt>key</tt> contains the reference key
     */
    private String key;

    /**
     * Creates a new object.
     *
     * @param theKey the key of the file to open
     * @param outFile the out file to open
     */
    public WhatsItOpenNode(final String theKey, final OutFile outFile) {

        super();
        this.key = theKey;
        this.file = outFile;
    }

    /**
     * This method performs any action which are required to executed at the
     * time of shipping the node to the DocumentWriter.
     * @param context the interpreter context
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.typesetter.type.Node#atShipping(
     *      de.dante.extex.interpreter.context.Context, Typesetter)
     */
    public void atShipping(final Context context, Typesetter typesetter) throws GeneralException {

        file.open();
        context.setOutFile(key, file, false);
    }
}
