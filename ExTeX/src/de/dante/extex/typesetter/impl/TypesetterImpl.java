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
package de.dante.extex.typesetter.impl;

import java.util.Stack;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.Box;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;

/**
 * This is a reference implementation of the Typesetter interface.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class TypesetterImpl implements Typesetter, Manager {
    /** the factory to produce glyph nodes */
    private CharNodeFactory charNodeFactory = new CharNodeFactory();

    /** the document writer for producing the output */
    private DocumentWriter documentWriter;

    /** the current list maker for efficiency */
    private ListMaker listMaker;

    /** the stack of list makers */
    private Stack saveStack = new Stack();

    /**
     * Creates a new object.
     */
    public TypesetterImpl(Configuration config) {
        super();
        listMaker = new VerticalListMaker(this);
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getCharNodeFactory()
     */
    public CharNodeFactory getCharNodeFactory() {
        return charNodeFactory;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setDocumentWriter(de.dante.extex.documentWriter.DocumentWriter)
     */
    public void setDocumentWriter(DocumentWriter doc) {
        this.documentWriter = doc;
    }

    /**
     * @see de.dante.extex.typesetter.impl.Manager#getDocumentWriter()
     */
    public DocumentWriter getDocumentWriter() {
        return documentWriter;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getMode()
     */
    public Mode getMode() {
        return listMaker.getMode();
    }

    /**
     * ...
     *
     * @param c
     * @throws GeneralException
     */
    public void add(Node c) throws GeneralException {
        listMaker.add(c);
    }

    /**
     * ...
     *
     * @param font
     * @param symbol
     * @throws GeneralException
     */
    public void add(TypesettingContext font, UnicodeChar symbol) throws GeneralException {
        listMaker.add(font, symbol);
    }

    /**
     * ...
     *
     * @param g
     * @throws GeneralException
     */
    public void addGlue(Glue g) throws GeneralException {
        listMaker.addGlue(g);
    }

    /**
     * ...
     *
     * @throws GeneralException
     */
    public void addSpace(TypesettingContext typesettingContext, Count spacefactor) throws GeneralException {
        listMaker.addSpace(typesettingContext, null);
    }

    /**
     * ...
     *
     * @return
     */
    public NodeList close() {
        return null;
    }

    /**
     * ...
     *
     * @return
     */
    public void closeTopList() throws GeneralException {
        NodeList list = listMaker.close();
        pop();
        listMaker.add(list);
    }

    /**
     * @see de.dante.util.configuration.Configurable#configure(de.dante.util.configuration.Configuration)
     */
    public void configure(Configuration config)
                   throws ConfigurationException {
        // TODO not needed yet
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#finish()
     */
    public void finish() throws GeneralException {
        par();
        //TODO
        System.err.println(listMaker.close().toString());
        
        
    }

    /**
     * ...
     *
     * @throws GeneralException in case of an error
     */
    public void par() throws GeneralException {
        listMaker.par();
    }

    /**
     * ...
     *
     * @throws GeneralException ...
     */
    public void pop() throws GeneralException {
        if (saveStack.empty()) {
            throw new GeneralException("xxx");// TODO
        }

        this.listMaker = (ListMaker) (saveStack.pop());
    }

    /**
     * ...
     *
     * @param typesetter ...
     */
    public void push(ListMaker list) {
        saveStack.push(this.listMaker);
        this.listMaker = list;
    }
    
    /**
     * ...
     * 
     * 
     */
    public void toggleDisplaymath() throws GeneralException {
        listMaker.toggleDisplaymath();
    }

    /**
     * ...
     * 
     * 
     */
    public void toggleMath() throws GeneralException {
        listMaker.toggleMath();
    }

    /**
     * ...
     * 
     * @param f the new value for the spacefactor
     * @throws GeneralException in case of an error
     */
    public void setSpacefactor(Count f) throws GeneralException {
        listMaker.setSpacefactor(f);
    }

    /**
     * Setter for the previous depth.
     * 
     * @param pd the value for previous depth
     * @throws GeneralException in case of an error
     */
    public void setPrevDepth(Dimen pd) throws GeneralException {
        listMaker.setPrevDepth(pd);
    }

    /**
     * This is the entry point for the document writer. Here it receives a
     * complete node list to be sent to the output writer.
     * 
     * @param nodes the nodes to send
     */
    public void shipout(Box nodes) {
        documentWriter.shipout(nodes);
    }
}
