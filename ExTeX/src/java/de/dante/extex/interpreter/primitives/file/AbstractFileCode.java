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

import de.dante.extex.i18n.EofHelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.SpaceToken;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.framework.configuration.Configurable;

/**
 * This abstract class provides some common methods for primitives dealing with
 * files.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public abstract class AbstractFileCode extends AbstractCode
        implements
            Configurable {

    /**
     * The field <tt>strictTeX</tt> contains the boolean indicating whether or
     * not to adhere strictly to the rules of TeX for file name parsing.
     */
    private boolean strictTeX = false;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public AbstractFileCode(final String name) {

        super(name);
    }

    /**
     * Configure an object according to a given Configuration.
     *
     * @param config the configuration to use
     *
     * @throws ConfigurationException in case of an error
     *
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        String strict = config.getAttribute("strict");
        strictTeX = (strict != null && Boolean.getBoolean(strict));
    }

    /**
     * Return the encoding for the AbstractFileCode file.
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
     * Scan the file name.
     *
     * <doc type="syntax" name="filename">
     * This method parses the following syntactic entity:
     * <pre class="syntax">
     *   &lang;filename&rang; </pre>
     *
     * The scanning is performed in one of two ways:
     * <ul>
     * <li>If the first token is a left brace then a block is read until the
     *   matching right brace is found. On the way the tokens are expanded.
     * </li>
     * <li>Otherwise tokens are read until a space token is encountered.
     * </li>
     * </ul>
     *
     * </doc>
     *
     * @param source the source for new tokens
     * @param context the processing context
     *
     * @return the file name as string
     *
     * @throws GeneralException in case of an error
     */
    protected String scanFileName(final TokenSource source,
            final Context context) throws GeneralException {

        Token t = source.scanNonSpace();

        if (t == null) {
            // Fall through to error
        } else if (strictTeX && t.isa(Catcode.LEFTBRACE)) {
            source.push(t);
            String name = source.scanTokensAsString();
            if (name != null) {
                return name;
            }

        } else {
            StringBuffer sb = new StringBuffer(t.getValue());

            for (t = source.getToken(); //
            t != null && !(t instanceof SpaceToken); //
            t = source.getToken()) {
                sb.append(t.getValue());
            }

            return sb.toString();
        }

        throw new EofHelpingException(printableControlSequence(context));
    }

}