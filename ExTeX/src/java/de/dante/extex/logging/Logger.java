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
package de.dante.extex.logging;

import java.util.logging.Level;

/**
 * This is a wrapper class for the logger.
 * Currently the Java 1.4 logger is wrapped.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Logger {
    /** the wrapped logger */
    private java.util.logging.Logger logger;

    /**
     * Creates a new object.
     *
     * @param name the name
     * @param resourceBundle the resource bundle
     */
    public Logger(String name, String resourceBundle) {
        super();
        logger = java.util.logging.Logger.getLogger(name, resourceBundle);
    }

    /**
     * Creates a new object.
     *
     * @param name the name
     */
    public Logger(String name) {
        super();
        logger = java.util.logging.Logger.getLogger(name);
    }

    /**
     * ...
     *
     * @param name ...
     * @return ...
     */
    public synchronized static Logger getLogger(String name) {
        return new Logger(name);
    }

    /**
     * ...
     *
     * @param arg0 ...
     */
    public void config(String arg0) {
        logger.config(arg0);
    }

    /**
     * ...
     *
     * @param arg0
     * @param arg1
     */
    public void entering(String arg0, String arg1) {
        logger.entering(arg0, arg1);
    }

    /**
     * ...
     *
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public void entering(String arg0, String arg1, Object arg2) {
        logger.entering(arg0, arg1, arg2);
    }

    /**
     * ...
     *
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public void entering(String arg0, String arg1, Object[] arg2) {
        logger.entering(arg0, arg1, arg2);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        return logger.equals(arg0);
    }

    /**
     * ...
     *
     * @param arg0
     * @param arg1
     */
    public void exiting(String arg0, String arg1) {
        logger.exiting(arg0, arg1);
    }

    /**
     * ...
     *
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public void exiting(String arg0, String arg1, Object arg2) {
        logger.exiting(arg0, arg1, arg2);
    }

    /**
     * ...
     *
     * @param arg0
     */
    public void fine(String arg0) {
        logger.fine(arg0);
    }

    /**
     * ...
     *
     * @param arg0
     */
    public void finer(String arg0) {
        logger.finer(arg0);
    }

    /**
     * ...
     *
     * @param arg0
     */
    public void finest(String arg0) {
        logger.finest(arg0);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return logger.hashCode();
    }

    /**
     * ...
     *
     * @param arg0
     */
    public void info(String arg0) {
        logger.info(arg0);
    }

    /**
     * ...
     *
     * @param arg0
     * @param arg1
     */
    public void log(Level arg0, String arg1) {
        logger.log(arg0, arg1);
    }

    /**
     * ...
     *
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public void log(Level arg0, String arg1, Object arg2) {
        logger.log(arg0, arg1, arg2);
    }

    /**
     * ...
     *
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public void log(Level arg0, String arg1, Object[] arg2) {
        logger.log(arg0, arg1, arg2);
    }

    /**
     * ...
     *
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public void log(Level arg0, String arg1, Throwable arg2) {
        logger.log(arg0, arg1, arg2);
    }

    /**
     * ...
     *
     * @param arg0
     */
    public void severe(String arg0) {
        logger.severe(arg0);
    }

    /**
     * ...
     *
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public void throwing(String arg0, String arg1, Throwable arg2) {
        logger.throwing(arg0, arg1, arg2);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return logger.toString();
    }

    /**
     * ...
     *
     * @param arg0
     */
    public void warning(String arg0) {
        logger.warning(arg0);
    }
}
