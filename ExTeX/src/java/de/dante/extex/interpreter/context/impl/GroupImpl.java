/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.font.type.other.NullFont;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextImpl;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.ImmutableCount;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.file.InFile;
import de.dante.extex.interpreter.type.file.OutFile;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;
import de.dante.util.observer.ObserverList;

/**
 * This is a simple implementation for a group. The whole stack of groups is
 * implemented as a linked list. The list itself is mixed within the pure
 * elements of the linked list.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.40 $
 */
public class GroupImpl implements Group, Tokenizer, Serializable {

    /**
     * The constant <tt>DELCODE_DEFAULT</tt> contains the default delcode.
     */
    private static final Count DELCODE_DEFAULT = new ImmutableCount(-1);

    /**
     * The constant <tt>INVALID_CHAR_CODE</tt> contains the code for an
     * invalid character.
     */
    private static final int INVALID_CHAR_CODE = 127;

    /**
     * The constant <tt>MATHCODE_DIGIT_OFFSET</tt> contains the offset for
     * non-letters when constructing a mathcode.
     */
    private static final int MATHCODE_DIGIT_OFFSET = 0x7000;

    /**
     * The constant <tt>MATHCODE_LETTER_OFFSET</tt> contains the offset for
     * letters when constructing a mathcode.
     */
    private static final int MATHCODE_LETTER_OFFSET = 0x7100;

    /**
     * The field <tt>SFCODE_DEFAULT</tt> contains the default sfcode for
     * non-letters.
     */
    private static final Count SFCODE_DEFAULT = new ImmutableCount(1000);

    /**
     * The field <tt>SFCODE_LETTER</tt> contains the default sfcode for letters.
     */
    private static final Count SFCODE_LETTER = new ImmutableCount(999);

    /**
     * The field <tt>afterGroup</tt> contains the tokens to be inserted after
     * the group has been closed.
     */
    private Tokens afterGroup = null;

    /**
     * The field <tt>afterGroupObservers</tt> contains the listr of observers
     * to be invoked after the group has been closed.
     */
    private transient ObserverList afterGroupObservers = new ObserverList();

    /**
     * The field <tt>boxMap</tt> contains the map for the boxes.
     */
    private Map boxMap = new HashMap();

    /**
     * The field <tt>catcodeMap</tt> contains the map for the catcodes.
     */
    private Map catcodeMap = new HashMap();

    /**
     * The field <tt>codeMap</tt> contains the map for the active characters and
     * macros. The key is a Token. The value is a Code.
     */
    private Map codeMap = new HashMap();

    /**
     * The field <tt>countMap</tt> contains the map for the count registers.
     */
    private Map countMap = new HashMap();

    /**
     * The field <tt>delcodeMap</tt> contains the map for the delcode of the
     * characters.
     */
    private Map delcodeMap = new HashMap();

    /**
     * The field <tt>dimenMap</tt> contains the map for the dimen registers.
     */
    private Map dimenMap = new HashMap();

    /**
     * The field <tt>fontMap</tt> contains the map for the fonts.
     */
    private Map fontMap = new HashMap();

    /**
     * The field <tt>ifMap</tt> contains the map for the booleans.
     */
    private Map ifMap = new HashMap();

    /**
     * The field <tt>inFileMap</tt> contains the map for the input files.
     */
    private Map inFileMap = new HashMap();

    /**
     * The field <tt>interaction</tt> contains the currently active
     * intercation mode.
     */
    private Interaction interaction = null;

    /**
     * The field <tt>lccodeMap</tt> contains the map for the translation to
     * lower case.
     */
    private Map lccodeMap = new HashMap();

    /**
     * The field <tt>mathcodeMap</tt> contains the map for the catcodes.
     */
    private Map mathcodeMap = new HashMap();

    /**
     * The field <tt>muskipMap</tt> contains the map for the muskip registers.
     */
    private Map muskipMap = new HashMap();

    /**
     * The field <tt>namespace</tt> contains the current namespace.
     */
    private String namespace = null;

    /**
     * The field <tt>next</tt> contains the next group in the linked list.
     */
    private Group next = null;

    /**
     * The field <tt>outFileMap</tt> contains the map for the output files.
     */
    private Map outFileMap = new HashMap();

    /**
     * The field <tt>sfcodeMap</tt> contains the map for the space factor.
     */
    private Map sfcodeMap = new HashMap();

    /**
     * The field <tt>skipMap</tt> contains the map for the skip registers
     */
    private Map skipMap = new HashMap();

    /**
     * The field <tt>standardTokenStream</tt> contains the standard token
     * stream.
     */
    private transient TokenStream standardTokenStream = null;

    /**
     * The field <tt>toksMap</tt> contains the map for the toks registers.
     */
    private Map toksMap = new HashMap();

    /**
     * The field <tt>typesettingContext</tt> contains the typesetting context
     * to be used.
     */
    private TypesettingContext typesettingContext = null;

    /**
     * The field <tt>uccodeMap</tt> contains the map for the translation to
     * upper case.
     */
    private Map uccodeMap = new HashMap();

    /**
     * Creates a new object.
     *
     * @param nextGroup the next group in the stack. If the value is
     *      <code>null</code> then this is the global base
     */
    public GroupImpl(final Group nextGroup) {

        super();
        this.next = nextGroup;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#afterGroup(
     *      de.dante.util.observer.Observer)
     */
    public void afterGroup(final Observer observer) {

        afterGroupObservers.add(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#afterGroup(
     *      de.dante.extex.scanner.Token)
     */
    public void afterGroup(final Token t) {

        if (afterGroup == null) {
            afterGroup = new Tokens();
        }

        afterGroup.add(t);
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getAfterGroup()
     */
    public Tokens getAfterGroup() {

        return afterGroup;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getBox(
     *      java.lang.String)
     */
    public Box getBox(final String name) {

        Box box = (Box) (boxMap.get(name));

        if (box == null && next != null) {
            box = next.getBox(name);
        }

        return box;
    }

    /**
     * @see de.dante.extex.interpreter.Tokenizer#getCatcode(
     *      de.dante.util.UnicodeChar)
     */
    public Catcode getCatcode(final UnicodeChar c) {

        Catcode value = (Catcode) catcodeMap.get(c);

        if (value != null) {
            return value;
        } else if (next != null) {
            return next.getCatcode(c);
        }

        // Fallback for predefined catcodes
        if (c.isLetter()) {
            return Catcode.LETTER;
        }

        switch (c.getCodePoint()) {
            case ' ':
                return Catcode.SPACE;
            case '\\':
                return Catcode.ESCAPE;
            case '\r':
            case '\n':
                return Catcode.CR;
            case '%':
                return Catcode.COMMENT;
            case 0:
                return Catcode.IGNORE;
            case INVALID_CHAR_CODE:
                return Catcode.INVALID;
            default:
                return Catcode.OTHER;
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getCode(
     *      CodeToken)
     */
    public Code getCode(final CodeToken token) {

        Code code = getCodeForToken(token);

        if (Namespace.SUPPORT_NAMESPACE_DEF && code == null
                && token instanceof CodeToken) {
            CodeToken t = (CodeToken) token.cloneInDefaultNamespace();
            if (t != token) {
                code = getCodeForToken(t);
            }
        }
        return code;
    }

    /**
     * Recurse down the group stack and search for the definition of a token.
     *
     * @param token the token to look-up the definmition for
     *
     * @return the code assigned to the token or <code>null</code> if none is
     *  found.
     */
    protected Code getCodeForToken(final CodeToken token) {

        Code code = (Code) (codeMap.get(token));
        return (code == null && next != null ? ((GroupImpl) next)
                .getCodeForToken(token) : code);
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getCount(
     *      java.lang.String)
     */
    public Count getCount(final String name) {

        Count count = (Count) (countMap.get(name));

        if (count == null) {
            if (next != null) {
                count = next.getCount(name);
            } else {
                count = new Count(0);
                countMap.put(name, count);
            }
        }

        return count;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getDelcode(
     *      de.dante.util.UnicodeChar)
     */
    public Count getDelcode(final UnicodeChar c) {

        Count delcode = (Count) (delcodeMap.get(c));

        if (delcode != null) {
            return delcode;
        } else if (next != null) {
            return next.getSfcode(c);
        }

        // Fallback for predefined delcodes
        return DELCODE_DEFAULT;

    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getDimen(
     *      java.lang.String)
     */
    public Dimen getDimen(final String name) {

        Dimen dimen = (Dimen) (dimenMap.get(name));

        if (dimen == null) {
            if (next != null) {
                dimen = next.getDimen(name);
            } else {
                dimen = new Dimen();
                dimenMap.put(name, dimen);
            }
        }

        return dimen;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getFont(
     *      java.lang.String)
     */
    public Font getFont(final String name) {

        Font font = (Font) (fontMap.get(name));

        if (font == null && next != null) {
            font = next.getFont(name);
        }

        return new NullFont(); //TODO gene: is a distinction of "not found" needed?
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getIf(
     *      java.lang.String)
     */
    public boolean getIf(final String name) {

        Boolean b = (Boolean) (ifMap.get(name));
        return b != null ? b.booleanValue() : next != null
                ? next.getIf(name)
                : false;
    }

    /**
     * Getter for a input file register.  In the case that the named
     * descriptor doe not exist yet a new one is returned. Especially if the
     * name is <code>null</code> then the default input stream is used.
     *
     * @param name the name or the number of the file register
     *
     * @return the input file descriptor
     *
     * @see de.dante.extex.interpreter.context.impl.Group#getInFile(
     *      java.lang.String)
     */
    public InFile getInFile(final String name) {

        if (name == null) {
            return new InFile(standardTokenStream);
        }

        InFile inFile = (InFile) (inFileMap.get(name));

        if (null != inFile) {
            return inFile;
        }

        return (next != null ? next.getInFile(name) : new InFile(
                standardTokenStream));
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getInteraction()
     */
    public Interaction getInteraction() {

        if (null != interaction) {
            return interaction;
        }

        return (next != null
                ? next.getInteraction()
                : Interaction.ERRORSTOPMODE);
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getLccode(
     *      de.dante.util.UnicodeChar)
     */
    public UnicodeChar getLccode(final UnicodeChar lc) {

        UnicodeChar value = (UnicodeChar) lccodeMap.get(lc);

        if (value != null) {
            return value;
        } else if (next != null) {
            return next.getLccode(lc);
        }

        // Fallback for predefined lccodes
        if (lc.isLetter()) {
            value = new UnicodeChar(lc.toLowerCase());
            // the value is stored to avoid constructing UnicodeChars again
            lccodeMap.put(lc, value);
            return value;
        }
        return UnicodeChar.NULL;
    }

    /**
     * Getter for the group level. The group level is the number of groups which
     * are currently open. Thus this number of groups can be closed. Since the
     * top-level group can not be closed this group counts as 0.
     *
     * @return the group level
     *
     * @see de.dante.extex.interpreter.context.impl.Group#getLevel()
     */
    public long getLevel() {

        return (next == null ? 0 : 1 + next.getLevel());
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getMathcode(
     *      de.dante.util.UnicodeChar)
     */
    public Count getMathcode(final UnicodeChar c) {

        Count mathcode = (Count) (mathcodeMap.get(c));

        if (mathcode == null) {
            if (next != null) {
                return next.getMathcode(c);
            } else if (c.isDigit()) {
                return new Count(c.getCodePoint() + MATHCODE_DIGIT_OFFSET);
            } else if (c.isLetter()) {
                return new Count(c.getCodePoint() + MATHCODE_LETTER_OFFSET);
            } else {
                return new Count(c.getCodePoint());
            }
        }

        return mathcode;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getMuskip(
     *      java.lang.String)
     */
    public Muskip getMuskip(final String name) {

        Muskip muskip = (Muskip) (muskipMap.get(name));
        return muskip != null ? muskip : next != null
                ? next.getMuskip(name)
                : new Muskip();
    }

    /**
     * Getter for the namespace.
     *
     * @return the namespace
     *
     * @see de.dante.extex.interpreter.context.impl.Group#getNamespace()
     */
    public String getNamespace() {

        if (namespace == null) {
            if (next == null) {
                namespace = Namespace.DEFAULT_NAMESPACE;
            } else {
                namespace = next.getNamespace();
            }
        }
        return this.namespace;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getNext()
     */
    public Group getNext() {

        return next;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getOutFile(
     *      java.lang.String)
     */
    public OutFile getOutFile(final String name) {

        OutFile file = (OutFile) (outFileMap.get(name));

        return file;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getSfcode(
     *      de.dante.util.UnicodeChar)
     */
    public Count getSfcode(final UnicodeChar c) {

        Count sfcode = (Count) (sfcodeMap.get(c));

        if (sfcode != null) {
            return sfcode;
        } else if (next != null) {
            return next.getSfcode(c);
        }

        // Fallback for predefined sfcodes
        if (c.isLetter()) {
            return SFCODE_LETTER;
        }
        return SFCODE_DEFAULT;

    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getSkip(
     *      java.lang.String)
     */
    public Glue getSkip(final String name) {

        Glue skip = (Glue) (skipMap.get(name));
        return skip != null ? skip : next != null
                ? next.getSkip(name)
                : new Glue(0);
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getToks(
     *      java.lang.String)
     */
    public Tokens getToks(final String name) {

        Tokens toks = (Tokens) (toksMap.get(name));
        return toks != null ? toks : next != null
                ? next.getToks(name)
                : new Tokens();
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getToksOrNull(java.lang.String)
     */
    public Tokens getToksOrNull(final String name) {

        Tokens toks = (Tokens) (toksMap.get(name));
        return toks != null ? toks : next != null ? next.getToks(name) : null;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getTypesettingContext()
     */
    public TypesettingContext getTypesettingContext() {

        TypesettingContext context = typesettingContext;
        return context != null //
                ? context //
                : next != null
                        ? next.getTypesettingContext()
                        : new TypesettingContextImpl();
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getUccode(
     *      de.dante.util.UnicodeChar)
     */
    public UnicodeChar getUccode(final UnicodeChar uc) {

        UnicodeChar value = (UnicodeChar) uccodeMap.get(uc);

        if (value != null) {
            return value;
        } else if (next != null) {
            return next.getLccode(uc);
        }

        // Fallback for predefined lccodes
        if (uc.isLetter()) {
            value = new UnicodeChar(uc.toUpperCase());
            // the value is stored to avoid constructing UnicodeChars again
            lccodeMap.put(uc, value);
            return value;
        }
        return UnicodeChar.NULL;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#runAfterGroup(
     *      de.dante.util.observer.Observable,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void runAfterGroup(final Observable observable,
            final Typesetter typesetter) throws InterpreterException {

        try {
            afterGroupObservers.update(observable, typesetter);
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setBox(
     *      java.lang.String, de.dante.extex.interpreter.type.box.Box, boolean)
     */
    public void setBox(final String name, final Box value, final boolean global) {

        boxMap.put(name, value);

        if (global && next != null) {
            next.setBox(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setCatcode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.extex.scanner.Catcode, boolean)
     */
    public void setCatcode(final UnicodeChar uc, final Catcode code,
            final boolean global) {

        catcodeMap.put(uc, code);

        if (global && next != null) {
            next.setCatcode(uc, code, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setCode(
     *      de.dante.extex.scanner.Token,
     *      de.dante.extex.interpreter.type.Code, boolean)
     */
    public void setCode(final Token token, final Code code, final boolean global) {

        codeMap.put(token, code);

        if (global && next != null) {
            next.setCode(token, code, global);
        }
    }

    /**
     * @throws ConfigurationInstantiationException
     * @see de.dante.extex.interpreter.context.impl.Group#setCount(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.count.Count, boolean)
     */
    public void setCount(final String name, final Count value,
            final boolean global) {

        countMap.put(name, value);

        if (global && next != null) {
            next.setCount(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setDelcode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.extex.interpreter.type.count.Count, boolean)
     */
    public void setDelcode(final UnicodeChar c, final Count code,
            final boolean global) {

        delcodeMap.put(c, code);

        if (global && next != null) {
            next.setDelcode(c, code, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setDimen(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.dimen.Dimen, boolean)
     */
    public void setDimen(final String name, final Dimen value,
            final boolean global) {

        dimenMap.put(name, value);

        if (global && next != null) {
            next.setDimen(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setFont(
     *      java.lang.String, de.dante.extex.interpreter.type.font.Font, boolean)
     */
    public void setFont(final String name, final Font font, final boolean global) {

        fontMap.put(name, font);

        if (global && next != null) {
            next.setFont(name, font, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setIf(
     *      java.lang.String,
     *      boolean, boolean)
     */
    public void setIf(final String name, final boolean value,
            final boolean global) {

        ifMap.put(name, (value ? Boolean.TRUE : Boolean.FALSE));

        if (global && next != null) {
            next.setIf(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setInFile(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.file.InFile, boolean)
     */
    public void setInFile(final String name, final InFile file,
            final boolean global) {

        inFileMap.put(name, file);

        if (global && next != null) {
            next.setInFile(name, file, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setInteraction(
     *      de.dante.extex.interpreter.Interaction,
     *      boolean)
     */
    public void setInteraction(final Interaction aInteraction,
            final boolean global) {

        this.interaction = aInteraction;

        if (global && next != null) {
            next.setInteraction(interaction, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setLccode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.UnicodeChar)
     */
    public void setLccode(final UnicodeChar lc, final UnicodeChar uc) {

        lccodeMap.put(lc, uc);
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setMathcode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.extex.interpreter.type.count.Count, boolean)
     */
    public void setMathcode(final UnicodeChar c, final Count code,
            final boolean global) {

        mathcodeMap.put(c, code);

        if (global && next != null) {
            next.setMathcode(c, code, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setMuskip(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.muskip.Muskip, boolean)
     */
    public void setMuskip(final String name, final Muskip value,
            final boolean global) {

        muskipMap.put(name, value);

        if (global && next != null) {
            next.setMuskip(name, value, global);
        }
    }

    /**
     * Setter for the namespace.
     *
     * @param theNamespace the new value for the namespace
     * @param global the scoping of the assignment
     *
     * @see de.dante.extex.interpreter.context.impl.Group#setNamespace(
     *      java.lang.String, boolean)
     */
    public void setNamespace(final String theNamespace, final boolean global) {

        this.namespace = theNamespace;
        if (global && next != null) {
            next.setNamespace(theNamespace, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setOutFile(
     *       java.lang.String,
     *       de.dante.extex.interpreter.type.file.OutFile, boolean)
     */
    public void setOutFile(final String name, final OutFile file,
            final boolean global) {

        outFileMap.put(name, file);

        if (global && next != null) {
            next.setOutFile(name, file, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setSfcode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.extex.interpreter.type.count.Count, boolean)
     */
    public void setSfcode(final UnicodeChar c, final Count code,
            final boolean global) {

        sfcodeMap.put(c, code);

        if (global && next != null) {
            next.setSfcode(c, code, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setSkip(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.glue.Glue, boolean)
     */
    public void setSkip(final String name, final Glue value,
            final boolean global) {

        skipMap.put(name, value);

        if (global && next != null) {
            next.setSkip(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setStandardTokenStream(
     *      de.dante.extex.scanner.stream.TokenStream)
     */
    public void setStandardTokenStream(final TokenStream standardTokenStream) {

        this.standardTokenStream = standardTokenStream;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setToks(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.tokens.Tokens, boolean)
     */
    public void setToks(final String name, final Tokens value,
            final boolean global) {

        toksMap.put(name, value);

        if (global && next != null) {
            next.setToks(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setTypesettingContext(
     *      de.dante.extex.interpreter.context.TypesettingContext)
     */
    public void setTypesettingContext(final TypesettingContext context) {

        typesettingContext = context;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setTypesettingContext(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      boolean)
     */
    public void setTypesettingContext(final TypesettingContext context,
            final boolean global) {

        typesettingContext = context;

        if (global && next != null) {
            next.setTypesettingContext(context, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setUccode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.UnicodeChar)
     */
    public void setUccode(final UnicodeChar uc, final UnicodeChar lc) {

        uccodeMap.put(uc, lc);
    }
}