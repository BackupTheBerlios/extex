/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.ligatureBuilder.LigatureBuilder;
import de.dante.extex.typesetter.listMaker.ListManager;
import de.dante.extex.typesetter.pageBuilder.PageBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;


/**
 * The dummy typesetter which does nothing but provide the appropriate
 * interface.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.26 $
 */
public class NullTypesetterImpl implements Typesetter {

    /**
     * Creates a new object.
     */
    public NullTypesetterImpl() {

        super();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void add(final Noad noad) throws GeneralException {

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
     * @see de.dante.extex.typesetter.ListMaker#addGlue(
     *      de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addGlue(final Glue g) {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace(
     *     de.dante.extex.interpreter.context.TypesettingContext,
     *     de.dante.extex.interpreter.type.count.Count)
     */
    public void addSpace(final TypesettingContext typesettingContext,
            final Count spacefactor) {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#close()
     */
    public NodeList close() {

        return null;
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *     de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration config) {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#finish()
     */
    public void finish() {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#getCharNodeFactory()
     */
    public CharNodeFactory getCharNodeFactory() {

        return null;
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
     * @see de.dante.extex.typesetter.ListMaker#mathShift(
     *      Context, TokenSource, de.dante.extex.scanner.Token)
     */
    public void mathShift(Context context, TokenSource source, Token t)
            throws GeneralException {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#par()
     */
    public void par() {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#push(
     *      de.dante.extex.typesetter.ListMaker)
     */
    public void push(final ListMaker listMaker) {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#removeLastNode()
     */
    public void removeLastNode() {

    }

    /**
     * @see de.dante.extex.typesetter.listMaker.NoadConsumer#scanNoads(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Noad scanNoads(final Context context, final TokenSource source)
            throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setDocumentWriter(
     *     de.dante.extex.documentWriter.DocumentWriter)
     */
    public void setDocumentWriter(final DocumentWriter doc) {

        // nothing to do
    }

    /**
     * Setter for the ligature builder.
     * Since the ligature builder is not needed this is a noop.
     *
     * @param ligatureBuilder the new ligature builder
     *
     * @see de.dante.extex.typesetter.Typesetter#setLigatureBuilder(
     *      de.dante.extex.typesetter.ligatureBuilder.LigatureBuilder)
     */
    public void setLigatureBuilder(final LigatureBuilder ligatureBuilder) {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#setOptions(
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void setOptions(final TypesetterOptions options) {

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
     *     de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setPrevDepth(final Dimen pd) {

        // nothing to do
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(
     *     de.dante.extex.interpreter.type.count.Count)
     */
    public void setSpacefactor(final Count f) {

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
     * @see de.dante.extex.typesetter.Typesetter#subscriptMark(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.scanner.Token)
     */
    public void subscriptMark(final TypesettingContext context, final Token t)
            throws GeneralException {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#superscriptMark(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.scanner.Token)
     */
    public void superscriptMark(final TypesettingContext context, final Token t)
            throws GeneralException {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#tab(
     *      Context,
     *      TokenSource, de.dante.extex.scanner.Token)
     */
    public void tab(final Context context, TokenSource source, final Token t)
            throws GeneralException {

    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#treatLetter(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.scanner.Token)
     */
    public void treatLetter(final TypesettingContext context, final Token t)
            throws GeneralException {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#treatLetter(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public void treatLetter(TypesettingContext context, UnicodeChar uc)
            throws GeneralException {

    }
}