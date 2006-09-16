
package de.dante.test.font;

import java.io.Serializable;

import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.Kerning;
import de.dante.extex.font.Ligature;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.UnicodeChar;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class LauncherFont implements Font, Serializable {

    /**
     * TODO gene: missing JavaDoc.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.3 $
     */
    final class LauncherGlyph implements Glyph, Serializable {

        /**
         * The field <tt>serialVersionUID</tt> contains the ...
         */
        private static final long serialVersionUID = 1L;

        /**
         * The field <tt>c</tt> contains the ...
         */
        final UnicodeChar c;

        /**
         * Creates a new object.
         *
         * @param c the character
         */
        private LauncherGlyph(final UnicodeChar c) {

            super();
            this.c = c;
        }

        public void addKerning(final Kerning kern) {

        }

        public void addLigature(final Ligature lig) {

        }

        /**
         * TODO gene: missing JavaDoc
         *
         * @return
         */
        public Dimen getDepth() {

            return Dimen.ONE_PT;
        }

        public FontByteArray getExternalFile() {

            return null;
        }

        public Dimen getHeight() {

            return Dimen.ONE_PT;
        }

        public Dimen getItalicCorrection() {

            return Dimen.ZERO_PT;
        }

        public Dimen getKerning(final UnicodeChar uc) {

            return Dimen.ZERO_PT;
        }

        /**
         * @see de.dante.extex.font.Glyph#getLeftSpace()
         */
        public Dimen getLeftSpace() {

            return Dimen.ZERO_PT;
        }

        public UnicodeChar getLigature(final UnicodeChar uc) {

            return null;
        }

        public String getName() {

            return "?";
        }

        public String getNumber() {

            return this.c.toString();
        }

        /**
         * @see de.dante.extex.font.Glyph#getRightSpace()
         */
        public Dimen getRightSpace() {

            return Dimen.ZERO_PT;
        }

        public Dimen getWidth() {

            return Dimen.ONE_PT;
        }

        public void setDepth(final Dimen d) {

        }

        public void setExternalFile(final FontByteArray file) {

        }

        public void setHeight(final Dimen h) {

        }

        public void setItalicCorrection(final Dimen d) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setLeftSpace(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setLeftSpace(final Dimen ls) {

        }

        public void setName(final String n) {

        }

        public void setNumber(final String nr) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setRightSpace(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setRightSpace(final Dimen rs) {

        }

        public void setWidth(final Dimen w) {

        }
    }

    /**
     * The field <tt>serialVersionUID</tt> contains the ...
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see de.dante.extex.font.type.Fount#getActualSize()
     */
    public FixedDimen getActualSize() {

        return new Dimen(Dimen.ONE_PT.getValue() * 10);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getBoundingBox()
     */
    public BoundingBox getBoundingBox() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getCheckSum()
     */
    public int getCheckSum() {

        return 0;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getDepth(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getDepth(final UnicodeChar uc) {

        // TODO gene: getDepth unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getDesignSize()
     */
    public FixedDimen getDesignSize() {

        return new Dimen(Dimen.ONE_PT.getValue() * 10);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getEfcode()
     */
    public long getEfcode(final UnicodeChar uc) {

        return 1000;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getEm()
     */
    public FixedDimen getEm() {

        return new Dimen(Dimen.ONE_PT.getValue() * 10);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getEx()
     */
    public FixedDimen getEx() {

        return new Dimen(Dimen.ONE_PT.getValue() * 5);
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontByteArray()
     */
    public FontByteArray getFontByteArray() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontDimen(java.lang.String)
     */
    public FixedDimen getFontDimen(final String key) {

        return new Dimen(Dimen.ZERO_PT.getValue());
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontKey()
     */
    public FountKey getFontKey() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getFontName()
     */
    public String getFontName() {

        return "testfont";
    }

    /**
     * @see de.dante.extex.font.type.Fount#getGlyph(
     *      de.dante.util.UnicodeChar)
     */
    public Glyph getGlyph(final UnicodeChar c) {

        return new LauncherGlyph(c);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getHeight(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getHeight(UnicodeChar uc) {

        // TODO gene: getHeight unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getHyphenChar()
     */
    public UnicodeChar getHyphenChar() {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getItalicCorrection(
     *      de.dante.util.UnicodeChar)
     */
    public FixedDimen getItalicCorrection(final UnicodeChar uc) {

        // TODO gene: getItalicCorrection unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getKerning(
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.UnicodeChar)
     */
    public FixedDimen getKerning(final UnicodeChar uc1, final UnicodeChar uc2) {

        // TODO gene: getKerning unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getLetterSpacing()
     */
    public FixedGlue getLetterSpacing() {

        return new Glue(Dimen.ZERO_PT.getValue());
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getLigature(
     *      de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     */
    public UnicodeChar getLigature(final UnicodeChar uc1, final UnicodeChar uc2) {

        // TODO gene: getLigature unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getProperty(java.lang.String)
     */
    public String getProperty(final String key) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getSkewChar()
     */
    public UnicodeChar getSkewChar() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.Fount#getSpace()
     */
    public FixedGlue getSpace() {

        return new Glue(Dimen.ONE_PT.getValue() * 10);
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#getWidth(
     *      de.dante.util.UnicodeChar)
     */
    public FixedGlue getWidth(final UnicodeChar uc) {

        // TODO gene: getWidth unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#hasGlyph(
     *      de.dante.util.UnicodeChar)
     */
    public boolean hasGlyph(final UnicodeChar uc) {

        return true;
    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setEfcode(de.dante.util.UnicodeChar, long)
     */
    public void setEfcode(UnicodeChar uc, long code) {

        // TODO gene: setEfcode unimplemented

    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setFontDimen(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setFontDimen(final String key, final Dimen value) {

    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setHyphenChar(
     *      de.dante.util.UnicodeChar)
     */
    public void setHyphenChar(final UnicodeChar hyphen) {

    }

    /**
     * @see de.dante.extex.interpreter.type.font.Font#setSkewChar(
     *      de.dante.util.UnicodeChar)
     */
    public void setSkewChar(final UnicodeChar skew) {

    }

}
