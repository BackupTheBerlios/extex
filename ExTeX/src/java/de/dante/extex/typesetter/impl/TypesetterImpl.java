/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.ligatureBuilder.LigatureBuilder;
import de.dante.extex.typesetter.listMaker.ListManager;
import de.dante.extex.typesetter.listMaker.VerticalListMaker;
import de.dante.extex.typesetter.pageBuilder.PageBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeIterator;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.CharNodeFactory;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.InsertionNode;
import de.dante.extex.typesetter.type.node.PenaltyNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This is a reference implementation of the
 * {@link de.dante.extex.typesetter.Typesetter Typesetter} interface.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.60 $
 */
public class TypesetterImpl
        implements
            Typesetter,
            ListManager,
            Localizable,
            LogEnabled {

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
     * The field <tt>ligatureBuilder</tt> contains the ligature builder to use.
     */
    private LigatureBuilder ligatureBuilder;

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
     * The field <tt>options</tt> contains the context for accessing parameters.
     */
    private TypesetterOptions options;

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
     * To make it fully functionalit is required that the paragraph builder
     * and the ligature builder are provided before they are used.
     */
    public TypesetterImpl() {

        super();

        listMaker = new VerticalListMaker(this);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *     de.dante.extex.typesetter.Node)
     */
    public void add(final Node node) throws GeneralException {

        listMaker.add(node);

        if (saveStack == null
                && (node instanceof PenaltyNode
                        || node instanceof InsertionNode
                        || node instanceof HorizontalListNode //
                || node instanceof VerticalListNode)) {

            pageBuilder.inspectAndBuild((VerticalListNode) listMaker
                    .complete(options));
        }
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(
     *     de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addGlue(final Glue glue) throws GeneralException {

        listMaker.addGlue(glue);
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
     * @see de.dante.extex.typesetter.Typesetter#clearShipoutMark()
     */
    public void clearShipoutMark() {

        shipoutMark = false;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#complete(TypesetterOptions)
     */
    public NodeList complete(final TypesetterOptions context)
            throws InterpreterException {

        NodeList nodes = listMaker.complete(context);
        pop();
        return nodes;
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
        if (ligatureBuilder instanceof LogEnabled) {
            ((LogEnabled) ligatureBuilder).enableLogging(theLogger);
        }
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.ListManager#endParagraph()
     */
    public void endParagraph() throws GeneralException {

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
    public void finish() throws GeneralException {

        par();
        pageBuilder.flush(listMaker.complete(options));
        if (saveStack != null) {
            //TODO gene: test that nothing is left behind
        }
        pageBuilder.close();
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getCharNodeFactory()
     */
    public CharNodeFactory getCharNodeFactory() {

        return charNodeFactory;
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.ListManager#getDocumentWriter()
     */
    public DocumentWriter getDocumentWriter() {

        return documentWriter;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getLastNode()
     */
    public Node getLastNode() {

        return listMaker.getLastNode();
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.ListManager#getLigatureBuilder()
     */
    public LigatureBuilder getLigatureBuilder() {

        return ligatureBuilder;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getListMaker()
     */
    public ListMaker getListMaker() {

        return listMaker;
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
     * @see de.dante.extex.typesetter.listMaker.ListManager#getOptions()
     */
    public TypesetterOptions getOptions() {

        return options;
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.ListManager#getParagraphBuilder()
     */
    public ParagraphBuilder getParagraphBuilder() {

        return paragraphBuilder;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#isShipoutMark()
     */
    public boolean isShipoutMark() {

        return shipoutMark;
    }

    /**
     * Notification method to deal the case that a left brace hs been
     * encountered.
     */
    public void leftBrace() {

        listMaker.leftBrace();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#letter(
     *      Context,
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public void letter(final Context context, final TypesettingContext tc,
            final UnicodeChar uc) throws GeneralException {

        listMaker.letter(context, tc, uc);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#mathShift(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.Token)
     */
    public void mathShift(final Context context, final TokenSource source,
            final Token t) throws GeneralException {

        listMaker.mathShift(context, source, t);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#par()
     */
    public void par() throws GeneralException {

        listMaker.par();

        if (saveStack.size() == 0) {
            pageBuilder.inspectAndBuild((VerticalListNode) listMaker
                    .complete(options));
        }
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.ListManager#pop()
     */
    public ListMaker pop() {

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
    public void push(final ListMaker list) {

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
    public void rightBrace() throws GeneralException {

        listMaker.rightBrace();
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setDocumentWriter(
     *     de.dante.extex.documentWriter.DocumentWriter)
     */
    public void setDocumentWriter(final DocumentWriter writer) {

        documentWriter = writer;
        pageBuilder.setDocumentWriter(writer);
    }

    /**
     * Setter for ligatureBuilder.
     *
     * @param theLigatureBuilder the ligatureBuilder to set.
     */
    public void setLigatureBuilder(final LigatureBuilder theLigatureBuilder) {

        ligatureBuilder = theLigatureBuilder;
        if (ligatureBuilder instanceof LogEnabled) {
            ((LogEnabled) ligatureBuilder).enableLogging(logger);
        }
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
     * Setter for the page builder.
     *
     * @param pageBuilder the new page builder
     *
     * @see de.dante.extex.typesetter.Typesetter#setPageBuilder(
     *      de.dante.extex.typesetter.pageBuilder.PageBuilder)
     */
    public void setPageBuilder(final PageBuilder pageBuilder) {

        this.pageBuilder = pageBuilder;
        pageBuilder.setDocumentWriter(documentWriter);
        if (pageBuilder instanceof LogEnabled) {
            ((LogEnabled) pageBuilder).enableLogging(logger);
        }
    }

    /**
     * Setter for paragraphBuilder.
     *
     * @param theParagraphBuilder the paragraphBuilder to set.
     */
    public void setParagraphBuilder(final ParagraphBuilder theParagraphBuilder) {

        paragraphBuilder = theParagraphBuilder;
        if (paragraphBuilder instanceof LogEnabled) {
            ((LogEnabled) paragraphBuilder).enableLogging(logger);
        }
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

        pageBuilder.flush(nodes);
        shipoutMark = true;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#subscriptMark(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.Token)
     */
    public void subscriptMark(final Context context, final TokenSource source,
            final Token t) throws GeneralException {

        listMaker.subscriptMark(context, source, t);
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#superscriptMark(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.Token)
     */
    public void superscriptMark(final Context context,
            final TokenSource source, final Token t) throws GeneralException {

        listMaker.superscriptMark(context, source, t);
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#tab(
     *      Context,
     *      TokenSource, de.dante.extex.scanner.Token)
     */
    public void tab(final Context context, final TokenSource source,
            final Token t) throws GeneralException {

        listMaker.tab(context, source, t);
    }
}