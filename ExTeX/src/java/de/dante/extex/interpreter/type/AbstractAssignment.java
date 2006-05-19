/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This is the base class for assignments.
 * The assignments are implemented like any Code with the exception that the
 * method <tt>assign</tt> is used instead of the method <tt>execute</tt>.
 *
 * <p>
 *  This abstract class takes care of the treatment of the <tt>\afterassign</tt>
 *  token and the <tt>\globaldefs</tt> declaration.
 * </p>
 *
 *
 * <doc name="globaldefs" type="register">
 * <h3>The Count Parameter <tt>\globaldefs</tt></h3>
 * <p>
 *  The count register <tt>\globaldefs</tt> contains the indicator that an
 *  assignment should be performed globally. If its value is greater than zero
 *  then all assignments are global. Otherwise the grouping is honored. In this
 *  sense setting <tt>\globaldefs</tt> to a positive value implicitly
 *  prefixes all assignments with <tt>\global</tt>.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;globaldefs&rang;
 *       &rarr; <tt>\globaldefs</tt> ...  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \globaldefs=1  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public abstract class AbstractAssignment extends AbstractCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public AbstractAssignment(final String name) {

        super(name);
    }

    /**
     * The method <tt>assign</tt> is the core of the functionality of
     * {@link #execute(Flags, Context, TokenSource, Typesetter) execute()}.
     * This method is preferable to <tt>execute()</tt> since the
     * <tt>execute()</tt> method provided in this class takes care of
     * <tt>\afterassignment</tt> and <tt>\globaldefs</tt> as well.
     *
     * @param prefix the prefix controlling the execution
     * @param context the interpreter context
     * @param source the token source
     * @param typesetter the typesetter
     *
     * @throws InterpreterException in case of an error
     */
    public abstract void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException;

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public final void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        long globaldef = context.getCount("globaldefs").getValue();
        if (globaldef != 0) {
            prefix.setGlobal((globaldef > 0));
        }

        assign(prefix, context, source, typesetter);

        Token afterassignment = context.getAfterassignment();
        if (afterassignment != null) {
            context.setAfterassignment(null);
            source.push(afterassignment);
        }
    }

}
