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

package de.dante.extex.font.type.afm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.jdom.Element;

import de.dante.extex.font.type.FontMetric;

/**
 * This class read a AFM-file and create a efm-element.
 *
 * <h1>Element and Attrtibutes</h1>
 * <table border="1">
 *  <tr><td><b>fontgroup</b></td><td></td>
 *          <td>Describe a font</td></tr>
 *          <tr><td></td><td>name</td>
 *          <td>The name of the font</td></tr>
 *          <tr><td></td><td>id</td>
 *          <td>The name of the font</td></tr>
 *          <tr><td></td><td>default-size</td>
 *          <td>The name of the font</td></tr>
 *          <tr><td></td><td>empr</td>
 *          <td>The size of em in percent (default 100%)</td></tr>
 *          <tr><td></td><td>units-per-em</td>
 *          <td>Units per em (default 1000)</td></tr>
 *          <tr><td></td><td>bbox</td>
 *          <td>The boundingbox</td></tr>
 *          <tr><td></td><td>underline-position</td>
 *          <td>The underline position</td></tr>
 *          <tr><td></td><td>underline-thickness</td>
 *          <td>The thickness of the underline</td></tr>
 *          <tr><td></td><td>xheight</td>
 *          <td>The hight of x</td></tr>
 *          <tr><td></td><td>capheight</td>
 *          <td>The hight of the cap</td></tr>
 *  <tr><td><b>font</b></td><td></td>
 *          <td>Describe one font of a group</td></tr>
 *          <tr><td></td><td>type</td>
 *          <td>The font-type (type1)</td></tr>
 *          <tr><td></td><td>filename</td>
 *          <td>The anme of the pfb-file</td></tr>
 *          <tr><td></td><td>font-name</td>
 *          <td>The name of the font</td></tr>
 *          <tr><td></td><td>font-fullname</td>
 *          <td>The fullname of the font</td></tr>
 *          <tr><td></td><td>font-family</td>
 *          <td>The family-name</td></tr>
 *          <tr><td></td><td>font-weight</td>
 *          <td>The weight of the font</td></tr>
 *  <tr><td><b>glyph</b></td><td></td>
 *          <td>Describe the glyph</td></tr>
 *          <tr><td></td><td>ID</td>
 *          <td>The ID of the glyph</td></tr>
 *          <tr><td></td><td>glyph-number</td>
 *          <td>The number of the glyph</td></tr>
 *          <tr><td></td><td>glyph-name</td>
 *          <td>The name of the glyph</td></tr>
 *          <tr><td></td><td>width</td>
 *          <td>The width of the glyph</td></tr>
 *          <tr><td></td><td>depth</td>
 *          <td>The depth of the glyph</td></tr>
 *          <tr><td></td><td>height</td>
 *          <td>The height of the glyph</td></tr>
 *          <tr><td></td><td>italic</td>
 *          <td>The italic of the glyph</td></tr>
 *  <tr><td><b>kerning</b></td><td></td>
 *          <td>The kerning of a glyph</td></tr>
 *          <tr><td></td><td>glyph-name</td>
 *          <td>The name of the glyph</td></tr>
 *          <tr><td></td><td>glyph-id</td>
 *          <td>The id of the glyph</td></tr>
 *          <tr><td></td><td>size</td>
 *          <td>The kerning-size</td></tr>
 *  <tr><td><b>ligature</b></td><td></td>
 *          <td>The ligature of a glyph</td></tr>
 *          <tr><td></td><td>letter</td>
 *          <td>The following letter</td></tr>
 *          <tr><td></td><td>letter-id</td>
 *          <td>The following letter-id</td></tr>
 *          <tr><td></td><td>lig</td>
 *          <td>The ligature</td></tr>
 *          <tr><td></td><td>lig-id</td>
 *          <td>The ligature-id</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class AFMReader implements FontMetric {

    /**
     * name of the pfb-file
     */
    private String pfbname;

    /**
     * defaultsize
     */
    private String defaultsize;

    /**
     * init
     * @param afmin          Stream for Reading the afm-file
     * @param pfbName        the name of the pfbfile
     * @param defaultSize    the defaultsize for the font
     * @throws IOException ...
     */
    public AFMReader(final InputStream afmin, final String pfbName,
            final String defaultSize) throws IOException {

        pfbname = pfbName;
        defaultsize = defaultSize;

        // create a Reader (AFM use US_ASCII)
        BufferedReader reader = new BufferedReader(new InputStreamReader(afmin,
                "US-ASCII"));
        readAFMFile(reader);
        reader.close();
        afmin.close();

        efmelement = createFontMetric();
    }

    /**
     * The Postscript font name.
     */
    private String afmFontName = "";

    /**
     * The full name of the font.
     */
    private String afmFullName = "";

    /**
     * The family name of the font.
     */
    private String afmFamilyName = "";

    /**
     * The weight of the font: normal, bold, etc.
     */
    private String afmWeight = "";

    /**
     * The italic angle of the font, usually 0.0 or negative.
     */
    private float afmItalicAngle = 0.0f;

    /**
     * <code>true</code> if all the characters have the same width.
     */
    private boolean afmIsFixedPitch = false;

    /**
     * The character set of the font.
     */
    private String afmCharacterSet = "";

    /**
     * not init
     */
    private static final int NOTINIT = -9999;

    /**
     * The llx of the FontBox.
     */
    private int afmllx = NOTINIT;

    /**
     * The lly of the FontBox.
     */
    private int afmlly = NOTINIT;

    /**
     * The lurx of the FontBox.
     */
    private int afmurx = NOTINIT;

    /**
     * The ury of the FontBox.
     */
    private int afmury = NOTINIT;

    /**
     * The underline position.
     */
    private int afmUnderlinePosition = 0;

    /**
     * The underline thickness.
     */
    private int afmUnderlineThickness = 0;

    /**
     * The font's encoding name.
     * This encoding is
     * - StandardEncoding
     * - AdobeStandardEncoding
     * - For all other names the font is treated as symbolic.
     ?     */
    private String afmEncodingScheme = "FontSpecific";

    /**
     * CapHeight
     */
    private int afmCapHeight = 0;

    /**
     * XHeight
     */
    private int afmXHeight = 0;

    /**
     * Ascender
     */
    private int afmAscender = 0;

    /**
     * Descender
     */
    private int afmDescender = 0;

    /**
     * StdHW
     */
    private int afmStdHW;

    /**
     * StdVW
     */
    private int afmStdVW = 0;

    /**
     * initsize for the ArrayList
     */
    private static final int ARRAYLISTINITSIZE = 256;

    /**
     * Represents the section CharMetrics in the AFM file.
     */
    private ArrayList afmCharMetrics = new ArrayList(ARRAYLISTINITSIZE);

    /**
     * Represents the section KerningPairs in the AFM file.
     */
    private ArrayList afmKerningPairs = new ArrayList(ARRAYLISTINITSIZE);

    /**
     * Char-Name - Char-Number
     */
    private HashMap afmCharNameNumber = new HashMap(ARRAYLISTINITSIZE);

    /**
     * base 16
     */
    private static final int BASEHEX = 16;

    /**
     * Read the AFM-File
     * @param reader          the Reader fore the fileinput
     * @throws IOException    if an io-error are throws
     */
    private void readAFMFile(final BufferedReader reader) throws IOException {

        // line
        String line;

        //read first the AFM-header and then the metrics
        boolean isMetrics = false;

        while ((line = reader.readLine()) != null) {

            // get the token from the line
            StringTokenizer tok = new StringTokenizer(line);

            // no more tokens
            if (!tok.hasMoreTokens()) {
                continue;
            }

            // read the command
            String command = tok.nextToken();

            // check the command
            if (command.equals("Comment")) {
                continue;
            } else if (command.equals("Notice")) {
                continue;
            } else if (command.equals("FontName")) {
                afmFontName = tok.nextToken("\u00ff").substring(1);
            } else if (command.equals("FullName")) {
                afmFullName = tok.nextToken("\u00ff").substring(1);
            } else if (command.equals("FamilyName")) {
                afmFamilyName = tok.nextToken("\u00ff").substring(1);
            } else if (command.equals("Weight")) {
                afmWeight = tok.nextToken("\u00ff").substring(1);
            } else if (command.equals("ItalicAngle")) {
                afmItalicAngle = Float.valueOf(tok.nextToken()).floatValue();
            } else if (command.equals("IsFixedPitch")) {
                afmIsFixedPitch = tok.nextToken().equals("true");
            } else if (command.equals("CharacterSet")) {
                afmCharacterSet = tok.nextToken("\u00ff").substring(1);
            } else if (command.equals("FontBBox")) {
                afmllx = (int) Float.valueOf(removeComma(tok.nextToken()))
                        .floatValue();
                afmlly = (int) Float.valueOf(removeComma(tok.nextToken()))
                        .floatValue();
                afmurx = (int) Float.valueOf(removeComma(tok.nextToken()))
                        .floatValue();
                afmury = (int) Float.valueOf(removeComma(tok.nextToken()))
                        .floatValue();
            } else if (command.equals("UnderlinePosition")) {
                afmUnderlinePosition = (int) Float.valueOf(tok.nextToken())
                        .floatValue();
            } else if (command.equals("UnderlineThickness")) {
                afmUnderlineThickness = (int) Float.valueOf(tok.nextToken())
                        .floatValue();
            } else if (command.equals("EncodingScheme")) {
                afmEncodingScheme = tok.nextToken("\u00ff").substring(1);
            } else if (command.equals("CapHeight")) {
                afmCapHeight = (int) Float.valueOf(tok.nextToken())
                        .floatValue();
            } else if (command.equals("XHeight")) {
                afmXHeight = (int) Float.valueOf(tok.nextToken()).floatValue();
            } else if (command.equals("Ascender")) {
                afmAscender = (int) Float.valueOf(tok.nextToken()).floatValue();
            } else if (command.equals("Descender")) {
                afmDescender = (int) Float.valueOf(tok.nextToken())
                        .floatValue();
            } else if (command.equals("StdHW")) {
                afmStdHW = (int) Float.valueOf(tok.nextToken()).floatValue();
            } else if (command.equals("StdVW")) {
                afmStdVW = (int) Float.valueOf(tok.nextToken()).floatValue();
            } else if (command.equals("StartCharMetrics")) {
                isMetrics = true;
                break;
            }
        }
        // metric not found
        if (!isMetrics) {
            System.err.println("Missing StartCharMetrics");
            System.exit(1);
        }

        // create metric
        isMetrics = createMetric(reader, isMetrics);

        // read  next command
        while ((line = reader.readLine()) != null) {
            StringTokenizer tok = new StringTokenizer(line);
            if (!tok.hasMoreTokens()) {
                continue;
            }
            String ident = tok.nextToken();
            if (ident.equals("EndFontMetrics")) {
                // end
                return;
            }
            if (ident.equals("StartKernPairs")) {
                isMetrics = true;
                break;
            }
        }
        if (!isMetrics) {
            System.err.println("Missing EndFontMetrics");
            System.exit(1);
        }

        // read KernPairs
        while ((line = reader.readLine()) != null) {
            StringTokenizer tok = new StringTokenizer(line);
            if (!tok.hasMoreTokens()) {
                continue;
            }
            String ident = tok.nextToken();
            if (ident.equals("KPX")) {
                KernPairs kp = new KernPairs();
                kp.charpre = tok.nextToken();
                kp.charpost = tok.nextToken();
                kp.kerningsize = tok.nextToken();
                afmKerningPairs.add(kp);

            } else if (ident.equals("EndKernPairs")) {
                isMetrics = false;
                break;
            }
        }
        if (isMetrics) {
            System.err.println("Missing EndKernPairs");
            System.exit(1);
        }
    }

    /**
     * Create Metric
     * @param reader        the reader
     * @param ism           ismetric
     * @return  ismetric
     * @throws IOException ...
     */
    private boolean createMetric(final BufferedReader reader, final boolean ism)
            throws IOException {

        boolean isMetrics = ism;

        String line;
        // read the metric
        while ((line = reader.readLine()) != null) {

            // get the token from the line
            StringTokenizer tok = new StringTokenizer(line);

            // no more tokens
            if (!tok.hasMoreTokens()) {
                continue;
            }

            // get the command
            String command = tok.nextToken();
            if (command.equals("EndCharMetrics")) {
                isMetrics = false;
                break;
            }

            // default values
            AFMCharMetric cm = new AFMCharMetric();

            // get the token separate bei ';'
            tok = new StringTokenizer(line, ";");
            while (tok.hasMoreTokens()) {
                StringTokenizer tokc = new StringTokenizer(tok.nextToken());
                if (!tokc.hasMoreTokens()) {
                    continue;
                }
                command = tokc.nextToken();

                // command ?
                if (command.equals("C")) {
                    cm.setC(Integer.parseInt(tokc.nextToken()));
                } else if (command.equals("CH")) {
                    cm.setC(Integer.parseInt(tokc.nextToken(), BASEHEX));
                } else if (command.equals("WX")) {
                    cm.setWx(Float.valueOf(tokc.nextToken()).intValue());
                } else if (command.equals("N")) {
                    cm.setN(tokc.nextToken());
                } else if (command.equals("B")) {
                    cm.setBllx((int) Float.valueOf(tokc.nextToken())
                            .floatValue());
                    cm.setBlly((int) Float.valueOf(tokc.nextToken())
                            .floatValue());
                    cm.setBurx((int) Float.valueOf(tokc.nextToken())
                            .floatValue());
                    cm.setBury((int) Float.valueOf(tokc.nextToken())
                            .floatValue());
                } else if (command.equals("L")) {
                    cm.addL(tokc.nextToken().trim(), tokc.nextToken().trim());
                }
            }
            afmCharMetrics.add(cm);

            // store name and number
            if (afmCharNameNumber.containsKey(cm.getN())) {
                if (cm.getC() != -1) {
                    afmCharNameNumber.put(cm.getN(), new Integer(cm.getC()));
                }
            } else {
                afmCharNameNumber.put(cm.getN(), new Integer(cm.getC()));
            }
        }

        // metric close?
        if (isMetrics) {
            System.err.println("Missing EndCharMetrics");
            System.exit(1);
        }
        return isMetrics;
    }

    /**
     * Remove all ',' in the string, if the string is <code>null</code>, a
     * empty string is returned.
     * @param   s   the string
     * @return the string without a ','
     */
    private String removeComma(final String s) {

        if (s != null) {
            return s.replaceAll(",", "");
        }
        return "";
    }

    /**
     * container for KerningPair
     */
    private class KernPairs {

        /**
         * pre
         */
        private String charpre;

        /**
         * post
         */
        private String charpost;

        /**
         * kerningsize
         */
        private String kerningsize;

        /**
         * @return Returns the kerningsize.
         */
        public String getKerningsize() {

            return kerningsize;
        }

        /**
         * @param ksize The kerningsize to set.
         */
        public void setKerningsize(final String ksize) {

            kerningsize = ksize;
        }

        /**
         * @return Returns the charpost.
         */
        public String getCharpost() {

            return charpost;
        }

        /**
         * @param cp The charpost to set.
         */
        public void setCharpost(final String cp) {

            charpost = cp;
        }

        /**
         * @return Returns the charpre.
         */
        public String getCharpre() {

            return charpre;
        }

        /**
         * @param cp The charpre to set.
         */
        public void setCharpre(final String cp) {

            charpre = cp;
        }
    }

    /**
     * Container for the AFM-CharMetrik
     */
    private class AFMCharMetric {

        /**
         * C
         */
        private int c = -1;

        /**
         * WX
         */
        private int wx = NOTINIT;

        /**
         * Name
         */
        private String n = "";

        /**
         * B llx
         */
        private int bllx = NOTINIT;

        /**
         * B lly
         */
        private int blly = NOTINIT;

        /**
         * B urx
         */
        private int burx = NOTINIT;

        /**
         * B ury
         */
        private int bury = NOTINIT;

        /**
         * Ligatur
         */
        private HashMap l = null;

        /**
         * Add a ligature
         * @param letter    the basic letter
         * @param lig        the ligature
         */
        public void addL(final String letter, final String lig) {

            if (l == null) {
                l = new HashMap();
            }
            l.put(letter, lig);
        }

        /**
         * @return Returns the bllx.
         */
        public int getBllx() {

            return bllx;
        }

        /**
         * @param ibllx The bllx to set.
         */
        public void setBllx(final int ibllx) {

            bllx = ibllx;
        }

        /**
         * @return Returns the blly.
         */
        public int getBlly() {

            return blly;
        }

        /**
         * @param iblly The blly to set.
         */
        public void setBlly(final int iblly) {

            blly = iblly;
        }

        /**
         * @return Returns the burx.
         */
        public int getBurx() {

            return burx;
        }

        /**
         * @param iburx The burx to set.
         */
        public void setBurx(final int iburx) {

            burx = iburx;
        }

        /**
         * @return Returns the bury.
         */
        public int getBury() {

            return bury;
        }

        /**
         * @param ibury The bury to set.
         */
        public void setBury(final int ibury) {

            bury = ibury;
        }

        /**
         * @return Returns the c.
         */
        public int getC() {

            return c;
        }

        /**
         * @param ic The c to set.
         */
        public void setC(final int ic) {

            c = ic;
        }

        /**
         * @return Returns the l.
         */
        public HashMap getL() {

            return l;
        }

        /**
         * @return Returns the n.
         */
        public String getN() {

            return n;
        }

        /**
         * @param s The n to set.
         */
        public void setN(final String s) {

            n = s;
        }

        /**
         * @return Returns the wx.
         */
        public int getWx() {

            return wx;
        }

        /**
         * @param iwx The wx to set.
         */
        public void setWx(final int iwx) {

            wx = iwx;
        }
    }

    /**
     * LineFeed
     */
    private static final char LF = '\n';

    /**
     * remove the fileextension and path, if exists
     * @param   file    the filename
     * @return the filename without extension and path
     */
    private String filenameWithoutExtensionAndPath(final String file) {

        String rt = file;
        int i = file.lastIndexOf(".");
        if (i > 0) {
            rt = file.substring(0, i);
        }
        i = rt.lastIndexOf(File.separator);
        if (i > 0) {
            rt = rt.substring(i + 1);
        }
        return rt;
    }

    /**
     * remove the path, if exists
     * @param  file the filename
     * @return  the filename without the path
     */
    private String filenameWithoutPath(final String file) {

        String rt = file;
        int i = rt.lastIndexOf(File.separator);
        if (i > 0) {
            rt = rt.substring(i + 1);
        }
        return rt;
    }

    /**
     * Return the id for a charname
     * @param   name    the name of char
     * @return the id
     */
    private String getIDforName(final String name) {

        int id = -1;

        String n = name;

        if (name != null) {
            Integer i = (Integer) afmCharNameNumber.get(name);
            if (i != null) {
                id = i.intValue();
            }
        } else {
            n = "null";
        }

        if (id >= 0) {
            return String.valueOf(id);
        } else {
            return "notdef_" + n;
        }
    }

    /**
     * efm-Element
     */
    private Element efmelement;

    /**
     * @see de.dante.util.font.FontMetric#getFontMetric()
     */
    private Element createFontMetric() throws IOException {

        // create efm-file
        Element root = new Element("fontgroup");
        root.setAttribute("name", filenameWithoutExtensionAndPath(pfbname));
        root.setAttribute("id", filenameWithoutExtensionAndPath(pfbname));
        root.setAttribute("default-size", defaultsize);
        root.setAttribute("empr", "100");

        Element efontdimen = new Element("fontdimen");
        root.addContent(efontdimen);

        Element font = new Element("font");
        font.setAttribute("type", "type1");
        font.setAttribute("filename", filenameWithoutPath(pfbname));
        root.addContent(font);

        font.setAttribute("font-name", afmFontName);
        font.setAttribute("font-fullname", afmFullName);
        font.setAttribute("font-family", afmFamilyName);
        font.setAttribute("font-weight", afmWeight);

        root.setAttribute("units-per-em", "1000");
        root.setAttribute("bbox", String.valueOf(afmllx) + ' '
                + String.valueOf(afmlly) + ' ' + String.valueOf(afmurx) + ' '
                + String.valueOf(afmury));
        if (afmUnderlineThickness != 0) {
            efontdimen.setAttribute("underline-position", String
                    .valueOf(afmUnderlinePosition));
            efontdimen.setAttribute("underline-thickness", String
                    .valueOf(afmUnderlineThickness));
        }
        efontdimen.setAttribute("xheight", String.valueOf(afmXHeight));
        efontdimen.setAttribute("capheight", String.valueOf(afmCapHeight));

        for (int i = 0; i < afmCharMetrics.size(); i++) {

            // create  glyph
            Element glyph = new Element("glyph");

            // get the AFMCharMertix-object
            AFMCharMetric cm = (AFMCharMetric) afmCharMetrics.get(i);

            // create attributes
            if (cm.getC() >= 0) {
                glyph.setAttribute("ID", String.valueOf(cm.getC()));
            } else {
                glyph.setAttribute("ID", "notdef_" + cm.getN());
            }
            glyph.setAttribute("glyph-number", String.valueOf(cm.getC()));
            glyph.setAttribute("glyph-name", cm.getN());

            if (cm.getWx() != NOTINIT) {
                glyph.setAttribute("width", String.valueOf(cm.getWx()));
            } else {
                // calculate with from bbox
                if (cm.getBllx() != NOTINIT) {
                    glyph.setAttribute("width", String.valueOf(cm.getBllx()
                            + cm.getBurx()));
                }
            }

            if (cm.getBllx() != NOTINIT) {
                if (cm.getBlly() < 0) {
                    glyph.setAttribute("depth", String.valueOf(-cm.getBlly()));
                } else {
                    glyph.setAttribute("depth", "0");
                }
                if (cm.getBury() > 0) {
                    glyph.setAttribute("height", String.valueOf(cm.getBury()));
                } else {
                    glyph.setAttribute("height", "0");
                }
            } else {
                throw new IOException("No boundingbox found : " + cm.getC());
            }
            glyph.setAttribute("italic", String.valueOf(afmItalicAngle));

            // kerning
            String glyphname = glyph.getAttributeValue("glyph-name");
            KernPairs kp;

            for (int k = 0; k < afmKerningPairs.size(); k++) {
                kp = (KernPairs) afmKerningPairs.get(k);
                if (kp.charpre.equals(glyphname)) {
                    Element kerning = new Element("kerning");
                    kerning.setAttribute("glyph-name", kp.charpost);
                    kerning.setAttribute("glyph-id", getIDforName(kp.charpost));
                    kerning.setAttribute("size", kp.kerningsize);
                    glyph.addContent(kerning);
                }
            }

            // ligature
            if (cm.getL() != null) {
                Iterator iterator = cm.getL().keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    Element lig = new Element("ligature");
                    lig.setAttribute("letter", key);
                    lig.setAttribute("letter-id", getIDforName(key));
                    String value = (String) cm.getL().get(key);
                    lig.setAttribute("lig", value);
                    lig.setAttribute("lig-id", getIDforName(value));
                    glyph.addContent(lig);
                }
            }

            // add to fontseg
            font.addContent(glyph);
        }
        return root;
    }

    /**
     * @see de.dante.util.font.FontMetric#getFontMetric()
     */
    public Element getFontMetric() {

        return efmelement;
    }
}