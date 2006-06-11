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

package de.dante.extex.interpreter.primitives.typesetter;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.register.box.AbstractBox;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.LeftBraceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.ParagraphObserver;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.NodeList;

/**
 * This class provides an implementation for the primitive
 * <code>\setlanguage</code>.
 *
 * <doc name="setlanguage">
 * <h3>The Primitive <tt>\\</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;setlanguage&rang;
 *       &rarr; <tt>\setlanguage</tt> &lang;number&rang; </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \setlanguage2  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.12 $
 */
public class Setlanguage extends AbstractBox {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * This observer can be used to restore the value of the registers
     * <tt>language</tt> and <tt>lang</tt> t the end of a paragraph.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.12 $
     */
    private class ParObserver implements ParagraphObserver {

        /**
         * The field <tt>context</tt> contains the interpreter context.
         */
        private Context context;

        /**
         * The field <tt>language</tt> contains the value of the language
         * register to restore.
         */
        private long language;

        /**
         * The field <tt>toks</tt> contains the value of the register lang to
         * restore.
         */
        private Tokens lang;

        /**
         * Creates a new object.
         *
         * @param context the context
         */
        public ParObserver(final Context context) {

            super();
            this.context = context;
            language = context.getCount("language").getValue();
            lang = context.getToks("lang");
        }

        /**
         * @see de.dante.extex.typesetter.ParagraphObserver#atParagraph(
         *      de.dante.extex.typesetter.type.NodeList)
         */
        public void atParagraph(final NodeList nodes)
                throws InterpreterException {

            context.setCount("language", language, false);
            context.setToks("lang", lang, false);
        }
    }

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Setlanguage(final String name) {

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

        typesetter.afterParagraph(new ParObserver(context));

        Token token = source.getToken(context);
        source.push(token);

        if (token instanceof LeftBraceToken) {
            Tokens tokens = source.getTokens(context, source, typesetter);
            context.setToks("lang", tokens, false);
        } else {
            long no = Count.scanInteger(context, source, typesetter);
            context.setCount("language", no, false);
            context.setToks("lang", Tokens.EMPTY, false);
        }
    }

}
