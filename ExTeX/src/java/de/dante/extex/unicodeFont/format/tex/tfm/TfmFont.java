/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.tex.tfm;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.key.FontKey;
import de.dante.extex.unicodeFont.key.FontKeyConfigurable;
import de.dante.extex.unicodeFont.type.ExtexFont;
import de.dante.extex.unicodeFont.type.FontInit;
import de.dante.extex.unicodeFont.type.InputStreamConfigurable;
import de.dante.extex.unicodeFont.type.TexFont;
import de.dante.util.UnicodeChar;
import de.dante.util.file.random.RandomAccessInputStream;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.framework.Registrar;
import de.dante.util.framework.logger.LogEnabled;
import de.dante.util.resource.PropertyConfigurable;

/**
 * A font from a tfm file.
 *
 * TODO add EE00 from Unicode
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */

public class TfmFont
        implements
            ExtexFont,
            TexFont,
            Serializable,
            LogEnabled,
            PropertyConfigurable,
            InputStreamConfigurable,
            FontKeyConfigurable,
            FontInit {

    /**
     * Default units per em.
     */
    public static final int DEFAULTUNITSPEREM = 1000;

    /**
     * permille factor for scale factor.
     */
    public static final int PERMILLE_FACTOR = 1000;

    /**
     * The font key.
     */
    private FontKey key;

    /**
     * The tfm reader.
     */
    private transient TfmReader tfmReader;

    /**
     * The actual font size.
     */
    private transient Dimen actualsize;

    /**
     * The design font size.
     */
    private transient Dimen designsize;

    /**
     * The hash for font dimens.
     */
    private transient HashMap fontdimen = new HashMap();

    /**
     * Create a new object.
     */
    public TfmFont() {

        super();
    }

    /**
     * set the font dimen values from the tfm parameter.
     */
    private void setFontDimenValues() {

        TfmParamArray param = tfmReader.getParam();
        TfmFixWord[] fw = param.getTable();
        for (int i = 0; i < fw.length; i++) {
            String labelname = param.getLabelName(i);
            Dimen d = convertFixWordToDimen(fw[i], designsize);
            setFontDimen(labelname, d);
        }
    }

    /**
     * Convert the fixword value to a dimen value.
     *
     * <p>
     * the simple calculation
     *     fw.getValue() * actualsize.getValue() /
     *     TFMFixWord.FIXWORDDENOMINATOR
     * leads to different rounding than in TeX due to
     * a limitation to 4 byte integer precission. Note
     * that fw.getValue() * actualsize.getValue() might
     * exceed the 4 byte range.
     * Hence TTP 571 cancels actualsize.getValue() and
     * TFMFixWord.FIXWORDDENOMINATOR to allow for a
     * bytewise calculation. We do not need this bytewise
     * calculation since we have long integers, but we
     * need to be precise in performing the same rounding.
     * </p>
     *
     * @param fw    the fixword value
     * @param size  The font size
     *              (design size for font dimen, actual size for all others)
     * @return Returns the Dimen value.
     */
    private Dimen convertFixWordToDimen(final TfmFixWord fw, final Dimen size) {

        int shift = TfmFixWord.POINTSHIFT;
        long z = size.getValue();
        while (z >= MAX_FIXWORD_VALUE) {
            z >>= 1;
            shift -= 1;
        }
        return new Dimen(z * fw.getValue() >> shift);
    }

    /**
     * the maximal value for fix word to calculate the dimen value.
     */
    private static final int MAX_FIXWORD_VALUE = 8388608;

    /**
     * Calculate the sizes.
     */
    private void calculateSize() {

        designsize = new Dimen((long) (tfmReader.getHeader().getDesignsize()
                .toDouble() * Dimen.ONE));
        actualsize = new Dimen(designsize);
    }

    // ------------------------------------------------

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getActualsize()
     */
    public Dimen getActualsize() {

        return actualsize;
    }

    /**
     * the Unicode char of the last query.
     */
    private transient UnicodeChar lastuc = null;

    /**
     * the char info word of the last query.
     */
    private transient TfmCharInfoWord ci = null;

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getDepth(
     *      de.dante.util.UnicodeChar)
     */
    public Glue getDepth(final UnicodeChar uc) {

        if (!(uc.equals(lastuc) && ci != null)) {
            TfmCharInfoArray charinfo = tfmReader.getCharinfo();
            ci = charinfo.getCharInfoWord(uc.getCodePoint());
            lastuc = uc;
        }
        if (ci != null) {
            return new Glue(convertFixWordToDimen(ci.getWidth(), actualsize));
        }
        return new Glue(0);
    }

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getDesignsize()
     */
    public Dimen getDesignsize() {

        return designsize;
    }

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getEm()
     */
    public Dimen getEm() {

        return actualsize;
    }

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getEx()
     */
    public Dimen getEx() {

        Dimen xheight = getFontDimen("XHEIGHT");
        if (xheight == null) {
            return new Dimen(actualsize);
        }
        return xheight;
    }

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getFontDimen(
     *      java.lang.String)
     */
    public Dimen getFontDimen(final String name) {

        Dimen rt = (Dimen) fontdimen.get(key);
        if (rt == null) {
            rt = new Dimen(0);
        }
        return new Dimen((long) (rt.getValue() * scaleFactor()));
    }

    /**
     * @see de.dante.extex.unicodeFont.type.TexFont#setActualsize(
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setActualsize(final Dimen size) {

        actualsize.set(size.getValue());
    }

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getFontName()
     */
    public String getFontName() {

        return key.getName();
    }

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getHeight(
     *      de.dante.util.UnicodeChar)
     */
    public Glue getHeight(final UnicodeChar uc) {

        if (!(uc.equals(lastuc) && ci != null)) {
            TfmCharInfoArray charinfo = tfmReader.getCharinfo();
            ci = charinfo.getCharInfoWord(uc.getCodePoint());
            lastuc = uc;
        }
        if (ci != null) {
            return new Glue(convertFixWordToDimen(ci.getHeight(), actualsize));
        }
        return new Glue(0);
    }

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getItalicCorrection(
     *      de.dante.util.UnicodeChar)
     */
    public Dimen getItalicCorrection(final UnicodeChar uc) {

        if (!(uc.equals(lastuc) && ci != null)) {
            TfmCharInfoArray charinfo = tfmReader.getCharinfo();
            ci = charinfo.getCharInfoWord(uc.getCodePoint());
            lastuc = uc;
        }
        if (ci != null) {
            return convertFixWordToDimen(ci.getItalic(), actualsize);
        }
        return new Dimen(0);
    }

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getKerning(
     *      de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     */
    public Dimen getKerning(final UnicodeChar uc1, final UnicodeChar uc2) {

        TfmLigKern[] ligKernTable = tfmReader.getLigkern().getLigKernTable();
        if (!(uc1.equals(lastuc) && ci != null)) {
            TfmCharInfoArray charinfo = tfmReader.getCharinfo();
            ci = charinfo.getCharInfoWord(uc1.getCodePoint());
            lastuc = uc1;
        }

        if (ci != null) {
            int ligstart = ci.getLigkernstart();
            if (ligstart != TfmCharInfoWord.NOINDEX) {

                for (int k = ligstart; k != TfmCharInfoWord.NOINDEX; k = ligKernTable[k]
                        .nextIndex(k)) {
                    TfmLigKern lk = ligKernTable[k];
                    if (lk instanceof TfmKerning) {
                        TfmKerning kern = (TfmKerning) lk;

                        if (uc2.getCodePoint() == kern.getNextChar()) {
                            return convertFixWordToDimen(kern.getKern(),
                                    actualsize);
                        }
                    }
                }
            }
        }
        return new Dimen(0);
    }

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getLigature(
     *      de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     */
    public UnicodeChar getLigature(final UnicodeChar uc1, final UnicodeChar uc2) {

        TfmLigKern[] ligKernTable = tfmReader.getLigkern().getLigKernTable();
        if (!(uc1.equals(lastuc) && ci != null)) {
            TfmCharInfoArray charinfo = tfmReader.getCharinfo();
            ci = charinfo.getCharInfoWord(uc1.getCodePoint());
            lastuc = uc1;
        }

        if (ci != null) {
            int ligstart = ci.getLigkernstart();
            if (ligstart != TfmCharInfoWord.NOINDEX) {

                for (int k = ligstart; k != TfmCharInfoWord.NOINDEX; k = ligKernTable[k]
                        .nextIndex(k)) {
                    TfmLigKern lk = ligKernTable[k];
                    if (lk instanceof TfmLigature) {
                        TfmLigature lig = (TfmLigature) lk;

                        if (uc2.getCodePoint() == lig.getNextChar()) {
                            return new UnicodeChar(lig.getAddingChar());
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Calculate the scale factor.
     * @return Returns the scale factor.
     */
    private double scaleFactor() {

        return (double) actualsize.getValue() / designsize.getValue();
    }

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getScalefactor()
     */
    public Count getScalefactor() {

        return new Count((long) (scaleFactor() * PERMILLE_FACTOR));
    }

    /**
     * @see de.dante.extex.unicodeFont.type.TexFont#setScalefactor(
     *      de.dante.extex.interpreter.type.count.Count)
     */
    public void setScalefactor(final Count scaleFactor) {

        if (scaleFactor != null && scaleFactor.getValue() >= 0) {
            actualsize.set(designsize.getValue() * scaleFactor.getValue()
                    / PERMILLE_FACTOR);
        }
    }

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getSpace()
     */
    public Glue getSpace() {

        Dimen spacedimen = getFontDimen("SPACE");
        if (spacedimen != null) {
            return new Glue(spacedimen);
        }

        // MGN Walter fragen, welche spacebreite?
        return new Glue(actualsize);
    }

    /**
     * @see de.dante.extex.unicodeFont.type.ExtexFont#getWidth(
     *      de.dante.util.UnicodeChar)
     */
    public Glue getWidth(final UnicodeChar uc) {

        if (!(uc.equals(lastuc) && ci != null)) {
            TfmCharInfoArray charinfo = tfmReader.getCharinfo();
            ci = charinfo.getCharInfoWord(uc.getCodePoint());
            lastuc = uc;
        }
        if (ci != null) {
            return new Glue(convertFixWordToDimen(ci.getWidth(), actualsize));
        }
        return new Glue(0);
    }

    /**
     * @see de.dante.extex.unicodeFont.type.Font#getFontKey()
     */
    public FontKey getFontKey() {

        return key;
    }

    /**
     * @see de.dante.extex.unicodeFont.type.Font#getActualFontKey()
     */
    public FontKey getActualFontKey() {

        // TODO incomplete
        return null;
    }

    /**
     * hyphen-char.
     */
    private transient UnicodeChar hyphenchar = new UnicodeChar('-');

    /**
     * skew-char.
     */
    private transient UnicodeChar skewchar = null;

    /**
     * @see de.dante.extex.unicodeFont.type.TexFont#getHyphenChar()
     */
    public UnicodeChar getHyphenChar() {

        return hyphenchar;
    }

    /**
     * @see de.dante.extex.unicodeFont.type.TexFont#getSkewChar()
     */
    public UnicodeChar getSkewChar() {

        return skewchar;
    }

    /**
     * @see de.dante.extex.unicodeFont.type.TexFont#setFontDimen(
     *      java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setFontDimen(final String name, final Dimen value) {

        if (value != null) {
            fontdimen.put(name, new Dimen(
                    (long) (value.getValue() / scaleFactor())));
        }
    }

    /**
     * @see de.dante.extex.unicodeFont.type.TexFont#setHyphenChar(
     *      de.dante.util.UnicodeChar)
     */
    public void setHyphenChar(final UnicodeChar uc) {

        hyphenchar = uc;
    }

    /**
     * @see de.dante.extex.unicodeFont.type.TexFont#setSkewChar(
     *      de.dante.util.UnicodeChar)
     */
    public void setSkewChar(final UnicodeChar uc) {

        skewchar = uc;
    }

    // -------------------------------------------------------
    // -------------------------------------------------------
    // -------------------------------------------------------
    // -------------------------------------------------------

    /**
     * reconnect after a dump.
     * @return an object.
     * @throws ObjectStreamException if an error occurred.
     */
    protected Object readResolve() throws ObjectStreamException {

        return Registrar.reconnect(this);
    }

    /**
     * the logger.
     */
    private transient Logger logger;

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        logger = theLogger;
    }

    /**
     * the properties.
     */
    private transient Properties properties;

    /**
     * @see de.dante.util.resource.PropertyConfigurable#setProperties(
     *      java.util.Properties)
     */
    public void setProperties(final Properties theProperties) {

        properties = theProperties;
    }

    /**
     * the input stream.
     */
    private transient InputStream in;

    /**
     * @see de.dante.extex.unicodeFont.type.InputStreamConfigurable#setInputStream(
     *      java.io.InputStream)
     */
    public void setInputStream(final InputStream theIn) {

        in = theIn;
    }

    /**
     * @see de.dante.extex.unicodeFont.key.FontKeyConfigurable#setFontKey(
     *      de.dante.extex.unicodeFont.key.FontKey)
     */
    public void setFontKey(final FontKey theFontKey) {

        key = theFontKey;
    }

    /**
     * @see de.dante.extex.unicodeFont.type.FontInit#init()
     */
    public void init() throws FontException {

        try {
            RandomAccessR rar = new RandomAccessInputStream(in);
            tfmReader = new TfmReader(rar, key.getName());
        } catch (IOException e) {
            // TODO: change
            throw new FontException(e.getMessage());
        }
        calculateSize();
        setFontDimenValues();
    }

    
    /**
     * Returns the tfmReader.
     * @return Returns the tfmReader.
     */
    public TfmReader getTfmReader() {
    
        return tfmReader;
    }
}
