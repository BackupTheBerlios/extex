/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.real;

import gnu.jel.CompilationException;
import gnu.jel.CompiledExpression;
import gnu.jel.Evaluator;
import gnu.jel.Library;
import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.real.Real;
import de.dante.extex.interpreter.type.real.RealConvertible;
import de.dante.util.GeneralException;

/**
 * Math. Expressions to get a real-value.
 *
 * <p>Example</p>
 * <pre>
 * \mathexpr{2*7}
 * \real7=\mathexpr{7+4-2*3}
 * \count99=\mathexpr{7+4-2*3}
 * </pre>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public class MathExpression extends AbstractMath
        implements
            Theable,
            RealConvertible,
            CountConvertible {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     * @throws GeneralException ...
     */
    public MathExpression(final String name) throws GeneralException {

        super(name);

        // use java.lang.Math
        Class[] staticLib = new Class[1];
        try {
            staticLib[0] = Class.forName("java.lang.Math");
        } catch (ClassNotFoundException e) {
            throw new GeneralException(e.getMessage());
        }

        // init library
        lib = new Library(staticLib, null, null, null, null);
        try {
            lib.markStateDependent("random", null);
        } catch (CompilationException e) {
            throw new GeneralException(e.getMessage());
        }
    }

    /**
     * Library
     */
    private Library lib = null;

    /**
     * Calculate
     * @param context   the context
     * @param source    the tokensource
     * @return  the real-value
     * @throws GeneralException if a error occoured
     */
    protected Real calculate(final Context context, final TokenSource source)
            throws GeneralException {

        Real real = new Real(0);

        // \mathexpr{7+5+3}
        String expr = source.scanTokensAsString(context);

        // compile
        CompiledExpression compileexpr = null;
        try {
            compileexpr = Evaluator.compile(expr, lib);
        } catch (CompilationException ce) {
            int col = ce.getColumn();
            StringBuffer buf = new StringBuffer();
            for (int i = 1; i < col; i++) {
                buf.append(' ');
            }
            buf.append('^');

            throw new HelpingException(getLocalizer(), "TTP.MathExpr", ce
                    .getMessage()
                    + " (at column " + String.valueOf(col) + ")", expr, buf
                    .toString());
        }

        if (compileexpr != null) {

            Object result = null;
            try {
                result = compileexpr.evaluate(null);
            } catch (Throwable e) {
                throw new HelpingException(getLocalizer(), "TTP.MathExprError",
                        e.getMessage());
            }

            System.err.println("\nresult = " + result);
            if (result != null) {
                real = new Real(Double.parseDouble(result.toString()));
            }
        }

        return real;
    }

}