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
package de.dante.extex.typesetter;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;

/**
 * The dummy typesetter which does nothing but provide the appropriate
 * interface.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class NullTypesetterImpl implements Typesetter {

    /**
     * Creates a new object.
     *
     * @param config the configuration object to consider
     */
    public NullTypesetterImpl(final Configuration config) {
        super();
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setDocumentWriter(
     *     de.dante.extex.documentWriter.DocumentWriter)
     */
    public void setDocumentWriter(final DocumentWriter doc) {
        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace(
     *     de.dante.extex.interpreter.context.TypesettingContext,
     *     de.dante.extex.interpreter.type.Count)
     */
    public void addSpace(final TypesettingContext typesettingContext,
            final Count spacefactor) {
        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getCharNodeFactory()
     */
    public CharNodeFactory getCharNodeFactory() {
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#par()
     */
    public void par() {
        // nothing to do
    }

    /**
     * @see de.dante.util.configuration.Configurable#configure(
     *     de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration config) {
        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *     de.dante.extex.typesetter.Node)
     */
    public void add(final Node c) {
        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#close()
     */
    public NodeList close() {
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getMode()
     */
    public Mode getMode() {
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#addGlue(
     *     de.dante.extex.interpreter.type.Glue)
     */
    public void addGlue(final Glue g) {
        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *     de.dante.extex.interpreter.context.TypesettingContext,
     *     de.dante.util.UnicodeChar)
     */
    public void add(final TypesettingContext font, final UnicodeChar symbol) {
        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#finish()
     */
    public void finish() {
        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#toggleDisplaymath()
     */
    public void toggleDisplaymath() {
        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#toggleMath()
     */
    public void toggleMath() {
        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(
     *     de.dante.extex.interpreter.type.Count)
     */
    public void setSpacefactor(final Count f) {
        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(
     *     de.dante.extex.interpreter.type.Dimen)
     */
    public void setPrevDepth(final Dimen pd) {
        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#shipout(
     *     de.dante.extex.typesetter.NodeList)
     */
    public void shipout(final NodeList nodes) {
        // nothing to do
    }
    /**
     * @see de.dante.extex.typesetter.Typesetter#openHbox()
     */
    public void openHbox() {

        // TODO Auto-generated method stub
    }
    /**
     * @see de.dante.extex.typesetter.Typesetter#openVbox()
     */
    public void openVbox() {

        // TODO Auto-generated method stub
    }

}
