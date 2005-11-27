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

package de.dante.extex.interpreter.unit.tex;

import java.util.logging.Logger;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.ContextInternals;
import de.dante.extex.interpreter.context.observer.count.CountObservable;
import de.dante.extex.interpreter.context.observer.count.CountObserver;
import de.dante.extex.interpreter.context.observer.load.LoadedObserver;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.observer.command.CommandObservable;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.unit.Loader;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides the setup for the unit <b>tex</b>.
 *
 * <h3>Tracing</h3>
 * <p>
 *  Tracing is <logo>TeX</logo> is controlled by some count registers.
 *  The implementation in <logo>ExTeX</logo> is based on observers. In
 *  the first stage a {@link
 *  de.dante.extex.interpreter.context.observer.count.CountObserver
 *  CountObserver} for the controlling count is registered. In this
 *  observer the observer for the real event is registered if this as
 *  not been done before and the value of the controlling count is
 *  greater than 0.
 * </p>
 * <p>
 *  This strategies tries to achieve that the overhead for the normal
 *  mode of operation is minimized. Here only the controlling cont has
 *  to be watched. The observer list for the event to be traced is
 *  empty and does not impose any performance overhead.
 * </p>
 *
 * <doc name="tracingonline" type="register">
 * <h3>The Parameter <tt>\tracingonline</tt></h3>
 * <p>
 *  This count register determines whether the tracing should go into
 *  the log file only or put on the standard output stream as well. If
 *  the value is less than 1 then the tracing goes to the log file
 *  only. Otherwise logging is duplicated to the console as well.
 * </p>
 * </doc>
 *
 * <doc name="tracingcommands" type="register">
 * <h3>The Parameter <tt>\tracingcommands</tt></h3>
 * <p>
 *  This count register determines whether the execution of commands
 *  should be traced. If the value is less or equal than 0 then no
 *  tracing is performed. If the value is greater than 0 then the
 *  tokens are logged before they are executed.
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class Setup implements Loader, LoadedObserver, LogEnabled {

    /**
     * The field <tt>TRACING_COMMANDS</tt> contains the name of the count
     * register controlling the activation of command tracing.
     */
    private static final String TRACING_COMMANDS = "tracingcommands";

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The field <tt>logger</tt> contains the local reference to the logger.
     */
    private Logger logger = null;

    /**
     * The field <tt>notRegistered</tt> contains the indicator that the observer
     * for command events as not been registered yet.
     */
    private transient boolean notRegistered = true;

    /**
     * Creates a new object.
     */
    public Setup() {

        super();
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger logger) {

        this.logger = logger;
    }

    /**
     * @see de.dante.extex.interpreter.primitives.dynamic.Loader#load(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void load(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        receiveLoaded(context, source);
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.load.LoadedObserver#receiveLoaded(
     *      de.dante.extex.interpreter.context.Context)
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void receiveLoaded(final Context context, final TokenSource source)
            throws InterpreterException {

        if (context.getCount(TRACING_COMMANDS).gt(Count.ZERO)) {
            if (notRegistered && source instanceof CommandObservable) {
                ((CommandObservable) source)
                        .registerObserver(new TraceCommandObserver(logger,
                                context));
                notRegistered = false;
            }
        } else if (context instanceof CountObservable) {
            ((CountObservable) context).registerCountObserver(
                    TRACING_COMMANDS, new CountObserver() {

                        /**
                         * @see de.dante.extex.interpreter.context.observer.count.CountObserver#receiveCountChange(
                         *      de.dante.extex.interpreter.context.ContextInternals,
                         *      java.lang.String,
                         *      de.dante.extex.interpreter.type.count.Count)
                         */
                        public void receiveCountChange(
                                final ContextInternals context,
                                final String name, final Count value)
                                throws Exception {

                            if (notRegistered && value != null
                                    && value.getValue() > 0
                                    && source instanceof CommandObservable) {
                                ((CommandObservable) source)
                                        .registerObserver(new TraceCommandObserver(
                                                logger, context));
                                notRegistered = false;
                            }
                        }
                    });
        }
    }

}
