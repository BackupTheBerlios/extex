/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

// created: 2004-07-29

// TODO: dvitype types out other sizes, as this class think (TE)
// TODO: 10^-7 (TE)
package de.dante.extex.documentWriter.dvi;

import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.font.Glyph;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.node.CharNode;
import de.dante.extex.interpreter.type.node.GlueNode;
import de.dante.extex.interpreter.type.node.RuleNode;
import de.dante.extex.interpreter.type.node.WhatsItNode;
import de.dante.extex.typesetter.Mode;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import java.io.OutputStream;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;


/**
 * This is a implementation of a dvi document writer.
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.7 $
 */
public class DviWriter {

    /**
     * Typeset character and move right.
     *
     * @see "TeX -- The Program [586]"
     */
    private static final int[] DVI_SET_CHAR_NUM
        = {0,   1,   2,   3,   4,   5,   6,   7,
           8,   9,  10,  11,  12,  13,  14,  15,
           16,  17,  18,  19,  20,  21,  22,  23,
           24,  25,  26,  27,  28,  29,  30,  31,
           32,  33,  34,  35,  36,  37,  38,  39,
           40,  41,  42,  43,  44,  45,  46,  47,
           48,  49,  50,  51,  52,  53,  54,  55,
           56,  57,  58,  59,  60,  61,  62,  63,
           64,  65,  66,  67,  68,  69,  70,  71,
           72,  73,  74,  75,  76,  77,  78,  79,
           80,  81,  82,  83,  84,  85,  86,  87,
           88,  89,  90,  91,  92,  93,  94,  95,
           96,  97,  98,  99, 100, 101, 102, 103,
           104, 105, 106, 107, 108, 109, 110, 111,
           112, 113, 114, 115, 116, 117, 118, 119,
           120, 121, 122, 123, 124, 125, 126, 127};


    /**
     * Typeset character and move right.  Codes for character number
     * greater 128.
     *
     */
    private static final int[] DVI_SET_CHAR =  {128, 129, 130, 131};


    /**
     * dvi-code for "typeset a rule and move right".
     *
     */
    /* TODO: not used, yet (TE)
    private static final int DVI_SET_RULE = 132;
    */


    /**
     * dvi-code for "typeset a character".
     *
     */
    /* TODO: not used, yet (TE)
    private static final int[] DVI_PUT = {133, 134, 135, 136};
    */


    /**
     * dvi-code for "typeset a rule".
     *
     */
    private static final int DVI_PUT_RULE = 137;


    /**
     * dvi-code for "no operation".
     *
     */
    /* TODO: not used, yet (TE)
    private static final int DVI_NOP = 138;
    */


    /**
     * dvi-code for "begining of page".
     *
     */
    private static final int DVI_BOP = 139;


    /**
     * dvi-code for "ending of page".
     *
     */
    private static final int DVI_EOP = 140;


    /**
     * dvi-code for "save the current positions".
     *
     */
    private static final int DVI_PUSH = 141;


    /**
     * dvi-code for "resotre previous positions".
     *
     */
    private static final int DVI_POP = 142;


    /**
     * dvi-codes for "move right".
     *
     */
    private static final int[] DVI_RIGHT = {143, 144, 145, 146};


    /**
     * dvi-code for "move right by w".
     *
     */
    /* TODO: not used, yet (TE)
    private static final int DVI_W0 = 147;
    */


    /**
     * dvi-codes for "move right, update w".
     *
     */
    /* TODO: not used, yet (TE)
    private static final int[] DVI_W = {148, 149, 150, 151};
    */


    /**
     * dvi-code for "move right by x".
     *
     */
    /* TODO: not used, yet (TE)
    private static final int DVI_X0 = 152;
    */


    /**
     * dvi-codes for "move right, update x".
     *
     */
    /* TODO: not used, yet (TE)
    private static final int[] DVI_X = {153, 154, 155, 156};
    */


    /**
     * dvi-codes for "move down".
     *
     */
    private static final int[] DVI_DOWN = {157, 158, 159, 160};


    /**
     * dvi-code for "move down by y".
     *
     */
    /* TODO: not used, yet (TE)
    private static final int DVI_Y0 = 161;
    */


    /**
     * dvi-codes for "move down, update y".
     *
     */
    /* TODO: not used, yet (TE)
    private static final int[] DVI_Y = {162, 163, 164, 165};
    */


    /**
     * dvi-code for "move down by z".
     *
     */
    /* TODO: not used, yet (TE)
    private static final int DVI_Z0 = 166;
    */


    /**
     * dvi-codes for "move down, udpate z".
     *
     */
    /* TODO: not used, yet (TE)
    private static final int[] DVI_Z = {167, 168, 169, 170};
    */


    /**
     * dvi-codes for "set current font".  Codes for font numbers less
     * 64.
     *
     */
    private static final int[] DVI_FNT_NUM
        = {171, 172, 173, 174, 175, 176, 177, 178,
           179, 180, 181, 182, 183, 184, 185, 186,
           187, 188, 189, 190, 191, 192, 193, 194,
           195, 196, 197, 198, 199, 200, 201, 202,
           203, 204, 205, 206, 207, 208, 209, 210,
           211, 212, 213, 214, 215, 216, 217, 218,
           219, 220, 221, 222, 223, 224, 225, 226,
           227, 228, 229, 230, 231, 232, 233, 234};


    /**
     * dvi-codes for "set current font".  Codes for font numbers
     * greater 63.
     *
     */
    private static final int[] DVI_FNT = {235, 236, 237, 238};


    /**
     * dvi-codes for "extension to DVI primitives".
     *
     */
    /* TODO: not used, yet (TE)
    private static final int[] DVI_XXX = {239, 240, 241, 242};
    */


    /**
     * dvi-codes for "define font".
     *
     */
    private static final int[] DVI_FNT_DEF = {243, 244, 245, 246};


    /**
     * dvi-code for "preamble".
     *
     */
    private static final int DVI_PRE = 247;

    /**
     * dvi-code for "postamble beginning".
     *
     */
    private static final int DVI_POST = 248;


    /**
     * dvi-code for "postamble ending".
     *
     */
    private static final int DVI_POST_POST = 249;


    /**
     * The dvi-file Version.Describe constant <code>DVI_VERSION</code> here.
     * @see "TeX -- The Program [587]"
     *
     */
    private static final int DVI_VERSION = 2;


    /**
     * Code of the trailing bytes in the dvi-file.
     * @see "TeX -- The Program [590]"
     *
     */
    private static final int DVI_TRAILING_BYTE = 223;


    /**
     * The size of the dvi-file is a mupltiply of this value.
     *
     */
    private static final int DVI_ALIGN_SIZE = 4;


    /**
     * The constant <tt>MINUTES_PER_HOUR</tt> contains the number of minutes
     * per hour.
     */
    private static final int MINUTES_PER_HOUR = 60;


    /**
     * Minimum written trailing bytes at the end of dvi-files.
     *
     */
    private static final int MINIMUM_TRAILING_BYTES = 4;


    /**
     * Dvi file base units per metre.
     *
     * @see #SP_NUMERATOR
     * @see "TeX -- The Program [587]"
     *
     */
    private static final int DVI_BASE_SIZE_PER_METRE = 100000;


    /**
     * Scale points in a point (pt).
     *
     * @see #SP_PER_POINT
     * @see "TeX -- The Program [587]"
     *
     */
    private static final int SP_PER_POINT = 65536;


    /**
     * Numberator for converting points to centimetre.
     *
     * @see #SP_NUMERATOR
     * @see "TeX -- The Program [587]"
     *
     */
    private static final int NUMERATOR_PT_TO_CM = 254;

    /**
     * Denominator for converting points to centimetre.
     *
     * @see #SP_DENOMINATOR
     * @see "TeX -- The Program [587]"
     *
     */
    private static final int DENOMINATOR_PT_TO_CM = 7227;


    /**
     * Describes the unit of messurement (sp).
     * 1sp=<code>SP_NUMERATOR</code>/{@link #SP_DENOMINATOR
     * SP_DENOMINATOR}*10^-7&nbsp;m.
     *
     */
    private static final int SP_NUMERATOR
        = NUMERATOR_PT_TO_CM * DVI_BASE_SIZE_PER_METRE;


    /**
     * Describes the unit of messurement (sp).  1sp={@link
     * #SP_NUMERATOR SP_NUMERATOR}/<code>SP_DENOMINATOR</code>*10^-7&nbsp;m.
     *
     * @see "TeX -- The Program [586]"
     */
    private static final int SP_DENOMINATOR
        = DENOMINATOR_PT_TO_CM * SP_PER_POINT;


    /**
     * Number of bytes in a quadruple.
     *
     */
    private static final int BYTES_PER_QUADRUPLE = 4;


    /**
     * Default magnification.  Used if the class could not get the
     * magnification of the document.
     *
     */
    private static final int MAGNIFICATION_DEFAULT = 1000;


    /**
     * Number of count-register in ExTeX.
     *
     */
    private static final int NUMBER_OF_COUNTERS = 10;


    /**
     * Any output is delegated to this instance.
     *
     */
    private DviOutputStream dviOutputStream = null;


    /**
     * Number of written pages.
     *
     */
    private int pages = 0;


    /**
     * The position where the last page began.
     *
     */
    private int lastBop = -1;


    /**
     * Last seen Glyph.
     *
     */
    private Glyph lastGlyph = null;


    /**
     * Fonts defined in the dvi-file.
     *
     */
    private Vector definedFonts = new Vector();


    /**
     * Options for the documentWriter.
     *
     */
    private DocumentWriterOptions documentWriterOptions;


    /**
     * Magnification of the document.
     *
     */
    private int magnification = MAGNIFICATION_DEFAULT;


    /**
     * Maximum page height.
     *
     */
    private int maximumPageHeight = 0;


    /**
     * Maximum page width.
     *
     */
    private int maximumPageWidth = 0;


    /**
     * Maximum reached stack depth.
     *
     */
    private int maximumStackDepth = 0;


    /**
     * Saves variables for dvi-file (h, v, v, w, x, y, z).
     *
     */
    private DviPositions currentPositions = new DviPositions(0, 0, 0, 0, 0, 0);


    /**
     * Space to save currentPosition.
     *
     */
    private Stack savedPositions = new Stack();


    /**
     * Variable for remembering errors.  If an error occurs this
     * variable is set to the error.  Until the error this variable
     * has the value null.
     *
     */
    private GeneralException error = null;


    /**
     * Creates a new <code>DviWriter</code> instance.
     *
     * @param outputStream the dvi file is written to this stream
     * @param options options for the dvi-file
     */
    public DviWriter(final OutputStream outputStream,
                     final DocumentWriterOptions options) {

        this.dviOutputStream = new DviOutputStream(outputStream);
        this.documentWriterOptions = options;

        try {
            magnification
                = convertToInt(documentWriterOptions.getMagnification());
        } catch (GeneralException e) {
            error = e;
        }
    }


    /**
     * Convert an <code>long</code> value to <code>int</code>.
     *
     * @param number the <code>long</code> value
     * @return the <code>int</code> value
     * @exception GeneralException iff the <code>long</code> value is
     * not in the range of an <code>int</code> value.
     */
    private int convertToInt(final long number) throws GeneralException {
        if ((number < Integer.MIN_VALUE)
                || (number > Integer.MAX_VALUE)) {
            throw new GeneralException("long-Value not in range of Integer");
        }
        return new Long(number).intValue();
    }


    /**
     * Get the number of written pages until now.
     *
     * @return the number of written pages
     */
    public int getPages() {
        return pages;
    }


    /**
     * Append number to buffer.  If number is less 10 a 0 is inserted before.
     *
     * @param buffer <code>StringBuffer</code> to append
     * @param number the value
     */
    private void appendZeroFilledToBuffer(final StringBuffer buffer,
                                          final long number) {
        if (number < 10) {
            buffer.append("0");
        }
        buffer.append(number);
    }


    /**
     * Before any output the method <code>beginDviFile</code> have to
     * be called.
     *
     * @exception GeneralException if an error occurs
     */
    public void beginDviFile() throws GeneralException {
        /* TODO: is this correct? Should the dviwrite get the year
           of the count registers? (TE) */
        StringBuffer comment = new StringBuffer();
        long time = documentWriterOptions.getCountOption("time").getValue();
        long hour = time / MINUTES_PER_HOUR;
        long minute = time % MINUTES_PER_HOUR;

        // create dvi-comment
        comment.append(" ExTeX output ");
        comment.append(documentWriterOptions.getCountOption("year"));
        appendZeroFilledToBuffer(comment,
            documentWriterOptions.getCountOption("month").getValue());
        appendZeroFilledToBuffer(comment,
            documentWriterOptions.getCountOption("day").getValue());
        comment.append(":");
        appendZeroFilledToBuffer(comment, hour);
        appendZeroFilledToBuffer(comment, minute);

        // write preamble, see TTP [587]
        dviOutputStream.writeByte(DVI_PRE);
        dviOutputStream.writeByte(DVI_VERSION);
        dviOutputStream.writeNumber(SP_NUMERATOR, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeNumber(SP_DENOMINATOR, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeNumber(magnification, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeStringAndSize(comment.toString());
    }


    /**
     * After the output the method <code>endDviFile</code> have to
     * be called.
     *
     * @exception GeneralException if an error occurs
     */
    public void endDviFile() throws GeneralException {
        int postPosition = dviOutputStream.getStreamPosition();
        int numberTrailingBytes;

        if (magnification <= 0) {
            throw new GeneralException("magnification <= 0, "
                                       + "maybe beginDviFile was not called");
        }

        // write postamble beginning, see TTP [590]
        dviOutputStream.writeByte(DVI_POST);
        dviOutputStream.writeNumber(lastBop, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeNumber(SP_NUMERATOR, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeNumber(SP_DENOMINATOR, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeNumber(magnification, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeNumber(maximumPageHeight, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeNumber(maximumPageWidth, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeNumber(maximumStackDepth, 2);
        dviOutputStream.writeNumber(pages, 2);

        writeFontDefinitions();

        // write postpreamble ending, see TTP [591]
        dviOutputStream.writeByte(DVI_POST_POST);
        dviOutputStream.writeNumber(postPosition, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeByte(DVI_VERSION);

        /* Write trailing bytes.  After writing the trailing bytes
         * the file size multiply of DVI_ALIGN_SIZE. */
        numberTrailingBytes
            = dviOutputStream.getStreamPosition() % DVI_ALIGN_SIZE;
        if (numberTrailingBytes != 0) {
            numberTrailingBytes = DVI_ALIGN_SIZE - numberTrailingBytes;
        }
        numberTrailingBytes += MINIMUM_TRAILING_BYTES;
        for (int i = 0; i < numberTrailingBytes; i++) {
            dviOutputStream.writeByte(DVI_TRAILING_BYTE);
        }

    }



    /**
     * <code>beginPage</code> starts a new page in the dvi file. to
     * start a new page.  Each page must be terminated with a call of
     * <code>{@link #endPage() endPage()}</code>.
     *
     * @exception GeneralException if an error occurs
     */
    public void beginPage() throws GeneralException {
        lastGlyph = null;
        int position = dviOutputStream.getStreamPosition();

        pages++;

        dviOutputStream.writeByte(DVI_BOP);
        writeCounters();
        dviOutputStream.writeNumber(lastBop, BYTES_PER_QUADRUPLE);
        lastBop = position;

    }


    /**
     * <code>endPage</code> terminates the current page.  The page
     * have to be startet with <code>{@link #beginPage()
     * beginPage()}</code>.
     *
     * @exception GeneralException if an error occurs
     */
    public void endPage() throws GeneralException {
        dviOutputStream.writeByte(DVI_EOP);

        // empty the stack
        savedPositions.clear();
    }



    /*
     * Functions dealing with fonts
     */


    /**
     * Define a font used in the dvi-file.
     *
     * @param font the <code>Font</code>
     * @exception GeneralException if an error occurs
     */
    private void defineFont(final Font font)
        throws GeneralException {

        int fontNumber = definedFonts.indexOf(font);
        /*
         * TODO: the following (checksum, scale and designSize)
         * is not correct (TE) */
        int checksum = 0x58ab510b;
        int scale = convertToInt(font.getEm().getValue());
        int designSize = scale;
        String area = "";
        String fontName = font.getFontName();

        // see TTP [588]
        dviOutputStream.writeCodeNumberAndArg(DVI_FNT_DEF, fontNumber);
        dviOutputStream.writeNumber(checksum, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeNumber(scale, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeNumber(designSize, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeByte(area.length());
        dviOutputStream.writeByte(fontName.length());
        dviOutputStream.writeString(area);
        dviOutputStream.writeString(fontName);

    }


    /**
     * Select <code>Font</code> for the next CharNodes.
     *
     * @param font the <code>Font</code>
     * @exception GeneralException if an error occurs
     */
    public void selectFont(final Font font) throws GeneralException {
        int fontNumber;

        // define font, if not defined
        if (!definedFonts.contains(font)) {
            definedFonts.add(font);
            defineFont(font);
        }

        // select font
        fontNumber = definedFonts.indexOf(font);
        dviOutputStream.writeCodeNumberAndArg(DVI_FNT_NUM,
                                              DVI_FNT, fontNumber);
    }

    /**
     * Write the definition of all definied fonts.
     *
     * @exception GeneralException if an error occurs
     */
    private void writeFontDefinitions() throws GeneralException {
        for (Enumeration e = definedFonts.elements(); e.hasMoreElements();) {
            defineFont((Font) e.nextElement());
        }
    }



    /**
     * Write a charnode to the dvi-file.
     *
     * @param node the <code>CharNode</code>
     * @exception GeneralException if an error occurs
     */
    public void writeNode(final CharNode node) throws GeneralException {
        UnicodeChar unicodeChar = node.getCharacter();
        Glyph glyph = node.getGlyph();
        int characterNumber;

        /* get the chracterNumber */
        try {
            characterNumber = Integer.parseInt(glyph.getNumber());
        } catch (NumberFormatException e) {
            throw new GeneralException(e);
        }

        /* write characer in dvi-file */
        dviOutputStream.writeCodeNumberAndArg(DVI_SET_CHAR_NUM,
                                              DVI_SET_CHAR, characterNumber);
        currentPositions.addToH(convertToInt(glyph.getWidth().getValue()));
        updateMaximumPageWidth();

        lastGlyph = node.getGlyph();

    }


    /**
     * Write a glue node to the dvi-file.
     *
     * @param node the <code>GlueNode</code>
     * @param mode current mode
     * @exception GeneralException if an error occurs
     * @deprecated Use {@link
     * #writeSpace(de.dante.extex.interpreter.type.dimen.FixedDimen,
     * de.dante.extex.typesetter.Mode) writeSpace(FixedDimen, Mode)}
     * instead}.
     */
    public void writeGlueNode(final GlueNode node, final Mode mode)
        throws GeneralException {

        lastGlyph = null;

        if (mode == Mode.HORIZONTAL) {
            writeHorizontalSpace(node.getWidth());
        } else  if (mode == Mode.VERTICAL) {
            writeVerticalSpace(node.getWidth());
        } else {
            throw new GeneralException("unknown Mode");
        }
    }


    /**
     * Write node to the dvi-file.
     *
     * @param node a <code>RuleNode</code>
     * @exception GeneralException if an error occurs
     * @see "TeX -- The Program [585]"
     */
    public void writeNode(final RuleNode node) throws GeneralException {
        int width = convertToInt(node.getWidth().getValue());
        int height = convertToInt(node.getHeight().getValue()
                                  + node.getDepth().getValue());

        dviOutputStream.writeByte(DVI_PUT_RULE);
        dviOutputStream.writeNumber(height, BYTES_PER_QUADRUPLE);
        dviOutputStream.writeNumber(width, BYTES_PER_QUADRUPLE);
    }


    /**
     * Write node to the dvi-file.
     *
     * @param node a <code>WhatsItNode</code>
     * @exception GeneralException if an error occurs
     */
    public void writeNode(final WhatsItNode node) throws GeneralException {
        // TODO unimplemented
        throw new RuntimeException("unimplemented");

    }


    /**
     * Get the last error.
     *
     * @return <code>null</code> if there was no error, otherwise the
     * occurred error
     */
    public GeneralException getError() {
        return error;
    }


    /**
     * Write space to the the dvi-file.
     *
     * @param space the space
     * @param mode current Mode
     * @exception GeneralException if an error occurs
     */
    public void writeSpace(final FixedDimen space, final Mode mode)
        throws GeneralException {

        lastGlyph = null;

        if (mode == Mode.HORIZONTAL) {
            writeHorizontalSpace(space);
        } else  if (mode == Mode.VERTICAL) {
            writeVerticalSpace(space);
        } else {
            throw new GeneralException("unknown Mode");
        }
    }


    /**
     * Write horizontal space to dvi-file.
     *
     * @param space space size
     * @exception GeneralException if an error occurs
     */
    public void writeHorizontalSpace(final FixedDimen space)
        throws GeneralException {

        writeRight(convertToInt(space.getValue()));
    }


    /**
     * Write vertical space to dvi-file.
     *
     * @param space space size
     * @exception GeneralException if an error occurs
     */
    public void writeVerticalSpace(final FixedDimen space) throws GeneralException {
        writeDown(convertToInt(space.getValue()));
    }


    /**
     * Save all saved Positions.
     *
     * @exception GeneralException if an error occurs
     * @see #restoreCurrentPositions()
     */
    public void saveCurrentPositions() throws GeneralException {
        dviOutputStream.writeByte(DVI_PUSH);

        int stackDepth;

        savedPositions.push(currentPositions.clone());

        stackDepth = savedPositions.size();

        if (stackDepth > maximumStackDepth) {
            maximumStackDepth = stackDepth;
        }
    }

    /**
     * Restore all saved Positions.
     *
     * @exception GeneralException if an error occurs
     * @see #saveCurrentPositions()
     */
    public void restoreCurrentPositions() throws GeneralException {
        dviOutputStream.writeByte(DVI_POP);

        try {
            currentPositions = (DviPositions) savedPositions.pop();
        } catch (EmptyStackException e) {
            throw new GeneralException(e);
        }
    }



    /*
     * internal Methods
     */

    /**
     * Write the state of the counter registers.
     *
     * @exception GeneralException if an error occurs
     */
    private void writeCounters() throws GeneralException {
        long value;

        for (int i = 0; i < NUMBER_OF_COUNTERS; i++) {
            value
                = documentWriterOptions.getCountOption("count" + i).getValue();
            dviOutputStream.writeNumber(convertToInt(value),
                                        BYTES_PER_QUADRUPLE);
        }
    }


    /**
     * Move right current position.
     *
     * @param units distance (in sp)
     * @exception GeneralException if an error occurs
     */
    private void writeDown(final int units) throws GeneralException {
        if (units != 0) {
            dviOutputStream.writeCodeNumberAndArg(DVI_DOWN, units);
            currentPositions.addToV(units);
            updateMaximumPageHeight();
        }
    }


    /**
     * Move right current position.
     *
     * @param units distance (in sp)
     * @exception GeneralException if an error occurs
     */
    private void writeRight(final int units) throws GeneralException {
        if (units != 0) {
            dviOutputStream.writeCodeNumberAndArg(DVI_RIGHT, units);
            currentPositions.addToH(units);
            updateMaximumPageWidth();
        }
    }


    /**
     * Update maximum page width.  This method have to be called
     * after incrementing dviH.
     *
     * TODO: This method should be called implicit. (TE)
     *
     */
    private void updateMaximumPageWidth() {
        int position = currentPositions.getH();

        if (position > maximumPageWidth) {
            maximumPageWidth = position;
        }
    }


    /**
     * Update maximum page height.  This method have to be called
     * after incrementing dviV.
     *
     * TODO: This method should be called implicit. (TE)
     *
     */
    private void updateMaximumPageHeight() {
        int position = currentPositions.getV();

        if (position > maximumPageHeight) {
            maximumPageHeight = position;
        }
    }
}
