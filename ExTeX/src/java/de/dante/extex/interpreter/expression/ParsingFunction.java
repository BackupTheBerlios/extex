
package de.dante.extex.interpreter.expression;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.typesetter.Typesetter;

/**
 * This interface describes a function object which is able to parse its
 * arguments from a token stream.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface ParsingFunction {

    /**
     * Acquire arguments and compute a function.
     *
     * @param accumulator the accumulator to receive the result
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @throws InterpreterException in case of an error
     */
    EType apply(Context context, TokenSource source, Typesetter typesetter)
            throws InterpreterException;

}