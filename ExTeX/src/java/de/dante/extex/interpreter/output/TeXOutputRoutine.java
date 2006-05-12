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
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.15 $
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
