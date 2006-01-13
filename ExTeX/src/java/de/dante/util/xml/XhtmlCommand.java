/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package de.dante.util.xml;

import java.io.PrintWriter;

import org.xml.sax.Attributes;

/**
 * The command for a xhtml element.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 *
 */
public interface XhtmlCommand {

    /**
     * Execute the start tag for an element.
     *
     * @param out        The Output.
     * @param attributes The attributes attached to the element.
     */
    void startElement(final PrintWriter out, final Attributes attributes);

    /**
     * Execute the stop tag for an element.
     *
     * @param out        The Output.
     */
    void endElement(final PrintWriter out);

}
