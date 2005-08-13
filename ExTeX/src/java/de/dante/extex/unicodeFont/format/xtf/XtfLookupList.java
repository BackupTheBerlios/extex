/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.xtf;

import java.io.IOException;

import de.dante.util.file.random.RandomAccessR;

/**
 * List for all LookupTables.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class XtfLookupList {

    /**
     * lookup count
     */
    private int lookupCount;

    /**
     * lookup offsetes
     */
    private int[] lookupOffsets;

    /**
     * lookups
     */
    private XtfLookup[] lookups;

    /**
     * Create a new object
     *
     * @param rar       input
     * @param offset    offset
     * @throws IOException if an IO-error occurs
     */
    XtfLookupList(final RandomAccessR rar, final int offset) throws IOException {

        rar.seek(offset);
        lookupCount = rar.readUnsignedShort();
        lookupOffsets = new int[lookupCount];
        lookups = new XtfLookup[lookupCount];
        for (int i = 0; i < lookupCount; i++) {
            lookupOffsets[i] = rar.readUnsignedShort();
        }
        for (int i = 0; i < lookupCount; i++) {
            lookups[i] = new XtfLookup(rar, offset + lookupOffsets[i]);
        }
    }

    /**
     * Returns the lookup
     * @param feature   feature
     * @param index     index
     * @return Returns the lookup
     */
    public XtfLookup getLookup(final XtfFeatureList.Feature feature, final int index) {

        if (feature.getLookupCount() > index) {
            int i = feature.getLookupListIndex(index);
            return lookups[i];
        }
        return null;
    }

    /**
     * Returns the lookupCount.
     * @return Returns the lookupCount.
     */
    public int getLookupCount() {

        return lookupCount;
    }

    /**
     * Returns the lookupOffsets.
     * @return Returns the lookupOffsets.
     */
    public int[] getLookupOffsets() {

        return lookupOffsets;
    }

    /**
     * Returns the lookups.
     * @return Returns the lookups.
     */
    public XtfLookup[] getLookups() {

        return lookups;
    }

    /**
     * Returns the info for this class
     * @return Returns the info for this class
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        buf.append("LookupList\n");
        buf.append("   lookup count  : ").append(lookupCount).append('\n');
        return buf.toString();
    }
}