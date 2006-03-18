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

/**
 * 819.
 *
 * An active node for a given breakpoint contains six fields:
 * <ul>
 * <li>
 *  <i>link</i> points to the next node in the list of active nodes; the
 *  last active node has <i>link=last_active</i>.
 * </li>
 * <li>
 *  <i>break_node</i> points to the passive node associated with this breakpoint.
 * </li>
 * <li>
 *  <i>line_number</i> is the number of the line that follows this breakpoint.
 * </li>
 * <li>
 *  <i>fitness</i> is the fitness classification of the line ending at this
 *  breakpoint.
 * </li>
 * <li>
 *  <i>type</i> is either hyphenated or unhyphenated, depending on
 *  whether this breakpoint is a disc_node.
 * </li>
 * <li>
 *  <i>total_demerits</i> is the minimum possible sum of demerits over all
 *  lines leading from the beginning of the paragraph to this breakpoint.
 * </li>
 * <ul>
 * <p>
 *  The value of <i>link(active)</i> points to the first active node on a
 *  linked list of all currently active nodes. This list is in order by
 *  <i>line_number<i>, except that nodes with <i>line_number &gt; easy_line</i>
 *  may be in any order relative to each other.
 * </p>
 *
 * <pre>
 * <b>define</b> active_node_size=3  {number of words in active nodes}
 *
 * <b>define</b> fitness ::= subtype  {very_loose_fit .. tight_fit on final line for this break}
 *
 * <b>define</b> break_node ::= rlink  {pointer to the corresponding passive node}
 *
 * <b>define</b> line_number ::= llink  {line that begins at this breakpoint}
 *
 * <b>define</b> total_demerits(#) ::= mem[#+2].int  {the quantity that TeX minimizes}
 *
 * <b>define</b> unhyphenated=0  {the type of a normal active break node}
 *
 * <b>define</b> hyphenated=1  {the type of an active node that breaks at a disc_node}
 *
 * <b>define</b> last_active ::= active  {the active list ends where it begins}
 * </pre>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ActiveNode {

    /**
     * The field <tt>breakNode</tt> contains the associated passive node.
     */
    private PassiveNode breakNode;

    /**
     * The field <tt>demerits</tt> contains the demerits for this break
     * point.
     */
    private long demerits;

    /**
     * The field <tt>fitness</tt> contains the fitness classification.
     */
    private Fitness fitness;

    /**
     * The field <tt>hyphenated</tt> contains the indicator for hyphenation at
     * this break point.
     */
    private boolean hyphenated;

    /**
     * The field <tt>lineNumber</tt> contains the sequence number of the line.
     */
    private int lineNumber;

    /**
     * Creates a new object.
     *
     * @param fitness the fitness classification
     * @param hyphenated the indicator for hyphenation at this break point
     * @param demerits the demerits for this break point
     * @param lineNumber the sequence number of the line
     * @param breakNode the index of the break
     */
    public ActiveNode(final Fitness fitness, final boolean hyphenated,
            final long demerits, final int lineNumber,
            final PassiveNode breakNode) {

        super();
        this.fitness = fitness;
        this.hyphenated = hyphenated;
        this.demerits = demerits;
        this.lineNumber = lineNumber;
        this.breakNode = breakNode;
    }

    /**
     * Getter for breakNode.
     *
     * @return the breakNode
     */
    protected PassiveNode getBreakNode() {

        return this.breakNode;
    }

    /**
     * Getter for fitness.
     *
     * @return the fitness
     */
    protected Fitness getFitness() {

        return this.fitness;
    }

    /**
     * Getter for lineNumber.
     *
     * @return the lineNumber
     */
    protected int getLineNumber() {

        return this.lineNumber;
    }

    /**
     * Getter for demerits.
     *
     * @return the demerits
     */
    protected long getTotalDemerits() {

        return this.demerits;
    }

    /**
     * Getter for hyphenated.
     *
     * @return the hyphenated
     */
    protected boolean isHyphenated() {

        return this.hyphenated;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return "active(" + breakNode + ")";
    }

}
