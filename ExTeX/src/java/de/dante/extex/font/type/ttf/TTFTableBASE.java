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

import org.apache.batik.bridge.BaseScriptingEnvironment;
import org.jdom.Comment;
import org.jdom.Element;

import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * The Baseline table (BASE) provides information used to
 * align glyphs of different scripts and sizes in a line of text,
 * whether the glyphs are in the same font or in different fonts.
 * To improve text layout, the Baseline table also provides
 * minimum (min) and maximum (max) glyph extent values for
 * each script, language system, or feature in a font.
 *
 * <p><img src="TTFTableBASE.png"/></p>
 *
 * <table border="1">
 *   <thead>
 *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </thead>
 *   <tr><td>fixed32</td><td>Version</td><td>
 *          Version of the BASE table-initially 0x00010000</td></tr>
 *   <tr><td>Offset</td><td>HorizAxis</td><td>
 *          Offset to horizontal Axis table-from beginning of
 *          BASE table-may be NULL</td></tr>
 *   <tr><td>Offset</td><td>VertAxis</td><td>
 *          Offset to vertical Axis table-from beginning of
 *          BASE table-may be NULL</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TTFTableBASE extends AbstractTTFTable
        implements
            TTFTable,
            XMLConvertible {

    /**
     * Create a new object
     *
     * @param tablemap  the tablemap
     * @param de        entry
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TTFTableBASE(final TableMap tablemap, final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());

        version = rar.readUnsignedShort();
        horizAxisOffset = rar.readUnsignedShort();
        vertAxisOffset = rar.readUnsignedShort();

//        vertical = new Axis(rar, de.getOffset() + vertAxisOffset, "vertical");
//        horizontal = new Axis(rar, de.getOffset() + horizAxisOffset,
//                "horizontal");

        // incomplete
    }

    /**
     * vertical Axis
     */
    private Axis vertical;

    /**
     * horizontal Axis
     */
    private Axis horizontal;

    /**
     * version
     */
    private int version;

    /**
     * horiz. Axis offset
     */
    private int horizAxisOffset;

    /**
     * vert. Axis offset
     */
    private int vertAxisOffset;

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return TTFFont.BASE;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element table = new Element("base");
        table.setAttribute("id", "0x" + Integer.toHexString(getType()));
        Comment c = new Comment("incomplete");
        table.addContent(c);
        return table;
    }

    /**
     * Returns the horizAxisOffset.
     * @return Returns the horizAxisOffset.
     */
    public int getHorizAxisOffset() {

        return horizAxisOffset;
    }

    /**
     * Returns the version.
     * @return Returns the version.
     */
    public int getVersion() {

        return version;
    }

    /**
     * Returns the vertAxisOffset.
     * @return Returns the vertAxisOffset.
     */
    public int getVertAxisOffset() {

        return vertAxisOffset;
    }

    /**
     * Axis (vertical or horizontal)
     *
     * <table border="1">
     *   <thead>
     *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
     *   </thead>
     *   <tr><td>Offset</td><td>BaseTagList</td><td>
     *      Offset to BaseTagList table-from beginning of Axis
     *      table-may be NULL</td></tr>
     *   <tr><td>Offset</td><td>BaseScriptList</td><td>
     *      Offset to BaseScriptList table-from beginning
     *      of Axis table</td></tr>
     * </table>
     */
    public class Axis implements XMLConvertible {

        /**
         * the name
         */
        private String name;

        /**
         * Create a new object.
         *
         * @param rar       the input
         * @param offset    the offset
         * @param n         the name of the table
         * @throws IOException if an IO-error occurs.
         */
        public Axis(final RandomAccessR rar, final int offset, final String n)
                throws IOException {

            name = n;
            rar.seek(offset);

            baseTagListOffset = rar.readUnsignedShort();
            baseScriptListOffset = rar.readUnsignedShort();

            basetaglist = new BaseTagList(rar, offset + baseTagListOffset, name);
        }

        /**
         * the basetaglist
         */
        private BaseTagList basetaglist;

        /**
         * basetaglist offset
         */
        private int baseTagListOffset;

        /**
         * basescript offset
         */
        private int baseScriptListOffset;

        /**
         * @see de.dante.util.XMLConvertible#toXML()
         */
        public Element toXML() {

            Element table = new Element("axis");
            table.setAttribute("name", name);
            Comment c = new Comment("incomplete");
            table.addContent(c);
            return table;
        }

        /**
         * Returns the name.
         * @return Returns the name.
         */
        public String getName() {

            return name;
        }

        /**
         * Returns the baseScriptListOffset.
         * @return Returns the baseScriptListOffset.
         */
        public int getBaseScriptListOffset() {

            return baseScriptListOffset;
        }

        /**
         * Returns the baseTagListOffset.
         * @return Returns the baseTagListOffset.
         */
        public int getBaseTagListOffset() {

            return baseTagListOffset;
        }
    }

    /**
     * The BaseTagList table identifies the baselines for
     * all scripts in the font that are rendered in the
     * same text direction. Each baseline is identified
     * with a 4-byte baseline tag.
     *
     */
    public class BaseTagList implements XMLConvertible {

        /**
         * the name
         */
        private String name;

        /**
         * Create a new object.
         *
         * @param rar       the input
         * @param offset    the offset
         * @param n         the name
         * @throws IOException if an IO-error occurs.
         */
        public BaseTagList(final RandomAccessR rar, final int offset,
                final String n) throws IOException {

            name = n;
            rar.seek(offset);

        }

        /**
         * @see de.dante.util.XMLConvertible#toXML()
         */
        public Element toXML() {

            Element table = new Element("basetaglist");
            table.setAttribute("name", name);
            Comment c = new Comment("incomplete");
            table.addContent(c);
            return table;
        }

    }
}