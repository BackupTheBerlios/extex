/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.output;

import de.dante.extex.backend.BackendDriver;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.typesetter.OutputRoutine;
import de.dante.extex.typesetter.type.page.Page;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class provides a link between the typesetter and the interpreter for the
 * output routine. In <logo>TeX</logo> the output routine is implemented in
 * <logo>TeX</logo>'s macro language. In <logo>ExTeX</logo> the output routine
 * is a Java class implementing a defined interface. This class implements the
 * interface required and forwards the request for processing to the appropriate
 * interpreter.
 *
 *
 * <doc name="output" type="register">
 * <h3>The Tokens Register <tt>\output</tt></h3>
 * <p>
 *  The tokens register <tt>\output</tt> contains the program executed whenever
 *  a page is completed. If it is not defined then the built-in output routine
 *  will be used.
 * </p>
 * <p>
 *  The box register 255 is used to pass in the current vertical list. This list
 *  is assumed to make it to the page. For this purpose the primitive
 *  {@link de.dante.extex.interpreter.primitives.typesetter.output.Shipout \shipout}
 *  can be used.
 * </p>
 * <p>
 *  The output routine is assumed to clear the box register 255. If some
 *  material is left in this box register then this is considered an error.
 * </p>
 * <p>
 *  The output routine is assumed to invoke
 *  {@link de.dante.extex.interpreter.primitives.typesetter.output.Shipout \shipout}.
 *  This does not have to happen at each invocation of the output routine.
 *  The count register
 *  <tt>\maxdeadcycles</tt> determines how many invocations are allowed which
 *  do not call
 *  {@link de.dante.extex.interpreter.primitives.typesetter.output.Shipout \shipout}.
 *  The count register <tt>\deadcycles</tt>
 *  contains the number of dead cycles encountered already.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;output&rang;
 *      &rarr; <tt>\output</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *        &lang;tokens&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \output={}  </pre>
 *
 * </doc>
 *
 *
 * <doc name="deadcycles" type="register">
 * <h3>The Count Parameter <tt>\deadcycles</tt></h3>
 * <p>
 *  The count register <tt>\deadcycles</tt> contains the number of attempts to
 *  call the output routine without any material being shipped out. Usually the
 *  output routine is expected to ship something out. Under some circumstances
 *  the output is delayed. Thus a large number of dead cycles can indicate a
 *  problem in the output routine.
 *  The register <tt>\deadcycles</tt> is compared with the register
 *  <tt>\maxdeadcycles</tt> to decide when an intervention seem appropriate.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;deadcycles&rang;
 *      &rarr; <tt>\deadcycles</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *        &lang;number&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \deadcycles=0  </pre>
 *
 * </doc>
 *
 *
 * <doc name="maxdeadcycles" type="register">
 * <h3>The Count Parameter <tt>\maxdeadcycles</tt></h3>
 * <p>
 *  The count register <tt>\maxdeadcycles</tt> contains the maximum number
 *  of attempts to call the output routine without any material being shipped
 *  out. The output routine is expected to ship something out.
 *  Under some circumstances the output is delayed.
 *  Thus a large number of dead cycles can indicate a problem in the output
 *  routine.
 *  The register <tt>\deadcycles</tt> is compared with the register
 *  <tt>\maxdeadcycles</tt> to decide when an intervention seem appropriate.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;maxdeadcycles&rang;
 *      &rarr; <tt>\maxdeadcycles</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *        &lang;number&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \maxdeadcycles=1  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.17 $
 */
public class TeXOutputRoutine implements OutputRoutine {

    /**
     * The field <tt>OUTPUT_BOX</tt> contains the index of the box register to
     * communicate with the output routine.
     */
    private static final String OUTPUT_BOX = "255";

    /**
     * The field <tt>interpreter</tt> contains the interpreter.
     */
    private Interpreter interpreter;

    /**
     * The field <tt>rightBrace</tt> contains the group closing token.
     */
    private Token rightBrace;

    /**
     * Creates a new object.
     *
     * @param interpreter the interpreter
     *
     * @throws GeneralException in case of an error
     */
    public TeXOutputRoutine(final Interpreter interpreter)
            throws GeneralException {

        super();
        this.interpreter = interpreter;
        TokenFactory tokenFactory = interpreter.getContext().getTokenFactory();
        rightBrace = tokenFactory.createToken(Catcode.RIGHTBRACE, '}',
                Namespace.DEFAULT_NAMESPACE);
    }

    /**
     * @see de.dante.extex.typesetter.OutputRoutine#output(
     *      de.dante.extex.typesetter.type.page.Page,
     *      de.dante.extex.backend.BackendDriver)
     */
    public void output(final Page page, final BackendDriver backend)
            throws GeneralException {

        Context context = interpreter.getContext();
        Count deadcyles = context.getCount("deadcyles");

        Tokens output = context.getToks("output");
        if (output.length() == 0) {
            backend.shipout(page);
            deadcyles.set(0);
            return;
        }

        deadcyles.add(1);
        if (deadcyles.ge(context.getCount("maxdeadcycles"))) {
            throw new InterpreterException(LocalizerFactory.getLocalizer(
                    TeXOutputRoutine.class.getName()).format("TTP.TooMuchDead"));
        }

        Box box = context.getBox(OUTPUT_BOX);
        if (box != null && !box.isVoid()) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(TeXOutputRoutine.class.getName()),
                    "TTP.NonEmptyOutBox", context.esc("box"), OUTPUT_BOX);
        }

        context.setBox(OUTPUT_BOX, new Box(page.getNodes()), true);
        interpreter.push(rightBrace);
        interpreter.push(output);
        try {
            context.openGroup(8);
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        }
        interpreter.executeGroup();

        if (!box.isVoid()) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(TeXOutputRoutine.class.getName()),
                    "TTP.NonEmptyOutBoxAfter", context.esc("box"), OUTPUT_BOX);
        }
    }

}
