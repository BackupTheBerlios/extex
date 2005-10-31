/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.hyphen;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.listMaker.ListManager;
import de.dante.extex.typesetter.listMaker.RestrictedHorizontalListMaker;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.util.Locator;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive
 * <code>\discretionary</code>.
 *
 * <doc name="discretionary">
 * <h3>The Primitive <tt>\discretionary</tt></h3>
 * <p>
 *  The primitive <tt>\discretionary</tt> can be used to insert an optional
 *  break point into the paragraph. The optional break point consists of three
 *  parts. The first part is inserted into the paragraph if no line breaking
 *  happens at this position. In case that the line breaking chooses this place
 *  for a line break then the second part of the discretionary is inserted at
 *  the end of the current line and the third part is inserted at the beginning
 *  of the next line.
 * </p>
 * <p>
 *  The three parts are given as three sequences of characters in braces. It
 *  may be composed of characters, ligatures, and rules only.
 * </p>
 * <p>
 *  In math mode the third part is forced to be empty.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;discretionary&rang;
 *      &rarr; <tt>\discretionary</tt>{...}{...}{...}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \discretionary{f-}{fi}{ffi}
 *    \discretionary{-}{}{}  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.26 $
 */
public class Discretionary extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Discretionary(final String name) {

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

        Tokens pre = source.getTokens(context, source, typesetter);
        Tokens post = source.getTokens(context, source, typesetter);
        Tokens nobreak = source.getTokens(context, source, typesetter);
        //CharNodeFactory cnf = new CharNodeFactory();
        TypesettingContext tc = context.getTypesettingContext();
        Locator locator = source.getLocator();

        try {
            typesetter.add(new DiscretionaryNode(fill(pre, tc, typesetter,
                    context, locator ), //
                    fill(post, tc, typesetter, context, locator), //
                    fill(nobreak, tc, typesetter, context, locator)));
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * This method creates a Node list for a list of tokens.
     *
     * @param tokens the tokens to put into a NodeList
     * @param tc the typesetting context
     * @param typesetter the typesetter
     * @param context the interpreter context
     * @param locator the locator pointing to the start
     *
     * @return the node list or <code>null</code> if there are no tokens to
     *  put into the list
     * @throws TypesetterException in case of an error
     * @throws ConfigurationException in case of a configuration error
     */
    private NodeList fill(final Tokens tokens, final TypesettingContext tc,
            final Typesetter typesetter, final Context context,
            final Locator locator)
            throws TypesetterException,
                ConfigurationException {

        if (tokens.length() == 0) {
            return null;
        }

        ListManager man = typesetter.getManager();
        ListMaker hlist = new RestrictedHorizontalListMaker(man, locator);
        //NodeList nodes = new HorizontalListNode();

        for (int i = 0; i < tokens.length(); i++) {
            hlist.letter(context, tc, tokens.get(i).getChar(), locator);
        }
        return hlist.complete((TypesetterOptions) context);
    }
}