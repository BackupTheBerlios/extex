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

package de.dante.extex.interpreter.primitives.omega.ocp.util;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.UndefinedControlSequenceException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.SpaceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class contains utility methods.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public final class OcpUtil {

    /**
     * Creates a new object.
     */
    private OcpUtil() {

    }

    /**
     * Get an ocp file name.
     *
     * @param source the source for new tokens
     * @param context the interpreter context
     *
     * @return the ocp file name
     *
     * @throws InterpreterException in case of an error
     */
    public static String scanOcpFileName(final TokenSource source,
            final Context context) throws InterpreterException {

        StringBuffer sb = new StringBuffer();

        for (Token t = source.getToken(context); //
        t != null && !(t instanceof SpaceToken); //
        t = source.getToken(context)) {
            sb.append(t.toText());
        }

        return sb.toString();
    }

    /**
     * Get an ocp.
     *
     * @param source the source for new tokens
     * @param context the interpreter context
     * @param typesetter the typesetter
     *
     * @return the ocp file name
     *
     * @throws InterpreterException in case of an error
     */
    public static Ocp scanOcp(final TokenSource source, final Context context,
            final Typesetter typesetter) throws InterpreterException {

        Token t = source.getToken(context);
        if (t == null) {
            throw new EofException();
        } else if (t instanceof CodeToken) {
            Code code = context.getCode((CodeToken) t);
            if (code == null) {
                throw new UndefinedControlSequenceException(t.toString());
            } else if (code instanceof OcpConvertible) {
                return ((OcpConvertible) code).convertOcp(context, source,
                        typesetter);
            }

        } else {
            source.push(t);
            return new Ocp(scanOcpFileName(source, context));
        }

        source.push(t);
        throw new HelpingException(
                LocalizerFactory.getLocalizer(OcpUtil.class),
                "Omega.MissingOcp");
    }

}
