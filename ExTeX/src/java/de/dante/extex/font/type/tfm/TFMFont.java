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

package de.dante.extex.font.type.tfm;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.type.PlFormat;
import de.dante.extex.font.type.PlWriter;
import de.dante.extex.font.type.pfb.PfbParser;
import de.dante.extex.font.type.tfm.enc.EncFactory;
import de.dante.extex.font.type.tfm.psfontsmap.PSFontEncoding;
import de.dante.extex.font.type.tfm.psfontsmap.PSFontsMapReader;
import de.dante.util.EFMWriterConvertible;
import de.dante.util.XMLWriterConvertible;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.xml.XMLStreamWriter;

/**
 * This class read a TFM-file.
 *
 * @see <a href="package-summary.html#TFMformat">TFM-Format</a>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */
public class TFMFont
        implements
            XMLWriterConvertible,
            PlFormat,
            EFMWriterConvertible,
            Serializable {

    /**
     * fontname
     */
    private String fontname;

    /**
     * Create e new object.
     *
     * @param rar       the input
     * @param afontname the fontname
     * @throws IOException if a IO-error occured
     */
    public TFMFont(final RandomAccessR rar, final String afontname)
            throws IOException {

        fontname = afontname;

        // read the input
        lengths = new TFMHeaderLengths(rar);
        header = new TFMHeaderArray(rar, lengths.getLh());
        charinfo = new TFMCharInfoArray(rar, lengths.getCc());
        width = new TFMWidthArray(rar, lengths.getNw());
        height = new TFMHeightArray(rar, lengths.getNh());
        depth = new TFMDepthArray(rar, lengths.getNd());
        italic = new TFMItalicArray(rar, lengths.getNi());
        ligkern = new TFMLigKernArray(rar, lengths.getNl());
        kern = new TFMKernArray(rar, lengths.getNk());
        exten = new TFMExtenArray(rar, lengths.getNe());
        param = new TFMParamArray(rar, lengths.getNp(), header.getFontype());

        // close input
        rar.close();

        // calculate lig/kern
        ligkern.calculate(charinfo, kern, lengths.getBc());

        // create chartable
        charinfo.createCharTable(width, height, depth, italic, exten, lengths
                .getBc(), ligkern.getLigKernTable());

    }

    /**
     * the lengths in the file
     */
    private TFMHeaderLengths lengths;

    /**
     * the header
     */
    private TFMHeaderArray header;

    /**
     * the char info
     */
    private TFMCharInfoArray charinfo;

    /**
     * the width
     */
    private TFMWidthArray width;

    /**
     * the height
     */
    private TFMHeightArray height;

    /**
     * the depth
     */
    private TFMDepthArray depth;

    /**
     * the italic
     */
    private TFMItalicArray italic;

    /**
     * the lig/kern array
     */
    private TFMLigKernArray ligkern;

    /**
     * the kern
     */
    private TFMKernArray kern;

    /**
     * the exten
     */
    private TFMExtenArray exten;

    /**
     * the param
     */
    private TFMParamArray param;

    /**
     * Returns the charinfo.
     * @return Returns the charinfo.
     */
    public TFMCharInfoArray getCharinfo() {

        return charinfo;
    }

    /**
     * Returns the depth.
     * @return Returns the depth.
     */
    public TFMDepthArray getDepth() {

        return depth;
    }

    /**
     * Returns the encfactory.
     * @return Returns the encfactory.
     */
    public EncFactory getEncfactory() {

        return encfactory;
    }

    /**
     * Returns the enctable.
     * @return Returns the enctable.
     */
    public String[] getEnctable() {

        return enctable;
    }

    /**
     * Returns the exten.
     * @return Returns the exten.
     */
    public TFMExtenArray getExten() {

        return exten;
    }

    /**
     * Returns the fontname.
     * @return Returns the fontname.
     */
    public String getFontname() {

        return fontname;
    }

    /**
     * Returns the face of the font.
     * @return Returns the face of the font.
     */
    public int getFace() {

        return header.getFace();
    }

    /**
     * Returns the header.
     * @return Returns the header.
     */
    public TFMHeaderArray getHeader() {

        return header;
    }

    /**
     * Returns the height.
     * @return Returns the height.
     */
    public TFMHeightArray getHeight() {

        return height;
    }

    /**
     * Returns the italic.
     * @return Returns the italic.
     */
    public TFMItalicArray getItalic() {

        return italic;
    }

    /**
     * Returns the kern.
     * @return Returns the kern.
     */
    public TFMKernArray getKern() {

        return kern;
    }

    /**
     * Returns the lengths.
     * @return Returns the lengths.
     */
    public TFMHeaderLengths getLengths() {

        return lengths;
    }

    /**
     * Returns the ligkern.
     * @return Returns the ligkern.
     */
    public TFMLigKernArray getLigkern() {

        return ligkern;
    }

    /**
     * Returns the param.
     * @return Returns the param.
     */
    public TFMParamArray getParam() {

        return param;
    }

    /**
     * Returns the pfbfilename.
     * @return Returns the pfbfilename.
     */
    public String getPfbfilename() {

        return pfbfilename;
    }

    /**
     * Returns the psfenc.
     * @return Returns the psfenc.
     */
    public PSFontEncoding getPsfenc() {

        return psfenc;
    }

    /**
     * Returns the psfontmap.
     * @return Returns the psfontmap.
     */
    public PSFontsMapReader getPsfontmap() {

        return psfontmap;
    }

    /**
     * Returns the width.
     * @return Returns the width.
     */
    public TFMWidthArray getWidth() {

        return width;
    }

    /**
     * Returns the font family.
     * @return Returns the font family.
     */
    public String getFontFamily() {

        return header.getFontfamily();
    }

    /**
     * Returns the checksum.
     * @return Returns the checksum.
     */
    public int getChecksum() {

        return header.getChecksum();
    }

    /**
     * Returns the design size.
     * @return Returns the design size.
     */
    public double getDesignSizeAsDouble() {

        return Double.parseDouble(header.getDesignsize().toString());
    }

    /**
     * Returns the font type.
     * @return Returns the font type.
     */
    public TFMFontType getFontType() {

        return header.getFontype();
    }

    //    /**
    //     * @see de.dante.extex.font.type.FontMetric#getFontMetric()
    //     */
    //    public Element getFontMetric() {
    //
    //        // create efm-file
    //        Element root = new Element("fontgroup");
    //        root.setAttribute("name", getFontFamily());
    //        root.setAttribute("id", getFontFamily());
    //        root.setAttribute("default-size", String.valueOf(getDesignSize()));
    //        root.setAttribute("empr", "100");
    //        root.setAttribute("type", "tfm");
    //
    //        Element fontdimen = new Element("fontdimen");
    //        root.addContent(fontdimen);
    //
    //        Element font = new Element("font");
    //        root.addContent(font);
    //
    //        font.setAttribute("font-name", getFontFamily());
    //        font.setAttribute("font-family", getFontFamily());
    //        root.setAttribute("units-per-em", "1000");
    //        font.setAttribute("checksum", String.valueOf(getChecksum()));
    //        font.setAttribute("type", getFontType().toTFMString());
    //        param.addParam(fontdimen);
    //
    //        // filename
    //        if (pfbfilename != null) {
    //            font.setAttribute("filename", pfbfilename);
    //        }
    //        // charinfo.addGlyphs(font);
    //
    //        return root;
    //    }
    //
    /**
     * psfontmap
     */
    private PSFontsMapReader psfontmap;

    /**
     * Encoderfactory
     */
    private EncFactory encfactory;

    /**
     * psfontencoding
     */
    private PSFontEncoding psfenc;

    /**
     * encodingtable
     */
    private String[] enctable;

    /**
     * pfb filename
     */
    private String pfbfilename;

    /**
     * the pfb parser
     */
    private PfbParser pfbparser;

    /**
     * Returns the pfbparser.
     * @return Returns the pfbparser.
     */
    public PfbParser getPfbParser() {

        return pfbparser;
    }

    /**
     * The pfbparser to set.
     * @param parser The pfbparser to set.
     */
    public void setPfbParser(final PfbParser parser) {

        pfbparser = parser;
        if (enctable == null && parser != null) {

            // no encoding table -> set the glyphname
            String enc[] = parser.getEncoding();
            // glyphname
            charinfo.setEncodingTable(enc);
        }
    }

    /**
     * Set the fontmap reader an d the encoding factory
     * @param apsfontmap    the psfonts.map reader
     * @param encf          the encoding factory
     * @throws FontException if a font-erorr occured
     * @throws ConfigurationException from the resourcefinder
     */
    public void setFontMapEncoding(final PSFontsMapReader apsfontmap,
            final EncFactory encf) throws FontException, ConfigurationException {

        psfontmap = apsfontmap;
        encfactory = encf;

        // read psfonts.map
        if (psfontmap != null) {
            psfenc = psfontmap.getPSFontEncoding(fontname);

            // encoding
            if (psfenc != null) {
                if (!"".equals(psfenc.getEncfile())) {
                    enctable = encfactory.getEncodingTable(psfenc.getEncfile());
                }
                // filename
                if (psfenc.getPfbfile() != null) {
                    pfbfilename = filenameWithoutPath(psfenc.getPfbfile());
                }
                // glyphname
                charinfo.setEncodingTable(enctable);
            }
        }
    }

    /**
     * remove the path, if exists
     * @param  file the filename
     * @return  the filename without the path
     */
    private String filenameWithoutPath(final String file) {

        String rt = file;
        int i = rt.lastIndexOf(File.separator);
        if (i > 0) {
            rt = rt.substring(i + 1);
        }
        return rt;
    }

    /**
     * @see de.dante.extex.font.type.PlFormat#toPL(de.dante.extex.font.type.PlWriter)
     */
    public void toPL(final PlWriter out) throws IOException {

        header.toPL(out);
        param.toPL(out);
        ligkern.toPL(out);
        charinfo.toPL(out);
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writer.writeStartElement("tfm");
        writer.writeAttribute("name", fontname);
        lengths.writeXML(writer);
        header.writeXML(writer);
        charinfo.writeXML(writer);
        width.writeXML(writer);
        height.writeXML(writer);
        depth.writeXML(writer);
        italic.writeXML(writer);
        ligkern.writeXML(writer);
        kern.writeXML(writer);
        exten.writeXML(writer);
        param.writeXML(writer);
        if (pfbparser != null) {
            pfbparser.writeXML(writer);
        }
        writer.writeEndElement();
    }

    /**
     * @see de.dante.util.EFMWriterConvertible#writeEFM(de.dante.util.xml.XMLStreamWriter)
     */
    public void writeEFM(final XMLStreamWriter writer) throws IOException {

        writer.writeStartElement("font");
        writer.writeAttribute("id", getFontFamily());
        writer.writeAttribute("font-name", getFontFamily());
        writer.writeAttribute("font-family", getFontFamily());
        writer.writeAttribute("default-size", String
                .valueOf(getDesignSizeAsDouble()));
        writer.writeAttribute("type", "tfm");
        writer.writeAttribute("units-per-em", "1000");
        writer.writeAttribute("checksum", String.valueOf(getChecksum()));
        writer.writeAttribute("subtype", getFontType().toTFMString());
        if (pfbfilename != null) {
            writer.writeAttribute("filename", pfbfilename);
        }
        param.writeEFM(writer);
        charinfo.writeEFM(writer);
        writer.writeEndElement();
    }
}
