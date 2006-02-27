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

package de.dante.extex.interpreter.primitives.conditional.analyze;

import java.util.HashMap;
import java.util.Map;

import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.conditional.If;
import de.dante.extex.interpreter.primitives.conditional.Ifcase;
import de.dante.extex.interpreter.primitives.conditional.Ifcat;
import de.dante.extex.interpreter.primitives.conditional.Ifcsname;
import de.dante.extex.interpreter.primitives.conditional.Ifdefined;
import de.dante.extex.interpreter.primitives.conditional.Ifdim;
import de.dante.extex.interpreter.primitives.conditional.Ifeof;
import de.dante.extex.interpreter.primitives.conditional.Iffalse;
import de.dante.extex.interpreter.primitives.conditional.Iffontchar;
import de.dante.extex.interpreter.primitives.conditional.Ifhbox;
import de.dante.extex.interpreter.primitives.conditional.Ifhmode;
import de.dante.extex.interpreter.primitives.conditional.Ifinner;
import de.dante.extex.interpreter.primitives.conditional.Ifmmode;
import de.dante.extex.interpreter.primitives.conditional.Ifnum;
import de.dante.extex.interpreter.primitives.conditional.Ifodd;
import de.dante.extex.interpreter.primitives.conditional.Iftrue;
import de.dante.extex.interpreter.primitives.conditional.Ifvbox;
import de.dante.extex.interpreter.primitives.conditional.Ifvmode;
import de.dante.extex.interpreter.primitives.conditional.Ifvoid;
import de.dante.extex.interpreter.primitives.conditional.Ifx;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\currentiftype</code>.
 *
 * <doc name="currentiftype">
 * <h3>The Primitive <tt>\currentiftype</tt></h3>
 * <p>
 *  TODO gene: missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;if&rang;
 *     &rarr; <tt>\currentiftype</tt> </pre>
 * </p>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \currentiftype  </pre>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Currentiftype extends AbstractCode
        implements
            CountConvertible,
            Theable {

    /**
     * The field <tt>map</tt> contains the map from \if implementations to
     * long values.
     */
    private static final Map MAP = new HashMap();

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    {
        MAP.put(If.class, new Long(1));
        MAP.put(Ifcat.class, new Long(2));
        MAP.put(Ifnum.class, new Long(3));
        MAP.put(Ifdim.class, new Long(4));
        MAP.put(Ifodd.class, new Long(5));
        MAP.put(Ifvmode.class, new Long(6));
        MAP.put(Ifhmode.class, new Long(7));
        MAP.put(Ifmmode.class, new Long(8));
        MAP.put(Ifinner.class, new Long(9));
        MAP.put(Ifvoid.class, new Long(10));
        MAP.put(Ifhbox.class, new Long(11));
        MAP.put(Ifvbox.class, new Long(12));
        MAP.put(Ifx.class, new Long(13));
        MAP.put(Ifeof.class, new Long(14));
        MAP.put(Iftrue.class, new Long(15));
        MAP.put(Iffalse.class, new Long(16));
        MAP.put(Ifcase.class, new Long(17));
        MAP.put(Ifdefined.class, new Long(18));
        MAP.put(Ifcsname.class, new Long(19));
        MAP.put(Iffontchar.class, new Long(20));
    }

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Currentiftype(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Conditional conditional = context.getConditional();
        if (conditional == null) {
            return 0;
        }
        Long l = (Long) MAP.get(conditional.getPrimitive().getClass());
        return (l == null ? 0 : //
                conditional.isNeg() ? -l.longValue() : l.longValue());
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return new Tokens(context, convertCount(context, source, typesetter));
    }

}
