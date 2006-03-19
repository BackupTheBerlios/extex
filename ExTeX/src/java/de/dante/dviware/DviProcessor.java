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
 * @version $Revision: 1.3 $
 */
public interface DviProcessor {

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
     * A DVI <tt>nop</tt> instruction has been encountered.
     * This instruction simply does nothing. It just occupies one byte in
     * the input stream.
     *
     * @param off the current byte position in the input stream
     */
    void nop(int off);

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
     * A DVI <tt>eop</tt> instruction has been encountered.
     * This instruction signals the end of a page.
     *
     * @param off the current byte position in the input stream
     */
    void eop(int off);

    /**
     * A DVI <tt>push</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     */
    void push(int off);

    /**
     * A DVI <tt>pop</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     */
    void pop(int off);

    //right1 143 b[1]. Set h ? h+b, i.e., move right b units. The parameter is a signed number in two's complement notation, -128 ? b<128; if b<0, the reference point moves left.
    //
    //right2 144 b[2]. Same as right1, except that b is a two-byte quantity in the range -32768 ? b<32768.
    //
    //right3 145 b[3]. Same as right1, except that b is a three-byte quantity in the range -223 ? b<223.
    //
    //right4 146 b[4]. Same as right1, except that b is a four-byte quantity in the range -231 ? b<231.

    /**
     * A DVI <tt>right</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param b
     */
    void right(int off, int b);

    //w0 147. Set h ? h+w; i.e., move right w units. With luck, this parameterless command will usually suffice, because the same kind of motion will occur several times in succession; the following commands explain how w gets particular values.
    /**
     * A DVI <tt>w0</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     */
    void w0(int ptr);

    //w1 148 b[1]. Set w ? b and h ? h+b. The value of b is a signed quantity in two's complement notation, -128 ? b<128. This command changes the current w spacing and moves right by b.
    //
    //w2 149 b[2]. Same as w1, but b is two bytes long, -32768 ? b<32768.
    //
    //w3 150 b[3]. Same as w1, but b is three bytes long, -223 ? b<223.
    //
    //w4 151 b[4]. Same as w1, but b is four bytes long, -231 ? b<231.

    /**
     * A DVI <tt>w</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param b
     */
    void w(int off, int b);

    //x0 152. Set h ? h+x; i.e., move right x units. The ` x' commands are like the `w' commands except that they involve x instead of w.
    /**
     * A DVI <tt>x0</tt> instruction has been encountered.
     *
     * @param ptr the current byte position in the input stream
     */
    void x0(int ptr);

    //x1 153 b[1]. Set x ? b and h ? h+b. The value of b is a signed quantity in two's complement notation, -128 ? b<128. This command changes the current x spacing and moves right by b.
    //
    //x2 154 b[2]. Same as x1, but b is two bytes long, -32768 ? b<32768.
    //
    //x3 155 b[3]. Same as x1, but b is three bytes long, -223 ? b<223.
    //
    //x4 156 b[4]. Same as x1, but b is four bytes long, -231 ? b<231.

    /**
     * A DVI <tt>x</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param b
     */
    void x(int off, int b);

    //down1 157 a[1]. Set v ? v+a, i.e., move down a units. The parameter is a signed number in two's complement notation, -128 ? a<128; if a<0, the reference point moves up.
    //
    //down2 158 a[2]. Same as down1, except that a is a two-byte quantity in the range -32768 ? a<32768.
    //
    //down3 159 a[3]. Same as down1, except that a is a three-byte quantity in the range -223 ? a<223.
    //
    //down4 160 a[4]. Same as down1, except that a is a four-byte quantity in the range -231 ? a<231.

    /**
     * A DVI <tt>down</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param a the number of DVI units to move down. If negative then the
     *  current position moves upwards.
     */
    void down(int off, int a);

    /**
     * A DVI <tt>y0</tt> instruction has been encountered.
     * <p>
     *  Set <i>v &larr; v + y</i>; thus the current position is moved down
     *  <i>y</i> DVI units.
     * </p>
     *
     * @param ptr the current byte position in the input stream
     */
    void y0(int ptr);

    //y1 162 a[1]. Set y ? a and v ? v+a. The value of a is a signed quantity in two's complement notation, -128 ? a<128. This command changes the current y spacing and moves down by a.
    //
    //y2 163 a[2]. Same as y1, but a is two bytes long, -32768 ? a<32768.
    //
    //y3 164 a[3]. Same as y1, but a is three bytes long, -223 ? a<223.
    //
    //y4 165 a[4]. Same as y1, but a is four bytes long, -231 ? a<231.

    /**
     * A DVI <tt>y</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param a
     */
    void y(int off, int a);

    //z0 166. Set v ? v+z; i.e., move down z units. The `z' commands are like the `y' commands except that they involve z instead of y.
    /**
     * A DVI <tt>z0</tt> instruction has been encountered.
     *
     * @param ptr the current byte position in the input stream
     */
    void z0(int ptr);

    //z1 167 a[1]. Set z ? a and v ? v+a. The value of a is a signed quantity in two's complement notation, -128 ? a<128. This command changes the current z spacing and moves down by a.
    //
    //z2 168 a[2]. Same as z1, but a is two bytes long, -32768 ? a<32768.
    //
    //z3 169 a[3]. Same as z1, but a is three bytes long, -223 ? a<223.
    //
    //z4 170 a[4]. Same as z1, but a is four bytes long, -231 ? a<231.

    /**
     * A DVI <tt>z</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param b
     */
    void z(int off, int b);

    //fnt_num_0 171. Set f ? 0. Font 0 must previously have been defined by a fnt_def instruction, as explained below.
    //
    //fnt_num_1 through fnt_num_63 (opcodes 172 to 234). Set f ? 1, ..., f ? 63, respectively.

    /**
     * A DVI <tt>fntNum</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param f
     */
    void fntNum(int off, int f);

    //fnt1 235 k[1]. Set f ? k. TEX82 uses this command for font numbers in the range 64 ? k<256.
    //
    //fnt2 236 k[2]. Same as fnt1, except that k is two bytes long, so it is in the range 0 ? k<65536. TEX82 never generates this command, but large font numbers may prove useful for specifications of color or texture, or they may be used for special fonts that have fixed numbers in some external coding scheme.
    //
    //fnt3 237 k[3]. Same as fnt1, except that k is three bytes long, so it can be as large as 224-1.
    //
    //fnt4 238 k[4]. Same as fnt1, except that k is four bytes long; this is for the really big font numbers (and for the negative ones).

    /**
     * A DVI <tt>fnt</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param k
     */
    void fnt(int off, int k);

    //xxx1 239 k[1] x[k]. This command is undefined in general; it functions as a (k+2)-byte nop unless special DVI-reading programs are being used. TEX82 generates xxx1 when a short enough \special appears, setting k to the number of bytes being sent. It is recommended that x be a string having the form of a keyword followed by possible parameters relevant to that keyword.
    //
    //xxx2 240 k[2] x[k]. Like xxx1, but 0 ? k<65536.
    //
    //xxx3 241 k[3] x[k]. Like xxx1, but 0 ? k< 224.
    //
    //xxx4 242 k[4] x[k]. Like xxx1, but k can be ridiculously large. TEX82 uses xxx4 when sending a string of length 256 or more.

    /**
     * A DVI <tt>xxx</tt> instruction has been encountered.
     * This instruction is used to pass some bytes uninterpreted to the DVI
     * processor. In <logo>TeX</logo> this is accomplished with the
     * primitive <tt>\special</tt>.
     *
     * @param off the current byte position in the input stream
     * @param x
     */
    void xxx(int off, byte[] x);

    //fnt_def1 243 k[1] c[4] s[4] d[4] a[1] l[1] n[a+l]. Define font k, where 0 ? k<256; font definitions will be explained shortly.
    //
    //fnt_def2 244 k[2] c[4] s[4] d[4] a[1] l[1] n[a+l]. Define font k, where 0 ? k<65536.
    //
    //fnt_def3 245 k[3] c[4] s[4] d[4] a[1] l[1] n[a+l]. Define font k, where 0 ? k<224.
    //
    //fnt_def4 246 k[4] c[4] s[4] d[4] a[1] l[1] n[a+l]. Define font k, where -231 ? k<231.

    /**
     * A DVI <tt>fntDef</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param k
     * @param c
     * @param s
     * @param d
     * @param n
     */
    void fntDef(int off, int k, int c, int s, int d, String n);

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
     * A DVI <tt>post</tt> instruction has been encountered.
     *
     * @param off the current byte position in the input stream
     * @param p
     * @param num
     * @param den
     * @param mag
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
     * A DVI undefined instruction has been encountered.
     * This callback is invoked for the op-codes 250-255 wich are undefined
     * in <logo>TeX</logo>.
     *
     * @param off the current byte position
     * @param opcode the opcode encountered
     * @param stream the input stream to read further bytes from
     */
    void undef(int off, int opcode, InputStream stream);

}
