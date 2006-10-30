/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.omega.dir;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.tc.Direction;
import de.dante.extex.interpreter.context.tc.Direction.Dir;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.Token;

/**
 * This is the abstract base class for primitives acquiring a direction.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class AbstractDirCode extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2006L;

    /**
     * Creates a new object.
     *
     * @param codeName the name of the primitive for tracing
     */
    public AbstractDirCode(final String codeName) {

        super(codeName);
    }

    /**
     * Scan a direction specification.
     *
     * @param source the source for new tokens
     * @param context the interpreter context
     *
     * @return the direction or null
     *
     * @throws InterpreterException in case of an error
     */
    protected Direction scanDir(final TokenSource source, final Context context)
            throws InterpreterException {

        Dir a = scan(source, context);
        if (a == null) {
            return null;
        }
        Dir b = scan(source, context);
        if (b == null) {
            return null;
        }
        Dir c = scan(source, context);
        if (c == null) {
            return null;
        }

        return new Direction(a, b, c);
    }

    /**
     * Scan a direction component specification.
     *
     * @param source the source for new tokens
     * @param context the interpreter context
     *
     * @return the direction found
     *
     * @throws InterpreterException in case of an error
     */
    private Dir scan(final TokenSource source, final Context context)
            throws InterpreterException {

        Token t = source.scanToken(context);

        if (t == null) {
            throw new EofException();
        } else if (t.equals(Catcode.LETTER, 'L')) {
            return Direction.L;
        } else if (t.equals(Catcode.LETTER, 'R')) {
            return Direction.R;
        } else if (t.equals(Catcode.LETTER, 'T')) {
            return Direction.T;
        } else if (t.equals(Catcode.LETTER, 'B')) {
            return Direction.B;
        }

        source.push(t);
        throw new BadDirectionException();
    }

}
