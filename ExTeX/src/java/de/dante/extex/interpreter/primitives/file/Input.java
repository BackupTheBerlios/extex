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
 * <code>\input</code>. It use the standardencoding (see
 * <code>\inputencoding</code> and <code>extex.encoding</code>.
 *
 * <doc name="input">
 * <h3>The Primitive <tt>\input</tt></h3>
 * <p>
 *  ...
 * </p>
 * </doc>
 *
 * Example:
 *
 * <pre>
 *  \input file.name
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.13 $
 */
public class Input extends AbstractFileCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Input(final String name) {
        super(name);
    }

    /**
     * Scan the filename (until a <code>SpaceToken</code>) and put the file
     * onto the tokenizer stream.
     *
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String name = scanFileName(source, context);
        TokenStreamFactory factory = source.getTokenStreamFactory();
        String encoding = getEncoding(context);

        try {
            source.addStream(factory.newInstance(name, "tex", encoding));
        } catch (FileNotFoundException e) {
            throw new GeneralException(e);
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        //} catch (IOException e) {
        //    throw new GeneralException(e);
        }
        return true;
    }

}
