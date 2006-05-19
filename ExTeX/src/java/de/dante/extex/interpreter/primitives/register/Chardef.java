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

package de.dante.extex.interpreter.primitives.register;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive
 * <code>\chardef</code>.
 *
 * <doc name="chardef">
 * <h3>The Primitive <tt>\chardef</tt></h3>
 * <p>
 *  The primitive <tt>\chardef</tt> allows you to define a control sequence or
 *  active character to be equivalent to a character. This mean that the new
 *  entity can be used wherever a character is allowed.
 * </p>
 * <p>
 *  A character is represented by a code point; i.e. a positive number denoting
 *  the character position. In <logo>TeX</logo> only 8-bit number where allowed.
 *  In <logo>ExTeX</logo> arbitrary positive numbers are valid as values.
 * </p>
 * <p>
 *  The definition is performed with respect to to group to keep it locally.
 *  The prefix <tt>/global</tt> or the value of <tt>\globaldefs</tt> can
 *  influence the scope.
 * </p>
 * <p>
 *  This primitive is an assignment. All actions around assignments are
 *  performed.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;chardef&rang;
 *      &rarr; <tt>\chardef</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getControlSequence(Context)
 *        &lang;control sequence&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanNumber(Context)
 *        &lang;number&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \chardef\abc=45  </pre>
 *  <pre class="TeXSample">
 *    \chardef\abc 33  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.24 $
 */
public class Chardef extends AbstractAssignment {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Chardef(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractAssignment#assign(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        CodeToken cs = source.getControlSequence(context);
        source.getOptionalEquals(context);
        UnicodeChar uc = source.scanCharacterCode(context, typesetter,
                getName());
        context.setCode(cs, new CharCode(uc.toString(), uc), prefix.isGlobal());
        prefix.clearGlobal();
    }

}
