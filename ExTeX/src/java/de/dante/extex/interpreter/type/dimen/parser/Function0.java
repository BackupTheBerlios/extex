
package de.dante.extex.interpreter.type.dimen.parser;

import de.dante.extex.interpreter.exception.InterpreterException;

/**
 * This interface describes a function object without any arguments.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface Function0 {

    /**
     * Return the value in the accumulator overwriting the value stored therein.
     *
     * @param accumulator the accumulator to receive the result
     *
     * @throws InterpreterException in case of an error
     */
    void apply(Accumulator accumulator) throws InterpreterException;

}