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

import java.io.File;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.file.OutFile;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.node.WhatsItOpenNode;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive
 * <code>\openout</code>.
 *
 * <doc name="openout">
 * <h3>The Primitive <tt>\openout</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The primitive <tt>\openout</tt> is not considered as assignment. Nor can
 *  it be prefixed with <tt>\global</tt>. The definition of an output stream is
 *  always global.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;openout&rang;
 *      &rarr; &lang;modifier&rang; <tt>\openout</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanNumber(Context)
 *        &lang;8-bit&nbsp;number&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.primitive.file.AbstractFileCode#scanFileName(Context,TokenSource)
 *        &lang;file name&rang;}
 *
 *    &lang;modifier&rang;
 *      &rarr;
 *       |  <tt>\immediate</tt> &lang;modifier&rang;  </pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 * \immediate\openout3= abc.def
 * \write3{Hi there!}
 * \closeout3 </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.26 $
 */
public class Openout extends AbstractFileCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Openout(final String name) {

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

        String key = AbstractFileCode.scanOutFileKey(context, source, typesetter);

        source.getOptionalEquals(context);
        String name = scanFileName(context, source);

        OutFile file = new OutFile(new File(name));

        if (prefix.isImmediate()) {
            file.open();
            context.setOutFile(key, file, prefix.isGlobal());
            prefix.clearImmediate();
        } else {
            try {
                typesetter.add(new WhatsItOpenNode(key, file));
            } catch (GeneralException e) {
                throw new InterpreterException(e);
            } catch (ConfigurationException e) {
                throw new InterpreterException(e);
            }
        }
    }

}
