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
import java.util.Map;

import org.jdom.Element;

import de.dante.extex.font.FontFactory;
import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.exception.FontNotFoundException;
import de.dante.extex.font.type.tfm.TFMFixWord;
import de.dante.extex.font.type.vf.exception.VFWrongCodeException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.file.random.RandomAccessR;

/**
 * VFCommand: fnt_def.
 *
 * <p>
 * After the <code>pre</code> command, the preamble continues with font definitions;
 * every font needed to specify 'actual' characters in later
 * <code>set_char</code> commands is defined here. The font definitions are
 * exactly the same in VF files as they are in DVI files, except
 * that the scaled size <code>s</code> is relative and the design size
 * <code>d</code> is absolute:
 * </p>
 * <pre>
 *    fnt_def1   243  k[1]  c[4]  s[4]  d[4]  a[1]  l[1]  n[a+l]
 * </pre>
 * <p>Define font <code>k</code>, where 0 &lt;= k &lt; 256.</p>
 * <pre>
 *    fnt_def2   244  k[2]  c[4]  s[4]  d[4]  a[1]  l[1]  n[a+l]
 * </pre>
 * <p>Define font <code>k</code>, where 0 &lt;= k &lt; 65536.</p>
 * <pre>
 *    fnt_def3   245  k[3]  c[4]  s[4]  d[4]  a[1]  l[1]  n[a+l]
 * </pre>
 * <p>Define font <code>k</code>, where 0 &lt;= k &lt; 2^24</p>.
 * <pre>
 *    fnt_def4   246  k[4]  c[4]  s[4]  d[4]  a[1]  l[1]  n[a+l]
 * </pre>
 * <p>Define font <code>k</code>, where -2^31 &lt;= k &lt;= 2^31.</p>
 * <p>
 * These font numbers <code>k</code> are 'local'; they have no relation
 * to font numbers defined in the DVI file that uses this virtual font.
 * The dimension <code>s</code>, which represents the scaled size of the
 * local font being defined, is a <code>fix_word</code> relative to the
 * design size of the virtual font. Thus if the local font is to be used
 * at the same size as the design size of the virtual font itself,
 * <code>s</code> will be the integer value 2^20. The value of
 * <code>s</code> must be positive and less than 2^24
 * (thus less than 16 when considered as a <code>fix_word</code>).
 * The dimension <code>d</code> is a <code>fix_word</code> in units of
 * printer's points; hence it is identical to the design size found
 * in the corresponding TFM file.
 * </p>
 *
 * <p>
 * The four-byte value <code>c</code> is the check sum.
 *
 * Parameter <code>s</code> contains a fixed-point scale factor that is
 * applied to the character widths in font <code>k</code>; font dimensions
 * in TFM files and other font files are relative to this
 * quantity, which is always positive and less than 227. It
 * is given in the same units as the other dimensions of the
 * DVI file.
 *
 * Parameter <code>d</code> is similar to <code>s</code>;
 * it is the design size, and (like <code>s</code>) it is given in DVI units.
 * Thus, font <code>k</code> is to be used at mag <code>s</code>/1000d
 * times its normal size.
 *
 * The remaining part of a font definition gives the external
 * name of the font, which is an ASCII string of length
 * <code>a</code> + <code>l</code>. The number a is the length
 * of the area or directory,
 * and <code>l</code> is the length of the font name itself; the
 * standard local system font area is supposed to be used
 * when a = 0. The n field contains the area in its first a
 * bytes.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class VFCommandFontDef extends VFCommand {

    /**
     * the font numbers (local number)
     */
    private int fontnumbers;

    /**
     * the checksum
     */
    private int checksum;

    /**
     * the scalefactor
     */
    private TFMFixWord scalefactor;

    /**
     * the designsize
     */
    private TFMFixWord designsize;

    /**
     * the font name
     */
    private String fontname;

    /**
     * Create e new object.
     *
     * @param rar           the input
     * @param ccode         the command code
     * @param fontfactory   the fontfactory
     * @param fontmap   the fontmap
     * @throws IOException if a IO-error occured
     * @throws FontException if a error reading the font.
     */
    public VFCommandFontDef(final RandomAccessR rar, final int ccode,
            final FontFactory fontfactory, final Map fontmap)
            throws IOException, FontException {

        super(ccode);

        switch (ccode) {
            case FNT_DEF_1 :
                // 8 bit
                fontnumbers = rar.readByteAsInt();
                break;
            case FNT_DEF_2 :
                // 16 bit
                fontnumbers = rar.readShort();
                break;
            case FNT_DEF_3 :
                // 24 bit
                fontnumbers = rar.readInt24();
                break;
            case FNT_DEF_4 :
                // 32 bit
                fontnumbers = rar.readInt();
                break;
            default :
                throw new VFWrongCodeException(String.valueOf(ccode));
        }

        checksum = rar.readInt();
        scalefactor = new TFMFixWord(rar.readInt(),
                TFMFixWord.FIXWORDDENOMINATOR);
        designsize = new TFMFixWord(rar.readInt(),
                TFMFixWord.FIXWORDDENOMINATOR);

        fontname = readFontName(rar);
        try {
            Dimen fds = new Dimen(Dimen.ONE_PT);
            fds.setValue((long) (fds.getValue() * Double.parseDouble(designsize
                    .toString())));
            Count scale = new Count((long) (Double.parseDouble(scalefactor
                    .toStringComma())));
            Font f = fontfactory.getInstance(fontname, fds, scale, new Glue(0),
                    true, true);
            fontmap.put(new Integer(fontnumbers), f);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FontNotFoundException(fontname);
        }
    }

    /**
     * Reads a character string from the header.
     *
     * @param rar   the input
     * @return the string
     * @throws IOException if an I/O error occured
     */
    private String readFontName(final RandomAccessR rar) throws IOException {

        int a = rar.readByteAsInt();
        int l = rar.readByteAsInt();

        int len = a + l;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < len; i++) {
            buf.append((char) rar.readByteAsInt());
        }
        return buf.toString();
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element element = new Element("fontdef");
        element.setAttribute("opcode", String.valueOf(getCommandCode()));
        element.setAttribute("fontnumbers", String.valueOf(fontnumbers));
        element.setAttribute("fontname", fontname);
        element.setAttribute("checksum", String.valueOf(checksum));
        element.setAttribute("scalefactor", String.valueOf(scalefactor));
        element.setAttribute("designsize", String.valueOf(designsize));
        return element;
    }
}
