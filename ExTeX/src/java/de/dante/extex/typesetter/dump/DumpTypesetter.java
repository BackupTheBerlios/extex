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
package de.dante.extex.typesetter.dump;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class DumpTypesetter implements Typesetter {

    /**
     * The field <tt>charNodeFactory</tt> contains the factory to produce glyph
     * nodes.
     */
    private CharNodeFactory charNodeFactory = new CharNodeFactory();

    /**
     * Creates a new object.
     */
    public DumpTypesetter() {
        super();
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setDocumentWriter(de.dante.extex.documentWriter.DocumentWriter)
     */
    public void setDocumentWriter(final DocumentWriter doc) {
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getCharNodeFactory()
     */
    public CharNodeFactory getCharNodeFactory() {
        return charNodeFactory;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#finish(de.dante.extex.interpreter.context.Context)
     */
    public void finish(final Context context) throws GeneralException {
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#shipout(de.dante.extex.typesetter.NodeList)
     */
    public void shipout(final NodeList nodes) throws GeneralException {
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.typesetter.Node)
     */
    public void add(final Node node) throws GeneralException {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.interpreter.context.TypesettingContext, de.dante.util.UnicodeChar)
     */
    public void add(final TypesettingContext typesettingContext,
            final UnicodeChar symbol) throws GeneralException {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace(de.dante.extex.interpreter.context.TypesettingContext, de.dante.extex.interpreter.type.Count)
     */
    public void addSpace(final TypesettingContext typesettingContext,
            final Count spacefactor) throws GeneralException {

        // TODO
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(de.dante.extex.interpreter.type.Glue)
     */
    public void addGlue(final Glue g) throws GeneralException {

        // TODO
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#par()
     */
    public void par() throws GeneralException {
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#toggleMath()
     */
    public void toggleMath() throws GeneralException {
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#toggleDisplaymath()
     */
    public void toggleDisplaymath() throws GeneralException {
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(de.dante.extex.interpreter.type.Count)
     */
    public void setSpacefactor(final Count f) throws GeneralException {
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(de.dante.extex.interpreter.type.Dimen)
     */
    public void setPrevDepth(final Dimen pd) throws GeneralException {
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getMode()
     */
    public Mode getMode() {
        return Mode.HORIZONTAL;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#close()
     */
    public NodeList close() throws GeneralException {
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#openHbox()
     */
    public void openHbox() {

        // TODO
    }
    /**
     * @see de.dante.extex.typesetter.Typesetter#openVbox()
     */
    public void openVbox() {

        // TODO
    }

}
