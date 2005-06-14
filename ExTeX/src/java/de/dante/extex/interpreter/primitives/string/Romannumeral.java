/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.string;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\romannumeral</code>.
 *
 * <doc name="romannumeral">
 * <h3>The Primitive <tt>\romannumeral</tt></h3>
 * <p>
 *  The primitive <tt>\romannumeral</tt> takes a single argument of a number and
 *  produces the representation of this number in lower case roman numerals.
 *  If the number is less than one than nothing is produced at all.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;romannumeral&rang;
 *        &rarr; <tt>\romannumeral</tt> {@linkplain
 *           de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *           &lang;number&rang;} </pre>
 * </p>
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \romannumeral\count1  </pre>
 *  <pre class="TeXSample">
 *    \romannumeral 2004  </pre>
 * </doc>
 *
 *
 * @see "<logo>TeX</logo> &ndash; the Program [69]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.18 $
 */
public class Romannumeral extends AbstractCode implements ExpandableCode {

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Romannumeral(final String name) {

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

        expand(prefix, context, source, typesetter);
    }

    /**
     * @see de.dante.extex.interpreter.type.ExpandableCode#expand(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        long n = source.scanInteger(context, typesetter);
        Tokens toks = new Tokens();
        TokenFactory factory = context.getTokenFactory();
        char[] magic = {'m', '2', 'd', '5', 'c', '2', 'l', '5', 'x', '2', 'v',
                '5', 'i'};
        int j = 0;
        int v = 1000;

        for (;;) {
            while (n >= v) {
                try {
                    toks.add(factory.createToken(Catcode.LETTER, magic[j],
                            Namespace.DEFAULT_NAMESPACE));
                } catch (CatcodeException e) {
                    throw new InterpreterException(e);
                }
                n = n - v;
            }

            if (n <= 0) {
                return; //nonpositive input produces no output
            }

            int k = j + 2;
            int u = v / (magic[k - 1] - '0');
            if (magic[k - 1] == '2') {
                k = k + 2;
                u = u / (magic[k - 1] - '0');
            }
            if (n + u >= v) {
                try {
                    toks.add(factory.createToken(Catcode.LETTER, magic[k], ""));
                } catch (CatcodeException e) {
                    throw new InterpreterException(e);
                }
                n = n + u;
            } else {
                j = j + 2;
                v = v / (magic[j - 1] - '0');
            }
        }
    }

}