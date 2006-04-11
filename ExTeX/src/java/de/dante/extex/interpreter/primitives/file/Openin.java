/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.file.InFile;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\openin</code>.
 *
 * <doc name="openin">
 * <h3>The Primitive <tt>\openin</tt></h3>
 * <p>
 *  The primitive <tt>\openin</tt> tries to open a file or other named resource
 *  for reading. The reference is stored in a read register to be used with
 *  <tt>\read</tt>. If the opening fails then the read register is void. This
 *  can be checked with the primitive <tt>\ifeof</tt>.
 * </p>
 * <p>
 *  The assignment to a read register is local to the current group unless
 *  specified differently. If the prefix <tt>\global</tt> is given then the
 *  read register is assigned globally.
 * </p>
 * <p>
 *  The stream should be closed with <tt>\closein</tt> when not needed any more.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;openin&rang;
 *      &rarr; &lang;modifier&rang; <tt>\openin</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanNumber(Context)
 *        &lang;8-bit&nbsp;number&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.primitive.file.AbstractFileCode#scanFileName(Context,TokenSource)
 *        &lang;file name&rang;}
 *
 *    &lang;modifier&rang;
 *      &rarr;
 *       |  <tt>\global</tt>  </pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 * \openin3= abc.def
 * \read3 to \line
 * \closein3 </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.26 $
 */
public class Openin extends AbstractFileCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 20060411L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Openin(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        String key = AbstractFileCode
                .scanInFileKey(context, source, typesetter);
        source.getOptionalEquals(context);
        String name = scanFileName(context, source);

        InFile file;
        try {
            file = new InFile(source.getTokenStreamFactory().newInstance(name,
                    "tex", getEncoding(context)), false);
        } catch (FileNotFoundException e) {
            file = null;
            //ignored on purpose
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
        context.setInFile(key, file, true);
    }

}
