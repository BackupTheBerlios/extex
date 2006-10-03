
package de.dante.extex.interpreter.expression.term;

import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.expression.EType;
import de.dante.extex.interpreter.expression.exception.CastException;
import de.dante.extex.interpreter.expression.exception.UnsupportedException;

/**
 * This data type contains an accumulator which can contain values of
 * different kinds.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Accumulator implements EType {

    /**
     * The field <tt>value</tt> contains the encapsulated value.
     */
    private EType value;

    /**
     * Creates a new object.
     */
    public Accumulator() {

        super();
        this.value = null;
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#add(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType add(final EType t)
            throws CastException,
                UnsupportedException {

        return (value != null ? value.add(t) : null);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#and(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType and(final EType t)
            throws CastException,
                UnsupportedException {

        return (value != null ? value.and(t) : null);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#divide(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType divide(final EType t)
            throws CastException,
                UnsupportedException,
                ArithmeticOverflowException {

        return (value != null ? value.divide(t) : null);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#eq(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public TBoolean eq(final EType t)
            throws CastException,
                UnsupportedException {

        return (value != null ? value.eq(t) : null);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#ge(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public TBoolean ge(final EType t)
            throws CastException,
                UnsupportedException {

        return (value != null ? value.ge(t) : null);
    }

    /**
     * Getter for value.
     *
     * @return the value
     */
    public EType getValue() {

        return this.value;
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#gt(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public TBoolean gt(final EType t)
            throws CastException,
                UnsupportedException {

        return (value != null ? value.gt(t) : null);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#le(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public TBoolean le(final EType t)
            throws CastException,
                UnsupportedException {

        return (value != null ? value.le(t) : null);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#lt(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public TBoolean lt(final EType t)
            throws CastException,
                UnsupportedException {

        return (value != null ? value.lt(t) : null);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#multiply(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType multiply(final EType t)
            throws CastException,
                UnsupportedException {

        return (value != null ? value.multiply(t) : null);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#ne(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public TBoolean ne(final EType t)
            throws CastException,
                UnsupportedException {

        return (value != null ? value.ne(t) : null);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#negate()
     */
    public EType negate() throws UnsupportedException {

        return (value != null ? value.negate() : null);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#not()
     */
    public EType not() {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#or(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType or(final EType t)
            throws CastException,
                UnsupportedException {

        return (value != null ? value.or(t) : null);
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#set(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType set(final EType t) {

        if (t instanceof Accumulator) {
            value = ((Accumulator) t).value;
        } else {
            value = t;
        }
        return this;
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#subtract(
     *      de.dante.extex.interpreter.expression.EType)
     */
    public EType subtract(final EType t)
            throws CastException,
                UnsupportedException {

        value = value.subtract(t);
        return this;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return value.toString();
    }

}
