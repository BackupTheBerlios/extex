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

package de.dante.extex.interpreter;

import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class implements a set of flags.
 * This is needed to pass controlling information to primitives.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.12 $
 */
public class FlagsImpl implements Flags {

    /**
     * The field <tt>expandedP</tt> contains the expanded flag.
     */
    private boolean expandedP = false;

    /**
     * The field <tt>globalP</tt> contains the global flag.
     */
    private boolean globalP = false;

    /**
     * The field <tt>immediateP</tt> contains the immediate flag.
     */
    private boolean immediateP = false;

    /**
     * The field <tt>longP</tt> contains the long flag.
     */
    private boolean longP = false;

    /**
     * The field <tt>outerP</tt> contains the outer flag
     */
    private boolean outerP = false;

    /**
     * The field <tt>protectedP</tt> contains the protected flag.
     */
    private boolean protectedP = false;

    /**
     * Creates a new object.
     * Initially no flags are set.
     */
    public FlagsImpl() {

        super();
    }

    /**
     * This method clears all flags.
     */
    public void clear() {

        globalP = false;
        longP = false;
        outerP = false;
        expandedP = false;
        immediateP = false;
        protectedP = false;
    }

    /**
     * @see de.dante.extex.interpreter.Flags#clearExpanded()
     */
    public boolean clearExpanded() {

        boolean flag = expandedP;
        expandedP = false;
        return flag;
    }

    /**
     * @see de.dante.extex.interpreter.Flags#clearGlobal()
     */
    public boolean clearGlobal() {

        boolean flag = globalP;
        globalP = false;
        return flag;
    }

    /**
     * @see de.dante.extex.interpreter.Flags#clearImmediate()
     */
    public boolean clearImmediate() {

        boolean flag = immediateP;
        immediateP = false;
        return flag;
    }

    /**
     * @see de.dante.extex.interpreter.Flags#clearLong()
     */
    public boolean clearLong() {

        boolean flag = longP;
        longP = false;
        return flag;
    }

    /**
     * @see de.dante.extex.interpreter.Flags#clearOuter()
     */
    public boolean clearOuter() {

        boolean flag = outerP;
        outerP = false;
        return flag;
    }

    /**
     * @see de.dante.extex.interpreter.Flags#clearProtected()
     */
    public boolean clearProtected() {

        boolean flag = protectedP;
        protectedP = false;
        return flag;
    }

    /**
     * @see de.dante.extex.interpreter.Flags#copy()
     */
    public Flags copy() {

        Flags f = new FlagsImpl();
        if (expandedP) {
            f.setExpanded();
        }
        if (globalP) {
            f.setGlobal();
        }
        if (longP) {
            f.setLong();
        }
        if (outerP) {
            f.setOuter();
        }
        if (immediateP) {
            f.setImmediate();
        }
        if (protectedP) {
            f.setProtected();
        }
        return f;
    }

    /**
     * @see de.dante.extex.interpreter.Flags#get()
     */
    public String[] get() {

        Localizer localizer = LocalizerFactory.getLocalizer(FlagsImpl.class);
        String[] result = new String[((globalP ? 1 : 0) //
                + (longP ? 1 : 0) //
                + (outerP ? 1 : 0) //
                + (immediateP ? 1 : 0) //
                + (protectedP ? 1 : 0) //
        + (expandedP ? 1 : 0))];
        int i = 0;
        if (globalP) {
            result[i++] = localizer.format("global.text");
        }
        if (longP) {
            result[i++] = localizer.format("long.text");
        }
        if (outerP) {
            result[i++] = localizer.format("outer.text");
        }
        if (immediateP) {
            result[i++] = localizer.format("immediate.text");
        }
        if (protectedP) {
            result[i++] = localizer.format("protected.text");
        }
        if (expandedP) {
            result[i++] = localizer.format("expanded.text");
        }
        return result;
    }

    /**
     * @see de.dante.extex.interpreter.Flags#isDirty()
     */
    public boolean isDirty() {

        return globalP || longP || immediateP || outerP || protectedP
                || expandedP;
    }

    /**
     * Getter for the expanded flag.
     *
     * @return the current value of the expanded flag
     */
    public boolean isExpanded() {

        return expandedP;
    }

    /**
     * Getter for the global flag.
     *
     * @return the current value of the global flag
     */
    public boolean isGlobal() {

        return globalP;
    }

    /**
     * Getter for the immediate flag.
     *
     * @return the current value of the immediate flag
     */
    public boolean isImmediate() {

        return immediateP;
    }

    /**
     * Getter for the long flag.
     *
     * @return the current value of the long flag
     */
    public boolean isLong() {

        return longP;
    }

    /**
     * Getter for the outer flag.
     *
     * @return the current value of the outer flag
     */
    public boolean isOuter() {

        return outerP;
    }

    /**
     * @see de.dante.extex.interpreter.Flags#isProtected()
     */
    public boolean isProtected() {

        return protectedP;
    }

    /**
     * @see de.dante.extex.interpreter.Flags#set(
     *      de.dante.extex.interpreter.Flags)
     */
    public void set(final Flags flags) {

        globalP = flags.isGlobal();
        immediateP = flags.isImmediate();
        longP = flags.isLong();
        outerP = flags.isOuter();
        expandedP = flags.isExpanded();
        protectedP = flags.isProtected();
    }

    /**
     * Setter for the expanded flag.
     */
    public void setExpanded() {

        expandedP = true;
    }

    /**
     * Setter for the global flag.
     */
    public void setGlobal() {

        globalP = true;
    }

    /**
     * Setter for the global flag.
     *
     * @param value the new value for the global flag
     */
    public void setGlobal(final boolean value) {

        globalP = value;
    }

    /**
     * Setter for the immediate flag.
     */
    public void setImmediate() {

        immediateP = true;
    }

    /**
     * Setter for the long flag.
     */
    public void setLong() {

        longP = true;
    }

    /**
     * Setter for the outer flag.
     */
    public void setOuter() {

        outerP = true;
    }

    /**
     * @see de.dante.extex.interpreter.Flags#setProtected()
     */
    public void setProtected() {

        protectedP = true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append(globalP ? 'G' : '-');
        sb.append(longP ? 'L' : '-');
        sb.append(outerP ? 'O' : '-');
        sb.append(immediateP ? 'I' : '-');
        sb.append(protectedP ? 'P' : '-');
        sb.append(expandedP ? 'X' : '-');
        return sb.toString();
    }

    /**
     * @see de.dante.extex.interpreter.Flags#toText()
     */
    public String toText() {

        String[] s = get();
        return LocalizerFactory.getLocalizer(FlagsImpl.class).format(
                "text." + Integer.toString(s.length), s);
    }

}
