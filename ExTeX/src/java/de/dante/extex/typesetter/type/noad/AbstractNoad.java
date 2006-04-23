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

package de.dante.extex.typesetter.type.noad;

import java.util.logging.Logger;

import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.primitives.register.font.NumberedFont;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.noad.util.MathSpacing;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This is the abstract base class for Noads.
 * A {@link de.dante.extex.typesetter.type.noad.Noad Noad} is the intermediate
 * data structure which is used for processing mathematical material. Finally
 * Noads are translated into {@link de.dante.extex.typesetter.type.Node Node}s.
 * Thus Noad will never arrive at the DocumentWriter.
 *
 *
 * <doc name="scriptspace" type="register">
 * <h3>The Dimen Parameter <tt>\scriptspace</tt></h3>
 * <p>
 *  The dimen parameter <tt>\scriptspace</tt>
 *  TODO gene: documentation missing
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.16 $
 */
public abstract class AbstractNoad implements Noad {

    public static final String MATH_X_HEIGHT = "5";

    public static final String MATH_QUAD = "6";

    public static final String NUM1 = "8";

    public static final String NUM2 = "9";

    public static final String NUM3 = "10";

    public static final String DENOM1 = "11";

    public static final String DENOM2 = "12";

    public static final String SUP1 = "13";

    public static final String SUP2 = "14";

    public static final String SUP3 = "15";

    public static final String SUB1 = "16";

    public static final String SUB2 = "17";

    public static final String SUP_DROP = "18";

    public static final String SUB_DROP = "19";

    public static final String DELIM1 = "20";

    public static final String DELIM2 = "21";

    public static final String AXIS_HEIGHT = "22";

    /**
     * Get a font dimen value from the symbol font.
     *
     * @param style the current style of processing
     * @param options the typesetter options in effect
     *
     * @return the font dimen
     *
     * @throws TypesetterException in case of an error. The exception will have
     *  a cause exception in it containing a HelpingException
     *
     * @see "TTP [700]"
     */
    protected static FixedDimen symbolValue(final String no,
            final StyleNoad style, final TypesetterOptions options)
            throws TypesetterException {

        Font font = options.getFont(NumberedFont.key(options, //
                style.getFontName(), "2"));
        Dimen value;
        if (font == null || (value = font.getFontDimen(no)) == null) {
            throw new TypesetterException(new HelpingException(
                    staticLocalizer(), "TTP.InsufficientSymbolFonts"));
        }
        return value;
    }

    /**
     * Extract the default rule thickness as used in <tt>\over</tt>,
     * <tt>\overline</tt>, and <tt>&#x5c;underline</tt>.
     * This value is taken from the font dimen 8 of the extension font.
     *
     * @param style the current style of processing
     * @param options the typesetter options in effect
     *
     * @return the default rule thickness
     *
     * @throws TypesetterException in case of an error. The exception will have
     *  a cause exception in it containing a HelpingException
     *
     * @see "TTP [708]"
     */
    protected static FixedDimen defaultRuleThickness(final StyleNoad style,
            final TypesetterOptions options) throws TypesetterException {

        Font font = options.getFont(NumberedFont.key(options, //
                style.getFontName(), "3"));
        Dimen thickness;
        if (font == null || (thickness = font.getFontDimen("8")) == null) {
            throw new TypesetterException(new HelpingException(
                    staticLocalizer(), "TTP.InsufficientExtensionFonts"));
        }
        return thickness;
    }

    /**
     * Arrange that the node has exactly the width given and the old content
     * is centered in it. If the node is a box then this can be achieved by
     * inserting the appropriate glue. Otherwise a new box has to be
     * constructed.
     *
     * @param node ...
     * @param width ...
     *
     * @see "TTP [715]"
     */
    protected static Node rebox(final Node node, final Dimen width) {

        if (node.getWidth().eq(width)) {
            return node;
        }
        if (node instanceof VerticalListNode) {

        }
        //TODO gene: incomplete
        return node;
    }

    /**
     * Getter for the localizer.
     *
     * @return the localizer
     */
    private static final Localizer staticLocalizer() {

        return LocalizerFactory.getLocalizer(AbstractNoad.class.getName());
    }

    /**
     * Print a noad to the string buffer preceded by some prefix if the noad
     * is not <code>null</code>.
     *
     * @param sb the target buffer
     * @param noad the noad to print
     * @param depth the recursion depth
     * @param prefix the prefix to print before the noad
     *
     * @see "TTP [692]"
     */
    protected static void toStringSubsidiaray(final StringBuffer sb,
            final Noad noad, final int depth, final String prefix) {

        if (noad != null) {
            sb.append(prefix);
            noad.toString(sb, depth);
        }
    }

    /**
     * The field <tt>spacingClass</tt> contains the class for spacing.
     */
    private MathSpacing spacingClass = MathSpacing.UNDEF;

    /**
     * The field <tt>subscript</tt> contains the subscript noad.
     */
    private Noad subscript = null;

    /**
     * The field <tt>superscript</tt> contains the superscript noad.
     */
    private Noad superscript = null;

    /**
     * Creates a new object.
     *
     */
    public AbstractNoad() {

        super();
    }

    /**
     * Getter for the localizer.
     *
     * @return the localizer
     */
    public Localizer getLocalizer() {

        return LocalizerFactory.getLocalizer(getClass().getName());
    }

    /**
     * Getter for spacingClass.
     *
     * @return the spacingClass
     */
    protected MathSpacing getSpacingClass() {

        return this.spacingClass;
    }

    /**
     * Getter for the subscript.
     *
     * @return the subscript.
     */
    public Noad getSubscript() {

        return this.subscript;
    }

    /**
     * Getter for the superscript.
     *
     * @return the superscript.
     */
    public Noad getSuperscript() {

        return this.superscript;
    }

    /**
     * Setter for spacingClass.
     *
     * @param spacingClass the spacingClass to set
     */
    protected void setSpacingClass(final MathSpacing spacingClass) {

        this.spacingClass = spacingClass;
    }

    /**
     * Setter for the subscript.
     *
     * @param subscript the subscript to set.
     */
    public void setSubscript(final Noad subscript) {

        this.subscript = subscript;
    }

    /**
     * Setter for the superscript.
     *
     * @param superscript the superscript to set.
     */
    public void setSuperscript(final Noad superscript) {

        this.superscript = superscript;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        final StringBuffer sb = new StringBuffer();
        toString(sb, Integer.MAX_VALUE);
        return sb.toString();
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        toString(sb, Integer.MAX_VALUE);
    }

    /**
     * @see "TTP [696]"
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer, int)
     */
    public void toString(final StringBuffer sb, final int depth) {

        if (depth < 0) {
            sb.append(" {}");
        } else {
            sb.append('\\');
            toStringAdd(sb, depth);
            toStringSubsidiaray(sb, superscript, depth, "^");
            toStringSubsidiaray(sb, subscript, depth, "_");
        }
    }

    /**
     * Add some information in the middle of the default toString method.
     *
     * @param sb the target string buffer
     * @param depth the recursion depth
     */
    protected void toStringAdd(final StringBuffer sb, final int depth) {

    }

    /**
     * Attach the subscripts and superscripts to the current hlist.
     *
     * @param node the current node
     * @param mc the math context
     * @param logger the logger
     *
     * @return ...
     *
     * @throws TypesetterException in case of an error
     * @throws ConfigurationException in case of an configuration error
     *
     * @see "TTP [756,757]"
     */
    protected Node makeScripts(final Node node, final MathContext mc,
            final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        if (superscript == null && subscript == null) {
            return node;
        }

        Dimen shiftDown = new Dimen();
        HorizontalListNode hlist;
        StyleNoad style = mc.getStyle();
        TypesetterOptions options = mc.getOptions();

        if (node instanceof CharNode) {
            hlist = new HorizontalListNode(node);
        } else if (node instanceof HorizontalListNode) {
            hlist = (HorizontalListNode) node;
            StyleNoad t = (mc.getStyle().less(StyleNoad.SCRIPTSTYLE)
                    ? StyleNoad.SCRIPTSTYLE
                    : StyleNoad.SCRIPTSCRIPTSTYLE);
            shiftDown.set(symbolValue(SUB_DROP, t, options));
            shiftDown.add(node.getDepth());

            shiftDown.max(symbolValue(SUB1, style, options));

        } else {
            throw new ImpossibleException("makeScripts");
        }

        HorizontalListNode sub = new HorizontalListNode();

        if (superscript == null) {
            // only subscript
            // @see "TTP [757]"
            mc.setStyle(style.sub());
            subscript.typeset(null, 0, sub, mc, options, logger);
            sub.getWidth().add(options.getDimenOption("scriptspace"));
            mc.setStyle(style);

            Dimen clr = new Dimen();
            clr.abs(symbolValue(MATH_X_HEIGHT, style, options));
            clr.multiply(-4, 5);
            clr.add(node.getHeight());
            shiftDown.max(clr);
            sub.setShift(shiftDown);

            hlist.add(sub);
            return hlist;
        } else if (subscript == null) {
            // only superscript
        } else {
            // both subscript and superscript
        }

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

}
