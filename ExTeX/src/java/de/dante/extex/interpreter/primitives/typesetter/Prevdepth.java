/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.typesetter;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.CantUseInException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.DimenConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.exception.TypesetterUnsupportedException;

/**
 * This class provides an implementation for the primitive
 * <code>\prevdepth</code>.
 *
 * <doc name="prevdepth">
 * <h3>The Primitive <tt>\prevdepth</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;prevdepth&rang;
 *      &rarr; <tt>\prevdepth ...</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \prevdepth ...  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Prevdepth extends AbstractAssignment
        implements
            CountConvertible,
            DimenConvertible,
            Theable {

    /**
     * The field <tt>IGNORE</tt> contains the numerical value which represents
     * the ignored value. This will be mapped to null.
     */
    private static final long IGNORE = -65536000;

    /**
     * The field <tt>IGNORE_DIMEN</tt> contains the ...
     */
    private static final Dimen IGNORE_DIMEN = new Dimen(IGNORE);

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Prevdepth(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        source.getOptionalEquals(context);
        Dimen pd = new Dimen(context, source, typesetter);
        if (pd.getValue() == IGNORE) {
            pd = null;
        }
        try {
            typesetter.setPrevDepth(pd);
        } catch (TypesetterUnsupportedException e) {
            throw new CantUseInException(printableControlSequence(context),
                    typesetter.getMode().toString());
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Dimen prevDepth;
        try {
            prevDepth = typesetter.getListMaker().getPrevDepth();
        } catch (TypesetterUnsupportedException e) {
            throw new HelpingException(getLocalizer(), "TTP.ImproperSForPD",
                    printableControlSequence(context));
        }

        return prevDepth != null ? prevDepth.getValue() : IGNORE;
    }

    /**
     * @see de.dante.extex.interpreter.type.dimen.DimenConvertible#convertDimen(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertDimen(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Dimen prevDepth;
        try {
            prevDepth = typesetter.getListMaker().getPrevDepth();
        } catch (TypesetterUnsupportedException e) {
            throw new HelpingException(getLocalizer(), "TTP.ImproperSForPD",
                    printableControlSequence(context));
        }

        return prevDepth != null ? prevDepth.getValue() : IGNORE;
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Dimen prevDepth;
        try {
            prevDepth = typesetter.getListMaker().getPrevDepth();
        } catch (TypesetterUnsupportedException e) {
            throw new HelpingException(getLocalizer(), "TTP.ImproperSForPD",
                    printableControlSequence(context));
        }

        if (prevDepth ==null) {
            prevDepth = IGNORE_DIMEN;
        }

        try {
            return prevDepth.toToks(context.getTokenFactory());
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        }
    }
}
