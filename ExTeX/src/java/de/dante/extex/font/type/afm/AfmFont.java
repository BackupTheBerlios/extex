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

package de.dante.extex.font.type.afm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.jdom.Element;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.type.FontMetric;
import de.dante.extex.font.type.afm.exception.AfmMissingEndCharMetricsException;
import de.dante.extex.font.type.afm.exception.AfmMissingEndFontMetricsException;
import de.dante.extex.font.type.afm.exception.AfmMissingEndKernPairsException;
import de.dante.extex.font.type.afm.exception.AfmMissingStartCharMetricsException;
import de.dante.extex.font.type.afm.exception.AfmNoBoundingBoxFoundException;
import de.dante.util.XMLConvertible;

/**
 * This class read a AFM-file and create a efm-element.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class AfmFont implements FontMetric, Serializable, XMLConvertible {

    /**
     * the fontname
     */
    private String fontname;

    /**
     * init
     * @param afmin     the input
     * @param fname     the font name
     * @throws IOException if an IO-error occurs.
     * @throws FontException if an font error occurs.
     */
    public AfmFont(final InputStream afmin, final String fname)
            throws IOException, FontException {

        fontname = fname;

        // create a Reader (AFM use US_ASCII)
        BufferedReader reader = new BufferedReader(new InputStreamReader(afmin,
                "US-ASCII"));
        header = new AfmHeader();
        readAFMFile(reader);
        reader.close();
        afmin.close();

    }

    /**
     * the header container
     */
    private AfmHeader header;

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
     * @throws IOException    if an io-error occurs.
     * @throws FontException  if a font error occurs.
     */
    private void readAFMFile(final BufferedReader reader) throws IOException,
            FontException {

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
                header.setFontname(tok.nextToken("\u00ff").substring(1));
            } else if (command.equals("FullName")) {
                header.setFullname(tok.nextToken("\u00ff").substring(1));
            } else if (command.equals("FamilyName")) {
                header.setFamilyname(tok.nextToken("\u00ff").substring(1));
            } else if (command.equals("Weight")) {
                header.setWeight(tok.nextToken("\u00ff").substring(1));
            } else if (command.equals("ItalicAngle")) {
                header.setItalicangle(Float.valueOf(tok.nextToken())
                        .floatValue());
            } else if (command.equals("IsFixedPitch")) {
                header.setFixedpitch(tok.nextToken().equals("true"));
            } else if (command.equals("CharacterSet")) {
                header.setCharacterset(tok.nextToken("\u00ff").substring(1));
            } else if (command.equals("FontBBox")) {
                header.setLlx((int) Float.valueOf(removeComma(tok.nextToken()))
                        .floatValue());
                header.setLly((int) Float.valueOf(removeComma(tok.nextToken()))
                        .floatValue());
                header.setUrx((int) Float.valueOf(removeComma(tok.nextToken()))
                        .floatValue());
                header.setUry((int) Float.valueOf(removeComma(tok.nextToken()))
                        .floatValue());
            } else if (command.equals("UnderlinePosition")) {
                header.setUnderlineposition((int) Float
                        .valueOf(tok.nextToken()).floatValue());
            } else if (command.equals("UnderlineThickness")) {
                header.setUnderlinethickness((int) Float.valueOf(
                        tok.nextToken()).floatValue());
            } else if (command.equals("EncodingScheme")) {
                header.setEncodingscheme(tok.nextToken("\u00ff").substring(1));
            } else if (command.equals("CapHeight")) {
                header.setCapheight((int) Float.valueOf(tok.nextToken())
                        .floatValue());
            } else if (command.equals("XHeight")) {
                header.setXheight((int) Float.valueOf(tok.nextToken())
                        .floatValue());
            } else if (command.equals("Ascender")) {
                header.setAscender((int) Float.valueOf(tok.nextToken())
                        .floatValue());
            } else if (command.equals("Descender")) {
                header.setDescender((int) Float.valueOf(tok.nextToken())
                        .floatValue());
            } else if (command.equals("StdHW")) {
                header.setStdhw((int) Float.valueOf(tok.nextToken())
                        .floatValue());
            } else if (command.equals("StdVW")) {
                header.setStdvw((int) Float.valueOf(tok.nextToken())
                        .floatValue());
            } else if (command.equals("StartCharMetrics")) {
                isMetrics = true;
                break;
            }
        }
        // metric not found
        if (!isMetrics) {
            throw new AfmMissingStartCharMetricsException();
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
            throw new AfmMissingEndFontMetricsException();
        }

        // read KernPairs
        while ((line = reader.readLine()) != null) {
            StringTokenizer tok = new StringTokenizer(line);
            if (!tok.hasMoreTokens()) {
                continue;
            }
            String ident = tok.nextToken();
            if (ident.equals("KPX")) {
                AfmKernPairs kp = new AfmKernPairs();
                kp.setCharpre(tok.nextToken());
                kp.setCharpost(tok.nextToken());
                kp.setKerningsize(tok.nextToken());
                afmKerningPairs.add(kp);

            } else if (ident.equals("EndKernPairs")) {
                isMetrics = false;
                break;
            }
        }
        if (isMetrics) {
            throw new AfmMissingEndKernPairsException();
        }
    }

    /**
     * Create Metric
     * @param reader        the reader
     * @param ism           ismetric
     * @return  ismetric
     * @throws IOException if an IO-error occurs
     * @throws FontException if a font-error occurs.
     */
    private boolean createMetric(final BufferedReader reader, final boolean ism)
            throws IOException, FontException {

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
            AfmCharMetric cm = new AfmCharMetric();

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
            throw new AfmMissingEndCharMetricsException();
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
        }
        return "notdef_" + n;
    }

    /**
     * @see de.dante.extex.font.type.FontMetric#getFontMetric()
     */
    public Element getFontMetric() throws FontException {

        // create efm-file
        Element root = new Element("fontgroup");
        root.setAttribute("name", fontname);
        root.setAttribute("id", fontname);
        root.setAttribute("empr", "100");

        Element efontdimen = new Element("fontdimen");
        root.addContent(efontdimen);

        Element font = new Element("font");
        font.setAttribute("type", "type1");
        root.addContent(font);

        font.setAttribute("font-name", header.getFontname());
        font.setAttribute("font-fullname", header.getFullname());
        font.setAttribute("font-family", header.getFamilyname());
        font.setAttribute("font-weight", header.getWeight());

        root.setAttribute("units-per-em", "1000");
        root.setAttribute("bbox", String.valueOf(header.getLlx()) + ' '
                + String.valueOf(header.getLly()) + ' '
                + String.valueOf(header.getUrx()) + ' '
                + String.valueOf(header.getUry()));
        if (header.getUnderlinethickness() != 0) {
            efontdimen.setAttribute("underline-position", String.valueOf(header
                    .getUnderlineposition()));
            efontdimen.setAttribute("underline-thickness", String
                    .valueOf(header.getUnderlinethickness()));
        }
        efontdimen.setAttribute("xheight", String.valueOf(header.getXheight()));
        efontdimen.setAttribute("capheight", String.valueOf(header
                .getCapheight()));

        for (int i = 0; i < afmCharMetrics.size(); i++) {

            // create  glyph
            Element glyph = new Element("glyph");

            // get the AFMCharMertix-object
            AfmCharMetric cm = (AfmCharMetric) afmCharMetrics.get(i);

            // create attributes
            if (cm.getC() >= 0) {
                glyph.setAttribute("ID", String.valueOf(cm.getC()));
            } else {
                glyph.setAttribute("ID", "notdef_" + cm.getN());
            }
            glyph.setAttribute("glyph-number", String.valueOf(cm.getC()));
            glyph.setAttribute("glyph-name", cm.getN());

            if (cm.getWx() != AfmHeader.NOTINIT) {
                glyph.setAttribute("width", String.valueOf(cm.getWx()));
            } else {
                // calculate with from bbox
                if (cm.getBllx() != AfmHeader.NOTINIT) {
                    glyph.setAttribute("width", String.valueOf(cm.getBllx()
                            + cm.getBurx()));
                }
            }

            if (cm.getBllx() != AfmHeader.NOTINIT) {
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
                throw new AfmNoBoundingBoxFoundException(String.valueOf(cm
                        .getC()));
            }
            glyph.setAttribute("italic", String
                    .valueOf(header.getItalicangle()));

            // kerning
            String glyphname = glyph.getAttributeValue("glyph-name");
            AfmKernPairs kp;

            for (int k = 0; k < afmKerningPairs.size(); k++) {
                kp = (AfmKernPairs) afmKerningPairs.get(k);
                if (kp.getCharpre().equals(glyphname)) {
                    Element kerning = new Element("kerning");
                    kerning.setAttribute("glyph-name", kp.getCharpost());
                    kerning.setAttribute("glyph-id", getIDforName(kp
                            .getCharpost()));
                    kerning.setAttribute("size", kp.getKerningsize());
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
     * Returns the fontname.
     * @return Returns the fontname.
     */
    public String getFontname() {

        return fontname;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element root = new Element("afm");
        root.setAttribute("name", fontname);
        root.addContent(header.toXML());
        for (int i = 0; i < afmCharMetrics.size(); i++) {

            // create  glyph
            Element glyph = new Element("glyph");

            // get the AFMCharMertix-object
            AfmCharMetric cm = (AfmCharMetric) afmCharMetrics.get(i);

            // create attributes
            if (cm.getC() >= 0) {
                glyph.setAttribute("ID", String.valueOf(cm.getC()));
            } else {
                glyph.setAttribute("ID", "notdef_" + cm.getN());
            }
            glyph.setAttribute("glyph-number", String.valueOf(cm.getC()));
            glyph.setAttribute("glyph-name", cm.getN());

            if (cm.getWx() != AfmHeader.NOTINIT) {
                glyph.setAttribute("width", String.valueOf(cm.getWx()));
            } else {
                // calculate with from bbox
                if (cm.getBllx() != AfmHeader.NOTINIT) {
                    glyph.setAttribute("width", String.valueOf(cm.getBllx()
                            + cm.getBurx()));
                }
            }

            if (cm.getBllx() != AfmHeader.NOTINIT) {
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
            }
            glyph.setAttribute("italic", String
                    .valueOf(header.getItalicangle()));

            // kerning
            String glyphname = glyph.getAttributeValue("glyph-name");
            AfmKernPairs kp;

            for (int k = 0; k < afmKerningPairs.size(); k++) {
                kp = (AfmKernPairs) afmKerningPairs.get(k);
                if (kp.getCharpre().equals(glyphname)) {
                    Element kerning = new Element("kerning");
                    kerning.setAttribute("glyph-name", kp.getCharpost());
                    kerning.setAttribute("glyph-id", getIDforName(kp
                            .getCharpost()));
                    kerning.setAttribute("size", kp.getKerningsize());
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
            root.addContent(glyph);
        }

        return root;
    }
}