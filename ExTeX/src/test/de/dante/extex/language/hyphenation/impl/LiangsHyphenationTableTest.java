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

package de.dante.extex.language.hyphenation.impl;

import junit.framework.TestCase;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.MockContext;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.Language;
import de.dante.extex.language.hyphenation.exception.DuplicateHyphenationException;
import de.dante.extex.language.hyphenation.liang.LiangsHyphenationTable;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.TokenFactory;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class LiangsHyphenationTableTest extends TestCase {

    /**
     * This mock implementation is for test purposes only.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.2 $
     */
    private class MyMockContext extends MockContext {

    }

    /**
     * The command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(LiangsHyphenationTableTest.class);
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param s the string specifiction
     * @param context the context
     *
     * @return the tokens
     *
     * @throws CatcodeException in case of problems in token creation
     */
    private static Tokens makeTokens(final String s, final Context context)
            throws CatcodeException {

        TokenFactory factory = context.getTokenFactory();
        Tokens tokens = new Tokens();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            tokens.add(factory.createToken((Character.isLetter(c)
                    ? Catcode.LETTER
                    : Catcode.OTHER), c, Namespace.DEFAULT_NAMESPACE));
        }
        return tokens;
    }

    /**
     * This test case checks that the insertion of two different pattern does
     * not lead to an exception.
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        Context context = new MyMockContext();
        Language table = new LiangsHyphenationTable();

        table.addPattern(makeTokens("0a2b1c0", context));
        table.addPattern(makeTokens("0x2b1c0", context));
        assertTrue(true);
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        Context context = new MyMockContext();
        LiangsHyphenationTable table = new LiangsHyphenationTable();

        table.addPattern(makeTokens("0a1b0", context));
        table.addPattern(makeTokens("0a2b1c0", context));
        assertTrue(true);
    }

    /**
     * This test case tests that the addPattern() method with identical
     * arguments leads to an exception.
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        Context context = new MyMockContext();
        Language table = new LiangsHyphenationTable();

        table.addPattern(makeTokens("0a2b1c0", context));
        try {
            table.addPattern(makeTokens("0a2b1c0", context));
            assertFalse(true);
        } catch (DuplicateHyphenationException e) {
            assertTrue(true);
        }
    }

    /**
     * This test case tests that the addPattern() method with identical
     * arguments on the character positions leads to an exception.
     *
     * @throws Exception in case of an error
     */
    public void testErr2() throws Exception {

        Context context = new MyMockContext();
        Language table = new LiangsHyphenationTable();

        table.addPattern(makeTokens("0a2b1c0", context));
        try {
            table.addPattern(makeTokens("0a3b2c0", context));
            assertFalse(true);
        } catch (DuplicateHyphenationException e) {
            assertTrue(true);
        }
    }
}