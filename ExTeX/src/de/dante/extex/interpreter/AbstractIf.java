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
package de.dante.extex.interpreter;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.context.*;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This is the abstract base class for all ifs.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public abstract class AbstractIf extends AbstractCode {

    /**
     * Creates a new object.
     * 
     * @param name the name for debugging
     */
    public AbstractIf(String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#isIf()
     */
    public boolean isIf() {
        return true;
    }

    /**
     * @see de.dante.extex.interpreter.Code#expand(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource, de.dante.extex.typesetter.Typesetter)
     */
    public void expand(Flags prefix, Context context, TokenSource source, Typesetter typesetter) throws GeneralException {
        if (conditional(context, source, typesetter)) {
            context.ifPush(source.getLocator(),true);
        } else {
            if (skipToElseOrFi(context, source)) {
                context.ifPush(source.getLocator(),true);
            }
        }

        prefix.clear();
    }

    /**
     * This method computes the boolean value of the conditional.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return the boolean value
     */
    protected abstract boolean conditional(Context context,
                                           TokenSource source, Typesetter typesetter)
                                    throws GeneralException;

    /**
     * Skip to the next matching <tt>\fi</tt> or <tT>\else</tt> Token counting 
     * the intermediate <tt>\if</tt>s and <tt>\fi</tt>s. 
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return <code>true</code> if a matching \else has been found; otherwise
     * return <code>false</code> if a matching \fi has been found
     *
     * @throws CodeException in case of en error
     */
    protected boolean skipToElseOrFi(Context context, TokenSource source)
                              throws GeneralException {
        Code code;
        int n = 1;

        for (Token t = source.getNextToken(); t != null; t = source.getNextToken()) {
            if (t instanceof ControlSequenceToken) {
                String name = t.getValue();

                if (name.equals("fi")) {
                    if (--n < 1) {
                        return false;
                    }
                } else if (name.equals("else")) {
                    if (n < 1) {
                        return true;
                    }
                } else if ((code = context.getMacro(name)) != null &&
                               code.isIf()) {
                    n++;
                }
            }
        }

        throw new GeneralHelpingException("TTP.EOFinSkipped");
    }
}
