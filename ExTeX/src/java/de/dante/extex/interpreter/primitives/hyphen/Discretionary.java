/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.hyphen;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.ligature.LigatureBuilder;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.listMaker.HorizontalListMaker;
import de.dante.extex.typesetter.listMaker.ListManager;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.CharNodeFactory;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\discretionary</code>.
 *
 * <doc name="discretionary">
 * <h3>The Primitive <tt>\discretionary</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;discretionary&rang;
 *      &rarr; <tt>\discretionary ...</tt>  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \discretionary{f-}{fi}{ffi}
 *    \discretionary{-}{}{}  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.15 $
 */
public class Discretionary extends AbstractCode {

    /**
     * TODO gene: missing JavaDoc.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.15 $
     */
    private class Manager implements ListManager {

        /**
         * The field <tt>charNodeFactory</tt> contains the ...
         */
        private CharNodeFactory charNodeFactory;

        /**
         * Creates a new object.
         */
        public Manager(final CharNodeFactory cnf) {

            super();
            charNodeFactory = cnf;
        }

        /**
         * @see de.dante.extex.typesetter.listMaker.ListManager#endParagraph()
         */
        public void endParagraph() throws TypesetterException {

            // TODO gene: endParagraph unimplemented

        }

        /**
         * @see de.dante.extex.typesetter.listMaker.ListManager#getCharNodeFactory()
         */
        public CharNodeFactory getCharNodeFactory() {

            return charNodeFactory;
        }

        /**
         * @see de.dante.extex.typesetter.listMaker.ListManager#getDocumentWriter()
         */
        public DocumentWriter getDocumentWriter() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.listMaker.ListManager#getLigatureBuilder()
         */
        public LigatureBuilder getLigatureBuilder() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.listMaker.ListManager#getOptions()
         */
        public TypesetterOptions getOptions() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.listMaker.ListManager#getParagraphBuilder()
         */
        public ParagraphBuilder getParagraphBuilder() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.listMaker.ListManager#pop()
         */
        public ListMaker pop() throws TypesetterException {

            // TODO gene: pop unimplemented
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.listMaker.ListManager#push(de.dante.extex.typesetter.ListMaker)
         */
        public void push(final ListMaker listMaker) throws TypesetterException {

            // TODO gene: push unimplemented

        }
    }

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

        Tokens pre = source.getTokens(context);
        Tokens post = source.getTokens(context);
        Tokens nobreak = source.getTokens(context);
        CharNodeFactory cnf = new CharNodeFactory();
        TypesettingContext tc = context.getTypesettingContext();

        try {
            typesetter.add(new DiscretionaryNode(fill(pre, tc, typesetter,
                    context), //
                    fill(post, tc, typesetter, context), //
                    fill(nobreak, tc, typesetter, context)));
        } catch (GeneralException e) {
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
     *
     * @return the node list or <code>null</code> if there are no tokens to
     *  put into the list
     *
     * @throws TypesetterException in case of an error
     */
    private NodeList fill(final Tokens tokens, final TypesettingContext tc,
            final Typesetter typesetter, final Context context)
            throws TypesetterException {

        if (tokens.length() == 0) {
            return null;
        }
        ListManager man = new Manager(typesetter.getCharNodeFactory());
        ListMaker hlist = new HorizontalListMaker(man);
        NodeList nodes = new HorizontalListNode();

        for (int i = 0; i < tokens.length(); i++) {
            hlist.letter(context, tc, tokens.get(i).getChar());
        }
        return hlist.complete((TypesetterOptions) context);
    }
}