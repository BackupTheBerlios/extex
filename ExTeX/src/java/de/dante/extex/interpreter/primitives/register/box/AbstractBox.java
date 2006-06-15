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

package de.dante.extex.interpreter.primitives.register.box;

import java.io.Serializable;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.typesetter.Typesetter;

/**
 * This is the abstract base class for primitives dealing with box registers.
 * It provides a method to get the key of a box register.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.20 $
 */
public abstract class AbstractBox extends AbstractCode implements Serializable {

    /**
     * Return the key (the number) for the box register.
     *
     * <doc type="syntax" name="box register name">
     * <h3>A Box Register Name</h3>
     * <p>
     *  A box register name determines under which key a box register can be
     *  addressed. In <logo>TeX</logo> this used to be a positive number only.
     *  This has been extended to allow also a token list in braces.
     * </p>
     * <p>
     *  The alternative is controlled by the count register
     *  <tt>\register.max</tt>. The following interpretation of the value of this
     *   count is used:
     *  <ul>
     *   <li>If the value of this count register is negative
     *    then a arbitrary non-negative number is allowed as register name
     *    as well as any list of tokens enclosed in braces.</li>
     *   <li>If the value of this count register is not-negative
     *    then a only a non-negative number is allowed as register name
     *    which does not exceed the value of the count register.</li>
     *  </ul>
     * </p>
     * <p>
     *  The value of the count register <tt>\register.max</tt> is set differently
     *  for various configurations of <logo>ExTeX</logo>:
     *  <ul>
     *   <li><logo>TeX</logo> uses the value 255.</li>
     *   <li><logo>eTeX</logo> uses the value 32767.</li>
     *   <li><logo>Omega</logo> uses the value 65536.</li>
     *   <li><logo>ExTeX</logo> uses the value -1.</li>
     *  </ul>
     * </p>
     * <p>
     *  Note that the register name <tt>\register.max</tt> contains a period.
     *  Thus it can normally not be entered easily since the catcode of the
     *  period is OTHER but needs to be LETTER. Thus you have to use a
     *  temporarily reassigned category code (see
     *  {@link de.dante.extex.interpreter.primitives.register.CatcodePrimitive <tt>\catcode}</tt>)
     *   or use
     *  {@link de.dante.extex.interpreter.primitives.macro.Csname <tt>\csname</tt>}.
     * </p>
     *
     * <h4>Syntax</h4>
     * <pre class="syntax">
     *   &lang;box register name&rang;
     *       &rarr; {@linkplain
     *        de.dante.extex.interpreter.TokenSource#scanTokens(Context)
     *        &lang;tokens&rang;}
     *        | {@linkplain de.dante.extex.interpreter.TokenSource#scanNumber(Context)
     *        &lang;number&rang;}  </pre>
     *
     * <h4>Examples</h4>
     * <pre class="TeXSample">
     *  123
     *  {abc}
     * </pre>
     * </doc>
     *
     *
     * @param context the interpreter context to use
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param name the name of the primitive for error messages
     *
     * @return the key for the box register
     *
     * @throws InterpreterException in case of an error
     */
    public static String getKey(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final String name) throws InterpreterException {

        String key = source.scanRegisterName(context, source, typesetter, name);

        if (Namespace.SUPPORT_NAMESPACE_BOX) {
            return context.getNamespace() + "#box#" + key;
        } else {
            return "box#" + key;
        }
    }

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public AbstractBox(final String name) {

        super(name);
    }

}
