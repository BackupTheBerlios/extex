/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */
package de.dante.extex.interpreter.primitives.register.font;

import de.dante.extex.font.type.other.NullFont;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.font.FontConvertible;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.configuration.exception.ConfigurationException;


/**
 * This class provides an implementation for the primitive
 * <code>\nullfont</code>.
 *
 * <doc name="nullfont">
 * <h3>The Primitive <tt>\nullfont</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\nullfont</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \font123=\nullfont  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class NullfontPrimitive extends AbstractCode
        implements FontConvertible {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The field <tt>nullFont</tt> contains the font encapsulated in this
     * primitive.
     */
    private Font nullFont = new NullFont();

    /**
     * Creates a new object.
     *
     * @param name the name of the primitive
     */
    public NullfontPrimitive(final String name) {

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

        try {
            context.set(nullFont, prefix.isGlobal());
            prefix.clearGlobal();
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }


    /**
     * @see de.dante.extex.interpreter.type.font.FontConvertible#convertFont(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Font convertFont(final Context context, final TokenSource source)
            throws InterpreterException {

        return nullFont;
    }

}
