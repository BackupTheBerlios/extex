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
package de.dante.extex.token;

import java.util.Iterator;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.impl.ContextImpl;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.TokenFactoryImpl;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.impl.TokenStreamBufferImpl;
import de.dante.extex.scanner.stream.impl.TokenStreamBuffersImpl;
import de.dante.util.StringList;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;

import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class TokenStreamStringImplTest extends TestCase {
    /**
     */
    public TokenStreamStringImplTest(String name) {
        super(name);
    }

    /**
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(TokenStreamStringImplTest.class);
    }
    
    private static TokenFactory fac;
    private static Context context;
    private static Tokenizer tokenizer;
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        fac    = new TokenFactoryImpl();
        context     = new ContextImpl(new MockConfiguration());
        tokenizer = context.getTokenizer();
    }
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * The empty string does not contain any characters
     */
    public void testEmpty() throws Exception {
        TokenStream stream  = new TokenStreamBufferImpl("");
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void test1() throws Exception {
        TokenStream stream  = new TokenStreamBufferImpl("1");
        assertEquals("the character 1", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void test12() throws Exception {
        TokenStream stream  = new TokenStreamBufferImpl("12");
        assertEquals("the character 1", stream.get(fac, tokenizer).toString());
        assertEquals("the character 2", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     * A single space at the beginning of the processing is skipped
     */
    public void testSpace() throws Exception {
        TokenStream stream  = new TokenStreamBufferImpl(" ");
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testSpaces() throws Exception {
        TokenStream stream  = new TokenStreamBufferImpl("  ");
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testSpace2() throws Exception {
        TokenStream stream  = new TokenStreamBufferImpl(". ");
        assertEquals("the character .", stream.get(fac, tokenizer).toString());
        assertEquals("blank space  ", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testSpace3() throws Exception {
        TokenStream stream  = new TokenStreamBufferImpl(". ");
        assertEquals("the character .", stream.get(fac, tokenizer).toString());
        assertEquals("blank space  ", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }
    
    /**
     */
    public void testIgnore() throws Exception {
        TokenStream stream  = new TokenStreamBufferImpl("\0");
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testLetter() throws Exception {
        TokenStream stream  = new TokenStreamBufferImpl("A");
        assertEquals("the letter A", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCaretEnd() throws Exception {
        context.setCatcode('^',Catcode.SUPMARK);
        TokenStream stream  = new TokenStreamBufferImpl("^");
        assertEquals("superscript character ^", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCaret1() throws Exception {
        context.setCatcode('^',Catcode.SUPMARK);
        TokenStream stream  = new TokenStreamBufferImpl("^1");
        assertEquals("superscript character ^", stream.get(fac, tokenizer).toString());
        assertEquals("the character 1", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCaretA() throws Exception {
        context.setCatcode('^',Catcode.SUPMARK);
        TokenStream stream  = new TokenStreamBufferImpl("^^41");
        assertEquals("the letter A", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCaretA2() throws Exception {
        context.setCatcode('^',Catcode.SUPMARK);
        TokenStream stream  = new TokenStreamBufferImpl("^^A");
        assertEquals("the character \1", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCaretA3() throws Exception {
        context.setCatcode('^',Catcode.SUPMARK);
        TokenStream stream  = new TokenStreamBufferImpl("^^A;");
        assertEquals("the character \1", stream.get(fac, tokenizer).toString());
        assertEquals("the character ;", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testMixed() throws Exception {
        TokenStream stream  = new TokenStreamBufferImpl("12 34");
        assertEquals("the character 1", stream.get(fac, tokenizer).toString());
        assertEquals("the character 2", stream.get(fac, tokenizer).toString());
        assertEquals("blank space  ", stream.get(fac, tokenizer).toString());
        assertEquals("the character 3", stream.get(fac, tokenizer).toString());
        assertEquals("the character 4", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCr1() throws Exception {
        TokenStream stream  = new TokenStreamBufferImpl("x\nx");
        assertEquals("the letter x", stream.get(fac, tokenizer).toString());
        assertEquals("blank space  ", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCr2() throws Exception {
        TokenStream stream  = new TokenStreamBufferImpl("\n\n");
        assertEquals("the control sequence par", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCr3() throws Exception {
        TokenStream stream  = new TokenStreamBuffersImpl(new String[]{"\naaa","  x"});
        assertEquals("the control sequence par", stream.get(fac, tokenizer).toString());
        assertEquals("the letter x", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCr4() throws Exception {
        TokenStream stream  = new TokenStreamBuffersImpl(new String[]{"\n","\nx"});
        assertEquals("the control sequence par", stream.get(fac, tokenizer).toString());
        assertEquals("the control sequence par", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }
    
    
    private static class MockConfiguration implements Configuration {
        private String classname = "de.dante.extex.interpreter.context.impl.ContextImpl";
        /**
         * Creates a new object.
         */
        public MockConfiguration() {
            super();
        }
        public MockConfiguration(String cn) {
            super();
            classname = cn;
        }
        /**
         * @see de.dante.util.configuration.Configuration#getAttribute(java.lang.String)
         */
        public String getAttribute(String name) throws ConfigurationException {
            return classname;
        }
        /**
         * @see de.dante.util.configuration.Configuration#getConfiguration(java.lang.String, java.lang.String)
         */
        public Configuration getConfiguration(String key, String attribute)
                throws ConfigurationException {
            return null;
        }
        /**
         * @see de.dante.util.configuration.Configuration#getConfiguration(java.lang.String)
         */
        public Configuration getConfiguration(String key)
                throws ConfigurationException {
            return new MockConfiguration("de.dante.extex.interpreter.context.impl.GroupImpl");
        }
        /**
         * @see de.dante.util.configuration.Configuration#getValue()
         */
        public String getValue() throws ConfigurationException {
            return null;
        }
        /**
         * @see de.dante.util.configuration.Configuration#getValue(java.lang.String)
         */
        public String getValue(String key) throws ConfigurationException {
            return null;
        }
        /**
         * @see de.dante.util.configuration.Configuration#getValueAsInteger(java.lang.String, int)
         */
        public int getValueAsInteger(String key, int defaultValue)
                throws ConfigurationException {
            return 0;
        }
        /**
         * @see de.dante.util.configuration.Configuration#getValues(java.lang.String)
         */
        public StringList getValues(String key) throws ConfigurationException {
            return null;
        }
        /**
         * @see de.dante.util.configuration.Configuration#iterator(java.lang.String)
         */
        public Iterator iterator(String key) throws ConfigurationException {
            return null;
        }
}
    
}
