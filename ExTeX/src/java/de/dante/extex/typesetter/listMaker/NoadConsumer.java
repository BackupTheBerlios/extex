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
package de.dante.extex.typesetter.listMaker;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.typesetter.type.MathClass;
import de.dante.extex.typesetter.type.MathGlyph;
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.util.GeneralException;


/**
 * This interface describes list makers which are able to consume a Noad.
 * This is usually the case for math list makers.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public interface NoadConsumer {

    /**
     * Add an arbitrary Noad to the internal list if it is prepared to hold one.
     * This is usually the case in math modes.
     *
     * @param noad the noad to add
     *
     * @throws GeneralException in case of an error
     */
    void add(Noad noad) throws GeneralException;

    /**
     * Add some math glue Noad to the internal lust.
     *
     * @param glue the glue to add
     *
     * @throws GeneralException in case of an error
     */
    void add(Muskip glue) throws GeneralException;

    /**
     * Process the input until a Noad is completed. A Noad is either a single
     * Noad or a list of Noades resulting from the processing of a block.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return the Noad read
     *
     * @throws GeneralException in case of an error
     */
    Noad scanNoad(Context context, TokenSource source) throws GeneralException;

    /**
     * Add a mathematical glyph.
     *
     * @param mclass the class
     * @param mg the glyph.
     *
     * @throws GeneralException in case of an error
     */
    void add(MathClass mclass, MathGlyph mg) throws GeneralException;
}
