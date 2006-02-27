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

package de.dante.extex.interpreter.primitives.file;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.interaction.Interaction;
import de.dante.extex.interpreter.primitives.macro.MacroCode;
import de.dante.extex.interpreter.primitives.macro.MacroPattern;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.file.InFile;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive <code>\readline</code>.
 *
 * <doc name="readline">
 * <h3>The Primitive <tt>\readline</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;readline&rang;
 *       &rarr; <tt>\readline</tt> &lang;read&rang; <tt>to</tt> &lang;control sequence&rang;</pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 * \openin3= abc.def
 * \readline3 to \line
 * \closein3 </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class Readline extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>TOKENIZER</tt> contains the tokenizer to use for this
     * primitive.
     */
    private static final Tokenizer TOKENIZER = new Tokenizer() {

        /**
         * @see de.dante.extex.interpreter.Tokenizer#getCatcode(
         *      de.dante.util.UnicodeChar)
         */
        public Catcode getCatcode(final UnicodeChar c) {

            return (c.getCodePoint() == ' ' ? Catcode.SPACE : Catcode.OTHER);
        }

        /**
         * @see de.dante.extex.interpreter.Tokenizer#getNamespace()
         */
        public String getNamespace() {

            return Namespace.DEFAULT_NAMESPACE;
        }

    };

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Readline(final String name) {

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

        String key = AbstractFileCode
                .scanInFileKey(context, source, typesetter);

        if (!source.getKeyword(context, "to")) {
            throw new HelpingException(getLocalizer(), "TTP.MissingToForRead");
        }
        CodeToken cs = source.getControlSequence(context);

        Interaction interaction = context.getInteraction();
        if (interaction != Interaction.ERRORSTOPMODE) {
            throw new HelpingException(getLocalizer(), "TTP.NoTermRead");
        }
        InFile file = context.getInFile(key);

        Tokens toks = file.read(context.getTokenFactory(), TOKENIZER);
        if (toks == null) {
            throw new HelpingException(getLocalizer(), "TTP.EOFinRead");
        }
        context.setCode(cs, new MacroCode(cs.getName(), prefix,
                MacroPattern.EMPTY, toks), prefix.isGlobal());
        prefix.clearGlobal();
    }

}
