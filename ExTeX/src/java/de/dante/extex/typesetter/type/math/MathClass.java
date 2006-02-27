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
 *
 */

package de.dante.extex.typesetter.type.math;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * This class provides the classification of mathematical characters.
 * In fact it is a finite enumeration which exposes the values as constants.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public abstract class MathClass implements Serializable {

    /**
     * This is a inner class for a binary operator.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.4 $
     */
    private static final class BinaryMathClass extends MathClass {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2005L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return MathClass.BINARY;
        }

        /**
         * Append the printable representation of the current instance to the
         * string buffer.
         *
         * @param sb the target string buffer
         */
        public void toString(final StringBuffer sb) {

            sb.append("bin");
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClass#visit(
         *      MathClassVisitor, java.lang.Object)
         */
        public Object visit(final MathClassVisitor visitor, final Object arg,
                final Object arg2) {

            return visitor.visitBinary(arg, arg2);
        }
    }

    /**
     * This is a inner class for closing.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.4 $
     */
    private static final class ClosingMathClass extends MathClass {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2005L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return MathClass.CLOSING;
        }

        /**
         * Append the printable representation of the current instance to the
         * string buffer.
         *
         * @param sb the target string buffer
         */
        public void toString(final StringBuffer sb) {

            sb.append("close");
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClass#visit(
         *      MathClassVisitor, java.lang.Object)
         */
        public Object visit(final MathClassVisitor visitor, final Object arg,
                final Object arg2) {

            return visitor.visitClosing(arg, arg2);
        }
    }

    /**
     * This is a inner class for large operators.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.4 $
     */
    private static final class LargeMathClass extends MathClass {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2005L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return MathClass.LARGE;
        }

        /**
         * Append the printable representation of the current instance to the
         * string buffer.
         *
         * @param sb the target string buffer
         */
        public void toString(final StringBuffer sb) {

            sb.append("large");
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClass#visit(
         *      MathClassVisitor, java.lang.Object, java.lang.Object)
         */
        public Object visit(final MathClassVisitor visitor, final Object arg,
                final Object arg2) {

            return visitor.visitLarge(arg, arg2);
        }
    }

    /**
     * This is a inner class for opening.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.4 $
     */
    private static final class OpeningMathClass extends MathClass {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2005L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return MathClass.OPENING;
        }

        /**
         * Append the printable representation of the current instance to the
         * string buffer.
         *
         * @param sb the target string buffer
         */
        public void toString(final StringBuffer sb) {

            sb.append("open");
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClass#visit(
         *      MathClassVisitor, java.lang.Object, java.lang.Object)
         */
        public Object visit(final MathClassVisitor visitor, final Object arg,
                final Object arg2) {

            return visitor.visitOpening(arg, arg2);
        }
    }

    /**
     * This is a inner class for ordinary characters.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.4 $
     */
    private static final class OrdinaryMathClass extends MathClass {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2005L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return MathClass.ORDINARY;
        }

        /**
         * Append the printable representation of the current instance to the
         * string buffer.
         *
         * @param sb the target string buffer
         */
        public void toString(final StringBuffer sb) {

            sb.append("ord");
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClass#visit(
         *      MathClassVisitor, java.lang.Object, java.lang.Object)
         */
        public Object visit(final MathClassVisitor visitor, final Object arg,
                final Object arg2) {

            return visitor.visitOrdinary(arg, arg2);
        }
    }

    /**
     * This is a inner class for punctation marks.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.4 $
     */
    private static final class PunctationMathClass extends MathClass {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2005L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return MathClass.PUNCTUATION;
        }

        /**
         * Append the printable representation of the current instance to the
         * string buffer.
         *
         * @param sb the target string buffer
         */
        public void toString(final StringBuffer sb) {

            sb.append("punct");
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClass#visit(
         *      MathClassVisitor, java.lang.Object, java.lang.Object)
         */
        public Object visit(final MathClassVisitor visitor, final Object arg,
                final Object arg2) {

            return visitor.visitPunctation(arg, arg2);
        }
    }

    /**
     * This is a inner class for relation symbols.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.4 $
     */
    private static final class RelationMathClass extends MathClass {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2005L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return MathClass.RELATION;
        }

        /**
         * Append the printable representation of the current instance to the
         * string buffer.
         *
         * @param sb the target string buffer
         */
        public void toString(final StringBuffer sb) {

            sb.append("rel");
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClass#visit(
         *      MathClassVisitor, java.lang.Object, java.lang.Object)
         */
        public Object visit(final MathClassVisitor visitor, final Object arg,
                final Object arg2) {

            return visitor.visitRelation(arg, arg2);
        }
    }

    /**
     * This is a inner class for variable width characters.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.4 $
     */
    private static final class VariableMathClass extends MathClass {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2005L;

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return MathClass.VARIABLE;
        }

        /**
         * Append the printable representation of the current instance to the
         * string buffer.
         *
         * @param sb the target string buffer
         */
        public void toString(final StringBuffer sb) {

            sb.append("var");
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClass#visit(
         *      MathClassVisitor, java.lang.Object, java.lang.Object)
         */
        public Object visit(final MathClassVisitor visitor, final Object arg,
                final Object arg2) {

            return visitor.visitVariable(arg, arg2);
        }
    }

    /**
     * The field <tt>BINARY</tt> contains the instance representing the binary
     * class. This class has the code 2 in <logo>TeX</logo>.
     */
    public static final MathClass BINARY = new BinaryMathClass();

    /**
     * The field <tt>CLOSING</tt> contains the instance representing the closing
     * class. This class has the code 5 in <logo>TeX</logo>.
     */
    public static final MathClass CLOSING = new ClosingMathClass();

    /**
     * The field <tt>LARGE</tt> contains the instance representing the large
     * class. This class has the code 1 in <logo>TeX</logo>.
     */
    public static final MathClass LARGE = new LargeMathClass();

    /**
     * The field <tt>OPENING</tt> contains the instance representing the opening
     * class. This class has the code 4 in <logo>TeX</logo>.
     */
    public static final MathClass OPENING = new OpeningMathClass();

    /**
     * The field <tt>ORDINARY</tt> contains the instance representing the
     * ordinary class. This class has the code 0 in <logo>TeX</logo>.
     */
    public static final MathClass ORDINARY = new OrdinaryMathClass();

    /**
     * The field <tt>PUNCTUATION</tt> contains the instance representing the
     * punctation class. This class has the code 6 in <logo>TeX</logo>.
     */
    public static final MathClass PUNCTUATION = new PunctationMathClass();

    /**
     * The field <tt>RELATION</tt> contains the instance representing the
     * relation class. This class has the code 3 in <logo>TeX</logo>.
     */
    public static final MathClass RELATION = new RelationMathClass();

    /**
     * The field <tt>VARIABLE</tt> contains the instance representing the
     * variable width class. This class has the code 7 in <logo>TeX</logo>.
     */
    public static final MathClass VARIABLE = new VariableMathClass();

    /**
     * The field <tt>mc</tt> contains the mapping from <logo>TeX</logo> numbers
     * to instances.
     *
     * @see MathClass#getMathClass(int)
     */
    private static final MathClass[] MC = {ORDINARY, LARGE, BINARY, RELATION,
            OPENING, CLOSING, PUNCTUATION, VARIABLE};

    /**
     * Factory method for the math class which maps the <logo>TeX</logo>
     * encoding into the appropriate instance. The following table gives a
     * mapping from <logo>TeX</logo> numbers to instances:
     * <table>
     *  <tr>
     *   <td>0</td>
     *   <td>{@link de.dante.extex.typesetter.type.math.MathClass#ORDINARY ORDINARY}</td>
     *  </tr>
     *  <tr>
     *   <td>1</td>
     *   <td>{@link de.dante.extex.typesetter.type.math.MathClass#LARGE LARGE}</td>
     *  </tr>
     *  <tr>
     *   <td>2</td>
     *   <td>{@link de.dante.extex.typesetter.type.math.MathClass#BINARY BINARY}</td>
     *  </tr>
     *  <tr>
     *   <td>3</td>
     *   <td>{@link de.dante.extex.typesetter.type.math.MathClass#RELATION RELATION}</td>
     *  </tr>
     *  <tr>
     *   <td>4</td>
     *   <td>{@link de.dante.extex.typesetter.type.math.MathClass#OPENING OPENING}</td>
     *  </tr>
     *  <tr>
     *   <td>5</td>
     *   <td>{@link de.dante.extex.typesetter.type.math.MathClass#CLOSING CLOSING}</td>
     *  </tr>
     *  <tr>
     *   <td>6</td>
     *   <td>{@link de.dante.extex.typesetter.type.math.MathClass#PUNCTATION PUNCTATION}</td>
     *  </tr>
     *  <tr>
     *   <td>7</td>
     *   <td>{@link de.dante.extex.typesetter.type.math.MathClass#VARIABLE VARIABLE}</td>
     *  </tr>
     * </table>
     *
     *
     * @param n the <logo>TeX</logo> encoded index of the class
     *
     * @return the MathClass instance corresponding to the <logo>TeX</logo>
     *  code
     */
    public static final MathClass getMathClass(final int n) {

        return MC[n];
    }

    /**
     * Creates a new object.
     */
    protected MathClass() {

        super();
    }

    /**
     * Append the printable representation of the current instance to the
     * string buffer.
     *
     * @param sb the target string buffer
     */
    public abstract void toString(final StringBuffer sb);

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }

    /**
     * Call a method in the visitor depending on the type.
     * This method is the entry point for the visitor pattern.
     *
     * @param visitor the visitor to call
     * @param arg an arbitrary argument passed to the visitor
     * @param arg2 an arbitrary second argument passed to the visitor
     *
     * @return an arbitrary return value
     */
    public abstract Object visit(final MathClassVisitor visitor,
            final Object arg, final Object arg2);

}
