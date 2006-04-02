/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.listMaker;

import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.Locator;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides a maker for a vertical list.
 *
 * <doc name="baselineskip" type="register">
 * <h3>The Parameter <tt>\baselineskip</tt></h3>
 *
 * </doc>
 *
 * <doc name="lineskiplimit" type="register">
 * <h3>The Parameter <tt>\lineskiplimit</tt></h3>
 *
 * </doc>
 *
 * <doc name="lineskip" type="register">
 * <h3>The Parameter <tt>\lineskip</tt></h3>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.22 $
 */
public class VerticalListMaker extends InnerVerticalListMaker {

    /**
     * Creates a new object.
     *
     * @param manager the manager to ask for global changes
     * @param locator the locator
     */
    public VerticalListMaker(final ListManager manager, final Locator locator) {

        super(manager, locator);
    }

    /**
     *
     * <i>
     *  <p>
     *   679.  When a box is being appended to the current vertical list,
     *   the baselineskip calculation is handled by the append_to_vlist routine.
     *  </p>
     *  <pre>
     *   procedure append_to_vlist(b:pointer);
     *    var d: scaled;  {deficiency of space between baselines}
     *    p: pointer;  {a new glue specification}
     * begin if prev_depth>ignore_depth then
     *    begin d ? width(baseline_skip)-prev_depth-height( b);
     *    if d<line_skip_limit then p ? new_param_glue(line_skip_code)
     *      else begin p ? new_skip_param(baseline_skip_code); width(temp_ptr) ? d;  {temp_ptr=glue_ptr(p)}
     *      end ;
     *    link(tail) ? p; tail ? p;
     *    end ;
     * link(tail) ? b; tail ? b; prev_depth ? depth(b);
     * end ;
     *  <pre>
     * </i>
     *
     *
     * @see de.dante.extex.typesetter.ListMaker#addAndAdjust(
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void addAndAdjust(final NodeList nodes,
            final TypesetterOptions context)
            throws TypesetterException,
                ConfigurationException {

        FixedDimen prevDepth = getPrevDepth();
        int size = nodes.size();
        FixedGlue baselineSkip = context.getGlueOption("baselineskip");
        FixedDimen lineSkipLimit = context.getDimenOption("lineskiplimit");
        Node node;
        Glue d;

        for (int i = 0; i < size; i++) {
            node = nodes.get(i);
            if (node instanceof HorizontalListNode) {
                if (prevDepth != null) {
                    d = new Glue(baselineSkip);
                    d.subtract(prevDepth);
                    d.subtract(node.getHeight());
                    if (d.lt(lineSkipLimit)) {
                        add(context.getGlueOption("lineskip"));
                    } else {
                        add(d);
                    }
                }

                prevDepth = node.getDepth();
            }
            add(node);
        }
        setPrevDepth(prevDepth);
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.InnerVerticalListMaker#getMode()
     */
    public Mode getMode() {

        return Mode.VERTICAL;
    }

}
