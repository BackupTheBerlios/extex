/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.scanner;


/**
 * This class provides a type-save enumeration of the category codes for
 * characters.
 * This is accomplished by the use of several static classes which are
 * derived from the common abstract superclass Catcode.
 * <p>
 * This class contains some inner classes representing the various incarnations
 * of a catcode.
 * Externally only some static constants for CATCODES are accessible.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.8 $
 */
public abstract class Catcode {

    /**
     * The constant <tt>ESCAPE</tt> contains the unique object representing the
     * escape catcode used to initiate control sequences.
     */
    public static final Catcode ESCAPE = new CatcodeEscape();

    /**
     * The constant <tt>LEFTBRACE</tt> contains the unique object representing
     * the left brace catcode.
     */
    public static final Catcode LEFTBRACE = new CatcodeLeftBrace();

    /**
     * The constant <tt>RIGHTBRACE</tt> contains the unique object representing
     * the right brace catcode.
     */
    public static final Catcode RIGHTBRACE = new CatcodeRigthBrace();

    /**
     * The constant <tt>MATHSHIFT</tt> contains the unique object representing
     * the math shift catcode.
     */
    public static final Catcode MATHSHIFT = new CatcodeMathShift();

    /**
     * The constant <tt>TABMARK</tt> contains the unique object representing
     * the tab mark catcode.
     */
    public static final Catcode TABMARK = new CatcodeTabMark();

    /**
     * The constant <tt>CR</tt> contains the unique object representing the
     * cr catcode.
     */
    public static final Catcode CR = new CatcodeCr();

    /**
     * The constant <tt>MACROPARAM</tt> contains the unique object representing
     * the macro param catcode.
     */
    public static final Catcode MACROPARAM = new CatcodeMacroParam();

    /**
     * The constant <tt>SUPMARK</tt> contains the unique object representing
     * the super mark catcode.
     */
    public static final Catcode SUPMARK = new CatcodeSupMark();

    /**
     * The constant <tt>SUBMARK</tt> contains the unique object representing
     * the sub mark catcode.
     */
    public static final Catcode SUBMARK = new CatcodeSubMark();

    /**
     * The constant <tt>IGNORE</tt> contains the unique object representing
     * the ignore catcode.
     */
    public static final Catcode IGNORE = new CatcodeIgnore();

    /**
     * The constant <tt>SPACE</tt> contains the unique object representing
     * the space catcode.
     */
    public static final Catcode SPACE = new CatcodeSpace();

    /**
     * The constant <tt>LETTER</tt> contains the unique object representing
     * the letter catcode.
     */
    public static final Catcode LETTER = new CatcodeLetter();

    /**
     * The constant <tt>OTHER</tt> contains the unique object representing
     * the other catcode.
     */
    public static final Catcode OTHER = new CatcodeOther();

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
     * The constant <tt>INVALID</tt> contains the unique object representing
     * the invalid catcode.
     */
    public static final Catcode INVALID = new CatcodeInvalid();

    /** This is an array of CATCODES where the integer catcode can be used as
     *  index.
     */
    private static final Catcode[] CATCODES = {ESCAPE,
                                                  LEFTBRACE,
                                                  RIGHTBRACE,
                                                  MATHSHIFT,
                                                  TABMARK,
                                                  CR,
                                                  MACROPARAM,
                                                  SUPMARK,
                                                  SUBMARK,
                                                  IGNORE,
                                                  SPACE,
                                                  LETTER,
                                                  OTHER,
                                                  ACTIVE,
                                                  COMMENT,
                                                  INVALID
                                              };

    /**
     * The field <tt>name</tt> contains the string representation of the
     * catcode for debugging.
     */
    private String name;

    /**
     * Creates a new object.
     * This constructor is private since only the static instances defined
     * above are allowed.
     *
     * @param theName the string representation
     */
    private Catcode(final String theName) {
        super();
        this.name = theName;
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
    public static Catcode toCatcode(final int theCode)
            throws CatcodeException {

        if (theCode < 0 || theCode >= CATCODES.length) {
            throw new CatcodeException(Integer.toString(theCode));
        }

        return CATCODES[theCode];
    }

    /**
     * Getter for the maximal numerical catcode.
     *
     * @return the maximal alloed category code
     */
    public static int getCatcodeMax() {

        return CATCODES.length - 1;
    }

    /**
     * Get the numerical representation for the Catcode.
     * Beware: this method should be used only as a last resort.
     * To discourage its use it is implemented rather inefficiently.
     *
     * @return the catcode as integer
     */
    public int getCode() {

        for (int i = 0; i < CATCODES.length; i++) {
            if (CATCODES[i] == this) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Getter for the name of the catcode.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the string representation of the catcode.
     *
     * @return the string representation
     */
    public String toString() {
        return name;
    }

    /**
     * Catcodes support the visitor pattern. This method is the entry for
     * visiting CATCODES.
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

    /**
     * This inner class represents an active catcode.
     */
    private static final class CatcodeActive extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeActive() {
            super("active");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeComment() {
            super("comment");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeCr() {
            super("cr");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeEscape() {
            super("escape");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeIgnore() {
            super("ignore");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeInvalid() {
            super("invalid");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeLeftBrace() {
            super("leftbrace");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeLetter() {
            super("letter");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeMacroParam() {
            super("macroparam");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeMathShift() {
            super("mathshift");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeOther() {
            super("other");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeRigthBrace() {
            super("rightbrace");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeSpace() {
            super("space");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeSubMark() {
            super("submark");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
         * Creates a new object.
         */
        private CatcodeSupMark() {
            super("supmark");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
            final Object arg2, final Object arg3)  throws Exception {

            return visitor.visitSupMark(arg1, arg2, arg3);
        }
    }

    /**
     * This inner class represents a tab mark character catcode.
     */
    private static final class CatcodeTabMark extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeTabMark() {
            super("tabmark");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting CATCODES.
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
            final Object arg2, final Object arg3)  throws Exception {

            return visitor.visitTabMark(arg1, arg2, arg3);
        }
    }
}
