/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
import java.util.Stack;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.i18n.GeneralPanicException;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.Box;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.typesetter.LineBreaker;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;

/**
 * This is a reference implementation of the
 * {@link de.dante.extex.typesetter.Typesetter Typesetter}interface.
 * 
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class TypesetterImpl implements Typesetter, Manager {

	/**
	 * The constant <tt>LINEBREAKER_TAG</tt> ...
	 */
	private static final String LINEBREAKER_TAG = "LineBreaker";

	/**
	 * The constant <tt>CLASS_ATTRIBUTE</tt> ...
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
	 * The field <tt>listMaker</tt> contains the current list maker for
	 * efficiency. Thus we can avoid to peek at the stack whenever the list
	 * maker is needed.
	 */
	private ListMaker listMaker;

	/**
	 * The field <tt>saveStack</tt> contains the stack of list makers.
	 */
	private Stack saveStack = new Stack();

	/**
	 * The <code>LineBreaker</code>
	 */
	private LineBreaker linebreaker;

	/**
	 * Creates a new object.
	 *
	 * @param config the configuration
	 */
	public TypesetterImpl(final Configuration config, Context context) throws ConfigurationException, GeneralException {
		super();
		this.context = context;

		// LineBreaker
		Configuration linebreakerConfiguration = config.getConfiguration(LINEBREAKER_TAG);
		String lbClass = linebreakerConfiguration.getAttribute(CLASS_ATTRIBUTE);

		if (lbClass == null || lbClass.equals("")) {
			throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE, linebreakerConfiguration);
		}

		try {
			linebreaker = (LineBreaker) (Class.forName(lbClass).newInstance());
		} catch (Exception e) {
			throw new ConfigurationInstantiationException(e);
		}

		// vertical list
		listMaker = new VerticalListMaker(this);
	}

	/**
	 * The context
	 */
	private Context context;

	/**
	 * @see de.dante.extex.typesetter.Typesetter#getCharNodeFactory()
	 */
	public CharNodeFactory getCharNodeFactory() {
		return charNodeFactory;
	}

	/**
	 * @see de.dante.extex.typesetter.Typesetter#setDocumentWriter(de.dante.extex.documentWriter.DocumentWriter)
	 */
	public void setDocumentWriter(final DocumentWriter doc) {
		documentWriter = doc;
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
	 * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.typesetter.Node)
	 */
	public void add(final Node c) throws GeneralException {
		listMaker.add(c);
	}

	/**
	 * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.interpreter.context.TypesettingContext, de.dante.util.UnicodeChar)
	 */
	public void add(final TypesettingContext font, final UnicodeChar symbol) throws GeneralException {
		listMaker.add(font, symbol);
	}

	/**
	 * @see de.dante.extex.typesetter.ListMaker#addGlue(de.dante.extex.interpreter.type.Glue)
	 */
	public void addGlue(final Glue g) throws GeneralException {
		listMaker.addGlue(g);
	}

	/**
	 * @see de.dante.extex.typesetter.ListMaker#addSpace(de.dante.extex.interpreter.context.TypesettingContext,
	 *      de.dante.extex.interpreter.type.Count)
	 */
	public void addSpace(final TypesettingContext typesettingContext, final Count spacefactor) throws GeneralException {
		listMaker.addSpace(typesettingContext, null);
	}

	/**
	 * @see de.dante.extex.typesetter.ListMaker#close()
	 */
	public NodeList close() {
		return null;
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
	 * @see de.dante.extex.typesetter.Typesetter#finish(de.dante.extex.interpreter.context.Context)
	 */
	public void finish(final Context context) throws GeneralException {
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
	 * @see de.dante.extex.typesetter.ListMaker#par()
	 */
	public void par() throws GeneralException {
		listMaker.par();
	}

	/**
	 * @see de.dante.extex.typesetter.impl.Manager#pop()
	 */
	public void pop() throws GeneralException {
		if (saveStack.empty()) {
			throw new GeneralException("xxx");
		}

		this.listMaker = (ListMaker) (saveStack.pop());
	}

	/**
	 * @see de.dante.extex.typesetter.impl.Manager#push(de.dante.extex.typesetter.ListMaker)
	 */
	public void push(final ListMaker list) {
		saveStack.push(this.listMaker);
		this.listMaker = list;
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

	/**
	 * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(de.dante.extex.interpreter.type.Count)
	 */
	public void setSpacefactor(final Count sf) throws GeneralException {
		listMaker.setSpacefactor(sf);
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
	 * This is the entry point for the document writer. Here it receives a
	 * complete node list to be sent to the output writer.
	 *
	 * @param nodes the nodes to send
	 */
	public void shipout(final Box nodes) throws GeneralException {
		try {
			documentWriter.shipout(nodes);
		} catch (IOException e) {
			throw new GeneralPanicException(e);
		}
	}

	/**
	 * @see de.dante.extex.typesetter.impl.Manager#getContext()
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @see de.dante.extex.typesetter.impl.Manager#getLineBreaker()
	 */
	public LineBreaker getLineBreaker() {
		return linebreaker;
	}
}
