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
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class WhatsItWriteNode extends WhatsItNode {

    /**
     * The field <tt>key</tt> contains the key of the outfile to write to.
     */
    private String key;

    /**
     * The field <tt>tokens</tt> contains the tokens to expand and write.
     */
    private Tokens tokens;

    /**
     * Creates a new object.
     *
     * @param theKey the key for the OutFile
     * @param theTokens the tokens to write (after expansion)
     */
    public WhatsItWriteNode(final String theKey, final Tokens theTokens) {

        super();
        this.key = theKey;
        this.tokens = theTokens;
    }

    /**
     * This method performs any action which are required to be executed at the
     * time of shipping the node to the DocumentWriter.
     *
     * @param context the interpreter context
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.typesetter.Node#atShipping(
     *      de.dante.extex.interpreter.context.Context)
     */
    public void atShipping(final Context context) throws GeneralException {

        OutFile file = context.getOutFile(key);
        Typesetter typesetter = null; //TODO fill with value
        Tokens toks = context.expand(tokens, typesetter);

        if (file == null || !file.isOpen()) {
            // TODO stdout unimplemented
            //source.update("message", toks.toText());
        } else {
            file.write(toks);
        }
    }
}