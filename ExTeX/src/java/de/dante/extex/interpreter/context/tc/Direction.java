/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context.tc;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * This interface provides a limited set of writing directions. The writing
 * directions are defined as constants. The constructor is private to avoid
 * that additional directions are defined.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Direction implements Serializable {

    /**
     * This interface restricts the values which can be used as components of a
     * direction.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    public static interface Dir extends Serializable {

    }

    /**
     * The constant <tt>B</tt> contains the direction component <i>bottom</i>.
     */
    public static final Dir B = new Dir() {

        /**
         * The field <tt>serialVersionUID</tt> contains the version number for
         * serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Magic method used during de-serialization to remap the object read.
         *
         * @return the constant
         */
        Object readResolve() {

            return B;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "B";
        }
    };

    /**
     * The constant <tt>L</tt> contains the direction component <i>left</i>.
     */
    public static final Dir L = new Dir() {

        /**
         * The field <tt>serialVersionUID</tt> contains the version number for
         * serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Magic method used during de-serialization to remap the object read.
         *
         * @return the constant
         */
        Object readResolve() {

            return L;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "L";
        }
    };

    /**
     * The constant <tt>LR</tt> contains the direction for left-to-right
     * languages.
     */
    public static final Direction LR = new Direction() {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return LR;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "LR";
        }

    };

    /**
     * The constant <tt>R</tt> contains the direction component <i>right</i>.
     */
    public static final Dir R = new Dir() {

        /**
         * The field <tt>serialVersionUID</tt> contains the version number for
         * serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Magic method used during de-serialization to remap the object read.
         *
         * @return the constant
         */
        Object readResolve() {

            return R;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "R";
        }
    };

    /**
     * The constant <tt>RL</tt> contains the direction for right-to-left
     * languages.
     */
    public static final Direction RL = new Direction() {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return RL;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "RL";
        }

    };

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2006L;

    /**
     * The constant <tt>T</tt> contains the direction component <i>top</i>.
     */
    public static final Dir T = new Dir() {

        /**
         * The field <tt>serialVersionUID</tt> contains the version number for
         * serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Magic method used during de-serialization to remap the object read.
         *
         * @return the constant
         */
        Object readResolve() {

            return T;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "T";
        }
    };

    /**
     * The field <tt>beginningOfLine</tt> contains the ...
     */
    private Dir beginningOfLine;

    /**
     * The field <tt>beginningOfPage</tt> contains the ...
     */
    private Dir beginningOfPage;

    /**
     * The field <tt>topOfLine</tt> contains the ...
     */
    private Dir topOfLine;

    /**
     * Creates a new object.
     *
     */
    private Direction() {

        super();
    }

    /**
     * Creates a new object.
     *
     * @param beginningOfPage the beginning of the page
     * @param beginningOfLine the beginning of the line
     * @param topOfLine the top of the line
     */
    public Direction(final Dir beginningOfPage, final Dir beginningOfLine,
            final Dir topOfLine) {

        super();
        this.beginningOfPage = beginningOfPage;
        this.beginningOfLine = beginningOfLine;
        this.topOfLine = topOfLine;

    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return beginningOfPage.toString() + beginningOfLine.toString()
                + topOfLine.toString();
    }

}
