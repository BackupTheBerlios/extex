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
package de.dante.extex.logging;

import java.util.logging.Level;

/*
 * This logger logs nothing at all. It just provides a null object for the
 * logging.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de"> Gerd Neugebauer </a>
 * 
 * @version $Revision: 1.1 $
 */
public class NullLogger implements Logger {

    /**
     * Creates a new object.
     * 
     * 
     */
    public NullLogger() {
        super();
    }

    /**
     * @see de.dante.extex.logging.Logger#config(java.lang.String)
     */
    public void config(String arg0) {
    }

    /**
     * @see de.dante.extex.logging.Logger#entering(java.lang.String, java.lang.String)
     */
    public void entering(String arg0, String arg1) {
    }

    /**
     * @see de.dante.extex.logging.Logger#entering(java.lang.String, java.lang.String, java.lang.Object)
     */
    public void entering(String arg0, String arg1, Object arg2) {
    }

    /**
     * @see de.dante.extex.logging.Logger#entering(java.lang.String, java.lang.String, java.lang.Object[])
     */
    public void entering(String arg0, String arg1, Object[] arg2) {
    }

    /**
     * @see de.dante.extex.logging.Logger#exiting(java.lang.String, java.lang.String)
     */
    public void exiting(String arg0, String arg1) {
    }

    /**
     * @see de.dante.extex.logging.Logger#exiting(java.lang.String, java.lang.String, java.lang.Object)
     */
    public void exiting(String arg0, String arg1, Object arg2) {
    }

    /**
     * @see de.dante.extex.logging.Logger#fine(java.lang.String)
     */
    public void fine(String arg0) {
    }

    /**
     * @see de.dante.extex.logging.Logger#finer(java.lang.String)
     */
    public void finer(String arg0) {
    }

    /**
     * @see de.dante.extex.logging.Logger#finest(java.lang.String)
     */
    public void finest(String arg0) {
    }

    /**
     * @see de.dante.extex.logging.Logger#info(java.lang.String)
     */
    public void info(String arg0) {
    }

    /**
     * @see de.dante.extex.logging.Logger#log(java.util.logging.Level, java.lang.String)
     */
    public void log(Level arg0, String arg1) {
    }

    /**
     * @see de.dante.extex.logging.Logger#log(java.util.logging.Level, java.lang.String, java.lang.Object)
     */
    public void log(Level arg0, String arg1, Object arg2) {
    }

    /**
     * @see de.dante.extex.logging.Logger#log(java.util.logging.Level, java.lang.String, java.lang.Object[])
     */
    public void log(Level arg0, String arg1, Object[] arg2) {
    }

    /**
     * @see de.dante.extex.logging.Logger#log(java.util.logging.Level, java.lang.String, java.lang.Throwable)
     */
    public void log(Level arg0, String arg1, Throwable arg2) {
    }

    /**
     * @see de.dante.extex.logging.Logger#severe(java.lang.String)
     */
    public void severe(String arg0) {
    }

    /**
     * @see de.dante.extex.logging.Logger#throwing(java.lang.String, java.lang.String, java.lang.Throwable)
     */
    public void throwing(String arg0, String arg1, Throwable arg2) {
    }

    /**
     * @see de.dante.extex.logging.Logger#warning(java.lang.String)
     */
    public void warning(String arg0) {
    }

}
