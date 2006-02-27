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

import java.io.IOException;
import java.util.logging.Logger;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.file.OutFile;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This WhatsIt node closes an out file on shipping.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class WhatsItCloseNode extends WhatsItNode implements LogEnabled {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>key</tt> contains the key of the outfile to close.
     */
    private String key;

    /**
     * The field <tt>logger</tt> contains the logger to use.
     */
    private Logger logger = null;

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
     * @param typesetter the typesetter
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.typesetter.type.Node#atShipping(
     *      de.dante.extex.interpreter.context.Context, Typesetter)
     */
    public void atShipping(final Context context, final Typesetter typesetter)
            throws GeneralException {

        OutFile file = context.getOutFile(key);
        if (file != null) {
            try {
                file.close();
            } catch (IOException e) {
                logger.info(e.getLocalizedMessage() + "\n");
            }
        }
    }

    /**
     * Setter for the logger.
     *
     * @param theLogger the new logger
     *
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

}
