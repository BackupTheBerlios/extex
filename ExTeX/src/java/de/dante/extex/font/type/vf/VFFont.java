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

package de.dante.extex.font.type.vf;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Element;

import de.dante.extex.font.FontFactory;
import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.type.vf.command.VFCommand;
import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * This class read a VF-file.
 *
 * @see <a href="package-summary.html#VFformat">VF-Format</a>
 *
 * <p>
 * A VF file is organized as a stream of 8-bit bytes, using conventions
 * borrowed from DVI and PK files. Thus, a device driver that knows
 * about DVI and PK format will already contain most of the mechanisms
 * necessary to process VF files.
 * We shall assume that DVI format is understood; the conventions in the
 * DVI documentation (see, for example, TeX: The Program, part 31)
 * are adopted here to define VF format.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class VFFont implements XMLConvertible, Serializable {

    /**
     * fontname
     */
    private String fontname;

    /**
     * comamndlist
     */
    private ArrayList cmds;

    /**
     * the map for the fonts
     */
    private Map fontmap;

    /**
     * Create e new object.
     *
     * @param rar       the input
     * @param afontname the fontname
     * @param fontfac   the font factory
     * @throws IOException if a IO-error occured
     * @throws FontException if a error reading the font.
     */
    public VFFont(final RandomAccessR rar, final String afontname,
            final FontFactory fontfac) throws IOException, FontException {

        fontname = afontname;
        cmds = new ArrayList();
        fontmap = new HashMap();

        // read ...
        try {

            while (true) {
                VFCommand command = VFCommand
                        .getInstance(rar, fontfac, fontmap);
                if (command == null) {
                    break;
                }
                cmds.add(command);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // close input
        rar.close();

    }

    /**
     * Returns the fontname.
     * @return Returns the fontname.
     */
    public String getFontname() {

        return fontname;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element element = new Element("vf");
        element.setAttribute("name", fontname);
        for (int i = 0; i < cmds.size(); i++) {
            VFCommand command = (VFCommand) cmds.get(i);
            element.addContent(command.toXML());
        }
        return element;
    }
}
