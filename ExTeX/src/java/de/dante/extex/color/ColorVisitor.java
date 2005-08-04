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

package de.dante.extex.color;

import de.dante.extex.color.model.CmykColor;
import de.dante.extex.color.model.GrayscaleColor;
import de.dante.extex.color.model.HsvColor;
import de.dante.extex.color.model.RgbColor;
import de.dante.util.GeneralException;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface ColorVisitor {

    /**
     * TODO gene: missing JavaDoc
     *
     * @param color the first parameter for the visitor is the color visited
     * @param value the second parameter is a  visitor specific object
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitCmyk(CmykColor color, Object value) throws GeneralException;

    /**
     * TODO gene: missing JavaDoc
     *
     * @param color the first parameter for the visitor is the color visited
     * @param value the second parameter is a  visitor specific object
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitGray(GrayscaleColor color, Object value)
            throws GeneralException;

    /**
     * TODO gene: missing JavaDoc
     *
     * @param color the first parameter for the visitor is the color visited
     * @param value the second parameter is a  visitor specific object
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitHsv(HsvColor color, Object value) throws GeneralException;

    /**
     * TODO gene: missing JavaDoc
     *
     * @param color the first parameter for the visitor is the color visited
     * @param value the second parameter is a  visitor specific object
     *
     * @return the visitor specific value
     *
     * @throws GeneralException in case of an error
     */
    Object visitRgb(RgbColor color, Object value) throws GeneralException;

}
