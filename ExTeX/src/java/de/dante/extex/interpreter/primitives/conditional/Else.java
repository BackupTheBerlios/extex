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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\else</code>.
 *
 * <doc name="else">
 * <h3>The Primitive <tt>\else</tt></h3>
 * <p>
 *  The primitive <tt>\else</tt> can not be used alone. It always comes in
 *  conjunction with a conditional. A isolated <tt>\else</tt> leads to an
 *  error immediately.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;else&rang;
 *      &rarr; <tt>\else</tt> &lang;...&rang; </pre>
 * </p>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \ifnum 1<2\else no\fi  </pre>
 * </doc>
 *
 *
 * Note:<br />
 * This primitive is <emph>not</emph> expandable!
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.25 $
 */
public class Else extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Else(final String name) {

        super(name);
    }

    /**
     * Executes the primitive.
     * <p>
     *  This primitive can only be seen when a conditional has been opened
     *  before for which the then branch is expanded. Thus the else branch
     *  has to be skipped. Additionally the conditional stack has to be
     *  updated. If the conditional stack is already empty then an exception
     *  is raised.
     * </p>
     *
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Conditional cond = context.popConditional();

        if (cond == null || AbstractIf.skipToElseOrFi(context, source)) {
            throw new HelpingException(getLocalizer(), "TTP.ExtraOrElseFi",
                    printableControlSequence(context));
        }
    }

}
