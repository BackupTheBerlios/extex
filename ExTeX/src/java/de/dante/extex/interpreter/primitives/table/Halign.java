/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.table;

import java.util.List;

import de.dante.extex.i18n.EofHelpingException;
import de.dante.extex.i18n.MissingLeftBraceHelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.box.Boxable;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.impl.HAlignListMaker;
import de.dante.util.GeneralException;
import de.dante.util.configuration.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\halign</code>.
 *
 * <doc name="halign">
 * <h3>The Primitive <tt>\halign</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;halign&rang;
 *       &rarr; <tt>\halign</tt> &lang;box specification&rang; <tt>{</tt> &lang;preamble&rang; <tt>\cr</tt> &lang;rows&rang; <tt>}</tt>
 *
 *    &lang;box specification&rang;
 *        &rarr;
 *         | <tt>to</tt> &lang;rule dimension&rang;
 *         | <tt>spread</tt> &lang;rule dimension&rang;  </pre>
 *
 *    &lang;preamble&rang;
 *       &rarr; ...
 *
 *    &lang;rows&rang;
 *       &rarr;
 *       | &lang;row&rang; &lang;rows&rang;
 *
 *    &lang;preamble&rang;
 *       &rarr; ...   </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \halign  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class Halign extends AbstractAlign implements Boxable {

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Halign(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        typesetter.add(getNodes(context, source, typesetter));

        return true;
    }

    /**
     * Getter for the box representation.
     *
     * @param context the interpreter context
     * @param source the token source
     * @param typesetter the typesetter
     *
     * @return a box containing the nodes generated
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.interpreter.type.box.Boxable#getBox(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Box getBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        return new Box(getNodes(context, source, typesetter));
    }

    /**
     * Getter for the nodes.
     *
     * @param context the interpreter context
     * @param source the token source
     * @param typesetter the typesetter
     *
     * @return the list of nodes gathered
     *
     * @throws GeneralException in case of an error
     * @throws EofHelpingException in case that an enof of file has occurred
     * @throws MissingLeftBraceHelpingException in case that the mandatory left
     *  brace was missing
     */
    private NodeList getNodes(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        FixedDimen width = null;
        boolean spread = false;

        if (source.getKeyword("to")) {
            width = new Dimen(context, source);
        } else if (source.getKeyword("spread")) {
            width = new Dimen(context, source);
            spread = true;
        }
        Token t = source.getToken();
        if (t == null) {
            throw new EofHelpingException(printableControlSequence(context));
        } else if (t.isa(Catcode.LEFTBRACE)) {
            List preamble = getPreamble(context, source);
            typesetter.push(new HAlignListMaker(typesetter.getManager(),
                    context, source, preamble, width, spread));
        } else {
            throw new MissingLeftBraceHelpingException(
                    printableControlSequence(context));
        }

        try {
            context.openGroup();
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        }

        source.executeGroup();
        return typesetter.close();
    }

}