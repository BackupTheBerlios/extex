/*
 * Copyright (C) 2003-2004 The ExTeX Group
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

/**
 * Vistior-interface for <code>Nodes</code>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public interface NodeVisitor {

	public abstract Object visitAdjust(Object value, Object value2);
	public abstract Object visitAfterMath(Object value, Object value2);
	public abstract Object visitAlignedLeaders(Object value, Object value2);
	public abstract Object visitBeforeMath(Object value, Object value2);
	public abstract Object visitCenteredLeaders(Object value, Object value2);
	public abstract Object visitChar(Object value, Object value2);
	public abstract Object visitDiscretionary(Object value, Object value2);
	public abstract Object visitExpandedLeaders(Object value, Object value2);
	public abstract Object visitGlue(Object value, Object value2);
	public abstract Object visitHorizontalList(Object value, Object value2);
	public abstract Object visitInsertion(Object value, Object value2);
	public abstract Object visitKern(Object value, Object value2);
	public abstract Object visitLigature(Object value, Object value2);
	public abstract Object visitMark(Object value, Object value2);
	public abstract Object visitPenalty(Object value, Object value2);
	public abstract Object visitRule(Object value, Object value2);
	public abstract Object visitSpace(Object value, Object value2);
	public abstract Object visitVerticalList(Object value, Object value2);
	public abstract Object visitWhatsIt(Object value, Object value2);

}
