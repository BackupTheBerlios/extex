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

import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;

/**
 * This noad provides a switch construction depending on the current style.
 *
 * @see "TTP [689]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
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
     * @see "TTP [695]"
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        sb.append("\\mathchoice");
        sb.append(" D");
        display.toString(sb);
        sb.append(" T");
        text.toString(sb);
        sb.append(" S");
        script.toString(sb);
        sb.append(" s");
        scriptScript.toString(sb);
        //TODO gene: Check

    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer, int)
     */
    public void toString(final StringBuffer sb, final int depth) {

        toString(sb);
    }

    /**
     * @see "TTP [731]"
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void typeset(final NodeList list, final MathContext mathContext,
            final TypesetterOptions context) {

        StyleNoad style = mathContext.getStyle();

        if (style == StyleNoad.DISPLAYSTYLE) {
            display.typeset(list, mathContext, context);
        } else if (style == StyleNoad.TEXTSTYLE) {
            text.typeset(list, mathContext, context);
        } else if (style == StyleNoad.SCRIPTSTYLE) {
            script.typeset(list, mathContext, context);
        } else if (style == StyleNoad.SCRIPTSCRIPTSTYLE) {
            scriptScript.typeset(list, mathContext, context);
        } else {
            throw new ImpossibleException("illegal style");
        }
    }

}