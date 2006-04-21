/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.dviware.type;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dante.dviware.Dvi;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;

/**
 * This class represents the DVI instruction <tt>post</tt> and the contents
 * up to the terminating <tt>post_post</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class DviPostamble extends AbstractDviCode {

    /**
     * The field <tt>bop</tt> contains the offset for the last bop instruction.
     */
    private int bop = -1;

    /**
     * The field <tt>fontMap</tt> contains the fonts and their mapping to
     * indices.
     */
    private Map fontMap = new HashMap();

    /**
     * The field <tt>fonts</tt> contains the list of the font def instructions
     * to be written out at the end.
     */
    private List fonts = new ArrayList();

    /**
     * The field <tt>mag</tt> contains the magnification in permille.
     */
    private int mag;

    /**
     * The field <tt>maxHeight</tt> contains the maximal height.
     */
    private Dimen maxHeight = new Dimen(0);

    /**
     * The field <tt>maxWidth</tt> contains the maximal width.
     */
    private Dimen maxWidth = new Dimen(0);

    /**
     * The field <tt>numberOfPages</tt> contains the number of pages.
     */
    private int numberOfPages = 0;

    /**
     * The field <tt>bopOffset</tt> contains the index of the last BOP
     * instruction in the output stream.
     */
    private int bopOffset;

    /**
     * The field <tt>stackDepth</tt> contains the stack depth needed to process
     * all push and pop operations.
     */
    private int stackDepth = 0;

    /**
     * Creates a new object.
     *
     * @param mag the magnification factor in permille
     */
    public DviPostamble(final int mag) {

        super();
        this.mag = mag;
    }

    /**
     * @see de.dante.dviware.type.DviCode#getName()
     */
    public String getName() {

        return "post";
    }

    /**
     * Determine the font index and insert a font definition into the output
     * list if required. As a side effect the font is remembered for writing
     * the postamble.
     *
     * @param fnt the font to resolve
     * @param list the list to add the font definition to
     *
     * @return the index in the font table to use
     */
    public int mapFont(final Font fnt, final List list) {

        Long idx = (Long) fontMap.get(fnt);
        if (idx != null) {
            return idx.intValue();
        }
        int index = fonts.size();
        fontMap.put(fnt, new Long(index));
        DviFntDef dviFntDef = new DviFntDef(index, fnt);
        list.add(dviFntDef);
        fonts.add(dviFntDef);
        return index;
    }

    /**
     * See a page and remember everything needed to be written out at the end.
     *
     * @param height the height of the page
     * @param depth the depth of the page
     * @param width the width of the page
     * @param stacksize the stack size needed to process the push and pop
     *  operations of the current page
     */
    public void recognizePage(final FixedDimen height, final FixedDimen depth,
            final FixedDimen width, final int stacksize) {

        numberOfPages++;

        Dimen x = new Dimen(height);
        x.add(depth);

        if (maxHeight.lt(x)) {
            maxHeight.set(x);
        }

        if (maxWidth.lt(width)) {
            maxWidth.set(width);
        }

        if (stacksize > stackDepth) {
            stackDepth = stacksize;
        }
    }

    /**
     * Setter for the bop index.
     *
     * @param bopPointer the pointer to the last BOP
     */
    public void setBop(final int bopPointer) {

        this.bop = bopPointer;
    }

    /**
     * Setter for the offset of the last bop instruction.
     *
     * @param pointer the offset for the last bop instruction
     */
    public void setOffset(final int pointer) {

        this.bopOffset = pointer;
    }

    /**
     * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
     */
    public int write(final OutputStream stream) throws IOException {

        int n = 39;
        stream.write(Dvi.POST);
        write4(stream, bop);
        write4(stream, Dvi.DVI_UNIT_NUMERATOR);
        write4(stream, Dvi.DVI_UNIT_DENOMINATOR);
        write4(stream, mag);
        write4(stream, (int) maxHeight.getValue());
        write4(stream, (int) maxWidth.getValue());
        write2(stream, stackDepth);
        write2(stream, numberOfPages);

        for (int i = 0; i < fonts.size(); i++) {
            n += (((DviFntDef) fonts.get(i))).write(stream);
        }

        stream.write(Dvi.POST_POST);
        write4(stream, bopOffset);
        stream.write(Dvi.DVI_ID);

        stream.write(Dvi.PADDING_BYTE);
        stream.write(Dvi.PADDING_BYTE);
        stream.write(Dvi.PADDING_BYTE);
        stream.write(Dvi.PADDING_BYTE);

        return n;
    }

}
