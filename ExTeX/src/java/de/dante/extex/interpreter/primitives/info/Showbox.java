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

package de.dante.extex.interpreter.primitives.info;

import java.util.logging.Logger;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.primitives.register.box.AbstractBox;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive <code>\showbox</code>.
 *
 * <doc name="showbox">
 * <h3>The Primitive <tt>\showbox</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;showbox&rang;
 *       &rarr; <tt>\showbox</tt> {@linkplain
 *          de.dante.extex.interpreter.TokenSource#scanNumber()
 *          &lang;8-bit&nbsp;number&rang;} </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \showbox 1  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class Showbox extends AbstractBox implements LogEnabled {

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private Logger logger = null;

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
            throws GeneralException {

        String key = getKey(context, source);
        Box b = context.getBox(key);

        if (b == null) {
            logger.info(context.esc(key) + "=void");
        } else {
            logger.info(b.getNodes().toString());
            //TODO gene: correct??
        }
        logger.info(getLocalizer().format("TTP.Show.OK"));
    }

}
