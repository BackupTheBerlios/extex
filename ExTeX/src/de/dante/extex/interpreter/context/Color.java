/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.context;

/**
 * This interface declares some methods to access the color in the RGB model
 * with an alpha channel.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public interface Color {

    /**
     * Getter for the red channel of the color in the RGB model.
     * The range of the value is 0x00 to 0xffff.
     *
     * @return the red channel
     */
    int getR();

    /**
     * Getter for the green channel of the color in the RGB model.
     * The range of the value is 0x00 to 0xffff.
     *
     * @return the green channel
     */
    int getG();

    /**
     * Getter for the blue channel of the color in the RGB model.
     * The range of the value is 0x00 to 0xffff.
     *
     * @return the blue channel
     */
    int getB();

    /**
     * Getter for the alpha channel.
     * The range of the value is 0x00 to 0xffff.
     *
     * @return the alpha channel
     */
    int getAlpha();

}
