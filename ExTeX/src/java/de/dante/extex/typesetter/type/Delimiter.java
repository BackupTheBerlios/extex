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

package de.dante.extex.typesetter.type;

import de.dante.extex.interpreter.TokenSource;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This class provides a container for a delimiter consisting of a class, a
 * large and a small math glyph.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Delimiter {

    /**
     * The field <tt>largeChar</tt> contains the code of the large character.
     */
    private MathGlyph largeChar;

    /**
     * The field <tt>mathClass</tt> contains the class of this delimiter.
     */
    private MathClass mathClass;

    /**
     * The field <tt>smallChar</tt> contains the code of the small character.
     */
    private MathGlyph smallChar;

    /**
     * Creates a new object from the TeX encoding.
     * <p>
     * The TeX encoding interprets the number as 27 bit hex number:
     * <tt>"csyylxx</tt>. Here the digits have the following meaning:
     * <dl>
     *  <dt>c</dt>
     *  <dd>the math class of this delimiter. It has a range from 0 to 7.</dd>
     *  <dt>l</dt>
     *  <dd>the family for the large character. it has a range from 0 to 15.</dd>
     *  <dt>xx</dt>
     *  <dd>the character code of the large character.</dd>
     *  <dt>s</dt>
     *  <dd>the family for the small character. it has a range from 0 to 15.</dd>
     *  <dt>yy</dt>
     *  <dd>the character code of the small character.</dd>
     * </dl>
     * </p>
     *
     * @param delcode the TeX encoding for the delimiter
     */
    public Delimiter(final long delcode) {

        super();
        mathClass = MathClass.getMathClass((int) ((delcode >> 24) & 0xf));
        smallChar = new MathGlyph((int) ((delcode >> 20) & 0xf),
                new UnicodeChar((int) ((delcode >> 12) & 0xff)));
        largeChar = new MathGlyph((int) ((delcode >> 8) & 0xf),
                new UnicodeChar((int) (delcode & 0xff)));
    }

    /**
     * Creates a new object.
     *
     * @param source the token source to read from
     *
     * @throws GeneralException in case of an error
     */
    public Delimiter(final TokenSource source) throws GeneralException {

        this(source.scanNumber());
    }

    /**
     * Getter for largeChar.
     *
     * @return the largeChar.
     */
    public MathGlyph getLargeChar() {

        return this.largeChar;
    }

    /**
     * Getter for mathClass.
     *
     * @return the mathClass.
     */
    public MathClass getMathClass() {

        return this.mathClass;
    }

    /**
     * Getter for smallChar.
     *
     * @return the smallChar.
     */
    public MathGlyph getSmallChar() {

        return this.smallChar;
    }

}