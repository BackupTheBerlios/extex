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
 * Glyph substitution.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class TTFTableGSUB extends AbstractTTFTable
        implements
            TTFTable,
            XMLConvertible {

    /**
     * Version
     */
    private int version;

    /**
     * scriptlist
     */
    private ScriptList scriptList;

    /**
     * featurelist
     */
    private FeatureList featureList;

    /**
     * lookuplist
     */
    private LookupList lookupList;

    /**
     * Create a new object
     *
     * @param tablemap  the tablemap
     * @param de        directory entry
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TTFTableGSUB(final TableMap tablemap, final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());

        // GSUB Header
        version = rar.readInt();
        int scriptListOffset = rar.readUnsignedShort();
        int featureListOffset = rar.readUnsignedShort();
        int lookupListOffset = rar.readUnsignedShort();

        // Script List
        scriptList = new ScriptList(rar, de.getOffset() + scriptListOffset);

        // Feature List
        featureList = new FeatureList(rar, de.getOffset() + featureListOffset);

        // Lookup List
        lookupList = new LookupList(rar, de.getOffset() + lookupListOffset);
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return TTFFont.GSUB;
    }

    /**
     * @return Returns the featureList.
     */
    public FeatureList getFeatureList() {

        return featureList;
    }

    /**
     * @return Returns the lookupList.
     */
    public LookupList getLookupList() {

        return lookupList;
    }

    /**
     * @return Returns the scriptList.
     */
    public ScriptList getScriptList() {

        return scriptList;
    }

    /**
     * @return Returns the version.
     */
    public int getVersion() {

        return version;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element table = new Element("gsub");
        table.setAttribute("id", "0x" + Integer.toHexString(getType()));
        // TODO incomplete
        return table;
    }

}