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

package de.dante.extex.font.type.vf.command;

import java.io.IOException;

import org.jdom.Element;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.type.vf.exception.VFWrongCodeException;
import de.dante.util.file.random.RandomAccessR;

/**
 * VFCommand: pre
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class VFCommandPost extends VFCommand {

    /**
     * Create e new object.
     *
     * @param rar       the input
     * @param ccode     the command code
     * @throws IOException if a IO-error occured
     * @throws FontException if a error reading the font.
     */
    public VFCommandPost(final RandomAccessR rar, final int ccode)
            throws IOException, FontException {

        super(ccode);

        if (ccode != POST) {
            throw new VFWrongCodeException(String.valueOf(ccode));
        }
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element element = new Element("post");
        return element;
    }

}
