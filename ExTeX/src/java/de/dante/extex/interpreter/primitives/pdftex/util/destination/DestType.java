/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.pdftex.util.destination;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.pdftex.InterpreterPdftexDestinationTypeException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.node.RuleNode;

/**
 * This is the abstract base class for destination types in PDF.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public abstract class DestType {

    /**
     * The constant <tt>FIT</tt> contains the destination type fit.
     */
    public static final DestType FIT = new DestType() {

        /**
         * @see de.dante.extex.interpreter.primitives.pdftex.util.destType.DestType#visit(
         *      de.dante.extex.interpreter.primitives.pdftex.util.destType.DestTypeVisitor)
         */
        public Object visit(final DestinationVisitor visitor) {

            return visitor.visitFit(this);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "fit";
        }

    };

    /**
     * The constant <tt>FITB</tt> contains the destination type fitb.
     */
    public static final DestType FITB = new DestType() {

        /**
         * @see de.dante.extex.interpreter.primitives.pdftex.util.destType.DestType#visit(
         *      de.dante.extex.interpreter.primitives.pdftex.util.destType.DestTypeVisitor)
         */
        public Object visit(final DestinationVisitor visitor) {

            return visitor.visitFitb(this);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "fitb";
        }

    };

    /**
     * The constant <tt>FITBH</tt> contains the destination type fitbh.
     */
    public static final DestType FITBH = new DestType() {

        /**
         * @see de.dante.extex.interpreter.primitives.pdftex.util.destType.DestType#visit(
         *      de.dante.extex.interpreter.primitives.pdftex.util.destType.DestTypeVisitor)
         */
        public Object visit(final DestinationVisitor visitor) {

            return visitor.visitFitbh(this);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "fitbh";
        }

    };

    /**
     * The constant <tt>FITBV</tt> contains the destination type fitbv.
     */
    public static final DestType FITBV = new DestType() {

        /**
         * @see de.dante.extex.interpreter.primitives.pdftex.util.destType.DestType#visit(
         *      de.dante.extex.interpreter.primitives.pdftex.util.destType.DestTypeVisitor)
         */
        public Object visit(final DestinationVisitor visitor) {

            return visitor.visitFitbv(this);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "fitbv";
        }

    };

    /**
     * The constant <tt>FITH</tt> contains the destination type fith.
     */
    public static final DestType FITH = new DestType() {

        /**
         * @see de.dante.extex.interpreter.primitives.pdftex.util.destType.DestType#visit(
         *      de.dante.extex.interpreter.primitives.pdftex.util.destType.DestTypeVisitor)
         */
        public Object visit(final DestinationVisitor visitor) {

            return visitor.visitFith(this);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "fith";
        }

    };

    /**
     * The constant <tt>FITV</tt> contains the destination type fitv.
     */
    public static final DestType FITV = new DestType() {

        /**
         * @see de.dante.extex.interpreter.primitives.pdftex.util.destType.DestType#visit(
         *      de.dante.extex.interpreter.primitives.pdftex.util.destType.DestTypeVisitor)
         */
        public Object visit(final DestinationVisitor visitor) {

            return visitor.visitFitv(this);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "fitv";
        }

    };

    /**
     * The constant <tt>XYZ</tt> contains the destination type xyz.
     */
    public static final DestType XYZ = new DestType() {

        /**
         * @see de.dante.extex.interpreter.primitives.pdftex.util.destType.DestType#visit(
         *      de.dante.extex.interpreter.primitives.pdftex.util.destType.DestTypeVisitor)
         */
        public Object visit(final DestinationVisitor visitor) {

            return visitor.visitXyz(this);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "xyz";
        }

    };

    /**
     * Parse a destination type.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param name the name of the invoking primitive
     *
     * @return the destination type
     *
     * @throws InterpreterException in case of an error
     */
    public static DestType parseDestType(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final String name) throws InterpreterException {

        if (source.getKeyword(context, "xyz")) {
            return XYZ;
        } else if (source.getKeyword(context, "fit")) {
            return FIT;
        } else if (source.getKeyword(context, "fith")) {
            return FITH;
        } else if (source.getKeyword(context, "fitv")) {
            return FITV;
        } else if (source.getKeyword(context, "fitb")) {
            return FITB;
        } else if (source.getKeyword(context, "fitbv")) {
            return FITBV;
        } else if (source.getKeyword(context, "fitbh")) {
            return FITBH;
        } else if (source.getKeyword(context, "fitr")) {

            Dimen width = null;
            Dimen height = null;
            Dimen depth = null;

            for (;;) {
                if (source.getKeyword(context, "width")) {
                    width = Dimen.parse(context, source, typesetter);
                } else if (source.getKeyword(context, "height")) {
                    height = Dimen.parse(context, source, typesetter);
                } else if (source.getKeyword(context, "depth")) {
                    depth = Dimen.parse(context, source, typesetter);
                } else {
                    break;
                }
            }

            return new FitrDestType(new RuleNode(width, height, depth, null,
                    true));

        } else if (source.getKeyword(context, "zoom")) {
            long zoom = Count.scanInteger(context, source, typesetter);
            return new ZoomDestType(zoom);
        }

        throw new InterpreterPdftexDestinationTypeException(name);
    }

    /**
     * Creates a new object.
     */
    protected DestType() {

        super();
    }

    /**
     * This method is the entry point for the visitor pattern.
     *
     * @param visitor the visitor to call back
     *
     * @return an arbitrary return object
     */
    public abstract Object visit(final DestinationVisitor visitor);

}
