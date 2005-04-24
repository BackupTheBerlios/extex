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

package de.dante.extex.interpreter.type.glue;

import junit.framework.TestCase;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.MockContext;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.MissingNumberException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.observer.NotObservableException;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class GlueTest extends TestCase {

    /**
     * TODO gene: missing JavaDoc.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private class MContext extends MockContext {

        /**
         * @see de.dante.extex.interpreter.context.ContextCode#getCode(de.dante.extex.scanner.type.CodeToken)
         */
        public Code getCode(final CodeToken t) throws InterpreterException {

            // TODO gene: getCode unimplemented
            return super.getCode(t);
        }
    }

    /**
     * TODO gene: missing JavaDoc.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private class MockTokenSource implements TokenSource {

        /**
         * @see de.dante.extex.interpreter.TokenSource#addStream(de.dante.extex.scanner.stream.TokenStream)
         */
        public void addStream(final TokenStream stream) {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#closeAllStreams(de.dante.extex.interpreter.context.Context)
         */
        public void closeAllStreams(final Context context)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#closeNextFileStream(de.dante.extex.interpreter.context.Context)
         */
        public void closeNextFileStream(final Context context)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#execute(de.dante.extex.scanner.type.Token, de.dante.extex.interpreter.context.Context, de.dante.extex.typesetter.Typesetter)
         */
        public void execute(final Token token, final Context context,
                final Typesetter typesetter) throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#executeGroup()
         */
        public void executeGroup() throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#getBox(de.dante.extex.interpreter.context.Context, de.dante.extex.typesetter.Typesetter)
         */
        public Box getBox(final Context context, final Typesetter typesetter)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#getControlSequence(de.dante.extex.interpreter.context.Context)
         */
        public CodeToken getControlSequence(final Context context)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#getFont(de.dante.extex.interpreter.context.Context)
         */
        public Font getFont(final Context context) throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#getKeyword(de.dante.extex.interpreter.context.Context, java.lang.String)
         */
        public boolean getKeyword(final Context context, final String keyword)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#getLocator()
         */
        public Locator getLocator() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#getNonSpace(de.dante.extex.interpreter.context.Context)
         */
        public Token getNonSpace(final Context context)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#getOptionalEquals(de.dante.extex.interpreter.context.Context)
         */
        public void getOptionalEquals(final Context context)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#getToken(de.dante.extex.interpreter.context.Context)
         */
        public Token getToken(final Context context)
                throws InterpreterException {

            try {
                return context.getTokenFactory().createToken(Catcode.ESCAPE,
                        new UnicodeChar('\\'), "testskip", "");
            } catch (CatcodeException e) {
                throw new InterpreterException(e);
            }
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#getTokens(de.dante.extex.interpreter.context.Context)
         */
        public Tokens getTokens(final Context context)
                throws InterpreterException {

            // TODO gene: getTokens unimplemented
            return null;
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#getTokenStreamFactory()
         */
        public TokenStreamFactory getTokenStreamFactory() {

            // TODO gene: getTokenStreamFactory unimplemented
            return null;
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#push(de.dante.extex.scanner.type.Token)
         */
        public void push(final Token token) throws InterpreterException {

            // TODO gene: push unimplemented

        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#push(de.dante.extex.scanner.type.Token[])
         */
        public void push(final Token[] tokens) throws InterpreterException {

            // TODO gene: push unimplemented

        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#push(de.dante.extex.interpreter.type.tokens.Tokens)
         */
        public void push(final Tokens tokens) throws InterpreterException {

            // TODO gene: push unimplemented

        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#scanCharacterCode(de.dante.extex.interpreter.context.Context)
         */
        public UnicodeChar scanCharacterCode(final Context context)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#scanInteger(de.dante.extex.interpreter.context.Context)
         */
        public long scanInteger(final Context context)
                throws InterpreterException,
                    MissingNumberException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#scanNonSpace(de.dante.extex.interpreter.context.Context)
         */
        public Token scanNonSpace(final Context context)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#scanNumber()
         */
        public long scanNumber() throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#scanNumber(de.dante.extex.interpreter.context.Context)
         */
        public long scanNumber(Context context) throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#scanNumber(de.dante.extex.interpreter.context.Context, de.dante.extex.scanner.type.Token)
         */
        public long scanNumber(final Context context, final Token token)
                throws InterpreterException,
                    MissingNumberException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#scanRegisterName(de.dante.extex.interpreter.context.Context)
         */
        public String scanRegisterName(final Context context)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#scanToken(de.dante.extex.interpreter.context.Context)
         */
        public Token scanToken(final Context context)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#scanTokens(de.dante.extex.interpreter.context.Context)
         */
        public Tokens scanTokens(final Context context)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#scanTokensAsString(de.dante.extex.interpreter.context.Context)
         */
        public String scanTokensAsString(final Context context)
                throws InterpreterException {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#skipSpace()
         */
        public void skipSpace() {

        }

        /**
         * @see de.dante.extex.interpreter.TokenSource#update(java.lang.String, java.lang.String)
         */
        public void update(final String name, final String text)
                throws InterpreterException,
                    NotObservableException {

        }
    }

    /**
     * Command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(GlueTest.class);
    }

    /*
     * Class under test for void Glue(TokenSource, Context)
     */
    public final void testGlueTokenSourceContext() throws Exception {

        TokenSource source = new MockTokenSource();
        Context context = new MContext();
        new Glue(source, context);
    }
}
