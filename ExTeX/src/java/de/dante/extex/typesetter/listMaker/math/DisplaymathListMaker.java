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

package de.dante.extex.typesetter.listMaker.math;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.CantUseInException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.listMaker.ListManager;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.MathList;
import de.dante.extex.typesetter.type.noad.StyleNoad;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.Locator;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This is the list maker for the display math formulae.
 *
 * <doc name="everydisplayend" type="register">
 * <h3>The Tokens Parameter <tt>\everydisplayend</tt></h3>
 * <p>
 *  The tokens parameter <tt>\everydisplayend</tt> contains a list of tokens
 *  which is inserted at the end of display math. Those tokens take effect just
 *  before the math mode is ended but after any tokens given explicitly.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;everydisplayend&rang;
 *      &rarr; <tt>\everydisplayend</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *        &lang;tokens&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \everydisplayend={\,}  </pre>
 *
 * </doc>
 *
 * <doc name="everydisplay" type="register">
 * <h3>The Tokens Parameter <tt>\everydisplay</tt></h3>
 * <p>
 *  The tokens parameter <tt>\everydisplay</tt> contains a list of tokens which
 *  is inserted at the beginning of display math. Those tokens take effect after
 *  the math mode has been entered but before any tokens given explicitly.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;everydisplay&rang;
 *      &rarr; <tt>\everydisplay</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *        &lang;tokens&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \everydisplay={\,}  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.18 $
 */
public class DisplaymathListMaker extends MathListMaker implements EqConsumer {

    /**
     * The field <tt>eqno</tt> contains the math list for the equation number.
     * It is <code>null</code> if no equation number is set.
     */
    private MathList eqno = null;

    /**
     * The field <tt>leq</tt> contains the indicator for the side of the
     * equation number. A value of <code>true</code> indicates an equation
     * number on the left side.
     */
    private boolean leq = false;

    /**
     * Creates a new object.
     *
     * @param manager the manager to ask for global changes
     * @param locator the locator
     */
    public DisplaymathListMaker(final ListManager manager, final Locator locator) {

        super(manager, locator);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#complete(TypesetterOptions)
     */
    public NodeList complete(final TypesetterOptions context)
            throws TypesetterException,
                ConfigurationException {

        // see [TTP 1195]
        if (insufficientSymbolFonts(context)) {
            throw new TypesetterException(new HelpingException(getLocalizer(),
                    "TTP.InsufficientSymbolFonts"));
        }
        // see [TTP 1195]
        if (insufficientExtensionFonts(context)) {
            throw new TypesetterException(new HelpingException(getLocalizer(),
                    "TTP.InsufficientExtensionFonts"));
        }

        HorizontalListNode list = new HorizontalListNode();

        if (eqno != null && leq) {
            //TODO gene: leqno unimplemented
            throw new RuntimeException("unimplemented");
        }

        getNoads().typeset(null, null, 0, list,
                new MathContext(StyleNoad.DISPLAYSTYLE, context), getLogger());

        if (eqno != null && !leq) {
            //TODO gene: eqno unimplemented
            throw new RuntimeException("unimplemented");
        }

        // see [TTP 1200]
        getManager().setSpacefactor(Count.THOUSAND);
        //TODO gene: set space factor 1000 etc

        return list;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getMode()
     */
    public Mode getMode() {

        return Mode.DISPLAYMATH;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#mathShift(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void mathShift(final Context context, final TokenSource source,
            final Token t) throws TypesetterException, ConfigurationException {

        if (!isClosing()) {
            Tokens toks = context.getToks("everydisplayend");
            if (toks != null && toks.length() != 0) {
                try {
                    source.push(t);
                    source.push(toks);
                } catch (InterpreterException e) {
                    throw new TypesetterException(e);
                }
                setClosing(true);
                return;
            }
        }

        Token token;
        try {
            token = source.getToken(context);
            if (token == null) {
                throw new EofException("$$");
            } else if (!t.equals(token)) {
                // see [TTP 1197]
                throw new HelpingException(getLocalizer(), "TTP.DisplayMathEnd");
            }
        } catch (InterpreterException e) {
            throw new TypesetterException(e);
        }

        getManager().endParagraph();
    }

    /**
     * This method switches the collection of material to the target "equation
     * number".
     *
     * @param left the indicator on which side to produce the equation number.
     *  A value <code>true</code> indicates that the left side should be used.
     *
     * @throws CantUseInException in case that the eq number has already been
     *  started
     *
     * @see de.dante.extex.typesetter.listMaker.math.EqConsumer#switchToNumber(boolean)
     */
    public void switchToNumber(final boolean left) throws CantUseInException {

        if (eqno != null) {
            throw new CantUseInException(null, null);
        }

        leq = left;
        eqno = new MathList();
        setInsertionPoint(eqno);
    }

}
