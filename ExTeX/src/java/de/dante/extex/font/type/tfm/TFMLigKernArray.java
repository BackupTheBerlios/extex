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

package de.dante.extex.font.type.tfm;

import java.io.IOException;
import java.io.Serializable;

import org.jdom.Element;

import de.dante.extex.font.type.PlFormat;
import de.dante.extex.font.type.PlWriter;
import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * Class for TFM lig/kern array.
 *
 * <p>
 * The array contains instructions in a simple programming language
 * that explains what to do for special letter pairs.
 * Each word is a lig_kern_command of four bytes.
 * </p>
 *
 * <table border="1">
 *   <thead>
 *     <tr><td>byte</td><td>description</td></tr>
 *   </thead>
 *   <tr><td>first  </td><td>skip_byte, indicates that this is the
 *                  final program step if the byte is 128 or more,
 *                  otherwise the next step is obtained by skipping
 *                  this number of intervening steps.</td></tr>
 *   <tr><td>second </td><td>next_char: if next_char follows the
 *                  current character, then perform the operation
 *                  and stop, otherwise continue.</td></tr>
 *   <tr><td>third  </td><td>op_byte, indicates a ligature step if
 *                  less than 128, a kern step otherwise.</td></tr>
 *   <tr><td>fourth </td><td>remainder</td></tr>
 * </table>
 *
 * <p>
 * There are eight kinds of ligature steps, having op_byte codes
 * 4a+2b+c where 0 &lt; a &lt; b+c and 0 &lt; b, c &lt; 1.
 * The character whose code is remainder is inserted between the current
 * character and next_char; then the current character is deleted
 * if b=0, and next_char is deleted if c=0; then we pass over
 * a characters to reach the next current character (which may have a
 * ligature/kerning program of its own).
 * </p>
 * <p>
 * Notice that if a=0 and b=1, the current character is unchanged;
 * if a=b and c=1, the current character is changed but the next
 * character is unchanged.
 * </p>
 * <p>
 * If the very first instruction of the lig_kern array has
 * skip_byte=255, the next_char byte is the so-called
 * right boundary character of this font; the value of next_char
 * need not lie between bc and ec. If the very last
 * instruction of the lig_kern array has skip_byte=255,
 * there is a special ligature/kerning program for a left boundary
 * character, beginning at location
 * 256op_byte+remainder. The interpretation is that
 * TeX puts implicit boundary characters before and after each
 * consecutive string of characters from the same font. These implicit
 * characters do not appear in the output, but they can affect ligatures
 * and kerning.
 * </p>
 * <p>
 * If the very first instruction of a character's lig_kern program
 * has skip_byte>128, the program actually begins in location
 * 256op_byte+remainder. This feature allows access to
 * large lig_kern arrays, because the first instruction must
 * otherwise appear in a location &lt> 255.
 * </p>
 * <p>
 * Any instruction with skip_byte>128 in the lig_kern
 * array must have 256op_byte+remainder &lt; nl.
 * If such an instruction is encountered during normal program execution,
 * it denotes an unconditional halt; no ligature command is performed.
 * </p>
 * <p>
 * Information from:
 * The DVI Driver Standard, Level 0
 * The TUG DVI Driver Standards Committee
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */

public class TFMLigKernArray implements XMLConvertible, PlFormat, Serializable {

    /**
     * the array
     */
    private TFMLigKernCommand[] ligkerncommand;

    /**
     * charinfo
     */
    private TFMCharInfoArray charinfo;

    /**
     * kern
     */
    private TFMKernArray kern;

    /**
     * Create a new object
     * @param rar   the input
     * @param nl    number of words in the lig/kern table
     * @throws IOException if an IO-error occurs.
     */
    public TFMLigKernArray(final RandomAccessR rar, final short nl)
            throws IOException {

        ligkerncommand = new TFMLigKernCommand[nl];
        for (int i = 0; i < nl; i++) {
            ligkerncommand[i] = new TFMLigKernCommand(rar, i);
        }
    }

    /**
     * smallest character code in the font
     */
    private short bc;

    /**
     * Calculate lig/kern
     * @param chari     the charinfo
     * @param akern     the kern
     * @param abc       smallest character code in the font
     */
    public void calculate(final TFMCharInfoArray chari,
            final TFMKernArray akern, final short abc) {

        charinfo = chari;
        kern = akern;
        bc = abc;

        if (ligkerncommand.length > 0) {
            setupBoundary();
        }
        buildLabels();
        promoteActivity();
        buildLigKernTable();

    }

    /**
     * Invisible right boundary character code.
     */
    private short boundaryChar = TFMCharInfoWord.NOCHARCODE;

    /**
     * Starting index of lig/kern program for invisible left boundary character
     * or <code>NOINDEX</code> if there is no such program.
     */
    private int boundaryStart = TFMCharInfoWord.NOINDEX;

    /**
     * The associative table of lig/kern program starts in <code>ligAuxTab</code>
     */
    private TFMIndexMultimap labels = new TFMIndexMultimap();

    /**
     * Code for left boundary lig/kern program in <code>labels</code> table
     */
    private static final int BOUNDARYLABEL = TFMCharInfoWord.NOCHARCODE;

    /**
     * Marks the lig/kern instructions which are really a part of some lig/kern
     * program (active), counts the final number of lig/kern instructions,
     * creates the blank final lig/kern table and checks for errors. Uses
     * <code>activity</code> field of LigKern for marking
     * the activity. It supposes that the first instructions of programs are
     * already marked active.
     * TFtoPL[70]
     */
    private void promoteActivity() {

        int ligKernLength = 0;
        for (int i = 0; i < ligkerncommand.length; i++) {
            TFMLigKernCommand lkc = ligkerncommand[i];
            if (lkc.getActivity() == TFMLigKernCommand.ACCESSIBLE) {
                if (!lkc.meansStop()) {
                    int next = lkc.nextIndex(i);
                    if (next < ligkerncommand.length) {
                        ligkerncommand[next]
                                .setActivity(TFMLigKernCommand.ACCESSIBLE);
                    } else {
                        lkc.makeStop();
                    }
                }
            }
            if (lkc.getActivity() != TFMLigKernCommand.PASSTHROUGH) {
                ligKernLength++;
            }
        }
        ligKernTable = new TFMLigKern[ligKernLength];
    }

    /**
     * Lig/kern programs in the final format
     */
    private TFMLigKern[] ligKernTable;

    /**
     * Fills in the blank <code>ligKernTable</code> by the final version of
     * lig/kern instructions.
     */
    private void buildLigKernTable() {

        int currIns = 0;
        for (int i = 0; i < ligkerncommand.length; i++) {
            setLigStarts(i, currIns);
            TFMLigKernCommand lkc = ligkerncommand[i];
            if (lkc.getActivity() != TFMLigKernCommand.PASSTHROUGH) {
                if (!lkc.meansRestart()) {
                    int skip = getSkip(i);
                    ligKernTable[currIns++] = (lkc.meansKern()) ? makeKern(lkc,
                            skip) : makeLig(lkc, skip);
                }
            }
        }
    }

    /**
     * Creates a final version of ligature instruction after validity checks.
     *
     * @param lkc   the original version of lig/kern instruction.
     * @param skip  the offset of next lig/kern instruction in the final version
     *              of the lig/kern program.
     * @return Returns the ligkern
     */
    private TFMLigKern makeLig(final TFMLigKernCommand lkc, final int skip) {

        //       if (!charExists(lkc.ligChar())) {
        //           badchar(lkc.ligChar(), "Ligature step produces the");
        //           lkc.setLigChar(firstCharCode);
        //       }
        boolean left = lkc.leaveLeft();
        boolean right = lkc.leaveRight();
        byte step = lkc.stepOver();
        return new TFMLigature(skip, lkc.nextChar(), lkc.ligChar(), left,
                right, step);
    }

    /**
     * Creates a final version of kerning instruction after validity checks.
     *
     * @param lkc   the original version of lig/kern instruction.
     * @param skip  the offset of next lig/kern instruction in the final version
     *              of the lig/kern program.
     * @return Returns the ligkern
     */
    private TFMLigKern makeKern(final TFMLigKernCommand lkc, final int skip) {

        int kernIdx = lkc.kernIndex();
        TFMFixWord kernword = null;
        if (kernIdx < kern.getTable().length) {
            kernword = kern.getTable()[kernIdx];
        } else {
            kernword = TFMFixWord.ZERO;
        }
        return new TFMKerning(skip, lkc.nextChar(), kernword);
    }

    /**
     * Gets the offset of next lig/kern instruction in a program based on
     * counting only those intervene instructions which will be converted to
     * final lig/kern program.
     *
     * @param pos   the position of current lig/kern instruction
     *              in ligAuxTable.
     * @return Returns the skip amount of the next instruction
     *         in the final version of lig/kern program.
     */
    private int getSkip(final int pos) {

        TFMLigKernCommand lkc = ligkerncommand[pos];
        if (lkc.meansStop()) {
            return -1;
        }
        int p = pos;
        int skip = 0;
        int next = lkc.nextIndex(pos);
        while (++p < next) {
            if (ligkerncommand[pos].getActivity() != TFMLigKernCommand.PASSTHROUGH) {
                skip++;
            }
        }
        return skip;
    }

    /**
     * Records the starting indexes of final lig/kern program in ligKernTable
     * to auxiliary character information field <code>ligkernstart</code>
     * of <code>AuxCharInfo</code>.
     *
     * @param pos   the position of currently processed instruction in original
     *              tfm lig/kern table <code>ligAuxTab</code>.
     * @param start the position of corresponding instruction in final lig/kern
     *              table LigKernTable.
     */
    private void setLigStarts(final int pos, final int start) {

        TFMIndexMultimap.Enum lab = labels.forKey(pos);
        while (lab.hasMore()) {
            int c = lab.next();
            if (c == BOUNDARYLABEL) {
                boundaryStart = start;
            } else {
                charinfo.getCharinfoword()[c].setLigkernstart(start);
            }
        }
    }

    /**
     * Tries to find the information about lig/kerns
     * for word boundaries in tfm lig/kern table
     * and checks for errors. TFtoPL[69]
     */
    private void setupBoundary() {

        // first entry
        TFMLigKernCommand lkc = ligkerncommand[0];

        if (lkc.meansBoundary()) {
            boundaryChar = lkc.nextChar();
            lkc.setActivity(TFMLigKernCommand.PASSTHROUGH);
        }

        // last entry
        lkc = ligkerncommand[ligkerncommand.length - 1];
        if (lkc.meansBoundary()) {
            int start = lkc.restartIndex();
            lkc.setActivity(TFMLigKernCommand.PASSTHROUGH);
            if (start < ligkerncommand.length) {
                ligkerncommand[start].setActivity(TFMLigKernCommand.ACCESSIBLE);
                labels.add(start, BOUNDARYLABEL);
            }
        }
    }

    /**
     * Builds associative table labels which maps the character
     * codes to lig/kern program starting indexes in ligAuxTab
     * for remapping later.
     * It also marks the starting instructions of lig/kern
     * programs as active
     * (using the <code>activity</code> field of LigKern).
     * TFtoPL[67]
     */
    private void buildLabels() {

        TFMCharInfoWord[] ciw = charinfo.getCharinfoword();

        for (int i = 0; i < ciw.length; i++) {
            if (ciw[i].getTag() == TFMCharInfoWord.LIG_TAG) {
                int start = ligStart(ciw[i].getRemainder());
                if (start < ligkerncommand.length) {
                    labels.add(start, i);
                    ligkerncommand[start]
                            .setActivity(TFMLigKernCommand.ACCESSIBLE);
                } else {
                    ciw[i].resetTag();
                }
            }
        }
    }

    /**
     * Finds out the actual starting index of lig/kern program in case there is
     * a restart instructions and checks for validity.
     * TFtoPL[67]
     *
     * @param start     the starting index of lig/kern program
     *                  given in a character info.
     * @return Returns the actual starting index.
     */
    private int ligStart(final int start) {

        int newstart = start;
        if (newstart < ligkerncommand.length) {
            TFMLigKernCommand lkc = ligkerncommand[start];
            if (lkc.meansRestart()) {
                newstart = lkc.restartIndex();
                if (newstart < ligkerncommand.length
                        && lkc.getActivity() == TFMLigKernCommand.UNREACHABLE) {
                    lkc.setActivity(TFMLigKernCommand.PASSTHROUGH);
                }
            }
        }
        return newstart;
    }

    /**
     * Returns the ligkerncommand.
     * @return Returns the ligkerncommand.
     */
    public TFMLigKernCommand[] getLigkerncommand() {

        return ligkerncommand;
    }

    /**
     * Returns the boundaryChar.
     * @return Returns the boundaryChar.
     */
    public short getBoundaryChar() {

        return boundaryChar;
    }

    /**
     * Returns the boundaryStart.
     * @return Returns the boundaryStart.
     */
    public int getBoundaryStart() {

        return boundaryStart;
    }

    /**
     * Returns the ligKernTable.
     * @return Returns the ligKernTable.
     */
    public TFMLigKern[] getLigKernTable() {

        return ligKernTable;
    }

    /**
     * @see de.dante.extex.font.type.PlFormat#toPL(
     *      de.dante.extex.font.type.PlWriter)
     */
    public void toPL(final PlWriter out) throws IOException {

        if (boundaryChar != TFMCharInfoWord.NOCHARCODE) {
            out.plopen("BOUNDARYCHAR").addChar(boundaryChar).plclose();
        }
        if (ligKernTable.length > 0) {
            out.plopen("LIGTABLE");
            for (int i = 0; i < charinfo.getCharinfoword().length; i++) {
                TFMCharInfoWord ciw = charinfo.getCharinfoword()[i];
                if (ciw != null) {
                    if (foundLigKern(ciw)) {
                        out.plopen("LABEL").addChar((short) (i + bc));
                        out.plclose();

                        // ligature
                        int ligstart = ciw.getLigkernstart();
                        if (ligstart != TFMCharInfoWord.NOINDEX
                                && ligKernTable != null) {

                            for (int k = ligstart; k != TFMCharInfoWord.NOINDEX; k = ligKernTable[k]
                                    .nextIndex(k)) {
                                TFMLigKern lk = ligKernTable[k];

                                if (lk instanceof TFMLigature) {
                                    TFMLigature lig = (TFMLigature) lk;

                                    out.plopen("LIG")
                                            .addChar(lig.getNextChar())
                                            .addChar(lig.getAddingChar())
                                            .plclose();
                                } else if (lk instanceof TFMKerning) {
                                    TFMKerning kerning = (TFMKerning) lk;

                                    out.plopen("KRN").addChar(
                                            kerning.getNextChar()).addReal(
                                            kerning.getKern()).plclose();
                                }
                            }
                        }
                        out.plopen("STOP").plclose();
                    }
                }
            }
            out.plclose();
        }
    }

    /**
     * Check, if char has ligature or kern
     * @param ciw   the char
     * @return Returns true, if the char has a ligature or a kern
     */
    private boolean foundLigKern(final TFMCharInfoWord ciw) {

        boolean found = false;
        int ligstart = ciw.getLigkernstart();
        if (ligstart != TFMCharInfoWord.NOINDEX && ligKernTable != null) {

            for (int k = ligstart; k != TFMCharInfoWord.NOINDEX; k = ligKernTable[k]
                    .nextIndex(k)) {
                TFMLigKern lk = ligKernTable[k];

                if (lk instanceof TFMLigature || lk instanceof TFMKerning) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element element = new Element("ligkern");
        for (int i = 0; i < ligkerncommand.length; i++) {
            element.addContent(ligkerncommand[i].toXML());
        }
        return element;
    }

}
