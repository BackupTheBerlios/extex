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

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.impl.ContextImpl;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.TokenFactoryImpl;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.impl.TokenStreamBufferImpl;

import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
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

    /**
     * The empty string does not contain any characters
     */
    public void testEmpty() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl("");
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void test1() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl("1");
        assertEquals("<other 1>", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void test12() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl("12");
        assertEquals("<other 1>", stream.get(fac, tokenizer).toString());
        assertEquals("<other 2>", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testSpace() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl(" ");
        assertEquals("<space ' '>", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testSpaces() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl("  ");
        assertEquals("<space '  '>", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testIgnore() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl("\0");
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testLetter() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl("A");
        assertEquals("<letter A>", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCaretEnd() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        context.setCatcode('^',Catcode.SUPMARK);
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl("^");
        assertEquals("<supMark ^>", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCaret1() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        context.setCatcode('^',Catcode.SUPMARK);
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl("^1");
        assertEquals("<supMark ^>", stream.get(fac, tokenizer).toString());
        assertEquals("<other 1>", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCaretA() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        context.setCatcode('^',Catcode.SUPMARK);
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl("^^41");
        assertEquals("<letter A>", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCaretA2() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        context.setCatcode('^',Catcode.SUPMARK);
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl("^^A");
        assertEquals("<other \1>", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }

    /**
     */
    public void testCaretA3() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        context.setCatcode('^',Catcode.SUPMARK);
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl("^^A;");
        assertEquals("<other \1>", stream.get(fac, tokenizer).toString());
        assertEquals("<other ;>", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }


    /**
     */
    public void testMixed() throws Exception {
        TokenFactory fac    = new TokenFactoryImpl();
        Context context     = new ContextImpl();
        Tokenizer tokenizer = context.getTokenizer();
        TokenStream stream  = new TokenStreamBufferImpl("12 34");
        assertEquals("<other 1>", stream.get(fac, tokenizer).toString());
        assertEquals("<other 2>", stream.get(fac, tokenizer).toString());
        assertEquals("<space ' '>", stream.get(fac, tokenizer).toString());
        assertEquals("<other 3>", stream.get(fac, tokenizer).toString());
        assertEquals("<other 4>", stream.get(fac, tokenizer).toString());
        assertNull(stream.get(fac, tokenizer));
    }
}
