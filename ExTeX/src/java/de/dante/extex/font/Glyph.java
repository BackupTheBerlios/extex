/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
 *
 */

package de.dante.extex.font;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.util.UnicodeChar;

/**
 * Interface for a Glyph
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public interface Glyph {

    /**
     * depth
     * @return Returns the depth.
     */
    Dimen getDepth();

    /**
     * @param d The depth to set.
     */
    void setDepth(Dimen d);

    //    /**
    //     * @param gsize         The size to set as <code>String</code>.
    //     * @param em            The em-size.
    //     * @param unitsperem    The unit per em.
    //     */
    //    void setDepth(String gsize, Dimen em, int unitsperem);

    //    /**
    //     * @param size          The size as fix word.
    //     * @param em            The em-size.
    //     */
    //    void setDepth(TFMFixWord size, Dimen em);

    /**
     * @return Returns the height.
     */
    Dimen getHeight();

    /**
     * @param h The height to set.
     */
    void setHeight(Dimen h);

    //    /**
    //     * @param gsize         The size to set as <code>String</code>.
    //     * @param em            The em-size.
    //     * @param unitsperem    The unit per em.
    //     */
    //    void setHeight(String gsize, Dimen em, int unitsperem);
    //
    //    /**
    //     * @param size          The size as fix word.
    //     * @param em            The em-size.
    //     */
    //    void setHeight(TFMFixWord size, Dimen em);

    /**
     * @return the italic correction.
     */
    Dimen getItalicCorrection();

    /**
     * @param d the italic correction to set.
     */
    void setItalicCorrection(Dimen d);

    //    /**
    //     * @param gsize         The size to set as <code>String</code>.
    //     * @param em            The em-size.
    //     * @param unitsperem    The unit per em.
    //     */
    //    void setItalicCorrection(String gsize, Dimen em, int unitsperem);
    //
    //    /**
    //     * @param size          The size as fix word.
    //     * @param em            The em-size.
    //     */
    //    void setItalicCorrection(TFMFixWord size, Dimen em);

    /**
     * @return Returns the width.
     */
    Dimen getWidth();

    /**
     * @param w The width to set.
     */
    void setWidth(Dimen w);

    //    /**
    //     * @param gsize         The size to set as <code>String</code>.
    //     * @param em            The em-size.
    //     * @param unitsperem    The unit per em.
    //     */
    //    void setWidth(String gsize, Dimen em, int unitsperem);
    //
    //    /**
    //     * @param size          The size as fix word.
    //     * @param em            The em-size.
    //     */
    //    void setWidth(TFMFixWord size, Dimen em);

    /**
     * @return Returns the name.
     */
    String getName();

    /**
     * @param n The name to set.
     */
    void setName(String n);

    /**
     * @return Returns the number.
     */
    String getNumber();

    /**
     * @param nr The number to set.
     */
    void setNumber(String nr);

    /**
     * Add kerning for the glyph.
     * @param kern  the kerning
     */
    void addKerning(Kerning kern);

    /**
     * Return the kerning for the glyph.
     * @param uc    the following character
     * @return  the kerning-size as <code>Dimen</code>
     */
    Dimen getKerning(UnicodeChar uc);

    /**
     * Add ligature for the glyph.
     * @param lig  the ligature
     */
    void addLigature(Ligature lig);

    /**
     * Return the ligature as <code>UnicodeChar</code>,
     * or <code>null</code>, if no ligature exists.
     *
     * If you get a ligature-character, then you MUST call the
     * method <code>getligature()</code> twice, if a ligature with
     * more then two characters exist.
     * (e.g. f - ff - ffl)
     * @param uc    the following character
     * @return  the ligature
     */
    UnicodeChar getLigature(UnicodeChar uc);

    /**
     * @return Returns the externalfile.
     * MGN change FontFile
     */
    FontByteArray getExternalFile();

    /**
     * @param file The externalfile to set.
     */
    void setExternalFile(FontByteArray file);

    /**
     * @return Returns the leftSpace.
     */
    Dimen getLeftSpace();

    /**
     * @param ls The leftSpace to set.
     */
    void setLeftSpace(Dimen ls);

    /**
     * @return Returns the rightSpace.
     */
    Dimen getRightSpace();

    /**
     * @param rs The rightSpace to set.
     */
    void setRightSpace(Dimen rs);
}