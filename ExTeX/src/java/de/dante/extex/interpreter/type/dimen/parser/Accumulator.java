package de.dante.extex.interpreter.type.dimen.parser;

import de.dante.extex.interpreter.type.scaled.ScaledNumber;

/**
 * This data type contains an accumulator which can contain values of
 * different kinds.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Accumulator {

    /**
     * The field <tt>sp</tt> contains the number of sp units encountered.
     */
    protected int sp;

    /**
     * The field <tt>value</tt> contains the numerical value in multiples of
     * 2<sup>-16</sup>.
     */
    protected long value;

    /**
     * Creates a new object.
     */
    public Accumulator() {

        super();
        this.sp = 0;
        this.value = 0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        ScaledNumber.toString(sb, value);
        if (sp != 0) {
            sb.append("sp");
            if (sp != 1) {
                sb.append("^");
                sb.append(sp);
            }
        }
        return sb.toString();
    }
}