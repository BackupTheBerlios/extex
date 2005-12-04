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

package de.dante.extex.interpreter.primitives.typesetter;

import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.listMaker.HorizontalListMaker;
import de.dante.extex.typesetter.listMaker.ListManager;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This an abstract base class for primitives in horizontal mode.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public abstract class AbstractHorizontalCode extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param codeName the name of the code
     */
    public AbstractHorizontalCode(final String codeName) {

        super(codeName);
    }

    /**
     * Check that the current mode is a horizontal mode and throw an exception
     * if another mode is detected.
     *
     * @param typesetter the typesetter to ask for the mode
     *
     * @throws HelpingException in case of an error
     */
    protected void ensureHorizontalMode(final Typesetter typesetter)
            throws HelpingException {

        Mode mode = typesetter.getMode();
        if (mode == Mode.VERTICAL || mode == Mode.INNER_VERTICAL) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(AbstractHorizontalCode.class.getName()),
                    "TTP.MissingInserted", "}");
        }

    }

    /**
     * Check that the current mode is a horizontal mode and open a new
     * list maker if another mode is detected.
     *
     * @param typesetter the typesetter to ask for the mode
     *
     * @throws InterpreterException in case of an error
     */
    protected void switchToHorizontalMode(final Typesetter typesetter)
            throws InterpreterException {

        Mode mode = typesetter.getMode();
        if (mode == Mode.VERTICAL || mode == Mode.INNER_VERTICAL) {
            ListManager man = typesetter.getManager();
            ListMaker hlist = new HorizontalListMaker(man, typesetter
                    .getLocator());
            try {
                man.push(hlist);
            } catch (TypesetterException e) {
                throw new InterpreterException(e);
            }
        }
    }

}
