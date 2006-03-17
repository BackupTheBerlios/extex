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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Dvi {

    /**
     * The field <tt>Y1</tt> contains the ...
     */
    public static final int Y1 = 162;

    /**
     * The field <tt>Z1</tt> contains the ...
     */
    public static final int Z1 = 167;

    /**
     * The field <tt>Z0</tt> contains the ...
     */
    public static final int Z0 = 166;

    /**
     * The field <tt>Y0</tt> contains the ...
     */
    public static final int Y0 = 161;

    /**
     * The field <tt>DOWN1</tt> contains the ...
     */
    public static final int DOWN1 = 157;

    /**
     * The field <tt>X1</tt> contains the ...
     */
    public static final int X1 = 153;

    /**
     * The field <tt>X0</tt> contains the ...
     */
    public static final int X0 = 152;

    /**
     * The field <tt>W4</tt> contains the ...
     */
    public static final int W4 = 151;

    /**
     * The field <tt>W3</tt> contains the ...
     */
    public static final int W3 = 150;

    /**
     * The field <tt>BOP</tt> contains the ...
     */
    public static final int BOP = 139;

    /**
     * The field <tt>DVI_ID</tt> contains the ...
     */
    public static final int DVI_ID = 2;

    /**
     * The field <tt>DVI_DEN</tt> contains the ...
     */
    public static final int DVI_UNIT_DENOMINATOR = 473628672;

    /**
     * The field <tt>DVI_NUM</tt> contains the ...
     */
    public static final int DVI_UNIT_NUMERATOR = 25400000;

    /**
     * The field <tt>EOP</tt> contains the ...
     */
    public static final int EOP = 140;

    /**
     * The field <tt>FNT_DEF1</tt> contains the ...
     */
    public static final int FNT_DEF1 = 243;

    /**
     * The field <tt>FNT_DEF2</tt> contains the ...
     */
    public static final int FNT_DEF2 = 244;

    /**
     * The field <tt>FNT_DEF3</tt> contains the ...
     */
    public static final int FNT_DEF3 = 245;

    /**
     * The field <tt>FNT_DEF4</tt> contains the ...
     */
    public static final int FNT_DEF4 = 246;

    /**
     * The field <tt>FNT_NUM_0</tt> contains the ...
     */
    public static final int FNT_NUM_0 = 171;

    /**
     * The field <tt>FNT1</tt> contains the ...
     */
    public static final int FNT1 = 235;

    /**
     * The field <tt>FNT2</tt> contains the ...
     */
    public static final int FNT2 = 236;

    /**
     * The field <tt>FNT3</tt> contains the ...
     */
    public static final int FNT3 = 237;

    /**
     * The field <tt>FNT4</tt> contains the ...
     */
    public static final int FNT4 = 238;

    /**
     * The field <tt>NOP</tt> contains the ...
     */
    public static final int NOP = 138;

    /**
     * The field <tt>SET_RULE</tt> contains the ...
     */
    public static final int PADDING_BYTE = 223;

    /**
     * The field <tt>POP</tt> contains the ...
     */
    public static final int POP = 142;

    /**
     * The field <tt>POST</tt> contains the ...
     */
    public static final int POST = 248;

    /**
     * The field <tt>POST_POST</tt> contains the ...
     */
    public static final int POST_POST = 249;

    /**
     * The field <tt>PRE</tt> contains the ...
     */
    public static final int PRE = 247;

    /**
     * The field <tt>PUSH</tt> contains the ...
     */
    public static final int PUSH = 141;

    /**
     * The field <tt>PUT_CHAR1</tt> contains the ...
     */
    public static final int PUT_CHAR1 = 133;

    /**
     * The field <tt>PUT_CHAR2</tt> contains the ...
     */
    public static final int PUT_CHAR2 = 134;

    /**
     * The field <tt>PUT_CHAR3</tt> contains the ...
     */
    public static final int PUT_CHAR3 = 135;

    /**
     * The field <tt>PUT_CHAR4</tt> contains the ...
     */
    public static final int PUT_CHAR4 = 136;

    /**
     * The field <tt>PUT_RULE</tt> contains the ...
     */
    public static final int PUT_RULE = 137;

    /**
     * The field <tt>RIGHT1</tt> contains the ...
     */
    public static final int RIGHT1 = 143;

    /**
     * The field <tt>RIGHT2</tt> contains the ...
     */
    public static final int RIGHT2 = 144;

    /**
     * The field <tt>RIGHT3</tt> contains the ...
     */
    public static final int RIGHT3 = 145;

    /**
     * The field <tt>RIGHT4</tt> contains the ...
     */
    public static final int RIGHT4 = 146;

    /**
     * The field <tt>SET_CHAR_0</tt> contains the ...
     */
    public static final int SET_CHAR_0 = 0x00;

    /**
     * The field <tt>SET_RULE</tt> contains the ...
     */
    public static final int SET_RULE = 132;

    /**
     * The field <tt>SET1</tt> contains the ...
     */
    public static final int SET1 = 128;

    /**
     * The field <tt>SET2</tt> contains the ...
     */
    public static final int SET2 = 129;

    /**
     * The field <tt>SET3</tt> contains the ...
     */
    public static final int SET3 = 130;

    /**
     * The field <tt>SET4</tt> contains the ...
     */
    public static final int SET4 = 131;

    /**
     * The field <tt>W0</tt> contains the ...
     */
    public static final int W0 = 147;

    /**
     * The field <tt>W1</tt> contains the ...
     */
    public static final int W1 = 148;

    /**
     * The field <tt>W2</tt> contains the ...
     */
    public static final int W2 = 149;

    /**
     * The field <tt>XXX1</tt> contains the ...
     */
    public static final int XXX1 = 239;

    /**
     * The field <tt>XXX2</tt> contains the ...
     */
    public static final int XXX2 = 240;

    /**
     * The field <tt>XXX3</tt> contains the ...
     */
    public static final int XXX3 = 241;

    /**
     * The field <tt>XXX4</tt> contains the ...
     */
    public static final int XXX4 = 242;

    /**
     * The field <tt>dvi</tt> contains the stream to read from.
     */
    private InputStream dvi;

    /**
     * The field <tt>pointer</tt> contains the index of the current character.
     */
    private int pointer = 0;

    /**
     * Creates a new object.
     *
     * @param dvi the input stream
     */
    public Dvi(final InputStream dvi) {

        super();
        this.dvi = dvi;
    }

    /**
     * Parse the input stream and invoke the callbacks on each code found.
     *
     * @param proc the processor
     *
     * @throws IOException in case of an error
     */
    public void parse(final DviProcessor proc) throws IOException {

        int a, b, c;
        int off = pointer;

        while ((c = read1()) >= 0) {
            switch (c) {
                case SET_CHAR_0:
                case 0x01:
                case 0x02:
                case 0x03:
                case 0x04:
                case 0x05:
                case 0x06:
                case 0x07:
                case 0x08:
                case 0x09:
                case 0x0a:
                case 0x0b:
                case 0x0c:
                case 0x0d:
                case 0x0e:
                case 0x0f:
                case 0x10:
                case 0x11:
                case 0x12:
                case 0x13:
                case 0x14:
                case 0x15:
                case 0x16:
                case 0x17:
                case 0x18:
                case 0x19:
                case 0x1a:
                case 0x1b:
                case 0x1c:
                case 0x1d:
                case 0x1e:
                case 0x1f:
                case 0x20:
                case 0x21:
                case 0x22:
                case 0x23:
                case 0x24:
                case 0x25:
                case 0x26:
                case 0x27:
                case 0x28:
                case 0x29:
                case 0x2a:
                case 0x2b:
                case 0x2c:
                case 0x2d:
                case 0x2e:
                case 0x2f:
                case 0x30:
                case 0x31:
                case 0x32:
                case 0x33:
                case 0x34:
                case 0x35:
                case 0x36:
                case 0x37:
                case 0x38:
                case 0x39:
                case 0x3a:
                case 0x3b:
                case 0x3c:
                case 0x3d:
                case 0x3e:
                case 0x3f:
                case 0x40:
                case 0x41:
                case 0x42:
                case 0x43:
                case 0x44:
                case 0x45:
                case 0x46:
                case 0x47:
                case 0x48:
                case 0x49:
                case 0x4a:
                case 0x4b:
                case 0x4c:
                case 0x4d:
                case 0x4e:
                case 0x4f:
                case 0x50:
                case 0x51:
                case 0x52:
                case 0x53:
                case 0x54:
                case 0x55:
                case 0x56:
                case 0x57:
                case 0x58:
                case 0x59:
                case 0x5a:
                case 0x5b:
                case 0x5c:
                case 0x5d:
                case 0x5e:
                case 0x5f:
                case 0x60:
                case 0x61:
                case 0x62:
                case 0x63:
                case 0x64:
                case 0x65:
                case 0x66:
                case 0x67:
                case 0x68:
                case 0x69:
                case 0x6a:
                case 0x6b:
                case 0x6c:
                case 0x6d:
                case 0x6e:
                case 0x6f:
                case 0x70:
                case 0x71:
                case 0x72:
                case 0x73:
                case 0x74:
                case 0x75:
                case 0x76:
                case 0x77:
                case 0x78:
                case 0x79:
                case 0x7a:
                case 0x7b:
                case 0x7c:
                case 0x7d:
                case 0x7e:
                case 0x7f:
                    proc.setChar(off, c);
                    break;
                case SET1:
                    c = read1();
                    proc.setChar(off, c);
                    break;
                case SET2:
                    c = read2();
                    proc.setChar(off, c);
                    break;
                case SET3:
                    c = read3();
                    proc.setChar(off, c);
                    break;
                case SET4:
                    c = read4();
                    proc.setChar(off, c);
                    break;
                case SET_RULE:
                    a = read4();
                    b = read4();
                    proc.setRule(off, a, b);
                    break;
                case PUT_CHAR1:
                    c = read1();
                    proc.putChar(off, c);
                    break;
                case PUT_CHAR2:
                    c = read2();
                    proc.putChar(off, c);
                    break;
                case PUT_CHAR3:
                    c = read3();
                    proc.putChar(off, c);
                    break;
                case PUT_CHAR4:
                    c = read4();
                    proc.putChar(off, c);
                    break;
                case PUT_RULE:
                    a = read4();
                    b = read4();
                    proc.putRule(off, a, b);
                    break;
                case NOP:
                    proc.nop(off);
                    break;
                case BOP:
                    int[] cc = new int[10];
                    cc[0] = read4();
                    cc[1] = read4();
                    cc[2] = read4();
                    cc[3] = read4();
                    cc[4] = read4();
                    cc[5] = read4();
                    cc[6] = read4();
                    cc[7] = read4();
                    cc[8] = read4();
                    cc[9] = read4();
                    c = read4();
                    proc.bop(off, cc, c);
                    break;
                case EOP:
                    proc.eop(off);
                    break;
                case PUSH:
                    proc.push(off);
                    break;
                case POP:
                    proc.pop(off);
                    break;
                case RIGHT1:
                    proc.right(off, read1signed());
                    break;
                case RIGHT2:
                    proc.right(off, read2signed());
                    break;
                case RIGHT3:
                    proc.right(off, read3signed());
                    break;
                case RIGHT4:
                    proc.right(off, read4signed());
                    break;
                case W0:
                    proc.w0(off);
                    break;
                case W1:
                    proc.w(off, read1signed());
                    break;
                case W2:
                    proc.w(off, read2signed());
                    break;
                case W3:
                    proc.w(off, read3signed());
                    break;
                case W4:
                    proc.w(off, read4signed());
                    break;
                case X0:
                    proc.x0(off);
                    break;
                case X1:
                    proc.x(off, read1signed());
                    break;
                case 154:
                    proc.x(off, read2signed());
                    break;
                case 155:
                    proc.x(off, read3signed());
                    break;
                case 156:
                    proc.x(off, read4signed());
                    break;
                case DOWN1:
                    proc.down(off, read1signed());
                    break;
                case 158:
                    proc.down(off, read2signed());
                    break;
                case 159:
                    proc.down(off, read3signed());
                    break;
                case 160:
                    proc.down(off, read4signed());
                    break;
                case Y0:
                    proc.y0(off);
                    break;
                case Y1:
                    proc.y(off, read1signed());
                    break;
                case 163:
                    proc.y(off, read2signed());
                    break;
                case 164:
                    proc.y(off, read3signed());
                    break;
                case 165:
                    proc.y(off, read4signed());
                    break;
                case Z0:
                    proc.z0(off);
                    break;
                case Z1:
                    proc.z(off, read1signed());
                    break;
                case 168:
                    proc.z(off, read2signed());
                    break;
                case 169:
                    proc.z(off, read3signed());
                    break;
                case 170:
                    proc.z(off, read4signed());
                    break;
                case FNT_NUM_0:
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177:
                case 178:
                case 179:
                case 180:
                case 181:
                case 182:
                case 183:
                case 184:
                case 185:
                case 186:
                case 187:
                case 188:
                case 189:
                case 190:
                case 191:
                case 192:
                case 193:
                case 194:
                case 195:
                case 196:
                case 197:
                case 198:
                case 199:
                case 200:
                case 201:
                case 202:
                case 203:
                case 204:
                case 205:
                case 206:
                case 207:
                case 208:
                case 209:
                case 210:
                case 211:
                case 212:
                case 213:
                case 214:
                case 215:
                case 216:
                case 217:
                case 218:
                case 219:
                case 220:
                case 221:
                case 222:
                case 223:
                case 224:
                case 225:
                case 226:
                case 227:
                case 228:
                case 229:
                case 230:
                case 231:
                case 232:
                case 233:
                case 234:
                    proc.fntNum(off, c - FNT_NUM_0);
                    break;
                case FNT1:
                    proc.fnt(off, read1());
                    break;
                case FNT2:
                    proc.fnt(off, read2());
                    break;
                case FNT3:
                    proc.fnt(off, read3());
                    break;
                case FNT4:
                    proc.fnt(off, read4());
                    break;
                case XXX1:
                    proc.xxx(off, readBytes(read1()));
                    break;
                case XXX2:
                    proc.xxx(off, readBytes(read2()));
                    break;
                case XXX3:
                    proc.xxx(off, readBytes(read3()));
                    break;
                case XXX4:
                    proc.xxx(off, readBytes(read4()));
                    break;
                case FNT_DEF1:
                    int k = read1();
                    c = read4();
                    int s = read4();
                    int d = read4();
                    int len = read2();
                    proc.fntDef(off, k, c, s, d, read(len));
                    break;
                case FNT_DEF2:
                    k = read2();
                    c = read4();
                    s = read4();
                    d = read4();
                    len = read2();
                    proc.fntDef(off, k, c, s, d, read(len));
                    break;
                case FNT_DEF3:
                    k = read3();
                    c = read4();
                    s = read4();
                    d = read4();
                    len = read2();
                    proc.fntDef(off, k, c, s, d, read(len));
                    break;
                case FNT_DEF4:
                    k = read4();
                    c = read4();
                    s = read4();
                    d = read4();
                    len = read2();
                    proc.fntDef(off, k, c, s, d, read(len));
                    break;
                case PRE:
                    int id = read1();
                    int num = read4();
                    int den = read4();
                    int mag = read4();
                    String comment = read();
                    proc.pre(off, id, num, den, mag, comment);
                    break;
                case POST:
                    // post p[4] num[4] den[4] mag[4] l[4] u[4] s[2] t[2]
                    int p = read4();
                    num = read4();
                    den = read4();
                    mag = read4();
                    int l = read4();
                    int u = read4();
                    int sp = read2();
                    int tp = read2();
                    proc.post(off, p, num, den, mag, l, u, sp, tp);
                    break;
                case POST_POST:
                    int q = read4();
                    int i = read1();
                    proc.postPost(off, q, i);
                    return;
                default:
                    proc.undef(off, c, dvi);
            }
            off = pointer;
        }
    }

    /**
     * Read a one byte length and that number of bytes.
     *
     * @return the bytes read
     *
     * @throws IOException in case of an error
     */
    private String read() throws IOException {

        pointer++;
        return read(dvi.read());
    }

    /**
     * Read a number of bytes.
     *
     * @param len the number of bytes to read
     *
     * @return the bytes read
     *
     * @throws IOException in case of an error
     */
    private String read(final int len) throws IOException {

        int c;
        StringBuffer sb = new StringBuffer();
        pointer += len;
        for (int i = 0; i < len; i++) {
            c = dvi.read();
            if (c < 0) {
                throw new EOFException();
            }
            sb.append((char) c);
        }
        return sb.toString();
    }

    /**
     * Read one byte into an int.
     *
     * @return the number read
     *
     * @throws IOException in case of an error
     */
    private int read1() throws IOException {

        pointer++;
        return dvi.read();
    }

    /**
     * Read two bytes into an int.
     *
     * @return the number read
     *
     * @throws IOException in case of an error
     */
    private int read2() throws IOException {

        int a = dvi.read();
        int b = dvi.read();
        a = (a << 8) | b;
        pointer += 2;
        if (b < 0) {
            throw new EOFException();
        }
        return a;
    }

    /**
     * Read three bytes into an int.
     *
     * @return the number read
     *
     * @throws IOException in case of an error
     */
    private int read3() throws IOException {

        int a = dvi.read();
        a = (a << 8) | dvi.read();
        a = (a << 8) | dvi.read();
        pointer += 3;
        return a;
    }

    /**
     * Read four bytes into an int.
     *
     * @return the number read
     *
     * @throws IOException in case of an error
     */
    private int read4() throws IOException {

        int a = dvi.read();
        a = (a << 8) | dvi.read();
        a = (a << 8) | dvi.read();
        a = (a << 8) | dvi.read();
        pointer += 4;
        return a;
    }

    /**
     * Read one byte into an int.
     *
     * @return the number read
     *
     * @throws IOException in case of an error
     */
    private int read1signed() throws IOException {

        pointer++;
        int a = dvi.read();
        return ((a & 0x80) == 0 ? a: (0xffffff00 | a));
    }

    /**
     * Read two bytes into an int.
     *
     * @return the number read
     *
     * @throws IOException in case of an error
     */
    private int read2signed() throws IOException {

        int a = dvi.read();
        int b = dvi.read();
        a = (a << 8) | b;
        pointer += 2;
        if (b < 0) {
            throw new EOFException();
        }
        return ((a & 0x8000) == 0 ? a: (0xffff0000 | a));
    }

    /**
     * Read three bytes into an int.
     *
     * @return the number read
     *
     * @throws IOException in case of an error
     */
    private int read3signed() throws IOException {

        int a = dvi.read();
        a = (a << 8) | dvi.read();
        a = (a << 8) | dvi.read();
        pointer += 3;
        return ((a & 0x800000) == 0 ? a: (0xff000000 | a));
    }

    /**
     * Read four bytes into an int.
     *
     * @return the number read
     *
     * @throws IOException in case of an error
     */
    private int read4signed() throws IOException {

        int a = dvi.read();
        a = (a << 8) | dvi.read();
        a = (a << 8) | dvi.read();
        a = (a << 8) | dvi.read();
        pointer += 4;
        return a;
    }

    /**
     * Read a number of bytes.
     *
     * @param len the number of bytes to read
     *
     * @return the bytes read
     *
     * @throws IOException in case of an error
     */
    private byte[] readBytes(final int len) throws IOException {

        int c;
        byte[] bytes = new byte[len];
        pointer += len;
        for (int i = 0; i < len; i++) {
            c = dvi.read();
            if (c < 0) {
                throw new EOFException();
            }
            bytes[i] = (byte) c;
        }
        return bytes;
    }

}
