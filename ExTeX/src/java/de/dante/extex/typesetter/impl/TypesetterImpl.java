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

package de.dante.extex.typesetter.impl;

import java.io.IOException;
import java.util.ArrayList;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.i18n.GeneralPanicException;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.ligatureBuilder.LigatureBuilder;
import de.dante.extex.typesetter.ligatureBuilder.impl.LigatureBuilderImpl;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.paragraphBuilder.impl.ParagraphBuilderImpl;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;

/**
 * This is a reference implementation of the
 * {@link de.dante.extex.typesetter.Typesetter Typesetter} interface.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.20 $
 */
public class TypesetterImpl implements Typesetter, Manager {

    /**
     * The constant <tt>CLASS_ATTRIBUTE</tt> contains the name of the attribute
     * use to acquire the class name from the configuration.
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * The field <tt>charNodeFactory</tt> contains the factory to produce glyph
     * nodes.
     */
    private CharNodeFactory charNodeFactory = new CharNodeFactory();

    /**
     * The field <tt>documentWriter</tt> contains the document writer for
     * producing the output.
     */
    private DocumentWriter documentWriter;

    /**
     * The field <tt>ligatureBuilder</tt> contains the ...
     */
    private LigatureBuilder ligatureBuilder;

    /**
     * The field <tt>listMaker</tt> contains the current list maker for
     * efficiency. Thus we can avoid to peek at the stack whenever the list
     * maker is needed.
     */
    private ListMaker listMaker;

    /**
     * The field <tt>options</tt> contains the context for accessing parameters.
     */
    private TypesetterOptions options;

    /**
     * The field <tt>paragraphBuilder</tt> contains the ...
     */
    private ParagraphBuilder paragraphBuilder = null;

    /**
     * The field <tt>saveStack</tt> contains the stack of list makers.
     */
    private ArrayList saveStack = new ArrayList();

    /**
     * Creates a new object.
     *
     * @param config the configuration
     * @param theOptions the interpreter context
     */
    public TypesetterImpl(final Configuration config,
            final TypesetterOptions theOptions) {

        super();
        this.options = theOptions;
        listMaker = new VerticalListMaker(this);
        ligatureBuilder = new LigatureBuilderImpl(config);//TODO: IoC
        paragraphBuilder = new ParagraphBuilderImpl(config);//TODO: IoC
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *     de.dante.extex.typesetter.Node)
     */
    public void add(final Node c) throws GeneralException {

        listMaker.add(c);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *     de.dante.extex.interpreter.context.TypesettingContext,
     *     de.dante.util.UnicodeChar)
     */
    public void add(final TypesettingContext font, final UnicodeChar symbol)
            throws GeneralException {

        listMaker.add(font, symbol);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(
     *     de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addGlue(final Glue g) throws GeneralException {

        listMaker.addGlue(g);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace(
     *     de.dante.extex.interpreter.context.TypesettingContext,
     *     de.dante.extex.interpreter.type.count.Count)
     */
    public void addSpace(final TypesettingContext typesettingContext,
            final Count spacefactor) throws GeneralException {

        listMaker.addSpace(typesettingContext, null);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#close()
     */
    public NodeList close() throws GeneralException {

        NodeList nodes = listMaker.close();
        pop();
        return nodes;
    }

    /**
     * @see de.dante.extex.typesetter.impl.Manager#closeTopList()
     */
    public void closeTopList() throws GeneralException {

        NodeList list = listMaker.close();
        pop();
        if (list instanceof VerticalListNode) {
            NodeIterator it = list.iterator();
            while (it.hasNext()) {
                listMaker.add(it.next());
            }
        } else {
            listMaker.add(list);
        }
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#finish()
     */
    public void finish() throws GeneralException {

        par();
        //TODO incomplete
        try {
            documentWriter.shipout(listMaker.close());
            documentWriter.close();
        } catch (IOException e) {
            throw new GeneralPanicException(e);
        }
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getCharNodeFactory()
     */
    public CharNodeFactory getCharNodeFactory() {

        return charNodeFactory;
    }

    /**
     * @see de.dante.extex.typesetter.impl.Manager#getDocumentWriter()
     */
    public DocumentWriter getDocumentWriter() {

        return documentWriter;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getLastNode()
     */
    public Node getLastNode() {

        return this.listMaker.getLastNode();
    }
    /**
     * @see de.dante.extex.typesetter.impl.Manager#getLigatureBuilder()
     */
    public LigatureBuilder getLigatureBuilder() {

        return ligatureBuilder;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getMode()
     */
    public Mode getMode() {

        return listMaker.getMode();
    }

    /**
     * @see de.dante.extex.typesetter.impl.Manager#getOptions()
     */
    public TypesetterOptions getOptions() {

        return options;
    }

    /**
     * @see de.dante.extex.typesetter.impl.Manager#getParagraphBuilder()
     */
    public ParagraphBuilder getParagraphBuilder() {

        return paragraphBuilder;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#openHbox()
     */
    public void openHbox() {

        push(new RestrictedHorizontalListMaker(this));
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#openVbox()
     */
    public void openVbox() {

        push(new InnerVerticalListMaker(this));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#par()
     */
    public void par() throws GeneralException {

        this.listMaker.par();
    }

    /**
     * @see de.dante.extex.typesetter.impl.Manager#pop()
     */
    public void pop() throws GeneralException {

        if (saveStack.isEmpty()) {
            throw new GeneralPanicException("TTP.Confusion");
        }

        this.listMaker = (ListMaker) (saveStack.remove(saveStack.size() - 1));
    }

    /**
     * @see de.dante.extex.typesetter.impl.Manager#push(
     *      de.dante.extex.typesetter.ListMaker)
     */
    public void push(final ListMaker list) {

        saveStack.add(this.listMaker);
        this.listMaker = list;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setDocumentWriter(
     *     de.dante.extex.documentWriter.DocumentWriter)
     */
    public void setDocumentWriter(final DocumentWriter writer) {

        documentWriter = writer;
    }

    /**
     * Setter for the previous depth.
     *
     * @param pd the value for previous depth
     *
     * @throws GeneralException in case of an error
     */
    public void setPrevDepth(final Dimen pd) throws GeneralException {

        listMaker.setPrevDepth(pd);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(
     *     de.dante.extex.interpreter.type.count.Count)
     */
    public void setSpacefactor(final Count sf) throws GeneralException {

        listMaker.setSpacefactor(sf);
    }

    /**
     * This is the entry point for the document writer. Here it receives a
     * complete node list to be sent to the output writer.
     *
     * @param nodes the nodes to send
     *
     * @throws GeneralException in case of an error
     */
    public void shipout(final NodeList nodes) throws GeneralException {

        try {
            documentWriter.shipout(nodes);
        } catch (IOException e) {
            throw new GeneralPanicException(e);
        }
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#toggleDisplaymath()
     */
    public void toggleDisplaymath() throws GeneralException {

        listMaker.toggleDisplaymath();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#toggleMath()
     */
    public void toggleMath() throws GeneralException {

        listMaker.toggleMath();
    }
}