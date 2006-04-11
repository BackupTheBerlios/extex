/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.pdftex.util.action;

import de.dante.extex.interpreter.primitives.pdftex.util.id.IdSpec;

/**
 * This is the a goto action with id for PDF.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class GotoIdActionSpec extends GotoActionSpec {

    /**
     * The field <tt>file</tt> contains the optional file.
     */
    private String file;

    /**
     * The field <tt>id</tt> contains the id.
     */
    private IdSpec id;

    /**
     * The field <tt>newWin</tt> contains the indicator for the new window.
     */
    private Boolean newWin;

    /**
     * Creates a new object.
     *
     * @param file the optional file.
     * @param id id.
     * @param newwin the indicator for the new window
     */
    public GotoIdActionSpec(final String file, final IdSpec id,
            final Boolean newwin) {

        super();
        this.file = file;
        this.id = id;
        this.newWin = newwin;
    }

    /**
     * Getter for file.
     *
     * @return the file
     */
    protected String getFile() {

        return this.file;
    }

    /**
     * Getter for id.
     *
     * @return the id
     */
    protected IdSpec getId() {

        return this.id;
    }

    /**
     * Getter for newWin.
     *
     * @return the newWin
     */
    protected Boolean getNewWin() {

        return this.newWin;
    }

    /**
     * @see de.dante.extex.interpreter.primitives.pdftex.util.action.ActionSpec#visit(
     *      de.dante.extex.interpreter.primitives.pdftex.util.action.ActionVisitor)
     */
    public Object visit(final ActionVisitor visitor) {

        return visitor.visitGotoId(this);
    }

}
