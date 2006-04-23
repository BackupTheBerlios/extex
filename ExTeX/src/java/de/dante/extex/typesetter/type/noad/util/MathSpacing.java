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

package de.dante.extex.typesetter.type.noad.util;

import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.StyleNoad;
import de.dante.extex.typesetter.type.node.GlueNode;

/**
 * TODO gene: missing JavaDoc.
 *
 * <doc name="thinmuskip" type="register">
 * <h3>The Muskip Parameter <tt>\thinmuskip</tt></h3>
 * <p>
 *  The muskip parameter <tt>\thinmuskip</tt>
 *  TODO gene: documentation missing
 * </p>
 * </doc>
 *
 * <doc name="medmuskip" type="register">
 * <h3>The Muskip Parameter <tt>\medmuskip</tt></h3>
 * <p>
 *  The muskip parameter <tt>\medmuskip</tt>
 *  TODO gene: documentation missing
 * </p>
 * </doc>
 *
 * <doc name="thickmuskip" type="register">
 * <h3>The Muskip Parameter <tt>\thickmuskip</tt></h3>
 * <p>
 *  The muskip parameter <tt>\thickmuskip</tt>
 *  TODO gene: documentation missing
 * </p>
 * </doc>
 *
 *
 * @see "TTP [764]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public final class MathSpacing {

    /**
     * The field <tt>BIN</tt> contains the spacing class for binary operators.
     */
    public static final MathSpacing BIN = new MathSpacing(2, "33**3**3");

    /**
     * The field <tt>CLOSE</tt> contains the spacing class for closing.
     */
    public static final MathSpacing CLOSE = new MathSpacing(5, "02340001");

    /**
     * The field <tt>INNER</tt> contains the spacing class for inner math.
     */
    public static final MathSpacing INNER = new MathSpacing(7, "12341011");

    /**
     * The field <tt>OP</tt> contains the spacing class for operators.
     */
    public static final MathSpacing OP = new MathSpacing(1, "22*40001");

    /**
     * The field <tt>OPEN</tt> contains the spacing class for opening.
     */
    public static final MathSpacing OPEN = new MathSpacing(4, "00*00000");

    /**
     * The field <tt>ORD</tt> contains the spacing class for ordinary characters.
     */
    public static final MathSpacing ORD = new MathSpacing(0, "02340001");

    /**
     * The field <tt>PUNCT</tt> contains the spacing class for punctation marks.
     */
    public static final MathSpacing PUNCT = new MathSpacing(6, "11*11111");

    /**
     * The field <tt>REL</tt> contains the spacing class for relation symbols.
     */
    public static final MathSpacing REL = new MathSpacing(3, "44*04004");

    /**
     * The field <tt>UNDEF</tt> contains the undefined spacing class.
     */
    public static final MathSpacing UNDEF = new MathSpacing(-1, null);

    /**
     * The field <tt>id</tt> contains the internal id.
     */
    private int id;

    /**
     * The field <tt>spec</tt> contains the specification to determine the
     * actual distance to be inserted.
     */
    private String spec;

    /**
     * Creates a new object.
     *
     * @param id the internal id
     * @param spec the string to determine the actual distance to a following
     *  noad
     */
    private MathSpacing(final int id, final String spec) {

        super();
        this.id = id;
        this.spec = spec;
    }

    /**
     * Add some spacing to a list of nodes.
     *
     * @param previous the previous spacing class
     * @param list the list to add the spacing to
     * @param mathContext the math context
     *
     * @see "TTP [766]"
     */
    public void addClearance(final MathSpacing previous, final NodeList list,
            final MathContext mathContext) {

        if (spec == null || previous == null) {
            return;
        }

        Muskip x = null;

        switch (spec.charAt(previous.id)) {
            case '0':
                break;
            case '1':
                if (mathContext.getStyle().less(StyleNoad.SCRIPTSTYLE)) {
                    x = mathContext.getOptions().getMuskip("thinmuskip");
                }
                break;
            case '2':
                x = mathContext.getOptions().getMuskip("thinmuskip");
                break;
            case '3':
                if (mathContext.getStyle().less(StyleNoad.SCRIPTSTYLE)) {
                    x = mathContext.getOptions().getMuskip("medmuskip");
                }
                break;
            case '4':
                if (mathContext.getStyle().less(StyleNoad.SCRIPTSTYLE)) {
                    x = mathContext.getOptions().getMuskip("thickmuskip");
                }
                break;
            default:
                throw new ImpossibleException("mlist4");
        }
        if (x != null && !x.isZero()) {
            list.add(new GlueNode(mathContext.convert(x), true));
        }
    }

}
