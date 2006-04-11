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

/**
 * This class represents the action to address a certain page.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class GotoPageActionSpec extends GotoActionSpec {

    /**
     * The field <tt>file</tt> contains the file name.
     * The value can also be <code>null</code>.
     */
    private String file;

    /**
     * The field <tt>newWin</tt> contains the indicator for the the new window.
     * The value can also be <code>null</code>.
     */
    private Boolean newWin;

    /**
     * The field <tt>page</tt> contains the page number with 0 as default.
     */
    private long page;

    /**
     * The field <tt>text</tt> contains the plain text.
     * The value can also be <code>null</code>.
     */
    private String text;

    /**
     * Creates a new object.
     *
     * @param file the file name
     * @param page the page number
     * @param text the plain text
     * @param newwin the window indicator
     */
    public GotoPageActionSpec(final String file, final long page,
            final String text, final Boolean newwin) {

        super();
        this.file = file;
        this.page = page;
        this.text = text;
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
     * Getter for page.
     *
     * @return the page
     */
    protected long getPage() {

        return this.page;
    }

    /**
     * Getter for text.
     *
     * @return the text
     */
    protected String getText() {

        return this.text;
    }

    /**
     * @see de.dante.extex.interpreter.primitives.pdftex.util.action.ActionSpec#visit(
     *      de.dante.extex.interpreter.primitives.pdftex.util.action.ActionVisitor)
     */
    public Object visit(final ActionVisitor visitor) {

        return visitor.visitGotoPage(this);
    }

}
