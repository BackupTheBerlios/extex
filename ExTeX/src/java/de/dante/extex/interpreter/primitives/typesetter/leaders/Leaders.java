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

package de.dante.extex.interpreter.primitives.typesetter.leaders;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.UndefinedControlSequenceException;
import de.dante.extex.interpreter.primitives.typesetter.spacing.VerticalSkip;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.box.Boxable;
import de.dante.extex.interpreter.type.box.RuleConvertible;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.node.AlignedLeadersNode;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\leaders</code>.
 *
 * <doc name="leaders">
 * <h3>The Primitive <tt>\leaders</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;leaders&rang;
 *      &rarr; <tt>\leaders</tt> ...  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \leaders\hrul\hfill  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class Leaders extends AbstractCode  {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Leaders(final String name) {

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
            throws GeneralException {

        CodeToken cs = source.getControlSequence(context);
        Code code = context.getCode(cs);

        if (code == null) {
            throw new UndefinedControlSequenceException(//
                    context.esc(cs.getName()));
        }

        Node node = null;
        if (code instanceof Boxable) {
            Box b = ((Boxable) code).getBox(context, source, typesetter);
            node = b.getNodes();
        } else if (code instanceof RuleConvertible) {
            node = ((RuleConvertible) code)
                    .getRule(context, source, typesetter);
        } else {
            throw new HelpingException(getLocalizer(), "TTP.BoxExpected");
        }

        CodeToken vskip = source.getControlSequence(context);
        code = context.getCode(cs);

        if (code == null) {
            throw new UndefinedControlSequenceException(//
                    context.esc(vskip.getName()));
        } else if (!(code instanceof VerticalSkip)) {
            throw new HelpingException(getLocalizer(),
                    "TTP.BadGlueAfterLeaders");
        }
        Glue skip = ((VerticalSkip) code).verticalSkip(context, source);

        typesetter.add(new AlignedLeadersNode(node, skip));
    }

}
