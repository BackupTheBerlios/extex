/*
 * Copyright (C) 2003-2004  Gerd Neugebauer, Michael Niedermair
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

import de.dante.util.UnicodeChar;

/**
 * Font-Interface
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface Font {

	public abstract Dimen getWidth(UnicodeChar c);
	public abstract Dimen getHeight(UnicodeChar c);
	public abstract Dimen getDepth(UnicodeChar c);

	public abstract boolean isDefined(UnicodeChar c);

	public abstract Dimen kern(UnicodeChar c1, UnicodeChar c2);
	public abstract String ligature(UnicodeChar c1, UnicodeChar c2);

	public abstract Glue getSpace();
	public abstract Dimen getEm();
	public abstract Dimen getEx();

	public abstract Dimen getFontDimen(long index);

	public abstract String getFontName();
}
