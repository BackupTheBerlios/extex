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

import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This Noad represents an operator.
 *
 * @see "TTP [682]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.14 $
 */
public class OperatorNoad extends AbstractNucleusNoad {

    /**
     * The field <tt>limits</tt> contains the indicator for limits. This can
     * either be unset or it can have the value TRUE for \limits and FALSE for
     * \nolimits.
     */
    private Boolean limits = null;

    /**
     * Creates a new object.
     *
     * @param nucleus the nucleus
     */
    public OperatorNoad(final Noad nucleus) {

        super(nucleus);
    }

    /**
     * Getter for limits.
     *
     * @return the limits
     */
    public Boolean getLimits() {

        return this.limits;
    }

    /**
     * Setter for limits.
     *
     * @param limits the limits to set
     */
    public void setLimits(final Boolean limits) {

        this.limits = limits;
    }

    /**
     * @see "TTP [696]"
     * @see de.dante.extex.typesetter.type.noad.Noad#toStringAdd(
     *      java.lang.StringBuffer,
     *      int)
     */
    protected void toStringAdd(final StringBuffer sb, final int depth) {

        sb.append("mathop");
        if (limits == Boolean.TRUE) {
            sb.append("\\limits");
        } else if (limits == Boolean.FALSE) {
            sb.append("\\nolimits");
        }
    }

    /**
     * @see "TTP [749,750]"
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void typeset(final NodeList list, final MathContext mathContext,
            final TypesetterOptions context) throws ConfigurationException {

        //TODO gene: typeset() unimplemented
        throw new RuntimeException("unimplemented");
    }

}