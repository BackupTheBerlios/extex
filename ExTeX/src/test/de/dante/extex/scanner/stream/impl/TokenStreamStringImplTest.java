/*
 * Copyright (C) 2003  Gerd Neugebauer
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

package de.dante.extex.scanner.stream.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import junit.framework.TestCase;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.impl.ContextImpl;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.scanner.type.TokenFactoryImpl;
import de.dante.util.StringList;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;

/**
 * Test cases for the string implementation of atoken stream.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.14 $
 */
public class TokenStreamStringImplTest extends TestCase {

    /**
     * Mock configuration class.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.14 $
     */
    private static class MockConfiguration implements Configuration {

        /**
         * @see de.dante.util.configuration.Configuration#getValues(
         *      de.dante.util.StringList, java.lang.String)
         */
        public void getValues(final StringList list, final String key) {

            // TODO unimplemented

        }
        /**
         * The field <tt>classname</tt> contains the name of the class to use.
         */
        private String classname = "de.dante.extex.interpreter.context.impl.ContextImpl";

        /**
         * Creates a new object.
         */
        public MockConfiguration() {

            super();
        }

        /**
         * Creates a new object.
         * @param cn the class name
         */
        public MockConfiguration(final String cn) {

            super();
            classname = cn;
        }

        /**
         * @see de.dante.util.configuration.Configuration#findConfiguration(
         *      java.lang.String)
         */
        public Configuration findConfiguration(final String key) {

            return null;
        }

        /**
         * @see de.dante.util.configuration.Configuration#findConfiguration(
         *      java.lang.String,
         *      java.lang.String)
         */
        public Configuration findConfiguration(final String key,
                final String attribute) throws ConfigurationException {

            return null;
        }

        /**
         * @see de.dante.util.configuration.Configuration#getAttribute(
         *      java.lang.String)
         */
        public String getAttribute(final String name) {

            return classname;
        }

        /**
         * @see de.dante.util.configuration.Configuration#getConfiguration(
         *      java.lang.String)
         */
        public Configuration getConfiguration(final String key) {

            return new MockConfiguration(
                    "de.dante.extex.interpreter.context.impl.GroupImpl");
        }

        /**
         * @see de.dante.util.configuration.Configuration#getConfiguration(
         *      java.lang.String,
         *      java.lang.String)
         */
        public Configuration getConfiguration(final String key,
                final String attribute) {

            return null;
        }

        /**
         * @see de.dante.util.configuration.Configuration#getValue()
         */
        public String getValue() {

            return null;
        }

        /**
         * @see de.dante.util.configuration.Configuration#getValue(
         *      java.lang.String)
         */
        public String getValue(final String key) {

            return null;
        }

        /**
         * @see de.dante.util.configuration.Configuration#getValueAsInteger(
         *      java.lang.String,
         *      int)
         */
        public int getValueAsInteger(final String key, final int defaultValue) {

            return 0;
        }

        /**
         * @see de.dante.util.configuration.Configuration#getValues(
         *      java.lang.String)
         */
        public StringList getValues(final String key) {

            return null;
        }

        /**
         * @see de.dante.util.configuration.Configuration#iterator(
         *      java.lang.String)
         */
        public Iterator iterator(final String key) {

            return null;
        }
    }

    /**
     * The field <tt>context</tt> contains the context to use.
     */
    private static Context context;

    /**
     * The field <tt>fac</tt> contains the token factory to use.
     */
    private static TokenFactory fac;

    /**
     * The field <tt>tokenizer</tt> contains the tokenizer to use for
     * categorizing characters.
     */
    private static Tokenizer tokenizer;

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(TokenStreamStringImplTest.class);
    }

    /**
     * Creates a new object.
     * @param name the name
     */
    public TokenStreamStringImplTest(final String name) {

        super(name);
    }

    /**
     * Create a stream of tokens fed from a string.
     *
     * @param line the input string
     * @return the new token stream
     * @throws IOException in case of an error
     */
    private TokenStream makeStream(final String line) throws IOException {

        return new TokenStreamImpl(null, null, new StringReader(line),
                Boolean.FALSE, "#");
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        super.setUp();
        fac = new TokenFactoryImpl();
        context = new ContextImpl(new MockConfiguration());
        tokenizer = context.getTokenizer();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    /**
     * The digit 1 is parsed as other character and nothing more.
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        TokenStream stream = makeStream("1");
        assertEquals("the character 1", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * The digits 1 and 2 are parsed as other character and nothing more.
     * @throws Exception in case of an error
     */
    public void test12() throws Exception {

        TokenStream stream = makeStream("12");
        assertEquals("the character 1", stream.get(fac, tokenizer).toString());
        assertEquals("the character 2", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * ...
     * @throws Exception in case of an error
     */
    public void testCaret1() throws Exception {

        context.setCatcode(new UnicodeChar('^'), Catcode.SUPMARK, true);
        TokenStream stream = makeStream("^1");
        assertEquals("superscript character ^", stream.get(fac, tokenizer)
                .toString());
        assertEquals("the character 1", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * ...
     * @throws Exception in case of an error
     */
    public void testCaretA() throws Exception {

        context.setCatcode(new UnicodeChar('^'), Catcode.SUPMARK, true);
        TokenStream stream = makeStream("^^41");
        assertEquals("the letter A", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * ...
     * @throws Exception in case of an error
     */
    public void testCaretA2() throws Exception {

        context.setCatcode(new UnicodeChar('^'), Catcode.SUPMARK, true);
        TokenStream stream = makeStream("^^A");
        assertEquals("the character \1", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * ...
     * @throws Exception in case of an error
     */
    public void testCaretA3() throws Exception {

        context.setCatcode(new UnicodeChar('^'), Catcode.SUPMARK, true);
        TokenStream stream = makeStream("^^A;");
        assertEquals("the character \1", stream.get(fac, tokenizer).toString());
        assertEquals("the character ;", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * ...
     * @throws Exception in case of an error
     */
    public void testCaretEnd() throws Exception {

        context.setCatcode(new UnicodeChar('^'), Catcode.SUPMARK, true);
        TokenStream stream = makeStream("^");
        assertEquals("superscript character ^", stream.get(fac, tokenizer)
                .toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * ...
     * @throws Exception in case of an error
     */
    public void testCr1() throws Exception {

        TokenStream stream = makeStream("x\nx");
        assertEquals("the letter x", stream.get(fac, tokenizer).toString());
        assertEquals("blank space  ", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * ...
     * @throws Exception in case of an error
     */
    public void testCr2() throws Exception {

        TokenStream stream = makeStream("\n\n"); // mgn: changed
        assertEquals("the control sequence \\par", stream.get(fac, tokenizer)
                .toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * ...
     * @throws Exception in case of an error
     */
    public void testCr3() throws Exception {

        //TokenStream stream  = new TokenStreamBuffersImpl(new String[]{"\naaa", "  x"});
        //assertEquals("the control sequence \\par", stream.get(fac, tokenizer).toString());
        //assertEquals("the letter x", stream.get(fac, tokenizer).toString());
        //assertNull(stream.get(fac, tokenizer));
    }

    /**
     * ...
     *
     * @throws Exception in case of an error
     */
    public void testCr4() throws Exception {

        //TokenStream stream  = new TokenStreamBuffersImpl(new String[]{"\n", "\nx"});
        //assertEquals("the control sequence \\par", stream.get(fac, tokenizer).toString());
        //assertEquals("the control sequence \\par", stream.get(fac, tokenizer).toString());
        //assertNull(stream.get(fac, tokenizer));
    }

    /**
     * The empty string does not contain any characters
     * @throws Exception in case of an error
     */
    public void testEmpty() throws Exception {

        TokenStream stream = makeStream("");
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * ...
     * @throws Exception in case of an error
     */
    public void testIgnore() throws Exception {

        TokenStream stream = makeStream("\0");
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * ...
     * @throws Exception in case of an error
     */
    public void testLetter() throws Exception {

        TokenStream stream = makeStream("A");
        assertEquals("the letter A", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * ...
     * @throws Exception in case of an error
     */
    public void testMixed() throws Exception {

        TokenStream stream = makeStream("12 34");
        assertEquals("the character 1", stream.get(fac, tokenizer).toString());
        assertEquals("the character 2", stream.get(fac, tokenizer).toString());
        assertEquals("blank space  ", stream.get(fac, tokenizer).toString());
        assertEquals("the character 3", stream.get(fac, tokenizer).toString());
        assertEquals("the character 4", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * A single space at the beginning of the processing is skipped
     * @throws Exception in case of an error
     */
    public void testSpace() throws Exception {

        TokenStream stream = makeStream(" ");
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * The character period and space in sequence are parsed into appropriate
     * tokens.
     * @throws Exception in case of an error
     */
    public void testSpace2() throws Exception {

        TokenStream stream = makeStream(". ");
        assertEquals("the character .", stream.get(fac, tokenizer).toString());
        assertEquals("blank space  ", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * The character period and two spaces in sequence are parsed into
     * appropriate tokens. The two spaces are collapsed into one.
     * @throws Exception in case of an error
     */
    public void testSpace3() throws Exception {

        TokenStream stream = makeStream(".  ");
        assertEquals("the character .", stream.get(fac, tokenizer).toString());
        assertEquals("blank space  ", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * Two spaces at the beginning are ignored.
     * @throws Exception in case of an error
     */
    public void testSpaces() throws Exception {

        TokenStream stream = makeStream("  ");
        assertNull(stream.get(fac, tokenizer));
    }

}