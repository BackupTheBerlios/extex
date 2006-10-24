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

package de.dante.extex.typesetter.listMaker;

import java.util.ArrayList;
import java.util.List;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.primitives.table.Noalign;
import de.dante.extex.interpreter.primitives.table.Omit;
import de.dante.extex.interpreter.primitives.table.util.PreambleItem;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides a list maker for horizontal alignments.
 *
 * @see "TTP [770]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.22 $
 */
public class HAlignListMaker extends RestrictedHorizontalListMaker
        implements
            AlignmentList {

    /**
     * This inner class is a container for the cell information in an alignment.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.22 $
     */
    protected class Cell {

        /**
         * The field <tt>list</tt> contains the nodes of this cell.
         */
        private NodeList list = null;

        /**
         * The field <tt>span</tt> contains the indicator that this cell should
         * be joined with the next cell when generating boxes.
         */
        private boolean span = false;

        /**
         * Creates a new object.
         *
         * @param nodes the nodes of this cell
         */
        public Cell(final NodeList nodes) {

            super();
            list = nodes;
        }

        /**
         * Getter for list.
         *
         * @return the list.
         */
        public NodeList getList() {

            return this.list;
        }

        /**
         * Getter for span.
         *
         * @return the span.
         */
        public boolean isSpan() {

            return this.span;
        }

        /**
         * Setter for span.
         */
        public void setSpan() {

            this.span = true;
        }
    }

    /**
     * The constant <tt>FIXED</tt> contains the default format consisting of
     * the pure contents only.
     */
    private static final PreambleItem FIXED = new PreambleItem(Tokens.EMPTY,
            Tokens.EMPTY);

    /**
     * The field <tt>col</tt> contains the indicator for the current column.
     */
    private int col;

    /**
     * The field <tt>format</tt> contains the format currently in effect.
     */
    private PreambleItem format;

    /**
     * The field <tt>line</tt> contains the cells of the current line.
     */
    private Cell[] line;

    /**
     * The field <tt>preamble</tt> contains the preamble for this halign.
     */
    private List preamble;

    /**
     * The field <tt>rows</tt> contains the rows of this alignment.
     */
    private List rows = new ArrayList();

    /**
     * The field <tt>spread</tt> contains the indicator that the width should
     * be interpreted relative to the natural width.
     */
    private boolean spread;

    /**
     * The field <tt>maxWidth</tt> contains the maximal width of each column.
     */
    private Dimen[] maxWidth;

    /**
     * The field <tt>width</tt> contains the target width or <code>null</code>
     * to indicate that the natural width should be used.
     */
    private FixedDimen width;

    /**
     * Creates a new object.
     *
     * @param manager the manager
     * @param context the interpreter context
     * @param source the token source
     * @param thePreamble the list of preamble items
     * @param theWidth the target width or <code>null</code> if the natural
     *  width should be used
     * @param theSpread indicator that the width should be interpreted relative
     *
     * @throws InterpreterException in case of an error
     */
    public HAlignListMaker(final ListManager manager, final Context context,
            final TokenSource source, final List thePreamble,
            final FixedDimen theWidth, final boolean theSpread)
            throws InterpreterException {

        super(manager, source.getLocator());
        preamble = thePreamble;
        width = theWidth;
        spread = theSpread;
        clearLine(context, source);

        maxWidth = new Dimen[line.length];

        for (int i = 0; i < line.length; i++) {
            maxWidth[i] = new Dimen(0);
        }
    }

    /**
     * Clear all entries of the current line.
     *
     * @param context the interpreter context
     * @param source the token source
     *
     * @throws TypesetterException in case of an error
     */
    private void clearLine(final Context context, final TokenSource source)
            throws TypesetterException {

        col = 0;
        line = new Cell[preamble.size()];
        startCell(context, source);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#complete(TypesetterOptions)
     */
    public NodeList complete(final TypesetterOptions context)
            throws TypesetterException,
                ConfigurationException {

        NodeList result = new VerticalListNode();
        NodeList nl;

        for (int j = 0; j < rows.size(); j++) {
            NodeList row = new HorizontalListNode();
            line = (Cell[]) rows.get(j);

            for (int i = 0; i < line.length; i++) {
                Cell cell = line[i];
                if (cell != null) {
                    nl = cell.getList();
                    if (nl instanceof HorizontalListNode) {
                        ((HorizontalListNode) nl).hpack(new Dimen(maxWidth[i])); //TODO gene: check
                    } else {
                        //TODO gene: unimplemented
                        throw new RuntimeException("unimplemented");
                    }
                    row.add(nl);
                }
            }
            result.add(row);
        }

        Dimen w = sum(maxWidth);

        if (width != null) {
            if (spread) {
                w.add(width);
            } else {
                w.set(width);
            }
        }

        result.setWidth(w);

        return result;
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.AlignmentList#cr(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public void cr(final Context context, final TokenSource source,
            final NodeList noalign) throws TypesetterException {

        rows.add(line);
        if (noalign != null) {
            //TODO gene: insert noalign
        }
        clearLine(context, source);
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.AlignmentList#crcr(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void crcr(final Context context, final TokenSource source,
            final Typesetter typesetter) throws TypesetterException {

        if (col <= 0) {
            return;
        }
        NodeList noalign = null;
        try {
            Token token = source.getToken(context);
            Code code;

            if (token instanceof CodeToken
                    && (code = context.getCode((CodeToken) token)) instanceof Noalign) {
                noalign = ((Noalign) code).exec(context, source, typesetter,
                        token);
            } else {
                source.push(token);
            }
        } catch (InterpreterException e) {
            throw new TypesetterException(e);
        }
        cr(context, source, noalign);
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.AlignmentList#omit()
     */
    public void omit() throws TypesetterException {

        //TODO gene: respect protected macros
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.AlignmentList#span(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void span(final Context context, final TokenSource source)
            throws TypesetterException {

        if (col >= line.length) {
            new HelpingException(getLocalizer(), "TTP.ExtraAlignTab", "???");
        }
        col++;
    }

    /**
     * Start a new cell.
     *
     * @param context the interpreter context
     * @param source the token source
     *
     * @throws TypesetterException in case of an error
     */
    private void startCell(final Context context, final TokenSource source)
            throws TypesetterException {

        format = (PreambleItem) preamble.get(col);

        try {
            Token t = source.scanNonSpace(context);
            if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code instanceof Omit) {
                    format = FIXED;
                } else {
                    source.push(t);
                }
            } else {
                source.push(t);
            }
            source.push(format.getPre());
        } catch (InterpreterException e) {
            throw new TypesetterException(e);
        }
    }

    /**
     * Compute the sum of an array of dimens.
     *
     * @param d the dimen array
     *
     * @return the sum in a new Dimen
     */
    public static Dimen sum(final Dimen[] d) {

        Dimen sum = new Dimen();

        for (int i = 0; i < d.length; i++) {
            sum.add(d[i]);
        }
        return sum;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#tab(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void tab(final Context context, final TokenSource source,
            final Token token)
            throws TypesetterException,
                ConfigurationException {

        if (col >= line.length) {
            new HelpingException(getLocalizer(), "TTP.ExtraAlignTab", token
                    .toString());
        }

        try {
            source.push(format.getPost()); //TODO gene: wrong! process the tokens before closing
        } catch (InterpreterException e) {
            throw new TypesetterException(e);
        }

        line[col] = new Cell(super.complete((TypesetterOptions) context));
        maxWidth[col].max(line[col].getList().getWidth());
        setNodes(new HorizontalListNode());
        col++;
        startCell(context, source);
    }

}
