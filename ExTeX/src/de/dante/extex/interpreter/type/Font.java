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
package de.dante.extex.interpreter.type;

/**
 * ...
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public interface Font {
    
    public abstract Dimen getWidth(String c);// TODO change to UnicodeChar
    public abstract Dimen getHeight(String c);// TODO change to UnicodeChar
    public abstract Dimen getDepth(String c);// TODO change to UnicodeChar

    public abstract boolean isDefined(String c);// TODO change to UnicodeChar

    public abstract Dimen kern(String c1, String c2);// TODO change to UnicodeChar
    public abstract String ligature(String c1, String c2);// TODO change to UnicodeChar

    public abstract Glue getSpace();
    public abstract Dimen getEm();
    public abstract Dimen getEx();

	public abstract Dimen getFontDimen(long index);

	public abstract String getFontName();
}
