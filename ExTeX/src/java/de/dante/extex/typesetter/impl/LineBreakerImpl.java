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

package de.dante.extex.typesetter.impl;

import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.typesetter.LineBreaker;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.typesetter.NodeList;

/**
 * Implementation for a <code>LineBreaker</code>.
 *
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class LineBreakerImpl implements LineBreaker {

    /**
     * Creates a new Obejct
     */
    public LineBreakerImpl() {

        super();
    }

    /**
     * @see de.dante.extex.typesetter.LineBreaker#breakLines(
     *      de.dante.extex.interpreter.type.node.HorizontalListNode,
     *      de.dante.extex.interpreter.context.Context)
     */
    // TODO incomplete (very simple solution, only for test)
    public NodeList breakLines(final HorizontalListNode nodes, final Manager manager) {

        VerticalListNode vlnode = new VerticalListNode();

        // linebreak already done?
        //gene: trashed because of merge
        //if (!nodes.isLineBreak()) {
        if (true) {

            int linenumber = 1;

            addParSkipBefore(vlnode, manager);
            addIndent(nodes, manager);

            Dimen hsize = calculateLineWidth(manager, linenumber);
            if (nodes.getWidth().lt(hsize)) {
                addLineSkip(vlnode, manager);
                //gene: trashed because of merge
                //nodes.setLineBreak(true);
                vlnode.add(nodes);
                return vlnode;
            } else {
                // break lines
                NodeIterator it = nodes.iterator();
                Dimen line = Dimen.ZERO_PT;
                HorizontalListNode hnode = new HorizontalListNode();
                while (it.hasNext()) {
                    Node node = it.next();
                    line.add(node.getWidth());
                    if (line.lt(hsize)) {
                        hnode.add(node);
                    } else {
                        addLineSkip(vlnode, manager);
                        vlnode.add(hnode);
                        hnode = new HorizontalListNode();
                        hnode.add(node);
                        line = node.getWidth();
                    }
                    hsize = calculateLineWidth(manager, ++linenumber);
                }
                if (!hnode.empty()) {
                    addLineSkip(vlnode, manager);
                    vlnode.add(hnode);
                }
            }
        } else {
            addLineSkip(vlnode, manager);
            vlnode.add(nodes);
        }

        addParSkipAfter(vlnode, manager);
        restoreParameter(manager);
        return vlnode;
    }

    /**
     * Add a paragraph-indent if necessary
     * @param hln       the <code>HorizontalListNode</code>
     * @param manager   the manager 
     */
    private void addIndent(final HorizontalListNode hln, final Manager manager) {

        // TODO incomplete
    }

    /**
     * Add a skip before a paragraph
     * @param vln        the vertical list
     * @param manager    the manager
     */
    private void addParSkipBefore(final VerticalListNode vln, final Manager manager) {

        vln.addSkip(new Glue(Dimen.ONE * 20)); // TODO change
    }

    /**
     * Add a skip after a paragraph
     * @param vln        the vertical list
     * @param manager    the manager
     */
    private void addParSkipAfter(final VerticalListNode vln, final Manager manager) {

        vln.addSkip(new Glue(Dimen.ONE * 20)); // TODO change
    }

    /**
     * Add a skip between lines
     * @param vln        the vertical list
     * @param manager    the manager
     */
    private void addLineSkip(final VerticalListNode vln, final Manager manager) {

        vln.addSkip(new Glue(Dimen.ONE * 12)); // TODO change
    }

    /**
     * Restore all paramter after the paragraph
     * @param manager    the manager
     */
    private void restoreParameter(final Manager manager) {

        // TODO incomplete
    }

    /**
     * Calculate the width of the line.
     * @param manager        the manager
     * @param linenumber    the linenumber
     * @return    the width of the line
     */
    private Dimen calculateLineWidth(final Manager manager, final int linenumber) {

        return manager.getContext().getDimen("hsize"); // TODO incomplete
    }
}
