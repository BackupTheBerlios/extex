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

package de.dante.extex.font.type.vf.command;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import de.dante.extex.font.FontFactory;
import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.type.tfm.TFMFont;
import de.dante.extex.font.type.vf.exception.VFWrongCodeException;
import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * Abstract class for all vf commands.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public abstract class VFCommand implements XMLConvertible, Serializable {

    /**
     * pre
     */
    public static final int PRE = 247;

    /**
     * fnt def 1
     */
    public static final int FNT_DEF_1 = 243;

    /**
     * fnt def 2
     */
    public static final int FNT_DEF_2 = 244;

    /**
     * fnt def 3
     */
    public static final int FNT_DEF_3 = 245;

    /**
     * fnt def 4
     */
    public static final int FNT_DEF_4 = 246;

    /**
     * minimun character nr
     */
    public static final int MIN_CHARACTER = 0;

    /**
     * maximum character nr
     */
    public static final int MAX_CHARACTER = 242;

    /**
     * post
     */
    public static final int POST = 248;

    /**
     * Shift 8
     */
    public static final int SHIFT8 = 8;

    /**
     * Shift 16
     */
    public static final int SHIFT16 = 16;

    /**
     * the command code
     */
    private int ccode;

    /**
     * Create a new object.
     * (only from subclasses)
     *
     * @param c the command code
     */
    VFCommand(final int c) {

        ccode = c;
    }

    /**
     * Return the new instance of the command.
     * The command read itself the data from the input.
     *
     * @param rar           the input
     * @param fontfactory   the font factory
     * @param fontmap       the fontmap
     * @param mastertfm     the master tfm-font
     * @return Returns the new instance, or <code>null</code>, if
     *         no more data exists.
     * @throws IOException  if a IO-error occurs
     * @throws FontException if a font-error occurs
     */
    public static VFCommand getInstance(final RandomAccessR rar,
            final FontFactory fontfactory, final Map fontmap,
            final TFMFont mastertfm) throws IOException, FontException {

        // EOF ?
        if (rar.getPointer() >= rar.length()) {
            return null;
        }
        int c = rar.readByteAsInt();

        // characters
        if (c >= MIN_CHARACTER && c <= MAX_CHARACTER) {
            return new VFCommandCharacterPackets(rar, c, fontfactory, fontmap,
                    mastertfm);
        }
        switch (c) {
            case PRE :
                return new VFCommandPre(rar, c);
            case FNT_DEF_1 :
            case FNT_DEF_2 :
            case FNT_DEF_3 :
            case FNT_DEF_4 :
                return new VFCommandFontDef(rar, c, fontfactory, fontmap);
            case POST :
                return new VFCommandPost(rar, c);
            default :
                throw new VFWrongCodeException(String.valueOf(c));
        }
    }

    /**
     * Returns the type of the command.
     * @return Returns the type of the command.
     */
    public int getCommandCode() {

        return ccode;
    }
}
