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

package de.dante.extex.interpreter.primitives.omega.mode;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.scanner.type.token.Token;

/**
 * This is the abstract base class for primitives dealing with an input or
 * output mode as defined by Omega.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public abstract class AbstractModeCode extends AbstractCode {

    /**
     * The constant <tt>INPUT_MODE</tt> contains the key for the input mode.
     */
    protected static final String DEFAULT_INPUT_MODE = "defaultInputMode";

    /**
     * The constant <tt>DEFAULT_INPUT_TRANSLATION</tt> contains the key for the
     * default input translation.
     */
    protected static final String DEFAULT_INPUT_TRANSLATION = "defaultInputTranslation.";

    /**
     * The constant <tt>OUTPUT_MODE</tt> contains the key for the output mode.
     */
    protected static final String DEFAULT_OUTPUT_MODE = "defaultOutputMode";

    /**
     * The constant <tt>DEFAULT_OUTPUT_TRANSLATION</tt> contains the key for the
     * default output translation.
     */
    protected static final String DEFAULT_OUTPUT_TRANSLATION = "defaultOutputTranslation.";

    /**
     * The constant <tt>INPUT_MODE</tt> contains the key for the input mode.
     */
    protected static final String INPUT_MODE = "inputMode";

    /**
     * The constant <tt>INPUT_TRANSLATION</tt> contains the key for the
     * input translation.
     */
    protected static final String INPUT_TRANSLATION = "inputTranslation";

    /**
     * The constant <tt>OUTPUT_MODE</tt> contains the key for the output mode.
     */
    protected static final String OUTPUT_MODE = "outputMode";

    /**
     * The constant <tt>OUTPUT_TRANSLATION</tt> contains the key for the
     * output translation.
     */
    protected static final String OUTPUT_TRANSLATION = "outputTranslation";

    /**
     * Creates a new object.
     *
     * @param codeName the name for tracing
     */
    public AbstractModeCode(final String codeName) {

        super(codeName);
    }

    /**
     * Scan the token source for a keyword describing an input mode.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return mode
     *
     * @throws InterpreterException in case of an error
     */
    protected OmegaMode scanInputMode(final Context context,
            final TokenSource source) throws InterpreterException {

        OmegaMode mode = scanMode(context, source);

        if (mode == null) {
            Token token = source.getToken(context);
            if (token == null) {
                throw new EofException();
            }
            source.push(token);

            throw new BadInputModeException();
        }

        return mode;
    }

    /**
     * Scan the token source for a keyword describing a mode.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return the mode or <code>null</code> if none is found
     *
     * @throws InterpreterException in case of an error
     */
    private OmegaMode scanMode(final Context context, final TokenSource source)
            throws InterpreterException {

        if (source.getKeyword(context, "onebyte")) {
            return OmegaMode.ONEBYTE;
        } else if (source.getKeyword(context, "ebcdic")) {
            return OmegaMode.EBCDIC;
        } else if (source.getKeyword(context, "twobyte")) {
            return OmegaMode.TWOBYTE;
        } else if (source.getKeyword(context, "twobyteLE")) {
            return OmegaMode.TWOBYTE_LE;
        }

        return null;
    }

    /**
     * Scan the token source for a keyword describing an input mode.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return mode
     *
     * @throws InterpreterException in case of an error
     */
    protected OmegaMode scanOutputMode(final Context context,
            final TokenSource source) throws InterpreterException {

        OmegaMode mode = scanMode(context, source);

        if (mode == null) {
            throw new BadOutputModeException();
        }

        return mode;
    }

}
