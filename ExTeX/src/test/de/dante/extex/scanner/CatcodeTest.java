/*
 * Copyright (C) 2004  Gerd Neugebauer
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
import junit.framework.TestCase;

/*
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class CatcodeTest extends TestCase implements CatcodeVisitor {

    /*
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(CatcodeTest.class);
    }

    /*
     */
    public void testCatcode0() throws Exception {
        assertEquals(Catcode.ESCAPE,Catcode.toCatcode(0));
        assertEquals(0,Catcode.ESCAPE.getCode());
        assertEquals("escape",Catcode.ESCAPE.getName());
        assertEquals("escape",Catcode.ESCAPE.toString());
    }
    
    /*
     */
    public void testCatcode1() throws Exception {
        assertEquals(Catcode.LEFTBRACE,Catcode.toCatcode(1));
        assertEquals(1,Catcode.LEFTBRACE.getCode());
        assertEquals("leftbrace",Catcode.LEFTBRACE.getName());
        assertEquals("leftbrace",Catcode.LEFTBRACE.toString());
    }

    /*
     */
    public void testCatcode2() throws Exception {
        assertEquals(Catcode.RIGHTBRACE,Catcode.toCatcode(2));
        assertEquals(2,Catcode.RIGHTBRACE.getCode());
        assertEquals("rightbrace",Catcode.RIGHTBRACE.getName());
        assertEquals("rightbrace",Catcode.RIGHTBRACE.toString());
    }

    /*
     */
    public void testCatcode3() throws Exception {
        assertEquals(Catcode.MATHSHIFT,Catcode.toCatcode(3));
        assertEquals(3,Catcode.MATHSHIFT.getCode());
        assertEquals("mathshift",Catcode.MATHSHIFT.getName());
        assertEquals("mathshift",Catcode.MATHSHIFT.toString());
    }

    /*
     */
    public void testCatcode4() throws Exception {
        assertEquals(Catcode.TABMARK,Catcode.toCatcode(4));
        assertEquals(4,Catcode.TABMARK.getCode());
        assertEquals("tabmark",Catcode.TABMARK.getName());
        assertEquals("tabmark",Catcode.TABMARK.toString());
    }

    /*
     */
    public void testCatcode5() throws Exception {
        assertEquals(Catcode.CR,Catcode.toCatcode(5));
        assertEquals(5,Catcode.CR.getCode());
        assertEquals("cr",Catcode.CR.getName());
        assertEquals("cr",Catcode.CR.toString());
    }

    /*
     */
    public void testCatcode6() throws Exception {
        assertEquals(Catcode.MACROPARAM,Catcode.toCatcode(6));
        assertEquals(6,Catcode.MACROPARAM.getCode());
        assertEquals("macroparam",Catcode.MACROPARAM.getName());
        assertEquals("macroparam",Catcode.MACROPARAM.toString());
    }

    /*
     */
    public void testCatcode7() throws Exception {
        assertEquals(Catcode.SUPMARK,Catcode.toCatcode(7));
        assertEquals(7,Catcode.SUPMARK.getCode());
        assertEquals("supmark",Catcode.SUPMARK.getName());
        assertEquals("supmark",Catcode.SUPMARK.toString());
    }

    /*
     */
    public void testCatcode8() throws Exception {
        assertEquals(Catcode.SUBMARK,Catcode.toCatcode(8));
        assertEquals(8,Catcode.SUBMARK.getCode());
        assertEquals("submark",Catcode.SUBMARK.getName());
        assertEquals("submark",Catcode.SUBMARK.toString());
    }

    /*
     */
    public void testCatcode9() throws Exception {
        assertEquals(Catcode.IGNORE,Catcode.toCatcode(9));
        assertEquals(9,Catcode.IGNORE.getCode());
        assertEquals("ignore",Catcode.IGNORE.getName());
        assertEquals("ignore",Catcode.IGNORE.toString());
    }

    /*
     */
    public void testCatcode10() throws Exception {
        assertEquals(Catcode.SPACE,Catcode.toCatcode(10));
        assertEquals(10,Catcode.SPACE.getCode());
        assertEquals("space",Catcode.SPACE.getName());
        assertEquals("space",Catcode.SPACE.toString());
    }

    /*
     */
    public void testCatcode11() throws Exception {
        assertEquals(Catcode.LETTER,Catcode.toCatcode(11));
        assertEquals(11,Catcode.LETTER.getCode());
        assertEquals("letter",Catcode.LETTER.getName());
        assertEquals("letter",Catcode.LETTER.toString());
    }

    /*
     */
    public void testCatcode12() throws Exception {
        assertEquals(Catcode.OTHER,Catcode.toCatcode(12));
        assertEquals(12,Catcode.OTHER.getCode());
        assertEquals("other",Catcode.OTHER.getName());
        assertEquals("other",Catcode.OTHER.toString());
    }

    /*
     */
    public void testCatcode13() throws Exception {
        assertEquals(Catcode.ACTIVE,Catcode.toCatcode(13));
        assertEquals(13,Catcode.ACTIVE.getCode());
        assertEquals("active",Catcode.ACTIVE.getName());
        assertEquals("active",Catcode.ACTIVE.toString());
    }

    /*
     */
    public void testCatcode14() throws Exception {
        assertEquals(Catcode.COMMENT,Catcode.toCatcode(14));
        assertEquals(14,Catcode.COMMENT.getCode());
        assertEquals("comment",Catcode.COMMENT.getName());
        assertEquals("comment",Catcode.COMMENT.toString());
    }

    /*
     */
    public void testCatcode15() throws Exception {
        assertEquals(Catcode.INVALID,Catcode.toCatcode(15));
        assertEquals(15,Catcode.INVALID.getCode());
        assertEquals("invalid",Catcode.INVALID.getName());
        assertEquals("invalid",Catcode.INVALID.toString());
    }

    /*
     */
    public void testToCatcodeFail1() throws Exception {
        try {
            Catcode cc = Catcode.toCatcode(-1);
            assertFalse("Test succeeded unexpectedly",true);
        } catch (CatcodeException e) {
            assertTrue(true);
        }
    }

    /*
     */
    public void testToCatcodeFail2() throws Exception {
        try {
            Catcode cc = Catcode.toCatcode(16);
            assertFalse("Test succeeded unexpectedly",true);
        } catch (CatcodeException e) {
            assertTrue(true);
        }
    }

    private static int visited = -1;

    /*
     */
    public void testVisit0() throws Exception {
        assertEquals("esc",Catcode.ESCAPE.visit(this,"1","2"));
        assertEquals(0,visited);
    }

    /*
     */
    public void testVisit1() throws Exception {
        assertEquals("{",Catcode.LEFTBRACE.visit(this,"1","2"));
        assertEquals(1,visited);
    }

    /*
     */
    public void testVisit2() throws Exception {
        assertEquals("}",Catcode.RIGHTBRACE.visit(this,"1","2"));
        assertEquals(2,visited);
    }

    /*
     */
    public void testVisit3() throws Exception {
        assertEquals("$",Catcode.MATHSHIFT.visit(this,"1","2"));
        assertEquals(3,visited);
    }

    /*
     */
    public void testVisit4() throws Exception {
        assertEquals("&",Catcode.TABMARK.visit(this,"1","2"));
        assertEquals(4,visited);
    }

    /*
     */
    public void testVisit5() throws Exception {
        assertEquals("cr",Catcode.CR.visit(this,"1","2"));
        assertEquals(5,visited);
    }

    /*
     */
    public void testVisit6() throws Exception {
        assertEquals("#",Catcode.MACROPARAM.visit(this,"1","2"));
        assertEquals(6,visited);
    }

    /*
     */
    public void testVisit7() throws Exception {
        assertEquals("^",Catcode.SUPMARK.visit(this,"1","2"));
        assertEquals(7,visited);
    }

    /*
     */
    public void testVisit8() throws Exception {
        assertEquals("_",Catcode.SUBMARK.visit(this,"1","2"));
        assertEquals(8,visited);
    }

    /*
     */
    public void testVisit9() throws Exception {
        assertEquals("ignore",Catcode.IGNORE.visit(this,"1","2"));
        assertEquals(9,visited);
    }

    /*
     */
    public void testVisit10() throws Exception {
        assertEquals(" ",Catcode.SPACE.visit(this,"1","2"));
        assertEquals(10,visited);
    }

    /*
     */
    public void testVisit11() throws Exception {
        assertEquals("letter",Catcode.LETTER.visit(this,"1","2"));
        assertEquals(11,visited);
    }

    /*
     */
    public void testVisit12() throws Exception {
        assertEquals(".",Catcode.OTHER.visit(this,"1","2"));
        assertEquals(12,visited);
    }

    /*
     */
    public void testVisit13() throws Exception {
        assertEquals("active",Catcode.ACTIVE.visit(this,"1","2"));
        assertEquals(13,visited);
    }

    /*
     */
    public void testVisit14() throws Exception {
        assertEquals("%",Catcode.COMMENT.visit(this,"1","2"));
        assertEquals(14,visited);
    }

    /*
     */
    public void testVisit15() throws Exception {
        assertEquals("invalid",Catcode.INVALID.visit(this,"1","2"));
        assertEquals(15,visited);
    }


    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitActive(java.lang.Object, java.lang.Object)
     */
    public Object visitActive(Object arg1, Object arg2) throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 13;
        return "active";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitComment(java.lang.Object, java.lang.Object)
     */
    public Object visitComment(Object arg1, Object arg2)
            throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 14;
        return "%";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitCr(java.lang.Object, java.lang.Object)
     */
    public Object visitCr(Object arg1, Object arg2) throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 5;
        return "cr";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitEscape(java.lang.Object, java.lang.Object)
     */
    public Object visitEscape(Object arg1, Object arg2) throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 0;
        return "esc";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitIgnore(java.lang.Object, java.lang.Object)
     */
    public Object visitIgnore(Object arg1, Object arg2) throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 9;
        return "ignore";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitInvalid(java.lang.Object, java.lang.Object)
     */
    public Object visitInvalid(Object arg1, Object arg2)
            throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 15;
        return "invalid";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(java.lang.Object, java.lang.Object)
     */
    public Object visitLeftBrace(Object arg1, Object arg2)
            throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 1;
        return "{";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(java.lang.Object, java.lang.Object)
     */
    public Object visitLetter(Object arg1, Object arg2) throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 11;
        return "letter";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(java.lang.Object, java.lang.Object)
     */
    public Object visitMacroParam(Object arg1, Object arg2)
            throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 6;
        return "#";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(java.lang.Object, java.lang.Object)
     */
    public Object visitMathShift(Object arg1, Object arg2)
            throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 3;
        return "$";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(java.lang.Object, java.lang.Object)
     */
    public Object visitOther(Object arg1, Object arg2) throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 12;
        return ".";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitRightBrace(java.lang.Object, java.lang.Object)
     */
    public Object visitRightBrace(Object arg1, Object arg2)
            throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 2;
        return "}";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(java.lang.Object, java.lang.Object)
     */
    public Object visitSpace(Object arg1, Object arg2) throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 10;
        return " ";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(java.lang.Object, java.lang.Object)
     */
    public Object visitSubMark(Object arg1, Object arg2)
            throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 8;
        return "_";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(java.lang.Object, java.lang.Object)
     */
    public Object visitSupMark(Object arg1, Object arg2)
            throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 7;
        return "^";
    }
    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(java.lang.Object, java.lang.Object)
     */
    public Object visitTabMark(Object arg1, Object arg2)
            throws GeneralException {
        assertEquals("1",arg1);
        assertEquals("2",arg2);
        visited = 4;
        return "&";
    }
}
