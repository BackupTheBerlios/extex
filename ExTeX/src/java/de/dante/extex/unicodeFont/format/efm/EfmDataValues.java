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

package de.dante.extex.unicodeFont.format.efm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.dante.extex.unicodeFont.format.tex.tfm.TfmFixWord;
import de.dante.extex.unicodeFont.format.tex.tfm.TfmParamArray;
import de.dante.extex.unicodeFont.format.tex.tfm.TfmReader;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Data values for the efm font.
 * e.g. the font dimen values from the tfm font.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class EfmDataValues {

    /**
     * The tag <code>data</code>.
     */
    private static final String TAG_DATA = "data";

    /**
     * The attribute <code>type</code>.
     */
    private static final String ATT_TYPE = "type";

    /**
     * The tag <code>parameter</code>.
     */
    private static final String TAG_PARAM = "parameter";

    /**
     * The attribute <code>name</code>.
     */
    private static final String ATT_NAME = "name";

    /**
     * The attribute <code>value</code>.
     */
    private static final String ATT_VALUE = "value";

    /**
     * the map width the data.
     */
    private Map map;

    /**
     * Create a new object.
     * @param tfm   The tfm reader.
     */
    public EfmDataValues(final TfmReader tfm) {

        TfmParamArray param = tfm.getParam();
        map = new HashMap();

        for (int i = 0, n = param.getLength(); i < n; i++) {
            String name = param.getLabelName(i);
            EfmData data = new EfmDataTfm(param.getParam(i));
            map.put(name, data);
        }

    }

    /**
     * Class for a efm data object with tfm data.
     */
    public class EfmDataTfm implements EfmData {

        /**
         * The value.
         */
        private TfmFixWord value;

        /**
         * Create a new object.
         * @param val  The tfm parameter value.
         */
        public EfmDataTfm(final TfmFixWord val) {

            value = val;
        }

        /**
         * @see de.dante.extex.unicodeFont.format.efm.EfmData#write(
         *      de.dante.util.xml.XMLStreamWriter)
         */
        public void write(final XMLStreamWriter writer) throws IOException {

            writer.writeAttribute(ATT_VALUE, value.getValue());
        }
    }

    /**
     * Write the data to the xml file.
     * @param writer    The writer.
     * @throws IOException if an IO-error occurred.
     */
    public void write(final XMLStreamWriter writer) throws IOException {

        writer.writeStartElement(TAG_DATA);
        writer.writeAttribute(ATT_TYPE, "tfm");

        Iterator it = map.keySet().iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            writer.writeStartElement(TAG_PARAM);
            writer.writeAttribute(ATT_NAME, key);
            ((EfmData) map.get(key)).write(writer);
            writer.writeEndElement();
        }
        writer.writeEndElement();

    }
}
