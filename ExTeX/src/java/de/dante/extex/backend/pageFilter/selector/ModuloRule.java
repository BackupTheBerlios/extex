/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend.pageFilter.selector;

/**
 * This rule checks that a number has a given remainder when divided by a
 * certain divider.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
class ModuloRule implements Rule {

    /**
     * The field <tt>mod</tt> contains the divider.
     */
    private int mod;

    /**
     * The field <tt>rem</tt> contains the remainder.
     */
    private int rem;

    /**
     * Creates a new object.
     *
     * @param mod the divider
     * @param rem the remainder
     */
    public ModuloRule(final int mod, final int rem) {

        super();
        this.mod = mod;
        this.rem = rem;
    }

    /**
     * @see de.dante.extex.backend.pageFilter.PageSelector.Rule#check(int)
     */
    public boolean check(final int value) {

        return value % mod == rem;
    }

}