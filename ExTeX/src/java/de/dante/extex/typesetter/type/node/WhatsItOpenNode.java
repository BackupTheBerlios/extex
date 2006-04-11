/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.exception.GeneralException;

/**
 * This WhatsIt node which opens an out file at shipping.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class WhatsItOpenNode extends WhatsItNode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060411L;

    /**
     * The field <tt>file</tt> contains the output file.
     */
    private OutFile file;

    /**
     * The field <tt>key</tt> contains the reference key.
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
     *
     * @param context the interpreter context
     * @param typesetter the typesetter
     * @param visitor the node visitor to be invoked when the node is hit. Note
     *  that each node in the output page is visited this way. Thus there is no
     *  need to implement a node traversal for the NodeList types
     * @param inHMode <code>true</code> iff the container is a horizontal list.
     *  Otherwise the container is a vertical list
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.typesetter.type.Node#atShipping(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.typesetter.Typesetter,
     *      de.dante.extex.typesetter.type.NodeVisitor,
     *      boolean)
     */
    public Node atShipping(final Context context, final Typesetter typesetter,
            final NodeVisitor visitor, final boolean inHMode)
            throws GeneralException {

        file.open();
        context.setOutFile(key, file, true);

        return null;
    }

}
