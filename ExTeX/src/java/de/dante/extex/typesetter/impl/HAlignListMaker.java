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

package de.dante.extex.typesetter.impl;

import java.util.ArrayList;
import java.util.List;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.primitives.table.Omit;
import de.dante.extex.interpreter.primitives.table.PreambleItem;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.NodeList;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class HAlignListMaker extends RestrictedHorizontalListMaker
        implements
            AlignmentList {

    /**
     * ...
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.5 $
     */
    protected class Cell {

        /**
         * The field <tt>list</tt> contains the ...
         */
        NodeList list;

        /**
         * The field <tt>span</tt> contains the ...
         */
        boolean span;

        /**
         * Creates a new object.
         *
         */
        public Cell() {

            super();
            // TODO unimplemented
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
     * The field <tt>span</tt> contains the number cells currently spanned.
     */
    private int span;

    /**
     * The field <tt>line</tt> contains the cells of the current line.
     * A cell might be null if it is joined with the previous cell.
     */
    private NodeList[] line;

    /**
     * The field <tt>preamble</tt> contains the preamble for this halign.
     */
    private List preamble;

    /**
     * The field <tt>rows</tt> contains the rows of this alignment.
     */
    private List rows = new ArrayList();

    /**
     * The field <tt>width</tt> contains the target width or <code>null</code>
     * to indicate that the natural width sould be used.
     */
    private Dimen width;

    /**
     * Creates a new object.
     * @param manager the manager
     * @param context TODO
     * @param source TODO
     * @param thePreamble the list of preamble items
     * @param theWidth the target width or <code>null</code> if the natural width
     *  should be used
     *
     * @throws GeneralException in case of an error
     */
    public HAlignListMaker(final Manager manager, final Context context,
            final TokenSource source, final List thePreamble,
            final Dimen theWidth) throws GeneralException {

        super(manager);
        preamble = thePreamble;
        width = theWidth;
        clearLine(context, source);
    }

    /**
     * Clear all entries of the current line.
     *
     * @param context TODO
     * @param source TODO
     *
     * @throws GeneralException in case of an error
     */
    private void clearLine(final Context context, final TokenSource source)
            throws GeneralException {

        line = new NodeList[preamble.size()];
        col = 0;

        startCell(context, source);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#close()
     */
    public NodeList close() throws GeneralException {

        Dimen[] wd = determineNaturalWidth();
        Dimen w = sum(wd);

        if (width != null) {
            //TODO
        }

        NodeList nl;
        for (int j = rows.size() - 1; j > 0; j--) {
            line = (NodeList[]) rows.get(j);
            for (int i = 0; i < line.length; i++) {
                nl = line[i];
                if (nl != null && i + 1 < line.length && line[i + 1] != null) {
                    nl.spread(wd[i], wd[i]); //TODO check
                }
            }
        }

        // TODO unimplemented
        throw new RuntimeException("unimplemented");
        //return null;
    }

    /**
     * ...
     *
     * @param d ...
     *
     * @return ...
     */
    private Dimen sum(final Dimen[] d) {

        Dimen sum = new Dimen();

        for (int i = 0; i < line.length; i++) {
            sum.add(d[i]);
        }
        return sum;
    }

    /**
     * ...
     *
     * @return
     */
    private Dimen[] determineNaturalWidth() {

        Dimen[] wd = new Dimen[line.length];
        NodeList nl;

        for (int i = 0; i < line.length; i++) {
            wd[i] = new Dimen(0);
        }

        for (int j = rows.size() - 1; j > 0; j--) {
            line = (NodeList[]) rows.get(j);
            for (int i = 0; i < line.length; i++) {
                nl = line[i];
                if (nl != null && i + 1 < line.length && line[i + 1] != null) {
                    wd[i].max(nl.getWidth());
                }
            }
        }
        return wd;
    }

    /**
     * @see de.dante.extex.typesetter.impl.AlignmentList#cr(Context, TokenSource)
     */
    public void cr(final Context context, final TokenSource source)
            throws GeneralException {

        rows.add(line);
        clearLine(context, source);
    }

    /**
     * @see de.dante.extex.typesetter.impl.AlignmentList#crcr(Context, TokenSource)
     */
    public void crcr(final Context context, final TokenSource source)
            throws GeneralException {

        if (col > 0) {
            cr(context, source);
        }
    }

    /**
     * @see de.dante.extex.typesetter.impl.AlignmentList#omit()
     */
    public void omit() throws GeneralException {

    }

    /**
     * @see de.dante.extex.typesetter.impl.AlignmentList#span()
     */
    public void span() throws GeneralException {

        if (col + span > line.length) {
            new HelpingException(getLocalizer(), "TTP.ExtraAlignTab", "???");
        }
        span++;
    }

    /**
     * ...
     *
     * @param context TODO
     * @param source TODO
     *
     * @throws GeneralException in case of an error
     */
    private void startCell(final Context context, final TokenSource source)
            throws GeneralException {

        format = (PreambleItem) preamble.get(col);
        span = 1;

        Token t = source.scanNonSpace();
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
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#tab(
     *      Context,
     *      TokenSource, de.dante.extex.scanner.Token)
     */
    public void tab(final Context context, final TokenSource source,
            final Token token) throws GeneralException {

        if (col + span > line.length) {
            new HelpingException(getLocalizer(), "TTP.ExtraAlignTab", "???");
        }

        source.push(format.getPost()); //TODO wrong
        line[col] = super.close();
        setNodes(new HorizontalListNode());
        col += span;
        startCell(context, source);
    }
}