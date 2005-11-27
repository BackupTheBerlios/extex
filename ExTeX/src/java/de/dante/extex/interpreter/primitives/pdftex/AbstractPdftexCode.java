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

package de.dante.extex.interpreter.primitives.pdftex;

import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.backend.documentWriter.PdftexSupport;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterPdftexException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractPdftexCode extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public AbstractPdftexCode(final String name) {

        super(name);
    }

    /**
     * Check that pdfTeX is active.
     *
     * @param context the interpreter context
     * @param typesetter the typesetter
     *
     * @throws InterpreterPdftexException in case that pdfTeX is not active
     */
    protected PdftexSupport ensurePdftex(final Context context,
            final Typesetter typesetter) throws InterpreterPdftexException {

        DocumentWriter documentWriter = typesetter.getDocumentWriter();

        if ( !(documentWriter instanceof PdftexSupport)) {
            
        }
        if (context.getCount("pdfoutput").gt(Count.ZERO)) {
            throw new InterpreterPdftexException(
                    printableControlSequence(context));
        }
        return (PdftexSupport) documentWriter;
    }

}
