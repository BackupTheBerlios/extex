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
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\pdfincludechars</code>.
 *
 * <doc name="pdfincludechars">
 * <h3>The PDF Primitive <tt>\pdfincludechars</tt></h3>
 * <p>
 *  This primitive tells the PDF back-end to include certain characters from a
 *  font into the generated output.
 *  This should overwrite any partial font downloading in effect.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;pdfincludechars&rang;
 *       &rarr; <tt>\pdfincludechars</tt> {@linkplain
 *          de.dante.extex.interpreter.TokenSource#getFont(Context, String)
 *          &lang;font&rang;} {@linkplain
 *          de.dante.extex.interpreter.TokenSource#scanTokens(Context, String)
 *          &lang;general text&rang;} </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \font\f cmr12
 *    \pdfincludechars \f {abc} </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class Pdfincludechars extends AbstractPdftexCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Pdfincludechars(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractCode#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        PdftexSupport writer = ensurePdftex(context, typesetter);

        Font font = source.getFont(context, getName());
        String text = source.scanTokens(context, false, false, getName()).toText();

        writer.pdfincludechars(font, text);
    }

}
