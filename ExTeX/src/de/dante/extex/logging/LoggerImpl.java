/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * This is a wrapper class for the logger.
 * Currently the Java 1.4 logger is wrapped.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class LoggerImpl implements Logger {
    /** the wrapped logger */
    private java.util.logging.Logger logger;

    /**
     * Creates a new object.
     *
     * @param name the name
     * @param resourceBundle the resource bundle
     */
    private LoggerImpl(final String name, final String resourceBundle) {
        super();
        logger = java.util.logging.Logger.getLogger(name, resourceBundle);
    }

    /**
     * Creates a new object.
     *
     * @param name the name
     */
    private LoggerImpl(final String name) {
        super();
        logger = java.util.logging.Logger.getLogger(name);
    }

    /**
     * ...
     *
     * @param name ...
     * @param logfile ...
     * @param template ...
     *
     * @return ...
     */
    public synchronized static Logger getLogger(final String name,
        final File logfile, final String template) throws SecurityException,
        IOException {

        InputStream s = LoggerImpl.class.getClassLoader().getResourceAsStream(
            template);
        byte[] bb = new byte[8192];
        String cfg = "";
        int i;

        while ((i = s.read(bb)) >= 0) {
            cfg = cfg + new String(bb, 0, i);
        }

        String path = logfile.getPath()
            .replaceAll("[\\\\]", "\\\\\\\\\\\\\\\\");
        cfg = cfg.replaceAll("\\$JOBNAME", path);

        LogManager logManager = LogManager.getLogManager();
        logManager.reset();
        logManager.readConfiguration(new ByteArrayInputStream(cfg.getBytes()));

        return new LoggerImpl(name);
    }

    /**
     * ...
     *
     * @param config ...
     */
    public void config(final String config) {
        logger.config(config);
    }

    /**
     * ...
     *
     * @param arg
     * @param arg1
     */
    public void entering(final String arg, final String arg1) {
        logger.entering(arg, arg1);
    }

    /**
     * ...
     * 
     * @param arg
     * @param arg1
     * @param arg2
     */
    public void entering(final String arg, final String arg1, final Object arg2) {
        logger.entering(arg, arg1, arg2);
    }

    /**
     * ...
     * 
     * @param arg
     * @param arg1
     * @param arg2
     */
    public void entering(final String arg, final String arg1,
        final Object[] arg2) {
        logger.entering(arg, arg1, arg2);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object arg) {
        return logger.equals(arg);
    }

    /**
     * ...
     *
     * @param arg
     * @param arg1
     */
    public void exiting(final String arg, final String arg1) {
        logger.exiting(arg, arg1);
    }

    /**
     * ...
     * 
     * @param arg
     * @param arg1
     * @param arg2
     */
    public void exiting(final String arg, final String arg1, final Object arg2) {
        logger.exiting(arg, arg1, arg2);
    }

    /**
     * ...
     *
     * @param arg
     */
    public void fine(final String arg) {
        logger.fine(arg);
    }

    /**
     * ...
     *
     * @param arg
     */
    public void finer(final String arg) {
        logger.finer(arg);
    }

    /**
     * ...
     *
     * @param arg
     */
    public void finest(final String arg) {
        logger.finest(arg);
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
     * @param arg
     */
    public void info(final String arg) {
        logger.info(arg);
    }

    /**
     * ...
     *
     * @param arg
     * @param arg1
     */
    public void log(final Level arg, final String arg1) {
        logger.log(arg, arg1);
    }

    /**
     * ...
     *
     * @param arg
     * @param arg1
     * @param arg2
     */
    public void log(final Level arg, final String arg1, final Object arg2) {
        logger.log(arg, arg1, arg2);
    }

    /**
     * ...
     * 
     * @param arg
     * @param arg1
     * @param arg2
     */
    public void log(final Level arg, final String arg1, final Object[] arg2) {
        logger.log(arg, arg1, arg2);
    }

    /**
     * ...
     *
     * @param arg
     * @param arg1
     * @param arg2
     */
    public void log(final Level arg, final String arg1, final Throwable arg2) {
        logger.log(arg, arg1, arg2);
    }

    /**
     * ...
     *
     * @param arg
     */
    public void severe(final String arg) {
        logger.severe(arg);
    }

    /**
     * ...
     * 
     * @param arg
     * @param arg1
     * @param arg2
     */
    public void throwing(final String arg, final String arg1,
        final Throwable arg2) {
        logger.throwing(arg, arg1, arg2);
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
     * @param arg
     */
    public void warning(final String arg) {
        logger.warning(arg);
    }
}
