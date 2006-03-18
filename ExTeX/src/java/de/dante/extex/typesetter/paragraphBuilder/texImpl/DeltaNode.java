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

package de.dante.extex.typesetter.paragraphBuilder.texImpl;

import de.dante.extex.interpreter.type.glue.WideGlue;

/** 822.
 *
 * The active list also contains "delta" nodes that help the
 * algorithm compute the badness of individual lines. Such nodes
 * appear only between two active nodes, and they have
 * <i>type=delta_node</i>. If <i>p</i> and <i>r</i> are active nodes and if
 * <i>q</i> is a delta node between them, so that <i>link(p)=q</i> and
 * <i>link(q)=r</i>, then <i>q</i> tells the space difference between lines
 * in the horizontal list that
 * start after breakpoint <i>p</i> and lines that start after breakpoint <i>r</i>.
 * In other words, if we know the length of the line that starts
 * after <i>p</i> and ends at our current position, then the corresponding
 * length of the line that starts after <i>r</i> is obtained by adding the
 * amounts in node <i>q</i>. A delta node contains six scaled numbers, since
 * it must record the net change in glue stretchability with respect
 * to all orders of infinity. The natural width difference appears in
 * <i>mem[q+1].sc</i>; the stretch differences in units of pt, fil, fill,
 * and filll appear in <i>mem[q+2 .. q+5].sc</i>; and the shrink difference
 * appears in <i>mem[q+6].sc</i>. The subtype field of a delta node is not
 * used.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class DeltaNode extends WideGlue {

    /**
     * Creates a new object.
     *
     * @param glue the glue to copy
     */
    public DeltaNode(final WideGlue glue) {

        super();
        set(glue);
    }

}
