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
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.noad.util.MathSpacing;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This noad provides a switch construction depending on the current style.
 *
 * @see "TTP [689]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.17 $
 */
public class ChoiceNoad implements Noad {

    /**
     * The field <tt>display</tt> contains the noads used in display style.
     */
    private Noad display;

    /**
     * The field <tt>script</tt> contains the noads used in script style.
     */
    private Noad script;

    /**
     * The field <tt>scriptScript</tt> contains the noads used in scriptscript
     * style.
     */
    private Noad scriptScript;

    /**
     * The field <tt>spacingClass</tt> contains the spacing class.
     */
    private MathSpacing spacingClass = MathSpacing.UNDEF;

    /**
     * The field <tt>text</tt> contains the noads used in text style.
     */
    private Noad text;

    /**
     * Creates a new object.
     *
     * @param displayMath the noads used in display style
     * @param textMath the noads used in text style
     * @param scriptMath the noads used in script style
     * @param scriptscriptMath the noads used in scriptscript style
     */
    public ChoiceNoad(final Noad displayMath, final Noad textMath,
            final Noad scriptMath, final Noad scriptscriptMath) {

        super();
        display = displayMath;
        text = textMath;
        script = scriptMath;
        scriptScript = scriptscriptMath;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSpacingClass()
     */
    public MathSpacing getSpacingClass() {

        return spacingClass;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSubscript()
     */
    public Noad getSubscript() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#getSuperscript()
     */
    public Noad getSuperscript() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#setSubscript(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void setSubscript(final Noad subscript) {

    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#setSuperscript(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void setSuperscript(final Noad superscript) {

    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        toString(sb, Integer.MAX_VALUE);
    }

    /**
     * @see "TTP [695]"
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer, int)
     */
    public void toString(final StringBuffer sb, final int depth) {

        sb.append("\\mathchoice");
        sb.append("D");
        display.toString(sb, depth);
        sb.append("T");
        text.toString(sb, depth);
        sb.append("S");
        script.toString(sb, depth);
        sb.append("s");
        scriptScript.toString(sb, depth);
    }

    /**
     * @see "TTP [731]"
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.noad.Noad,
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger)
     */
    public void typeset(final Noad previousNoad, final NoadList noads,
            final int index, final NodeList list,
            final MathContext mathContext, final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        StyleNoad style = mathContext.getStyle();

        if (style == StyleNoad.DISPLAYSTYLE) {
            display.typeset(previousNoad, noads, index, list, mathContext,
                    logger);
            spacingClass = display.getSpacingClass();
        } else if (style == StyleNoad.TEXTSTYLE) {
            text.typeset(previousNoad, noads, index, list, mathContext, logger);
            spacingClass = text.getSpacingClass();
        } else if (style == StyleNoad.SCRIPTSTYLE) {
            script.typeset(previousNoad, noads, index, list, mathContext,
                    logger);
            spacingClass = script.getSpacingClass();
        } else if (style == StyleNoad.SCRIPTSCRIPTSTYLE) {
            scriptScript.typeset(previousNoad, noads, index, list, mathContext,
                    logger);
            spacingClass = scriptScript.getSpacingClass();
        } else {
            throw new ImpossibleException("illegal style");
        }
    }

}
