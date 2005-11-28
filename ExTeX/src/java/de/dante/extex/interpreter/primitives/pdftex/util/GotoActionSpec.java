/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.pdftex.util;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.typesetter.Typesetter;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class GotoActionSpec extends ActionSpec {

    /**
     * TODO gene: missing JavaDoc
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the action spec found
     *
     * @throws InterpreterException in case of an error
     */
    public static ActionSpec parseActionSpec(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final String name) throws InterpreterException {

        if (source.getKeyword(context, "num")) {
            long num = source.scanNumber(context);
            String id = Long.toString(num);
            return new GotoIdActionSpec(null, id, null);

        } else if (source.getKeyword(context, "name")) {
            String id = source.scanTokensAsString(context, name);
            return new GotoIdActionSpec(null, id, null);

        } else if (source.getKeyword(context, "page")) {
            long page = source.scanNumber(context);
            String text = source.scanTokensAsString(context, name);
            return new GotoPageActionSpec(null, page, text, null);

        } else if (!source.getKeyword(context, "file")) {
            //TODO gene: error unimplemented
            throw new RuntimeException("unimplemented");
        }
        String file = source.scanTokensAsString(context, name);

        if (source.getKeyword(context, "name")) {
            String id = source.scanTokensAsString(context, name);
            Boolean newWindow = null;
            if (source.getKeyword(context, "newwindow")) {
                newWindow = Boolean.TRUE;
            } else if (source.getKeyword(context, "newwindow")) {
                newWindow = Boolean.FALSE;
            }
            return new GotoIdActionSpec(file, id, newWindow);

        } else if (source.getKeyword(context, "page")) {
            long page = source.scanNumber(context);
            String text = source.scanTokensAsString(context, name);
            Boolean newWindow = null;
            if (source.getKeyword(context, "newwindow")) {
                newWindow = Boolean.TRUE;
            } else if (source.getKeyword(context, "newwindow")) {
                newWindow = Boolean.FALSE;
            }
            return new GotoPageActionSpec(file, page, text, newWindow);
        }

        //TODO gene: error unimplemented
        throw new RuntimeException("unimplemented");
    }

}
