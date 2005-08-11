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

package de.dante.extex.language.hyphenation.liang;

import junit.framework.TestCase;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.scanner.type.token.TokenFactoryImpl;

/**
 * This class contains soem test cases fro liang's hyphenation table.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class LiangsHyphenationTableTest extends TestCase {

    /**
     * The command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(LiangsHyphenationTableTest.class);
    }

    /**
     * Create new tokens from a string. Letters and the period are LETTERs,
     * spaces are SPACEs, and anything else is an OTHER token.
     *
     * @param s the string to translate
     *
     * @return the tokens crated from the string's characters
     *
     * @throws CatcodeException in case of an error
     */
    private static Tokens newTokens(final String s) throws CatcodeException {

        TokenFactory factory = new TokenFactoryImpl();
        Tokens tokens = new Tokens();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                tokens.add(factory.createToken(Catcode.SPACE, ' ',
                        Namespace.DEFAULT_NAMESPACE));
            } else if (Character.isLetter(c) || c == '.') {
                tokens.add(factory.createToken(Catcode.LETTER, c,
                        Namespace.DEFAULT_NAMESPACE));
            } else {
                tokens.add(factory.createToken(Catcode.OTHER, c,
                        Namespace.DEFAULT_NAMESPACE));
            }
        }
        return tokens;
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        LiangsHyphenationTable table = new LiangsHyphenationTable();
        table.addPattern(newTokens("0a3b0"));
        table.addPattern(newTokens("0a0b4c0"));
        table.addPattern(newTokens("0a2b0c0d0e0f4"));
        assertEquals("'a' nil\n" //
                + "  'b' (030)\n" //
                + "    'c' (0340)\n" //
                + "      'd' nil\n" //
                + "        'e' nil\n" //
                + "          'f' (0340004)\n", //
                table.getPatterns().toString());
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        LiangsHyphenationTable table = new LiangsHyphenationTable();
        table.addPattern(newTokens("0a2b0c0d0e0f4"));
        table.addPattern(newTokens("0a0b4c0x5y1"));
        table.addPattern(newTokens("0a0b4c0"));
        table.addPattern(newTokens("0a3b0"));
        assertEquals("'a' nil\n" //
                + "  'b' (030)\n" //
                + "    'c' (0340)\n" //
                + "      'd' nil\n" //
                + "        'e' nil\n" //
                + "          'f' (0340004)\n"//
                + "      'x' nil\n" //
                + "        'y' (034051)\n", //
                table.getPatterns().toString());
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        LiangsHyphenationTable table = new LiangsHyphenationTable();
        table.addPattern(newTokens("0a3b0"));
        table.addPattern(newTokens("0a0b4c0"));
        table.addPattern(newTokens("0a0b3.0"));
        table.addPattern(newTokens("0a2b0c0d0e0f4"));
        assertEquals("'a' nil\n" //
                + "  'b' (030)\n" //
                + "    '.' (0330)\n" //
                + "    'c' (0340)\n" //
                + "      'd' nil\n" //
                + "        'e' nil\n" //
                + "          'f' (0340004)\n", //
                table.getPatterns().toString());
    }
}