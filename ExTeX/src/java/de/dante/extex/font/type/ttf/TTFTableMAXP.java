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

import java.io.IOException;

import org.jdom.Element;

import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * The 'maxp' table establishes the memory requirements for a font.
 *
 * <table BORDER="1">
 *   <tbody>
 *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </tbody>
 *   <tr><td>Fixed</td><td>Table version number</td><td>
 *          0x00010000 for version 1.0.</td></tr>
 *   <tr><td>USHORT</td><td>numGlyphs</td><td>
 *          The number of glyphs in the font.</td></tr>
 *   <tr><td>USHORT</td><td>maxPoints</td><td>
 *          Maximum points in a non-composite glyph.</td></tr>
 *   <tr><td>USHORT</td><td>maxContours</td><td>
 *          Maximum contours in a non-composite glyph.</td></tr>
 *   <tr><td>USHORT</td><td>maxCompositePoints</td><td>
 *          Maximum points in a composite glyph.</td></tr>
 *   <tr><td>USHORT</td><td>maxCompositeContours</td><td>
 *          Maximum contours in a composite glyph.</td></tr>
 *   <tr><td>USHORT</td><td>maxZones</td><td>
 *          1 if instructions do not use the twilight zone (Z0),
 *          or 2 if instructions do use Z0; should be set to 2 in most cases.</td></tr>
 *   <tr><td>USHORT</td><td>maxTwilightPoints</td><td>
 *          Maximum points used in Z0.</td></tr>
 *   <tr><td>USHORT</td><td>maxStorage</td><td>
 *          Number of Storage Area locations.</td></tr>
 *   <tr><td>USHORT</td><td>maxFunctionDefs</td><td>
 *          Number of FDEFs.</td></tr>
 *   <tr><td>USHORT</td><td>maxInstructionDefs</td><td>Number of IDEFs.</td></tr>
 *   <tr><td>USHORT</td><td>maxStackElements</td><td>
 *          Maximum stack depth 2.</td></tr>
 *   <tr><td>USHORT</td><<td>maxSizeOfInstructions</td><td>
 *          Maximum byte count for glyph instructions.</td></tr>
 *   <tr><td>USHORT</td><td>maxComponentElements</td><td>
 *          Maximum number of components referenced at &ldquo;top
 *          level&rdquo; for any composite glyph.</td></tr>
 *   <tr><td>USHORT</td><td>maxComponentDepth</td><td>
 *          Maximum levels of recursion; 1 for simple
 *          components.</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class TTFTableMAXP extends AbstractTTFTable
        implements
            TTFTable,
            XMLConvertible {

    /**
     * version
     */
    private int version;

    /**
     * numGlyphs
     */
    private int numGlyphs;

    /**
     * maxPoints
     */
    private int maxPoints;

    /**
     * maxContours
     */
    private int maxContours;

    /**
     * maxCompositePoints
     */
    private int maxCompositePoints;

    /**
     * maxCompositeContours
     */
    private int maxCompositeContours;

    /**
     * maxZones
     */
    private int maxZones;

    /**
     * maxTwilightPoints
     */
    private int maxTwilightPoints;

    /**
     * maxStorage
     */
    private int maxStorage;

    /**
     * maxFunctionDefs
     */
    private int maxFunctionDefs;

    /**
     * maxInstructionDefs
     */
    private int maxInstructionDefs;

    /**
     * maxStackElements
     */
    private int maxStackElements;

    /**
     * maxSizeOfInstructions
     */
    private int maxSizeOfInstructions;

    /**
     * maxComponentElements
     */
    private int maxComponentElements;

    /**
     * maxComponentDepth
     */
    private int maxComponentDepth;

    /**
     * Create a new object.
     *
     * @param tablemap  the tablemap
     * @param de        directory entry
     * @param rar       input
     * @throws IOException  if an IO-error occurs
     */
    TTFTableMAXP(final TableMap tablemap, final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());
        version = rar.readInt();
        numGlyphs = rar.readUnsignedShort();
        maxPoints = rar.readUnsignedShort();
        maxContours = rar.readUnsignedShort();
        maxCompositePoints = rar.readUnsignedShort();
        maxCompositeContours = rar.readUnsignedShort();
        maxZones = rar.readUnsignedShort();
        maxTwilightPoints = rar.readUnsignedShort();
        maxStorage = rar.readUnsignedShort();
        maxFunctionDefs = rar.readUnsignedShort();
        maxInstructionDefs = rar.readUnsignedShort();
        maxStackElements = rar.readUnsignedShort();
        maxSizeOfInstructions = rar.readUnsignedShort();
        maxComponentElements = rar.readUnsignedShort();
        maxComponentDepth = rar.readUnsignedShort();
    }

    /**
     * Returns the max component depth
     * @return Returns the max component depth
     */
    public int getMaxComponentDepth() {

        return maxComponentDepth;
    }

    /**
     * Returns the max component elements
     * @return Returns the max component elements
     */
    public int getMaxComponentElements() {

        return maxComponentElements;
    }

    /**
     * Returns the max composite contours
     * @return Returns the max composite contours
     */
    public int getMaxCompositeContours() {

        return maxCompositeContours;
    }

    /**
     * Returns the max composite points
     * @return Returns the max composite points
     */
    public int getMaxCompositePoints() {

        return maxCompositePoints;
    }

    /**
     * Returns the max contours
     * @return Returns the max contours
     */
    public int getMaxContours() {

        return maxContours;
    }

    /**
     * Returns the max function defs
     * @return Returns the max function defs
     */
    public int getMaxFunctionDefs() {

        return maxFunctionDefs;
    }

    /**
     * Returns the max instruction defs
     * @return Returns the max instruction defs
     */
    public int getMaxInstructionDefs() {

        return maxInstructionDefs;
    }

    /**
     * Returns the max points
     * @return Returns the max points
     */
    public int getMaxPoints() {

        return maxPoints;
    }

    /**
     * Returns the max size of instructions
     * @return Returns the max size of instructions
     */
    public int getMaxSizeOfInstructions() {

        return maxSizeOfInstructions;
    }

    /**
     * Returns the max stack elements
     * @return Returns the max stack elements
     */
    public int getMaxStackElements() {

        return maxStackElements;
    }

    /**
     * Returns the max storage
     * @return Returns the max storage
     */
    public int getMaxStorage() {

        return maxStorage;
    }

    /**
     * Returns the max twilight points
     * @return Returns the max twilight points
     */
    public int getMaxTwilightPoints() {

        return maxTwilightPoints;
    }

    /**
     * Returns the max zones
     * @return Returns the max zones
     */
    public int getMaxZones() {

        return maxZones;
    }

    /**
     * Returns the number of glyphs
     * @return Returns the number of glyphs
     */
    public int getNumGlyphs() {

        return numGlyphs;
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return TTFFont.MAXP;
    }

    /**
     * Returns the versionNumber.
     * @return Returns the versionNumber.
     */
    public int getVersion() {

        return version;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element table = new Element("maxp");
        table.setAttribute("id", TTFFont.convertIntToHexString(getType()));
        table.setAttribute("version", TTFFont.convertIntToHexString(version));
        table.setAttribute("numberofglyphs", String.valueOf(numGlyphs));
        table.setAttribute("maxpoints", String.valueOf(maxPoints));
        table.setAttribute("maxcontours", String.valueOf(maxContours));
        table.setAttribute("maxcompositepoints", String
                .valueOf(maxCompositePoints));
        table.setAttribute("maxcompositecontours", String
                .valueOf(maxCompositeContours));
        table.setAttribute("maxzones", String.valueOf(maxZones));
        table.setAttribute("maxtwilightpoints", String
                .valueOf(maxTwilightPoints));
        table.setAttribute("maxstorage", String.valueOf(maxStorage));
        table.setAttribute("maxfunctiondefs", String.valueOf(maxFunctionDefs));
        table.setAttribute("maxinstructionsdefs", String
                .valueOf(maxInstructionDefs));
        table
                .setAttribute("maxstackelements", String
                        .valueOf(maxStackElements));
        table.setAttribute("maxsizeofinstructions", String
                .valueOf(maxSizeOfInstructions));
        table.setAttribute("maxcomponentelements", String
                .valueOf(maxComponentElements));
        table.setAttribute("maxcomponentdepth", String
                .valueOf(maxComponentDepth));
        return table;
    }
}