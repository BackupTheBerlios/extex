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
package de.dante.extex.scanner;

import de.dante.util.GeneralException;


/**
 * This class provides a type-save enumeration of the category codes for
 * characters.
 * This is accomplished by the use of several static classes which are
 * derived from the common abstract superclass Catcode.
 * <p>
 * This class contains some inner classes representing the various incarnations
 * of a catcode.
 * Externally only some static constants for catcodes are accessible.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public abstract class Catcode {
    /** the constant for the escape catcode used to initiate control
     *  sequences
     */
    public static final Catcode ESCAPE = new CatcodeEscape();

    /** the constant for the left brace catcode */
    public static final Catcode LEFTBRACE = new CatcodeLeftBrace();

    /** the constant for the right brace catcode */
    public static final Catcode RIGHTBRACE = new CatcodeRigthBrace();

    /** the constant for the math shift catcode */
    public static final Catcode MATHSHIFT = new CatcodeMathShift();

    /** the constant for the tab mark catcode */
    public static final Catcode TABMARK = new CatcodeTabMark();

    /** the constant for the cr catcode */
    public static final Catcode CR = new CatcodeCr();

    /** the constant for the macro param catcode */
    public static final Catcode MACROPARAM = new CatcodeMacroParam();

    /** the constant for the super mark catcode */
    public static final Catcode SUPMARK = new CatcodeSupMark();

    /** the constant for the sub mark catcode */
    public static final Catcode SUBMARK = new CatcodeSubMark();

    /** the constant for the ignore catcode */
    public static final Catcode IGNORE = new CatcodeIgnore();

    /** the constant for the space catcode */
    public static final Catcode SPACE = new CatcodeSpace();

    /** the constant for the letter catcode */
    public static final Catcode LETTER = new CatcodeLetter();

    /** the constant for the other catcode */
    public static final Catcode OTHER = new CatcodeOther();

    /** the constant for the active catcode */
    public static final Catcode ACTIVE = new CatcodeActive();

    /** the constant for the comment catcode */
    public static final Catcode COMMENT = new CatcodeComment();

    /** the constant for the invalid catcode */
    public static final Catcode INVALID = new CatcodeInvalid();

    /** This is an array of catcodes where the integer catcode can be used as
     *  index.
     */
    private static final Catcode[] catcodes = {
                                                  ESCAPE,
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

    /** the string representation of the catcode */
    private String name;

    /** the integer representation of the catcode */
    private int code;

    /**
     * Creates a new object.
     * This constructor is private since only the static instances defined
     * above are allowed.
     *
     * @param code the numerical value
     * @param name the string representation
     */
    private Catcode(int code, String name) {
        super();
        this.code = code;
        this.name = name;
    }

    /**
     * Return a catcode for a given numerical value.
     *
     * @param code the numerical code.
     *
     * @return the catcode
     *
     * @throws CatcodeException in case that the catcode is not in the
     * allowed range
     */
    public static Catcode toCatcode(int code) throws CatcodeException {
        if (code < 0 || code >= catcodes.length) {
            throw new CatcodeException(Integer.toString(code));
        }

        return catcodes[code];
    }

    /**
     * Get the numerical representation for the Catcode.
     * Beware: this method should be used only as a last resort.
     *
     * @return the catcode as integer
     */
    public int getCode() {
        return code;
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
     * visiting catcodes.
     *
     * @param visitor the visitor
     * @param value the first argument to pass
     * @param value2 the second argument to pass
     *
     * @return some return value form the visit
     */
    public abstract Object visit(CatcodeVisitor visitor, Object value,
                                 Object value2) throws GeneralException;

    /**
     * This inner class represents an active catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeActive extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeActive() {
            super(13, "active");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For active characters the visitor method visitActive() is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object value,
                            Object value2) throws GeneralException {
            return visitor.visitActive(value, value2);
        }
    }

    /**
     * This inner class represents a comment catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeComment extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeComment() {
            super(14, "comment");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For comment starting characters the visitor method visitComment() 
         * is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitComment(arg1, arg2);
        }
    }

    /**
     * This inner class represents a CR catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeCr extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeCr() {
            super(5, "cr");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For CR characters the visitor method visitActive() is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitCr(arg1, arg2);
        }
    }

    /**
     * This inner class represents an escape catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeEscape extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeEscape() {
            super(0, "escape");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For escape characters the visitor method visitEscape() is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitEscape(arg1, arg2);
        }
    }

    /**
     * This inner class represents an ignore catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeIgnore extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeIgnore() {
            super(9, "ignore");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For ignored characters the visitor method visitIgnore() is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitIgnore(arg1, arg2);
        }
    }

    /**
     * This inner class represents an invalid character catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeInvalid extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeInvalid() {
            super(15, "invalid");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For invalid characters the visitor method visitInvalid() is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitInvalid(arg1, arg2);
        }
    }

    /**
     * This inner class represents a left brace character.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeLeftBrace extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeLeftBrace() {
            super(1, "leftbrace");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For left brace characters the visitor method visitLeftBrace() is 
         * invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitLeftBrace(arg1, arg2);
        }
    }

    /**
     * This inner class represents a letter character catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeLetter extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeLetter() {
            super(11, "letter");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For letter characters the visitor method visitLetter() is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitLetter(arg1, arg2);
        }
    }

    /**
     * This inner class represents a macro parameter character.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeMacroParam extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeMacroParam() {
            super(6, "macroparam");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For macro parameter characters the visitor method visitMacroParam()
         * is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitMacroParam(arg1, arg2);
        }
    }

    /**
     * This inner class represents a math shift character token.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeMathShift extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeMathShift() {
            super(3, "mathshift");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For math shift characters the visitor method visitMathShift()
         * is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitMathShift(arg1, arg2);
        }
    }

    /**
     * This inner class represents an other character catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeOther extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeOther() {
            super(12, "other");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For other characters the visitor method visitOther() is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitOther(arg1, arg2);
        }
    }

    /**
     * This inner class represents a right brace character catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeRigthBrace extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeRigthBrace() {
            super(2, "rightbrace");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For right brace characters the visitor method visitRightBrace() 
         * is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitRightBrace(arg1, arg2);
        }
    }

    /**
     * This inner class represents a space character catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeSpace extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeSpace() {
            super(10, "space");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For space characters the visitor method visitSpace() is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitSpace(arg1, arg2);
        }
    }

    /**
     * This inner class represents a sub mark character catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeSubMark extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeSubMark() {
            super(8, "submark");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For sub mark characters the visitor method visitSubMark() is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2) throws GeneralException {
            return visitor.visitSubMark(arg1, arg2);
        }
    }

    /**
     * This inner class represents a sup mark character catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeSupMark extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeSupMark() {
            super(7, "supmark");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For sup mark characters the visitor method visitSupMark() is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2)  throws GeneralException {
            return visitor.visitSupMark(arg1, arg2);
        }
    }

    /**
     * This inner class represents a tab mark character catcode.
     *
     * @author $Author: gene $
     * @version $Revision: 1.3 $
     */
    private static class CatcodeTabMark extends Catcode {
        /**
         * Creates a new object.
         */
        private CatcodeTabMark() {
            super(4, "tabmark");
        }

        /**
         * Catcodes support the visitor pattern. This method is the entry for
         * visiting catcodes.
         * For tab mark characters the visitor method visitTabMark() is invoked.
         *
         * @param visitor the visitor
         * @param value the first argument to pass
         * @param value2 the second argument to pass
         *
         * @return some return value form the visit
         */
        public Object visit(CatcodeVisitor visitor, Object arg1,
                            Object arg2)  throws GeneralException {
            return visitor.visitTabMark(arg1, arg2);
        }
    }
}
