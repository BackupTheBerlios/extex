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

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.TokensWriter;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;

/**
 * This WhatsIt node writes some expanded tokens to an out file on shipping.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class WhatsItWriteNode extends WhatsItNode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The field <tt>key</tt> contains the key of the outfile to write to.
     */
    private String key;

    /**
     * The field <tt>tokens</tt> contains the tokens to expand and write.
     */
    private Tokens tokens;

    /**
     * The field <tt>writer</tt> contains the writer used as target when the
     * node is shipped out.
     */
    private TokensWriter writer;

    /**
     * The field <tt>interpreter</tt> contains the ...
     */
    private TokenSource source;

    /**
     * Creates a new object.
     *
     * @param key the key for the OutFile
     * @param tokens the tokens to write (after expansion)
     * @param source the interpreter for expansion
     * @param writer the target writer
     */
    public WhatsItWriteNode(final String key, final Tokens tokens,
            final TokenSource source, final TokensWriter writer) {

        super();
        this.key = key;
        this.tokens = tokens;
        this.source = source;
        this.writer = writer;
    }

    /**
     * This method performs any actions which are required to be executed at the
     * time of shipping the node to the DocumentWriter. In the case od this node
     * this means that the tokens are expanded and written to an output writer.
     *
     * @param context the interpreter context
     * @param typesetter the typesetter
     *
     * @throws GeneralException in case of an IO error
     *
     * @see de.dante.extex.typesetter.type.Node#atShipping(
     *      de.dante.extex.interpreter.context.Context, Typesetter)
     */
    public void atShipping(final Context context, final Typesetter typesetter)
            throws GeneralException {

        Tokens toks = source.expand(tokens, typesetter);
        writer.write(key, toks, context);
    }

}