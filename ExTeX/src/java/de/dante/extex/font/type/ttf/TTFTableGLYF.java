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

package de.dante.extex.font.type.ttf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Vector;

import org.jdom.Element;

import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * The 'glyf' table contains the data that defines the appearance
 * of the glyphs in the font. This includes specification of the
 * points that describe the contours that make up a glyph outline
 * and the instructions that grid-fit that glyph. The 'glyf' table
 * supports the definition of simple glyphs and compound glyphs,
 * that is, glyphs that are made up of other glyphs.
 *
 * <table BORDER="1">
 *   <tbody>
 *    <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </tbody>
 *   <tr><td>SHORT</td><td>numberOfContours</td><td>
 *           If the number of contours is greater than or equal
 *           to zero, this is a single glyph; if negative, this is a composite
 *           glyph.</td></tr>
 *   <tr><td>FWord</td><td>xMin</td><td>Minimum x for coordinate data.</td></tr>
 *   <tr><td>FWord</td><td>yMin</td><td>Minimum y for coordinate data.</td></tr>
 *   <tr><td>FWord</td><td>xMax</td><td>Maximum x for coordinate data.</td></tr>
 *   <tr><td>FWord</td><td>yMax</td><td>Maximum y for coordinate data.</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public class TTFTableGLYF extends AbstractTTFTable
        implements
            TTFTable,
            XMLConvertible {

    /**
     * buf
     */
    private byte[] buf = null;

    /**
     * descript
     */
    private Descript[] descript;

    /**
     * Create a new object.
     *
     * @param tablemap  the tablemap
     * @param de        directory entry
     * @param rar       the RandomAccessInput
     * @throws IOException if an error occured
     */
    TTFTableGLYF(final TableMap tablemap, final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());
        buf = new byte[de.getLength()];
        rar.readFully(buf); // mgn rar.read(buf)
        // buf ...
    }

    /**
     * @see de.dante.extex.font.type.ttf.TTFTable#getInitOrder()
     */
    public int getInitOrder() {

        return 2;
    }

    /**
     * @see de.dante.extex.font.type.ttf.TTFTable#init()
     */
    public void init() {

        TTFTableLOCA loca = (TTFTableLOCA) getTableMap().get(TTFFont.LOCA);
        TTFTableMAXP maxp = (TTFTableMAXP) getTableMap().get(TTFFont.MAXP);
        if (loca == null || maxp == null) {
            return;
        }
        int numGlyphs = maxp.getNumGlyphs();

        if (buf == null) {
            return;
        }

        descript = new Descript[numGlyphs];
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        for (int i = 0; i < numGlyphs; i++) {
            int len = loca.getOffset((short) (i + 1)) - loca.getOffset(i);
            if (len > 0) {
                bais.reset();
                bais.skip(loca.getOffset(i));
                short numberOfContours = (short) (bais.read() << TTFConstants.SHIFT8 | bais
                        .read());
                if (numberOfContours >= 0) {
                    descript[i] = new SimpleDescript(this, numberOfContours,
                            bais);
                }
            } else {
                descript[i] = null;
            }
        }

        for (int i = 0; i < numGlyphs; i++) {
            int len = loca.getOffset((short) (i + 1)) - loca.getOffset(i);
            if (len > 0) {
                bais.reset();
                bais.skip(loca.getOffset(i));
                short numberOfContours = (short) (bais.read() << TTFConstants.SHIFT8 | bais
                        .read());
                if (numberOfContours < 0) {
                    descript[i] = new CompositeDescript(this, bais);
                }
            }
        }
        buf = null;
    }

    /**
     * Returns the description
     * @param i index
     * @return Returns the description
     */
    public Descript getDescription(final int i) {

        return descript[i];
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return TTFFont.GLYF;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element table = new Element("glyf");
        table.setAttribute("id", TTFFont.convertIntToHexString(getType()));
        for (int i = 0; i < descript.length; i++) {
            Element des = new Element("description");
            table.addContent(des);
            des.setAttribute("id", String.valueOf(i));
            Descript d = descript[i];
            if (d != null) {
                des.addContent(d.toXML());
            }
        }
        return table;
    }

    // ------------------------------------------------
    // ------------------------------------------------
    // ------------------------------------------------
    // ------------------------------------------------

    /**
     * Simple Glyph Description.
     *
     * <table BORDER="1">
     *   <tbody>
     *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
     *   </tbody>
     *   <tr><td>USHORT</td><td>endPtsOfContours[<I>n</I>]</td></td>
     *              Array of last points of each contour; <I>n</I>  is
     *              the number of contours.</td></tr>
     *   <tr><td>USHORT</td><td>instructionLength</td><td>
     *              Total number of bytes for instructions.</td></tr>
     *   <tr><td>BYTE</td><td>instructions[<I>n</I>]</td><td>
     *              Array of instructions for each glyph; <I>n  </I>is
     *              the number of instructions.</td></tr>
     *   <tr><td>BYTE</td><td>flags[<I>n</I>]</td><td>
     *              Array of flags for each coordinate in outline; <I>n</I>
     *              is the number of flags.</td></tr>
     *   <tr><td>BYTE or SHORT</td><td>xCoordinates[ ]</td><td>
     *              First coordinates relative to (0,0); others are
     *              relative to previous point.</td></tr>
     *   <tr><td>BYTE or SHORT</td><td>yCoordinates[ ]</td><td>
     *              First coordinates relative to (0,0); others are
     *              relative to previous point.</td></tr>
     * </table>
     *
     * <p>Each flag is a single byte. Their meanings are shown below.</p>
     *
     * <table BORDER="1">
     *   <tbody>
     *     <tr><td><b>Flags</b></td><td><b>Bit </b></td><td><b>Description</b></td></tr>
     *   </tbody>
     *   <tr><td>On Curve</td><td>0</td><td>
     *              If set, the point is on the curve; otherwise, it is
     *              off the curve.</td></tr>
     *   <tr><td>x-Short Vector</td><td>1</td><td>
     *              If set, the corresponding x-coordinate is 1 byte
     *              long, not 2.</td></tr>
     *   <tr><td>y-Short Vector</td><td>2</td><td>
     *              If set, the corresponding y-coordinate is 1 byte
     *               long, not 2.</td></tr>
     *   <tr><td>Repeat</td><td>3</td><td>
     *              If set, the next byte specifies the number of
     *              additional times this set of flags is to be repeated. In this
     *              way, the number of flags listed can be smaller than the number of
     *              points in a character.</td></tr>
     *   <tr><td>This x is same (Positive x-Short Vector)</td><td>4</td><td>
     *              This flag has two meanings, depending on how the
     *               x-Short Vector flag is set. If x-Short Vector is set, this bit
     *              describes the sign of the value, with 1 equalling positive and 0
     *              negative. If the x-Short Vector bit is not set and this bit is
     *              set, then the current x-coordinate is the same as the previous
     *              x-coordinate. If the x-Short Vector bit is not set and this bit
     *              is also not set, the current x-coordinate is a signed 16-bit
     *              delta vector.</td>/tr>
     *    <tr><td>This y is same (Positive y-Short Vector)</td><td>5</td><td>
     *              This flag has two meanings, depending on how the
     *               y-Short Vector flag is set. If y-Short Vector is set, this bit
     *              describes the sign of the value, with 1 equalling positive and 0
     *              negative. If the y-Short Vector bit is not set and this bit is
     *              set, then the current y-coordinate is the same as the previous
     *              y-coordinate. If the y-Short Vector bit is not set and this bit
     *              is also not set, the current y-coordinate is a signed 16-bit
     *              delta vector.</td></tr>
     *     <tr><td>Reserved</td><td>6</td><td>This bit is reserved. Set it to zero.</td></tr>
     *     <tr><td>Reserved</td><td>7</td><td>This bit is reserved. Set it to zero.</td></tr>
     * </table>
     */
    public abstract static class Descript implements XMLConvertible {

        /**
         * Flag: ONCURVE
         */
        public static final byte ONCURVE = 0x01;

        /**
         * Flag: XSHORTVECTOR
         */
        public static final byte XSHORTVECTOR = 0x02;

        /**
         * Flag: YSHORTVECTOR
         */
        public static final byte YSHORTVECTOR = 0x04;

        /**
         * Flag: REPEAT
         */
        public static final byte REPEAT = 0x08;

        /**
         * Flag: XDUAL
         */
        public static final byte XDUAL = 0x10;

        /**
         * Flag: YDUAL
         */
        public static final byte YDUAL = 0x20;

        /**
         * parent table
         */
        protected TTFTableGLYF parentTable;

        /**
         * numberOfContours
         */
        private int numberOfContours;

        /**
         * xMin
         */
        private short xMin;

        /**
         * yMin
         */
        private short yMin;

        /**
         * xMax
         */
        private short xMax;

        /**
         * yMax
         */
        private short yMax;

        /**
         * instructions
         */
        private short[] instructions;

        /**
         * Returns the instructions
         * @return Returns the instructions
         */
        public short[] getInstructions() {

            return instructions;
        }

        /**
         * Create a new object.
         *
         * @param parentTable       the parent table
         * @param numberOfContours  number of conttours
         * @param bais              the basis
         */
        protected Descript(final TTFTableGLYF parentTable,
                final short numberOfContours, final ByteArrayInputStream bais) {

            this.parentTable = parentTable;
            this.numberOfContours = numberOfContours;
            xMin = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
            yMin = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
            xMax = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
            yMax = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
        }

        /**
         * Returns the number of contours.
         * @return Returns the number of contours.
         */
        public int getNumberOfContours() {

            return numberOfContours;
        }

        /**
         * Returns the x max.
         * @return Returns the x max.
         */
        public short getXMax() {

            return xMax;
        }

        /**
         * Returns the x min.
         * @return Returns the x min
         */
        public short getXMin() {

            return xMin;
        }

        /**
         * Returns the y max.
         * @return Returns the y max.
         */
        public short getYMax() {

            return yMax;
        }

        /**
         * Returns the y min.
         * @return Returns the y min.
         */
        public short getYMin() {

            return yMin;
        }

        /**
         * Returns the end pt of contours
         * @param i the index
         * @return Returns the end pt of contours
         */
        abstract int getEndPtOfContours(final int i);

        /**
         * Returns the flags.
         * @param i the index
         * @return Returns the flags.
         */
        abstract byte getFlags(final int i);

        /**
         * Returns the x coordinate.
         * @param i the index
         * @return Returns the x coordinate
         */
        abstract short getXCoordinate(final int i);

        /**
         * Returns the y coordinate.
         * @param i the index
         * @return Returns the y coordinate
         */
        abstract short getYCoordinate(final int i);

        /**
         * Returns the composite.
         * @return Returns the composite.
         */
        abstract boolean isComposite();

        /**
         * Returns the point count.
         * @return Returns the point count.
         */
        abstract int getPointCount();

        /**
         * Returns the contour count.
         * @return Returns the contour count.
         */
        abstract int getContourCount();

        /**
         * Read the instructions
         * @param rar       input
         * @param count     count
         * @throws IOException if an IO-error occurs
         */
        protected void readInstructions(final RandomAccessR rar, final int count)
                throws IOException {

            instructions = new short[count];
            for (int i = 0; i < count; i++) {
                instructions[i] = (short) rar.readUnsignedByte();
            }
        }

        /**
         * Read the instructions
         * @param bais      input
         * @param count     count
         */
        protected void readInstructions(final ByteArrayInputStream bais,
                final int count) {

            instructions = new short[count];
            for (int i = 0; i < count; i++) {
                instructions[i] = (short) bais.read();
            }
        }

        /**
         * @see de.dante.util.XMLConvertible#toXML()
         */
        public Element toXML() {

            Element des = new Element("descript");
            des.setAttribute("numberofcontours", String
                    .valueOf(numberOfContours));
            des.setAttribute("xmin", String.valueOf(xMin));
            des.setAttribute("ymin", String.valueOf(yMin));
            des.setAttribute("xmax", String.valueOf(xMax));
            des.setAttribute("ymax", String.valueOf(yMax));

            // TODO incomplete
            return des;
        }

    }

    /**
     * <p>
     * Compound glyphs are glyphs made up of two or more component glyphs. A
     * compound glyph description begins like a simple glyph description with four
     * words describing the bounding box. It is followed by n component glyph parts.
     * Each component glyph parts consists of a flag entry, two offset entries
     * and from one to four transformation entries.
     * </p>
     *
     * <table border="1">
     *   <tbody>
     *     <tr><th>Type</th><th>Name</th><th>Description</th></tr>
     *   </tbody>
     *   <tr><td>uint16</td><td>flags</td><td>Component flag</td></tr>
     *   <tr><td>uint16</td><td>glyphIndex</td><td>
     *      Glyph index of component</td></tr>
     *   <tr><td>int16, uint16, int8 or uint8</td><td>argument1</td><td>
     *      X-offset for component or point number; type depends on
     *      bits 0 and 1 in component flags</td></tr>
     *   <tr><td>int16, uint16, int8 or uint8</td><td>argument2</td><td>
     *      Y-offset for component or point number type depends
     *      on bits 0 and 1 in component flags</td></tr>
     *   <tr><td>transformation option</td><td>
     *      One of the transformation options from Table 19</td></tr>
     * </table>
     *
     * <p>Component flags</p>
     *
     * <table border="1">
     *   <tbody>
     *      <tr><th>Flags</th><th>Bit</th><th>Description</th></tr>
     *   </tbody>
     *   <tr><td>ARG_1_AND_2_ARE_WORDS</td><td>0</td><td>
     *      If set, the arguments are words; <br>
     *      If not set, they are bytes.</td></tr>
     *   <tr><td>ARGS_ARE_XY_VALUES</td><td>1</td><td>
     *      If set, the arguments are xy values; <br>
     *      If not set, they are points.</td></tr>
     *   <tr><td>ROUND_XY_TO_GRID</td><td>2</td><td>
     *      If set, round the xy values to grid; <br>
     *      if not set do not round xy values to grid
     *      (relevant only to bit 1 is set)</td></tr>
     *   <tr><td>WE_HAVE_A_SCALE</td><td>3</td><td>
     *      If set, there is a simple scale for the component.<br>
     *      If not set, scale is 1.0.</td></tr>
     *   <tr><td>(this bit is obsolete)</td><td>4</td><td>
     *      (obsolete; set to zero)</td></tr>
     *   <tr><td>MORE_COMPONENTS</td><td>5</td><td>
     *      If set, at least one additional glyph follows this one.</td></tr>
     *   <tr><td>WE_HAVE_AN_X_AND_Y_SCALE</td><td>6</td><td>
     *      If set the x direction will use a different scale than
     *      the y direction.</td></tr>
     *   <tr><td>WE_HAVE_A_TWO_BY_TWO</td><td>7</td><td>
     *      If set there is a 2-by-2 transformation that will be used
     *      to scale the component.</td></tr>
     *   <tr><td>WE_HAVE_INSTRUCTIONS</td><td>8</td><td>
     *      If set, instructions for the component character follow
     *      the last component.</td></tr>
     *   <tr><td>USE_MY_METRICS</td><td>9</td><td>Use metrics from
     *      this component for the compound glyph.</td></tr>
     *   <tr><td>OVERLAP_COMPOUND</td><td>10</td><td>
     *      If set, the components of this compound glyph overlap.</td></tr>
     * </table>
     */
    public class CompositeDescript extends Descript {

        /**
         * the components
         */
        private Vector components = new Vector();

        /**
         * Create a new object.
         *
         * @param parentTable   the parent table
         * @param bais          the bais
         */
        public CompositeDescript(final TTFTableGLYF parentTable,
                final ByteArrayInputStream bais) {

            super(parentTable, (short) -1, bais);

            // Get all of the composite components
            CompositeComp comp;
            int firstIndex = 0;
            int firstContour = 0;
            do {
                comp = new CompositeComp(firstIndex, firstContour, bais);
                components.addElement(comp);

                Descript desc;
                desc = parentTable.getDescription(comp.getGlyphIndex());
                if (desc != null) {
                    firstIndex += desc.getPointCount();
                    firstContour += desc.getContourCount();
                }
            } while ((comp.getFlags() & CompositeComp.MORE_COMPONENTS) != 0);

            if ((comp.getFlags() & CompositeComp.WE_HAVE_INSTRUCTIONS) != 0) {
                readInstructions(bais, (bais.read() << TTFConstants.SHIFT8 | bais.read()));
            }
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#getEndPtOfContours(int)
         */
        public int getEndPtOfContours(final int i) {

            CompositeComp c = getCompositeCompEndPt(i);
            if (c != null) {
                Descript gd = parentTable.getDescription(c.getGlyphIndex());
                return gd.getEndPtOfContours(i - c.getFirstContour())
                        + c.getFirstIndex();
            }
            return 0;
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#getFlags(int)
         */
        public byte getFlags(final int i) {

            CompositeComp c = getCompositeComp(i);
            if (c != null) {
                Descript gd = parentTable.getDescription(c.getGlyphIndex());
                return gd.getFlags(i - c.getFirstIndex());
            }
            return 0;
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#getXCoordinate(int)
         */
        public short getXCoordinate(final int i) {

            CompositeComp c = getCompositeComp(i);
            if (c != null) {
                Descript gd = parentTable.getDescription(c.getGlyphIndex());
                int n = i - c.getFirstIndex();
                int x = gd.getXCoordinate(n);
                int y = gd.getYCoordinate(n);
                short x1 = (short) c.scaleX(x, y);
                x1 += c.getXTranslate();
                return x1;
            }
            return 0;
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#getYCoordinate(int)
         */
        public short getYCoordinate(final int i) {

            CompositeComp c = getCompositeComp(i);
            if (c != null) {
                Descript gd = parentTable.getDescription(c.getGlyphIndex());
                int n = i - c.getFirstIndex();
                int x = gd.getXCoordinate(n);
                int y = gd.getYCoordinate(n);
                short y1 = (short) c.scaleY(x, y);
                y1 += c.getYTranslate();
                return y1;
            }
            return 0;
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#isComposite()
         */
        public boolean isComposite() {

            return true;
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#getPointCount()
         */
        public int getPointCount() {

            CompositeComp c = (CompositeComp) components.elementAt(components
                    .size() - 1);
            return c.getFirstIndex()
                    + parentTable.getDescription(c.getGlyphIndex())
                            .getPointCount();
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#getContourCount()
         */
        public int getContourCount() {

            CompositeComp c = (CompositeComp) components.elementAt(components
                    .size() - 1);
            return c.getFirstContour()
                    + parentTable.getDescription(c.getGlyphIndex())
                            .getContourCount();
        }

        /**
         * Returns the component index.
         * @param i the index
         * @return Returns the component index.
         */
        public int getComponentIndex(final int i) {

            return ((CompositeComp) components.elementAt(i)).getFirstIndex();
        }

        /**
         * Returns the component count.
         * @return Returns the component count
         */
        public int getComponentCount() {

            return components.size();
        }

        /**
         * Returns the composite comp.
         * @param i the index
         * @return Returns the composite comp.
         */
        protected CompositeComp getCompositeComp(final int i) {

            CompositeComp c;
            for (int n = 0; n < components.size(); n++) {
                c = (CompositeComp) components.elementAt(n);
                Descript gd = parentTable.getDescription(c.getGlyphIndex());
                if (c.getFirstIndex() <= i
                        && i < (c.getFirstIndex() + gd.getPointCount())) {
                    return c;
                }
            }
            return null;
        }

        /**
         * Returns the composite end pt.
         * @param i the index
         * @return Returns the composite end
         */
        protected CompositeComp getCompositeCompEndPt(final int i) {

            CompositeComp c;
            for (int j = 0; j < components.size(); j++) {
                c = (CompositeComp) components.elementAt(j);
                Descript gd = parentTable.getDescription(c.getGlyphIndex());
                if (c.getFirstContour() <= i
                        && i < (c.getFirstContour() + gd.getContourCount())) {
                    return c;
                }
            }
            return null;
        }
    }

    /**
     * SimleDescript.
     * <p>
     * the table specifies the format of a simple glyph.
     * A more detailed description 0f glyph outline requirements is given in
     * <a href="http://developer.apple.com/fonts/TTRefMan/RM01/Chap1.html">
     * Digitizing Letterform Designs</a>.
     * </p>
     * <table border="1">
     *   <tbody>
     *     <tr><td>Type</td><td>Name</td><td>Description</td></tr>
     *   </tbody>
     *   <tr><td>uint16</td><td>endPtsOfContours[n]</td><td>
     *      Array of last points of each contour;
     *      n is the number of contours; array
     *      entries are point indices</td></tr>
     *   <tr><td>uint16</td><td>instructionLength</td><td>
     *      Total number of bytes needed for instructions</td></tr>
     *   <tr><td>uint8</td><td>instructions[instructionLength]</td><td>
     *      Array of instructions for this glyph</td></tr>
     *   <tr><td>uint8</td><td>flags[variable]</td><td>Array of flags</td></tr>
     *   <tr><td>uint8 or int16</td><td>xCoordinates[]</td><td>
     *      Array of x-coordinates; the first is relative to (0,0),
     *      others are relative to previous point</td></tr>
     *   <tr><td>uint8 or int16</td><td>yCoordinates[]</td><td>
     *      Array of y-coordinates; the first is relative to (0,0),
     *      others are relative to previous point</td></tr>
     * </table>
     *
     * <p>
     * Each entry in the flags array is a byte in size.The meanings associated
     * with each bit in that byte are given in Table 16 below.
     * </p>
     *
     * <table border="1">
     *   <tbody>
     *      <tr><td>Flags</td><td>Bit (0 is lsb)</td><td>Description</td></tr>
     *   </tbody>
     *   <tr><td>On Curve</td><td>0</td><td>
     *      If set, the point is on the curve;<br>
     *      Otherwise, it is off the curve.</td></tr>
     *   <tr><td>x-Short Vector</td><td>1</td><td>
     *      If set, the corresponding x-coordinate is 1 byte long;<br>
     *      Otherwise, the corresponding x-coordinate is 2 bytes long</td></tr>
     *   <tr><td>y-Short Vector</td><td>2</td><td>
     *      If set, the corresponding y-coordinate is 1 byte long;<br>
     *      Otherwise, the corresponding y-coordinate is 2 bytes long</td></tr>
     *   <tr><td>Repeat</td><td>3</td><td>
     *      If set, the next byte specifies the number of additional
     *      times this set of flags is to be repeated. In this way,
     *      the number of flags listed can be smaller than the number
     *      of points in a character.</td></tr>
     *   <tr><td>This x is same (Positive x-Short vector)</td><td>4</td><td>
     *      This flag has one of two meanings, depending on how the x-Short
     *      Vector flag is set.<br>
     *      If the x-Short Vector bit is set, this bit describes the sign
     *      of the value, with a value of 1 equalling positive and a zero
     *      value negative.<br>
     *      If the x-short Vector bit is not set, and this bit is set,
     *      then the current x-coordinate is the same as the previous
     *      x-coordinate.<br>
     *      If the x-short Vector bit is not set, and this bit is not set,
     *      the current x-coordinate is a signed 16-bit delta vector.
     *      In this case, the delta vector is the change in x</td></tr>
     *   <tr><td>This y is same (Positive y-Short vector)</td><td>5</td><td>
     *      This flag has one of two meanings, depending on how the
     *      y-Short Vector flag is set.<br>
     *      If the y-Short Vector bit is set, this bit describes the
     *      sign of the value, with a value of 1 equalling positive and
     *      a zero value negative.<br>
     *      If the y-short Vector bit is not set, and this bit is set,
     *      then the current y-coordinate is the same as the previous
     *      y-coordinate.<br
     *      If the y-short Vector bit is not set, and this bit is not set,
     *      the current y-coordinate is a signed 16-bit delta vector.
     *      In this case, the delta vector is the change in y</td></tr>
     *   <tr><td>Reserved</td><td>6 - 7</td><td>Set to zero</td></tr>
     * </table>
     */
    public class SimpleDescript extends Descript {

        /**
         * endPtsOfContours
         */
        private int[] endPtsOfContours;

        /**
         * flags
         */
        private byte[] flags;

        /**
         * xCoordinates
         */
        private short[] xCoordinates;

        /**
         * yCoordinates
         */
        private short[] yCoordinates;

        /**
         * count
         */
        private int count;

        /**
         * Create a new object.
         *
         * @param parentTable       the parent table
         * @param numberOfContours  the number of contours
         * @param bais              the basis
         */
        SimpleDescript(final TTFTableGLYF parentTable,
                final short numberOfContours, final ByteArrayInputStream bais) {

            super(parentTable, numberOfContours, bais);

            endPtsOfContours = new int[numberOfContours];
            for (int i = 0; i < numberOfContours; i++) {
                endPtsOfContours[i] = (bais.read() << TTFConstants.SHIFT8 | bais.read());
            }

            // The last end point index reveals the total number of points
            count = endPtsOfContours[numberOfContours - 1] + 1;
            flags = new byte[count];
            xCoordinates = new short[count];
            yCoordinates = new short[count];

            int instructionCount = (bais.read() << TTFConstants.SHIFT8 | bais.read());
            readInstructions(bais, instructionCount);
            readFlags(count, bais);
            readCoords(count, bais);
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#getEndPtOfContours(int)
         */
        public int getEndPtOfContours(final int i) {

            return endPtsOfContours[i];
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#getFlags(int)
         */
        public byte getFlags(final int i) {

            return flags[i];
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#getXCoordinate(int)
         */
        public short getXCoordinate(final int i) {

            return xCoordinates[i];
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#getYCoordinate(int)
         */
        public short getYCoordinate(final int i) {

            return yCoordinates[i];
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#isComposite()
         */
        public boolean isComposite() {

            return false;
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#getPointCount()
         */
        public int getPointCount() {

            return count;
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableGLYF.Descript#getContourCount()
         */
        public int getContourCount() {

            return getNumberOfContours();
        }

        /**
         * The table is stored as relative values,
         * but we store them as absolutes
         * @param   cnt         count
         * @param   bais        the input
         */
        private void readCoords(final int cnt, final ByteArrayInputStream bais) {

            short x = 0;
            short y = 0;
            for (int i = 0; i < cnt; i++) {
                if ((flags[i] & XDUAL) != 0) {
                    if ((flags[i] & XSHORTVECTOR) != 0) {
                        x += (short) bais.read();
                    }
                } else {
                    if ((flags[i] & XSHORTVECTOR) != 0) {
                        x += (short) -((short) bais.read());
                    } else {
                        x += (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
                    }
                }
                xCoordinates[i] = x;
            }

            for (int i = 0; i < cnt; i++) {
                if ((flags[i] & YDUAL) != 0) {
                    if ((flags[i] & YSHORTVECTOR) != 0) {
                        y += (short) bais.read();
                    }
                } else {
                    if ((flags[i] & YSHORTVECTOR) != 0) {
                        y += (short) -((short) bais.read());
                    } else {
                        y += (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
                    }
                }
                yCoordinates[i] = y;
            }
        }

        /**
         * The flags are run-length encoded.
         * @param   flagCount   the flag count
         * @param   bais        the input
         */
        private void readFlags(final int flagCount,
                final ByteArrayInputStream bais) {

            try {
                for (int index = 0; index < flagCount; index++) {
                    flags[index] = (byte) bais.read();
                    if ((flags[index] & REPEAT) != 0) {
                        int repeats = bais.read();
                        for (int i = 1; i <= repeats; i++) {
                            flags[index + i] = flags[index];
                        }
                        index += repeats;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // TODO change
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Composite comp
     */
    public static class CompositeComp {

        /**
         * ARG_1_AND_2_ARE_WORDS
         */
        public static final short ARG_1_AND_2_ARE_WORDS = 0x0001;

        /**
         * ARGS_ARE_XY_VALUES
         */
        public static final short ARGS_ARE_XY_VALUES = 0x0002;

        /**
         * ROUND_XY_TO_GRID
         */
        public static final short ROUND_XY_TO_GRID = 0x0004;

        /**
         * WE_HAVE_A_SCALE
         */
        public static final short WE_HAVE_A_SCALE = 0x0008;

        /**
         * MORE_COMPONENTS
         */
        public static final short MORE_COMPONENTS = 0x0020;

        /**
         * WE_HAVE_AN_X_AND_Y_SCALE
         */
        public static final short WE_HAVE_AN_X_AND_Y_SCALE = 0x0040;

        /**
         * WE_HAVE_A_TWO_BY_TWO
         */
        public static final short WE_HAVE_A_TWO_BY_TWO = 0x0080;

        /**
         * WE_HAVE_INSTRUCTIONS
         */
        public static final short WE_HAVE_INSTRUCTIONS = 0x0100;

        /**
         * USE_MY_METRICS
         */
        public static final short USE_MY_METRICS = 0x0200;

        /**
         * firstIndex
         */
        private int firstIndex;

        /**
         * firstContour
         */
        private int firstContour;

        /**
         * argument1
         */
        private short argument1;

        /**
         * argument2
         */
        private short argument2;

        /**
         * flags
         */
        private short flags;

        /**
         * glyphIndex
         */
        private short glyphIndex;

        /**
         * xscale
         */
        private double xscale = 1.0;

        /**
         * yscale
         */
        private double yscale = 1.0;

        /**
         * scale01
         */
        private double scale01 = 0.0;

        /**
         * scale10
         */
        private double scale10 = 0.0;

        /**
         * xtranslate
         */
        private int xtranslate = 0;

        /**
         * ytranslat
         */
        private int ytranslate = 0;

        /**
         * point1
         */
        private int point1 = 0;

        /**
         * point2
         */
        private int point2 = 0;

        /**
         * Create a new object.
         *
         * @param firstIndex    the first index
         * @param firstContour  the first contour
         * @param bais          the input
         */
        CompositeComp(final int firstIndex, final int firstContour,
                final ByteArrayInputStream bais) {

            this.firstIndex = firstIndex;
            this.firstContour = firstContour;
            flags = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
            glyphIndex = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());

            // Get the arguments as just their raw values
            if ((flags & ARG_1_AND_2_ARE_WORDS) != 0) {
                argument1 = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
                argument2 = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
            } else {
                argument1 = (short) bais.read();
                argument2 = (short) bais.read();
            }

            // Assign the arguments according to the flags
            if ((flags & ARGS_ARE_XY_VALUES) != 0) {
                xtranslate = argument1;
                ytranslate = argument2;
            } else {
                point1 = argument1;
                point2 = argument2;
            }

            // Get the scale values (if any)
            if ((flags & WE_HAVE_A_SCALE) != 0) {
                int i = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
                xscale = yscale = (double) i / (double) 0x4000;
            } else if ((flags & WE_HAVE_AN_X_AND_Y_SCALE) != 0) {
                short i = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
                xscale = (double) i / (double) 0x4000;
                i = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
                yscale = (double) i / (double) 0x4000;
            } else if ((flags & WE_HAVE_A_TWO_BY_TWO) != 0) {
                int i = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
                xscale = (double) i / (double) 0x4000;
                i = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
                scale01 = (double) i / (double) 0x4000;
                i = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
                scale10 = (double) i / (double) 0x4000;
                i = (short) (bais.read() << TTFConstants.SHIFT8 | bais.read());
                yscale = (double) i / (double) 0x4000;
            }
        }

        /**
         * Returns the first index.
         * @return Returns the first index.
         */
        public int getFirstIndex() {

            return firstIndex;
        }

        /**
         * Returns the first contour.
         * @return Returns the first contour.
         */
        public int getFirstContour() {

            return firstContour;
        }

        /**
         * Returns the argument 1.
         * @return Returns the argument 1.
         */
        public short getArgument1() {

            return argument1;
        }

        /**
         * Returns the argument 2.
         * @return Returns the argument 2.

         */
        public short getArgument2() {

            return argument2;
        }

        /**
         * Returns the flags.
         * @return Returns the flags.
         */
        public short getFlags() {

            return flags;
        }

        /**
         * Returns the glyph index.
         * @return Returns the glyph index.
         */
        public short getGlyphIndex() {

            return glyphIndex;
        }

        /**
         * Returns the scale 1.
         * @return Returns the scale 1.
         */
        public double getScale01() {

            return scale01;
        }

        /**
         * Returns the scale 0.
         * @return Returns the scale 0.
         */
        public double getScale10() {

            return scale10;
        }

        /**
         * Returns the x scale.
         * @return Returns the x scale.
         */
        public double getXScale() {

            return xscale;
        }

        /**
         * Returns the y scale.
         * @return Returns the y scale.
         */
        public double getYScale() {

            return yscale;
        }

        /**
         * Returns the x translate.
         * @return Returns the x translate.
         */
        public int getXTranslate() {

            return xtranslate;
        }

        /**
         * Returns the y translate.
         * @return Returns the y translate.
         */
        public int getYTranslate() {

            return ytranslate;
        }

        /**
         * Transforms an x-coordinate of a point for this component.
         *
         * @param x The x-coordinate of the point to transform
         * @param y The y-coordinate of the point to transform
         * @return The transformed x-coordinate
         */
        public int scaleX(final int x, final int y) {

            return Math.round((float) (x * xscale + y * scale10));
        }

        /**
         * Transforms a y-coordinate of a point for this component.
         *
         * @param x The x-coordinate of the point to transform
         * @param y The y-coordinate of the point to transform
         * @return The transformed y-coordinate
         */
        public int scaleY(final int x, final int y) {

            return Math.round((float) (x * scale01 + y * yscale));
        }
    }
}