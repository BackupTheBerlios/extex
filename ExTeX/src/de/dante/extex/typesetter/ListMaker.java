/*
 * Copyright (C) 2003  Gerd Neugebauer
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
package de.dante.extex.typesetter;


import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.util.GeneralException;

/**
 * ...
 * 
 * 
 * @see "TeX -- The Program [211]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface ListMaker {
    
    public abstract void add(Node node) throws GeneralException;

    public abstract void add(TypesettingContext typesettingContext, String symbol) throws GeneralException;

    public abstract void addSpace(TypesettingContext typesettingContext) throws GeneralException;

    public abstract void addGlue(Glue g) throws GeneralException;

    public abstract void par() throws GeneralException;

    public abstract void toggleMath() throws GeneralException;

    public abstract void toggleDisplaymath() throws GeneralException;

    public abstract void setSpacefactor(int f) throws GeneralException;

    public abstract void setPrevDepth(Dimen pd) throws GeneralException;

    public abstract Mode getMode();

    public abstract NodeList close();

}
