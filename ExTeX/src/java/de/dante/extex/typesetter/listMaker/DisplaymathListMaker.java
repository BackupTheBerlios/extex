/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.listMaker;

import de.dante.extex.interpreter.exception.CantUseInException;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.noad.MathList;
import de.dante.extex.typesetter.type.noad.StyleNoad;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.util.GeneralException;

/**
 * This is the list maker for the display math formulae.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class DisplaymathListMaker extends MathListMaker implements EqConsumer {

    /**
     * The field <tt>eqno</tt> contains the math list for the equation number.
     * It is <code>null</code> if no equation number is set.
     */
    private MathList eqno = null;

    /**
     * The field <tt>leq</tt> contains the indicator for the side of the
     * equation number. A value of <code>true</code> indicates an equation
     * number on the left side.
     */
    private boolean leq = false;

    /**
     * Creates a new object.
     *
     * @param manager the manager to ask for global changes
     */
    public DisplaymathListMaker(final ListManager manager) {

        super(manager);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#close(TypesetterOptions)
     */
    public NodeList close(final TypesetterOptions context) {

        HorizontalListNode list = new HorizontalListNode();

        getNoades().typeset(list, new MathContext(StyleNoad.DISPLAYSTYLE, context),
                context);

        if (eqno != null) {
            //TODO gene: unimplemented
            throw new RuntimeException("unimplemented");
        }
        return list;
    }

    /**
     * This method switches the collection of material to the target "equation
     * number".
     *
     * @param left the indicator on which side to produce the equation number.
     *  A value <code>true</code> indicates that the left side should be used.
     *
     * @throws GeneralException in case on an error
     *
     * @see de.dante.extex.typesetter.listMaker.EqConsumer#switchToNumber(boolean)
     */
    public void switchToNumber(final boolean left) throws GeneralException {

        if (eqno != null) {
            throw new CantUseInException(null, null);
        }

        leq = left;
        eqno = new MathList();
        setInsertionPoint(eqno);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getMode()
     */
    public Mode getMode() {

        return Mode.DISPLAYMATH;
    }

}
