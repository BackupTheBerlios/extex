/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.primitives.file;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.SpaceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This abstract class provides some common methods for primitives dealing with
 * files.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public abstract class AbstractFileCode extends AbstractCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public AbstractFileCode(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public abstract void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException;

    /**
     * Return the encoding for the AbstractFileCodefile
     * <p>
     * First of all, <code>\AbstractFileCodeencoding</code> is used,
     * if there is no
     * value, then the property <code>extex.encoding</code> is used,
     * or <code>ISO8859-1</code>,
     * if no entry exists.
     *
     * @param context the context
     *
     * @return the encoding for the AbstractFileCodefile
     */
    protected String getEncoding(final Context context) {
        String encoding = context.getToks("fileencoding").toText().trim();
        if (encoding.length() == 0) {
            String enc = System.getProperty("extex.encoding");
            if (enc != null) {
                encoding = enc;
            } else {
                encoding = "ISO8859-1";
            }
        }
        return encoding;
    }

    /**
     * Scan the file name until a <code>SpaceToken</code> is found.
     *
     * @param source the source for new tokens
     *
     * @return the file name as string
     *
     * @throws GeneralException in case of an error
     */
    protected String scanFileName(final TokenSource source,
            final Context context) throws GeneralException {

        Token t = source.scanNonSpace();

        if (t == null) {
            throw new GeneralHelpingException("UnexpectedEOF",
                    printableControlSequence(context));
        }

        StringBuffer sb = new StringBuffer(t.getValue());

        for (t = source.scanToken(); //
        t != null && !(t instanceof SpaceToken); //
        t = source.scanToken()) {
            sb.append(t.getValue());
        }

        return sb.toString();
    }

}
