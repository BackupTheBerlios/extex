/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter;

import de.dante.extex.i18n.Messages;

/**
 * This class provides type-safe constants for the modes of a typesetter.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public abstract class Mode {

    /**
     * This inner class represents math modes.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.7 $
     */
    private static class MathMode extends Mode {

        /**
         * Creates a new object.
         *
         * @param theTag the tag of the mode
         */
        protected MathMode(final String theTag) {

            super(theTag);
        }

        /**
         * Check for a math mode.
         *
         * @return <code>true</code> since this is one of the math modes
         */
        public final boolean isMath() {

            return true;
        }
    }

    /**
     * This inner class represents vertical modes.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.7 $
     */
    private static class VerticalMode extends Mode {

        /**
         * Creates a new object.
         *
         * @param theTag the tag of the mode
         */
        protected VerticalMode(final String theTag) {

            super(theTag);
        }

        /**
         * Check for a vertical mode.
         *
         * @return <code>true</code> since this is one of the vertical modes
         */
        public final boolean isVmode() {

            return true;
        }
    }

    /**
     * This inner class represents vertical modes.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.7 $
     */
    private static class HorizontalMode extends Mode {

        /**
         * Creates a new object.
         *
         * @param theTag the tag of the mode
         */
        protected HorizontalMode(final String theTag) {

            super(theTag);
        }

        /**
         * Check for a horizontal mode.
         *
         * @return <code>true</code> since this is a horizontal modes
         */
        public final boolean isHmode() {

            return true;
        }
    }

    /**
     * The constant <tt>DISPLAYMATH</tt> contains the display math mode of
     * the typesetter.
     */
    public static final Mode DISPLAYMATH = new MathMode("Mode.DisplayMathMode");

    /**
     * The constant <tt>HORIZONTAL</tt> contains the horizontal mode of the
     * typesetter.
     */
    public static final Mode HORIZONTAL = new HorizontalMode("Mode.HorizontalMode");

    /**
     * The constant <tt>INNER_VERTICAL</tt> contains the inner vertical mode of
     * the typesetter.
     */
    public static final Mode INNER_VERTICAL = new VerticalMode("Mode.VerticalMode");

    /**
     * The constant <tt>MATH</tt> contains the math mode of the typesetter.
     */
    public static final Mode MATH = new MathMode("Mode.MathMode");

    /**
     * The constant <tt>RESTRICTED_HORIZONTAL</tt> contains the restricted
     * horizontal mode of the typesetter.
     */
    public static final Mode RESTRICTED_HORIZONTAL = new HorizontalMode(
            "Mode.HorizontalMode");

    /**
     * The constant <tt>VERTICAL</tt> contains the vertical mode of the
     * typesetter.
     */
    public static final Mode VERTICAL = new HorizontalMode("Mode.VerticalMode");

    /**
     * The field <tt>tag</tt> contains the key for the message (cf. i18n)
     *  to be used as a short description of the mode.
     */
    private String tag;

    /**
     * Creates a new object.
     * <p>
     * This constructor is private since only a limited number of modes should
     * be usable. Those are provided by the static constants in this class.
     * </p>
     *
     * @param theTag the tag of the mode
     */
    protected Mode(final String theTag) {

        super();
        this.tag = theTag;
    }

    /**
     * Check for a math mode.
     *
     * @return <code>true</code> iff the mode is one of the math modes
     */
    public boolean isMath() {

        return false;
    }

    /**
     * Check for a horizontal mode.
     *
     * @return <code>true</code> iff the mode is one of the horizontal modes
     */
    public boolean isHmode() {

        return false;
    }

    /**
     * Check for a vertical mode.
     *
     * @return <code>true</code> iff the mode is one of the vertical modes
     */
    public boolean isVmode() {

        return false;
    }

    /**
     * Getter for the string representation.
     *
     * @return the string representation
     */
    public String toString() {

        return Messages.format(tag);
    }
}