/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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
 */

package de.dante.extex.interpreter.type.font;

import java.io.ObjectStreamException;

import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.font.type.InternalFount;
import de.dante.extex.font.type.other.NullFont;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.Registrar;

/**
 * This class constitutes a wrapper for a font. Here all information is stored
 * which should be saved in a format. Especially all modifiable fields have to
 * be kept in this class.
 * <p>
 *  The loadable and constant parts are delegated and should not make it into
 *  the format file. Thus it is necessary to reconstitute this contents when the
 *  format has to be provided by the loader.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class FontImpl extends NullFont {

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    protected static final long serialVersionUID = 2006L;

    /**
     * The field <tt>cacheChar</tt> contains the Unicode character for the
     * most recently accessed glyph.
     */
    private transient UnicodeChar cacheChar = null;

    /**
     * The field <tt>cacheGlyph</tt> contains the cached glyph or
     * <code>null</code>. This means the last glyph accessed is stored here to
     * speed up access.
     */
    private transient Glyph cacheGlyph = null;

    /**
     * The fount.
     */
    private transient InternalFount fount;

    /**
     * The field <tt>key</tt> contains the font key. It is kept here since the
     * fount is transient and will not make it into the format file.
     */
    private FountKey key;

    /**
     * Create a new Object
     */
    public FontImpl() {

        super();

        this.fount = null;
        key = null;
    }

    /**
     * Create a new Object
     *
     * @param fount the fount
     */
    public FontImpl(final InternalFount fount) {

        super();

        this.fount = fount;
        key = (fount != null ? fount.getFontKey() : null);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getActualSize()
     */
    public FixedDimen getActualSize() {

        return fount.getActualSize();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#hasGlyph(
     *      de.dante.util.UnicodeChar)
     */
    public boolean hasGlyph(final UnicodeChar uc) {

        return getGlyph(uc) != null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getBoundingBox()
     */
    public BoundingBox getBoundingBox() {

        return fount.getBoundingBox();
    }

    /**
     * @see de.dante.extex.font.type.Fount#getCheckSum()
     */
    public int getCheckSum() {

        return fount.getCheckSum();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getDepth(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getDepth(final UnicodeChar uc) {

        Glyph g = getGlyph(uc);
        return (g == null ? Glue.ZERO : new Glue(g.getDepth()));
    }

    /**
     * @see de.dante.extex.font.type.Fount#getDesignSize()
     */
    public FixedDimen getDesignSize() {

        return fount.getDesignSize();
    }

    /**
     * @see de.dante.extex.font.type.Fount#getEm()
     */
    public FixedDimen getEm() {

        return fount.getEm();
    }

    /**
     * @see de.dante.extex.font.type.Fount#getEx()
     */
    public FixedDimen getEx() {

        return fount.getEx();
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontByteArray()
     */
    public FontByteArray getFontByteArray() {

        return fount.getFontByteArray();
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontKey()
     */
    public FountKey getFontKey() {

        return key;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontName()
     */
    public String getFontName() {

        return fount.getFontName();
    }

    /**
     * Getter for the fount.
     *
     * @return the fount.
     */
    public InternalFount getFount() {

        return fount;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getGlyph(
     *      de.dante.util.UnicodeChar)
     */
    public Glyph getGlyph(final UnicodeChar c) {

        if (cacheChar != null && cacheChar.equals(c)) {
            return cacheGlyph;
        }
        cacheChar = c;
        cacheGlyph = (fount != null ? fount.getGlyph(c) : null);

        return cacheGlyph;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getHeight(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getHeight(final UnicodeChar uc) {

        Glyph g = getGlyph(uc);
        return (g == null ? Glue.ZERO : new Glue(g.getHeight()));
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getItalicCorrection(
     *      de.dante.util.UnicodeChar)
     */
    public FixedDimen getItalicCorrection(final UnicodeChar uc) {

        Glyph g = getGlyph(uc);
        return (g == null ? Dimen.ZERO_PT : g.getItalicCorrection());
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getKerning(
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.UnicodeChar)
     */
    public FixedDimen getKerning(final UnicodeChar uc1, final UnicodeChar uc2) {

        Glyph g = getGlyph(uc1);
        return (g == null ? Dimen.ZERO_PT : g.getKerning(uc2));
    }

    /**
     * @see de.dante.extex.font.type.Fount#getLetterSpacing()
     */
    public FixedGlue getLetterSpacing() {

        return fount.getLetterSpacing();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getLigature(
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.UnicodeChar)
     */
    public UnicodeChar getLigature(final UnicodeChar uc1, final UnicodeChar uc2) {

        Glyph g = getGlyph(uc1);
        return (g == null ? null : g.getLigature(uc2));
    }

    /**
     * @see de.dante.extex.font.type.Fount#getProperty(java.lang.String)
     */
    public String getProperty(final String k) {

        return fount.getProperty(k);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getSpace()
     */
    public FixedGlue getSpace() {

        return fount.getSpace();
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getWidth(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getWidth(final UnicodeChar uc) {

        Glyph g = getGlyph(uc);
        return (g == null ? Glue.ZERO : new Glue(g.getWidth()));
    }

    /**
     * Setter for the fount.
     *
     * @param fount the fount to set
     */
    public void setFount(final InternalFount fount) {

        this.fount = fount;
    }

    /**
     * Magic method for deserialization.
     *
     * @return the reconnection result
     *
     * @throws ObjectStreamException in case of an error
     */
    protected Object readResolve() throws ObjectStreamException {

        return Registrar.reconnect(this);
    }

}
