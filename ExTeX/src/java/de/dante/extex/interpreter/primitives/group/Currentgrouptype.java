/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.interpreter.primitives.group;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\currentgrouptype</code>.
 *
 * <doc name="currentgrouptype">
 * <h3>The Primitive <tt>\currentgrouptype</tt></h3>
 * <p>
 *  TODO gene: missing documentation
 * </p>
 *
 *
 *
 * <table format="rl">
 *  <tr><td> 0</td><td>bottom level (no group)</td></tr>
 *  <tr><td> 1</td><td>simple group</td></tr>
 *  <tr><td> 2</td><td>hbox group</td></tr>
 *  <tr><td> 3</td><td>adjusted hbox group</td></tr>
 *  <tr><td> 4</td><td>vbox group</td></tr>
 *  <tr><td> 5</td><td>vtop group</td></tr>
 *  <tr><td> 6</td><td>align group</td></tr>
 *  <tr><td> 7</td><td>no align group</td></tr>
 *  <tr><td> 8</td><td>output group</td></tr>
 *  <tr><td> 9</td><td>math group</td></tr>
 *  <tr><td>10</td><td>disc group</td></tr>
 *  <tr><td>11</td><td>insert group</td></tr>
 *  <tr><td>12</td><td>vcenter group</td></tr>
 *  <tr><td>13</td><td>math choice group</td></tr>
 *  <tr><td>14</td><td>semi simple group</td></tr>
 *  <tr><td>15</td><td>math shift group</td></tr>
 *  <tr><td>16</td><td>math left group</td></tr>
 * </table>
 *
 *
 *
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;currentgrouptype&rang;
 *      &rarr; <tt>\currentgrouptype</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \the\currentgrouptype  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class Currentgrouptype extends AbstractCode
        implements
            CountConvertible,
            Theable {

    /**
     * The field <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060512L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Currentgrouptype(final String name) {

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

        return context.getGroupType();
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
