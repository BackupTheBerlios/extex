/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.impl;

import java.util.ArrayList;
import java.util.logging.Logger;

import de.dante.extex.backend.BackendDriver;
import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.OutputRoutine;
import de.dante.extex.typesetter.ParagraphObserver;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.InvalidSpacefactorException;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.exception.TypesetterUnsupportedException;
import de.dante.extex.typesetter.listMaker.ListManager;
import de.dante.extex.typesetter.listMaker.VerticalListMaker;
import de.dante.extex.typesetter.pageBuilder.PageBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeIterator;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.InsertionNode;
import de.dante.extex.typesetter.type.node.PenaltyNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.extex.typesetter.type.node.factory.CachingNodeFactory;
import de.dante.extex.typesetter.type.node.factory.NodeFactory;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This is a reference implementation of the
 * {@link de.dante.extex.typesetter.Typesetter Typesetter} interface.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.85 $
 */
public class TypesetterImpl
        implements
            Typesetter,
            ListManager,
            Localizable,
            LogEnabled {

    /**
     * The field <tt>backend</tt> contains the back-end driver for
     * producing the output.
     */
    private BackendDriver backend = null;

    /**
     * The field <tt>listMaker</tt> contains the current list maker for
     * efficiency. Thus we can avoid to peek at the stack whenever the list
     * maker is needed.
     */
    private ListMaker listMaker;

    /**
     * The field <tt>localizer</tt> contains the localizer.
     */
    private Localizer localizer;

    /**
     * The field <tt>logger</tt> contains the logger to use.
     */
    private Logger logger = null;

    /**
     * The field <tt>charNodeFactory</tt> contains the factory to produce glyph
     * nodes.
     */
    private NodeFactory nodeFactory = new CachingNodeFactory();

    /**
     * The field <tt>options</tt> contains the context for accessing parameters.
     */
    private TypesetterOptions options;

    /**
     * The field <tt>outputRoutine</tt> contains the output routine.
     */
    private OutputRoutine outputRoutine = null;

    /**
     * The field <tt>pageBuilder</tt> contains the current page builder.
     */
    private PageBuilder pageBuilder = null;

    /**
     * The field <tt>paragraphBuilder</tt> contains the current paragraph
     * builder.
     */
    private ParagraphBuilder paragraphBuilder = null;

    /**
     * The field <tt>saveStack</tt> contains the stack of list makers.
     */
    private ArrayList saveStack = new ArrayList();

    /**
     * The field <tt>shipoutMark</tt> contains the recorded state of the
     * shipout mark. Initially the shipout mark is <code>false</code>.
     */
    private boolean shipoutMark = false;

    /**
     * Creates a new object and initializes it to receive material.
     * To make it fully functionality is required that the paragraph builder
     * and the ligature builder are provided before they are used.
     */
    public TypesetterImpl() {

        super();

        listMaker = new VerticalListMaker(this, new Locator("", 0, "", 0)); //TODO gene: find better initial locator
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *     de.dante.extex.typesetter.type.Node)
     */
    public void add(final Node node)
            throws TypesetterException,
                ConfigurationException {

        if (node == null) {
            return;
        }

        listMaker.add(node);

        if (saveStack == null
                && (node instanceof PenaltyNode
                        || node instanceof InsertionNode
                        || node instanceof HorizontalListNode //
                || node instanceof VerticalListNode)) {

            pageBuilder.inspectAndBuild((VerticalListNode) listMaker
                    .complete(options), this);
        }
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(
     *     de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addGlue(final Glue glue) throws TypesetterException {

        listMaker.addGlue(glue);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace(
     *     de.dante.extex.interpreter.context.TypesettingContext,
     *     de.dante.extex.interpreter.type.count.Count)
     */
    public void addSpace(final TypesettingContext typesettingContext,
            final Count spacefactor)
            throws TypesetterException,
                ConfigurationException {

        listMaker.addSpace(typesettingContext, null);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#afterParagraph(
     *      ParagraphObserver)
     */
    public void afterParagraph(final ParagraphObserver observer) {

        listMaker.afterParagraph(observer);
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.ListManager#buildParagraph(
     *      de.dante.extex.typesetter.type.node.HorizontalListNode)
     */
    public NodeList buildParagraph(final HorizontalListNode nodes)
            throws TypesetterException {

        return this.paragraphBuilder.build(nodes);
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#clearShipoutMark()
     */
    public void clearShipoutMark() {

        shipoutMark = false;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#complete(TypesetterOptions)
     */
    public NodeList complete(final TypesetterOptions context)
            throws TypesetterException,
                ConfigurationException {

        NodeList nodes = listMaker.complete(context);
        pop();
        return nodes;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#cr(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public void cr(final Context context, final TypesettingContext tc,
            final UnicodeChar uc) throws TypesetterException {

        listMaker.cr(context, tc, uc);
    }

    /**
     * Setter for the localizer.
     *
     * @param theLocalizer the new localizer
     *
     * @see de.dante.util.framework.i18n.Localizable#enableLocalization(
     *      de.dante.util.framework.i18n.Localizer)
     */
    public void enableLocalization(final Localizer theLocalizer) {

        localizer = theLocalizer;
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        logger = theLogger;
        if (pageBuilder instanceof LogEnabled) {
            ((LogEnabled) pageBuilder).enableLogging(theLogger);
        }
        if (paragraphBuilder instanceof LogEnabled) {
            ((LogEnabled) paragraphBuilder).enableLogging(theLogger);
        }
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.ListManager#endParagraph()
     */
    public void endParagraph()
            throws TypesetterException,
                ConfigurationException {

        NodeList list = listMaker.complete(options);
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
    public void finish() throws TypesetterException, ConfigurationException {

        par();
        pageBuilder.flush(listMaker.complete(options), this);
        if (saveStack != null && saveStack.size() != 0) {
            throw new InternalError("typesetter.saveStack.notEmpty");
        }
        pageBuilder.close();
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getDocumentWriter()
     */
    public DocumentWriter getDocumentWriter() {

        return backend.getDocumentWriter();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getLastNode()
     */
    public Node getLastNode() {

        return listMaker.getLastNode();
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getListMaker()
     */
    public ListMaker getListMaker() {

        return listMaker;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getLocator()
     */
    public Locator getLocator() {

        return listMaker.getLocator();
    }

    /**
     * Getter for the manager of the list maker stack.
     * This instance also acts as a manager.
     *
     * @return this instance
     *
     * @see de.dante.extex.typesetter.Typesetter#getManager()
     */
    public ListManager getManager() {

        return this;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getMode()
     */
    public Mode getMode() {

        return listMaker.getMode();
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getNodeFactory()
     */
    public NodeFactory getNodeFactory() {

        return nodeFactory;
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.ListManager#getOptions()
     */
    public TypesetterOptions getOptions() {

        return options;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getPrevDepth()
     */
    public FixedDimen getPrevDepth() throws TypesetterUnsupportedException {

        return this.listMaker.getPrevDepth();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getSpacefactor()
     */
    public long getSpacefactor() throws TypesetterUnsupportedException {

        return this.listMaker.getSpacefactor();
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#isShipoutMark()
     */
    public boolean isShipoutMark() {

        return shipoutMark;
    }

    /**
     * Notification method to deal the case that a left brace has been
     * encountered.
     */
    public void leftBrace() {

        listMaker.leftBrace();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#letter(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public void letter(final Context context, final TypesettingContext tc,
            final UnicodeChar uc, final Locator locator)
            throws TypesetterException {

        listMaker.letter(context, tc, uc, locator);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#mathShift(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void mathShift(final Context context, final TokenSource source,
            final Token t) throws TypesetterException, ConfigurationException {

        listMaker.mathShift(context, source, t);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#par()
     */
    public void par() throws TypesetterException, ConfigurationException {

        listMaker.par();

        if (saveStack.size() == 0) {
            pageBuilder.inspectAndBuild((VerticalListNode) listMaker
                    .complete(options), this);
        }
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.ListManager#pop()
     */
    public ListMaker pop() throws TypesetterException {

        if (saveStack.isEmpty()) {
            throw new ImpossibleException("Typesetter.EmptyStack");
        }
        ListMaker current = listMaker;
        listMaker = (ListMaker) (saveStack.remove(saveStack.size() - 1));
        return current;
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.ListManager#push(
     *      de.dante.extex.typesetter.ListMaker)
     */
    public void push(final ListMaker list) throws TypesetterException {

        saveStack.add(listMaker);
        listMaker = list;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#removeLastNode()
     */
    public void removeLastNode() {

        if (listMaker != null) {
            listMaker.removeLastNode();
        }
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#rightBrace()
     */
    public void rightBrace() throws TypesetterException {

        listMaker.rightBrace();
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setDocumentWriter(
     *     de.dante.extex.backend.documentWriter.DocumentWriter)
     */
    public void setBackend(final BackendDriver driver) {

        backend = driver;
        pageBuilder.setBackend(driver);
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setNodeFactory(
     *      de.dante.extex.typesetter.type.node.factory.NodeFactory)
     */
    public void setNodeFactory(final NodeFactory nodeFactory) {

        this.nodeFactory = nodeFactory;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setOptions(
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void setOptions(final TypesetterOptions options) {

        this.options = options;
        pageBuilder.setOptions(options);
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setOutputRoutine(
     *      de.dante.extex.typesetter.OutputRoutine)
     */
    public void setOutputRoutine(final OutputRoutine output) {

        this.outputRoutine = output;
        if (this.pageBuilder != null) {
            this.pageBuilder.setOutputRoutine(output);
        }
    }

    /**
     * Setter for the page builder.
     *
     * @param pageBuilder the new page builder
     *
     * @see de.dante.extex.typesetter.Typesetter#setPageBuilder(
     *      de.dante.extex.typesetter.pageBuilder.PageBuilder)
     */
    public void setPageBuilder(final PageBuilder pageBuilder) {

        this.pageBuilder = pageBuilder;
        pageBuilder.setBackend(backend);
        pageBuilder.setOutputRoutine(this.outputRoutine);
        if (pageBuilder instanceof LogEnabled) {
            ((LogEnabled) pageBuilder).enableLogging(logger);
        }
    }

    /**
     * Setter for paragraph builder.
     *
     * @param parBuilder the paragraph builder to set.
     */
    public void setParagraphBuilder(final ParagraphBuilder parBuilder) {

        paragraphBuilder = parBuilder;
        if (paragraphBuilder instanceof LogEnabled) {
            ((LogEnabled) paragraphBuilder).enableLogging(logger);
        }
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setPrevDepth(final Dimen pd)
            throws TypesetterUnsupportedException {

        listMaker.setPrevDepth(pd);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(
     *     de.dante.extex.interpreter.type.count.Count)
     */
    public void setSpacefactor(final Count sf)
            throws TypesetterUnsupportedException,
                InvalidSpacefactorException {

        listMaker.setSpacefactor(sf);
    }

    /**
     * This is the entry point for the document writer. Here it receives a
     * complete node list to be sent to the output writer.
     *
     * @param nodes the nodes to send
     *
     * @throws TypesetterException in case of an error
     *
     * @see de.dante.extex.typesetter.Typesetter#shipout(
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public void shipout(final NodeList nodes) throws TypesetterException {

        pageBuilder.flush(nodes, this);
        shipoutMark = true;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#showlist(java.lang.StringBuffer,
     *       long, long)
     */
    public void showlist(final StringBuffer sb, final long depth,
            final long breadth) {

        listMaker.showlist(sb, depth, breadth);
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#showlists(
     *      java.lang.StringBuffer, long, long)
     */
    public void showlists(final StringBuffer sb, final long depth,
            final long breadth) {

        sb.append(localizer.format("Showlist.Format", listMaker.getMode()
                .toString(), Integer.toString(listMaker.getLocator()
                .getLineNumber())));
        listMaker.showlist(sb, depth, breadth);

        for (int i = saveStack.size() - 1; i >= 0; i--) {
            ListMaker lm = (ListMaker) saveStack.get(i);
            sb.append(localizer.format("Showlist.Format", lm.getMode()
                    .toString(), Integer.toString(lm.getLocator()
                    .getLineNumber())));
            lm.showlist(sb, depth, breadth);
        }
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#subscriptMark(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void subscriptMark(final Context context, final TokenSource source,
            final Typesetter typesetter, final Token t)
            throws TypesetterException {

        listMaker.subscriptMark(context, source, typesetter, t);
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#superscriptMark(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      Typesetter, de.dante.extex.scanner.type.token.Token)
     */
    public void superscriptMark(final Context context,
            final TokenSource source, final Typesetter typesetter, final Token t)
            throws TypesetterException {

        listMaker.superscriptMark(context, source, typesetter, t);
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#tab(
     *      Context,
     *      TokenSource, de.dante.extex.scanner.type.token.Token)
     */
    public void tab(final Context context, final TokenSource source,
            final Token t) throws TypesetterException, ConfigurationException {

        listMaker.tab(context, source, t);
    }

}
