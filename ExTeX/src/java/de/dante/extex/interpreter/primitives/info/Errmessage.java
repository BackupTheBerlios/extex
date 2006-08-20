/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.info;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.info.util.FixedHelpingException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\errmessage</code>.
 *
 * <doc name="errmessage">
 * <h3>The Primitive <tt>\errmessage</tt></h3>
 * <p>
 *  The primitive <tt>\errmessage</tt> takes one argument. This argument is an
 *  expanded list of tokens. Those tokens are presented as error message. The
 *  help text is taken from the token register <tt>\errhelp</tt>
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;errmessage&rang;
 *       &rarr; <tt>\errmessage</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanTokens(Context,boolean,boolean,String)
 *        &lang;tokens&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \errhelp={Too bad}
 *    \errmessage{I found an error}  </pre>
 *
 * </doc>
 *
 *
 * <doc name="errhelp" type="register">
 * <h3>The Token Register <tt>\errhelp</tt></h3>
 * <p>
 *  The token register <tt>\errhelp</tt> contains the help text for the
 *  primitive
 *  {@link de.dante.extex.interpreter.primitives.info.Errmessage <tt>\errmessage</tt>}.
 *  Nevertheless this register can be used in any place where a token register
 *  is needed.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;errhelp&rang;
 *      &rarr; <tt>\errhelp</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *        &lang;tokens&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \errhelp={Too bad}
 *    \errmessage{I found an error}  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.22 $
 */
public class Errmessage extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Errmessage(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        String message = source.scanUnprotectedTokens(context, false, false,
                getName()).toText();
        String help = context.getToks("errhelp").toText();
        throw new FixedHelpingException(message, help);
    }

}
