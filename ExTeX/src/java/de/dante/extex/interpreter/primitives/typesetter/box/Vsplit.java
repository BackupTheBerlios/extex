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

package de.dante.extex.interpreter.primitives.typesetter.box;

import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.primitives.register.box.AbstractBox;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.box.Boxable;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive <code>\vsplit</code>.
 *
 * <doc name="vsplit">
 * <h3>The Primitive <tt>\vsplit</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;vsplit&rang;
 *       &rarr; <tt>\vsplit</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \vsplit 2 to 123pt  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class Vsplit extends AbstractBox implements Boxable, LogEnabled {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private transient Logger logger = null;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Vsplit(final String name) {

        super(name);
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
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

        NodeList nl = vsplit(context, source, typesetter);
        try {
            typesetter.add(nl);
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.box.Boxable#getBox(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Box getBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return new Box(vsplit(context, source, typesetter));
    }

    /**
     * Perform the operation of the primitive <tt>\vsplit</tt> and return the
     * result as a NodeList.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the nodes of the vlist cut off
     *
     * @throws InterpreterException in case of an error
     */
    private NodeList vsplit(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        String key = getKey(context, source, getName());
        if (!source.getKeyword(context, "to")) {
            throw new HelpingException(getLocalizer(), "TTP.MissingToForVsplit");
        }
        Dimen ht = Dimen.parse(context, source, typesetter);
        Box b = context.getBox(key);
        if (b == null || !b.isVbox()) {
            throw new HelpingException(getLocalizer(), "TTP.SplittingNonVbox",
                    printableControlSequence(context), context.esc("vbox"));
        }
        // TODO gene: set splitmark etc
        try {
            return b.vsplit(ht, //
                    (Count.ONE.le(context.getCount("tracingpages"))
                            ? logger
                            : null));
        } catch (OperationNotSupportedException e) {
            // just to be sure. This should not happen
            throw new HelpingException(getLocalizer(), "TTP.SplittingNonVbox",
                    printableControlSequence(context), context.esc("vbox"));
        }
    }

}
