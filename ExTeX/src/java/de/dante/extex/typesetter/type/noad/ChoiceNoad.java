/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;

/**
 * ...
 *
 * @see "TTP [689]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ChoiceNoad implements Noad {

    /**
     * The field <tt>display</tt> contains the noads used in display style.
     */
    private MathList display;

    /**
     * The field <tt>text</tt> contains the noads used in text style.
     */
    private MathList text;

    /**
     * The field <tt>script</tt> contains the noads used in script style.
     */
    private MathList script;

    /**
     * The field <tt>scriptScript</tt> contains the noads used in scriptscript
     * style.
     */
    private MathList scriptScript;

    /**
     * Creates a new object.
     *
     * @param displayMath the noads used in display style
     * @param textMath the noads used in text style
     * @param scriptMath the noads used in script style
     * @param scriptscriptMath the noads used in scriptscript style
     */
    public ChoiceNoad(final MathList displayMath, final MathList textMath,
            final MathList scriptMath, final MathList scriptscriptMath) {

        super();
        display = displayMath;
        text = textMath;
        script = scriptMath;
        scriptScript = scriptscriptMath;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(MathContext)
     */
    public NodeList typeset(final MathContext mathContext) {

        StyleNoad style = mathContext.getStyle();

        if (style == StyleNoad.DISPLAYSTYLE) {
            return display.typeset(mathContext);
        } else if (style == StyleNoad.TEXTSTYLE) {
            return text.typeset(mathContext);
        } else if (style == StyleNoad.SCRIPTSTYLE) {
            return script.typeset(mathContext);
        } else if (style == StyleNoad.SCRIPTSCRIPTSTYLE) {
            return scriptScript.typeset(mathContext);
        }

        throw new RuntimeException("internal error"); //TODO error handling
    }

}