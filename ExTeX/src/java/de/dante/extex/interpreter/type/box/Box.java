/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.type.box;

import java.io.Serializable;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.configuration.ConfigurationException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class Box implements Serializable {

    /**
     * The field <tt>nodes</tt> contains the node list stored in this box.
     * Thus is either a
     * {@link de.dante.extex.interpreter.type.node.HorizontalListNode HorizontalListNode}
     * or a
     * {@link de.dante.extex.interpreter.type.node.VerticalListNode VerticalListNode}
     * or it is <code>null</code>.
     * In case of a <code>null</code> value the box is void.
     */
    private NodeList nodes = null;

    /**
     * Creates a new object.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter stack
     * @param horizontal indicator whether a <tt>\hbox</tt> should be
     *     constructed. The alternative is a <tt>\vbox</tt>.
     *
     * @throws GeneralException in case of an error
     */
    public Box(final Context context, final TokenSource source,
            final Typesetter typesetter, final boolean horizontal)
            throws GeneralException {

        super();

        Token t = source.getToken();

        if (!t.isa(Catcode.LEFTBRACE)) {
            //TODO insert { and try to recover
            throw new GeneralHelpingException("TTP.MissingLeftBrace");
        }

        if (horizontal) {
            typesetter.openHbox();
        } else {
            typesetter.openVbox();
        }

        try {
            context.openGroup();
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        }

        source.executeGroup();
        nodes = typesetter.close();
    }

     /**
      * Clear the contents of the box. Afterwards the box is void.
      */
     public void clear() {
         nodes = null;
     }

    /**
     * Getter for the depth of this box.
     *
     * @return the depth of this box or 0pt in case of a void box
     */
    public Dimen getDepth() {

        return (nodes == null ? new Dimen(0) : nodes.getDepth());
    }

    /**
     * Getter for the height of this box.
     *
     * @return the height of this box or 0pt in case of a void box
     */
    public Dimen getHeight() {

        return (nodes == null ? new Dimen(0) : nodes.getHeight());
    }

    /**
     * Getter for the move parameter.
     * The move parameter describes hpw far from its original position the box
     * is moved leftwards or rightwards. Positive values indicate a move
     * rightwards.
     *
     * @return the move parameter
     */
    public Dimen getMove() {

        return this.nodes.getMove();
    }

    /**
     * Getter for nodes.
     *
     * @return the nodes.
     */
    public NodeList getNodes() {

        return nodes;
    }

    /**
     * Getter for the shift parameter.
     * The shift parameter describes hpw far from its original position the box
     * is shifted up or down. Positive values indicate a move upwards.
     *
     * @return the shift parameter
     */
    public Dimen getShift() {

        return this.nodes.getShift();
    }

    /**
     * Getter for the width of this box.
     *
     * @return the width of this box or 0pt in case of a void box
     */
    public Dimen getWidth() {

        return (nodes == null ? new Dimen(0) : nodes.getWidth());
    }

     /**
      * Checks whether the box is a hbox.
      *
      * @return <tt>true</tt> iff the box is a hbox.
      */
     public boolean isHbox() {
         return (nodes != null && nodes instanceof HorizontalListNode);
     }

     /**
      * Checks whether the box is a vbox.
      *
      * @return <tt>true</tt> iff the box is a vbox.
      */
     public boolean isVbox() {
         return (nodes != null && nodes instanceof VerticalListNode);
     }

     /**
      * Checks whether the box is void.
      *
      * @return <tt>true</tt> iff the box is void.
      */
     public boolean isVoid() {
         return nodes == null;
     }

    /**
     * Setter for the depth of the box.
     * If the box is void then this method simply does nothing.
     *
     * @param depth the new width
     */
    public void setDepth(final Dimen depth) {

        if (nodes != null) {
            nodes.setDepth(depth);
        }
    }

    /**
     * Setter for the height of the box.
     * If the box is void then this method simply does nothing.
     *
     * @param height the new width
     */
    public void setHeight(final Dimen height) {

        if (nodes != null) {
            nodes.setHeight(height);
        }
    }

    /**
     * Setter for the move parameter.
     * The move parameter describes hpw far from its original position the box
     * is moved leftwards or rightwards. Positive values indicate a move
     * rightwards.
     *
     * @param d the new move parameter
     */
    public void setMove(final Dimen d) {

        this.nodes.setMove(d);
    }

    /**
     * Setter for the shift parameter.
     * The shift parameter describes hpw far from its original position the box
     * is shifted up or down. Positive values indicate a move upwards.
     *
     * @param d the new shift parameter
     */
    public void setShift(final Dimen d) {

        this.nodes.setShift(d);
    }

    /**
     * Setter for the width of the box.
     * If the box is void then this method simply does nothing.
     *
     * @param width the new width
     */
    public void setWidth(final Dimen width) {

        if (nodes != null) {
            nodes.setWidth(width);
        }
    }

}
