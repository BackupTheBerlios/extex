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
 * The passive nodes constitute linked lists with break points.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class PassiveNode {

    /**
     * The field <tt>curBreak</tt> contains the index of this break point.
     */
    private int curBreak;

    /**
     * The field <tt>nextBreak</tt> contains the next passive node.
     */
    private PassiveNode nextBreak = null;

    /**
     * The field <tt>prevBreak</tt> contains the previous passive node.
     */
    private PassiveNode prevBreak;

    /**
     * The field <tt>serial</tt> contains the serial number for printing.
     */
    private int serial;

    /**
     * Creates a new object.
     *
     * @param curBreak the index of this break point
     * @param serial the serial number for printing
     * @param prevBreak the previous passive node
     */
    public PassiveNode(final int curBreak, final int serial,
            final PassiveNode prevBreak) {

        super();
        this.curBreak = curBreak;
        this.serial = serial;
        this.prevBreak = prevBreak;
    }

    /**
     * Getter for curBreak.
     *
     * @return the curBreak
     */
    public int getCurBreak() {

        return this.curBreak;
    }

    /**
     * Getter for nextBreak.
     *
     * @return the nextBreak
     */
    public PassiveNode getNextBreak() {

        return this.nextBreak;
    }

    /**
     * Getter for prevBreak.
     *
     * @return the prevBreak
     */
    public PassiveNode getPrevBreak() {

        return this.prevBreak;
    }

    /**
     * Getter for serial.
     *
     * @return the serial
     */
    public int getSerial() {

        return this.serial;
    }

    /**
     * Setter for the next break node.
     *
     * @param pn the next passive node
     */
    public void setNextBreak(final PassiveNode pn) {

        nextBreak = pn;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }

    /**
     * Print the current object to a target string buffer.
     *
     * @param sb the target string buffer
     */
    public void toString(final StringBuffer sb) {

        sb.append("<");
        sb.append(curBreak);
        sb.append(">");
        if (nextBreak != null) {
            nextBreak.toString(sb);
        }
    }

}
