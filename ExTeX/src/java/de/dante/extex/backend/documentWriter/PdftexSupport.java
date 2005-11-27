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

package de.dante.extex.backend.documentWriter;

import de.dante.extex.interpreter.type.font.Font;

/**
 * This interface describes the methods needed for <logo>pdfTeX</logo> support.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface PdftexSupport {

    /**
     * TODO gene: missing JavaDoc
     *
     * @param font the font to query
     *
     * @return the name used by PDF for the font
     */
    String pdffontname(Font font);

    /**
     * TODO gene: missing JavaDoc
     *
     * @param font the font to query
     *
     * @return the object number
     */
    long pdffontobjnum(Font font);

    /**
     * This method inserts the text to te info section.
     *
     * @param text the text to add
     */
    void pdfinfo(String text);

    /**
     * TODO gene: missing JavaDoc
     *
     * @return
     */
    long pdflastannot();

    /**
     * TODO gene: missing JavaDoc
     *
     * @return
     */
    long pdflastobj();

    /**
     * TODO gene: missing JavaDoc
     *
     * @return
     */
    long pdflastxform();

    /**
     * TODO gene: missing JavaDoc
     *
     * @return
     */
    long pdflastximage();

    /**
     * This method inserts the text to <tt>/Names</tt> array.
     *
     * @param text the text to add
     */
    void pdfnames(String text);

    /**
     * TODO gene: missing JavaDoc
     *
     * @param font
     * @param text
     */
    void pdfincludechars(Font font, String text);

}
