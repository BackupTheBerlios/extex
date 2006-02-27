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
 * TODO gene: missing JavaDoc.
 *
 *  819.

 An active node for a given breakpoint contains six fields:

 link points to the next node in the list of active nodes; the
 last active node has link=last_active.

 break_node points to the passive node associated with this
 breakpoint.

 line_number is the number of the line that follows this
 breakpoint.

 fitness is the fitness classification of the line ending at this
 breakpoint.

 type is either hyphenated or unhyphenated, depending on
 whether this breakpoint is a disc_node.

 total_demerits is the minimum possible sum of demerits over all
 lines leading from the beginning of the paragraph to this breakpoint.


 The value of link(active) points to the first active node on a linked
 list
 of all currently active nodes. This list is in order by line_number,
 except that nodes with line_number > easy_line may be in any order
 relative
 to each other.

 define active_node_size=3  {number of words in active nodes}

 define fitness ::= subtype  {very_loose_fit .. tight_fit on
 final line for this break}

 define break_node ::= rlink  {pointer to the corresponding passive
 node}

 define line_number ::= llink  {line that begins at this breakpoint}

 define total_demerits(#) ::= mem[#+2].int  {the quantity that
 TeX minimizes}

 define unhyphenated=0  {the type of a normal active break node}

 define hyphenated=1  {the type of an active node that breaks at a
 disc_node}

 define last_active ::= active  {the active list ends where it begins}

 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ActiveNode {

    /**
     * The field <tt>breakNode</tt> contains the ...
     */
    private PassiveNode breakNode;

    /**
     * The field <tt>demerits</tt> contains the ...
     */
    private long demerits;

    /**
     * The field <tt>fitness</tt> contains the ...
     */
    private Fitness fitness;

    /**
     * The field <tt>hyphenated</tt> contains the ...
     */
    private boolean hyphenated;

    /**
     * The field <tt>lineNumber</tt> contains the ...
     */
    private int lineNumber;

    /**
     * The field <tt>preBreak</tt> contains the ...
     */
    private int preBreak;

    /**
     * Creates a new object.
     *
     * @param fitness ...
     * @param hyphenated ...
     * @param demerits ...
     * @param lineNumber ...
     * @param breakNode ...
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
     * Getter for preBreak.
     *
     * @return the preBreak
     */
    protected int getPreBreak() {

        return this.preBreak;
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
