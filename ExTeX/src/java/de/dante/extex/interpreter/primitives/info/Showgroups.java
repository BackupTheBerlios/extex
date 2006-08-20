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
import de.dante.extex.interpreter.context.group.GroupInfo;
import de.dante.extex.interpreter.context.group.GroupTypeVisitor;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.register.box.AbstractBox;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.Locator;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive <code>\showgroups</code>.
 *
 * <doc name="showgroups">
 * <h3>The Primitive <tt>\showgroups</tt></h3>
 * <p>
 *  The primitive <tt>\showgroups</tt> prints the list of group descriptions
 *  to the log file. The list consists of descriptions of the currently open
 *  groups in descending order.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;showgroups&rang;
 *       &rarr; <tt>\showgroups</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \showgroups  </pre>
 *
 * </doc>
 *
 * <h4>Configuration</h4>
 *
 * <p>
 *  The primitive takes an optional attribute named <tt>format</tt>. If this
 *  attribute is present and has the value <tt>short</tt> then only the line
 *  number is logged instead of the complete locator.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Showgroups extends AbstractBox implements LogEnabled, Configurable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060616L;

    /**
     * The field <tt>gtv</tt> contains the group tpe visitor to map the group
     * types to keys for the localizer.
     */
    private final transient GroupTypeVisitor gtv = new GroupTypeVisitor() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitAdjustedHboxGroup(java.lang.Object)
         */
        public Object visitAdjustedHboxGroup(final Object arg) {

            return "adjusted.hbox.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitAlignGroup(java.lang.Object)
         */
        public Object visitAlignGroup(final Object arg) {

            return "align.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitBottomLevelGroup(java.lang.Object)
         */
        public Object visitBottomLevelGroup(final Object arg) {

            return "bottom.level.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitDiscGroup(java.lang.Object)
         */
        public Object visitDiscGroup(final Object arg) {

            return "disc.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitHboxGroup(java.lang.Object)
         */
        public Object visitHboxGroup(final Object arg) {

            return "hbox.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitInsertGroup(java.lang.Object)
         */
        public Object visitInsertGroup(final Object arg) {

            return "insert.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitMathChoiceGroup(java.lang.Object)
         */
        public Object visitMathChoiceGroup(final Object arg) {

            return "math.choice.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitMathGroup(java.lang.Object)
         */
        public Object visitMathGroup(final Object arg) {

            return "math.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitMathLeftGroup(java.lang.Object)
         */
        public Object visitMathLeftGroup(final Object arg) {

            return "math.left.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitMathShiftGroup(java.lang.Object)
         */
        public Object visitMathShiftGroup(final Object arg) {

            return "math.shift.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitNoAlignGroup(java.lang.Object)
         */
        public Object visitNoAlignGroup(final Object arg) {

            return "no.align.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitOutputGroup(java.lang.Object)
         */
        public Object visitOutputGroup(final Object arg) {

            return "output.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitSemiSimpleGroup(java.lang.Object)
         */
        public Object visitSemiSimpleGroup(final Object arg) {

            return "semi.simple.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitSimpleGroup(java.lang.Object)
         */
        public Object visitSimpleGroup(final Object arg) {

            return "simple.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitVboxGroup(java.lang.Object)
         */
        public Object visitVboxGroup(final Object arg) {

            return "vbox.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitVcenterGroup(java.lang.Object)
         */
        public Object visitVcenterGroup(final Object arg) {

            return "vcenter.group";
        }

        /**
         * @see de.dante.extex.interpreter.context.group.GroupTypeVisitor#visitVtopGroup(java.lang.Object)
         */
        public Object visitVtopGroup(final Object arg) {

            return "vtop.group";
        }

    };

    /**
     * The field <tt>key</tt> contains the key to be appended to the format
     * name.
     */
    private String key = "";

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private transient Logger logger = null;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Showgroups(final String name) {

        super(name);
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.framework.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        String format = config.getAttribute("format");
        if ("short".equals(format)) {
            key = ".short";
        }
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

        Localizer loc = getLocalizer();
        GroupInfo[] gi = context.getGroupInfos();
        GroupInfo g;
        Token start;
        Locator locator;

        for (int i = gi.length - 1; i >= 0; i--) {
            g = gi[i];
            String level = (i == 0 ? "" //
                    : loc.format("log.level" + key, Integer.toString(i)));
            start = g.getGroupStart();
            String token = (start == null ? "" //
                    : loc.format("log.token" + key, start.toText()));
            locator = g.getLocator();
            String line = (locator == null ? "" //
                    : loc.format("log.resource" + key, locator
                            .getResourceName(), Integer.toString(locator
                            .getLineNumber())));

            logger.info(loc.format("log.pattern", //
                    loc.format((String) g.getGroupType().visit(gtv, null)), //
                    level, //
                    line, //
                    token));
        }
    }
}
