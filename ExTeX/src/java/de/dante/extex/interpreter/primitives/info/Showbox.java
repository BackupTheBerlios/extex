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

package de.dante.extex.interpreter.primitives.info;

import java.util.logging.Logger;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.register.box.AbstractBox;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive <code>\showbox</code>.
 *
 * <doc name="showbox">
 * <h3>The Primitive <tt>\showbox</tt></h3>
 * <p>
 *  The primitive <tt>\showbox</tt> produces a listing of the box register
 *  given as parameter. The listing is restricted in breadth and depth by the
 *  count registers <tt>\showboxbreadth</tt> and <tt>\showboxdepth</tt>
 *  respectively.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;showbox&rang;
 *      &rarr; <tt>\showbox</tt> {@linkplain
 *        de.dante.extex.interpreter.primitives.register.box.AbstractBox#getKey(Context,Source,String)
 *        &lang;box&nbsp;register&nbsp;name&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \showbox 1  </pre>
 *
 * </doc>
 *
 *
 * <doc name="showboxbreadth" type="register">
 * <h3>The Count Parameter <tt>\showboxbreadth</tt></h3>
 * <p>
 *  The count register <tt>\showboxbreadth</tt> contains the breadth to which
 *  the box produced by <tt>\showbox</tt> should be presented.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;showboxbreadth&rang;
 *      &rarr; <tt>\showboxbreadth</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *        &lang;number&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \showboxbreadth=16  </pre>
 *
 * </doc>
 *
 *
 * <doc name="showboxdepth" type="register">
 * <h3>The Count Parameter <tt>\showboxdepth</tt></h3>
 * <p>
 *  The count register <tt>\showboxdepth</tt> contains the depth to which
 *  the box produced by <tt>\showbox</tt> should be presented.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;showboxdepth&rang;
 *      &rarr; <tt>\showboxdepth</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *        &lang;number&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \showboxdepth=16  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.20 $
 */
public class Showbox extends AbstractBox implements LogEnabled {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private transient Logger logger = null;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Showbox(final String name) {

        super(name);
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
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

        String key = getKey(context, source, typesetter, getName());
        Box b = context.getBox(key);

        if (b == null) {
            logger.info(getLocalizer().format("TTP.Show.void", //
                    context.esc(key)));
        } else {
            long depth = context.getCount("showboxdepth").getValue();
            long width = context.getCount("showboxbreadth").getValue();
            StringBuffer sb = new StringBuffer();
            b.getNodes().toString(sb, "", (int) depth, (int) width);
            logger.info(sb.toString());
        }
        logger.info(getLocalizer().format("TTP.Show.OK"));
    }

}
