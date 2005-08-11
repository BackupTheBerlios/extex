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

package de.dante.extex.interpreter.type.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.ActiveCharacterToken;
import de.dante.extex.scanner.type.token.ControlSequenceToken;
import de.dante.extex.scanner.type.token.CrToken;
import de.dante.extex.scanner.type.token.LeftBraceToken;
import de.dante.extex.scanner.type.token.LetterToken;
import de.dante.extex.scanner.type.token.MacroParamToken;
import de.dante.extex.scanner.type.token.MathShiftToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.RightBraceToken;
import de.dante.extex.scanner.type.token.SpaceToken;
import de.dante.extex.scanner.type.token.SubMarkToken;
import de.dante.extex.scanner.type.token.SupMarkToken;
import de.dante.extex.scanner.type.token.TabMarkToken;
import de.dante.extex.scanner.type.token.TokenVisitor;

/**
 * This class holds an output file onto which tokens can be wrtitten.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.12 $
 */
public class OutFile implements Serializable {

    /**
     * This anonymous inner class is used with the visitor pattern to map the
     * tokens to appropriate print strings.
     */
    private static final TokenVisitor VISITOR = new TokenVisitor() {

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitActive(
         *      de.dante.extex.scanner.type.ActiveCharacterToken,
         *      java.lang.Object)
         */
        public Object visitActive(final ActiveCharacterToken token,
                final Object w) throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitCr(
         *      de.dante.extex.scanner.type.CrToken, java.lang.Object)
         */
        public Object visitCr(final CrToken token, final Object w)
                throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitEscape(
         *      de.dante.extex.scanner.type.ControlSequenceToken,
         *      java.lang.Object)
         */
        public Object visitEscape(final ControlSequenceToken token,
                final Object w) throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            ((Writer) w).write(token.getName());
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitLeftBrace(
         *      de.dante.extex.scanner.type.LeftBraceToken,
         *      java.lang.Object)
         */
        public Object visitLeftBrace(final LeftBraceToken token,
                final Object w) throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitLetter(
         *      de.dante.extex.scanner.type.LetterToken,
         *      java.lang.Object)
         */
        public Object visitLetter(final LetterToken token, final Object w)
                throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitMacroParam(
         *      de.dante.extex.scanner.type.MacroParamToken,
         *      java.lang.Object)
         */
        public Object visitMacroParam(final MacroParamToken token,
                final Object w) throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitMathShift(
         *      de.dante.extex.scanner.type.MathShiftToken,
         *      java.lang.Object)
         */
        public Object visitMathShift(final MathShiftToken token,
                final Object w) throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitOther(
         *      de.dante.extex.scanner.type.OtherToken,
         *      java.lang.Object)
         */
        public Object visitOther(final OtherToken token, final Object w)
                throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitRightBrace(
         *      de.dante.extex.scanner.type.RightBraceToken,
         *      java.lang.Object)
         */
        public Object visitRightBrace(final RightBraceToken token,
                final Object w) throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitSpace(
         *      de.dante.extex.scanner.type.SpaceToken,
         *      java.lang.Object)
         */
        public Object visitSpace(final SpaceToken token, final Object w)
                throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitSubMark(
         *      de.dante.extex.scanner.type.SubMarkToken,
         *      java.lang.Object)
         */
        public Object visitSubMark(final SubMarkToken token, final Object w)
                throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitSupMark(
         *      de.dante.extex.scanner.type.SupMarkToken,
         *      java.lang.Object)
         */
        public Object visitSupMark(final SupMarkToken token, final Object w)
                throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitTabMark(
         *      de.dante.extex.scanner.type.TabMarkToken,
         *      java.lang.Object)
         */
        public Object visitTabMark(final TabMarkToken token, final Object w)
                throws Exception {

            ((Writer) w).write(token.getChar().getCodePoint());
            return null;
        }
    };

    /**
     * The field <tt>file</tt> contains the file assigned to this instance.
     * If the value is <code>null</code> then it can never be opened.
     */
    private File file;

    /**
     * The field <tt>writer</tt> contains the real writer assigned to this
     * instance.
     */
    private transient Writer writer = null;

    /**
     * Creates a new object.
     *
     * @param name the file to write to
     */
    public OutFile(final File name) {

        super();
        this.file = name;
    }

    /**
     * Close the current file.
     *
     * @throws IOException in case of an error
     */
    public void close() throws IOException {

        if (writer != null) {
            try {
                writer.close();
            } finally {
                writer = null;
            }
        }
    }

    /**
     * Check whether the output file is open.
     *
     * @return <code>true</code> iff the instance is open
     */
    public boolean isOpen() {

        return (null != writer);
    }

    /**
     * Open the current file.
     */
    public void open() {

        if (file != null) {
            try {
                writer = new BufferedWriter(new FileWriter(file));
            } catch (FileNotFoundException e) {
                // ignored on purpose
            } catch (IOException e) {
                // ignored on purpose
            }
        }
    }

    /**
     * Write some tokens to the output writer.
     *
     * @param toks tokens to write
     *
     * @throws InterpreterException in case of an error
     * @throws IOException in case of an IO error
     */
    public void write(final Tokens toks)
            throws InterpreterException,
                IOException {

        if (writer == null) {
            return;
        }
        int len = toks.length();

        for (int i = 0; i < len; i++) {
            try {

                toks.get(i).visit(VISITOR, writer);

            } catch (IOException e) {
                throw e;
            } catch (InterpreterException e) {
                throw e;
            } catch (Exception e) {
                throw new InterpreterException(e);
            }
        }
    }
}