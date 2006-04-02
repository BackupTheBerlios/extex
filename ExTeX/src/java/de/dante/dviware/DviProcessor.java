/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.dviware;

import java.io.InputStream;

/**
 * This interface describes a callback handler for DVI instructions. This refers
 * to an abstract machine which translates the DVI file into some kind of
 * printed representation.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface DviProcessor {

    /**
     * A DVI <tt>bop</tt> instruction has been encountered.
     * This instruction signals the beginning of a new page.
     *
     * @param off the current byte position in the input stream
     * @param c the array of page number indicators. The array has length 10.
     *  It is initialized from the count registers 0 to 9 at the time the page
     *  is shipped out.
     * @param p the pointer to the previous <tt>bop</tt> instruction or -1
     *  for the first page
     */
    void bop(int off, int[] c, int p);

    /**
     * A DVI <tt>down</tt> instruction has been encountered.
     *
     * <p>
     *  <i>v &larr; v + &lang;a&rang;</i>
     * </p>
     *
     * @param off the current byte position in the input stream
     * @param a the number of DVI units to move down. If negative then the
     *  current position moves upwards.
     */
    void down(int off, int a);

    /**
     * A DVI <tt>eop</tt> instruction has been encountered.
     * This instruction signals the end of a page.
     *
     * @param off the current byte position in the input stream
     */
    void eop(int off);

    /**
     * A DVI <tt>fnt</tt> instruction has been encountered.
     *
     * <p>
     *  <i>f &larr; &lang;k&rang;</i>
     * </p>
     *
     * @param off the current byte position in the input stream
     * @param k the new font number; this number is not negative
     */
    void fnt(int off, int k);

    /**
     * A DVI <tt>fntDef</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param k the number of the font
     * @param c
     * @param s
     * @param d
     * @param n
     */
    void fntDef(int off, int k, int c, int s, int d, String n);

    /**
     * A DVI <tt>nop</tt> instruction has been encountered.
     * This instruction simply does nothing. It just occupies one byte in
     * the input stream.
     *
     * @param off the current byte position in the input stream
     */
    void nop(int off);

    /**
     * A DVI <tt>pop</tt> instruction has been encountered.
     *
     * <p>
     *  The registers h, b, w, x, y, z are poped from the stack.
     * </p>
     *
     * @param off the current byte position in the input stream
     */
    void pop(int off);

    /**
     * A DVI <tt>post</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param p
     * @param num the numerator
     * @param den the denominator
     * @param mag the magnification
     * @param l
     * @param u
     * @param sp
     * @param tp
     */
    void post(int off, int p, int num, int den, int mag, int l, int u, int sp,
            int tp);

    /**
     * Invoke the callback on a POST_POST instruction.
     * This is the last instruction in a DVI file.
     *
     * @param off the offset in the file of this instruction
     * @param bop the index of the last BOP instruction
     * @param id the id of this DVI version. Usually this is 2.
     */
    void postPost(int off, int bop, int id);

    /**
     * A DVI <tt>pre</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param i
     * @param num
     * @param den
     * @param mag
     * @param comment
     */
    void pre(int off, int i, int num, int den, int mag, String comment);

    /**
     * A DVI <tt>push</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     */
    void push(int off);

    /**
     * A DVI <tt>put_char</tt> instruction has been encountered.
     *
     * @param off the current byte position
     * @param c the number of the character to set
     */
    void putChar(int off, int c);

    /**
     * A DVI <tt>put_rule</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param a the width
     * @param b the height
     */
    void putRule(int off, int a, int b);

    /**
     * A DVI <tt>right</tt> instruction has been encountered.
     *
     * <p>
     *  <i>h &larr; h + &lang;b&rang;</i>
     * </p>
     * <p>
     *  The argument b is added to the horizontal position h. If b is positive
     *  then the horizontal position is moved rightward by the given number of
     *  DVI units. If b is negative this means a movement leftwards.
     * </p>
     *
     * @param off the current byte position in the input stream
     * @param b the distance to move in DVI units
     */
    void right(int off, int b);

    /**
     * A DVI <tt>set_char</tt> instruction has been encountered.
     *
     * @param off the current byte position
     * @param c the number of the character to set
     */
    void setChar(int off, int c);

    /**
     * A DVI <tt>set_rule</tt> instruction has been encountered.
     *
     * @param off the current byte position
     * @param a the width
     * @param b the height
     */
    void setRule(int off, int a, int b);

    /**
     * A DVI undefined instruction has been encountered.
     * This callback is invoked for the op-codes 250&ndash;255 which are
     * undefined in <logo>TeX</logo>.
     *
     * @param off the current byte position
     * @param opcode the opcode encountered
     * @param stream the input stream to read further bytes from
     */
    void undef(int off, int opcode, InputStream stream);

    /**
     * A DVI <tt>w</tt> instruction has been encountered.
     *
     * <p>
     *  The register w is assigned from the parameter of this instruction.
     *  Afterwards the register w is added to the horizontal position h.
     * </p>
     *
     * @param off the current byte position in the input stream
     * @param b the distance to add in DVI units
     */
    void w(int off, int b);

    /**
     * A DVI <tt>w0</tt> instruction has been encountered.
     *
     * <p>
     *  <i>h &larr; h + w</i>
     * </p>
     * <p>
     *  The register w is added to the horizontal position h.
     * </p>
     *
     * @param off the current byte position in the input stream
     */
    void w0(int off);

    /**
     * A DVI <tt>x</tt> instruction has been encountered.
     *
     * <p>
     *  <i>x &larr; &lang;b&rang;</i><br>
     *  <i>h &larr; h + x</i>
     * </p>
     *
     * @param off the current byte position in the input stream
     * @param b the distance to move in DVI units
     */
    void x(int off, int b);

    /**
     * A DVI <tt>x0</tt> instruction has been encountered.
     *
     * <p>
     *  <i>h &larr; h + x</i>
     * </p>
     * <p>
     *  The register x is added to the horizontal position h.
     * </p>
     *
     * @param ptr the current byte position in the input stream
     */
    void x0(int ptr);

    /**
     * A DVI <tt>xxx</tt> instruction has been encountered.
     * This instruction is used to pass some bytes uninterpreted to the DVI
     * processor. In <logo>TeX</logo> this is accomplished with the
     * primitive <tt>\special</tt>.
     *
     * @param off the current byte position in the input stream
     * @param x the array of bytes carrying the content
     */
    void xxx(int off, byte[] x);

    /**
     * A DVI <tt>y</tt> instruction has been encountered.
     *
     * <p>
     *  <i>y &larr; &lang;a&rang;</i><br>
     *  <i>v &larr; v + y</i>
     * </p>
     *
     * @param off the current byte position in the input stream
     * @param a the distance to move
     */
    void y(int off, int a);

    /**
     * A DVI <tt>y0</tt> instruction has been encountered.
     *
     * <p>
     *  Set <i>v &larr; v + y</i>; thus the current position is moved down
     *  <i>y</i> DVI units.
     * </p>
     *
     * @param ptr the current byte position in the input stream
     */
    void y0(int ptr);

    /**
     * A DVI <tt>z</tt> instruction has been encountered.
     *
     * <p>
     *  <i>z &larr; &lang;a&rang;</i><br>
     *  <i>v &larr; v + z</i>
     * </p>
     *
     * @param off the current byte position in the input stream
     * @param b the distance to move
     */
    void z(int off, int b);

    /**
     * A DVI <tt>z0</tt> instruction has been encountered.
     *
     * <p>
     *  <i>v &larr; v + z</i>
     * </p>
     *
     * @param ptr the current byte position in the input stream
     */
    void z0(int ptr);

}
