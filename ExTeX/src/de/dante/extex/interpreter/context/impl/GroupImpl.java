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
package de.dante.extex.interpreter.context.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextImpl;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.Muskip;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;

/**
 * This is a simple implementation for a group. The whole stack of group is implemented as
 * a linked list. The list itself is mixed with the pure elements of the linked list.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class GroupImpl implements Tokenizer, Group, Serializable {

    /** The next group in the linked list */
    private Group next = null;

    /** ... */
    private Interaction interaction = null;

    /** The map for the active characters */
    private Map activeMap = new HashMap();

    /** The map for the catcodes */
    private Map catcodeMap = new HashMap();

    /** The map for the count registers */
    private Map countMap = new HashMap();

    /** The map for the dimen registers */
    private Map dimenMap = new HashMap();

    /** The map for the fonts */
    private Map fontMap = new HashMap();

    /** The map for the boolean */
    private Map ifMap = new HashMap();

    /** The map for the macros */
    private Map macroMap = new HashMap();

    /** The map for the muskip registers */
    private Map muskipMap = new HashMap();

    /** The map for the skip registers */
    private Map skipMap = new HashMap();

    /** The map for the toks registers */
    private Map toksMap = new HashMap();

    /** ... */
    private Tokens afterGroup = null;

    /** ... */
    private TypesettingContext typesettingContext;

    /**
     * Creates a new object.
     *
     * @param next the next group in the stack. If the value is <code>null</code> then
     * this is the global base
     */
    public GroupImpl(Group next) {
        super();
        this.next = next;
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setActive(java.lang.String, de.dante.extex.interpreter.Code)
     */
    public void setActive(String name, Code code) {
        activeMap.put(name, code);
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setActive(java.lang.String, de.dante.extex.interpreter.Code, boolean)
     */
    public void setActive(String name, Code code, boolean global) {
        activeMap.put(name, code);

        if (global && next != null) {
            next.setActive(name, code, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#getActive(java.lang.String)
     */
    public Code getActive(String name) {
        Code code = (Code) (activeMap.get(name));
        return code != null ? code
               : next != null ? next.getActive(name) : null;
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#getAfterGroup()
     */
    public Tokens getAfterGroup() {
        return afterGroup;
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setCatcode(char,de.dante.extex.scanner.Catcode)
     */
    public void setCatcode(char c, Catcode code) {
        catcodeMap.put(new Character(c), code);
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setCatcode(java.lang.String, de.dante.extex.scanner.Catcode, boolean)
     */
    public void setCatcode(String name, Catcode value, boolean global) {
        catcodeMap.put(name, value);

        if (global && next != null) {
            next.setCatcode(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#getCatcode(char)
     * @author Modifications by MGN
     */
    public Catcode getCatcode(char c) {
        Catcode value = (Catcode) catcodeMap.get(new Character(c));

        if (value != null) {
            return value;
        }

        if (next != null) {
            return next.getCatcode(c);
        }

        // Fallback for predefined catcodes
        if (Character.isLetter(c)) {
            return Catcode.LETTER;
        }

        switch (c) {
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
        case 127:
            return Catcode.INVALID;
        }

        return Catcode.OTHER;
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setCount(java.lang.String, de.dante.extex.interpreter.type.Dimen)
     */
    public void setCount(String name, Count value) {
        countMap.put(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setCount(java.lang.String, de.dante.extex.interpreter.type.Count, boolean)
     */
    public void setCount(String name, Count value, boolean global) {
        countMap.put(name, value);

        if (global && next != null) {
            next.setCount(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#getCount(java.lang.String)
     */
    public Count getCount(String name) {
        Count count = (Count) (countMap.get(name));

        if (count == null) {
            if (next == null) {
                count = next.getCount(name);
            } else {
                count = new Count(name);
                setCount(name, count);
            }
        }

        return count;
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setDimen(java.lang.String, de.dante.extex.interpreter.type.Dimen)
     */
    public void setDimen(String name, Dimen value) {
        dimenMap.put(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setDimen(java.lang.String, de.dante.extex.interpreter.type.Dimen, boolean)
     */
    public void setDimen(String name, Dimen value, boolean global) {
        dimenMap.put(name, value);

        if (global && next != null) {
            next.setDimen(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#getDimen(java.lang.String)
     */
    public Dimen getDimen(String name) {
        Dimen dimen = (Dimen) (dimenMap.get(name));

        if (dimen == null) {
            if (next == null) {
                dimen = next.getDimen(name);
            } else {
                dimen = new Dimen();
                setDimen(name, dimen);
            }
        }

        return dimen;
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setIf(java.lang.String, boolean)
     */
    public void setIf(String name, boolean value) {
        ifMap.put(name, (value ? Boolean.TRUE : Boolean.FALSE));
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setIf(java.lang.String, boolean, boolean)
     */
    public void setIf(String name, boolean value, boolean global) {
        ifMap.put(name, (value ? Boolean.TRUE : Boolean.FALSE));

        if (global && next != null) {
            next.setIf(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#getIf(java.lang.String)
     */
    public boolean getIf(String name) {
        Boolean b = (Boolean) (ifMap.get(name));
        return b != null ? b.booleanValue()
               : next != null ? next.getIf(name) : false;
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setInteraction(de.dante.extex.interpreter.Interaction)
     */
    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setInteraction(de.dante.extex.interpreter.Interaction, boolean)
     */
    public void setInteraction(Interaction interaction, boolean global) {
        this.interaction = interaction;

        if (global && next != null) {
            next.setInteraction(interaction, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#getInteraction()
     */
    public Interaction getInteraction() {
        return interaction;
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setMacro(java.lang.String, de.dante.extex.interpreter.Code)
     */
    public void setMacro(String name, Code code) {
        macroMap.put(name, code);
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setMacro(java.lang.String, de.dante.extex.interpreter.Code, boolean)
     */
    public void setMacro(String name, Code value, boolean global) {
        catcodeMap.put(name, value);

        if (global && next != null) {
            next.setMacro(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#getMacro(java.lang.String)
     */
    public Code getMacro(String name) {
        Code value = (Code) macroMap.get(name);
        return (value == null && next != null ? next.getMacro(name)
                : value);
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setMuskip(java.lang.String, de.dante.extex.interpreter.type.Muskip)
     */
    public void setMuskip(String name, Muskip value) {
        muskipMap.put(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setMuskip(java.lang.String, de.dante.extex.interpreter.type.Muskip, boolean)
     */
    public void setMuskip(String name, Muskip value, boolean global) {
        muskipMap.put(name, value);

        if (global && next != null) {
            next.setMuskip(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#getMuskip(java.lang.String)
     */
    public Muskip getMuskip(String name) {
        Muskip muskip = (Muskip) (muskipMap.get(name));
        return muskip != null ? muskip
               : next != null ? next.getMuskip(name) : new Muskip();
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#getNext()
     */
    public Group getNext() {
        return next;
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setSkip(java.lang.String, de.dante.extex.interpreter.type.Glue)
     */
    public void setSkip(String name, Glue value) {
        skipMap.put(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setSkip(java.lang.String, de.dante.extex.interpreter.type.Glue, boolean)
     */
    public void setSkip(String name, Glue value, boolean global) {
        skipMap.put(name, value);

        if (global && next != null) {
            next.setSkip(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#getSkip(java.lang.String)
     */
    public Glue getSkip(String name) {
        Glue skip = (Glue) (skipMap.get(name));
        return skip != null ? skip
               : next != null ? next.getSkip(name) : new Glue(0);
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setToks(java.lang.String, de.dante.extex.interpreter.type.Tokens)
     */
    public void setToks(String name, Tokens value) {
        toksMap.put(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#setToks(java.lang.String, de.dante.extex.interpreter.type.Tokens, boolean)
     */
    public void setToks(String name, Tokens value, boolean global) {
        toksMap.put(name, value);

        if (global && next != null) {
            next.setToks(name, value, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#getToks(java.lang.String)
     */
    public Tokens getToks(String name) {
        Tokens toks = (Tokens) (toksMap.get(name));
        return toks != null ? toks
               : next != null ? next.getToks(name) : new Tokens();
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setTypesettingContext(de.dante.extex.interpreter.context.TypesettingContext, boolean)
     */
    public void setTypesettingContext(TypesettingContext context,
                                      boolean global) {
        typesettingContext = context;

        if (global && next != null) {
            next.setTypesettingContext(context, global);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#setTypesettingContext(de.dante.extex.interpreter.context.TypesettingContext)
     */
    public void setTypesettingContext(TypesettingContext context) {
        typesettingContext = context;
    }

    /**
     * @see de.dante.extex.interpreter.context.impl.Group#getTypesettingContext()
     */
    public TypesettingContext getTypesettingContext() {
        TypesettingContext context = typesettingContext;
        return context != null ? context
               : next != null ? next.getTypesettingContext()
                 : new TypesettingContextImpl();
    }

    /**
     * @see de.dante.extex.interpreter.context.Group#afterGroup(de.dante.extex.scanner.Token)
     */
    public void afterGroup(Token t) {
        if (afterGroup == null) {
            afterGroup = new Tokens();
        }

        afterGroup.add(t);
    }
}
