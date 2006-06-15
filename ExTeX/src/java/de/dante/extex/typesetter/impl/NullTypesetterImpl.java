/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

import de.dante.extex.backend.BackendDriver;
import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
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
import de.dante.extex.typesetter.pageBuilder.PageBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.factory.NodeFactory;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * The dummy typesetter which does nothing but provide the appropriate
 * interface.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.23 $
 */
public class NullTypesetterImpl implements Typesetter {

    /**
     * The field <tt>backend</tt> contains the back-end driver for
     * producing the output.
     */
    private BackendDriver backend = null;

    /**
     * Creates a new object.
     */
    public NullTypesetterImpl() {

        super();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.interpreter.type.glue.FixedGlue)
     */
    public void add(final FixedGlue g) throws TypesetterException {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *     de.dante.extex.typesetter.type.Node)
     */
    public void add(final Node c)
            throws TypesetterException,
                ConfigurationException {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addAndAdjust(
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void addAndAdjust(final NodeList list,
            final TypesetterOptions options) throws TypesetterException {

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

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#afterParagraph(ParagraphObserver)
     */
    public void afterParagraph(final ParagraphObserver observer) {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#clearShipoutMark()
     */
    public void clearShipoutMark() {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#complete(TypesetterOptions)
     */
    public NodeList complete(final TypesetterOptions context)
            throws TypesetterException,
                ConfigurationException {

        return null;
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *     de.dante.util.framework.configuration.Configuration)
     */
    public void configure(final Configuration config) {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#cr(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public void cr(final Context context, final TypesettingContext tc,
            final UnicodeChar uc) throws TypesetterException {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#ensureHorizontalMode(
     *      de.dante.util.Locator)
     */
    public ListMaker ensureHorizontalMode(Locator locator) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#finish()
     */
    public void finish() throws ConfigurationException {

        // nothing to do
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

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getListMaker()
     */
    public ListMaker getListMaker() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getLocator()
     */
    public Locator getLocator() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getManager()
     */
    public ListManager getManager() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getMode()
     */
    public Mode getMode() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getNodeFactory()
     */
    public NodeFactory getNodeFactory() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getPrevDepth()
     */
    public FixedDimen getPrevDepth() throws TypesetterUnsupportedException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getSpacefactor()
     */
    public long getSpacefactor() throws TypesetterUnsupportedException {

        return 0;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#isShipoutMark()
     */
    public boolean isShipoutMark() {

        return false;
    }

    /**
     * Notification method to deal the case that a left brace hs been
     * encountered.
     *
     * @see de.dante.extex.typesetter.ListMaker#leftBrace()
     */
    public void leftBrace() {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#letter(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.Locator)
     */
    public void letter(final Context context, final TypesettingContext tc,
            final UnicodeChar uc, final Locator locator)
            throws TypesetterException {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#mathShift(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void mathShift(final Context context, final TokenSource source,
            final Token t) throws TypesetterException, ConfigurationException {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#par()
     */
    public void par() throws TypesetterException, ConfigurationException {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#push(
     *      de.dante.extex.typesetter.ListMaker)
     */
    public void push(final ListMaker listMaker) throws TypesetterException {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#removeLastNode()
     */
    public void removeLastNode() {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#rightBrace()
     */
    public void rightBrace() throws TypesetterException {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setBackend(
     *      de.dante.extex.backend.BackendDriver)
     */
    public void setBackend(final BackendDriver driver) {

        backend = driver;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setNodeFactory(
     *      de.dante.extex.typesetter.type.node.factory.NodeFactory)
     */
    public void setNodeFactory(final NodeFactory nodeFactory) {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setOptions(
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void setOptions(final TypesetterOptions options) {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setOutputRoutine(
     *      de.dante.extex.typesetter.OutputRoutine)
     */
    public void setOutputRoutine(final OutputRoutine output) {

    }

    /**
     * Setter for the page builder.
     * Since the page builder is not needed this is a noop.
     *
     * @param pageBuilder the new page builder
     *
     * @see de.dante.extex.typesetter.Typesetter#setPageBuilder(
     *       de.dante.extex.typesetter.pageBuilder.PageBuilder)
     */
    public void setPageBuilder(final PageBuilder pageBuilder) {

    }

    /**
     * Setter for the paragraph builder.
     * Since the paragraph builder is not needed this is a noop.
     *
     * @param paragraphBuilder the new paragraph builder
     *
     * @see de.dante.extex.typesetter.Typesetter#setParagraphBuilder(
     *       de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder)
     */
    public void setParagraphBuilder(final ParagraphBuilder paragraphBuilder) {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(
     *     de.dante.extex.interpreter.type.dimen.FixedDimen)
     */
    public void setPrevDepth(final FixedDimen pd) {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(
     *     de.dante.extex.interpreter.type.count.FixedCount)
     */
    public void setSpacefactor(final FixedCount f)
            throws InvalidSpacefactorException {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#shipout(
     *     de.dante.extex.typesetter.type.NodeList)
     */
    public void shipout(final NodeList nodes) {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#showlist(
     *      java.lang.StringBuffer, long, long)
     */
    public void showlist(final StringBuffer sb, final long l, final long m) {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#showlists(
     *      java.lang.StringBuffer,
     *      long,
     *      long)
     */
    public void showlists(final StringBuffer sb, final long l, final long m) {

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

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#superscriptMark(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void superscriptMark(final Context context,
            final TokenSource source, final Typesetter typesetter, final Token t)
            throws TypesetterException {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#tab(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void tab(final Context context, final TokenSource source,
            final Token t) throws TypesetterException, ConfigurationException {

    }

}
