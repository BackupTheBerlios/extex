
package de.dante.extex.interpreter.expression;

import de.dante.extex.interpreter.exception.InterpreterException;

/**
 * This interface describes an operation object without any arguments.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface ConstantFunction {

    /**
     * Return the value in the accumulator overwriting the value stored therein.
     *
     * @return the operation result, i.e. the constant
     *
     * @throws InterpreterException in case of an error
     */
    EType apply() throws InterpreterException;

}