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

import de.dante.extex.font.type.ttf.TTFTableGLYF.Descript;

/**
 * An individual glyph.
 * (see Chap01)
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TTFGlyph {

    /**
     * left side bearing
     */
    private short leftSideBearing;

    /**
     * advance width
     */
    private int advanceWidth;

    /**
     * points
     */
    private Point[] points;

    /**
     * Create a new object
     *
     * @param gd        glyph description
     * @param lsb       lsb
     * @param advance   advance
     */
    public TTFGlyph(final Descript gd, final short lsb, final int advance) {

        leftSideBearing = lsb;
        advanceWidth = advance;
        describe(gd);
    }

    /**
     * Returns the advance width.
     * @return Returns the advance width.
     */
    public int getAdvanceWidth() {

        return advanceWidth;
    }

    /**
     * Returns the left side bearing.
     * @return Returns the left side bearing.
     */
    public short getLeftSideBearing() {

        return leftSideBearing;
    }

    /**
     * Returns the point.
     * @param i     the index
     * @return Returns the point.
     */
    public Point getPoint(final int i) {

        return points[i];
    }

    /**
     * Returns the count of points.
     * @return Returns the count of points.
     */
    public int getPointCount() {

        return points.length;
    }

    /**
     * Resets the glyph to the TrueType table settings
     */
    public void reset() {

    }

    /**
     * Scale
     * @param factor a 16.16 fixed value
     */
    public void scale(final int factor) {

        for (int i = 0; i < points.length; i++) {
            //points[i].x = ( points[i].x * factor ) >> 6;
            //points[i].y = ( points[i].y * factor ) >> 6;
            points[i].setX(((points[i].getX() << 10) * factor) >> 26);
            points[i].setY(((points[i].getY() << 10) * factor) >> 26);
        }
        leftSideBearing = (short) ((leftSideBearing * factor) >> 6);
        advanceWidth = (advanceWidth * factor) >> 6;
    }

    /**
     * Set the points of a glyph from the Description
     * @param gd glyph description
     */
    private void describe(final Descript gd) {

        int endPtIndex = 0;
        points = new Point[gd.getPointCount() + 2];
        for (int i = 0; i < gd.getPointCount(); i++) {
            boolean endPt = gd.getEndPtOfContours(endPtIndex) == i;
            if (endPt) {
                endPtIndex++;
            }
            points[i] = new Point(gd.getXCoordinate(i), gd.getYCoordinate(i),
                    (gd.getFlags(i) & TTFTableGLYF.Descript.ONCURVE) != 0,
                    endPt);
        }

        // Append the origin and advanceWidth points (n & n+1)
        points[gd.getPointCount()] = new Point(0, 0, true, true);
        points[gd.getPointCount() + 1] = new Point(advanceWidth, 0, true, true);
    }

    /**
     * Points.
     *
     * <p>
     * At the lowest level, each glyph in a TrueType font is described
     * by a sequence of points on a grid.
     * (see Chap01)
     * </p>
     */
    public class Point {

        /**
         * x
         */
        private int x = 0;

        /**
         * y
         */
        private int y = 0;

        /**
         * on curve
         */
        private boolean onCurve = true;

        /**
         * end of contour
         */
        private boolean endOfContour = false;

        /**
         * touched
         */
        private boolean touched = false;

        /**
         * Create a new object.
         *
         * @param ax             the x
         * @param ay             the y
         * @param aonCurve       on curve
         * @param aendOfContour  end of contour
         */
        public Point(final int ax, final int ay, final boolean aonCurve,
                final boolean aendOfContour) {

            x = ax;
            y = ay;
            onCurve = aonCurve;
            endOfContour = aendOfContour;
        }

        /**
         * Returns the endOfContour.
         * @return Returns the endOfContour.
         */
        public boolean isEndOfContour() {

            return endOfContour;
        }

        /**
         * Returns the onCurve.
         * @return Returns the onCurve.
         */
        public boolean isOnCurve() {

            return onCurve;
        }

        /**
         * Returns the touched.
         * @return Returns the touched.
         */
        public boolean isTouched() {

            return touched;
        }

        /**
         * Returns the x.
         * @return Returns the x.
         */
        public int getX() {

            return x;
        }

        /**
         * Returns the y.
         * @return Returns the y.
         */
        public int getY() {

            return y;
        }

        /**
         * @param aendOfContour The endOfContour to set.
         */
        public void setEndOfContour(final boolean aendOfContour) {

            endOfContour = aendOfContour;
        }

        /**
         * @param aonCurve The onCurve to set.
         */
        public void setOnCurve(final boolean aonCurve) {

            onCurve = aonCurve;
        }

        /**
         * @param atouched The touched to set.
         */
        public void setTouched(final boolean atouched) {

            touched = atouched;
        }

        /**
         * @param ax The x to set.
         */
        public void setX(final int ax) {

            x = ax;
        }

        /**
         * @param ay The y to set.
         */
        public void setY(final int ay) {

            y = ay;
        }
    }
}