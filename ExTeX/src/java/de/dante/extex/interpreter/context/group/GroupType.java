/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context.group;

import java.io.Serializable;

/**
 * TODO gene: missing JavaDoc.
 *
 *  0: bottom level (no group)
 *  1: simple group
 *  2: hbox group
 *  3: adjusted hbox group
 *  4: vbox group
 *  5: vtop group
 *  6: align group
 *  7: no align group
 *  8: output group
 *  9: math group
 * 10: disc group
 * 11: insert group
 * 12: vcenter group
 * 13: math choice group
 * 14: semi simple group
 * 15: math shift group
 * 16: math left group
 *
 * TODO gene: apply readResolve()
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class GroupType implements Serializable {

    /**
     * The field <tt>ADJUSTED_HBOX_GROUP</tt> contains the ...
     */
    public static final GroupType ADJUSTED_HBOX_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitAdjustedHboxGroup(arg);
        }
    };

    /**
     * The field <tt>ALIGN_GROUP</tt> contains the ...
     */
    public static final GroupType ALIGN_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitAlignGroup(arg);
        }
    };

    /**
     * The field <tt>BOTTOM_LEVEL_GROUP</tt> contains the ...
     */
    public static final GroupType BOTTOM_LEVEL_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitBottomLevelGroup(arg);
        }
    };

    /**
     * The field <tt>DISC_GROUP</tt> contains the ...
     */
    public static final GroupType DISC_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitDiscGroup(arg);
        }
    };

    /**
     * The field <tt>HBOX_GROUP</tt> contains the ...
     */
    public static final GroupType HBOX_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitHboxGroup(arg);
        }
    };

    /**
     * The field <tt>INSERT_GROUP</tt> contains the ...
     */
    public static final GroupType INSERT_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitInsertGroup(arg);
        }
    };

    /**
     * The field <tt>MATH_CHOICE_GROUP</tt> contains the ...
     */
    public static final GroupType MATH_CHOICE_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitMathChoiceGroup(arg);
        }
    };

    /**
     * The field <tt>MATH_GROUP</tt> contains the ...
     */
    public static final GroupType MATH_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitMathGroup(arg);
        }
    };

    /**
     * The field <tt>MATH_LEFT_GROUP</tt> contains the ...
     */
    public static final GroupType MATH_LEFT_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitMathLeftGroup(arg);
        }
    };

    /**
     * The field <tt>MATH_SHIFT_GROUP</tt> contains the ...
     */
    public static final GroupType MATH_SHIFT_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitMathShiftGroup(arg);
        }
    };

    /**
     * The field <tt>NO_ALIGN_GROUP</tt> contains the ...
     */
    public static final GroupType NO_ALIGN_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitNoAlignGroup(arg);
        }
    };

    /**
     * The field <tt>OUTPUT_GROUP</tt> contains the ...
     */
    public static final GroupType OUTPUT_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitOutputGroup(arg);
        }
    };

    /**
     * The field <tt>SEMI_SIMPLE_GROUP</tt> contains the ...
     */
    public static final GroupType SEMI_SIMPLE_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitSemiSimpleGroup(arg);
        }
    };

    /**
     * The field <tt>SIMPLE_GROUP</tt> contains the ...
     */
    public static final GroupType SIMPLE_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitSimpleGroup(arg);
        }
    };

    /**
     * The field <tt>VBOX_GROUP</tt> contains the ...
     */
    public static final GroupType VBOX_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitVboxGroup(arg);
        }
    };

    /**
     * The field <tt>VCENTER_GROUP</tt> contains the ...
     */
    public static final GroupType VCENTER_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitVcenterGroup(arg);
        }
    };

    /**
     * The field <tt>VTOP_GROUP</tt> contains the ...
     */
    public static final GroupType VTOP_GROUP = new GroupType() {

        /**
         * @see de.dante.extex.interpreter.context.group.GroupType#visit(
         *      de.dante.extex.interpreter.context.group.GroupTypeVisitor,
         *      java.lang.Object)
         */
        public Object visit(final GroupTypeVisitor visitor, final Object arg) {

            return visitor.visitVtopGroup(arg);
        }
    };

    /**
     * Creates a new object.
     * This constructor is private to avoid that other instances beside the
     * symbolic constants are created.
     */
    private GroupType() {

        super();
    }

    /**
     * This is the entry point to separate the different group types.
     *
     * @param visitor the group type visitor
     * @param arg an arbitrary argument to be used by the visitor
     */
    public abstract Object visit(GroupTypeVisitor visitor, Object arg);

}
