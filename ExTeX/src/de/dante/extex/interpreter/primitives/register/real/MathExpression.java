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

package de.dante.extex.interpreter.primitives.register.real;

import gnu.jel.CompilationException;
import gnu.jel.CompiledExpression;
import gnu.jel.Evaluator;
import gnu.jel.Library;
import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.CountConvertible;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.RealConvertible;
import de.dante.extex.interpreter.Theable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Real;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.typesetter.Typesetter;
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
 * @version $Revision: 1.1 $
 */
public class MathExpression extends AbstractCode
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
     * Calculate the math expression and
     * put the solotion on the source-stack.
     *
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Real real = calculate(source);
        source.push(new Tokens(context, real.toString()));
    }

    /**
     * Calculate the math expression.
     *
     * @param source    the tokensource
     * @return  the real-value
     * @throws GeneralException if a error occoured
     */
    private Real calculate(final TokenSource source) throws GeneralException {

        Real real = new Real(0);

        // \mathexpr{7+5+3}
        String expr = source.scanTokensAsString();

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

            throw new GeneralHelpingException("TTP.MathExpr", ce.getMessage()
                    + " (at column " + String.valueOf(col) + ")", expr, buf
                    .toString());
        }

        if (compileexpr != null) {

            Object result = null;
            try {
                result = compileexpr.evaluate(null);
            } catch (Throwable e) {
                throw new GeneralHelpingException("TTP.MathExprError", e
                        .getMessage());
            }

            System.err.println("\nresult = " + result);
            if (result != null) {
                real = new Real(Double.parseDouble(result.toString()));
            }
        }

        return real;
    }

    /**
     * @see de.dante.extex.interpreter.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Tokens the(final Context context, final TokenSource source)
            throws GeneralException {

        Real real = calculate(source);
        return new Tokens(context, real.toString());
    }

    /**
     * @see de.dante.extex.interpreter.RealConvertible#convertReal(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Real convertReal(final Context context, final TokenSource source)
            throws GeneralException {

        return calculate(source);
    }

    /**
     * @see de.dante.extex.interpreter.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public long convertCount(final Context context, final TokenSource source)
            throws GeneralException {

        return calculate(source).getLong();
    }
}