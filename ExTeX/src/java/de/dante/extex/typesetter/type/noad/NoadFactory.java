/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type.noad;

import de.dante.extex.typesetter.type.math.MathClass;
import de.dante.extex.typesetter.type.math.MathClassVisitor;

/**
 * This class is a factory for CharNoades.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class NoadFactory {

    /**
     * Inner class for the visiting of a MathClass.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.5 $
     */
    private static final class ClassVisitor implements MathClassVisitor {

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitBinary(
         *      java.lang.Object)
         */
        public Object visitBinary(final Object arg) {

            return new BinaryNoad((Noad) arg);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitClosing(
         *      java.lang.Object)
         */
        public Object visitClosing(final Object arg) {

            return new CloseNoad((Noad) arg);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitLarge(
         *      java.lang.Object)
         */
        public Object visitLarge(final Object arg) {

            // TODO gene: visitLarge() unimplemented
            throw new RuntimeException("unimplemented");
            //return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitOpening(
         *      java.lang.Object)
         */
        public Object visitOpening(final Object arg) {

            return new OpenNoad((Noad) arg);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitOrdinary(
         *      java.lang.Object)
         */
        public Object visitOrdinary(final Object arg) {

            return new CharNoad((MathGlyph) arg);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitPunctation(
         *      java.lang.Object)
         */
        public Object visitPunctation(final Object arg) {

            return new PunctationNoad((Noad) arg);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitRelation(
         *      java.lang.Object)
         */
        public Object visitRelation(final Object arg) {

            return new RelationNoad((Noad) arg);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitVariable(
         *      java.lang.Object)
         */
        public Object visitVariable(final Object arg) {

            // TODO gene: difference to ordinary ??
            return new CharNoad((MathGlyph) arg);
        }
    }

    /**
     * The constant <tt>CLASS_MASK</tt> contains the mask for the class value.
     */
    private static final int CLASS_MASK = 0xf;

    /**
     * The constant <tt>CLASS_SHIFT</tt> contains the shift for the class value.
     */
    private static final int CLASS_SHIFT = 12;

    /**
     * The constant <tt>GLYPH_MASK</tt> contains the mask for the math glyph.
     */
    private static final int GLYPH_MASK = 0xfff;

    /**
     * The constant <tt>VISITOR</tt> contains the math class visitor.
     */
    private static final MathClassVisitor VISITOR = new ClassVisitor();

    /**
     * Creates a new object.
     *
     */
    public NoadFactory() {

        super();
    }

    /**
     * Provides an instance of a CharNoad.
     *
     * @param mc the code of the character to use
     *
     * @return an instance of a CharNoad
     */
    public Noad getNoad(final long mc) {

        return getNoad(MathClass
                .getMathClass((int) ((mc >> CLASS_SHIFT) & CLASS_MASK)), //
                new MathGlyph((int) (mc & GLYPH_MASK)));

    }

    /**
     * Provides an instance of a CharNoad.
     *
     * @param mathClass the math class
     * @param nucleus the character the character
     *
     * @return an instance of a CharNoad
     */
    public Noad getNoad(final MathClass mathClass, final Noad nucleus) {

        return (Noad) mathClass.visit(VISITOR, nucleus);
    }

}