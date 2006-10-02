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

package de.dante.extex.interpreter.expression.term;

import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.expression.EType;
import de.dante.extex.interpreter.expression.exception.CastException;
import de.dante.extex.interpreter.expression.exception.UnsupportedException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class encapsulates a long value for the use in the expression
 * evaluator.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class TCount extends Count implements EType {

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * Cast a terminal to a double.
     *
     * @param t the terminal to cast
     *
     * @return the double encountered
     *
     * @throws CastException in case of an error
     */
    protected static long castTerminal(final EType t) throws CastException {

        if (t instanceof TCount) {
            return ((TCount) t).getValue();
        } else if (t instanceof Accumulator) {
            return castTerminal(((Accumulator) t).getValue());
        } else {
            throw new CastException(t.toString(), LocalizerFactory
                    .getLocalizer(TCount.class).format("Name"));
        }
    }

    /**
     * Creates a new object.
     *
     */
    public TCount() {

        super(0);
    }

    /**
     * Creates a new object.
     *
     * @param value the value
     */
    protected TCount(final long value) {

        super(value);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#add(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType add(final EType t) throws CastException {

        add(castTerminal(t));
        return this;
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#and(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType and(final EType t) throws UnsupportedException {

        throw new UnsupportedException("&&", toString());
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#divide(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType divide(final EType t)
            throws CastException,
                ArithmeticOverflowException {

        super.divide(castTerminal(t));
        return this;
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#eq(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public TBoolean eq(final EType t) throws CastException {

        return new TBoolean(getValue() == castTerminal(t));
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#ge(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public TBoolean ge(final EType t) throws CastException {

        return new TBoolean(getValue() >= castTerminal(t));
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#gt(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public TBoolean gt(final EType t) throws CastException {

        return new TBoolean(getValue() > castTerminal(t));
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#le(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public TBoolean le(final EType t) throws CastException {

        return new TBoolean(getValue() <= castTerminal(t));
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#lt(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public TBoolean lt(final EType t) throws CastException {

        return new TBoolean(getValue() < castTerminal(t));
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#multiply(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType multiply(final EType t) throws CastException {

        multiply(castTerminal(t));
        return this;
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#ne(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public TBoolean ne(final EType t) throws CastException {

        return new TBoolean(getValue() != castTerminal(t));
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#negate()
     */
    public EType negate() {

        set(-getValue());
        return this;
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#not()
     */
    public EType not() throws UnsupportedException {

        throw new UnsupportedException("!", toString());
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#or(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType or(final EType t) throws CastException, UnsupportedException {

        throw new UnsupportedException("||", toString());
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#parse(
     *      java.lang.CharSequence)
     */
    public EType parse(final CharSequence sequence) {

        long val = 0;
        int length = sequence.length();
        if (length == 0) {
            return null;
        }

        int i = 0;
        char c = sequence.charAt(0);

        for (; i < length; i++) {
            c = sequence.charAt(i);
            switch (c) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    val = val * 10 + c - '0';
                    break;
                case ' ':
                    for (; i < length; i++) {
                        c = sequence.charAt(i);
                        if (c != ' ') {
                            return null;
                        }
                    }
                    break;
                default:
                    return null;
            }
        }
        return new TCount(val);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#set(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType set(final EType t) throws CastException {

        set(castTerminal(t));
        return this;
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#subtract(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType subtract(final EType t) throws CastException {

        add(-castTerminal(t));
        return this;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return LocalizerFactory.getLocalizer(TCount.class).format("Format",
                Long.toString(this.getValue()));
    }

}
