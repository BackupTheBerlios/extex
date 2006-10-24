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

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.pdftex.util.id.IdSpec;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.node.RuleNode;
import de.dante.extex.typesetter.type.node.pdftex.PdfThread;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\pdfthread</code>.
 *
 * <doc name="pdfthread">
 * <h3>The Primitive <tt>\pdfthread</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;pdfthread&rang;
 *       &rarr; <tt>\pdfthread</tt> ... </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \pdfthread {abc.png}  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public class Pdfthread extends AbstractPdftexCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Pdfthread(final String name) {

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

        ensurePdftex(context, typesetter);
        String attr = null;
        Dimen width = null;
        Dimen height = null;
        Dimen depth = null;

        for (;;) {
            if (source.getKeyword(context, "width")) {
                width = Dimen.parse(context, source, typesetter);
            } else if (source.getKeyword(context, "height")) {
                height = Dimen.parse(context, source, typesetter);
            } else if (source.getKeyword(context, "depth")) {
                depth = Dimen.parse(context, source, typesetter);
            } else if (source.getKeyword(context, "attr")) {
                attr = source.scanTokensAsString(context, getName());
            } else {
                break;
            }
        }

        if (width == null) {
            width = Dimen.ONE_PT; //TODO gene:provide correct default
        }
        if (height == null) {
            height = Dimen.ONE_PT; //TODO gene:provide correct default
        }
        if (depth == null) {
            depth = Dimen.ONE_PT; //TODO gene:provide correct default
        }

        IdSpec id = IdSpec.parseIdSpec(context, source, typesetter, getName());

        PdfThread thread = new PdfThread(new RuleNode(width, height, depth,
                context.getTypesettingContext(), true), attr, id);

        try {
            typesetter.add(thread);
        } catch (TypesetterException e) {
            throw new InterpreterException(e);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }

        prefix.clearImmediate();
    }

}
