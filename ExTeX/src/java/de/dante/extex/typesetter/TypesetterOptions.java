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

package de.dante.extex.typesetter;

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextFactory;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;
import de.dante.util.UnicodeChar;

/**
 * This interface describes the possibilities of the typesetter to access its
 * options.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public interface TypesetterOptions {

    /**
     * Getter for a count register.
     *
     * @param name the name of the register
     *
     * @return the content of the count register
     */
    FixedCount getCountOption(String name);

    /**
     * Getter for a dimen register.
     *
     * @param name the name of the register
     *
     * @return the content of the dimen register
     */
    FixedDimen getDimenOption(String name);

    /**
     * Getter for a current font register.
     *
     * @param name the name or the number of the register
     *
     * @return the named font register or <code>null</code> if none is set
     */
    Font getFont(String name);

    /**
     * Getter for a glue register.
     *
     * @param name the name of the register
     *
     * @return the content of the glue register
     */
    FixedGlue getGlueOption(String name);

    /**
     * Getter for the lccode mapping of upper case characters to their
     * lower case equivalent.
     *
     * @param uc the upper case character
     *
     * @return the lower case equivalent or null if none exists
     */
    UnicodeChar getLccode(UnicodeChar uc);

    /**
     * Getter for the current name space.
     *
     * @return the current namespace
     */
    String getNamespace();

    /**
     * Getter fot the paragraph shape.
     *
     * @return the paragraph shape or <code>null</code> if no special shape
     *   is present
     */
    ParagraphShape getParshape();

    /**
     * Getter for the token factory. The token factory can be used to get new
     * tokens of some kind.
     *
     * @return the token factory
     */
    TokenFactory getTokenFactory();

    /**
     * Getter for the typesetting context.
     *
     * @return the typesetting context
     */
    TypesettingContext getTypesettingContext();

    /**
     * Getter for the typesetting context factory.
     *
     * @return the typesetting context factory
     */
    TypesettingContextFactory getTypesettingContextFactory();

    /**
     * Setter for the paragraph shape.
     *
     * @param shape the new paragraph shape
     */
    void setParshape(ParagraphShape shape);

}