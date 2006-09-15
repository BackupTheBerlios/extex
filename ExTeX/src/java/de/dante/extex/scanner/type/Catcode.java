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

package de.dante.extex.scanner.type;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * This class provides a type-save enumeration of the category codes for
 * characters.
 * This is accomplished by the use of several static classes which are
 * derived from the common abstract super class Catcode.
 * <p>
 * This class contains some inner classes representing the various incarnations
 * of a catcode.
 * Externally only some static constants for category codes are accessible.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public abstract class Catcode implements Serializable {

    /**
     * This inner class represents an active catcode.
     */
    private static final class CatcodeActive extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "active";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.ACTIVE;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For active characters the visitor method visitActive() is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case or an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitActive(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents a comment catcode.
     */
    private static final class CatcodeComment extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "comment";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.COMMENT;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For comment starting characters the visitor method visitComment()
         * is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitComment(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents a CR catcode.
     */
    private static final class CatcodeCr extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "cr";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.CR;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For CR characters the visitor method visitActive() is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitCr(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents an escape catcode.
     */
    private static final class CatcodeEscape extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "escape";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.ESCAPE;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For escape characters the visitor method visitEscape() is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitEscape(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents an ignore catcode.
     */
    private static final class CatcodeIgnore extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "ignore";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.IGNORE;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For ignored characters the visitor method visitIgnore() is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitIgnore(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents an invalid character catcode.
     */
    private static final class CatcodeInvalid extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "invalid";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.INVALID;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For invalid characters the visitor method visitInvalid() is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitInvalid(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents a left brace character.
     */
    private static final class CatcodeLeftBrace extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "leftbrace";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.LEFTBRACE;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For left brace characters the visitor method visitLeftBrace() is
         * invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitLeftBrace(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents a letter character catcode.
     */
    private static final class CatcodeLetter extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "letter";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.LETTER;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For letter characters the visitor method visitLetter() is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitLetter(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents a macro parameter character.
     */
    private static final class CatcodeMacroParam extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "macroparam";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.MACROPARAM;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For macro parameter characters the visitor method visitMacroParam()
         * is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitMacroParam(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents a math shift character token.
     */
    private static final class CatcodeMathShift extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "mathshift";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.MATHSHIFT;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For math shift characters the visitor method visitMathShift()
         * is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitMathShift(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents an other character catcode.
     */
    private static final class CatcodeOther extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "other";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.OTHER;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For other characters the visitor method visitOther() is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitOther(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents a right brace character catcode.
     */
    private static final class CatcodeRigthBrace extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "rightbrace";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.RIGHTBRACE;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For right brace characters the visitor method visitRightBrace()
         * is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitRightBrace(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents a space character catcode.
     */
    private static final class CatcodeSpace extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "space";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.SPACE;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For space characters the visitor method visitSpace() is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitSpace(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents a sub mark character catcode.
     */
    private static final class CatcodeSubMark extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "submark";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.SUBMARK;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For sub mark characters the visitor method visitSubMark() is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitSubMark(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents a sup mark character catcode.
     */
    private static final class CatcodeSupMark extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "supmark";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.SUPMARK;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For sup mark characters the visitor method visitSupMark() is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitSupMark(arg1, arg2, arg3);
        }

    }

    /**
     * This inner class represents a tab mark character catcode.
     */
    private static final class CatcodeTabMark extends Catcode {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for serialization.
         */
        protected static final long serialVersionUID = 2006L;

        /**
         * Getter for the name of the catcode.
         *
         * @return the name
         */
        public String getName() {

            return "tabmark";
        }

        /**
         * Return the singleton constant object after the serialized instance
         * has been read back in.
         *
         * @return the one and only instance of this object
         *
         * @throws ObjectStreamException never
         */
        protected Object readResolve() throws ObjectStreamException {

            return Catcode.TABMARK;
        }

        /**
         * Catcode supports the visitor pattern. This method is the entry for
         * visiting category codes.
         * For tab mark characters the visitor method visitTabMark() is invoked.
         *
         * @param visitor the visitor
         * @param arg1 the first argument to pass
         * @param arg2 the second argument to pass
         * @param arg3 the third argument to pass
         *
         * @return some return value form the visit
         *
         * @throws Exception in case of an error
         */
        public Object visit(final CatcodeVisitor visitor, final Object arg1,
                final Object arg2, final Object arg3) throws Exception {

            return visitor.visitTabMark(arg1, arg2, arg3);
        }

    }

    /**
     * The constant <tt>ACTIVE</tt> contains the unique object representing
     * the active catcode.
     */
    public static final Catcode ACTIVE = new CatcodeActive();

    /**
     * The constant <tt>COMMENT</tt> contains the unique object representing
     * the comment catcode. A comment is started with this catcode and lasts
     * to the end of the line.
     */
    public static final Catcode COMMENT = new CatcodeComment();

    /**
     * The constant <tt>CR</tt> contains the unique object representing the
     * cr catcode.
     */
    public static final Catcode CR = new CatcodeCr();

    /**
     * The constant <tt>ESCAPE</tt> contains the unique object representing the
     * escape catcode used to initiate control sequences.
     */
    public static final Catcode ESCAPE = new CatcodeEscape();

    /**
     * The constant <tt>IGNORE</tt> contains the unique object representing
     * the ignore catcode.
     */
    public static final Catcode IGNORE = new CatcodeIgnore();

    /**
     * The constant <tt>INVALID</tt> contains the unique object representing
     * the invalid catcode.
     */
    public static final Catcode INVALID = new CatcodeInvalid();

    /**
     * The constant <tt>LEFTBRACE</tt> contains the unique object representing
     * the left brace catcode.
     */
    public static final Catcode LEFTBRACE = new CatcodeLeftBrace();

    /**
     * The constant <tt>LETTER</tt> contains the unique object representing
     * the letter catcode.
     */
    public static final Catcode LETTER = new CatcodeLetter();

    /**
     * The constant <tt>MACROPARAM</tt> contains the unique object representing
     * the macro param catcode.
     */
    public static final Catcode MACROPARAM = new CatcodeMacroParam();

    /**
     * The constant <tt>MATHSHIFT</tt> contains the unique object representing
     * the math shift catcode.
     */
    public static final Catcode MATHSHIFT = new CatcodeMathShift();

    /**
     * The constant <tt>OTHER</tt> contains the unique object representing
     * the other catcode.
     */
    public static final Catcode OTHER = new CatcodeOther();

    /**
     * The constant <tt>RIGHTBRACE</tt> contains the unique object representing
     * the right brace catcode.
     */
    public static final Catcode RIGHTBRACE = new CatcodeRigthBrace();

    /**
     * The constant <tt>SPACE</tt> contains the unique object representing
     * the space catcode.
     */
    public static final Catcode SPACE = new CatcodeSpace();

    /**
     * The constant <tt>SUBMARK</tt> contains the unique object representing
     * the sub mark catcode.
     */
    public static final Catcode SUBMARK = new CatcodeSubMark();

    /**
     * The constant <tt>SUPMARK</tt> contains the unique object representing
     * the super mark catcode.
     */
    public static final Catcode SUPMARK = new CatcodeSupMark();

    /**
     * The constant <tt>TABMARK</tt> contains the unique object representing
     * the tab mark catcode.
     */
    public static final Catcode TABMARK = new CatcodeTabMark();

    /**
     * The field <tt>CATCODES</tt> contains an array of category codes where the
     * integer catcode can be used as index.
     */
    private static final Catcode[] CATCODES = {ESCAPE, LEFTBRACE, RIGHTBRACE,
            MATHSHIFT, TABMARK, CR, MACROPARAM, SUPMARK, SUBMARK, IGNORE,
            SPACE, LETTER, OTHER, ACTIVE, COMMENT, INVALID};

    /**
     * Getter for the maximal numerical catcode.
     *
     * @return the maximal allowed category code
     */
    public static int getCatcodeMax() {

        return CATCODES.length - 1;
    }

    /**
     * Get the numerical representation for the Catcode.
     *
     * @param cc the catcode to map
     *
     * @return the catcode as integer
     */
    public static int getCode(final Catcode cc) {

        for (int i = 0; i < CATCODES.length; i++) {
            if (CATCODES[i] == cc) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the numerical representation for the Catcode.
     *
     * @return the catcode as integer
     */
    public int getCode() {

        return getCode(this);
    }

    /**
     * Return a catcode for a given numerical value.
     *
     * @param theCode the numerical code.
     *
     * @return the catcode
     *
     * @throws CatcodeException in case that the catcode is not in the
     *     allowed range
     */
    public static Catcode toCatcode(final int theCode) throws CatcodeException {

        if (theCode < 0 || theCode >= CATCODES.length) {
            throw new CatcodeException(Integer.toString(theCode));
        }

        return CATCODES[theCode];
    }

    /**
     * Creates a new object.
     * This constructor is private since only the static instances defined
     * above are allowed.
     */
    private Catcode() {

    }

    /**
     * Getter for the name of the catcode.
     *
     * @return the name
     */
    public abstract String getName();

    /**
     * Get the string representation of the catcode.
     *
     * @return the string representation
     */
    public String toString() {

        return getName();
    }

    /**
     * Catcode support the visitor pattern. This method is the entry for
     * visiting CATCODE.
     *
     * @param visitor the visitor
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some return value form the visit
     *
     * @throws Exception in case of an error
     */
    public abstract Object visit(final CatcodeVisitor visitor,
            final Object arg1, final Object arg2, final Object arg3)
            throws Exception;

}
