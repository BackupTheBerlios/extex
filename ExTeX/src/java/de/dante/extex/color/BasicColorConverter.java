/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.color;

import de.dante.extex.color.model.CmykColor;
import de.dante.extex.color.model.ColorFactory;
import de.dante.extex.color.model.GrayscaleColor;
import de.dante.extex.color.model.HsvColor;
import de.dante.extex.color.model.RgbColor;
import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.util.exception.GeneralException;

/**
 * This implementation of a color converter is based on the formulas in the
 * color space FAQ.
 *
 * TODO gene: use the new ColorVisitor
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class BasicColorConverter implements ColorConverter {

    /**
     * Creates a new object.
     */
    public BasicColorConverter() {

        super();
    }

    /**
     * The field <tt>cmyk</tt> contains the converter for colors to the CMYK
     * model.
     */
    private static ColorVisitor cmyk = new ColorVisitor() {

        /**
         * @see de.dante.extex.color.ColorVisitor#visitCmyk(
         *      de.dante.extex.color.model.CmykColor,
         *      java.lang.Object)
         */
        public Object visitCmyk(final CmykColor color, final Object value)
                throws GeneralException {

            return color;
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitGray(
         *      de.dante.extex.color.model.GrayscaleColor,
         *      java.lang.Object)
         */
        public Object visitGray(final GrayscaleColor color, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitHsv(
         *      de.dante.extex.color.model.HsvColor,
         *      java.lang.Object)
         */
        public Object visitHsv(final HsvColor color, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitRgb(
         *      de.dante.extex.color.model.RgbColor,
         *      java.lang.Object)
         */
        public Object visitRgb(final RgbColor color, final Object value)
                throws GeneralException {

            int r = Color.MAX_VALUE - ((RgbColor) color).getRed();
            int g = Color.MAX_VALUE - ((RgbColor) color).getGreen();
            int b = Color.MAX_VALUE - ((RgbColor) color).getBlue();
            int black = (r > b ? (b > g ? g : b) : (r > g ? g : r));
            return ColorFactory.getCmyk(//
                    black == Color.MAX_VALUE ? 0 : (r - black)
                            / (Color.MAX_VALUE - black), //
                    black == Color.MAX_VALUE ? 0 : (g - black)
                            / (Color.MAX_VALUE - black), //
                    black == Color.MAX_VALUE ? 0 : (r - black)
                            / (Color.MAX_VALUE - black), //
                    black, ((RgbColor) color).getAlpha());
        }
    };

    /**
     * The field <tt>gray</tt> contains the converter for colors to the
     * grayscale model.
     */
    private static ColorVisitor gray = new ColorVisitor() {

        /**
         * @see de.dante.extex.color.ColorVisitor#visitCmyk(
         *      de.dante.extex.color.model.CmykColor,
         *      java.lang.Object)
         */
        public Object visitCmyk(final CmykColor color, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitGray(
         *      de.dante.extex.color.model.GrayscaleColor,
         *      java.lang.Object)
         */
        public Object visitGray(final GrayscaleColor color, final Object value)
                throws GeneralException {

            return color;
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitHsv(
         *      de.dante.extex.color.model.HsvColor,
         *      java.lang.Object)
         */
        public Object visitHsv(final HsvColor color, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitRgb(
         *      de.dante.extex.color.model.RgbColor,
         *      java.lang.Object)
         */
        public Object visitRgb(final RgbColor color, final Object value)
                throws GeneralException {

            return ColorFactory
                    .getGray(
                            (222 * ((RgbColor) color).getRed() + 707
                                    * ((RgbColor) color).getGreen() + 713 * ((RgbColor) color)
                                    .getBlue()) / 1000, ((RgbColor) color)
                                    .getAlpha());
        }
    };

    /**
     * The field <tt>rgb</tt> contains the converter for colors to the RGB
     * model.
     */
    private static ColorVisitor rgb = new ColorVisitor() {

        /**
         * @see de.dante.extex.color.ColorVisitor#visitCmyk(
         *      de.dante.extex.color.model.CmykColor,
         *      java.lang.Object)
         */
        public Object visitCmyk(final CmykColor color, final Object value)
                throws GeneralException {

            int r = Color.MAX_VALUE - ((CmykColor) color).getCyan()
                    * (Color.MAX_VALUE - ((CmykColor) color).getBlack())
                    / Color.MAX_VALUE + ((CmykColor) color).getBlack();
            int g = Color.MAX_VALUE - ((CmykColor) color).getMagenta()
                    * (Color.MAX_VALUE - ((CmykColor) color).getBlack())
                    / Color.MAX_VALUE + ((CmykColor) color).getBlack();
            int b = Color.MAX_VALUE - ((CmykColor) color).getYellow()
                    * (Color.MAX_VALUE - ((CmykColor) color).getBlack())
                    / Color.MAX_VALUE + ((CmykColor) color).getBlack();
            return ColorFactory.getRgb((r < 0 ? 0 : r), //
                    (g < 0 ? 0 : g), //
                    (b < 0 ? 0 : b), //
                    ((CmykColor) color).getAlpha());
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitGray(
         *      de.dante.extex.color.model.GrayscaleColor,
         *      java.lang.Object)
         */
        public Object visitGray(final GrayscaleColor color, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitHsv(
         *      de.dante.extex.color.model.HsvColor,
         *      java.lang.Object)
         */
        public Object visitHsv(final HsvColor color, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitRgb(
         *      de.dante.extex.color.model.RgbColor,
         *      java.lang.Object)
         */
        public Object visitRgb(final RgbColor color, final Object value)
                throws GeneralException {

            return color;
        }
    };

    /**
     * The field <tt>hsv</tt> contains the converter for colors to the HSV
     * model.
     */
    private static ColorVisitor hsv = new ColorVisitor() {

        /**
         * @see de.dante.extex.color.ColorVisitor#visitCmyk(
         *      de.dante.extex.color.model.CmykColor,
         *      java.lang.Object)
         */
        public Object visitCmyk(final CmykColor color, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitGray(
         *      de.dante.extex.color.model.GrayscaleColor,
         *      java.lang.Object)
         */
        public Object visitGray(final GrayscaleColor color, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitHsv(
         *      de.dante.extex.color.model.HsvColor,
         *      java.lang.Object)
         */
        public Object visitHsv(final HsvColor color, final Object value)
                throws GeneralException {

            return color;
        }

        /**
         * @see de.dante.extex.color.ColorVisitor#visitRgb(
         *      de.dante.extex.color.model.RgbColor,
         *      java.lang.Object)
         */
        public Object visitRgb(final RgbColor color, final Object value)
                throws GeneralException {

            return null;
        }
    };

    /**
     * Convert an arbitrary color to the CMYK model.
     * If an conversion is not supported then <code>null</code> is returned.
     *
     * @param color the color to convert
     *
     * @return the corresponding color in the CMYK model or <code>null</code>
     *  if a conversion is not supported.
     *
     * @see de.dante.extex.color.ColorConverter#toCmyk(
     *      de.dante.extex.interpreter.context.Color)
     */
    public CmykColor toCmyk(final Color color) {

        try {
            return (CmykColor) color.visit(cmyk, null);
        } catch (GeneralException e) {
            throw new ImpossibleException(this.getClass().getName());
        }
    }

    /**
     * Convert an arbitrary color to the RGB model.
     * If an conversion is not supported then <code>null</code> is returned.
     *
     * @param color the color to convert
     *
     * @return the corresponding color in the RGB model or <code>null</code> if
     *  a conversion is not supported.
     *
     * @see de.dante.extex.color.ColorConverter#toGrayscale(
     *      de.dante.extex.interpreter.context.Color)
     */
    public GrayscaleColor toGrayscale(final Color color) {

        try {
            return (GrayscaleColor) color.visit(gray, null);
        } catch (GeneralException e) {
            throw new ImpossibleException(this.getClass().getName());
        }
    }

    /**
     * Convert an arbitrary color to the HSV model.
     * If an conversion is not supported then <code>null</code> is returned.
     *
     * @param color the color to convert
     *
     * @return the corresponding color in the HSV model or <code>null</code> if
     *  a conversion is not supported.
     *
     * @see de.dante.extex.color.ColorConverter#toHsv(
     *      de.dante.extex.interpreter.context.Color)
     */
    public HsvColor toHsv(final Color color) {

        try {
            return (HsvColor) color.visit(hsv, null);
        } catch (GeneralException e) {
            throw new ImpossibleException(this.getClass().getName());
        }
    }

    /**
     * Convert an arbitrary color to the RGB model.
     * If an conversion is not supported then <code>null</code> is returned.
     *
     * @param color the color to convert
     *
     * @return the corresponding color in the RGB model or <code>null</code> if
     *  a conversion is not supported.
     *
     * @see de.dante.extex.color.ColorConverter#toRgb(
     *      de.dante.extex.interpreter.context.Color)
     */
    public RgbColor toRgb(final Color color) {

        try {
            return (RgbColor) color.visit(rgb, null);
        } catch (GeneralException e) {
            throw new ImpossibleException(this.getClass().getName());
        }
    }
}
