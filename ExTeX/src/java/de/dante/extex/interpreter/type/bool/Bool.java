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

package de.dante.extex.interpreter.type.bool;

import java.io.Serializable;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.real.RealConvertible;
import de.dante.extex.scanner.type.ControlSequenceToken;
import de.dante.extex.scanner.type.Token;
import de.dante.util.GeneralException;

/**
 * Bool
 *
 * @author <a href="mailto:m.g.sn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */
public class Bool implements Serializable {

    /**
     * The value
     */
    private boolean value = false;

    /**
     * Creates a new object.
     */
    public Bool() {

        super();
    }

    /**
     * Creates a new object.
     *
     * @param val init with a boolean-value
     */
    public Bool(final boolean val) {

        super();
        value = val;
    }

    /**
     * Creates a new object.
     * Scan the <code>TokenSource</code> for a <code>Bool</code>.
     * @param context   the context
     * @param source    the token source
     * @throws InterpreterException ...
     */
    public Bool(final Context context, final TokenSource source)
            throws InterpreterException {

        super();
        value = scanBool(context, source);
    }

    /**
     * Scan the input stream for tokens making up a <code>Bool</code>.
     *
     * @param context   the context
     * @param source    the tokensource
     * @return the boolean value
     * @throws InterpreterException in case of an error
     */
    private boolean scanBool(final Context context, final TokenSource source)
            throws InterpreterException {

        Token tok = source.scanNonSpace(context);

        if (tok == null) {
            throw new HelpingException("TTP.NoBoolValue");
        } else if (tok instanceof ControlSequenceToken) {
            Code code = context.getCode((ControlSequenceToken) tok);
            if (code instanceof CountConvertible) {
                return (new Bool(((CountConvertible) code).convertCount(
                        context, source, null))).getValue();
            } else if (code instanceof RealConvertible) {
                return (new Bool(((((RealConvertible) code).convertReal(
                        context, source)).getLong()))).getValue();
            } else {
                throw new HelpingException("TTP.NoBoolValue");
            }
        }

        source.push(tok);
        if (tok.getChar().isDigit()) {
            return (new Bool(Count.scanCount(context, source, null))).getValue();
        }

        if (source.getKeyword(context, "true")) {
            return true;
        } else if (source.getKeyword(context, "false")) {
            return false;
        } else if (source.getKeyword(context, "on")) {
            return true;
        } else if (source.getKeyword(context, "off")) {
            return false;
        }
        throw new HelpingException("TTP.NoBoolValue");

    }

    /**
     * Creates a new object.
     * <p>0  -> false</p>
     * <p>!= -> true</p>
     * @param l the value as long
     */
    public Bool(final long l) {

        if (l == 0) {
            value = false;
        } else {
            value = true;
        }
    }

    /**
     * Creates a new object.
     * Possible values are
     * <tt>true</tt>, <tt>false</tt> or
     * <tt>on</tt>, <tt>off</tt> or
     * <tt>!0</tt>, <tt>0</tt>
     * @param s     the value as String
     * @throws GeneralException if no boolean-value are found
     */
    public Bool(final String s) throws GeneralException {

        try {
            if ("true".equalsIgnoreCase(s)) {
                value = true;
            } else if ("on".equalsIgnoreCase(s)) {
                value = true;
            } else if ("off".equalsIgnoreCase(s)) {
                value = false;
            } else if ("false".equalsIgnoreCase(s)) {
                value = false;
            } else if (s.trim().length() == 0) {
                value = false;
            } else if (Integer.parseInt(s) == 0) {
                value = false;
            } else if (Integer.parseInt(s) != 0) {
                value = true;
            } else {
                throw new HelpingException("TTP.NoBoolValue", s);
            }
        } catch (Exception e) {
            throw new HelpingException("TTP.NoBoolValue", s);
        }
    }

    /**
     * Setter for the value.
     * @param b the new value
     */
    public void setValue(final boolean b) {

        value = b;
    }

    /**
     * Getter for the value
     *
     * @return the value
     */
    public boolean getValue() {

        return value;
    }

    /**
     * not
     */
    public void not() {

        value = !value;
    }

    /**
     * Return the value as <code>String</code>
     * @return the value as <code>String</code>
     */
    public String toString() {

        return Boolean.toString(value);
    }
}