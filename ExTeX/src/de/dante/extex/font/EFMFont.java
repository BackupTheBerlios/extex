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

package de.dante.extex.font;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Font;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.Glyph;
import de.dante.extex.interpreter.type.Kerning;
import de.dante.extex.interpreter.type.Ligature;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.file.FileFinder;

/**
 * Abstract class for a efm-font.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.16 $
 */
public abstract class EFMFont implements Font {

    /**
     * The fontname
     */
    private String name;

    /**
     * the external fontfile
     */
    private File externalfile;

    /**
     * the em-size for the font
     */
    private Dimen emsize;

    /**
     * Creates a new object.
     * @param   doc         the efm-document
     * @param   fontname    the fontname
     * @param   size        the emsize of the font
     * @param   filefinder  the fileFinder-object
     * @throws GeneralException ...
     * @throws ConfigurationException ...
     */
    public EFMFont(final Document doc, final String fontname, final Dimen size,
            final FileFinder filefinder) throws GeneralException,
            ConfigurationException {

        super();
        if (fontname != null) {
            name = fontname;
        }
        emsize = new Dimen(size);
        em = new Dimen(size);

        loadFont(doc, filefinder);
    }

    /**
     * Attribut empr
     */
    private static final String ATTREMPR = "empr";

    /**
     * load the Font
     * @param   doc         the efm-document
     * @param   fileFinder  the fileFinder
     * @throws GeneralException if a error is thrown.
     * @throws ConfigurationException ...
     */
    private void loadFont(final Document doc, final FileFinder fileFinder)
            throws GeneralException, ConfigurationException {

        try {

            // get fontgroup
            Element fontgroup = doc.getRootElement();

            if (fontgroup == null) {
                throw new GeneralHelpingException("EFM.nofontgroup");
            }

            // empr
            Attribute attr = fontgroup.getAttribute(ATTREMPR);

            if (attr != null) {
                try {
                    empr = attr.getFloatValue();
                } catch (Exception e) {
                    empr = DEFAULTEMPR;
                }
            }
            // calculate em
            em = new Dimen((long) (emsize.getValue() * empr / PROZ100));

            // get unitsperem
            attr = fontgroup.getAttribute("units-per-em");
            if (attr != null) {
                unitsperem = attr.getIntValue();
            }

            // get ex
            attr = fontgroup.getAttribute("xheight");
            if (attr != null) {
                ex = attr.getIntValue();
            }

            // fontdimen-key-values
            List list = fontgroup.getAttributes();
            for (int i = 0; i < list.size(); i++) {
                attr = (Attribute) list.get(i);
                String key = attr.getName();
                String val = attr.getValue();
                if (val != null && val.trim().length() > 0) {
                    fontdimen.put(key, val);
                }
            }

            // get glyph-list
            Element font = scanForElement(fontgroup, "font");
            if (font != null) {
                List glyphlist = font.getChildren("glyph");
                Element e;

                for (int i = 0; i < glyphlist.size(); i++) {
                    e = (Element) glyphlist.get(i);
                    String key = e.getAttributeValue("ID");
                    if (key != null) {
                        Glyph gv = new Glyph();
                        gv.setNumber(e.getAttributeValue("glyph-number"));
                        gv.setName(e.getAttributeValue("glyph-name"));
                        gv.setWidth(e.getAttributeValue("width"), em,
                                unitsperem);
                        gv.setDepth(e.getAttributeValue("depth"), em,
                                unitsperem);
                        gv.setHeight(e.getAttributeValue("height"), em,
                                unitsperem);
                        gv.setItalic(e.getAttributeValue("italic"));

                        // kerning
                        List kerninglist = e.getChildren("kerning");
                        for (int k = 0; k < kerninglist.size(); k++) {
                            Element kerning = (Element) kerninglist.get(k);
                            Kerning kv = new Kerning();
                            kv.setId(kerning.getAttributeValue("glyph-id"));
                            kv.setName(kerning.getAttributeValue("glyph-name"));
                            kv.setSize(kerning.getAttributeValue("size"), em,
                                    unitsperem);
                            gv.addKerning(kv);
                        }

                        // ligature
                        List ligaturelist = e.getChildren("ligature");
                        for (int k = 0; k < ligaturelist.size(); k++) {
                            Element ligature = (Element) ligaturelist.get(k);
                            Ligature lv = new Ligature();
                            lv.setLetter(ligature.getAttributeValue("letter"));
                            lv.setLetterid(ligature
                                    .getAttributeValue("letter-id"));
                            lv.setLig(ligature.getAttributeValue("lig"));
                            lv.setLigid(ligature.getAttributeValue("lig-id"));
                            gv.addLigature(lv);
                        }
                        glyphmap.put(key, gv);
                        glyphname.put(gv.getName(), key);
                    }
                }
            }

            // exernal fontfile
            String efile = font.getAttributeValue("filename");

            if (efile != null) {
                efile = efile.replaceAll(".pfb", "");
                externalfile = fileFinder.findFile(efile, "pfb");
            }

        } catch (JDOMException e) {
            throw new GeneralHelpingException("EFM.jdomerror", e.getMessage());
        }
    }

    /**
     * initsize for the hashmap
     */
    private static final int HASHMAPINITSIZE = 256;

    /**
     * The hash for the glyphs (ID)
     */
    private HashMap glyphmap = new HashMap(HASHMAPINITSIZE);

    /**
     * The hash for the glyphs (name -> ID)
     */
    private HashMap glyphname = new HashMap(HASHMAPINITSIZE);

    /**
     * Return the with of a space.
     *
     * @see de.dante.extex.interpreter.type.Font#getSpace()
     */
    public Glue getSpace() {

        // use em-size for 'space'
        Glue rt = new Glue(em);

        // glyph 'space' exists?
        String key = (String) glyphname.get("space");
        if (key != null) {
            try {
                Glyph rglyph = getGlyph(new UnicodeChar(Integer.parseInt(key)));
                rt = new Glue(rglyph.getWidth());
            } catch (Exception e) {
                // do nothing, use default
                rt = new Glue(em);
            }
        }
        // TODO use key 'SPACE' from getFontDimen()
        return rt;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getEm()
     */
    public Dimen getEm() {

        return em;
    }

    /**
     * ex: the height of 'x'
     */
    private int ex = 0;

    /**
     * the em-procent-size
     */
    private float empr = DEFAULTEMPR;

    /**
     * Default for em-prozent-size
     */
    private static final float DEFAULTEMPR = 100.0f;

    /**
     * 100 %
     */
    private static final int PROZ100 = 100;

    /**
     * em: the width
     */
    private Dimen em;

    /**
     * Default unitsperem
     */
    private static final int DEFAULTUNITSPEREM = 1000;

    /**
     * units per em
     */
    private int unitsperem = DEFAULTUNITSPEREM;

    /**
     * @see de.dante.extex.interpreter.type.Font#getEx()
     */
    public Dimen getEx() {

        return new Dimen(ex * em.getValue() / unitsperem);
    }

    /**
     * hash for fontdimen-keys
     */
    private HashMap fontdimen = new HashMap();

    /**
     * Return the <code>Dimen</code>-value for a key-entry.
     * If no key exists, ZERO-<code>Dimen</code> is returned.
     *
     * @see de.dante.extex.interpreter.type.Font#getFontDimen(String)
     */
    public Dimen getFontDimen(final String key) {

        String val = (String) fontdimen.get(key);
        Dimen rt = new Dimen(0);
        try {
            float f = Float.parseFloat(val);
            rt = new Dimen((long) (f * em.getValue() / unitsperem));
        } catch (Exception e) {
            // do nothing, use default
            rt = new Dimen(0);
        }
        return rt;
    }

    /**
     * Set the <code>Dimen</code>-value for a key-entry.
     *
     * @see de.dante.extex.interpreter.type.Font#setFontDimen(String, Dimen)
     */
    public void setFontDimen(final String key, final Dimen value) {

        double d = value.getValue() / (double) em.getValue() * unitsperem;
        fontdimen.put(key, String.valueOf(d));
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getFontName()
     */
    public String getFontName() {

        return name;
    }

    /**
     * Return String for this class.
     * @return the String for this class
     */
    public abstract String toString();

    /**
     * @see de.dante.extex.interpreter.type.Font#getGlyph(de.dante.util.UnicodeChar)
     */
    public Glyph getGlyph(final UnicodeChar c) {

        Glyph g = (Glyph) glyphmap.get(String.valueOf(c.getCodePoint()));
        if (g == null) {
            g = new Glyph();
            g.setWidth(getEm());
            g.setHeight(getEx());
            // TODO incomplete: use glyph-symbol
        }
        return g;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#isDefined(de.dante.util.UnicodeChar)
     */
    public boolean isDefined(final UnicodeChar c) {

        return glyphmap.containsKey(String.valueOf(c.getCodePoint()));
    }

    /**
     * Return the kerning between c1 and c2.
     * @see de.dante.extex.interpreter.type.Font#kern(
     *      de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     * @deprecated use glyph.getKerning()
     */
    public Dimen kern(final UnicodeChar c1, final UnicodeChar c2) {

        //        Dimen rt = new Dimen(0);
        //
        //        GlyphValues gv = (GlyphValues) glyph.get(String.valueOf(c1
        //                .getCodePoint()));
        //        if (gv != null) {
        //            KerningValues kv = (KerningValues) gv.kerning.get(String.valueOf(c2
        //                    .getCodePoint()));
        //            try {
        //                float size = Float.parseFloat(kv.size);
        //                rt = new Dimen((long) (size * em.getValue() / unitsperem));
        //            } catch (Exception e) {
        //                // do nothing, use default
        //            }
        //        }
        //        return rt;
        return null;
    }

    /**
     * Return the ligature as <code>UnicodeChar</code>,
     * or <code>null</code>, if no ligature exists.
     *
     * If you get a ligature-character, then you MUST call the
     * method <code>ligature()</code> twice, if a ligature with
     * more then two characters exist.
     * (e.g. f - ff - ffl)
     * @see de.dante.extex.interpreter.type.Font#ligature(
     *      de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     * @deprecated use glyph.getLigature()
     */
    public UnicodeChar ligature(final UnicodeChar c1, final UnicodeChar c2) {

        //        UnicodeChar rt = null;
        //
        //        GlyphValues gv = (GlyphValues) glyph.get(String.valueOf(c1
        //                .getCodePoint()));
        //        if (gv != null) {
        //            LigatureValues lv = (LigatureValues) gv.ligature.get(String
        //                    .valueOf(c2.getCodePoint()));
        //            try {
        //                int id = Integer.parseInt(lv.ligid);
        //                rt = new UnicodeChar(id);
        //            } catch (Exception e) {
        //                // do nothing, use default
        //            }
        //        }
        //        return rt;
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getExternalFile()
     */
    public File getExternalFile() {

        return externalfile;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getExternalID()
     */
    public String getExternalID(final UnicodeChar c) {

        String rt = null;
        if (externalfile != null) {
            Glyph gv = (Glyph) glyphmap.get(String.valueOf(c.getCodePoint()));
            if (gv != null) {
                rt = gv.getNumber();
            }
        }
        return rt;
    }

    /**
     * hyphen-char
     */
    private UnicodeChar hyphenchar;

    /**
     * @see de.dante.extex.interpreter.type.Font#setHyphenChar(de.dante.util.UnicodeChar)
     */
    public void setHyphenChar(final UnicodeChar hyphen) {

        hyphenchar = hyphen;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getHyphenChar()
     */
    public UnicodeChar getHyphenChar() {

        return hyphenchar;
    }

    /**
     * skew-char
     */
    private UnicodeChar skewchar;

    /**
     * @see de.dante.extex.interpreter.type.Font#setSkewChar(de.dante.util.UnicodeChar)
     */
    public void setSkewChar(final UnicodeChar skew) {

        skewchar = skew;
    }

    /**
     * @see de.dante.extex.interpreter.type.Font#getSkewChar()
     */
    public UnicodeChar getSkewChar() {

        return skewchar;
    }

    /**
     * Return the Element in the tree or <code>null</code>, if not found.
     * @param e     the Element
     * @param name  the elementname
     * @return the element or <code>null</code>, if not found
     */
    public static Element scanForElement(final Element e, final String name) {

        if (e.getName().equals(name)) {
            return e;
        } else {
            Element element = e.getChild(name);
            if (element != null) {
                return element;
            } else {
                List liste = e.getChildren();
                for (int i = 0; i < liste.size(); i++) {
                    element = scanForElement((Element) liste.get(i), name);
                    if (element != null) {
                        return element;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return Returns the emsize.
     */
    protected Dimen getEmsize() {

        return emsize;
    }

    /**
     * @return Returns the unitsperem.
     */
    protected int getUnitsperem() {

        return unitsperem;
    }

    /**
     * @return Returns the empr.
     */
    protected float getEmpr() {

        return empr;
    }

    /**
     * Return the size of the glyphmap
     * @return  the size
     */
    protected int getGylphMapSize() {

        return glyphmap.size();
    }

}
