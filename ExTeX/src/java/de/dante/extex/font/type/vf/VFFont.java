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
import de.dante.extex.font.type.FontMetric;
import de.dante.extex.font.type.PlFormat;
import de.dante.extex.font.type.PlWriter;
import de.dante.extex.font.type.tfm.TFMFont;
import de.dante.extex.font.type.vf.command.VFCommand;
import de.dante.extex.font.type.vf.command.VFCommandCharacterPackets;
import de.dante.extex.font.type.vf.command.VFCommandFontDef;
import de.dante.extex.font.type.vf.exception.VFMasterTFMNotFoundException;
import de.dante.util.XMLConvertible;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.file.random.RandomAccessR;

/**
 * This class read a VF-file.
 * <p>
 * A VF file is organized as a stream of 8-bit bytes, using conventions
 * borrowed from DVI and PK files. Thus, a device driver that knows
 * about DVI and PK format will already contain most of the mechanisms
 * necessary to process VF files.
 * We shall assume that DVI format is understood; the conventions in the
 * DVI documentation (see, for example, TeX: The Program, part 31)
 * are adopted here to define VF format.
 * </p>
 * @see <a href="package-summary.html#VFformat">VF-Format</a>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public class VFFont
        implements
            XMLConvertible,
            Serializable,
            FontMetric,
            PlFormat {

    /**
     * units per em (default-value)
     */
    public static final int UNITS_PER_EM_DEFAULT = 1000;
    
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
     * the master tfm-file for the vf-font
     */
    private TFMFont mastertfm;

    /**
     * Create e new object.
     *
     * @param rar       the input
     * @param afontname the fontname
     * @param fontfac   the font factory
     * @throws IOException if a IO-error occured
     * @throws FontException if a error reading the font.
     * @throws ConfigurationException from the config-system.
     */
    public VFFont(final RandomAccessR rar, final String afontname,
            final FontFactory fontfac) throws IOException, FontException,
            ConfigurationException {

        fontname = afontname;
        cmds = new ArrayList();
        fontmap = new HashMap();

        // read the master tfm
        mastertfm = fontfac.readTFMFont(fontname);
        if (mastertfm == null) {
            throw new VFMasterTFMNotFoundException(fontname);
        }

        // read
        try {

            while (true) {
                VFCommand command = VFCommand.getInstance(rar, fontfac,
                        fontmap, mastertfm);
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
        element.addContent(mastertfm.toXML());
        for (int i = 0; i < cmds.size(); i++) {
            VFCommand command = (VFCommand) cmds.get(i);
            element.addContent(command.toXML());
        }
        return element;
    }

    /**
     * @see de.dante.extex.font.type.PlFormat#toPL(
     *      de.dante.extex.font.type.PlWriter)
     */
    public void toPL(final PlWriter out) throws IOException, FontException {

        out.plopen("VTITLE ").plclose();
        out.plopen("FAMILY").addStr(mastertfm.getFontFamily()).plclose();
        out.plopen("FACE").addFace(mastertfm.getFace()).plclose();
        out.plopen("CODINGSCHEME").addStr(
                mastertfm.getHeader().getCodingscheme()).plclose();
        out.plopen("DESIGNSIZE").addReal(mastertfm.getDesignSize()).plclose();
        out.addComment("DESIGNSIZE IS IN POINTS");
        out.addComment("OTHER SIZES ARE MULTIPLES OF DESIGNSIZE");
        out.plopen("CHECKSUM").addOct(mastertfm.getChecksum()).plclose();
        mastertfm.getParam().toPL(out);
        // print font-info
        for (int i = 0; i < cmds.size(); i++) {
            VFCommand command = (VFCommand) cmds.get(i);
            if (command instanceof VFCommandFontDef) {
                VFCommandFontDef fd = (VFCommandFontDef) command;
                fd.toPL(out);
            }
        }
        // ligtable
        mastertfm.getLigkern().toPL(out);

        // character
        for (int i = 0; i < cmds.size(); i++) {
            VFCommand command = (VFCommand) cmds.get(i);
            if (command instanceof VFCommandCharacterPackets) {
                VFCommandCharacterPackets ch = (VFCommandCharacterPackets) command;
                ch.toPL(out);
            }
        }
    }

    /**
     * @see de.dante.extex.font.type.FontMetric#getFontMetric()
     */
    public Element getFontMetric() throws FontException {

        // create efm-file
        Element root = new Element("fontgroup");
        root.setAttribute("name", getFontname());
        root.setAttribute("id", getFontname());
        root.setAttribute("default-size", String.valueOf(mastertfm
                .getDesignSize()));
        root.setAttribute("empr", "100");
        root.setAttribute("type", "vf");
        root.setAttribute("virtual", "true");

        Element fontdimen = new Element("fontdimen");
        root.addContent(fontdimen);

        Element font = new Element("font");
        root.addContent(font);

        font.setAttribute("font-name", getFontname());
        font.setAttribute("font-family", getFontname());
        root.setAttribute("units-per-em", "1000");
        font.setAttribute("checksum", String.valueOf(mastertfm.getChecksum()));
        font.setAttribute("type", mastertfm.getFontType().toTFMString());
        mastertfm.getParam().addParam(fontdimen);

        // character
        for (int i = 0; i < cmds.size(); i++) {
            VFCommand command = (VFCommand) cmds.get(i);
            if (command instanceof VFCommandCharacterPackets) {
                VFCommandCharacterPackets ch = (VFCommandCharacterPackets) command;
                ch.addGlyph(font);
            }
        }
        return root;
    }
}
