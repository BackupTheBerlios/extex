/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import de.dante.extex.font.type.tfm.TFMFixWord;

/**
 * Writer for the PL-Format.
 *
 * <p>
 * see TFtoPL
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class PlWriter extends PrintWriter {

    /**
     * level of the current property list
     */
    private int level = 0;

    /**
     * newline set
     */
    private boolean newLine = true;

    /**
     * Write a character.
     * @param c the char
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter out(final char c) {

        print(c);
        newLine = false;
        return this;
    }

    /**
     * Write a String.
     * @param s the string
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter out(final String s) {

        print(s);
        newLine = false;
        return this;
    }

    /**
     * Prints new line and the apropriate amount of indentation.
     * @return Return this, reference for subsequent printing.
     */
    private PlWriter outLn() {

        println();
        newLine = true;
        for (int i = level; i > 0; i--) {
            print("   ");
        }
        return this;
    }

    /**
     * Increases nesting level and prints left parenthesis
     * folowed by the property name.
     * @param   s the property name.
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter plopen(final String s) {

        if (!newLine) {
            outLn();
        }
        level++;
        return out('(').out(s);
    }

    /**
     * Decreases nesting level and prints right parenthesis.
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter plclose() {

        level--;
        return out(')').outLn();
    }

    /**
     * Prints <code>D</code> prefix and decimal number.
     * @param   i the number to be printed.
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter addDec(final int i) {

        return out(" D " + i);
    }

    /**
     * Prints <code>O</code> prefix and octal number.
     * @param   i the number to be printed.
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter addOct(final int i) {

        return out(" O " + Integer.toOctalString(i));
    }

    /**
     * Prints <code>R</code> prefix and real number.
     * @param   o the object which represents the real number.
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter addReal(final Object o) {

        return out(" R " + o.toString());
    }

    /**
     * Prints <code>R</code> prefix and real number.
     * @param   d    the real number.
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter addReal(final double d) {

        return out(" R " + d);
    }

    /**
     * Roman or Italic slope
     */
    private static final char[] RI = {'R', 'I'};

    /**
     * Medium, Bold or Light weight
     */
    private static final char[] MBL = {'M', 'B', 'L'};

    /**
     * Regular, Condensed or Extended expansion
     */
    private static final char[] RCE = {'R', 'C', 'E'};

    /**
     * Prints <code>F</code> prefix and Xerox face code.
     * The code is printed in the three character slope/weight/expansion form
     * or in octal if the symbolic form cannot be found.
     * @param   face the Xerox face code to be printed.
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter addFace(final int face) {

        int f = face;
        int ri = f % RI.length;
        f /= RI.length;
        int mbl = f % MBL.length;
        f /= MBL.length;
        int rce = f % RCE.length;
        f /= RCE.length;
        return (f != 0) ? addOct(face) : out(" F ").out(MBL[mbl]).out(RI[ri])
                .out(RCE[rce]);
    }

    /**
     * Prints a character string after one space.
     * @param   s the string to be printed.
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter addStr(final String s) {

        return out(' ').out(s);
    }

    /**
     * Prints a symbolic form of boolean value.
     * @param   b the boolean value to be printed.
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter addBool(final boolean b) {

        return out(b ? " TRUE" : " FALSE");
    }

    /**
     * If set, the character codes are printed in octal even if they
     * represent a printable character.
     */
    private boolean octChars = false;

    /**
     * Prints symbolic representation of character code.
     * If the character code represents printable character and the member
     * <code>octChars</code> is <code>false</code> then
     * it prints <code>C</code> prefix folowed by the
     * character.
     * Otherwise it prints the octal representation
     * (with <code>O</code> prefix).
     * @param   c the character code to be printed.
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter addChar(final short c) {

        return (!octChars && ('0' <= c && c <= '9' || 'A' <= c && c <= 'Z' || 'a' <= c
                && c <= 'z')) ? out(" C ").out((char) c) : addOct(c);
    }

    /**
     * Print always character in numerical (octal) format.
     */
    public void forceNumChars() {

        octChars = true;
    }

    /**
     * Finishes all posibly unclosed property lists and closes the output.
     */
    public void close() {

        while (level-- > 0) {
            plclose();
        }
        super.close();
    }

    /**
     * Add a comment.
     * @param s the string
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter addComment(final String s) {

        return plopen("COMMENT").addStr(s).plclose();
    }

    /**
     * Add a Fixword.
     * @param fw    the TFmFixWord
     * @param name  the name
     * @return Return this, reference for subsequent printing.
     */
    public PlWriter addFixWord(final TFMFixWord fw, final String name) {

        if (fw != null && fw.getValue() != 0) {
            plopen(name).addReal(fw).plclose();
        } else {
            if (printZeroWidth && "CHARWD".equals(name)) {
                plopen(name).addReal(0.0d).plclose();
            }
        }
        return this;
    }

    /**
     * Print a zero fixpointwidth.
     */
    private boolean printZeroWidth = false;

    /**
     * Print a zero fixpointwidth.
     * @param pzw   print or not
     */
    public void printZeroWidth(final boolean pzw) {

        printZeroWidth = pzw;
    }

    /**
     * Create a new PlWriter.
     *
     * @param  out        A character-output stream
     * @param  autoFlush  A boolean; if true, the println() methods will flush
     *                    the output buffer
     */
    public PlWriter(final Writer out, final boolean autoFlush) {

        super(out, autoFlush);
    }

    /**
     * Create a new PlWriter, without automatic line flushing, from an
     * existing OutputStream.  This convenience constructor creates the
     * necessary intermediate OutputStreamWriter, which will convert characters
     * into bytes using the default character encoding.
     *
     * @param  out        An output stream
     * @see java.io.OutputStreamWriter#OutputStreamWriter(java.io.OutputStream)
     */
    public PlWriter(final OutputStream out) {

        this(out, false);
    }

    /**
     * Create a new PlWriter from an existing OutputStream.  This
     * convenience constructor creates the necessary intermediate
     * OutputStreamWriter, which will convert characters into bytes using the
     * default character encoding.
     *
     * @param  out        An output stream
     * @param  autoFlush  A boolean; if true, the println() methods will flush
     *                    the output buffer
     *
     * @see java.io.OutputStreamWriter#OutputStreamWriter(java.io.OutputStream)
     */
    public PlWriter(final OutputStream out, final boolean autoFlush) {

        this(new BufferedWriter(new OutputStreamWriter(out)), autoFlush);
    }
}
