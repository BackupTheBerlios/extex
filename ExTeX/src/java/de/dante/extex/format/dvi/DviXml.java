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

package de.dante.extex.format.dvi;

import java.io.IOException;

import org.jdom.Element;

import de.dante.extex.format.dvi.exception.DviException;
import de.dante.extex.format.dvi.exception.DviUndefinedOpcodeException;
import de.dante.util.file.random.RandomAccessR;

/**
 * DVI to XML converter.
 *
 * <p>
 * Commands are taken from DVItype 3.4.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */

public class DviXml implements DviInterpreter {

    /**
     * the root element
     */
    private Element root;

    /**
     * Create a new object.
     *
     * @param element   the root element
     */
    public DviXml(final Element element) {

        root = element;
    }

    /**
     * bob length
     */
    public static final int BOP_LENGTH = 10;

    /**
     * the unit
     */
    private static final int UNIT = 1000;

    /**
     * Interpreter step for setchar
     *
     * set_char_1 through set_char_127 (opcodes 1 to 127):
     * Do the operations of <code>set_char_0</code>; but use the character whose
     * number matches the opcode, instead of character 0.
     */
    private DviInterpreterStep setchar = new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("setchar");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("char", String.valueOf(opcode));
            root.addContent(element);
        }
    };

    /**
     * the min fontnum
     */
    private static final int MIN_FONTNUM = 172;

    /**
     * Interpreter step for fontnum
     *
     * fnt_num_1 through fnt_num_63  (opcodes 172 to 234):
     * Set <code>f = 1</code>, ..., <code>f = 63</code>, respectively.
     */
    private DviInterpreterStep fntnum = new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            int fn = opcode - MIN_FONTNUM + 1;
            Element element = new Element("fntnum" + String.valueOf(fn));
            element.setAttribute("opcode", String.valueOf(opcode));
            root.addContent(element);
        }
    };

    /**
     * Interpreter step for undefined opcode
     */
    private DviInterpreterStep undef = new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            throw new DviUndefinedOpcodeException();
        }
    };

    /**
     * the interpreter steps
     */
    private DviInterpreterStep[] opcodearray = {//
    /**
     * set_char_0:
     * Typeset character number 0 from font <code>f</code> such that the reference
     * point of the character is at <code>(h,v)</code>.
     * Then increase <code>h</code> by the width
     * of that character. Note that a character may have zero or negative
     * width, so one cannot be sure that <code>h</code> will advance after this
     * command; but <code>h</code> usually does increase.
     */
    setchar, /* 0 */

    /**
     * set_char_1 through set_char_127 (opcodes 1 to 127):
     * Do the operations of <code>set_char_0</code>; but use the character whose
     * number matches the opcode, instead of character 0.
     */
    setchar, /* 1 */
    setchar, /* 2 */
    setchar, /* 3 */
    setchar, /* 4 */
    setchar, /* 5 */
    setchar, /* 6 */
    setchar, /* 7 */
    setchar, /* 8 */
    setchar, /* 9 */
    setchar, /* 10 */
    setchar, /* 11 */
    setchar, /* 12 */
    setchar, /* 13 */
    setchar, /* 14 */
    setchar, /* 15 */
    setchar, /* 16 */
    setchar, /* 17 */
    setchar, /* 18 */
    setchar, /* 19 */
    setchar, /* 20 */
    setchar, /* 21 */
    setchar, /* 22 */
    setchar, /* 23 */
    setchar, /* 24 */
    setchar, /* 25 */
    setchar, /* 26 */
    setchar, /* 27 */
    setchar, /* 28 */
    setchar, /* 29 */
    setchar, /* 30 */
    setchar, /* 31 */
    setchar, /* 32 */
    setchar, /* 33 */
    setchar, /* 34 */
    setchar, /* 35 */
    setchar, /* 36 */
    setchar, /* 37 */
    setchar, /* 38 */
    setchar, /* 39 */
    setchar, /* 40 */
    setchar, /* 41 */
    setchar, /* 42 */
    setchar, /* 43 */
    setchar, /* 44 */
    setchar, /* 45 */
    setchar, /* 46 */
    setchar, /* 47 */
    setchar, /* 48 */
    setchar, /* 49 */
    setchar, /* 50 */
    setchar, /* 51 */
    setchar, /* 52 */
    setchar, /* 53 */
    setchar, /* 54 */
    setchar, /* 55 */
    setchar, /* 56 */
    setchar, /* 57 */
    setchar, /* 58 */
    setchar, /* 59 */
    setchar, /* 60 */
    setchar, /* 61 */
    setchar, /* 62 */
    setchar, /* 63 */
    setchar, /* 64 */
    setchar, /* 65 */
    setchar, /* 66 */
    setchar, /* 67 */
    setchar, /* 68 */
    setchar, /* 69 */
    setchar, /* 70 */
    setchar, /* 71 */
    setchar, /* 72 */
    setchar, /* 73 */
    setchar, /* 74 */
    setchar, /* 75 */
    setchar, /* 76 */
    setchar, /* 77 */
    setchar, /* 78 */
    setchar, /* 79 */
    setchar, /* 80 */
    setchar, /* 81 */
    setchar, /* 82 */
    setchar, /* 83 */
    setchar, /* 84 */
    setchar, /* 85 */
    setchar, /* 86 */
    setchar, /* 87 */
    setchar, /* 88 */
    setchar, /* 89 */
    setchar, /* 90 */
    setchar, /* 91 */
    setchar, /* 92 */
    setchar, /* 93 */
    setchar, /* 94 */
    setchar, /* 95 */
    setchar, /* 96 */
    setchar, /* 97 */
    setchar, /* 98 */
    setchar, /* 99 */
    setchar, /* 100 */
    setchar, /* 101 */
    setchar, /* 102 */
    setchar, /* 103 */
    setchar, /* 104 */
    setchar, /* 105 */
    setchar, /* 106 */
    setchar, /* 107 */
    setchar, /* 108 */
    setchar, /* 109 */
    setchar, /* 110 */
    setchar, /* 111 */
    setchar, /* 112 */
    setchar, /* 113 */
    setchar, /* 114 */
    setchar, /* 115 */
    setchar, /* 116 */
    setchar, /* 117 */
    setchar, /* 118 */
    setchar, /* 119 */
    setchar, /* 120 */
    setchar, /* 121 */
    setchar, /* 122 */
    setchar, /* 123 */
    setchar, /* 124 */
    setchar, /* 125 */
    setchar, /* 126 */
    setchar, /* 127 */

    /**
     * set1 128, c[1]:
     * Same as <code>set_char_0</code>, except that character number
     * <code>c</code> is typeset. TeX82 uses this command for
     * characters in the range 128 &lt;= c &lt; 256.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            // 8 bit
            int ch = rar.readByteAsInt();
            Element element = new Element("set1");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("char", String.valueOf(ch));
            root.addContent(element);
        }
    },

    /**
     * set2 129, c[2]:
     * Same as <code>set1</code>, except that <code>c</code> is two bytes long,
     * so it is in the range 0 &lt;= c &lt; 65536. TeX82 never uses this command,
     * which is intended for processors that deal with oriental languages; but a
     * DVI processor should allow character codes greater than 255.
     * The processor may then assume that these characters have the same width
     * as the character whose respective codes are c mod 256.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            // 16 bit
            int ch = rar.readShort();
            Element element = new Element("set2");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("char", String.valueOf(ch));
            root.addContent(element);
        }
    },

    /**
     * set3  130, c[3]:
     * Same as set1, except that <code>c</code> is three bytes long, so it can be
     * as large as 2^24-1.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            // 24 bit
            int ch = rar.readInt24();
            Element element = new Element("set3");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("char", String.valueOf(ch));
            root.addContent(element);
        }
    },

    /**
     * set4  131, c[+4]:
     * Same as <code>set1</code>, except that <code>c</code> is four bytes long,
     * possibly even negative. Imagine that.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            // 32 bit
            int ch = rar.readInt();
            Element element = new Element("set4");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("char", String.valueOf(ch));
            root.addContent(element);
        }
    },

    /**
     * set_rule  132, a[+4] b[+4]:
     * Typeset a solid black rectangle of height <code>a</code> and
     * width <code>b</code>, with its bottom left corner at <code>(h,v)</code>.
     * Then set <code>h = h + b</code>. If either a &lt;= 0 or
     * b &lt;= 0, nothing should be typeset.
     * Note that if b &lt; 0, the value of <code>h</code> will decrease even
     * though nothing else happens. Programs that typeset from DVI files
     * should be careful to make the rules line up carefully with digitized
     * characters, as explained in connection with the <code>rule_pixels</code>
     * subroutine below.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            int a = rar.readInt();
            int b = rar.readInt();

            Element element = new Element("setrule");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("height", String.valueOf(a));
            element.setAttribute("width", String.valueOf(b));
            root.addContent(element);
        }
    },

    /**
     * put1  133, c[1]:
     * Typeset character number <code>c</code> from font <code>f</code> such
     * that the reference point of the character is at <code>(h,v)</code>.
     * (The 'put' commands are exactly like the 'set' commands,
     * except that they simply put out a character or a rule without moving
     * the reference point afterwards.)
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            int c = rar.readByteAsInt();

            Element element = new Element("put1");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("char", String.valueOf(c));
            root.addContent(element);
        }
    },

    /**
     * put2  134, c[2]:
     * Same as <code>set2</code>, except that <code>h</code> is not changed.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            int c = rar.readShort();

            Element element = new Element("put2");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("char", String.valueOf(c));
            root.addContent(element);
        }
    },

    /**
     * put3  135, c[3]:
     * Same as <code>set3</code>, except that <code>h</code> is not changed.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            int c = rar.readInt24();

            Element element = new Element("put3");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("char", String.valueOf(c));
            root.addContent(element);
        }
    },

    /**
     * put4  136, c[+4]:
     * Same as <code>set4</code>, except that <code>h</code> is not changed.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            int c = rar.readInt();

            Element element = new Element("put4");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("char", String.valueOf(c));
            root.addContent(element);
        }
    },

    /**
     * put_rule  137, a[+4] b[+4]:
     * Same as <code>set_rule</code>, except that <code>h</code> is not changed.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            int a = rar.readInt();
            int b = rar.readInt();

            Element element = new Element("putrule");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("height", String.valueOf(a));
            element.setAttribute("width", String.valueOf(b));
            root.addContent(element);
        }
    },

    /**
     * nop  138:
     * No operation, do nothing. Any number of <code>nop</code>'s may occur between
     * DVI commands, but a <code>nop</code> cannot be inserted between a
     * command and its parameters or between two parameters.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("nop");
            element.setAttribute("opcode", String.valueOf(opcode));
            root.addContent(element);
        }
    },

    /**
     * bop 139, c_0[+4] c_1[+4] ... c_9[+4] p[+4]:
     * Beginning of a page: Set <code>(h,v,w,x,y,z) = (0,0,0,0,0,0)</code>
     * and set the stack empty. Set the current font <code>f</code> to an
     * undefined value. The ten <code>c_i</code> parameters can be used
     * to identify pages, if a user wants to print only part of a DVI file;
     * TeX82 gives them the values of <code>count0</code> ... <code>count9</code>
     * at the time <code>shipout</code> was invoked for this page.
     * The parameter <code>p</code> points to the previous <code>bop</code>
     * command in the file, where the first <code>bop</code>
     * has <code>p=-1</code>.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("bop");
            element.setAttribute("opcode", String.valueOf(opcode));
            for (int i = 0; i < BOP_LENGTH; i++) {
                element.setAttribute("c" + String.valueOf(i), String
                        .valueOf(rar.readInt()));
            }
            element.setAttribute("p", String.valueOf(rar.readInt()));
            root.addContent(element);
        }
    },

    /**
     * eop  140:
     * End of page: Print what you have read since the previous <code>bop</code>.
     * At this point the stack should be empty.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("eop");
            element.setAttribute("opcode", String.valueOf(opcode));
            root.addContent(element);
        }
    },

    /**
     * push  141:
     * Push the current values of <code>(h,v,w,x,y,z)</code> onto the top
     * of the stack; do not change any of these values.
     * Note that <code>f</code> is not pushed.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("push");
            element.setAttribute("opcode", String.valueOf(opcode));
            root.addContent(element);
        }
    },

    /**
     * pop  142:
     * Pop the top six values off of the stack and assign them to
     * <code>(h,v,w,x,y,z)</code>. The number of pops should never
     * exceed the number of pushes, since it would be highly embarrassing
     * if the stack were empty at the time of a <code>pop</code> command.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("pop");
            element.setAttribute("opcode", String.valueOf(opcode));
            root.addContent(element);
        }
    },

    /**
     * right1  143, b[+1]:
     * Set <code>h = h + b</code>, i.e., move right <code>b</code> units.
     * The parameter is a signed number in two's complement notation,
     * -128 &lt;= b &lt; 128; if b &lt; 0, the reference point actually
     * moves left.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("right1");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readByteAsInt()));
            root.addContent(element);
        }
    },

    /**
     * right2:  144, b[+2]:
     * Same as <code>right1</code>, except that <code>b</code> is a two-byte
     * quantity in the range -32768 &lt;= b &lt; 32768.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("right2");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readShort()));
            root.addContent(element);
        }
    },

    /**
     * right3  145, b[+3]:
     * Same as <code>right1</code>, except that <code>b</code> is a three-byte
     * quantity in the range -2^23 &lt;= b &lt; 2^23.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("right3");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt24()));
            root.addContent(element);
        }
    },

    /**
     * right4  146, b[+4]:
     * Same as <code>right1</code>, except that <code>b</code> is a four-byte
     * quantity in the range -2^31 &lt;= b &lt; 2^31.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("right4");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt()));
            root.addContent(element);
        }
    },

    /**
     * w0  147:
     * Set <code>h = h + w</code>; i.e., move right <code>w</code> units.
     * With luck, this parameterless command will usually suffice,
     * because the same kind of motion will occur several times in succession;
     * the following commands explain how <code>w</code> gets particular values.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("w0");
            element.setAttribute("opcode", String.valueOf(opcode));
            root.addContent(element);
        }
    },

    /**
     * w1  148, b[+1]:
     * Set <code>w = b</code> and <code>h = h + b</code>. The value of
     * <code>b</code> is a signed quantity in two's complement notation,
     * -128 &lt;= b /lt; 128. This command changes the current
     * <code>w</code> spacing and moves right by <code>b</code>.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("w1");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readByteAsInt()));
            root.addContent(element);
        }
    },

    /**
     * w2  149, b[+2]:
     * Same as <code>w1</code>, but <code>b</code> is a two-byte-long parameter,
     * -32768 &lt;= b &lt; 32768.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("w2");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readShort()));
            root.addContent(element);
        }
    },

    /**
     * w3  150, b[+3]:
     * Same as <code>w1</code>, but <code>b</code> is a three-byte-long parameter,
     * -2^23 &lt;= b &lt; 2^23.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("w3");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt24()));
            root.addContent(element);
        }
    },

    /**
     * w4  151, b[+4]:
     * Same as <code>w1</code>, but <code>b</code> is a four-byte-long parameter,
     * -2^31 &lt;= b &lt; 2^31.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("w4");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt()));
            root.addContent(element);
        }
    },

    /**
     * x0  152:
     * Set <code>h = h + x</code>; i.e., move right <code>x</code> units.
     * The <code>x</code> commands are like the <code>w</code> commands
     * except that they involve <code>x</code> instead of <code>w</code>.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("x0");
            element.setAttribute("opcode", String.valueOf(opcode));
            root.addContent(element);
        }
    },

    /**
     * x1  153, b[+1]:
     * Set <code>x = b</code> and <code>h =  h + b</code>.
     * The value of <code>b</code> is a signed quantity in two's complement
     * notation, -128 &lt;= b &lt; 128. This command changes the current
     * <code>x</code> spacing and moves right by <code>b</code>.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("x1");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readByteAsInt()));
            root.addContent(element);
        }
    },

    /**
     * x2  154, b[+2]:
     * Same as <code>x1</code>, but <code>b</code> is a two-byte-long parameter,
     * -32768 &lt;= b &lt; 32768.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("x2");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readShort()));
            root.addContent(element);
        }
    },

    /**
     * x3  155, b[+3]:
     * Same as <code>x1</code>, but <code>b</code> is a three-byte-long parameter,
     * -2^23 &lt;= b &lt; 2^23.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("x3");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt24()));
            root.addContent(element);
        }
    },

    /**
     * x4  156, b[+4]:
     * Same as <code>x1</code>, but <code>b</code> is a four-byte-long parameter,
     * -2^31 &lt;= b /lt; 2^31.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("x4");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt()));
            root.addContent(element);
        }
    },

    /**
     * down1  157, a[+1]:
     * Set <code>v = v + a</code>, i.e., move down <code>a</code> units.
     * The parameter is a signed number in two's complement notation,
     * -128 &lt;=  a &lt; 128; if a &lt; 0, the reference point actually moves up.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("down1");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readByteAsInt()));
            root.addContent(element);
        }
    },

    /**
     * down2  158, a[+2]:
     * Same as <code>down1</code>, except that <code>a</code> is a two-byte
     * quantity in the range -32768 &lt;=  a &lt; 32768.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("down2");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readShort()));
            root.addContent(element);
        }
    },

    /**
     * down3  159, a[+3]:
     * Same as <code>down1</code>, except that <code>a</code> is a three-byte
     * quantity in the range -2^23 &lt;=  a &lt; 2^23.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("down3");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt24()));
            root.addContent(element);
        }
    },

    /**
     * down4  160, a[+4]:
     * Same as <code>down1</code>, except that <code>a</code> is a four-byte
     * quantity in the range -2^31 &lt;= a &lt; 2^31.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("down4");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt()));
            root.addContent(element);
        }
    },

    /**
     * y0  161:
     * Set <code>v = v + y</code>; i.e., move down <code>y</code> units.
     * With luck, this parameterless command will usually suffice,
     * because the same kind of motion will occur several times in succession;
     * the following commands explain how <code>y</code> gets particular values.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("y0");
            element.setAttribute("opcode", String.valueOf(opcode));
            root.addContent(element);
        }
    },

    /**
     * y1  162, a[+1]:
     * Set <code>y = a</code> and <code>v = v + a</code>.
     * The value of <code>a</code> is a signed quantity in two's complement
     * notation, -128 &lt;= a &lt; 128.
     * This command changes the current <code>y</code> spacing and
     * moves down by <code>a</code>.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("y1");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readByteAsInt()));
            root.addContent(element);
        }
    },

    /**
     * y2  163, a[+2]:
     * Same as <code>y1</code>, but <code>a</code> is a two-byte-long parameter,
     * -32768 &lt;= a &lt; 32768.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("y2");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readShort()));
            root.addContent(element);
        }
    },

    /**
     * y3  164, a[+3]:
     * Same as <code>y1</code>, but <code>a</code> is a three-byte-long
     * parameter, -2^23 &lt;= a &lt; 2^23.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("y3");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt24()));
            root.addContent(element);
        }
    },

    /**
     * y4  165, a[+4]:
     * Same as <code>y1</code>, but <code>a</code> is a four-byte-long parameter,
     * -2^31 &lt;= a &lt; 2^31.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("y4");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt()));
            root.addContent(element);
        }
    },

    /**
     * z0  166:
     * Set <code>v = v + z</code>; i.e., move down <code>z</code> units.
     * The <code>z</code> commands are like the <code>y</code> commands
     * except that they involve <code>z</code> instead of <code>y</code>.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("z0");
            element.setAttribute("opcode", String.valueOf(opcode));
            root.addContent(element);
        }
    },

    /**
     * z1  167, a[+1]:
     * Set <code>z = a</code> and <code>v = v + a</code>.
     * The value of <code>a</code> is a signed quantity in two's complement
     * notation, -128 &lt;= a &lt; 128. This command changes the current
     * <code>z</code> spacing and moves down by <code>a</code>.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("z1");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readByteAsInt()));
            root.addContent(element);
        }
    },

    /**
     * z2  168, a[+2]:
     * Same as <code>z1</code>, but <code>a</code> is a two-byte-long parameter,
     * -32768 &lt;= a &lt; 32768.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("z2");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readShort()));
            root.addContent(element);
        }
    },

    /**
     * z3  169, a[+3]:
     * Same as <code>z1</code>, but <code>a</code> is a three-byte-long parameter,
     * -2^23 &lt;= a &lt; 2^23.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("z3");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt24()));
            root.addContent(element);
        }
    },

    /**
     * z4  170, a[+4]:
     * Same as <code>z1</code>, but <code>a</code> is a four-byte-long parameter,
     * -2^31 &lt;= a &lt; 2^31.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("z4");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt()));
            root.addContent(element);
        }
    },

    /**
     * fnt_num_0  171:
     * Set <code>f = 0</code>. Font 0 must previously have been defined by a
     * <code>fnt_def</code> instruction.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("fntnum0");
            element.setAttribute("opcode", String.valueOf(opcode));
            root.addContent(element);
        }
    },

    /**
     * fnt_num_1 through fnt_num_63  (opcodes 172 to 234):
     * Set <code>f = 1</code>, ..., <code>f = 63</code>, respectively.
     */
    fntnum, /* 1 */
    fntnum, /* 2 */
    fntnum, /* 3 */
    fntnum, /* 4 */
    fntnum, /* 5 */
    fntnum, /* 6 */
    fntnum, /* 7 */
    fntnum, /* 8 */
    fntnum, /* 9 */
    fntnum, /* 10 */
    fntnum, /* 11 */
    fntnum, /* 12 */
    fntnum, /* 13 */
    fntnum, /* 14 */
    fntnum, /* 15 */
    fntnum, /* 16 */
    fntnum, /* 17 */
    fntnum, /* 18 */
    fntnum, /* 19 */
    fntnum, /* 20 */
    fntnum, /* 21 */
    fntnum, /* 22 */
    fntnum, /* 23 */
    fntnum, /* 24 */
    fntnum, /* 25 */
    fntnum, /* 26 */
    fntnum, /* 27 */
    fntnum, /* 28 */
    fntnum, /* 29 */
    fntnum, /* 30 */
    fntnum, /* 31 */
    fntnum, /* 32 */
    fntnum, /* 33 */
    fntnum, /* 34 */
    fntnum, /* 35 */
    fntnum, /* 36 */
    fntnum, /* 37 */
    fntnum, /* 38 */
    fntnum, /* 39 */
    fntnum, /* 40 */
    fntnum, /* 41 */
    fntnum, /* 42 */
    fntnum, /* 43 */
    fntnum, /* 44 */
    fntnum, /* 45 */
    fntnum, /* 46 */
    fntnum, /* 47 */
    fntnum, /* 48 */
    fntnum, /* 49 */
    fntnum, /* 50 */
    fntnum, /* 51 */
    fntnum, /* 52 */
    fntnum, /* 53 */
    fntnum, /* 54 */
    fntnum, /* 55 */
    fntnum, /* 56 */
    fntnum, /* 57 */
    fntnum, /* 58 */
    fntnum, /* 59 */
    fntnum, /* 60 */
    fntnum, /* 61 */
    fntnum, /* 62 */
    fntnum, /* 63 */

    /**
     * fnt1  235, k[1]:
     * Set <code>f = k</code>. TeX82 uses this command for font numbers in the
     * range 64 &lt;=  k &lt; 256.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("fnt1");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readByteAsInt()));
            root.addContent(element);
        }
    },

    /**
     * fnt2  236, k[2]:
     * Same as <code>fnt1</code>, except that <code>k</code> is two bytes long,
     * so it is in the range 0 &lt;= k &lt; 65536. TeX82 never generates this
     * command, but large font numbers may prove useful for specifications
     * of color or texture, or they may be used for special fonts that have fixed
     * numbers in some external coding scheme.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("fnt2");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readShort()));
            root.addContent(element);
        }
    },

    /**
     * fnt3  237, k[3]:
     * Same as <code>fnt1</code>, except that <code>k</code> is three bytes long,
     * so it can be as large as 2^24-1.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("fnt3");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt24()));
            root.addContent(element);
        }
    },

    /**
     * fnt4  238, k[+4]:
     * Same as <code>fnt1</code>, except that <code>k</code> is four bytes long;
     * this is for the really big font numbers (and for the negative ones).
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("fnt4");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("value", String.valueOf(rar.readInt()));
            root.addContent(element);
        }
    },

    /**
     * xxx1  239, k[1] x[k]:
     * This command is undefined in general; it functions as a
     * <code>(k+2)</code>-byte <code>nop</code> unless special DVI-reading
     * programs are being used. TeX82 generates <code>xxx1</code>
     * when a short enough <code>special</code>
     * appears, setting <code>k</code> to the number of bytes being sent. It is
     * recommended that $x$ be a string having the form of a keyword
     * followed by possible parameters relevant to that keyword.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("xxx1");
            element.setAttribute("opcode", String.valueOf(opcode));
            int k = rar.readByteAsInt();
            element.setAttribute("k", String.valueOf(k));
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < k; i++) {
                buf.append("0x").append(rar.readByteAsInt()).append(" ");
            }
            element.setText(buf.toString());
            root.addContent(element);
        }
    },

    /**
     * xxx2  240, k[2] x[k]:
     * Like <code>xxx1</code>, but 0 &lt;= k &lt; 65536.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("xxx2");
            element.setAttribute("opcode", String.valueOf(opcode));
            int k = rar.readShort();
            element.setAttribute("k", String.valueOf(k));
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < k; i++) {
                buf.append("0x").append(rar.readByteAsInt()).append(" ");
            }
            element.setText(buf.toString());
            root.addContent(element);
        }
    },

    /**
     * xxx3  241, k[3] x[k]:
     * Like <code>xxx1</code>, but 0 &lt;= k &lt; 2^24.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("xxx3");
            element.setAttribute("opcode", String.valueOf(opcode));
            int k = rar.readInt24();
            element.setAttribute("k", String.valueOf(k));
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < k; i++) {
                buf.append("0x").append(rar.readByteAsInt()).append(" ");
            }
            element.setText(buf.toString());
            root.addContent(element);
        }
    },

    /**
     * xxx4  242, k[4] x[k]:
     * Like <code>xxx1</code>, but <code>k</code> can be ridiculously large.
     * TeX82 uses <code>xxx4</code> when <code>xxx1</code> would be incorrect.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("xxx4");
            element.setAttribute("opcode", String.valueOf(opcode));
            int k = rar.readInt();
            element.setAttribute("k", String.valueOf(k));
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < k; i++) {
                buf.append("0x").append(rar.readByteAsInt()).append(" ");
            }
            element.setText(buf.toString());
            root.addContent(element);
        }
    },

    /**
     * fnt_def1  243, k[1] c[4] s[4] d[4] a[1] l[1] n[a+l]:
     * Define font <code>k</code>, where 0 &lt;= k &lt; 256;
     * font definitions will be explained shortly.
     * <p>
     * The four-byte value <code>c</code> is the check sum.
     *
     * Parameter <code>s</code> contains a fixed-point scale factor that is
     * applied to the character widths in font <code>k</code>; font dimensions
     * in TFM files and other font files are relative to this
     * quantity, which is always positive and less than 227. It
     * is given in the same units as the other dimensions of the
     * DVI file.
     *
     * Parameter <code>d</code> is similar to <code>s</code>;
     * it is the design size, and (like <code>s</code>) it is given in DVI units.
     * Thus, font <code>k</code> is to be used at <code>mag * s / (1000 * d)</code>
     * times its normal size.
     *
     * The remaining part of a font definition gives the external
     * name of the font, which is an ASCII string of length
     * <code>a</code> + <code>l</code>. The number a is the length
     * of the area or directory,
     * and <code>l</code> is the length of the font name itself; the
     * standard local system font area is supposed to be used
     * when a = 0. The n field contains the area in its first a
     * bytes.
     * </p>
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("fntdef1");
            element.setAttribute("opcode", String.valueOf(opcode));
            int k = rar.readByteAsInt();
            int c = rar.readInt();
            int s = rar.readInt();
            int d = rar.readInt();
            int a = rar.readByteAsInt();
            int l = rar.readByteAsInt();
            StringBuffer bufa = new StringBuffer();
            StringBuffer bufl = new StringBuffer();
            for (int i = 0; i < a; i++) {
                bufa.append((char) rar.readByteAsInt());
            }
            for (int i = 0; i < l; i++) {
                bufl.append((char) rar.readByteAsInt());
            }
            element.setAttribute("font", String.valueOf(k));
            element.setAttribute("checksum", String.valueOf(c));
            element.setAttribute("scalefactor", String.valueOf(s));
            element.setAttribute("designsize", String.valueOf(d));
            element.setAttribute("scaled", String.valueOf(getScaled(s, d)));
            element.setAttribute("area", bufa.toString());
            element.setAttribute("name", bufl.toString());
            root.addContent(element);
        }
    },

    /**
     * fnt_def2  244, k[2] c[4] s[4] d[4] a[1] l[1] n[a+l]:
     * Define font <code>k</code>, where 0 &lt;= k &lt; 65536.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("fntdef2");
            element.setAttribute("opcode", String.valueOf(opcode));
            int k = rar.readShort();
            int c = rar.readInt();
            int s = rar.readInt();
            int d = rar.readInt();
            int a = rar.readByteAsInt();
            int l = rar.readByteAsInt();

            StringBuffer bufa = new StringBuffer();
            StringBuffer bufl = new StringBuffer();
            for (int i = 0; i < a; i++) {
                bufa.append((char) rar.readByteAsInt());
            }
            for (int i = 0; i < l; i++) {
                bufl.append((char) rar.readByteAsInt());
            }
            element.setAttribute("font", String.valueOf(k));
            element.setAttribute("checksum", String.valueOf(c));
            element.setAttribute("scalefactor", String.valueOf(s));
            element.setAttribute("designsize", String.valueOf(d));
            element.setAttribute("scaled", String.valueOf(getScaled(s, d)));
            element.setAttribute("area", bufa.toString());
            element.setAttribute("name", bufl.toString());
            root.addContent(element);
        }
    },

    /**
     * fnt_def3  245, k[3] c[4] s[4] d[4] a[1] l[1] n[a+l]:
     * Define font <code>k</code>, where 0 &lt;= k &lt; 2^24.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("fntdef3");
            element.setAttribute("opcode", String.valueOf(opcode));
            int k = rar.readInt24();
            int c = rar.readInt();
            int s = rar.readInt();
            int d = rar.readInt();
            int a = rar.readByteAsInt();
            int l = rar.readByteAsInt();

            StringBuffer bufa = new StringBuffer();
            StringBuffer bufl = new StringBuffer();
            for (int i = 0; i < a; i++) {
                bufa.append((char) rar.readByteAsInt());
            }
            for (int i = 0; i < l; i++) {
                bufl.append((char) rar.readByteAsInt());
            }
            element.setAttribute("font", String.valueOf(k));
            element.setAttribute("checksum", String.valueOf(c));
            element.setAttribute("scalefactor", String.valueOf(s));
            element.setAttribute("designsize", String.valueOf(d));
            element.setAttribute("scaled", String.valueOf(getScaled(s, d)));
            element.setAttribute("area", bufa.toString());
            element.setAttribute("name", bufl.toString());
            root.addContent(element);
        }
    },

    /**
     * fnt_def4  246, k[+4] c[4] s[4] d[4] a[1] l[1] n[a+l]:
     * Define font <code>k</code>, where -2^31 &lt;= k &lt; 2^31.
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("fntdef4");
            element.setAttribute("opcode", String.valueOf(opcode));
            int k = rar.readInt();
            int c = rar.readInt();
            int s = rar.readInt();
            int d = rar.readInt();
            int a = rar.readByteAsInt();
            int l = rar.readByteAsInt();

            StringBuffer bufa = new StringBuffer();
            StringBuffer bufl = new StringBuffer();
            for (int i = 0; i < a; i++) {
                bufa.append((char) rar.readByteAsInt());
            }
            for (int i = 0; i < l; i++) {
                bufl.append((char) rar.readByteAsInt());
            }
            element.setAttribute("font", String.valueOf(k));
            element.setAttribute("checksum", String.valueOf(c));
            element.setAttribute("scalefactor", String.valueOf(s));
            element.setAttribute("designsize", String.valueOf(d));
            element.setAttribute("scaled", String.valueOf(getScaled(s, d)));
            element.setAttribute("area", bufa.toString());
            element.setAttribute("name", bufl.toString());
            root.addContent(element);
        }
    },

    /**
     * pre  247, i[1] num[4] den[4] mag[4] k[1] x[k]:
     * Beginning of the preamble; this must come at the very beginning of
     * the file.
     * <p>
     * The <code>i</code> byte identifies DVI format.
     * The next two parameters, <code>num</code> and <code>den</code>,
     * are positive integers that define the units of measurement;
     * they are the numerator and denominator of a fraction by which
     * all dimensions in the DVI file could be multiplied in order to get
     * lengths in units of 10^-7 meters.
     * (For example, there are exactly 7227 TeX points in 254 centimeters,
     * and TeX82 works with scaled points where there are 2^16 sp in a point,
     * so TeX82 sets num=25400000 and den= 7227 * 2^16 =473628672.)
     * The <code>mag</code> parameter is what TeX82 calls <code>mag</code>,
     * i.e., 1000 times the desired magnification. The actual fraction by which
     * dimensions are multiplied is therefore mn/1000d. Note that if a
     * TeX source document does not call for any <code>true</code> dimensions,
     * and if you change it only by specifying a different <code>mag</code>
     * setting, the DVI file that TeX creates will be completely
     * unchanged except for the value of mag in the preamble and
     * postamble. (Fancy DVI-reading programs allow users to override
     * the <code>mag</code> setting when a DVI file is being printed.)
     * Finally, <code>k</code> and <code>x</code> allow the DVI writer
     * to include a comment, which is not interpreted further.
     * The length of comment <code>x</code> is <code>k</code>,
     * where 0 &lt;=  k &lt; 256.</p>
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("pre");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("identifies", String.valueOf(rar
                    .readByteAsInt()));
            int num = rar.readInt();
            int den = rar.readInt();
            mag = rar.readInt();
            element.setAttribute("num", String.valueOf(num));
            element.setAttribute("den", String.valueOf(den));
            element.setAttribute("mag", String.valueOf(mag));
            int k = rar.readByteAsInt();
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < k; i++) {
                buf.append((char) rar.readByteAsInt());
            }
            if (buf.length() > 0) {
                Element comment = new Element("comment");
                comment.setText(buf.toString());
                element.addContent(comment);
            }
            root.addContent(element);
        }
    },

    /**
     * post  248:
     * Beginning of the postamble.
     * <pre>
     * post p[4] num[4] den[4] mag[4] l[4] u[4] s[2] t[2]
     * </pre>
     * <p>
     * Here <code>p</code> is a pointer to the final <code>bop</code> in the file.
     * The next three parameters, <code>num</code>, <code>den</code>, and
     * <code>mag</code>, are duplicates of the quantities that appeared in
     * the preamble.
     * Parameters <code>l</code> and <code>u</code> give respectively
     * the height-plus-depth of the tallest page and the width of the widest
     * page, in the same units as other dimensions of the file. These numbers
     * might be used by a DVI-reading program to position individual 'pages'
     * on large sheets of film or paper; however, the standard convention
     * for output on normal size paper is to position each page so that the
     * upper left-hand corner is exactly one inch from the left and the top.
     * Experience has shown that it is unwise to design DVI-to-printer
     * software that attempts cleverly to center the output; a fixed
     * position of the upper left corner is easiest for users to understand
     * and to work with. Therefore <code>l</code> and <code>u</code> are
     * often ignored.
     * Parameter <code>s</code> is the maximum stack depth (i.e., the
     * largest excess of <code>push</code> commands over <code>pop</code>
     * commands) needed to process this file. Then comes <code>t</code>,
     * the total number of pages (<code>bop</code> commands) present.
     * </p>
     * <p>
     * The postamble continues with font definitions, which are any number
     * of <code>fnt_def</code> commands as described above, possibly interspersed
     * with <code>nop</code> commands. Each font number that is used in the
     * DVI file must be defined exactly twice: Once before it is first
     * selected by a <code>fnt</code> command, and once in the postamble.
     * </p>
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("post");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("p", String.valueOf(rar.readInt()));
            element.setAttribute("num", String.valueOf(rar.readInt()));
            element.setAttribute("den", String.valueOf(rar.readInt()));
            element.setAttribute("mag", String.valueOf(rar.readInt()));
            element.setAttribute("heightplusdepth", String.valueOf(rar
                    .readInt()));
            element.setAttribute("widthwidest", String.valueOf(rar.readInt()));
            element.setAttribute("stackdepth", String.valueOf(rar.readShort()));
            element.setAttribute("totalpage", String.valueOf(rar.readShort()));
            root.addContent(element);
        }
    },

    /**
     * post_post  249:
     * Ending of the postamble.
     * <p>
     * The last part of the postamble, following the <code>post_post</code> byte
     * that signifies the end of the font definitions, contains <code>q</code>, a
     * pointer to the <code>post</code> command that started the postamble.
     * An identification byte, <code>i</code>, comes next; this currently
     * equals 2, as in the preamble.
     * </p>
     * <p>
     * The <code>i</code> byte is followed by four or more bytes that are all
     * equal to the decimal number 223 (i.e., 337 in octal). TeX puts out four
     * to seven of these trailing bytes, until the total length of the file
     * is a multiple of four bytes, since this works out best on machines
     * that pack four bytes per word; but any number of 223's is allowed, as
     * long as there are at least four of them. In effect, 223 is a sort of
     * signature that is added at the very end.
     * </p>
     */
    new DviInterpreterStep() {

        /**
         * @see de.dante.extex.format.dvi.DviInterpreterStep#interpret(
         *      de.dante.util.file.random.RandomAccessR, int)
         */
        public void interpret(final RandomAccessR rar, final int opcode)
                throws IOException, DviException {

            Element element = new Element("postpost");
            element.setAttribute("opcode", String.valueOf(opcode));
            element.setAttribute("q", String.valueOf(rar.readInt()));
            element.setAttribute("identifies", String.valueOf(rar
                    .readByteAsInt()));
            // read 223 until EOF
            StringBuffer buf = new StringBuffer();
            while (rar.getPointer() < rar.length()) {
                buf.append(rar.readByteAsInt()).append(" ");
            }
            element.setText(buf.toString());
            root.addContent(element);
        }
    },

    /**
     * Commands 250--255 are undefined at the present time.
     */
    undef, /* 250 */
    undef, /* 251 */
    undef, /* 252 */
    undef, /* 253 */
    undef, /* 254 */
    undef /* 255 */

    };

    /**
     * the mag
     */
    private int mag;

    /**
     * Calculate the scaled from a font (with times 1000).
     *
     * @param s the scalefactor
     * @param d the desigsize
     * @return Return the scaled.
     */
    private int getScaled(final int s, final int d) {

        return (int) ((double) mag * s * UNIT / (UNIT * (double) d) + 0.5);
    }

    /**
     * @see de.dante.extex.format.dvi.DVIInterpreter#interpret(
     *      de.dante.util.file.random.RandomAccessR)
     */
    public void interpret(final RandomAccessR rar) throws IOException,
            DviException {

        while (rar.getPointer() < rar.length()) {
            int opcode = rar.readByteAsInt();
            opcodearray[opcode].interpret(rar, opcode);
        }
    }
}
