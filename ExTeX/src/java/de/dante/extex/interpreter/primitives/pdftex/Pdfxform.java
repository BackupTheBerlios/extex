/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.pdftex;

import de.dante.extex.backend.documentWriter.PdftexSupport;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.node.pdftex.PdfXForm;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\pdfxform</code>.
 *
 * <doc name="pdfxform">
 * <h3>The Primitive <tt>\pdfxform</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;pdfxform&rang;
 *       &rarr; <tt>\pdfxform</tt> ... </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \pdfxform 42  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class Pdfxform extends AbstractPdftexCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Pdfxform(final String name) {

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

        PdftexSupport writer = ensurePdftex(context, typesetter);

        String attr = null;
        String resources = null;

        for (;;) {
            if (source.getKeyword(context, "attr")) {
                attr = source.scanTokensAsString(context, getName());
            } else if (source.getKeyword(context, "resources")) {
                resources = source.scanTokensAsString(context, getName());
            } else {
                break;
            }
        }

        long b = Count.scanInteger(context, source, typesetter);
        Box box = context.getBox(Long.toString(b));
        PdfXForm form = writer.getXForm(attr, resources, box);

        try {
            typesetter.add(form);
        } catch (TypesetterException e) {
            throw new InterpreterException(e);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }

        prefix.clearImmediate();
    }

}
