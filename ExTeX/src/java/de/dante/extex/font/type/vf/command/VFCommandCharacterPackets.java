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
import de.dante.extex.font.type.tfm.TFMFixWord;
import de.dante.extex.font.type.vf.exception.VFWrongCodeException;
import de.dante.util.file.random.RandomAccessR;

/**
 * VFCommand: character packets
 *
 * <p>
 * The preamble is followed by zero or more character packets, where each
 * character packet begins with a byte that is &lt;<243.
 * Character packets have two formats, one long and one short:
 * </p>
 * <pre>
 *    long_char  242     pl[4]  cc[4]   tfm[4]  dvi[pl]
 * </pre>
 * <p>This long form specifies a virtual character in the general case.</p>
 * <pre>
 *    short_char 0..241=pl[1]  cc[1]   tfm[3]  dvi[pl]
 * </pre>
 * <p>
 * This short form specifies a virtual character in the common case
 * when 0 &lt;= pl &lt;242 and 0 &lt;= cc &lt;256
 * and 0 &lt; &lt;= tfm &lt; 2^24. The ccode is pl!
 * </p>
 * <p>
 * Here <code>pl</code> denotes the packet length following
 * the <code>tfm</code> value; <code>cc</code> is the character code;
 * and <code>tfm</code> is the character width copied from the
 * TFM file for this virtual font. There should be at most one character
 * packet having any given <code>cc</code> code.
 * </p>
 * <p>
 * The <code>dvi</code> bytes are a sequence of complete DVI commands, properly
 * nested with respect to <code>push</code> and <code>pop</code>.
 * All DVI operations are permitted except <code>bop</code>, <code>eop</code>,
 * and commands with opcodes &gt;=243.
 * Font selection commands (<code>fnt_num0</code> through <code>fnt4</code>)
 * must refer to fonts defined in the preamble.
 * </p>
 * <p>
 * Dimensions that appear in the DVI instructions are analogous to
 * <code>fix_word</code> quantities; i.e., they are integer multiples
 * of 2^-20 times the design size of the virtual font.
 * For example, if the virtual font has design size 10pt, the DVI command
 * to move down 5pt would be a <code>down</code> instruction with
 * parameter 2^19. The virtual font itself might be used at a different size,
 * say 12pt; then that <code>down</code> instruction would move down
 * 6pt instead. Each dimension must be less than 2^24 in absolute value.
 * </p>
 * <p>
 * Device drivers processing VF files treat the sequences of <code>dvi</code> bytes
 * as subroutines or macros, implicitly enclosing them with <code>push</code>
 * and <code>pop</code>.
 * Each subroutine begins with <code>w=x=y=z=0</code>, and with current
 * font <code>f</code> the number of the first-defined in the preamble
 * (undefined if there's no such font). After the <code>dvi</code> commands
 * have been performed, the <code>h</code> and <code>v</code> position registers
 * of DVI format and the current font <code>f</code> are restored to their
 * former values; then, if the subroutine has been invoked by
 * a <code>set_char</code> or <code>set</code> command, <code>h</code> is
 * increased by the TFM width (properly scaled) - just as if a simple character
 * had been typeset.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */

public class VFCommandCharacterPackets extends VFCommand {

    /**
     * the packet length (pl)
     */
    private int packetlength;

    /**
     * the character code (cc)
     */
    private int charactercode;

    /**
     * the character width (from tfm file)   (tfm)
     */
    private TFMFixWord width;

    /**
     * the dvi commands
     */
    private short[] dvi;

    /**
     * Create e new object.
     *
     * @param rar       the input
     * @param ccode     the command code
     * @throws IOException if a IO-error occured
     * @throws FontException if a error reading the font.
     */
    public VFCommandCharacterPackets(final RandomAccessR rar, final int ccode)
            throws IOException, FontException {

        super(ccode);

        // check range
        if (ccode < MIN_CHARACTER || ccode > MAX_CHARACTER) {
            throw new VFWrongCodeException(String.valueOf(ccode));
        }

        if (ccode == MAX_CHARACTER) {
            // long format
            packetlength = rar.readInt();
            charactercode = rar.readInt();
            width = new TFMFixWord(rar.readInt(), TFMFixWord.FIXWORDDENOMINATOR);
        } else {
            // short format
            packetlength = ccode;
            charactercode = rar.readByteAsInt();
            // 24 bit
            width = new TFMFixWord(rar.readInt24(), TFMFixWord.FIXWORDDENOMINATOR);
        }
        dvi = new short[packetlength];
        for (int i = 0; i < packetlength; i++) {
            dvi[i] = (short) rar.readByteAsInt();
        }
    }

    /**
     * Returns the charactercode.
     * @return Returns the charactercode.
     */
    public int getCharactercode() {

        return charactercode;
    }

    /**
     * Returns the dvi.
     * @return Returns the dvi.
     */
    public short[] getDvi() {

        return dvi;
    }

    /**
     * Returns the packetlength.
     * @return Returns the packetlength.
     */
    public int getPacketlength() {

        return packetlength;
    }

    /**
     * Returns the width.
     * @return Returns the width.
     */
    public TFMFixWord getWidth() {

        return width;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element element = new Element("character");
        element.setAttribute("ccode", String.valueOf(getCommandCode()));
        element.setAttribute("charactercode", String.valueOf(charactercode));
        element.setAttribute("packetlength", String.valueOf(packetlength));
        element.setAttribute("width", width.toString());
        for (int i = 0; i < dvi.length; i++) {
            Element d = new Element("dvi");
            d.setAttribute("id", String.valueOf(i));
            d.setAttribute("value", String.valueOf(dvi[i]));
            element.addContent(d);
        }
        return element;
    }
}
