/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.util.font;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.dante.extex.ExTeX;
import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.format.afm.AfmCharMetric;
import de.dante.extex.unicodeFont.format.afm.AfmKernPairs;
import de.dante.extex.unicodeFont.format.afm.AfmParser;
import de.dante.extex.unicodeFont.format.pl.PlWriter;
import de.dante.extex.unicodeFont.format.tex.psfontmap.enc.EncReader;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Utilities for a afm file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public final class AfmUtil extends AbstractFontUtil {

    /**
     * Number of glyphs.
     */
    private static final int NUMBEROFGLYPHS = 256;

    /**
     * Create a xml file.
     */
    private boolean toxml = false;

    /**
     * The xml file name.
     */
    private String xmlname = "";

    /**
     * Create a efm file.
     */
    private boolean toefm = false;

    /**
     * The efm file name.
     */
    private String efmname = "";

    /**
     * The list for the encoding vectors.
     */
    private ArrayList enclist = new ArrayList();

    /**
     * Create encoding vectors.
     */
    private boolean toenc = false;

    /**
     * The name for the encoding vectors.
     */
    private String encname = "";

    /**
     * The directory for the output.
     */
    private String outdir = ".";

    /**
     * Create a map file.
     */
    private boolean tomap = false;

    /**
     * Writer for the map file.
     */
    private BufferedWriter mapout = null;

    /**
     * Create a pl file.
     */
    private boolean topl = false;

    /**
     * Create a new object.
     *
     * @throws ConfigurationException if a config-error occurs.
     */
    private AfmUtil() throws ConfigurationException {

        super(AfmUtil.class);
    }

    /**
     * The afm parser.
     */
    private AfmParser parser;

    /**
     * The afm file.
     */
    private File afmfile;

    /**
     * do it.
     *
     * @param file  The afm file name.
     * @throws Exception if an error occurs.
     */
    private void doIt(final String file) throws Exception {

        InputStream afmin = null;

        // find directly the afm file.
        afmfile = new File(file);

        if (afmfile.canRead()) {
            afmin = new FileInputStream(afmfile);
        } else {
            // use the file finder
            afmin = getFinder().findResource(afmfile.getName(), "");
        }

        if (afmin == null) {
            throw new FileNotFoundException(file);
        }

        parser = new AfmParser(afmin);

        if (toxml) {
            toXml();
        }

        if (toefm) {
            toEfm();
        }

        if (tomap) {
            mapout = new BufferedWriter(new FileWriter(outdir + File.separator
                    + encname + ".map"));
        }
        if (toenc) {
            toEnc();
        }
        if (mapout != null) {
            mapout.close();
        }
    }

    /**
     * Create encoding vectors.
     * @throws IOException if an IO-error occurred.
     * @throws ConfigurationException from the configuration system.
     * @throws FontException if a font error occurred.
     */
    private void toEnc() throws IOException, ConfigurationException,
            FontException {

        // read all glyphs from the encoding vectors
        ArrayList readenc = new ArrayList();
        readAllGlyphName(readenc);

        // get the glpyh names form the afm file.
        ArrayList names = readGlyphNames();
        Collections.sort(names);

        // remove all names from readenc in names
        removeExistingNames(readenc, names);

        createEncFiles(names);

    }

    /**
     * Create the encoding files.
     * @param names     The names for the files.
     * @throws IOException if a IO-error occurred.
     */
    private void createEncFiles(final ArrayList names) throws IOException {

        int cnt = 0;
        int filecnt = 0;
        char filechar = 'a';
        BufferedWriter out = null;
        for (int i = 0, n = names.size(); i < n; i++) {
            if (cnt == 0) {
                String na = encname + filechar;
                File newenc = new File(outdir + File.separator + na + ".enc");
                out = new BufferedWriter(new FileWriter(newenc));
                out.write("% " + createVersion() + "\n");
                out.write("/" + na + "Encoding [\n");

                if (tomap && mapout != null) {
                    mapout.write(na);
                    mapout.write(" ");
                    mapout.write(parser.getHeader().getFontname());
                    mapout.write(" ");
                    mapout.write("\" ");
                    mapout.write(na + "Encoding");
                    mapout.write(" \" ");
                    mapout.write("<");
                    mapout.write(na + ".enc");
                    mapout.write(" ");
                    mapout.write("<");
                    mapout.write(afmfile.getName().replaceAll(
                            "\\.[aA][fF][mM]", ".pfb"));
                    mapout.write("\n");
                }

            }
            out.write("% " + cnt++ + "\n");
            out.write("/" + names.get(i) + "\n");
            if (cnt == NUMBEROFGLYPHS) {
                out.write("] def\n");
                out.close();
                cnt = 0;
                filecnt++;
                filechar = (char) (filechar + 1);
            }
        }
        if (cnt != 0) {
            for (int i = cnt; i < NUMBEROFGLYPHS; i++) {
                out.write("% " + i + "\n");
                out.write("/.notdef\n");
            }
            out.write("] def\n");
            out.close();
        }
        getLogger().severe(
                getLocalizer().format("AfmUtil.EncCreate",
                        String.valueOf(names.size()), String.valueOf(filecnt)));

    }

    /**
     * Remove existing glyph name from the list.
     * @param readenc   The existing glyph names.
     * @param names     The glyph names from the afm file.
     */
    private void removeExistingNames(final ArrayList readenc,
            final ArrayList names) {

        for (int i = 0, n = readenc.size(); i < n; i++) {
            String name = (String) readenc.get(i);
            names.remove(name);
        }
    }

    /**
     * Read the glyph names.
     * @return Returns the glyph name list.
     */
    private ArrayList readGlyphNames() {

        ArrayList cmlist = parser.getAfmCharMetrics();
        ArrayList names = new ArrayList(cmlist.size());

        for (int i = 0, n = cmlist.size(); i < n; i++) {
            AfmCharMetric cm = (AfmCharMetric) cmlist.get(i);
            names.add(cm.getN());
        }
        return names;
    }

    /**
     * Create a pl file.
     * @param enc   The encdoing vector
     * @param name  The name of the pl file.
     * @throws IOException if an IO-error occurred.
     */
    private void createPl(final EncReader enc, final String name)
            throws IOException {

        PlWriter pl = new PlWriter(new FileOutputStream(outdir + File.separator
                + name + ".pl"));

        pl.addComment(createVersion());
        pl.plopen("FAMILY").addStr(
                parser.getHeader().getFontname().toUpperCase()).plclose();
        pl.plopen("CODINGSCHEME").addStr(enc.getEncname().toUpperCase())
                .plclose();
        pl.plopen("DESIGNSIZE").addReal(10.0f).plclose();
        pl.addComment("DESIGNSIZE IS IN POINTS");
        pl.addComment("OTHER SIZES ARE MULTIPLES OF DESIGNSIZE");
        pl.plopen("CHECKSUM").addOct(0).plclose();

        printFontDimen(pl);

        printLigTable(enc, pl);

        printCharacter(enc, pl);

        // close
        pl.close();
    }

    /**
     * Print Character.
     * @param enc   The encoding table.
     * @param pl    The pl writer.
     */
    private void printCharacter(final EncReader enc, final PlWriter pl) {

        String[] table = enc.getTable();
        for (int i = 0; i < table.length; i++) {

            String glyph = table[i].replaceAll("/", "");
            AfmCharMetric cm = parser.getAfmCharMetric(glyph);

            if (cm != null) {
                pl.plopen("CHARACTER").addChar((short) i);
                pl.addComment(cm.getN());
                pl.addCharMetric(scale((long) cm.getWx()), "CHARWD");
                pl.addCharMetric(scale((long) cm.getBury()), "CHARHT");
                pl.addCharMetric(scale(-(long) cm.getBlly()), "CHARDP");
                if (cm.getBurx() > cm.getWx()) {
                    pl.addCharMetric(scale((long) (cm.getBurx() - cm.getWx())),
                            "CHARIC");
                }
                pl.plclose();
            }
        }
    }

    /**
     * Print LigTable.
     * @param enc   The encoding vector.
     * @param pl    The pl writer.
     */
    private void printLigTable(final EncReader enc, final PlWriter pl) {

        pl.plopen("LIGTABLE");

        String[] table = enc.getTable();
        for (int i = 0; i < table.length; i++) {

            String glyph = table[i].replaceAll("/", "");
            AfmCharMetric cm = parser.getAfmCharMetric(glyph);

            if (cm != null && (cm.isKerning() || cm.isLigatur())) {
                pl.plopen("LABEL").addChar((short) i).plclose();
                pl.addComment(cm.getN());
                if (cm.isLigatur()) {
                    HashMap ligmap = cm.getL();
                    Iterator it = ligmap.keySet().iterator();
                    while (it.hasNext()) {
                        String letter = (String) it.next();
                        String lig = (String) ligmap.get(letter);
                        short posletter = (short) enc.getPosition(letter);
                        short poslig = (short) enc.getPosition(lig);
                        if (posletter >= 0 && poslig >= 0) {
                            pl.plopen("LIG").addChar(posletter).addChar(poslig)
                                    .plclose();
                        }
                    }
                }
                if (cm.isKerning()) {
                    List kernlist = cm.getK();
                    for (int k = 0, n = kernlist.size(); k < n; k++) {
                        AfmKernPairs kp = (AfmKernPairs) kernlist.get(k);
                        String pre = kp.getCharpre();
                        String post = kp.getCharpost();
                        float size = kp.getKerningsize();
                        short pospre = (short) enc.getPosition(pre);
                        short pospost = (short) enc.getPosition(post);
                        if (pospre >= 0 && pospost >= 0) {
                            pl.plopen("KRN").addChar(pospost).addReal(
                                    scale((long) size)).plclose();
                        }
                    }
                }
                pl.plopen("STOP").plclose();
            }
        }

        pl.plclose();
    }

    /**
     * Print the FONTDIMEN.
     * @param pl    The pl writer.
     */
    private void printFontDimen(final PlWriter pl) {

        pl.plopen("FONTDIMEN");

        // slant
        double newslant = slant
                - efactor
                * Math.tan(parser.getHeader().getItalicangle()
                        * (Math.PI / 180.0));
        pl.plopen("SLANT").addReal((long) (FIXFACTOR * newslant + 0.5))
                .plclose();

        // space
        int fontspace = 0;
        AfmCharMetric space = parser.getAfmCharMetric("space");
        if (space != null) {
            fontspace = (int) space.getWx();
        } else {
            space = parser.getAfmCharMetric(32);
            if (space != null) {
                fontspace = (int) space.getWx();
            } else {
                fontspace = transform(500, 0);
            }
        }
        pl.plopen("SPACE").addReal(scale(fontspace)).plclose();

        // stretch
        pl.plopen("STRETCH").addReal(
                (parser.getHeader().isFixedpitch()
                        ? 0
                        : scale((long) (300 * efactor + 0.5)))).plclose();
        // shrink
        pl.plopen("SHRINK").addReal(
                (parser.getHeader().isFixedpitch()
                        ? 0
                        : scale((long) (100 * efactor + 0.5)))).plclose();

        // xheight
        pl.plopen("XHEIGHT").addReal(
                scale((long) parser.getHeader().getXheight())).plclose();

        // quad
        pl.plopen("QUAD").addReal(scale((long) (1000 * efactor + 0.5)))
                .plclose();
        pl.plclose();
    }

    /**
     * efactor.
     */
    private float efactor = 1.0f;

    /**
     * slant.
     */
    private float slant = 0.0f;

    /**
     * fixfactor.
     */
    private static final long FIXFACTOR = 0x100000L; // 2^{20}, the unit fixnum

    /**
     * scale (from afm2tfm.c).
     * @param value  The value to scale
     * @return Scale the value.
     */
    private double scale(final long value) {

        //      (((what / 1000) << 20) + (((what % 1000) << 20) + 500) / 1000);
        return value / 1000d;
    }

    /**
     * transform (from afm2tfm.c).
     * @param x
     * @param y
     * @return The transform value.
     */
    private int transform(final int x, final int y) {

        double acc = efactor * x + slant * y;
        return (int) (acc >= 0 ? Math.floor(acc + 0.5) : Math.ceil(acc - 0.5));
    }

    /**
     * Read all glyph names from the encoding vectors.
     *
     * @param readenc   The list for the names.
     * @throws IOException if an IO-error occurred.
     * @throws ConfigurationException from the configuration system.
     * @throws FontException if a font error occurred.
     */
    private void readAllGlyphName(final ArrayList readenc) throws IOException,
            ConfigurationException, FontException {

        for (int i = 0, n = enclist.size(); i < n; i++) {

            String encv = (String) enclist.get(i);

            InputStream encin = null;
            File encfile = new File(encv);
            if (encfile.canRead()) {
                encin = new FileInputStream(encfile);
            } else {
                // use the file finder
                encin = getFinder().findResource(encfile.getName(), "");
            }

            if (encin == null) {
                throw new FileNotFoundException(encv);
            }

            EncReader enc = new EncReader(encin);

            String[] table = enc.getTable();

            for (int k = 0; k < table.length; k++) {
                String name = table[k].replaceAll("/", "");
                readenc.add(name);
            }

            if (tomap && mapout != null) {
                mapout.write(encname);
                mapout.write(encv.replaceAll("\\.[eE][nN][cC]", ""));
                mapout.write(" ");
                mapout.write(parser.getHeader().getFontname());
                mapout.write(" ");
                mapout.write("\" ");
                mapout.write(enc.getEncname());
                mapout.write(" \" ");
                mapout.write("<");
                mapout.write(encv);
                mapout.write(" ");
                mapout.write("<");
                mapout.write(afmfile.getName().replaceAll("\\.[aA][fF][mM]",
                        ".pfb"));
                mapout.write("\n");
            }

            if (tomap) {
                createPl(enc, encname + encv.replaceAll("\\.[eE][nN][cC]", ""));
            }
        }
    }

    /**
     * Export to xml.
     * @throws IOException  if a IO-error occurred.
     */
    private void toXml() throws IOException {

        File xmlfile = new File(outdir + File.separator + xmlname);

        // write to xml-file
        XMLStreamWriter writer = new XMLStreamWriter(new FileOutputStream(
                xmlfile), "ISO-8859-1");
        writer.setBeauty(true);
        writer.writeStartDocument();
        writer.writeComment(createVersion());
        parser.writeXML(writer);
        writer.writeEndDocument();
        writer.close();

        getLogger().severe(getLocalizer().format("AfmUtil.XmlCreate", xmlname));
    }

    /**
     * Export to efm.
     * @throws IOException  if a IO-error occurred.
     */
    private void toEfm() throws IOException {

        File efmfile = new File(outdir + File.separator + efmname);

        // write to efm-file
        XMLStreamWriter writer = new XMLStreamWriter(new FileOutputStream(
                efmfile), "ISO-8859-1");
        writer.setBeauty(true);
        writer.writeStartDocument();
        writer.writeComment(createVersion());
        parser.writeEFM(writer);
        writer.writeEndDocument();
        writer.close();

        getLogger().severe(getLocalizer().format("AfmUtil.EfmCreate", efmname));
    }

    /**
     * Create the comment with ExTeX-version and date.
     * @return Returns the comment with the ExTeX-Version and the date.
     */
    private String createVersion() {

        Calendar cal = Calendar.getInstance();
        return getLocalizer().format("AfmUtil.Version", ExTeX.getVersion(),
                cal.getTime().toString());
    }

    /**
     * parameter.
     */
    private static final int PARAMETER = 1;

    /**
     * main.
     * @param args the command line arguments.
     * @throws Exception if a error occurs.
     */
    public static void main(final String[] args) throws Exception {

        AfmUtil afm = new AfmUtil();

        if (args.length < PARAMETER) {
            afm.getLogger().severe(afm.getLocalizer().format("AfmUtil.Call"));
            System.exit(1);
        }

        boolean toxml = false;
        String xmlname = "";
        boolean toefm = false;
        String efmname = "";
        ArrayList enclist = new ArrayList();
        boolean toenc = false;
        String encname = "";
        boolean tomap = false;
        boolean topl = false;
        String outdir = ".";
        String file = "";

        int i = 0;
        do {
            if ("-x".equals(args[i]) || "--xml".equals(args[i])) {
                if (i + 1 < args.length) {
                    toxml = true;
                    xmlname = args[++i];
                }
            } else if ("-e".equals(args[i]) || "--efm".equals(args[i])) {
                if (i + 1 < args.length) {
                    toefm = true;
                    efmname = args[++i];
                }

            } else if ("-c".equals(args[i]) || "--createenc".equals(args[i])) {
                if (i + 1 < args.length) {
                    toenc = true;
                    encname = args[++i];
                }
            } else if ("-v".equals(args[i]) || "--encvector".equals(args[i])) {
                if (i + 1 < args.length) {
                    enclist.add(args[++i]);
                }
            } else if ("-o".equals(args[i]) || "--outdir".equals(args[i])) {
                if (i + 1 < args.length) {
                    outdir = args[++i];
                }
            } else if ("-m".equals(args[i]) || "--map".equals(args[i])) {
                tomap = true;
            } else if ("-p".equals(args[i]) || "--pl".equals(args[i])) {
                topl = true;
            } else {
                file = args[i];
            }
            i++;
        } while (i < args.length);

        afm.setToxml(toxml);
        afm.setXmlname(xmlname);
        afm.setToefm(toefm);
        afm.setEfmname(efmname);
        afm.setEnclist(enclist);
        afm.setToenc(toenc);
        afm.setEncname(encname);
        afm.setOutdir(outdir);
        afm.setTomap(tomap);

        afm.doIt(file);
    }

    /**
     * Returns the toxml.
     * @return Returns the toxml.
     */
    public boolean isToxml() {

        return toxml;
    }

    /**
     * The toxml to set.
     * @param xml The toxml to set.
     */
    public void setToxml(final boolean xml) {

        toxml = xml;
    }

    /**
     * Returns the xmlname.
     * @return Returns the xmlname.
     */
    public String getXmlname() {

        return xmlname;
    }

    /**
     * The xmlname to set.
     * @param name The xmlname to set.
     */
    public void setXmlname(final String name) {

        xmlname = name;
    }

    /**
     * Returns the efmname.
     * @return Returns the efmname.
     */
    public String getEfmname() {

        return efmname;
    }

    /**
     * The efmname to set.
     * @param name The efmname to set.
     */
    public void setEfmname(final String name) {

        efmname = name;
    }

    /**
     * Returns the toefm.
     * @return Returns the toefm.
     */
    public boolean isToefm() {

        return toefm;
    }

    /**
     * The toefm to set.
     * @param efm The toefm to set.
     */
    public void setToefm(final boolean efm) {

        toefm = efm;
    }

    /**
     * Returns the enclist.
     * @return Returns the enclist.
     */
    public ArrayList getEnclist() {

        return enclist;
    }

    /**
     * The enclist to set.
     * @param list The enclist to set.
     */
    public void setEnclist(final ArrayList list) {

        enclist = list;
    }

    /**
     * Returns the encname.
     * @return Returns the encname.
     */
    public String getEncname() {

        return encname;
    }

    /**
     * The encname to set.
     * @param name The encname to set.
     */
    public void setEncname(final String name) {

        encname = name;
    }

    /**
     * Returns the toenc.
     * @return Returns the toenc.
     */
    public boolean isToenc() {

        return toenc;
    }

    /**
     * The toenc to set.
     * @param enc The toenc to set.
     */
    public void setToenc(final boolean enc) {

        toenc = enc;
    }

    /**
     * Returns the outdir.
     * @return Returns the outdir.
     */
    public String getOutdir() {

        return outdir;
    }

    /**
     * The outdir to set.
     * @param out The outdir to set.
     */
    public void setOutdir(final String out) {

        outdir = out;
    }

    /**
     * Returns the tomap.
     * @return Returns the tomap.
     */
    public boolean isTomap() {

        return tomap;
    }

    /**
     * The tomap to set.
     * @param map The tomap to set.
     */
    public void setTomap(final boolean map) {

        tomap = map;
    }

    /**
     * Returns the topl.
     * @return Returns the topl.
     */
    public boolean isTopl() {

        return topl;
    }

    /**
     * The topl to set.
     * @param pl The topl to set.
     */
    public void setTopl(final boolean pl) {

        topl = pl;
    }
}
