/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

import java.io.FileNotFoundException;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.configuration.ConfigurationException;

/**
 * This class provides an implementation for the primitive
 * <code>\input</code>. It uses the standard encoding (see
 * <code>\inputencoding</code> and <code>extex.encoding</code>.
 *
 * <doc name="input">
 * <h3>The Primitive <tt>\input</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;input&rang;
 *       &rarr; <tt>\input</tt> &lang;filename&rang; </pre>
 * </p>
 * <p>
 *  Examples:<br/>
 *  The traditional version of the file name parsing allows the following
 *  syntax:
 *  <pre class="TeXSample">
 *    \input file.name  </pre>
 *  If the parsing is not configured to be strict then the following syntax
 *  is allowed as well:
 *  <pre class="TeXSample">
 *    \input{file.name}  </pre>
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.21 $
 */
public class Input extends AbstractFileCode {

    /**
     * The field <tt>FILE_TYPE</tt> contains the file type to create an input
     * stream for.
     */
    private static final String FILE_TYPE = "tex";
    //TODO gene: is "tex" as constant good?

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Input(final String name) {

        super(name);
    }

    /**
     * Scan the file name and put the file onto the stack of tokenizer streams.
     *
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String name = scanFileName(context, source);
        String encoding = getEncoding(context);
        TokenStreamFactory factory = source.getTokenStreamFactory();

        try {
            source.addStream(factory.newInstance(name, FILE_TYPE, encoding));
        } catch (FileNotFoundException e) {
            throw new GeneralException(e);
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        }
    }

}