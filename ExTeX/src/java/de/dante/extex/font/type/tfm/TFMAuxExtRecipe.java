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

package de.dante.extex.font.type.tfm;

/**
 * The data structure for extensible recipe from tfm file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class TFMAuxExtRecipe {

    /**
     * Create a new object.
     *
     * @param t top
     * @param m mid
     * @param b bot
     * @param r rep
     */
    public TFMAuxExtRecipe(final short t, final short m, final short b,
            final short r) {

        super();
        top = t;
        mid = m;
        bot = b;
        rep = r;
    }

    /**
     * Character code of the top part of extensible character.
     */
    private short top;

    /**
     * Character code of the middle part of extensible character.
     */
    private short mid;

    /**
     * Character code of the bottom part of extensible character.
     */
    private short bot;

    /**
     * Character code of the repeatable part of extensible character.
     */
    private short rep;

    /**
     * @return Returns the bot.
     */
    public short getBot() {

        return bot;
    }

    /**
     * @return Returns the mid.
     */
    public short getMid() {

        return mid;
    }

    /**
     * @return Returns the rep.
     */
    public short getRep() {

        return rep;
    }

    /**
     * @return Returns the top.
     */
    public short getTop() {

        return top;
    }
}