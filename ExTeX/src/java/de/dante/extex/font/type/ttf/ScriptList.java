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

import de.dante.util.file.random.RandomAccessR;

/**
 * ScriptList
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class ScriptList {

    /**
     * script count
     */
    private int scriptCount = 0;

    /**
     * script records
     */
    private Record[] records;

    /**
     * scripts
     */
    private Script[] scripts;

    /**
     * Create a new object
     *
     * @param rar       input
     * @param offset    offset
     * @throws IOException if an IO-error occurs
     */
    ScriptList(final RandomAccessR rar, final int offset) throws IOException {

        rar.seek(offset);

        scriptCount = rar.readUnsignedShort();
        records = new Record[scriptCount];
        scripts = new Script[scriptCount];
        for (int i = 0; i < scriptCount; i++) {
            records[i] = new Record(rar);
        }
        for (int i = 0; i < scriptCount; i++) {
            scripts[i] = new Script(rar, offset + records[i].getOffset());
        }
    }

    /**
     * Returns the scriptcount
     * @return Returns the scriptcount
     */
    public int getCount() {

        return scriptCount;
    }

    /**
     * Returns the script record
     * @param i     the number
     * @return Rweturns the script record
     */
    public Record getRecord(final int i) {

        return records[i];
    }

    /**
     * Find a script
     * @param tag   the tag for the script
     * @return Returns the script
     */
    public Script findScript(final String tag) {

        if (tag.length() != 4) {
            return null;
        }
        int tagVal = ((tag.charAt(0) << 24) | (tag.charAt(1) << 16)
                | (tag.charAt(2) << 8) | tag.charAt(3));
        for (int i = 0; i < scriptCount; i++) {
            if (records[i].getTag() == tagVal) {
                return scripts[i];
            }
        }
        return null;
    }

    /**
     * Returns the info for this class
     * @return Returns the info for this class
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        buf.append("ScriptList\n");
        buf.append("   count : " + String.valueOf(scriptCount) + '\n');
        for (int i = 0; i < records.length; i++) {
            buf.append(records[i].toString());
        }
        for (int i = 0; i < scripts.length; i++) {
            buf.append(scripts[i].toString());
        }
        return buf.toString();
    }

    /**
     * Record
     */
    public class Record {

        /**
         * tag
         */
        private int tag;

        /**
         * offset
         */
        private int offset;

        /**
         * Create a new object
         *
         * @param rar       input
         * @throws IOException if an IO-error occurs
         */
        Record(final RandomAccessR rar) throws IOException {

            tag = rar.readInt();
            offset = rar.readUnsignedShort();
        }

        /**
         * Returns the tag
         * @return Returns the tag
         */
        public int getTag() {

            return tag;
        }

        /**
         * Returns the offset
         * @return returns the offset
         */
        public int getOffset() {

            return offset;
        }

        /**
         * Returns the info for this class
         * @return Returns the info for this class
         */
        public String toString() {

            StringBuffer buf = new StringBuffer();
            buf.append("ScriptRecord\n");
            buf.append("   tag    : " + String.valueOf(tag) + '\n');
            buf.append("   offset : " + String.valueOf(offset) + '\n');
            return buf.toString();
        }

    }

    /**
     * Script
     */
    public class Script {

        /**
         * defaultLangSysOffset
         */
        private int defaultLangSysOffset;

        /**
         * LangSysCount
         */
        private int langSysCount;

        /**
         * LangSysRecord
         */
        private LangSysRecord[] langSysRecords;

        /**
         * LangSys
         */
        private LangSys defaultLangSys;

        /**
         * langsys
         */
        private LangSys[] langSys;

        /**
         * Create a new object
         *
         * @param rar       input
         * @param offset    offset
         * @throws IOException if an IO-error occurs
         */
        Script(final RandomAccessR rar, final int offset) throws IOException {

            rar.seek(offset);

            defaultLangSysOffset = rar.readUnsignedShort();
            langSysCount = rar.readUnsignedShort();
            if (langSysCount > 0) {
                langSysRecords = new LangSysRecord[langSysCount];
                for (int i = 0; i < langSysCount; i++) {
                    langSysRecords[i] = new LangSysRecord(rar);
                }
            }

            // Read the LangSys tables
            if (langSysCount > 0) {
                langSys = new LangSys[langSysCount];
                for (int i = 0; i < langSysCount; i++) {
                    rar.seek(offset + langSysRecords[i].getOffset());
                    langSys[i] = new LangSys(rar);
                }
            }
            if (defaultLangSysOffset > 0) {
                rar.seek(offset + defaultLangSysOffset);
                defaultLangSys = new LangSys(rar);
            }
        }

        /**
         * Returns the default LangSys
         * @return Returns the default LangSys
         */
        public LangSys getDefaultLangSys() {

            return defaultLangSys;
        }

        /**
         * Returns the defaultLangSysOffset.
         * @return Returns the defaultLangSysOffset.
         */
        public int getDefaultLangSysOffset() {

            return defaultLangSysOffset;
        }

        /**
         * Returns the langSys.
         * @return Returns the langSys.
         */
        public LangSys[] getLangSys() {

            return langSys;
        }

        /**
         * @return Returns the langSysCount.
         */
        public int getLangSysCount() {

            return langSysCount;
        }

        /**
         * Returns the langSysRecords.
         * @return Returns the langSysRecords.
         */
        public LangSysRecord[] getLangSysRecords() {

            return langSysRecords;
        }

        /**
         * Returns the info for this class
         * @return Returns the info for this class
         */
        public String toString() {

            StringBuffer buf = new StringBuffer();
            buf.append("Script\n");
            buf.append("   langSyscount : " + String.valueOf(langSysCount)
                    + '\n');
            return buf.toString();
        }
    }

    /**
     * langsys
     */
    public static class LangSys {

        /**
         * lookup order
         */
        private int lookupOrder;

        /**
         * reg feature index
         */
        private int reqFeatureIndex;

        /**
         * feature count
         */
        private int featureCount;

        /**
         * feature index
         */
        private int[] featureIndex;

        /**
         * Create a new object
         *
         * @param rar       input
         * @throws IOException if an IO-error occurs
         */
        LangSys(final RandomAccessR rar) throws IOException {

            lookupOrder = rar.readUnsignedShort();
            reqFeatureIndex = rar.readUnsignedShort();
            featureCount = rar.readUnsignedShort();
            featureIndex = new int[featureCount];
            for (int i = 0; i < featureCount; i++) {
                featureIndex[i] = rar.readUnsignedShort();
            }
        }

        /**
         * Check, if is a feature index
         * @param n the index
         * @return  Returns <code>true</code>, if is a feature index,
         *          otherwise <code>false</code>
         */
        public boolean isFeatureIndexed(final int n) {

            for (int i = 0; i < featureCount; i++) {
                if (featureIndex[i] == n) {
                    return true;
                }
            }
            return false;
        }

        /**
         * @return Returns the featureCount.
         */
        public int getFeatureCount() {

            return featureCount;
        }

        /**
         * @return Returns the featureIndex.
         */
        public int[] getFeatureIndex() {

            return featureIndex;
        }

        /**
         * @return Returns the lookupOrder.
         */
        public int getLookupOrder() {

            return lookupOrder;
        }

        /**
         * @return Returns the reqFeatureIndex.
         */
        public int getReqFeatureIndex() {

            return reqFeatureIndex;
        }

        /**
         * Returns the info for this class
         * @return Returns the info for this class
         */
        public String toString() {

            StringBuffer buf = new StringBuffer();
            buf.append("LangSys\n");
            buf.append("   feature count : ").append(featureCount).append('\n');
            return buf.toString();
        }
    }

    /**
     * langsysrecord
     */
    public class LangSysRecord {

        /**
         * tag
         */
        private int tag;

        /**
         * offset
         */
        private int offset;

        /**
         * Create a new object
         *
         * @param rar       input
         * @throws IOException if an IO-error occurs
         */
        LangSysRecord(final RandomAccessR rar) throws IOException {

            tag = rar.readInt();
            offset = rar.readUnsignedShort();
        }

        /**
         * Returns the offset.
         * @return Returns the offset.
         */
        public int getOffset() {

            return offset;
        }

        /**
         * Returns the tag.
         * @return Returns the tag.
         */
        public int getTag() {

            return tag;
        }

        /**
         * Returns the info for this class
         * @return Returns the info for this class
         */
        public String toString() {

            StringBuffer buf = new StringBuffer();
            buf.append("LangSysRecord\n");
            buf.append("   tag    : " + String.valueOf(tag) + '\n');
            buf.append("   offset : " + String.valueOf(offset) + '\n');
            return buf.toString();
        }

    }
}

