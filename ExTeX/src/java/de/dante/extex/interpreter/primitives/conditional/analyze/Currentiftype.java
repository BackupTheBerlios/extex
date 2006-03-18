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
import de.dante.extex.interpreter.type.count.Count;
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
 *  The primitive <tt>\currentiftype</tt> is an internal count register.
 *  It returns an indication of the conditional currently in use.
 *  If no conditional is active then 0 is returned.
 *  The following table lists the return values for the different types of
 *  conditionals:
 * </p>
 * <table>
 *  <tr><td><tt>/if</tt></td><td>1</td></tr>
 *  <tr><td><tt>/ifcat</tt></td><td>2</td></tr>
 *  <tr><td><tt>/ifnum</tt></td><td>3</td></tr>
 *  <tr><td><tt>/ifdim</tt></td><td>4</td></tr>
 *  <tr><td><tt>/ifodd</tt></td><td>5</td></tr>
 *  <tr><td><tt>/ifvmode</tt></td><td>6</td></tr>
 *  <tr><td><tt>/ifhmode</tt></td><td>7</td></tr>
 *  <tr><td><tt>/ifmmode</tt></td><td>8</td></tr>
 *  <tr><td><tt>/ifinner</tt></td><td>9</td></tr>
 *  <tr><td><tt>/ifvoid</tt></td><td>10</td></tr>
 *  <tr><td><tt>/ifhbox</tt></td><td>11</td></tr>
 *  <tr><td><tt>/ifvbox</tt></td><td>12</td></tr>
 *  <tr><td><tt>/ifx</tt></td><td>13</td></tr>
 *  <tr><td><tt>/ifeof</tt></td><td>14</td></tr>
 *  <tr><td><tt>/iftrue</tt></td><td>15</td></tr>
 *  <tr><td><tt>/iffalse</tt></td><td>16</td></tr>
 *  <tr><td><tt>/ifcase</tt></td><td>17</td></tr>
 *  <tr><td><tt>/ifdefined</tt></td><td>18</td></tr>
 *  <tr><td><tt>/ifcsname</tt></td><td>19</td></tr>
 *  <tr><td><tt>/iffontchar</tt></td><td>20</td></tr>
 * </table>
 * <p>
 *  The value returned by the primitive is negated if the expansion appears
 *  in the else branch.
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
 *    \count0=\currentiftype  </pre>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
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
        MAP.put(If.class, new Count(1));
        MAP.put(Ifcat.class, new Count(2));
        MAP.put(Ifnum.class, new Count(3));
        MAP.put(Ifdim.class, new Count(4));
        MAP.put(Ifodd.class, new Count(5));
        MAP.put(Ifvmode.class, new Count(6));
        MAP.put(Ifhmode.class, new Count(7));
        MAP.put(Ifmmode.class, new Count(8));
        MAP.put(Ifinner.class, new Count(9));
        MAP.put(Ifvoid.class, new Count(10));
        MAP.put(Ifhbox.class, new Count(11));
        MAP.put(Ifvbox.class, new Count(12));
        MAP.put(Ifx.class, new Count(13));
        MAP.put(Ifeof.class, new Count(14));
        MAP.put(Iftrue.class, new Count(15));
        MAP.put(Iffalse.class, new Count(16));
        MAP.put(Ifcase.class, new Count(17));
        MAP.put(Ifdefined.class, new Count(18));
        MAP.put(Ifcsname.class, new Count(19));
        MAP.put(Iffontchar.class, new Count(20));
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
        Count l = (Count) MAP.get(conditional.getPrimitive().getClass());
        return (l == null ? 0 : //
                conditional.isNeg() ? -l.getValue() : l.getValue());
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
