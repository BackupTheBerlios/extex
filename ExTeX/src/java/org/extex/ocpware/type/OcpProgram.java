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

package org.extex.ocpware.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class OcpProgram {

    /**
     * Dump a number as hex and as decimal and optionally as character to an
     * output stream.
     *
     * @param out the output stream
     * @param pre the prefix string
     * @param value the value to print
     * @param post the postfix string
     */
    private static void dump(final PrintStream out, final String pre,
            final int value, final String post) {

        out.print(pre);
        out.print(Integer.toHexString(value));
        out.print(" (");
        out.print(value);
        if (value >= ' ' && value <= 0x7d) {
            out.print(",`");
            out.print((char) value);
            out.print("'");
        }
        out.print(")");
        out.print(post);
    }

    /**
     * Load an OCP program from an input stream.
     *
     * @param in the input stream
     *
     * @return the program
     */
    public static OcpProgram load(final InputStream stream) throws IOException {

        OcpProgram ocp = new OcpProgram();

        ocp.setLength(readWord(stream));
        ocp.setInput(readWord(stream));
        ocp.setOutput(readWord(stream));

        int tables = readWord(stream);
        int tableSpace = readWord(stream);
        int states = readWord(stream);
        int stateSpace = readWord(stream);

        int[] t = read(stream, tables);
        for (int i = 0; i < t.length; i++) {
            ocp.addTable(read(stream, t[i]));
        }

        t = read(stream, states);
        for (int i = 0; i < t.length; i++) {
            ocp.addState(read(stream, t[i]));
        }

        return ocp;
    }

    /**
     * Read an array of words.
     *
     * @param in the input stream
     * @param len the number of words to read
     *
     * @return the array read
     *
     * @throws IOException in case of an error
     */
    private static int[] read(final InputStream in, final int len)
            throws IOException {

        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = readWord(in);
        }
        return a;
    }

    /**
     * Read a single word of 4 bytes (octets).
     *
     * @param in the input stream
     *
     * @return the word read
     *
     * @throws IOException in case of an error
     */
    private static int readWord(final InputStream in) throws IOException {

        int a = in.read();
        if (a < 0) {
            throw new IOException("unexpected EOF");
        }
        int b = in.read();
        if (b < 0) {
            throw new IOException("unexpected EOF");
        }
        int c = in.read();
        if (c < 0) {
            throw new IOException("unexpected EOF");
        }
        int d = in.read();
        if (d < 0) {
            throw new IOException("unexpected EOF");
        }

        return (a << 24) | (b << 16) | (c << 8) | d;
    }

    /**
     * The field <tt>input</tt> contains the input parameter.
     */
    private int input = 0;

    /**
     * The field <tt>length</tt> contains the length for dumping. A negative
     * value is used to indicate an undefined state.
     */
    private int length = -1;

    /**
     * The field <tt>output</tt> contains the output parameter.
     */
    private int output = 0;

    /**
     * The field <tt>states</tt> contains the states.
     */
    private List states = new ArrayList();

    /**
     * The field <tt>tables</tt> contains the tables.
     */
    private List tables = new ArrayList();

    /**
     * Creates a new object.
     *
     */
    public OcpProgram() {

        super();
    }

    /**
     * Add a state.
     *
     * @param t the state to add
     */
    public void addState(final int[] t) {

        states.add(t);
    }

    /**
     * Add a table.
     *
     * @param t the table to add
     */
    public void addTable(final int[] t) {

        tables.add(t);
    }

    /**
     * Dump the contents of the instance to an output stream.
     *
     * @param out the output stream
     */
    public void dump(final PrintStream out, boolean orig) {

        if (length >= 0) {
            dump(out, "ctp_length     : ", length, "\n");
        }
        dump(out, "ctp_input      : ", input, "\n");
        dump(out, "ctp_output     : ", output, "\n");
        dump(out, "ctp_no_tables  : ", tables.size(), "\n");
        int mem = 0;
        for (int i = 0; i < tables.size(); i++) {
            mem += ((int[]) tables.get(i)).length;
        }
        dump(out, "ctp_room_tables: ", mem, "\n");
        dump(out, "ctp_no_states  : ", states.size(), "\n");
        mem = 0;
        for (int i = 0; i < states.size(); i++) {
            mem += ((int[]) states.get(i)).length;
        }
        dump(out, "ctp_room_states: ", mem, "\n");

        for (int i = 0; i < states.size(); i++) {
            int[] t = ((int[]) states.get(i));
            dump(out, "\nState ", i, "");
            dump(out, ": ", t.length, " entries\n\n");

            boolean dis = true;

            for (int j = 0; j < t.length; j++) {
                if (orig) {
                    dump(out, "State ", i, ", ");
                    dump(out, "entry ", j, ": ");
                } else {
                    String hex = Integer.toHexString(j);
                    out.print("           ".substring(hex.length()));
                    out.print(hex);
                    out.print("\t");
                }

                if (dis) {
                    if (orig) {
                        out.print("OTP_");
                    }
                    dis = dumpDis(out, t, j);
                } else {
                    if (orig) {
                        out.print("    ");
                    }
                    out.print("                ");
                    dump(out, "", t[j], "");
                    dis = true;
                }
                out.println();
            }
        }
    }

    /**
     * Dump a dis-assembled form of an instruction.
     *
     * @param out the output stream
     * @param t the array
     * @param j the index
     *
     * @return <code>true</code> iff another word as argument is required
     */
    private boolean dumpDis(final PrintStream out, final int[] t, final int j) {

        boolean two = true;
        int c = t[j];
        switch (c >> 24) {
            case 1:
                out.print("RIGHT_OUTPUT    ");
                break;
            case 2:
                out.print("RIGHT_NUM       ");
                break;
            case 3:
                out.print("RIGHT_CHAR      ");
                break;
            case 4:
                out.print("RIGHT_LCHAR     ");
                break;
            case 5:
                out.print("RIGHT_SOME      ");
                two = false;
                break;
            case 6:
                out.print("PBACK_OUTPUT    ");
                break;
            case 7:
                out.print("PBACK_NUM       ");
                break;
            case 8:
                out.print("PBACK_CHAR      ");
                break;
            case 9:
                out.print("PBACK_LCHAR     ");
                break;
            case 10:
                out.print("PBACK_SOME      ");
                two = false;
                break;
            case 11:
                out.print("ADD             ");
                break;
            case 12:
                out.print("SUB             ");
                break;
            case 13:
                out.print("MULT            ");
                break;
            case 14:
                out.print("DIV             ");
                break;
            case 15:
                out.print("MOD             ");
                break;
            case 16:
                out.print("LOOKUP          ");
                break;
            case 17:
                out.print("PUSH_NUM        ");
                break;
            case 18:
                out.print("PUSH_CHAR       ");
                break;
            case 19:
                out.print("PUSH_LCHAR      ");
                break;
            case 20:
                out.print("STATE_CHANGE    ");
                break;
            case 21:
                out.print("STATE_PUSH      ");
                break;
            case 22:
                out.print("STATE_POP       ");
                break;
            case 23:
                out.print("LEFT_START      ");
                break;
            case 24:
                out.print("LEFT_RETURN     ");
                break;
            case 25:
                out.print("LEFT_BACKUP     ");
                break;
            case 26:
                out.print("GOTO            ");
                break;
            case 27:
                out.print("GOTO_NE         ");
                two = false;
                break;
            case 28:
                out.print("GOTO_EQ         ");
                two = false;
                break;
            case 29:
                out.print("GOTO_LT         ");
                two = false;
                break;
            case 30:
                out.print("GOTO_LE         ");
                two = false;
                break;
            case 31:
                out.print("GOTO_GT         ");
                two = false;
                break;
            case 32:
                out.print("GOTO_GE         ");
                two = false;
                break;
            case 33:
                out.print("GOTO_NO_ADVANCE ");
                break;
            case 34:
                out.print("GOTO_BEG        ");
                break;
            case 35:
                out.print("GOTO_END        ");
                break;
            case 36:
                out.print("STOP            ");
                break;
            default:
                dump(out, "                ", c, "");
                return true;
        }
        dump(out, "", c & 0xffff, "");
        return two;
    }

    /**
     * Getter for input.
     *
     * @return the input
     */
    public int getInput() {

        return this.input;
    }

    /**
     * Getter for output.
     *
     * @return the output
     */
    public int getOutput() {

        return this.output;
    }

    /**
     * Setter for input.
     *
     * @param input the input to set
     */
    public void setInput(final int input) {

        this.input = input;
    }

    /**
     * Setter for the length.
     *
     * @param len the length
     */
    private void setLength(int len) {

        this.length = len;
    }

    /**
     * Setter for output.
     *
     * @param output the output to set
     */
    public void setOutput(final int output) {

        this.output = output;
    }

    /**
     * Reset the internal state of the program to the initial values.
     */
    public void reset() {

        pc = 0;
        state = 0;
        stateStack.clear();
        arithStack.clear();
    }

    /**
     * TODO gene: missing JavaDoc
     *
     */
    public void run() {

        step(null, null, (int[]) states.get(state));
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param currentState
     */
    private void step(final PushbackInputStream in, final OutputStream out,
            final int[] currentState) {

        int a, b, c;

        for (;;) {
            c = currentState[pc++];

            switch (c >> 24) {
                case 1:
                    // OTP_RIGHT_OUTPUT
                    break;
                case 2:
                    // OTP_RIGHT_NUM
                    break;
                case 3:
                    // OTP_RIGHT_CHAR
                    break;
                case 4:
                    // OTP_RIGHT_LCHAR
                    break;
                case 5:
                    // OTP_RIGHT_SOME
                    //two = false;
                    break;
                case 6:
                    // OTP_PBACK_OUTPUT
                    break;
                case 7:
                    // OTP_PBACK_NUM
                    break;
                case 8:
                    // OTP_PBACK_CHAR
                    break;
                case 9:
                    // OTP_PBACK_LCHAR
                    break;
                case 10:
                    // OTP_PBACK_SOME
                    a = currentState[pc++];
                    break;
                case 11:
                    // OTP_ADD
                    a = ((Integer) arithStack.pop()).intValue();
                    b = ((Integer) arithStack.pop()).intValue();
                    arithStack.push(new Integer(b + a));
                    break;
                case 12:
                    // OTP_SUB
                    a = ((Integer) arithStack.pop()).intValue();
                    b = ((Integer) arithStack.pop()).intValue();
                    arithStack.push(new Integer(b - a));
                    break;
                case 13:
                    // OTP_MULT
                    a = ((Integer) arithStack.pop()).intValue();
                    b = ((Integer) arithStack.pop()).intValue();
                    arithStack.push(new Integer(b * a));
                    break;
                case 14:
                    // OTP_DIV
                    a = ((Integer) arithStack.pop()).intValue();
                    b = ((Integer) arithStack.pop()).intValue();
                    arithStack.push(new Integer(b / a));
                    break;
                case 15:
                    // OTP_MOD
                    a = ((Integer) arithStack.pop()).intValue();
                    b = ((Integer) arithStack.pop()).intValue();
                    arithStack.push(new Integer(b % a));
                    break;
                case 16:
                    // OTP_LOOKUP
                    break;
                case 17:
                    // OTP_PUSH_NUM
                    break;
                case 18:
                    // OTP_PUSH_CHAR
                    break;
                case 19:
                    // OTP_PUSH_LCHAR
                    break;
                case 20:
                    // OTP_STATE_CHANGE
                    state = c & 0xfff;
                    break;
                case 21:
                    // OTP_STATE_PUSH
                    stateStack.push(new Integer(state));
                    state = c & 0xfff;
                    break;
                case 22:
                    // OTP_STATE_POP
                    break;
                case 23:
                    // OTP_LEFT_START
                    first = last + 1;
                    break;
                case 24:
                    // OTP_LEFT_RETURN
                    last = first + 1;
                    break;
                case 25:
                    // OTP_LEFT_BACKUP
                    last--; // ???
                    break;
                case 26:
                    // OTP_GOTO
                    pc = c & 0xfff;
                    break;
                case 27:
                    // OTP_GOTO_NE
                    a = currentState[pc++];
                    if ((a & 0xffff) != (a >> 16)) {
                        pc = c & 0xfff;
                    }
                    break;
                case 28:
                    // OTP_GOTO_EQ
                    a = currentState[pc++];
                    if ((a & 0xffff) == (a >> 16)) {
                        pc = c & 0xfff;
                    }
                    break;
                case 29:
                    // OTP_GOTO_LT
                    a = currentState[pc++];
                    if ((a & 0xffff) < (a >> 16)) {
                        pc = c & 0xfff;
                    }
                    break;
                case 30:
                    // OTP_GOTO_LE
                    a = currentState[pc++];
                    if ((a & 0xffff) <= (a >> 16)) {
                        pc = c & 0xfff;
                    }
                    break;
                case 31:
                    // OTP_GOTO_GT
                    a = currentState[pc++];
                    if ((a & 0xffff) > (a >> 16)) {
                        pc = c & 0xfff;
                    }
                    break;
                case 32:
                    // OTP_GOTO_GE
                    a = currentState[pc++];
                    if ((a & 0xffff) >= (a >> 16)) {
                        pc = c & 0xfff;
                    }
                    break;
                case 33:
                    // OTP_GOTO_NO_ADVANCE
                    break;
                case 34:
                    // OTP_GOTO_BEG
                    if (false) { //TODO at beginning
                        pc = c & 0xfff;
                    }
                    break;
                case 35:
                    // OTP_GOTO_END
                    if (false) { //TODO at end
                        pc = c & 0xfff;
                    }
                    break;
                case 36:
                    // OTP_STOP
                    return;
                default:
                    //TODO gene: unimplemented
                    throw new RuntimeException("unimplemented");
            }
        }
    }

    /**
     * The field <tt>pc</tt> contains the ...
     */
    private int pc = 0;

    private int first = 0;

    private int last = 0;

    /**
     * The field <tt>state</tt> contains the ...
     */
    private int state = 0;

    /**
     * The field <tt>stateStack</tt> contains the ...
     */
    private Stack stateStack = new Stack();

    /**
     * The field <tt>arithStack</tt> contains the ...
     */
    private Stack arithStack = new Stack();

}
