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

package de.dante.extex.interpreter.primitives.pdftex.util;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class GotoIdActionSpec extends GotoActionSpec {

    /**
     * The field <tt>file</tt> contains the ...
     */
    private String file;

    /**
     * The field <tt>newWin</tt> contains the ...
     */
    private Boolean newWin;

    /**
     * The field <tt>id</tt> contains the ...
     */
    private String id;

    /**
     * Creates a new object.
     *
     */
    public GotoIdActionSpec(final String file, final String id,
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
     * Getter for newWin.
     *
     * @return the newWin
     */
    protected Boolean getNewWin() {

        return this.newWin;
    }

    /**
     * Getter for id.
     *
     * @return the id
     */
    protected String getId() {

        return this.id;
    }

}
