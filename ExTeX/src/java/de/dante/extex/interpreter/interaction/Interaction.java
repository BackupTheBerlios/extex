/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.interaction;

import java.io.ObjectStreamException;
import java.io.Serializable;

import de.dante.util.exception.GeneralException;

/**
 * This class provides a type-save enumeration of the interactions styles of
 * <logo>ExTeX</logo>. It provides constants for the supported interaction
 * modes. In addition it supports the visitor pattern to react on them.
 *
 * @see "<logo>TeX</logo> &ndash; The Program [73]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class Interaction implements Serializable {

    /**
     * This inner class is use to represent the batch mode.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private static class BatchMode extends Interaction {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Interaction.BATCHMODE;
        }

        /**
         * @see "<logo>TeX</logo> &ndash; The Program [73]"
         * @see de.dante.extex.interpreter.Interaction#getIndex()
         */
        public String getIndex() {

            return "0";
        }

        /**
         * @see de.dante.extex.interpreter.Interaction#visit(
         *      de.dante.extex.interpreter.InteractionVisitor,
         *      java.lang.Object, java.lang.Object, java.lang.Object)
         */
        public boolean visit(final InteractionVisitor visitor,
                final Object arg1, final Object arg2, final Object arg3)
                throws GeneralException {

            return visitor.visitBatchmode(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class is use to represent the error stop mode.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private static class ErrorstopMode extends Interaction {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for
         * serialization.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Interaction.ERRORSTOPMODE;
        }

        /**
         * @see "<logo>TeX</logo> &ndash; The Program [73]"
         * @see de.dante.extex.interpreter.Interaction#getIndex()
         */
        public String getIndex() {

            return "3";
        }

        /**
         * @see de.dante.extex.interpreter.Interaction#visit(
         *      de.dante.extex.interpreter.InteractionVisitor,
         *      java.lang.Object, java.lang.Object, java.lang.Object)
         */
        public boolean visit(final InteractionVisitor visitor,
                final Object arg1, final Object arg2, final Object arg3)
                throws GeneralException {

            return visitor.visitErrorstopmode(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class is use to represent the nonstop mode.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private static class NonstopMode extends Interaction {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Interaction.NONSTOPMODE;
        }

        /**
         * @see "<logo>TeX</logo> &ndash; The Program [73]"
         * @see de.dante.extex.interpreter.Interaction#getIndex()
         */
        public String getIndex() {

            return "1";
        }

        /**
         * @see de.dante.extex.interpreter.Interaction#visit(
         *      de.dante.extex.interpreter.InteractionVisitor,
         *      java.lang.Object, java.lang.Object, java.lang.Object)
         */
        public boolean visit(final InteractionVisitor visitor,
                final Object arg1, final Object arg2, final Object arg3)
                throws GeneralException {

            return visitor.visitNonstopmode(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class is use to represent the scroll mode.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private static class ScrollMode extends Interaction {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Interaction.SCROLLMODE;
        }

        /**
         * @see "<logo>TeX</logo> &ndash; The Program [73]"
         * @see de.dante.extex.interpreter.Interaction#getIndex()
         */
        public String getIndex() {

            return "2";
        }

        /**
         * @see de.dante.extex.interpreter.Interaction#visit(
         *      de.dante.extex.interpreter.InteractionVisitor,
         *      java.lang.Object, java.lang.Object, java.lang.Object)
         */
        public boolean visit(final InteractionVisitor visitor,
                final Object arg1, final Object arg2, final Object arg3)
                throws GeneralException {

            return visitor.visitScrollmode(arg1, arg2, arg3);
        }

    }

    /**
     * The constant <tt>BATCHMODE</tt> contains the constant for batch mode.
     */
    public static final Interaction BATCHMODE = new BatchMode();

    /**
     * The constant <tt>ERRORSTOPMODE</tt> contains the constant for error stop
     * mode.
     */
    public static final Interaction ERRORSTOPMODE = new ErrorstopMode();

    /**
     * The constant <tt>NONSTOPMODE</tt> contains the constant for non-stop
     * mode.
     */
    public static final Interaction NONSTOPMODE = new NonstopMode();

    /**
     * The constant <tt>SCROLLMODE</tt> contains the constant for scroll mode.
     */
    public static final Interaction SCROLLMODE = new ScrollMode();

    /**
     * The constant <tt>MODE_MAP</tt> contains the list for mapping integers to
     * modes.
     *
     * @see "<logo>TeX</logo> &ndash; The Program [73]"
     */
    private static final Interaction[] MODE_MAP = //
    {BATCHMODE, NONSTOPMODE, SCROLLMODE, ERRORSTOPMODE};

    /**
     * This is a factory method for interaction modes. It mapps numerical
     * values to interaction mode instances. The instances are reused and may
     * be compared with ==.
     *
     * @param mode the integer value for the interaction mode
     *
     * @return the appropriate interaction mode constant
     *
     * @throws InteractionUnknownException in case that the numerical value
     *             is out of range
     */
    public static Interaction get(final int mode)
            throws InteractionUnknownException {

        if (mode < 0 || mode >= MODE_MAP.length) {
            throw new InteractionUnknownException(Integer.toString(mode));
        }

        return MODE_MAP[mode];
    }

    /**
     * Find the integer number corresponding to an interaction
     *
     * @param mode the mode to identify
     *
     * @return the number of the mode
     *
     * @throws InteractionUnknownException in case of an error
     */
    public static int get(final Interaction mode)
            throws InteractionUnknownException {

        for (int i = 0; i < MODE_MAP.length; i++) {
            if (mode == MODE_MAP[i]) {
                return i;
            }
        }

        throw new InteractionUnknownException(mode.toString());
    }

    /**
     * This is a factory method for interaction modes. It maps numerical
     * values to interaction mode instances. The instances are reused and may
     * be compared with ==.
     *
     * Allowed values are the numbers 0 to 3 or the symbolic names batchmode
     * (0), nonstopmode (1), scrollmode (2), and errorstopmode (3). The
     * symbolic names can be abbreviated up to the least unique prefix, i.e. up
     * to one character.
     *
     * @param mode the string representation for the mode
     *
     * @return the appropriate interaction mode constant
     *
     * @throws InteractionUnknownException in case that something is passed
     *             in which can not be interpreted as interaction mode
     */
    public static Interaction get(final String mode)
            throws InteractionUnknownException {

        if (mode == null || "".equals(mode)) {
            throw new InteractionUnknownException("");
        } else if ("batchmode".startsWith(mode) || mode.equals("0")) {
            return BATCHMODE;
        } else if ("nonstopmode".startsWith(mode) || mode.equals("1")) {
            return NONSTOPMODE;
        } else if ("scrollmode".startsWith(mode) || mode.equals("2")) {
            return SCROLLMODE;
        } else if ("errorstopmode".startsWith(mode) || mode.equals("3")) {
            return ERRORSTOPMODE;
        }

        throw new InteractionUnknownException(mode);
    }

    /**
     * Creates a new object. This constructor is private to avoid that other
     * interaction modes than the predefined ones are used.
     */
    protected Interaction() {

        super();
    }

    /**
     * This method provides an entry point for the visitor pattern.
     *
     * @param visitor this argument contains the visitor which has initiated
     * the request.
     * @param arg1 the first argument
     * @param arg2 the second argument
     * @param arg3 the third argument
     *
     * @return a boolean indicator
     *
     * @throws GeneralException in case of an error
     *
     * @see InteractionVisitor
     */
    public abstract boolean visit(final InteractionVisitor visitor,
            final Object arg1, final Object arg2, final Object arg3)
            throws GeneralException;

    /**
     * Get the numeric index of the interaction mode.
     * According to the definition os <logo>TeX</logo> the following mapping
     * holds:
     * <table>
     *  <tr>
     *   <td>BatchMode</td><td>0</td>
     *  </tr>
     *  <tr>
     *   <td>NonstopMode</td><td>1</td>
     *  </tr>
     *  <tr>
     *   <td>ScrollMode</td><td>2</td>
     *  </tr>
     *  <tr>
     *   <td>ErrorstopMode</td><td>3</td>
     *  </tr>
     * </table>
     *
     * @return the numeric index
     */
    public abstract String getIndex();

}