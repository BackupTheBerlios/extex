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

package de.dante.extex.font.type.efm.commands;

import java.io.Serializable;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 * EFM special command.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class EfmSpecial implements Serializable {

    /**
     * the text
     */
    private String text;

    /**
     * Create a new object.
     * @param element       the char element
     */
    public EfmSpecial(final Element element) {

        Attribute attr = element.getAttribute("xxx");
        text = attr.getValue();

    }

    /**
     * Returns the text.
     * @return Returns the text.
     */
    public String getText() {

        return text;
    }
}
