/*
 * Copyright (C) 2003  Gerd Neugebauer
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
import de.dante.extex.interpreter.type.Box;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;

/**
 * The dummy typesetter which does nothing but provide the appropriate
 * interface.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class NullTypesetterImpl implements Typesetter {

    /**
     * Creates a new object.
     */
    public NullTypesetterImpl(Configuration config) {
        super();
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setDocumentWriter(de.dante.extex.documentWriter.DocumentWriter)
     */
    public void setDocumentWriter(DocumentWriter doc) {
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#addSpace()
     */
    public void addSpace(TypesettingContext typesettingContext, Count spacefactor) {
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
    }

    /**
     * @see de.dante.util.configuration.Configurable#configure(de.dante.util.configuration.Configuration)
     */
    public void configure(Configuration config) throws ConfigurationException {
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#add(de.dante.extex.interpreter.type.node.CharNode)
     */
    public void add(Node c) {
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
     * @see de.dante.extex.typesetter.Typesetter#open()
     */
    public void open() {
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#addGlue(de.dante.extex.interpreter.type.Glue)
     */
    public void addGlue(Glue g) {
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#add(de.dante.extex.interpreter.type.Font, java.lang.String)
     */
    public void add(TypesettingContext font, UnicodeChar symbol) {
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#finish()
     */
    public void finish() {
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#toggleDisplaymath()
     */
    public void toggleDisplaymath() {
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#toggleMath()
     */
    public void toggleMath() {
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(int)
     */
    public void setSpacefactor(Count f) throws GeneralException {
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(de.dante.extex.interpreter.type.Dimen)
     */
    public void setPrevDepth(Dimen pd) throws GeneralException {
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#shipout(de.dante.extex.typesetter.NodeList)
     */
    public void shipout(Box nodes) throws GeneralException {
    }
}
